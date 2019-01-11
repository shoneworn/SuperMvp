package com.shoneworn.libcore.net.okhttputils.utils;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import android.os.StatFs;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    public IOUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (Exception var2) {
                OkLogger.printStackTrace(var2);
            }

        }
    }

    public static void flushQuietly(Flushable flushable) {
        if(flushable != null) {
            try {
                flushable.flush();
            } catch (Exception var2) {
                OkLogger.printStackTrace(var2);
            }

        }
    }

    public static InputStream toInputStream(CharSequence input) {
        return new ByteArrayInputStream(input.toString().getBytes());
    }

    public static InputStream toInputStream(CharSequence input, String encoding) throws UnsupportedEncodingException {
        byte[] bytes = input.toString().getBytes(encoding);
        return new ByteArrayInputStream(bytes);
    }

    public static BufferedInputStream toBufferedInputStream(InputStream inputStream) {
        return inputStream instanceof BufferedInputStream?(BufferedInputStream)inputStream:new BufferedInputStream(inputStream);
    }

    public static BufferedOutputStream toBufferedOutputStream(OutputStream outputStream) {
        return outputStream instanceof BufferedOutputStream?(BufferedOutputStream)outputStream:new BufferedOutputStream(outputStream);
    }

    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader?(BufferedReader)reader:new BufferedReader(reader);
    }

    public static BufferedWriter toBufferedWriter(Writer writer) {
        return writer instanceof BufferedWriter?(BufferedWriter)writer:new BufferedWriter(writer);
    }

    public static String toString(InputStream input) throws IOException {
        return new String(toByteArray(input));
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return new String(toByteArray(input), encoding);
    }

    public static String toString(Reader input) throws IOException {
        return new String(toByteArray(input));
    }

    public static String toString(Reader input, String encoding) throws IOException {
        return new String(toByteArray(input), encoding);
    }

    public static String toString(byte[] byteArray) {
        return new String(byteArray);
    }

    public static String toString(byte[] byteArray, String encoding) {
        try {
            return new String(byteArray, encoding);
        } catch (UnsupportedEncodingException var3) {
            return new String(byteArray);
        }
    }

    public static byte[] toByteArray(Object input) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(input);
            oos.flush();
            byte[] var3 = baos.toByteArray();
            return var3;
        } catch (IOException var7) {
            OkLogger.printStackTrace(var7);
        } finally {
            closeQuietly(oos);
            closeQuietly(baos);
        }

        return null;
    }

    public static Object toObject(byte[] input) {
        if(input == null) {
            return null;
        } else {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;

            try {
                bais = new ByteArrayInputStream(input);
                ois = new ObjectInputStream(bais);
                Object var3 = ois.readObject();
                return var3;
            } catch (Exception var7) {
                OkLogger.printStackTrace(var7);
            } finally {
                closeQuietly(ois);
                closeQuietly(bais);
            }

            return null;
        }
    }

    public static byte[] toByteArray(CharSequence input) {
        return input == null?new byte[0]:input.toString().getBytes();
    }

    public static byte[] toByteArray(CharSequence input, String encoding) throws UnsupportedEncodingException {
        return input == null?new byte[0]:input.toString().getBytes(encoding);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write((InputStream)input, (OutputStream)output);
        output.close();
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write((Reader)input, (OutputStream)output);
        output.close();
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        write((Reader)input, (OutputStream)output, encoding);
        output.close();
        return output.toByteArray();
    }

    public static char[] toCharArray(CharSequence input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write((CharSequence)input, (Writer)output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write((InputStream)input, (Writer)output);
        return output.toCharArray();
    }

    public static char[] toCharArray(InputStream input, String encoding) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write((InputStream)input, (Writer)output, encoding);
        return output.toCharArray();
    }

    public static char[] toCharArray(Reader input) throws IOException {
        CharArrayWriter output = new CharArrayWriter();
        write((Reader)input, (Writer)output);
        return output.toCharArray();
    }

    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        Reader reader = new InputStreamReader(input, encoding);
        return readLines((Reader)reader);
    }

    public static List<String> readLines(InputStream input) throws IOException {
        Reader reader = new InputStreamReader(input);
        return readLines((Reader)reader);
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = toBufferedReader(input);
        List<String> list = new ArrayList();

        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            list.add(line);
        }

        return list;
    }

    public static void write(byte[] data, OutputStream output) throws IOException {
        if(data != null) {
            output.write(data);
        }

    }

    public static void write(byte[] data, Writer output) throws IOException {
        if(data != null) {
            output.write(new String(data));
        }

    }

    public static void write(byte[] data, Writer output, String encoding) throws IOException {
        if(data != null) {
            output.write(new String(data, encoding));
        }

    }

    public static void write(char[] data, Writer output) throws IOException {
        if(data != null) {
            output.write(data);
        }

    }

    public static void write(char[] data, OutputStream output) throws IOException {
        if(data != null) {
            output.write((new String(data)).getBytes());
        }

    }

    public static void write(char[] data, OutputStream output, String encoding) throws IOException {
        if(data != null) {
            output.write((new String(data)).getBytes(encoding));
        }

    }

    public static void write(CharSequence data, Writer output) throws IOException {
        if(data != null) {
            output.write(data.toString());
        }

    }

    public static void write(CharSequence data, OutputStream output) throws IOException {
        if(data != null) {
            output.write(data.toString().getBytes());
        }

    }

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        if(data != null) {
            output.write(data.toString().getBytes(encoding));
        }

    }

    public static void write(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[4096];

        int len;
        while((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }

    }

    public static void write(Reader input, OutputStream output) throws IOException {
        Writer out = new OutputStreamWriter(output);
        write((Reader)input, (Writer)out);
        out.flush();
    }

    public static void write(InputStream input, Writer output) throws IOException {
        Reader in = new InputStreamReader(input);
        write((Reader)in, (Writer)output);
    }

    public static void write(Reader input, OutputStream output, String encoding) throws IOException {
        Writer out = new OutputStreamWriter(output, encoding);
        write((Reader)input, (Writer)out);
        out.flush();
    }

    public static void write(InputStream input, OutputStream output, String encoding) throws IOException {
        Reader in = new InputStreamReader(input, encoding);
        write((Reader)in, (OutputStream)output);
    }

    public static void write(InputStream input, Writer output, String encoding) throws IOException {
        Reader in = new InputStreamReader(input, encoding);
        write((Reader)in, (Writer)output);
    }

    public static void write(Reader input, Writer output) throws IOException {
        char[] buffer = new char[4096];

        int len;
        while(-1 != (len = input.read(buffer))) {
            output.write(buffer, 0, len);
        }

    }

    public static boolean contentEquals(InputStream inputStream1, InputStream inputStream2) throws IOException {
        InputStream input1 = toBufferedInputStream(inputStream1);
        InputStream input2 = toBufferedInputStream(inputStream2);

        int ch2;
        for(int ch = input1.read(); -1 != ch; ch = input1.read()) {
            ch2 = input2.read();
            if(ch != ch2) {
                return false;
            }
        }

        ch2 = input2.read();
        return ch2 == -1;
    }

    public static boolean contentEquals(Reader inputStream1, Reader inputStream2) throws IOException {
        Reader input1 = toBufferedReader(inputStream1);
        Reader input2 = toBufferedReader(inputStream2);

        int ch2;
        for(int ch = input1.read(); -1 != ch; ch = input1.read()) {
            ch2 = input2.read();
            if(ch != ch2) {
                return false;
            }
        }

        ch2 = input2.read();
        return ch2 == -1;
    }

    public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
        BufferedReader br1 = toBufferedReader(input1);
        BufferedReader br2 = toBufferedReader(input2);
        String line1 = br1.readLine();

        String line2;
        for(line2 = br2.readLine(); line1 != null && line2 != null && line1.equals(line2); line2 = br2.readLine()) {
            line1 = br1.readLine();
        }

        return line1 != null && (line2 == null || line1.equals(line2));
    }

    public static long getDirSize(String path) {
        StatFs stat;
        try {
            stat = new StatFs(path);
        } catch (Exception var3) {
            OkLogger.printStackTrace(var3);
            return 0L;
        }

        return VERSION.SDK_INT >= 18?getStatFsSize(stat, "getBlockSizeLong", "getAvailableBlocksLong"):getStatFsSize(stat, "getBlockSize", "getAvailableBlocks");
    }

    private static long getStatFsSize(StatFs statFs, String blockSizeMethod, String availableBlocksMethod) {
        try {
            Method getBlockSizeMethod = statFs.getClass().getMethod(blockSizeMethod, new Class[0]);
            getBlockSizeMethod.setAccessible(true);
            Method getAvailableBlocksMethod = statFs.getClass().getMethod(availableBlocksMethod, new Class[0]);
            getAvailableBlocksMethod.setAccessible(true);
            long blockSize = ((Long)getBlockSizeMethod.invoke(statFs, new Object[0])).longValue();
            long availableBlocks = ((Long)getAvailableBlocksMethod.invoke(statFs, new Object[0])).longValue();
            return blockSize * availableBlocks;
        } catch (Throwable var9) {
            OkLogger.printStackTrace(var9);
            return 0L;
        }
    }

    public static boolean canWrite(String path) {
        return (new File(path)).canWrite();
    }

    public static boolean canRead(String path) {
        return (new File(path)).canRead();
    }

    public static boolean createFolder(String folderPath) {
        if(!TextUtils.isEmpty(folderPath)) {
            File folder = new File(folderPath);
            return createFolder(folder);
        } else {
            return false;
        }
    }

    public static boolean createFolder(File targetFolder) {
        if(targetFolder.exists()) {
            if(targetFolder.isDirectory()) {
                return true;
            }

            targetFolder.delete();
        }

        return targetFolder.mkdirs();
    }

    public static boolean createNewFolder(String folderPath) {
        return delFileOrFolder(folderPath) && createFolder(folderPath);
    }

    public static boolean createNewFolder(File targetFolder) {
        return delFileOrFolder(targetFolder) && createFolder(targetFolder);
    }

    public static boolean createFile(String filePath) {
        if(!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            return createFile(file);
        } else {
            return false;
        }
    }

    public static boolean createFile(File targetFile) {
        if(targetFile.exists()) {
            if(targetFile.isFile()) {
                return true;
            }

            delFileOrFolder(targetFile);
        }

        try {
            return targetFile.createNewFile();
        } catch (IOException var2) {
            return false;
        }
    }

    public static boolean createNewFile(String filePath) {
        if(!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            return createNewFile(file);
        } else {
            return false;
        }
    }

    public static boolean createNewFile(File targetFile) {
        if(targetFile.exists()) {
            delFileOrFolder(targetFile);
        }

        try {
            return targetFile.createNewFile();
        } catch (IOException var2) {
            return false;
        }
    }

    public static boolean delFileOrFolder(String path) {
        return TextUtils.isEmpty(path)?false:delFileOrFolder(new File(path));
    }

    public static boolean delFileOrFolder(File file) {
        if(file != null && file.exists()) {
            if(file.isFile()) {
                file.delete();
            } else if(file.isDirectory()) {
                File[] files = file.listFiles();
                if(files != null) {
                    File[] var2 = files;
                    int var3 = files.length;

                    for(int var4 = 0; var4 < var3; ++var4) {
                        File sonFile = var2[var4];
                        delFileOrFolder(sonFile);
                    }
                }

                file.delete();
            }
        }

        return true;
    }
}
