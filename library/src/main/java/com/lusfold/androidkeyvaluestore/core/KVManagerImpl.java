/*
 * Copyright (C) 2016 Lusfold
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lusfold.androidkeyvaluestore.core;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lusfold.androidkeyvaluestore.exception.KVStoreKeyNullException;
import com.lusfold.androidkeyvaluestore.exception.KVStoreValueNullException;
import com.lusfold.androidkeyvaluestore.utils.CursorUtils;
import com.lusfold.androidkeyvaluestore.utils.LogUtils;
import com.lusfold.androidkeyvaluestore.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class KVManagerImpl implements KVManger {
    private static final String TAG = "KVManagerImpl";
    //key column name
    public static final String COLUMN_KEY = "Key";
    //value column name
    public static final String COLUMN_VALUE = "Value";
    //table name
    public static final String TABLE_NAME = "KVStore";
    //table sql
    private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ? (? TEXT PRIMARY KEY NOT NULL,? TEXT NOT NULL)";
    private static final String SQL_QUERY_TABLE = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name= '?'";
    private static final String SQL_DELETE_TABLE = "DELETE FROM ?";
    //data sql
    private static final String SQL_QUERY_DATA = "SELECT * FROM ? WHERE ? = '?'";
    private static final String SQL_QUERY_DATA_PREFIX = "SELECT * FROM ? WHERE ? LIKE '?%'";
    private static final String SQL_QUERY_DATA_CONTAINS = "SELECT * FROM ? WHERE ? LIKE '%?%'";

    private SQLiteDatabase database;

    public KVManagerImpl(SQLiteDatabase database) {
        this.database = database;
        if (!isTableExist()) {
            createKVTable();
        }
    }

    /**
     * create KVStore table if not exists
     */
    private void createKVTable() {
        execNonQuery(SQL_CREATE_TABLE, new String[]{TABLE_NAME, COLUMN_KEY, COLUMN_VALUE});
    }

    /**
     * delete KVStore table
     */
    private void deleteKVTable() {
        execNonQuery(SQL_DELETE_TABLE, new String[]{TABLE_NAME});
    }

    /**
     * @return if KVStore table exists return true else return false
     */
    private boolean isTableExist() {
        Cursor cursor = execQuery(SQL_QUERY_TABLE, new String[]{TABLE_NAME});
        if (cursor != null) {
            if (cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Execute query sql with selectionArgs,will replace ?s with selectionArgs.
     *
     * @param sql
     * @param selectionArgs
     * @return The result.
     */
    public Cursor execQuery(String sql, String[] selectionArgs) {
        for (int i = 0, count = selectionArgs.length; i < count; i++)
            sql = sql.replaceFirst("[?]", selectionArgs[i]);
        LogUtils.d(sql);
        return database.rawQuery(sql, null);
    }

    /**
     * Execute non query sql with selectionArgs,will replace ?s with selectionArgs.
     *
     * @param sql
     * @param selectionArgs
     */
    public void execNonQuery(String sql, String[] selectionArgs) {
        for (int i = 0, count = selectionArgs.length; i < count; i++)
            sql = sql.replaceFirst("[?]", selectionArgs[i]);
        LogUtils.d(sql);
        database.execSQL(sql);
    }

    /**
     * Check whether the key exists.
     *
     * @param key
     * @return If the key exists return true,else return false.
     */
    @Override
    public boolean keyExists(String key) {
        if (StringUtils.isNull(key))
            throw new KVStoreKeyNullException();
        Cursor cursor = execQuery(SQL_QUERY_DATA, new String[]{TABLE_NAME, COLUMN_KEY, key});
        if (cursor != null) {
            try {
                if (cursor.moveToNext())
                    return true;
            } finally {
                CursorUtils.closeCursorQuietly(cursor);
            }
        }
        return false;
    }

    private void checkKey(String key) {
        if (StringUtils.isNull(key))
            throw new KVStoreKeyNullException();
    }

    private void checkValue(String value) {
        if (StringUtils.isNull(value))
            throw new KVStoreValueNullException();
    }

    private long doInsert(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_KEY, key);
        contentValues.put(COLUMN_VALUE, value);
        return database.insert(TABLE_NAME, null, contentValues);
    }

    private int doUpdate(String key, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_VALUE, value);
        return database.update(TABLE_NAME, contentValues, COLUMN_KEY + " = ?", new String[]{key});
    }


    /**
     * Insert data to KVStore table.
     *
     * @param key
     * @param value
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    @Override
    public long insert(String key, String value) {
        checkKey(key);
        checkValue(value);
        if (keyExists(key))
            return -1;
        return doInsert(key, value);
    }

    /**
     * Insert data to KVStore table,if the key exists will update the key value.
     *
     * @param key
     * @param value
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    @Override
    public long insertOrUpdate(String key, String value) {
        checkKey(key);
        checkValue(value);
        if (keyExists(key))
            return doUpdate(key, value);
        return doInsert(key, value);
    }

    /**
     * Update data.
     *
     * @param key
     * @param value
     * @return the number of rows affected.
     */
    @Override
    public int update(String key, String value) {
        checkKey(key);
        checkValue(value);
        if (!keyExists(key))
            return -1;
        return doUpdate(key, value);
    }

    /**
     * Delete data.
     *
     * @param key
     * @return the number of rows affected.
     */
    @Override
    public int delete(String key) {
        checkKey(key);
        return database.delete(TABLE_NAME, COLUMN_KEY + " = ?", new String[]{key});
    }


    /**
     * Query value according the key.
     *
     * @param key
     * @return result.
     */
    @Override
    public String get(String key) {
        checkKey(key);
        Cursor cursor = execQuery(SQL_QUERY_DATA, new String[]{TABLE_NAME, COLUMN_KEY, key});
        String result = null;
        if (cursor != null) {
            if (cursor.moveToNext()) {
                result = cursor.getString(1);
            }
            CursorUtils.closeCursorQuietly(cursor);
        }
        return result;
    }

    /**
     * Query values according the key prefix string.
     *
     * @param keyPrefix
     * @return result.
     */
    @Override
    public Map<String, String> getByPrefix(String keyPrefix) {
        checkKey(keyPrefix);
        Cursor cursor = execQuery(SQL_QUERY_DATA_PREFIX, new String[]{TABLE_NAME, COLUMN_KEY, keyPrefix});
        Map<String, String> result = null;
        if (cursor != null) {
            result = new HashMap<>();
            while (cursor.moveToNext()) {
                result.put(cursor.getString(0), cursor.getString(1));
            }
            CursorUtils.closeCursorQuietly(cursor);
        }
        return result;
    }

    /**
     * Query values according the key contains string.
     *
     * @param keyContains
     * @return result.
     */
    @Override
    public Map<String, String> getByContains(String keyContains) {
        checkKey(keyContains);
        Cursor cursor = execQuery(SQL_QUERY_DATA_CONTAINS, new String[]{TABLE_NAME, COLUMN_KEY, keyContains});
        Map<String, String> result = null;
        if (cursor != null) {
            result = new HashMap<>();
            while (cursor.moveToNext()) {
                result.put(cursor.getString(0), cursor.getString(1));
            }
            CursorUtils.closeCursorQuietly(cursor);
        }
        return result;
    }


    /**
     * Clear the KVStore table.
     */
    @Override
    public void clearTable() {
        deleteKVTable();
        createKVTable();
    }

    /**
     * Get database.
     *
     * @return database.
     */
    @Override
    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * @param debug Set debug flag.
     */
    @Override
    public void setDebug(boolean debug) {
        LogUtils.debug = debug;
    }
}
