package ar.uba.fi.talker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.uba.fi.talker.component.ComponentType;
import ar.uba.fi.talker.component.EraserStroke;
import ar.uba.fi.talker.dao.ContactDAO;
import ar.uba.fi.talker.dao.ConversationDAO;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dataSource.ContactTalkerDataSource;
import ar.uba.fi.talker.dataSource.ConversationTalkerDataSource;
import ar.uba.fi.talker.dataSource.ImageTalkerDataSource;
import ar.uba.fi.talker.fragment.CalculatorFragment;
import ar.uba.fi.talker.fragment.DatePickerFragment;
import ar.uba.fi.talker.fragment.DatePickerFragment.DatePickerDialogListener;
import ar.uba.fi.talker.fragment.EraseAllConfirmationDialogFragment;
import ar.uba.fi.talker.fragment.EraseAllConfirmationDialogFragment.EraseAllConfirmationDialogListener;
import ar.uba.fi.talker.fragment.ExitConversationConfirmationDialogFragment;
import ar.uba.fi.talker.fragment.ExitConversationConfirmationDialogFragment.ExitConversationConfirmationDialogListener;
import ar.uba.fi.talker.fragment.InsertImageDialogFragment;
import ar.uba.fi.talker.fragment.InsertImageDialogFragment.InsertImageDialogListener;
import ar.uba.fi.talker.fragment.SaveAllConfirmationDialogFragment;
import ar.uba.fi.talker.fragment.SaveAllConfirmationDialogFragment.SaveAllConfirmationDialogListener;
import ar.uba.fi.talker.fragment.TextDialogFragment;
import ar.uba.fi.talker.fragment.TextDialogFragment.TextDialogListener;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.preferences.TalkerSettingManager;
import ar.uba.fi.talker.utils.ImageUtils;
import ar.uba.fi.talker.utils.ResultConstant;
import ar.uba.fi.talker.view.Scenario;

