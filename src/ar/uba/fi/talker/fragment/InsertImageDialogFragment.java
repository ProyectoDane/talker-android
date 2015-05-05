package ar.uba.fi.talker.fragment;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.adapter.InsertImageAdapter;
import ar.uba.fi.talker.adapter.InsertImageCategoryAdapter;
import ar.uba.fi.talker.dao.CategoryDAO;
import ar.uba.fi.talker.dao.CategoryTalkerDataSource;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dao.ImageTalkerDataSource;

public class InsertImageDialogFragment extends TalkerDialogFragment {

	private ViewFlipper flipper;
	
	private AlertDialog alert;
	private View viewSelected;
	private Boolean isContactSearch = false;
	
	private CategoryTalkerDataSource categoryTalkerDataSource;
	private ImageTalkerDataSource imageTalkerDataSource;
	
	public interface InsertImageDialogListener {
		public void onDialogPositiveClickInsertImageDialogListener(Uri uri,
				Matrix matrix);
		public void onDialogPositiveClickInsertImageDialogListener(Bitmap bitmap);
		public void onDialogPositiveClickInsertImageDialogListener(
				InsertImageDialogFragment insertImageDialogFragment);
	}

	private InsertImageDialogListener listener;

	public InsertImageDialogFragment(Boolean isContactSearch) {
		super();
		this.isContactSearch = true;
	}

	public InsertImageDialogFragment() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (categoryTalkerDataSource == null ) {
			categoryTalkerDataSource = new CategoryTalkerDataSource(getActivity());
		}
		categoryTalkerDataSource.open();
		if (imageTalkerDataSource == null ) {
			imageTalkerDataSource = new ImageTalkerDataSource(getActivity());
		}
		imageTalkerDataSource.open();
	    
		try {
			listener = (InsertImageDialogListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement InsertImageDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		View gridViewContainer = View.inflate(getActivity(), R.layout.insert_image_gridview, null);

		this.flipper = (ViewFlipper) gridViewContainer
				.findViewById(R.id.vfImages);
		
		GridView gridView = (GridView) gridViewContainer
				.findViewById(R.id.insert_cat_gridview);
		
		final GridView gridViewImages = (GridView) gridViewContainer
				.findViewById(R.id.insert_image_gridview);
		
		List<CategoryDAO> categories;
		if (isContactSearch) {
			categories = categoryTalkerDataSource.getContactCategories();
		} else {
			categories = categoryTalkerDataSource.getImageCategories();
		}
		
		final InsertImageCategoryAdapter insertImageCategoryAdapter = new InsertImageCategoryAdapter(getActivity(), categories);
		gridView.setAdapter(insertImageCategoryAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				long categId = insertImageCategoryAdapter.getItemId(position);
				List<ImageDAO> innnerImages = imageTalkerDataSource.getImagesForCategory(categId);
				gridViewImages.setAdapter(new InsertImageAdapter(getActivity(),innnerImages));
				flipper.showNext();
			}
		});

		gridViewImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				for (int i = 0; i < parent.getChildCount(); i++) {
					parent.getChildAt(i).setBackgroundColor(Color.WHITE);	
				}
				v.setBackgroundColor(Color.CYAN);
				viewSelected = v;
	/*			if (position == 4) {
					Intent i = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					getActivity().startActivityForResult(i, RESULT_LOAD_IMAGE);
				}
				*/
			}
		});

		builder.setView(gridViewContainer)
				.setTitle(this.isContactSearch == Boolean.TRUE ? R.string.insert_contact_title : R.string.insert_image_title)
				.setPositiveButton("ACEPTAR",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if (viewSelected != null){
									ImageView imageView = (ImageView) viewSelected.findViewById(R.id.grid_item_image);
									imageView.buildDrawingCache();
									Bitmap bmap = Bitmap.createBitmap(imageView.getDrawingCache());
									listener.onDialogPositiveClickInsertImageDialogListener(bmap);								
									dialog.dismiss();
								} else {
									listener.onDialogPositiveClickInsertImageDialogListener(InsertImageDialogFragment.this);
								}
							}
						})
				.setNegativeButton(R.string.insert_image_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		
		alert = builder.create();
		return alert; 
	}

	
	@Override
	public void onStart() {
		super.onStart(); // super.onStart() is where dialog.show() is actually called on the underlying dialog, so we have to do it after
							// this point
		final AlertDialog dialog = (AlertDialog) getDialog();
		if (dialog != null) {
			Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
			negativeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (flipper.getCurrentView().equals(flipper.getChildAt(1))){
						flipper.showPrevious();
				/*		Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
						positiveButton.setEnabled(false);*/
					} else {
						dialog.dismiss();
					}
				}
			});
		}
	}
}
