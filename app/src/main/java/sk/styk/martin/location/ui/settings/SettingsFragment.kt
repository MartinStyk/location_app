package sk.styk.martin.location.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
import sk.styk.martin.location.R
import sk.styk.martin.location.util.Preferences

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(context), Preferences.KEY_LINE_COLOR)
        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(context), Preferences.KEY_LINE_WIDTH)
        onSharedPreferenceChanged(PreferenceManager.getDefaultSharedPreferences(context), Preferences.KEY_LOCATION_UPDATES_INTERVAL)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preference = findPreference(key)

        if (preference is ListPreference) {
            val prefIndex = preference.findIndexOfValue(sharedPreferences.getString(key, ""))
            if (prefIndex >= 0) {
                preference.setSummary(preference.entries[prefIndex])
            }
        } else {
            preference.summary = sharedPreferences.getString(key, "")

        }
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

}