package com.example.dafitserov.racelogic;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class SettingsFragment extends PreferenceFragment {

    int speedFrom;
    int speedTo;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final ListPreference listPreferenceSpeedFrom =
                (ListPreference)getPreferenceManager().findPreference("but_list_speed_from");


        final ListPreference listPreferenceSpeedTo =
                (ListPreference)getPreferenceManager().findPreference("but_list_speed_to");

        Log.d("EEE", "cvc   " + listPreferenceSpeedFrom.getValue() + "  df  " +   listPreferenceSpeedTo.getValue());
        String a = listPreferenceSpeedFrom.getValue();
        String b = listPreferenceSpeedTo.getValue();

        speedFrom = Integer.parseInt(a);
        speedTo = Integer.parseInt(b);



        listPreferenceSpeedFrom.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                int choosedSpeed = Integer.parseInt(listPreferenceSpeedFrom.getValue());
                    if(choosedSpeed >= speedTo){
                        Toast.makeText(getActivity(),  R.string.toast_fail_choosed_speed_to, Toast.LENGTH_LONG);
                        listPreferenceSpeedTo.setValue(String.valueOf(R.string.max_speed_value));
                        listPreferenceSpeedTo.setSummary(R.string.max_speed);
                        speedTo = Integer.parseInt(String.valueOf(R.string.max_speed_value));
                        speedFrom = choosedSpeed;
                    }
                return true;
            }
        });

        listPreferenceSpeedTo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                return true;
            }
        });


    }

    private void setIntSpeed(){

    }

}
