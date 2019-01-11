package com.shoneworn.libcore.net.okhttputils.cookie;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Cookie.Builder;
import okhttp3.internal.Util;
import okhttp3.internal.publicsuffix.PublicSuffixDatabase;

public class CookieParseUtil {
    private static final Pattern YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
    private static final Pattern MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
    private static final Pattern DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
    private static final boolean IS_HOST_MATCH_DOMAIN = false;

    public CookieParseUtil() {
    }

    public static List<Cookie> parseAll(HttpUrl url, Headers headers) {
        List cookieStrings = headers.values("Set-Cookie");
        ArrayList cookies = null;
        int i = 0;

        for(int size = cookieStrings.size(); i < size; ++i) {
            Cookie cookie = parse(System.currentTimeMillis(), url, (String)cookieStrings.get(i));
            if(cookie != null) {
                if(cookies == null) {
                    cookies = new ArrayList();
                }

                cookies.add(cookie);
            }
        }

        return cookies != null?Collections.unmodifiableList(cookies):Collections.emptyList();
    }

    public static Cookie parse(long currentTimeMillis, HttpUrl url, String setCookie) {
        byte pos = 0;
        int limit = setCookie.length();
        int cookiePairEnd = Util.delimiterOffset(setCookie, pos, limit, ';');
        int pairEqualsSign = Util.delimiterOffset(setCookie, pos, cookiePairEnd, '=');
        if(pairEqualsSign == cookiePairEnd) {
            return null;
        } else {
            String cookieName = Util.trimSubstring(setCookie, pos, pairEqualsSign);
            if(!cookieName.isEmpty() && Util.indexOfControlOrNonAscii(cookieName) == -1) {
                String cookieValue = Util.trimSubstring(setCookie, pairEqualsSign + 1, cookiePairEnd);
                if(Util.indexOfControlOrNonAscii(cookieValue) != -1) {
                    return null;
                } else {
                    long expiresAt = 253402300799999L;
                    long deltaSeconds = -1L;
                    String domain = null;
                    String path = null;
                    boolean secureOnly = false;
                    boolean httpOnly = false;
                    boolean hostOnly = true;
                    boolean persistent = false;

                    int urlHost;
                    for(int pos1 = cookiePairEnd + 1; pos1 < limit; pos1 = urlHost + 1) {
                        urlHost = Util.delimiterOffset(setCookie, pos1, limit, ';');
                        int encodedPath = Util.delimiterOffset(setCookie, pos1, urlHost, '=');
                        String lastSlash = Util.trimSubstring(setCookie, pos1, encodedPath);
                        String attributeValue = encodedPath < urlHost?Util.trimSubstring(setCookie, encodedPath + 1, urlHost):"";
                        if(lastSlash.equalsIgnoreCase("expires")) {
                            try {
                                expiresAt = parseExpires(attributeValue, 0, attributeValue.length());
                                persistent = true;
                            } catch (IllegalArgumentException var28) {
                                ;
                            }
                        } else if(lastSlash.equalsIgnoreCase("max-age")) {
                            try {
                                deltaSeconds = parseMaxAge(attributeValue);
                                persistent = true;
                            } catch (NumberFormatException var27) {
                                ;
                            }
                        } else if(lastSlash.equalsIgnoreCase("domain")) {
                            try {
                                domain = parseDomain(attributeValue);
                                hostOnly = false;
                            } catch (IllegalArgumentException var26) {
                                ;
                            }
                        } else if(lastSlash.equalsIgnoreCase("path")) {
                            path = attributeValue;
                        } else if(lastSlash.equalsIgnoreCase("secure")) {
                            secureOnly = true;
                        } else if(lastSlash.equalsIgnoreCase("httponly")) {
                            httpOnly = true;
                        }
                    }

                    if(deltaSeconds == -9223372036854775808L) {
                        expiresAt = -9223372036854775808L;
                    } else if(deltaSeconds != -1L) {
                        long urlHost1 = deltaSeconds <= 9223372036854775L?deltaSeconds * 1000L:9223372036854775807L;
                        expiresAt = currentTimeMillis + urlHost1;
                        if(expiresAt < currentTimeMillis || expiresAt > 253402300799999L) {
                            expiresAt = 253402300799999L;
                        }
                    }

                    String urlHost2 = url.host();
                    if(domain == null) {
                        domain = urlHost2;
                    }

                    if(urlHost2.length() != domain.length() && PublicSuffixDatabase.get().getEffectiveTldPlusOne(domain) == null) {
                        return null;
                    } else {
                        if(path == null || !path.startsWith("/")) {
                            String encodedPath1 = url.encodedPath();
                            int lastSlash1 = encodedPath1.lastIndexOf(47);
                            path = lastSlash1 != 0?encodedPath1.substring(0, lastSlash1):"/";
                        }

                        return (new Builder()).name(cookieName).value(cookieValue).expiresAt(expiresAt).domain(domain).path(path).build();
                    }
                }
            } else {
                return null;
            }
        }
    }

