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
package com.lusfold.androidkeyvaluestore.utils;


public class StringUtils {

    /**
     * @param str
     * @return if str is null return true,else return false.
     */
    public static boolean isNull(String str) {
        return str == null ? true : false;
    }

    /**
     * @param str
     * @return if str is not null return ture,else return false.
     */
    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    /**
     * @param str
     * @return if str is null or equals "" return true,else return false.
     */
    public static boolean isBlank(String str) {
        if (isNull(str))
            return true;
        if (str.equals(""))
            return true;
        return false;
    }

    /**
     * @param str
     * @return if str is not null and do not equals "" return true,else return false.
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
