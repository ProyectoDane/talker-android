package ar.uba.fi.talker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.uba.fi.talker.component.ComponentType;
import ar.uba.fi.talker.component.Setting;
import ar.uba.fi.talker.dao.ConversationTalkerDataSource;
import ar.uba.fi.talker.dao.ScenarioDAO;
import ar.uba.fi.talker.dao.ScenarioTalkerDataSource;
import ar.uba.fi.talker.dao.SettingTalkerDataSource;
import ar.uba.fi.talker.fragment.CalculatorFragment;
import ar.uba.fi.talker.fragment.DatePickerFragment;
import ar.uba.fi.talker.fragment.DatePickerFragment.DatePickerDialogListener;
import ar.uba.fi.talker.fragment.EraseAllConfirmationDialogFragment;
import ar.uba.fi.talker.fragment.EraseAllConfirmationDialogFragment.EraseAllConfirmationDialogListener;
import ar.uba.fi.talker.fragment.InsertImageDialogFragment;
import ar.uba.fi.talker.fragment.InsertImageDialogFragment.InsertImageDialogListener;
import ar.uba.fi.talker.fragment.SaveAllConfirmationDialogFragment;
import ar.uba.fi.talker.fragment.SaveAllConfirmationDialogFragment.SaveAllConfirmationDialogListener;
import ar.uba.fi.talker.fragment.TextDialogFragment;
import ar.uba.fi.talker.fragment.TextDialogFragment.TextDialogListener;
import ar.uba.fi.talker.paint.PaintManager;
import ar.uba.fi.talker.utils.ImageUtils;
import ar.uba.fi.talker.view.Scenario;

public class CanvasActivity extends ActionBarActivity implements
		TextDialogListener, InsertImageDialogListener,
		EraseAllConfirmationDialogListener, OnDateSetListener, 
		DatePickerDialogListener, SaveAllConfirmationDialogListener {

	final String TAG = "CanvasActivity";

	private Scenario scenario;
	
	private SettingTalkerDataSource settingDatasource;
	private ConversationTalkerDataSource datasourceConversation;
	private ScenarioTalkerDataSource datasource;
	
	private static int RESULT_LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.canvas_default);
		
		//seteo de configuracion
		settingDatasource = new SettingTalkerDataSource(this);
		settingDatasource.open();
		
		Setting settings = settingDatasource.getSettings();
		PaintManager.setSettings(settings);
		
		scenario = (Scenario) this.findViewById(R.id.gestureOverlayView1);
		if(getIntent().hasExtra("BMP")) {
		    Bundle extras = getIntent().getExtras();
		    byte[] bytes = extras.getByteArray("BMP");
		    Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		    
		    scenario.setBackgroundImage(image);
		} else if (getIntent().hasExtra("position")) {
			int imageViewId = getIntent().getExtras().getInt("position");
			datasource = new ScenarioTalkerDataSource(this.getApplicationContext());
			datasource.open();
			ScenarioDAO scenariodao = datasource.getScenarioByID(imageViewId);
			datasource.close();
			long imageDatasourceID = scenariodao.getIdCode();
			byte[] bytes = null;
			Bitmap image = null;
			if (imageDatasourceID != 0){
				bytes = ImageUtils.transformImage(getResources(), imageDatasourceID); 
			    image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			} else {
				image = ImageUtils.getImageBitmap(this, scenariodao.getPath());
			}
		    scenario.setBackgroundImage(image);
		}

		ImageButton pencilOp = (ImageButton) findViewById(R.id.pencilOption);
		pencilOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.PENCIL);
				scenario.invalidate();
			}
		});

		ImageButton eraserOp = (ImageButton) findViewById(R.id.eraserOption);
		eraserOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.ERASER);
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
			}
		});

		ImageButton textOp = (ImageButton) findViewById(R.id.textOption);
		textOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new TextDialogFragment();
				newFragment.show(getSupportFragmentManager(), "insert_text");
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
			}
		});

		ImageButton calcOption = (ImageButton) findViewById(R.id.calculatorOption);
		calcOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new CalculatorFragment();
				newFragment.show(getSupportFragmentManager(), "calculator");
			}
		});

		ImageButton calendarOption = (ImageButton) findViewById(R.id.calendarOption);
		calendarOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(getSupportFragmentManager(), "calendar");
			}
		});
		
		ImageButton saveOption = (ImageButton) findViewById(R.id.saveAll);
		saveOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new SaveAllConfirmationDialogFragment();
				newFragment.show(getSupportFragmentManager(), "save_all");
			}
		});
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
	public void onDialogPositiveClickInsertImageDialogListener(Uri uri, Matrix matrix) {
		
		try {
			Bitmap ima1 = Media.getBitmap(this.getContentResolver(), uri);
			scenario.addImage(Bitmap.createBitmap(ima1, 0, 0, ima1.getWidth(), ima1.getHeight(), matrix, true));
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
	public void onDialogPositiveClickInsertImageDialogListener(Bitmap bitmap) {
		scenario.addImage(bitmap);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
			Uri selectedImage = data.getData();

			String[] filePathColumn = { MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION };

			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int orientation = -1;
			if (cursor != null && cursor.moveToFirst()) {
				orientation = cursor.getInt(cursor.getColumnIndex(filePathColumn[1]));
			}
			Matrix matrix = new Matrix();
			matrix.postRotate(orientation);
			cursor.close();

			this.onDialogPositiveClickInsertImageDialogListener(selectedImage, matrix);

		}
		super.onActivityResult(requestCode, resultCode, data);
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
		datasourceConversation.open();
		if (filename != null) {
			Context ctx = this.getApplicationContext();
			generateSnapshot(filename, ctx);
			File file = new File(ctx.getFilesDir(), filename);
			datasourceConversation.createConversation(file.getPath() + ".json", filename, file.getPath());
		}
	}

	public void generateSnapshot(String filename, Context ctx) {
		Bitmap bitmap = screenShot(scenario);
		ImageUtils.saveFileInternalStorage(filename, bitmap, ctx);		
	}
	
	public Bitmap screenShot(View view) {
	    Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(), Config.ARGB_8888);
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
	protected void onDestroy() {
		super.onDestroy();
		datasource.close();
		if (datasourceConversation != null ) {
			datasourceConversation.close();
		}
	}

}

