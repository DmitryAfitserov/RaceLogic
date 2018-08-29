package com.example.dafitserov.racelogic;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

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

      //  Log.d("EEE", "cvc   " + listPreferenceSpeedFrom.getValue() + "  df  " +   listPreferenceSpeedTo.getValue());
        String a = listPreferenceSpeedFrom.getValue();
        String b = listPreferenceSpeedTo.getValue();

        speedFrom = Integer.parseInt(a);
        speedTo = Integer.parseInt(b);

        listPreferenceSpeedFrom.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                int selectedSpeed = Integer.parseInt(o.toString());
                int index = listPreferenceSpeedFrom.findIndexOfValue(o.toString());

                CharSequence[] entries = listPreferenceSpeedFrom.getEntries();

           //       Log.d("EEE", "choosedSpeed   " +  entries[index] + "   " + textValue);

                 //   Log.d("EEE", "selectedSpeed   " + selectedSpeed + "  speedTo  " +   speedTo);
                if(selectedSpeed >= speedTo){

                 //       Log.d("EEE", "if(choosedSpeed >= speedTo)   ");
                Toast.makeText(getActivity(),  R.string.toast_fail_selected_speed_to,
                                Toast.LENGTH_LONG).show();

                    String value = getResources().getString(R.string.max_speed_value);
                    listPreferenceSpeedTo.setValue(value);

                    String summary = getResources().getString(R.string.max_speed);
                    listPreferenceSpeedTo.setSummary(summary);
                    speedTo = Integer.parseInt(String.valueOf(value));
                }
                    speedFrom = selectedSpeed;
                    listPreferenceSpeedFrom.setSummary(entries[index]);
                return true;
            }
        });

        listPreferenceSpeedTo.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {

                int selectedSpeed = Integer.parseInt(o.toString());

                int index = listPreferenceSpeedTo.findIndexOfValue(o.toString());

                CharSequence[] entries = listPreferenceSpeedTo.getEntries();

                //     Log.d("EEE", "choosedSpeed   " +  entries[index] + "   " + textValue);

                //  Log.d("EEE", "selectedSpeed   " + selectedSpeed + "  speedTo  " +   speedTo);
                if (selectedSpeed <= speedFrom) {
                 //   Log.d("EEE", "if(electedSpeed <= speedFrom)   ");

                    Toast.makeText(getActivity(), R.string.toast_fail_selected_speed_from,
                            Toast.LENGTH_LONG).show();

                    String value = getResources().getString(R.string.min_speed_value);
                    listPreferenceSpeedFrom.setValue(value);

                    String summary = getResources().getString(R.string.min_speed);
                    listPreferenceSpeedFrom.setSummary(summary);

                    speedFrom = Integer.parseInt(String.valueOf(value));

                }
                    speedTo = selectedSpeed;
                    listPreferenceSpeedTo.setSummary(entries[index]);
                return true;

            }

        });
        Preference preference =
                (Preference)getPreferenceManager().findPreference("but_reset_speed");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                String value = getResources().getString(R.string.min_speed_value);
                listPreferenceSpeedFrom.setValue(value);
                String summary = getResources().getString(R.string.min_speed);
                listPreferenceSpeedFrom.setSummary(summary);
                speedFrom = Integer.parseInt(String.valueOf(value));

                value = getResources().getString(R.string.default_speed_value);
                listPreferenceSpeedTo.setValue(value);

                summary = getResources().getString(R.string.default_speed);
                listPreferenceSpeedTo.setSummary(summary);
                speedTo = Integer.parseInt(String.valueOf(value));


                return true;
            }
        });
    }


}
