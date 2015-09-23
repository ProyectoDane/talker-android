package ar.uba.fi.talker.fragment;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.adapter.InsertImageAdapter;
import ar.uba.fi.talker.adapter.InsertImageCategoryAdapter;
import ar.uba.fi.talker.dao.CategoryDAO;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dataSource.CategoryTalkerDataSource;
import ar.uba.fi.talker.dataSource.ImageTalkerDataSource;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.utils.ResultConstant;

public class InsertImageDialogFragment extends ParentDialogFragment {

	private ViewFlipper flipper;
	private View gridViewContainer;
	private ImageButton addButton;
	private AlertDialog alert;
	private View viewSelected;
	private Boolean isContactSearch = false;
	private int idSelected = 0;
	public static long categId = 0;
	private CategoryTalkerDataSource categoryTalkerDataSource;
	private ImageTalkerDataSource imageTalkerDataSource;
	
	public interface InsertImageDialogListener {
		public void onDialogPositiveClickInsertImageDialogListener(Uri uri,
				int orientation);
		public void onDialogPositiveClickInsertImageDialogListener(Bitmap bitmap, String label);
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
		if (imageTalkerDataSource == null ) {
			imageTalkerDataSource = new ImageTalkerDataSource(getActivity());
		}
	    
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
		
		gridViewContainer = View.inflate(getActivity(), R.layout.insert_image_gridview, null);

		this.flipper = (ViewFlipper) gridViewContainer
				.findViewById(R.id.vfImages);
		
		addButton = (ImageButton) gridViewContainer
				.findViewById(R.id.add_image);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				int result = isContactSearch ? ResultConstant.RESULT_INSERT_NEW_CONTACT : ResultConstant.RESULT_INSERT_NEW_IMAGE;
				getActivity().startActivityForResult(i, result);
				getDialog().dismiss();
			}
		});
		
		GridView gridView = (GridView) gridViewContainer
				.findViewById(R.id.insert_cat_gridview);
		
		final GridView gridViewImages = (GridView) gridViewContainer
				.findViewById(R.id.insert_image_gridview);
		
		List<CategoryDAO> categories;
		if (isContactSearch) {
			categories = categoryTalkerDataSource.getAllContacts();
		} else {
			categories = categoryTalkerDataSource.getImageCategories();
		}
		
		final InsertImageCategoryAdapter insertImageCategoryAdapter = new InsertImageCategoryAdapter(getActivity(), categories);
		gridView.setAdapter(insertImageCategoryAdapter);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				categId = insertImageCategoryAdapter.getItemId(position);
				List<ImageDAO> innnerImages = imageTalkerDataSource.getImagesForCategory(categId);
				gridViewImages.setAdapter(new InsertImageAdapter(getActivity(),innnerImages));
				addButton = (ImageButton)gridViewContainer.findViewById(R.id.add_image);
				addButton.setVisibility(View.VISIBLE);
				flipper.showNext();
			}
		});

		gridViewImages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				idSelected = (int)id;
				for (int i = 0; i < parent.getChildCount(); i++) {
					parent.getChildAt(i).setBackgroundColor(Color.WHITE);	
				}
				v.setBackgroundColor(getActivity().getResources().getColor(R.color.selectionViolet));
				viewSelected = v;
			}
		});

		builder.setView(gridViewContainer)
				.setTitle(this.isContactSearch == Boolean.TRUE ? R.string.insert_contact_title : R.string.insert_image_title)
				.setPositiveButton("ACEPTAR",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								if (viewSelected != null){
									TextView t = (TextView) viewSelected.findViewById(R.id.grid_item_label);
									ImageDAO imageDAO = imageTalkerDataSource.get(idSelected);
									Bitmap bmap = null;
									if (imageDAO.getPath().contains("/")){
										bmap = BitmapFactory.decodeFile(imageDAO.getPath());
									} else {
										int idCode = Integer.valueOf(imageDAO.getPath());
										bmap = BitmapFactory.decodeResource(getActivity().getResources(),idCode);
									}
									String imagelabel = t.getText().toString();
									boolean shouldShowLabel = isContactSearch == Boolean.TRUE ? 
											PaintManager.getSettings().getIsEnabledLabelContact() : PaintManager.getSettings().getIsEnabledLabelImage();
									if (shouldShowLabel){
										listener.onDialogPositiveClickInsertImageDialogListener(bmap, imagelabel);								
									}else{
										listener.onDialogPositiveClickInsertImageDialogListener(bmap, null);								
									}
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
								if (flipper.getCurrentView().equals(flipper.getChildAt(1))){
									flipper.showPrevious();
								}
								dialog.dismiss();
							}
			
						});
		
		alert = builder.create();
		return alert; 
	}

	@Override
	public void onStart() {
		super.onStart(); 
		//oculto el botón inicialmente, despues veo si estoy en categoria o en las imagenes
		addButton = (ImageButton)gridViewContainer.findViewById(R.id.add_image);
		
		final AlertDialog dialog = (AlertDialog) getDialog();
		if (dialog != null) {
			Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
			negativeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (flipper.getCurrentView().equals(flipper.getChildAt(1))){
						addButton = (ImageButton)gridViewContainer.findViewById(R.id.add_image);
						addButton.setVisibility(View.GONE);
						flipper.showPrevious();
					} else {
						dialog.dismiss();
					}
				}
			});
		}
	}
}
