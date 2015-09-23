package ar.uba.fi.talker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.uba.fi.talker.adapter.GridScenesAdapter;
import ar.uba.fi.talker.adapter.PagerScenesAdapter;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dataSource.ContactTalkerDataSource;
import ar.uba.fi.talker.dataSource.ImageTalkerDataSource;
import ar.uba.fi.talker.dataSource.TalkerDataSource;
import ar.uba.fi.talker.dto.TalkerDTO;
import ar.uba.fi.talker.fragment.ContactDialogFragment;
import ar.uba.fi.talker.fragment.ScenesGridFragment;
import ar.uba.fi.talker.utils.GridItems;
import ar.uba.fi.talker.utils.GridUtils;
import ar.uba.fi.talker.utils.ImageUtils;

import com.viewpagerindicator.PageIndicator;

public class ImageSettingsActivity extends CommonImageSettingsActiviy implements DialogInterface.OnDismissListener {

    private final ImageTalkerDataSource imageDatasource;
    private final ContactTalkerDataSource contactDatasource;
	public PageIndicator pageIndicator;
	private ViewPager viewPager;
	private GridView gridView = null;
	private int keyId;
	private boolean isContact;
	private static int RESULT_LOAD_IMAGE = 1;
	
	public static ContactDialogFragment newFragment;
	
	public ImageSettingsActivity() {
		imageDatasource = new ImageTalkerDataSource(this);
		contactDatasource = new ContactTalkerDataSource(this);
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle b = getIntent().getExtras();
		keyId= b.getInt("keyId");
		isContact= b.getBoolean("isContact");
		setContentView(R.layout.layout_images);
		viewPager = (ViewPager) this.findViewById(R.id.pager);
		pageIndicator = (PageIndicator) this.findViewById(R.id.pagerIndicator);
		
		this.imagesPagerSetting();
		ImageButton createImageBttn = (ImageButton) this.findViewById(R.id.new_image_gallery);
		if (isContact){
			createImageBttn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageSettingsActivity.newFragment = new ContactDialogFragment();
					Bundle args = new Bundle();
					args.putLong("category", keyId);
					ImageSettingsActivity.newFragment.setArguments(args);
					ImageSettingsActivity.newFragment.show(getSupportFragmentManager(), "insert_contact");
				}
			});
		} else {
			createImageBttn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
				}
			});
		}
	}
	

	private void imagesPagerSetting() {
		List<ImageDAO> allImages = imageDatasource.getImagesForCategory(keyId);
		List<ScenesGridFragment> gridFragments = GridUtils.setScenesGridFragments(this, allImages, imageDatasource);

		PagerScenesAdapter pagerAdapter = new PagerScenesAdapter(getSupportFragmentManager(), gridFragments);
		viewPager.setAdapter(pagerAdapter);
		pageIndicator.setViewPager(viewPager);
	}

	@Override
	public void onDialogPositiveClickDeleteResourceDialogListener(
			TalkerDTO scenarioView) {
		
		boolean deleted = true;
		if (scenarioView.getPath().contains("/")) {
			File file = new File(scenarioView.getPath());
			deleted = file.delete();
		}
		if (deleted){
			ImageDAO entity = new ImageDAO();
			entity.setId(scenarioView.getId());
			imageDatasource.delete(entity.getId());
		} else {
			Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
			Log.e("NewScene", "Unexpected error deleting imagen.");
		}
		imagesPagerSetting();
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ImageDAO imageDAO = null;
		if (requestCode == RESULT_LOAD_IMAGE && null != data) {
			Uri imageUri = data.getData();
			String imageName = imageUri.getLastPathSegment(); 
	        Bitmap bitmap = null;
			try {/*Entra al if cuando se elige una foto de google +*/
				if (imageUri != null && imageUri.getHost().contains("com.google.android.apps.photos.content")){
					InputStream is = getContentResolver().openInputStream(imageUri);
					bitmap = BitmapFactory.decodeStream(is);
				} else {
					bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
				}
				imageName = "IMAGE_" + String.valueOf(imageDatasource.getLastImageID() + 1);
				ImageUtils.saveFileInternalStorage(imageName, bitmap, this.getApplicationContext(),0);
				File file = new File(this.getApplicationContext().getFilesDir(), imageName);
				imageDAO = imageDatasource.createImage(file.getPath(), imageName, keyId);
				if (gridView == null ){
					setGridViewAdapter();
				}
				GridScenesAdapter gsa = (GridScenesAdapter) gridView.getAdapter();
				TalkerDTO elementGridView = new TalkerDTO();
				elementGridView.setId(imageDAO.getId());
				elementGridView.setName(imageDAO.getName());
				elementGridView.setPath(imageDAO.getPath());
				GridItems gridItem = new GridItems(imageDAO.getId(), elementGridView);
				gsa.add(gridItem);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.imagesPagerSetting();
	}

	private void setGridViewAdapter() {
		TalkerDTO element = new TalkerDTO();
		List<TalkerDTO> imageViews = new ArrayList<TalkerDTO>();
		imageViews.add(element);
		TalkerDataSource datasource = this.isContact ? contactDatasource : imageDatasource;
		
		List<ScenesGridFragment> gridFragments = GridUtils.setScenesGridFragments(this, imageViews, datasource);
		ScenesGridFragment sgf = gridFragments.get(0);
		GridScenesAdapter mGridAdapter = new GridScenesAdapter(this, sgf.getGridItems());
		mGridAdapter.setDao(datasource);
		gridView = new GridView(this);
		gridView.setAdapter(mGridAdapter);
	}

	public boolean isContact() {
		return isContact;
	}

	@Override
	public void onDialogPositiveClickTextDialogListener(DialogFragment dialog) {
		// nothing to do
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		ImageSettingsActivity.newFragment.onDismiss(dialog);
		this.imagesPagerSetting();
	}
	
}
