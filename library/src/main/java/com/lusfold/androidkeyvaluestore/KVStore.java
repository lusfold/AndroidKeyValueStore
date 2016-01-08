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
package com.lusfold.androidkeyvaluestore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lusfold.androidkeyvaluestore.core.KVManagerImpl;
import com.lusfold.androidkeyvaluestore.core.KVManger;

public class KVStore {
    public static final String TAG = "KVStore";
    private static KVManger instance;

    /**
     * Init component.
     *
     * @param context      used to open or create the database
     * @param databaseName database name for opening or creating
     */
    public static KVManger init(Context context, String databaseName) {
        SQLiteDatabase database = context.openOrCreateDatabase(databaseName, Context.MODE_PRIVATE, null);
        return init(database);
    }

    /**
     * Init component.
     *
     * @param database
     * @return KVManager instance.
     */
    public static KVManger init(SQLiteDatabase database) {
        if (instance == null) {
            synchronized (KVManger.class) {
                if (instance == null) {
                    instance = new KVManagerImpl(database);
                }
            }
        }
        return instance;
    }

    /**
     * Get KVManager instance.
     *
     * @return KVManager instance.
     */
    public static KVManger getInstance() {
        return instance;
    }

    /**
     * Must be called when this app is finishing.
     */
    public static void destroy() {
        if (instance != null) {
            instance.getDatabase().close();
        }
    }
}