public class CanvasActivity extends ActionBarActivity implements
		TextDialogListener, InsertImageDialogListener,
		EraseAllConfirmationDialogListener, OnDateSetListener, 
		DatePickerDialogListener, SaveAllConfirmationDialogListener, ExitConversationConfirmationDialogListener {

	private static long backPressed;
	
	final String TAG = "CanvasActivity";

	private Scenario scenario;
	
	private ConversationTalkerDataSource datasourceConversation;

	private View activeTool;
	private ImageTalkerDataSource datasourceImage;
		
	private void setActiveTool(View view) {
		if (activeTool != null) {
			this.activeTool.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
		}
		this.activeTool = view;
		this.activeTool.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
		EraserStroke.enabled = false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.canvas_default);
		
		//seteo de configuracion
		PaintManager.setSettings(TalkerSettingManager.getSettings(this));

		scenario = (Scenario) this.findViewById(R.id.gestureOverlayView1);
		this.setBackground();

		View settings = findViewById(R.id.button_settings);
		settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(),
						MyPreferenceActivity.class);
				startActivityForResult(i, ResultConstant.RESULT_SETTINGS);
			}
		});
		
		ImageButton pencilOp = (ImageButton) findViewById(R.id.pencilOption);
		pencilOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.PENCIL);
				scenario.invalidate();
				CanvasActivity.this.setActiveTool(v);
			}
		});

		ImageButton eraserOp = (ImageButton) findViewById(R.id.eraserOption);
		eraserOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.ERASER);
				CanvasActivity.this.setActiveTool(v);
				EraserStroke.enabled = true;
				scenario.invalidate();
			}
		});

		ImageButton eraseAllOp = (ImageButton) findViewById(R.id.eraseAllOption);
		eraseAllOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new EraseAllConfirmationDialogFragment();
				newFragment.show(getSupportFragmentManager(), "erase_all");
				scenario.invalidate();
				CanvasActivity.this.setActiveTool(v);
			}
		});

		ImageButton textOp = (ImageButton) findViewById(R.id.textOption);
		textOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new TextDialogFragment();
				newFragment.show(getSupportFragmentManager(), "insert_text");
				CanvasActivity.this.setActiveTool(v);
			}
		});

		ImageButton imageOption = (ImageButton) findViewById(R.id.insertImageOption);
		imageOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.IMAGE);
				DialogFragment newFragment = new InsertImageDialogFragment();
				newFragment.show(getSupportFragmentManager(), "insert_image");
				scenario.invalidate();
				CanvasActivity.this.setActiveTool(v);
			}
		});

		ImageButton contactOption = (ImageButton) findViewById(R.id.insertContactOption);
		contactOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.CONTACT);
				DialogFragment newFragment = new InsertImageDialogFragment(Boolean.TRUE);
				newFragment.show(getSupportFragmentManager(), "insert_image");
				scenario.invalidate();
				CanvasActivity.this.setActiveTool(v);
			}
		});

		ImageButton calcOption = (ImageButton) findViewById(R.id.calculatorOption);
		calcOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new CalculatorFragment();
				newFragment.show(getSupportFragmentManager(), "calculator");
				CanvasActivity.this.setActiveTool(v);
			}
		});

		ImageButton calendarOption = (ImageButton) findViewById(R.id.calendarOption);
		calendarOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "calendar");
				CanvasActivity.this.setActiveTool(v);
			}
		});
		
		ImageButton saveOption = (ImageButton) findViewById(R.id.saveAll);
		saveOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new SaveAllConfirmationDialogFragment();
				newFragment.show(getSupportFragmentManager(), "save_all");
				CanvasActivity.this.setActiveTool(v);
			}
		});
		
		ImageButton exitOption = (ImageButton) findViewById(R.id.exit);
		exitOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new ExitConversationConfirmationDialogFragment();
				newFragment.show(getSupportFragmentManager(), "exit_canvas");
				scenario.invalidate();
				CanvasActivity.this.setActiveTool(v);
			}
		});
		
		//set pencil active 
		scenario.setActiveComponentType(ComponentType.PENCIL);
		CanvasActivity.this.setActiveTool(pencilOp);
	}

	private void setBackground() {
		Intent intent = getIntent();
		boolean isHistory = false;
		if (intent.hasExtra("isHistory")) {
			isHistory = intent.getBooleanExtra("isHistory", false);
		}
		if(intent.hasExtra("BMP")) {
		    Bundle extras = intent.getExtras();
		    byte[] bytes = extras.getByteArray("BMP");
		    Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		    
		    scenario.setBackgroundImage(image, isHistory);
		} else if (intent.hasExtra("code")) {
		    Bundle extras = intent.getExtras();
		    int code = extras.getInt("code");
		    Bitmap image = BitmapFactory.decodeResource(getResources(), code);
		    scenario.setBackgroundImage(image, isHistory);
		} else if (intent.hasExtra("path")) {
		    Bundle extras = intent.getExtras();
		    String path = extras.getString("path");
		    Bitmap image = BitmapFactory.decodeFile(path);
		    scenario.setBackgroundImage(image, isHistory);
		}
	}
	
	@Override
	protected void onRestart() {
		scenario.restore();
		super.onRestart();
	}
	
	@Override
	public void onDialogPositiveClickTextDialogListener(DialogFragment dialog) {
		Dialog dialogView = dialog.getDialog();
		EditText inputText = (EditText) dialogView
				.findViewById(R.id.insert_text_input);
		scenario.setText(inputText.getText());
	}
	

	@Override
	public void onDialogPositiveClickInsertImageDialogListener(Uri uri, int orientation) {

		Bitmap bitmap=null;
		try {
			if (uri != null && uri.getHost().contains("com.google.android.apps.photos.content")){
				InputStream is = getContentResolver().openInputStream(uri);
				bitmap = BitmapFactory.decodeStream(is);
			} else {
				bitmap = Media.getBitmap(this.getContentResolver(), uri);
			}
			Matrix matrix = ImageUtils.generateMatrix(bitmap, orientation);
			scenario.addImage(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true), null);
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
			Log.e("CANVAS", "Unexpected error adding imagen.", e);
		} catch (IOException e) {
			Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
			Log.e("CANVAS", "Unexpected error adding imagen.", e);
		}
	}
	
	@Override
	public void onDialogPositiveClickEraseAllConfirmationListener(
			DialogFragment dialog) {
		scenario.clear();
	}

	@Override
	public void onDialogPositiveClickInsertImageDialogListener(Bitmap bitmap, String label) {
		scenario.addImage(bitmap, label);
	}
	
	@Override
	public void onDialogPositiveClickInsertImageDialogListener(Bitmap bitmap) {
		scenario.addImage(bitmap, null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if ((requestCode == ResultConstant.RESULT_LOAD_IMAGE 
				|| requestCode == ResultConstant.RESULT_INSERT_NEW_IMAGE
				|| requestCode == ResultConstant.RESULT_INSERT_NEW_CONTACT) 
				&& resultCode == Activity.RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			int orientation = ImageUtils.getImageRotation(this.getApplicationContext(), selectedImage);			
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);
			if (requestCode == ResultConstant.RESULT_INSERT_NEW_IMAGE
					|| requestCode == ResultConstant.RESULT_INSERT_NEW_CONTACT){
				boolean isContact = requestCode == ResultConstant.RESULT_INSERT_NEW_CONTACT;
				//TODO: pasar a otro thread asi no frena todo
				saveNewImage(data, selectedImage, orientation, isContact);
			}
			this.onDialogPositiveClickInsertImageDialogListener(selectedImage, orientation);

		} else if (requestCode == ResultConstant.RESULT_SETTINGS && resultCode == Activity.RESULT_CANCELED) {
			PaintManager.setSettings(TalkerSettingManager.getSettings(this));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void saveNewImage(Intent data, Uri selectedImage, int orientation, boolean isContact) {
		datasourceImage = new ImageTalkerDataSource(this);
		String imageName = selectedImage.getLastPathSegment(); 
		Bitmap bitmap = null;
		Context ctx = this.getApplicationContext();
		try {
			if (selectedImage != null && selectedImage.getHost().contains("com.google.android.apps.photos.content")){
				InputStream is = getContentResolver().openInputStream(selectedImage);
				bitmap = BitmapFactory.decodeStream(is);
			} else {
				bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
			}
			imageName = "IMAGE_" + String.valueOf(datasourceImage.getLastImageID() + 1);
			ImageUtils.saveFileInternalStorage(imageName, bitmap, ctx, orientation);
			File file = new File(ctx.getFilesDir(), imageName);
			
		    ImageDAO createImage = datasourceImage.createImage(file.getPath(), "", InsertImageDialogFragment.categId);
		    
		    if (isContact) {
		    	ContactTalkerDataSource dataSourceContact = new ContactTalkerDataSource(this);
		    	ContactDAO contactDAO = new ContactDAO();
		    	contactDAO.setImageId(createImage.getId());
		    	dataSourceContact.add(contactDAO);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if (view.isShown()) {

			final Calendar calendar = Calendar.getInstance();
			calendar.set(year, monthOfYear, dayOfMonth);

			SpannableStringBuilder date = new SpannableStringBuilder();
			date.append(String.format("%1$tA %1$td/%1$tB/%1$tY", calendar));

			scenario.setText(date);
		}
	}
	
	@Override
	public void onDialogPositiveClickDatePickerDialogListener(
			DatePickerFragment datePickerFragment) {
		DatePickerDialog dialogView = (DatePickerDialog) datePickerFragment.getDialog();
		DatePicker datePicker = dialogView.getDatePicker();
		int dayOfMonth = datePicker.getDayOfMonth();
		int monthOfYear = datePicker.getMonth();
		int year = datePicker.getYear();
		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, monthOfYear, dayOfMonth);

		scenario.addCalendar(calendar, R.drawable.blanco);
	}

	@Override
	public void onDialogPositiveClickSaveAllConfirmationListener(
			DialogFragment dialog) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String filename = dateFormat.format(date);
		
		if (datasourceConversation == null ) {
			datasourceConversation = new ConversationTalkerDataSource(this.getApplicationContext());
		}
		if (filename != null) {
			Context ctx = this.getApplicationContext();
			generateSnapshot(filename, ctx);
			File file = new File(ctx.getFilesDir(), filename);
			ConversationDAO conversation = new ConversationDAO();
			conversation.setName(filename);
			conversation.setPath(file.getPath());
			conversation.setPathSnapshot(file.getPath());
			datasourceConversation.add(conversation);
			Toast.makeText(this, "LA CONVERSACIÓN SE HA GUARDADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
		}
	}

	public void generateSnapshot(String filename, Context ctx) {
		Bitmap bitmap = screenShot(scenario);
		ImageUtils.saveFileInternalStorage(filename, bitmap, ctx, 0);		
	}
	
	public Bitmap screenShot(View view) {
		view.setDrawingCacheEnabled(true); 
		Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); 
		view.setDrawingCacheEnabled(false);
	    Canvas canvas = new Canvas(bitmap);
	    view.draw(canvas);
	    return bitmap;
	}

	@Override
	public void onDialogPositiveClickInsertImageDialogListener(
			InsertImageDialogFragment insertImageDialogFragment) {
		Toast.makeText(this, "SELECCIONE UNA IMAGEN", Toast.LENGTH_LONG).show();
		//TODO: ver porque cierra el dialog
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}  
	
	@Override
	public void onBackPressed() {
		if (backPressed + 2000 > System.currentTimeMillis()) {
			super.onBackPressed();
		} else {
			Toast.makeText(getBaseContext(), "PARA SALIR, PRESIONA ATRÁS UNA VEZ MÁS!", Toast.LENGTH_SHORT).show();
			backPressed = System.currentTimeMillis();
		}
	}

	@Override
	public void onDialogPositiveClickExitConversationConfirmationListener(DialogFragment dialog) {
		super.onBackPressed();
	}
	
}

