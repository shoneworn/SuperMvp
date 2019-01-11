package com.shoneworn.libcore.net.okhttputils.db;

/**
 * Created by chenxiangxiang on 2019/1/10.
 */


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableEntity {
    public String tableName;
    private List<ColumnEntity> list;

    public TableEntity(String tableName) {
        this.tableName = tableName;
        this.list = new ArrayList();
    }

    public TableEntity addColumn(ColumnEntity columnEntity) {
        this.list.add(columnEntity);
        return this;
    }

    public String buildTableString() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(this.tableName).append('(');
        Iterator var2 = this.list.iterator();

        while(true) {
            while(var2.hasNext()) {
                ColumnEntity entity = (ColumnEntity)var2.next();
                if(entity.compositePrimaryKey != null) {
                    sb.append("PRIMARY KEY (");
                    String[] var4 = entity.compositePrimaryKey;
                    int var5 = var4.length;

                    for(int var6 = 0; var6 < var5; ++var6) {
                        String primaryKey = var4[var6];
                        sb.append(primaryKey).append(",");
                    }

                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(")");
                } else {
                    sb.append(entity.columnName).append(" ").append(entity.columnType);
                    if(entity.isNotNull) {
                        sb.append(" NOT NULL");
                    }

                    if(entity.isPrimary) {
                        sb.append(" PRIMARY KEY");
                    }

                    if(entity.isAutoincrement) {
                        sb.append(" AUTOINCREMENT");
                    }

                    sb.append(",");
                }
            }

            if(sb.toString().endsWith(",")) {
                sb.deleteCharAt(sb.length() - 1);
            }

            sb.append(')');
            return sb.toString();
        }
    }

    public String getColumnName(int columnIndex) {
        return ((ColumnEntity)this.list.get(columnIndex)).columnName;
    }

    public int getColumnCount() {
        return this.list.size();
    }

    public int getColumnIndex(String columnName) {
        int columnCount = this.getColumnCount();

        for(int i = 0; i < columnCount; ++i) {
            if(((ColumnEntity)this.list.get(i)).columnName.equals(columnName)) {
                return i;
            }
        }

        return -1;
    }
}
