package ar.uba.fi.talker.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ar.uba.fi.talker.R;
import ar.uba.fi.talker.dao.ContactDAO;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dataSource.ContactTalkerDataSource;
import ar.uba.fi.talker.dataSource.ImageTalkerDataSource;
import ar.uba.fi.talker.utils.ImageUtils;

public class ContactDialogFragment extends ParentDialogFragment implements View.OnClickListener, DialogInterface.OnClickListener {

	private static int RESULT_LOAD_IMAGE_CONTACT = 3;
	private ImageView imageView = null;
	private View contactView;
	private ImageDAO image;
	private ContactDAO contact;
	
	public ContactDialogFragment() {
		this.image = null;
		this.contact = null;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		contactView = View.inflate(getActivity(), R.layout.contact_form, null);
		imageView = (ImageView) contactView.findViewById(R.id.contact_image);
		imageView.setOnClickListener(this);
		
		Bundle arguments = ContactDialogFragment.this.getArguments();
		if (arguments.containsKey("contact")) {
			contact = arguments.getParcelable("contact");
			image = new ImageTalkerDataSource(getActivity()).get(contact.getImageId());
			
			Bitmap bm = BitmapFactory.decodeFile(image.getPath());
			imageView.setImageBitmap(bm);

			TextView name = (TextView) contactView.findViewById(R.id.contact_input_name);
			name.setText(image.getName());
			TextView address = (TextView) contactView.findViewById(R.id.contact_input_address);
			address.setText(contact.getAddress());
			TextView phone = (TextView) contactView.findViewById(R.id.contact_input_phone);
			phone.setText(contact.getPhone());
		}

		// Use the Builder class for convenient dialog construction
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(contactView)
				.setTitle(R.string.insert_contact_title)
				.setPositiveButton(R.string.generic_accept, this)
				.setNegativeButton(R.string.generic_cancel, this);
		return builder.create();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == RESULT_LOAD_IMAGE_CONTACT && null != data) {
				Uri imageUri = data.getData();
				String imageName = imageUri.getLastPathSegment(); 
		        Bitmap bitmap = null;
		        int orientation = ImageUtils.getImageRotation(this.getActivity(), imageUri);
				try {/*Entra al if cuando se elige una foto de google +*/
					ContentResolver contentResolver = getActivity().getContentResolver();
					InputStream is = contentResolver.openInputStream(imageUri);
					if (imageUri != null && imageUri.getHost().contains("com.google.android.apps.photos.content")){
						/* if image belongs to google+*/
						bitmap = BitmapFactory.decodeStream(is);
					} else {
						bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri);
					}
				} catch (IOException e) {
					Log.e("ADD_SCENARIO", "Unexpected problem new scenario process.", e);
				}
				
				bitmap = ImageUtils.resizeBitmapWithOrientation(bitmap, orientation);
				imageView.setImageBitmap(bitmap);
				new ContactSaverTask(this.getActivity(), imageName).execute(bitmap);
			}
		}
	}

	private class ContactSaverTask extends AsyncTask<Bitmap, Boolean, File> {

		private final Context context;
		private final String imageName;

		public ContactSaverTask(Context context, String imageName) {
			this.context = context;
			this.imageName = imageName;
		}
		
		@Override
		protected void onPostExecute(File result) {
			ImageTalkerDataSource dataSource = new ImageTalkerDataSource(context);
			if (image == null) {
				image = new ImageDAO();
				image.setPath(result.getPath());
				image.setName(result.getName());
				Bundle arguments = ContactDialogFragment.this.getArguments();
				image.setIdCategory(arguments.getLong("category"));
				
				long imageId = dataSource.add(image);
				image.setId(imageId);
			} else {
				image.setPath(result.getPath());
				dataSource.update(image);
			}
			ContactTalkerDataSource contactDataSource = new ContactTalkerDataSource(context);
			if (contact == null) {
				contact = new ContactDAO();
				contact.setImageId(image.getId());
				contactDataSource.add(contact);				
			}
		}
		
		@Override
		protected File doInBackground(Bitmap... params) {
			Bitmap bitmap = params[0];
			ImageUtils.saveFileInternalStorage(imageName , bitmap , context, 0);
			
			return new File(context.getFilesDir(), imageName);
		}
		
	}

	@Override
	public void onStart() {
		super.onStart();
		// Buttons
		final Dialog dialog = getDialog();
		dialog.setOnDismissListener((OnDismissListener) getActivity());
		Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
		positiveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ContactDialogFragment.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
			}
		});
	}
	
	@Override
	public void onClick(DialogInterface dialog, int which) {

		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (image == null) {
				Toast.makeText(getActivity(), "DEBE SELECCIONAR UNA IMAGEN", Toast.LENGTH_LONG).show();
				return;
			}
			ImageTalkerDataSource dataSource = new ImageTalkerDataSource(getActivity());

			TextView name = (TextView) contactView.findViewById(R.id.contact_input_name);
			image.setName(name.getText().toString());
			image.setId(contact.getImageId());
			dataSource.update(image);

			ContactTalkerDataSource contactDataSource = new ContactTalkerDataSource(this.getActivity());
			TextView address = (TextView) contactView.findViewById(R.id.contact_input_address);
			contact.setAddress(address.getText().toString());
			
			TextView phone = (TextView) contactView.findViewById(R.id.contact_input_phone);
			contact.setPhone(phone.getText().toString());
			contactDataSource.update(contact);
			
		}
		dialog.dismiss();
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		this.startActivityForResult(i, RESULT_LOAD_IMAGE_CONTACT);
	}
	
}
