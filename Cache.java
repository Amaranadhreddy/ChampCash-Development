package com.tms.govt.champcash.home.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

/**
 * Created by govt on 18-04-2017.
 */

public class Cache {
    
    public static final int CACHE_LOACATION_MEMORY = 0;
    public static final int CACHE_LOCATION_DISK = 1;
    private static final String CACHE_PREFERENCE = "application_cache";
    public static HashMap<String, Object> mMemoryCache = new HashMap<String, Object>();


    public static void putData(String key, Context context, Object dataToCache, int cacheLocation) {
        
        switch (cacheLocation){
            
            case CACHE_LOACATION_MEMORY:
                saveDataToDisk(key, context, dataToCache);
                break;
            
            case CACHE_LOCATION_DISK:
                saveDataToDisk(key, context, dataToCache);
                break;
            
            default:
                break;
        }
    }

    private static void saveDataToDisk(String key, Context context, Object dataToCache) {
        mMemoryCache.put(key, dataToCache);
    }

    public static Object getData(String key, Context context) {
        Object lreturnData = null;
        SharedPreferences prefs = context.getSharedPreferences(CACHE_PREFERENCE, PreferenceActivity.MODE_MULTI_PROCESS);
        String data = prefs.getString(key, null);
        if (data != null) {
            try {
                lreturnData = deserializeObjectFromString(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return lreturnData;
    }

    public static Object deserializeObjectFromString(String objectString) throws Exception {

        byte[] bytes = Base64.decode(objectString, Base64.DEFAULT);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        GZIPInputStream gzipIn = new GZIPInputStream(bais);
        ObjectInputStream objectIn = new ObjectInputStream(gzipIn);
        Object lObject = objectIn.readObject();
        objectIn.close();

        return lObject;
    }
}
