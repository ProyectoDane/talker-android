package ar.uba.fi.talker.preferences;

import java.util.Map;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.component.Setting;

public class TalkerSettingManager {

	public static Setting getSettings(Activity activity) {

		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		Resources resources = activity.getResources();

		Setting setting = new Setting();
		Map<String, ?> settings = sharedPref.getAll();
		
		setting.setTextColor((String) settings.get(resources.getString(R.string.settings_text_color_key)));
		String textSize = (String) settings.get(resources.getString(R.string.settings_text_size_key));
		setting.setTextSize(Integer.parseInt(textSize));
		setting.setPencilColor((String) settings.get(resources.getString(R.string.settings_pencil_color_key)));
		
		String pencilSize = (String) settings.get(resources.getString(R.string.settings_pencil_size_key));
		setting.setPencilSize(Integer.parseInt(pencilSize));
		String eraserSize = (String) settings.get(resources.getString(R.string.settings_eraser_size_key));
		setting.setEraserSize(Integer.parseInt(eraserSize));
		Boolean isEnabledLabelImage = (Boolean) settings.get(resources.getString(R.string.settings_image_tag_key));
		setting.setIsEnabledLabelImage(isEnabledLabelImage);
		Boolean isEnabledLabelContact = (Boolean) settings.get(resources.getString(R.string.settings_contact_tag_key));
		setting.setIsEnabledLabelContact(isEnabledLabelContact);

		return setting;
	}

}
