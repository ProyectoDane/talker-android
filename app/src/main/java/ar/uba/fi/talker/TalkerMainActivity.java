package ar.uba.fi.talker;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ar.uba.fi.talker.dataSource.ConversationTalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.fragment.ExitApplicationConfirmationDialogFragment;
import ar.uba.fi.talker.fragment.ExitApplicationConfirmationDialogFragment.ExitAplicationDialogListener;
import ar.uba.fi.talker.utils.PreferenceMapper;
import ar.uba.fi.talker.utils.ResultConstant;

public class TalkerMainActivity extends ActionBarActivity implements ExitAplicationDialogListener {

    private static Context context;
	final TalkerMainActivity self = this;

    public static Context getAppContext() {
        return TalkerMainActivity.context;
    }
	
    private void init(){
		Button historicalBttn = (Button) findViewById(R.id.history_panel_button);
    	evaluateVisibilityButton(historicalBttn);
    	
    	SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		Resources resources = this.getResources();
		
		if (sharedPref.getAll().isEmpty()) {
			Editor edit = sharedPref.edit();
			edit.putString(resources.getString(R.string.settings_text_color_key), "#000000");
			edit.putString(resources.getString(R.string.settings_text_size_key), "100");
			edit.putString(resources.getString(R.string.settings_pencil_color_key), "#00abea");
			edit.putString(resources.getString(R.string.settings_pencil_size_key), PreferenceMapper.SIZE_MEDIUM);
			edit.putString(resources.getString(R.string.settings_eraser_size_key), PreferenceMapper.SIZE_BIG);
			edit.putBoolean(resources.getString(R.string.settings_image_tag_key), true);
			edit.putBoolean(resources.getString(R.string.settings_contact_tag_key), true);
			edit.apply();
		}
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talker_main);
		
		TalkerMainActivity.context = getApplicationContext();

		
		Button exitBttn = (Button) findViewById(R.id.exit_app_button);
		Button settingsBttn = (Button) findViewById(R.id.action_settings_button);
		Button scenarioBttn = (Button) findViewById(R.id.new_conversation_button);
		Button historicalBttn = (Button) findViewById(R.id.history_panel_button);
		Button mannerOfUseBttn = (Button) findViewById(R.id.manner_of_use_button);
		init();
		
		scenarioBttn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						NewSceneActivity.class);
				startActivity(i);
			}
		});
		
		historicalBttn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						HistoricalActivity.class);
				startActivity(i);
			}
		});
		
		settingsBttn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						MyPreferenceActivity.class);
				startActivityForResult(i, ResultConstant.RESULT_SETTINGS);
			}
		});
		
		mannerOfUseBttn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						MannerOfUseActivity.class);
				startActivity(i);
			}
		});
		
		exitBttn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new ExitApplicationConfirmationDialogFragment();
				newFragment.onAttach(self);
				newFragment.show(self.getSupportFragmentManager(),"exit");
			}
		});
	}

	private void evaluateVisibilityButton(Button historicalBttn) {
		ConversationTalkerDataSource datasource = new ConversationTalkerDataSource(this);
		List<TalkerDTO> conversations = datasource.getAll();
		if (!conversations.isEmpty()){
			historicalBttn.setVisibility(View.VISIBLE);
		} else {
			historicalBttn.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onDialogPositiveClickExitApplicationDialogListener(DialogFragment dialog) {
		finish();
		System.exit(0);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		init();
	}
}
