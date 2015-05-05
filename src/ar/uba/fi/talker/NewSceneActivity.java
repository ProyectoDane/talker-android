package ar.uba.fi.talker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import ar.uba.fi.talker.adapter.GridScenesAdapter;
import ar.uba.fi.talker.adapter.PagerScenesAdapter;
import ar.uba.fi.talker.dao.ScenarioDAO;
import ar.uba.fi.talker.dao.ScenarioTalkerDataSource;
import ar.uba.fi.talker.fragment.ChangeNameDialogFragment.TextDialogListener;
import ar.uba.fi.talker.fragment.DeleteScenarioConfirmationDialogFragment.DeleteScenarioDialogListener;
import ar.uba.fi.talker.fragment.ScenesGridFragment;
import ar.uba.fi.talker.utils.GridItems;
import ar.uba.fi.talker.utils.GridUtils;
import ar.uba.fi.talker.utils.ImageUtils;
import ar.uba.fi.talker.utils.ScenarioView;

import com.viewpagerindicator.PageIndicator;

public class NewSceneActivity extends ActionBarActivity implements TextDialogListener, DeleteScenarioDialogListener {

	// Use this instance of the interface to deliver action events
	private static int RESULT_LOAD_IMAGE = 1;
	public PageIndicator pageIndicator;
	private ViewPager viewPager;
	private PagerScenesAdapter pagerAdapter;
	private ScenarioTalkerDataSource datasource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final NewSceneActivity self = this;
		setContentView(R.layout.layout_ext_scenes);

		scenesPagerSetting();
		
		ImageButton galleryScenarioBttn = (ImageButton) this.findViewById(R.id.new_scene_gallery);
		galleryScenarioBttn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				self.startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
	
	}

	private void scenesPagerSetting() {
		viewPager = (ViewPager) this.findViewById(R.id.pager);
		pageIndicator = (PageIndicator) this.findViewById(R.id.pagerIndicator);
		ArrayList<ScenarioView> scenarios = new ArrayList<ScenarioView>();

		if (datasource == null ) {
			datasource = new ScenarioTalkerDataSource(this.getApplicationContext());
		}
	    datasource.open();
		List<ScenarioDAO> allImages = datasource.getAllImages();
		datasource.close();
		for (int i = 0; i < allImages.size(); i++) {
			ScenarioDAO scenarioDAO = allImages.get(i);
			scenarios.add(new ScenarioView(scenarioDAO));
		}
		List<ScenesGridFragment> gridFragments = GridUtils.setScenesGridFragments(this, scenarios);

		pagerAdapter = new PagerScenesAdapter(this.getSupportFragmentManager(), gridFragments);
		viewPager.setAdapter(pagerAdapter);
		pageIndicator.setViewPager(viewPager);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.scenes, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//TODO acá esta configurado el codigo de empezar directo y guardarlo en la base
		byte[] bytes = null;
		ScenarioDAO scenario = null;
		if (requestCode == RESULT_LOAD_IMAGE && null != data) {
			Uri imageUri = data.getData();
			String scenarioName = imageUri.getLastPathSegment(); 
	        Bitmap bitmap = null;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
				bytes = ImageUtils.transformImage(bitmap);
				Context ctx = this.getApplicationContext();
				ImageUtils.saveFileInternalStorage(scenarioName, bitmap, ctx);
				File file = new File(ctx.getFilesDir(), scenarioName);
				scenario = datasource.createScenario(file.getPath(), scenarioName);
				
				ScenesGridFragment sgf = pagerAdapter.getItem(data.getIntExtra("position", 0));
				GridScenesAdapter gsa = (GridScenesAdapter) sgf.getmGridView().getAdapter();
				ScenarioView scenarioView = new ScenarioView(scenario);
				GridItems gridItem = new GridItems(scenario.getId(), scenarioView);
				//gsa.addItem(gridItem);
				scenesPagerSetting();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Bundle extras = new Bundle();
			extras.putByteArray("BMP",bytes);
			Intent intent = new Intent(this.getApplicationContext(), CanvasActivity.class);
			intent.putExtras(extras);
			startActivity(intent);
		}		
	}
	
	@Override
	public void onDialogPositiveClickTextDialogListener(DialogFragment dialog, int position) {
		Dialog dialogView = dialog.getDialog();
		EditText inputText = (EditText) dialogView.findViewById(R.id.insert_text_input);
		ScenesGridFragment sgf = pagerAdapter.getItem(position);
		GridScenesAdapter gsa = (GridScenesAdapter) sgf.getmGridView().getAdapter();
		String newScenarioName = inputText.getText().toString();
		((GridItems)gsa.getItem(position)).getScenarioView().setName(newScenarioName);
		gsa.notifyDataSetInvalidated();
		datasource.updateScenario(GridScenesAdapter.getItemSelectedId(), newScenarioName);
	}

	@Override
	public void onDialogPositiveClickDeleteScenarioDialogListener(
			DialogFragment dialog, int position) {
		int imageViewId = GridScenesAdapter.getItemSelectedId().intValue();
		ScenarioDAO scenarioDAO = datasource.getScenarioByID(imageViewId);
		boolean deleted = true;
		if (scenarioDAO.getPath() != null) {
			File file = new File(scenarioDAO.getPath());
			deleted = file.delete();
		}
		if (deleted){
			datasource.deleteScenario(GridScenesAdapter.getItemSelectedId());
		} // TODO informar error al usuario?

		ScenesGridFragment sgf = pagerAdapter.getItem(position);
		GridScenesAdapter gsa = (GridScenesAdapter) sgf.getmGridView().getAdapter();
		gsa.removeItem(position);
		gsa.notifyDataSetInvalidated();
		scenesPagerSetting();
	}

}
