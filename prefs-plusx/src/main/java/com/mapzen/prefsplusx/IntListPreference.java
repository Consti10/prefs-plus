package com.mapzen.prefsplusx;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;

import androidx.preference.ListPreference;

import java.util.Arrays;

/**
 * {@link androidx.preference.ListPreference} that saves integer values to
 * {@link android.content.SharedPreferences}.
 */
public class IntListPreference extends ListPreference {
    public static final String TAG = IntListPreference.class.getSimpleName();

    public IntListPreference(Context context) {
        super(context);
        setDefaultEntryValuesIfNull();
    }

    public IntListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDefaultEntryValuesIfNull();
    }

    // If there are no entry values set populate it with a default array that holds
    // as many entries as the entry array and maps element 0 -> 0, element 1 -> 1 and more
    private void setDefaultEntryValuesIfNull(){
        if(getEntryValues()==null){
            //Log.d(TAG,"Setting default entry values");
            if(getEntries()==null){
                Log.w(TAG,"No entries - will crash");
            }
            final int size=getEntries().length;
            CharSequence[] defaultEntryValues =new CharSequence[size];
            for(int i=0;i<size;i++){
                defaultEntryValues[i]=Integer.toString(i);
            }
            setEntryValues(defaultEntryValues);
        }
    }

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        if(getSharedPreferences().contains(getKey())) {
            int intValue = getPersistedInt(0);
            return String.valueOf(intValue);
        } else {
            return defaultReturnValue;
        }
    }

    @Override
    protected boolean persistString(String value) {
        int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Unable to parse preference value: " + value);
            setSummary("Invalid value");
            return false;
        }

        final int valueIndex = Arrays.asList(getEntryValues()).indexOf(value);
        setSummary(getEntries()[valueIndex]);
        return persistInt(intValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Integer.decode(a.getString(index)).toString();
    }
}
