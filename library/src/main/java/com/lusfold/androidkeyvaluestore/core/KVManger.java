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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

public interface KVManger {

    /**
     * Execute query sql with selectionArgs,will replace ?s with selectionArgs.
     *
     * @param sql
     * @param selectionArgs
     * @return The result.
     */
    public abstract Cursor execQuery(String sql, String[] selectionArgs);

    /**
     * Execute non query sql with selectionArgs,will replace ?s with selectionArgs.
     *
     * @param sql
     * @param selectionArgs
     */
    public abstract void execNonQuery(String sql, String[] selectionArgs);

    /**
     * Check whether the key exists.
     *
     * @param key
     * @return If the key exists return true,else return false.
     */
    public abstract boolean keyExists(String key);

    /**
     * Insert data to KVStore table.
     *
     * @param key
     * @param value
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public abstract long insert(String key, String value);

    /**
     * Insert data to KVStore table,if the key exists will update the key value.
     *
     * @param key
     * @param value
     * @return the row ID of the newly inserted row, or -1 if an error occurred.
     */
    public abstract long insertOrUpdate(String key, String value);


    /**
     * Update data.
     *
     * @param key
     * @param value
     * @return the number of rows affected.
     */
    public abstract int update(String key, String value);

    /**
     * Delete data.
     *
     * @param key
     * @return the number of rows affected.
     */
    public abstract int delete(String key);


    /**
     * Query value according the key.
     *
     * @param key
     * @return result.
     */
    public abstract String get(String key);

    /**
     * Query values according the key prefix string.
     *
     * @param keyPrefix
     * @return result.
     */
    public abstract Map<String, String> getByPrefix(String keyPrefix);

    /**
     * Query values according the key contains string.
     *
     * @param keyContains
     * @return result.
     */
    public abstract Map<String, String> getByContains(String keyContains);

    /**
     * Clear the KVStore table.
     */
    public abstract void clearTable();

    /**
     * Get database.
     *
     * @return database.
     */
    public abstract SQLiteDatabase getDatabase();

    /**
     * @param debug Set debug flag.
     */
    public abstract void setDebug(boolean debug);
}
