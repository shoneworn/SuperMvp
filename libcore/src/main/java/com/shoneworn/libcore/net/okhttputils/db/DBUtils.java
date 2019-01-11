package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shoneworn.libcore.net.okhttputils.utils.OkLogger;

public class DBUtils {
    public DBUtils() {
    }

    public static boolean isNeedUpgradeTable(SQLiteDatabase db, TableEntity table) {
        if (!isTableExists(db, table.tableName)) {
            return true;
        } else {
            Cursor cursor = db.rawQuery("select * from " + table.tableName, (String[]) null);
            if (cursor == null) {
                return false;
            } else {
                boolean var9;
                try {
                    int columnCount = table.getColumnCount();
                    if (columnCount != cursor.getColumnCount()) {
                        var9 = true;
                        return var9;
                    }

                    for (int i = 0; i < columnCount; ++i) {
                        if (table.getColumnIndex(cursor.getColumnName(i)) == -1) {
                            boolean var5 = true;
                            return var5;
                        }
                    }

                    var9 = false;
                } finally {
                    cursor.close();
                }

                return var9;
            }
        }
    }

    public static boolean isTableExists(SQLiteDatabase db, String tableName) {
        if (tableName != null && db != null && db.isOpen()) {
            Cursor cursor = null;
            int count = 0;

            try {
                String dbSql = "SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?";
                cursor = db.rawQuery(dbSql, new String[]{"table", tableName});
                if (!cursor.moveToFirst()) {
                    boolean var4 = false;
                    return var4;
                }

                count = cursor.getInt(0);
            } catch (Exception var8) {
                OkLogger.printStackTrace(var8);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }

            return count > 0;
        } else {
            return false;
        }
    }

    public static boolean isFieldExists(SQLiteDatabase db, String tableName, String fieldName) {
        if (tableName != null && db != null && fieldName != null && db.isOpen()) {
            Cursor cursor = null;

            boolean var5;
            try {
                cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0", (String[]) null);
                boolean var4 = cursor != null && cursor.getColumnIndex(fieldName) != -1;
                return var4;
            } catch (Exception var9) {
                OkLogger.printStackTrace(var9);
                var5 = false;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }

            return var5;
        } else {
            return false;
        }
    }
}