    private static boolean domainMatch(String urlHost, String domain) {
        return urlHost.equals(domain)?true:urlHost.endsWith(domain) && urlHost.charAt(urlHost.length() - domain.length() - 1) == 46 && !Util.verifyAsIpAddress(urlHost);
    }

    private static boolean pathMatch(HttpUrl url, String path) {
        String urlPath = url.encodedPath();
        if(urlPath.equals(path)) {
            return true;
        } else {
            if(urlPath.startsWith(path)) {
                if(path.endsWith("/")) {
                    return true;
                }

                if(urlPath.charAt(path.length()) == 47) {
                    return true;
                }
            }

            return false;
        }
    }

    private static long parseExpires(String s, int pos, int limit) {
        pos = dateCharacterOffset(s, pos, limit, false);
        int hour = -1;
        int minute = -1;
        int second = -1;
        int dayOfMonth = -1;
        int month = -1;
        int year = -1;

        int calendar;
        for(Matcher matcher = TIME_PATTERN.matcher(s); pos < limit; pos = dateCharacterOffset(s, calendar + 1, limit, false)) {
            calendar = dateCharacterOffset(s, pos + 1, limit, true);
            matcher.region(pos, calendar);
            if(hour == -1 && matcher.usePattern(TIME_PATTERN).matches()) {
                hour = Integer.parseInt(matcher.group(1));
                minute = Integer.parseInt(matcher.group(2));
                second = Integer.parseInt(matcher.group(3));
            } else if(dayOfMonth == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
                dayOfMonth = Integer.parseInt(matcher.group(1));
            } else if(month == -1 && matcher.usePattern(MONTH_PATTERN).matches()) {
                String monthString = matcher.group(1).toLowerCase(Locale.US);
                month = MONTH_PATTERN.pattern().indexOf(monthString) / 4;
            } else if(year == -1 && matcher.usePattern(YEAR_PATTERN).matches()) {
                year = Integer.parseInt(matcher.group(1));
            }
        }

        if(year >= 70 && year <= 99) {
            year += 1900;
        }

        if(year >= 0 && year <= 69) {
            year += 2000;
        }

        if(year < 1601) {
            throw new IllegalArgumentException();
        } else if(month == -1) {
            throw new IllegalArgumentException();
        } else if(dayOfMonth >= 1 && dayOfMonth <= 31) {
            if(hour >= 0 && hour <= 23) {
                if(minute >= 0 && minute <= 59) {
                    if(second >= 0 && second <= 59) {
                        GregorianCalendar calendar1 = new GregorianCalendar(Util.UTC);
                        calendar1.setLenient(false);
                        calendar1.set(Calendar.YEAR, year);
                        calendar1.set(Calendar.MONTH, month - 1);
                        calendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar1.set(Calendar.HOUR, hour);
                        calendar1.set(Calendar.MINUTE, minute);
                        calendar1.set(Calendar.SECOND, second);
                        calendar1.set(Calendar.MILLISECOND, 0);
                        return calendar1.getTimeInMillis();
                    } else {
                        throw new IllegalArgumentException();
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static int dateCharacterOffset(String input, int pos, int limit, boolean invert) {
        for(int i = pos; i < limit; ++i) {
            char c = input.charAt(i);
            boolean dateCharacter = c < 32 && c != 9 || c >= 127 || c >= 48 && c <= 57 || c >= 97 && c <= 122 || c >= 65 && c <= 90 || c == 58;
            if(dateCharacter == !invert) {
                return i;
            }
        }

        return limit;
    }

    private static long parseMaxAge(String s) {
        try {
            long e = Long.parseLong(s);
            return e <= 0L?-9223372036854775808L:e;
        } catch (NumberFormatException var3) {
            if(s.matches("-?\\d+")) {
                return s.startsWith("-")?-9223372036854775808L:9223372036854775807L;
            } else {
                throw var3;
            }
        }
    }

    private static String parseDomain(String s) {
        if(s.endsWith(".")) {
            throw new IllegalArgumentException();
        } else {
            if(s.startsWith(".")) {
                s = s.substring(1);
            }

            String canonicalDomain = Util.toHumanReadableAscii(s);
            if(canonicalDomain == null) {
                throw new IllegalArgumentException();
            } else {
                return canonicalDomain;
            }
        }
    }
}
