package ar.uba.fi.talker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.uba.fi.talker.adapter.PagerScenesAdapter;
import ar.uba.fi.talker.component.indicator.PageIndicator;
import ar.uba.fi.talker.dao.ScenarioDAO;
import ar.uba.fi.talker.dataSource.ScenarioTalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.fragment.DeleteResourceConfirmationDialogFragment.DeleteResourceDialogListener;
import ar.uba.fi.talker.fragment.ScenesGridFragment;
import ar.uba.fi.talker.utils.GridUtils;
import ar.uba.fi.talker.utils.ImageUtils;
import ar.uba.fi.talker.utils.ResultConstant;

public class NewSceneActivity extends ActionBarActivity implements DeleteResourceDialogListener {

	private ScenarioTalkerDataSource dataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final NewSceneActivity self = this;
		setContentView(R.layout.layout_images);

		scenesPagerSetting();
		ImageButton galleryScenarioBttn = (ImageButton) this.findViewById(R.id.new_image_button);
		galleryScenarioBttn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				self.startActivityForResult(i, ResultConstant.RESULT_LOAD_IMAGE);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		scenesPagerSetting();
	}

	private void scenesPagerSetting() {
		ViewPager viewPager = (ViewPager) this.findViewById(R.id.pager);
		PageIndicator pageIndicator = (PageIndicator) this.findViewById(R.id.pagerIndicator);

		if (dataSource == null ) {
			dataSource = new ScenarioTalkerDataSource(this.getApplicationContext());
		}
		List<TalkerDTO> allImages = dataSource.getAll();
		List<ScenesGridFragment> gridFragments = GridUtils.setScenesGridFragments(this, allImages, dataSource);

		PagerScenesAdapter pagerAdapter = new PagerScenesAdapter(this.getSupportFragmentManager(), gridFragments);
		viewPager.setAdapter(pagerAdapter);
		pageIndicator.setViewPager(viewPager);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/* With requestCode == ResultConstant.RESULT_LOAD_IMAGE saves the image on DB and begins conversation */
		byte[] bytes = null;
		if (requestCode == ResultConstant.RESULT_LOAD_IMAGE && null != data) {
			Uri imageUri = data.getData();
			String scenarioName = imageUri.getLastPathSegment(); 
	        Bitmap bitmap = null;
	        int orientation = ImageUtils.getImageRotation(this.getApplicationContext(), imageUri);
			try {/*Entra al if cuando se elige una foto de google +*/
				if (imageUri != null && imageUri.getHost().contains("com.google.android.apps.photos.content")){
					/* if image belongs to google+*/
					InputStream is = getContentResolver().openInputStream(imageUri);
					bitmap = BitmapFactory.decodeStream(is);
				} else {
					bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
				}
			} catch (IOException e) {
				Log.e("ADD_SCENARIO", "Unexpected problem new scenario process.", e);
			}
			
			Intent intent = new Intent(this.getApplicationContext(), CanvasActivity.class);
			if (bitmap != null) {
				scenarioName = "SCENARIO_" + String.valueOf(dataSource.getLastId() + 1);
				new ImageSaverTask(this.getApplicationContext(), scenarioName, orientation).execute(bitmap);
				bytes = ImageUtils.transformImage(bitmap);
				Bundle extras = new Bundle();
				extras.putByteArray("BMP",bytes);
				intent.putExtras(extras);
			}
			startActivity(intent);
		}
	}

	private class ImageSaverTask extends AsyncTask<Bitmap, Boolean, Boolean> {

		private Context context;
		private String name;
		private int orientation;

		public ImageSaverTask(Context context, String name, int orientation) {
			this.context = context;
			this.name = name;
			this.orientation = orientation;
		}
		
		@Override
		protected Boolean doInBackground(Bitmap... params) {
			Bitmap bitmap = params[0];
			ImageUtils.saveFileInternalStorage(name, bitmap , context, orientation);
			File file = new File(context.getFilesDir(), name);

			ScenarioDAO scenario = new ScenarioDAO();
			scenario.setName(name);
			scenario.setPath(file.getPath());
			
			dataSource.add(scenario);
			return null;
		}
		
	}
	
	@Override
	public void onDialogPositiveClickDeleteResourceDialogListener(TalkerDTO scenarioView) {
		boolean deleted = true;
		if (scenarioView.getPath().contains("/")) {
			File file = new File(scenarioView.getPath());
			deleted = file.delete();
		}
		if (deleted){
			dataSource.delete(scenarioView.getId());
		} else {
			Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
			Log.e("NewScene", "Unexpected error deleting imagen.");
		}
		scenesPagerSetting();
	}

}
