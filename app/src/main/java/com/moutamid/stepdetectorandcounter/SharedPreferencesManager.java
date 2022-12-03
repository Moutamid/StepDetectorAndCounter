package com.moutamid.stepdetectorandcounter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesManager {
    /**
     * SharedPreferences to store the settings. This way, they'll be available next time the user starts the app
     */
    private final SharedPreferences sPreferences;
    /**
     * The class itself
     */
    private final Context context;
    /**
     * Editor to make changes on sharedPreferences
     */
    private SharedPreferences.Editor sEditor;

    public SharedPreferencesManager(Context context) {
        this.context = context;
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharedPreferences.Editor getEditor() {
        return sPreferences.edit();
    }

    /**
     * Store a boolean value in sharedPreferences
     *
     * @param tag   identifies the value
     * @param value the value itself
     */

    public void storeBoolean(String tag, boolean value) {
        sEditor = getEditor();
        sEditor.putBoolean(tag, value);
        sEditor.apply();
    }

    /**
     * Store a string in sharedPreferences
     *
     * @param tag identifies the value
     * @param str the string itself
     */

    public void storeString(String tag, String str) {
        sEditor = getEditor();
        sEditor.putString(tag, str);
        sEditor.apply();
    }
    public void storeInt(String tag, int value) {
        sEditor = getEditor();
        sEditor.putInt(tag, value);
        sEditor.apply();
    }
    /**
     * @param tag      identifies the value
     * @param defValue default value
     * @return the stored or default value
     */

    public boolean retrieveBoolean(String tag, boolean defValue) {
        return sPreferences.getBoolean(tag, defValue);

    }
    public int retrieveInt(String tag, int defValue) {
        return sPreferences.getInt(tag, defValue);

    }
    /**
     * @param tag    identifies the string
     * @param defStr default string
     * @return the stored or default string
     */

    public String retrieveString(String tag, String defStr) {
        return sPreferences.getString(tag, defStr);
    }

}
//Incorrect Bracket Closing Removal.