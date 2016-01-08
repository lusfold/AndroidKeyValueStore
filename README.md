AndroidKeyValueStore
======

A KV store base on sqlite for Android Application. Inspire by [YTKKeyValueStore][1].

Usage
--------

Download [the latest JAR][0] or grab via Maven:
```xml
<dependency>
  <groupId>com.lusfold.androidkevaluestore</groupId>
  <artifactId>androidkevaluestore</artifactId>
  <version>0.1.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.lusfold.androidkeyvaluesotre:androidkevaluestore:0.1.0'
```

Init before use:  

```java  
KVStore.init(Context context,String databaseName)  
```  

or With a Database:  

```java
KVStore.init(SqliteDatabase database)
```  

Destroy when app is down:  
```java  
KVStore.destroy()  
```  

Then you can use like this:
```java
KVStore.getInstance().setDebug(true);  
KVStore.getInstance().clearTable();  
KVStore.getInstance().insert("a", "a");  
KVStore.getInstance().delete("a");  
KVStore.getInstance().insertOrUpdate("a", "b");  
KVStore.getInstance().get("a");  
KVStore.getInstance().getByPrefix("a");  
KVStore.getInstance().getByContains("a");  
```  

More details please check source code.

License
-------

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


 [0]: https://bintray.com/lusfold/maven/AndroidKeyValueStore/_latestVersion
 [1]: https://github.com/yuantiku/YTKKeyValueStore
