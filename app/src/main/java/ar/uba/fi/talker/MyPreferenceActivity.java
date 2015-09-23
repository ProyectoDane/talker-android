package ar.uba.fi.talker;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class MyPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragment()).commit();

	}

	public static class PrefsFragment extends PreferenceFragment implements OnPreferenceChangeListener {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);

			// Register the ListPreferences
			// pencil
			ListPreference settingsPencilSize = (ListPreference) findPreference(getResources().getString(R.string.settings_pencil_size_key));
			settingsPencilSize.setSummary(settingsPencilSize.getEntry());
			settingsPencilSize.setOnPreferenceChangeListener(this);
			ListPreference settingsPencilColor = (ListPreference) findPreference(getResources().getString(
					R.string.settings_pencil_color_key));
			settingsPencilColor.setSummary(settingsPencilColor.getEntry());
			settingsPencilColor.setOnPreferenceChangeListener(this);
			// text
			ListPreference settingsTextSize = (ListPreference) findPreference(getResources().getString(R.string.settings_text_size_key));
			settingsTextSize.setSummary(settingsTextSize.getEntry());
			settingsTextSize.setOnPreferenceChangeListener(this);
			ListPreference settingsTextColor = (ListPreference) findPreference(getResources().getString(R.string.settings_text_color_key));
			settingsTextColor.setSummary(settingsTextColor.getEntry());
			settingsTextColor.setOnPreferenceChangeListener(this);
			// eraser
			ListPreference settingsEraserSize = (ListPreference) findPreference(getResources().getString(R.string.settings_eraser_size_key));
			settingsEraserSize.setSummary(settingsEraserSize.getEntry());
			settingsEraserSize.setOnPreferenceChangeListener(this);
		}

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			ListPreference listPreference = (ListPreference) preference;
			int id = 0;
			for (int i = 0; i < listPreference.getEntryValues().length; i++) {
				if (listPreference.getEntryValues()[i].equals(newValue.toString())) {
					id = i;
					break;
				}
			}
			preference.setSummary(listPreference.getEntries()[id]);
			return true;
		}
	}

}
