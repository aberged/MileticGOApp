package com.mileticgo.app.utils

import android.app.Activity
import android.content.Context
import android.util.Log

object SharedPrefs {

    /**
     * Called to save supplied value in shared preferences against given key.
     *
     * @param activity Caller activity
     * @param key     Key of value to save against
     * @param value   Value to save
     */
    fun save(activity: Activity, key: String, value: Any) {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        when (value) {
            is Int -> editor.putInt(key, value)
            is String -> editor.putString(key, value.toString())
            is Boolean -> editor.putBoolean(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Double -> editor.putLong(key, java.lang.Double.doubleToRawLongBits(value))
        }
        editor.apply()
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     *
     * @param activity     Caller activity
     * @param key          Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    fun get(activity: Activity, key: String, defaultValue: Any): Any? {
        val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        try {
            when (defaultValue) {
                is String -> return sharedPreferences.getString(key, defaultValue.toString())
                is Int -> return sharedPreferences.getInt(key, defaultValue)
                is Boolean -> return sharedPreferences.getBoolean(key, defaultValue)
                is Long -> return sharedPreferences.getLong(key, defaultValue)
                is Float -> return sharedPreferences.getFloat(key, defaultValue)
                is Double -> return java.lang.Double.longBitsToDouble(
                    sharedPreferences.getLong(
                        key,
                        java.lang.Double.doubleToLongBits(defaultValue)
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("Exception: ", e.message.toString())
            return defaultValue
        }

        return defaultValue
    }

    /**
     * @param activity Caller activity
     * @param key     Key to delete from SharedPreferences
     */
    fun remove(activity: Activity, key: String) {
        val prefs = activity.getPreferences(Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.remove(key)
        editor.apply()

    }

    fun hasKey(activity: Activity, key: String): Boolean {
        val prefs = activity.getPreferences(Context.MODE_PRIVATE)
        return prefs.contains(key)
    }
}