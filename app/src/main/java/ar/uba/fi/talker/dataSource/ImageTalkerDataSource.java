package ar.uba.fi.talker.dataSource;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dto.TalkerDTO;

public class ImageTalkerDataSource extends TalkerDataSource {
	
	private final String[] allColumns = { ResourceSQLiteHelper.IMAGE_COLUMN_ID, ResourceSQLiteHelper.IMAGE_COLUMN_PATH,
			ResourceSQLiteHelper.IMAGE_COLUMN_NAME, ResourceSQLiteHelper.IMAGE_COLUMN_IDCATEGORY };

	public ImageTalkerDataSource(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return ResourceSQLiteHelper.IMAGE_TABLE;
	}
	
	@Override
	protected String getIdColumnName() {
		return ResourceSQLiteHelper.IMAGE_COLUMN_ID;
	}
	
	public long getLastImageID() {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.IMAGE_TABLE, new String[]{"*"},
				null, null, null, null, ResourceSQLiteHelper.IMAGE_COLUMN_ID + " DESC ", "1");
		cursor.moveToFirst();
		ImageDAO image = cursorToImages(cursor);
		cursor.close();
		database.close();
		return image.getId();
	}
	
	public ImageDAO createImage(String path, String name, long idCategory) {
		ImageDAO entity = new ImageDAO();
		entity.setIdCategory(idCategory);
		entity.setName(name);
		entity.setPath(path);
		
		long id = this.add(entity);
		entity.setId(id);
		return entity;
	}

	public List<ImageDAO> getAllImages() {
		List<ImageDAO> images = new ArrayList<ImageDAO>();

		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.IMAGE_TABLE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ImageDAO image = cursorToImages(cursor);
			images.add(image);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return images;
	}

	private ImageDAO cursorToImages(Cursor cursor) {
		ImageDAO image = new ImageDAO();
		image.setId(cursor.getInt(0));
		image.setPath(cursor.getString(1));
		image.setName(cursor.getString(2));
		image.setIdCategory(cursor.getInt(3));
		return image;
	}
	
	public List<ImageDAO> getImagesForCategory(long keyId) {
		List<ImageDAO> images = new ArrayList<ImageDAO>();

		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.IMAGE_TABLE, new String[]{"*"},
				ResourceSQLiteHelper.IMAGE_COLUMN_IDCATEGORY + " = " + keyId, null, null, null, 
				ResourceSQLiteHelper.IMAGE_COLUMN_NAME + " ASC ", null);	
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ImageDAO image = cursorToImages(cursor);
			images.add(image);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return images;
	}

	@Override
	public ImageDAO get(long id) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.IMAGE_TABLE, new String[]{"*"},
				ResourceSQLiteHelper.IMAGE_COLUMN_ID + " = " + id, null, null, null, null, null);
		cursor.moveToFirst();
		ImageDAO image = cursorToImages(cursor);
		cursor.close();
		database.close();
		return image;
	}

	@Override
	public List<TalkerDTO> getAll() {
		List<TalkerDTO> images = new ArrayList<TalkerDTO>();

		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.IMAGE_TABLE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ImageDAO image = cursorToImages(cursor);
			images.add(image);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return images;
	}

	@Override
	public long add(TalkerDTO entity) {
		ImageDAO imageDAO = null;
		if (entity instanceof ImageDAO) {
			imageDAO = (ImageDAO) entity;
		} else {
			throw new InvalidParameterException();
		}
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.IMAGE_COLUMN_PATH, entity.getPath());
		values.put(ResourceSQLiteHelper.IMAGE_COLUMN_NAME, entity.getName());
		values.put(ResourceSQLiteHelper.IMAGE_COLUMN_IDCATEGORY, imageDAO.getIdCategory());

		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		long insertId = database.insert(ResourceSQLiteHelper.IMAGE_TABLE, null, values);
		database.close();
		entity.setId(insertId);
		return insertId;
	}

	@Override
	public void update(TalkerDTO entity) {
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.IMAGE_COLUMN_NAME, entity.getName());
		values.put(ResourceSQLiteHelper.IMAGE_COLUMN_PATH, entity.getPath());

		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		database.update(this.getTableName(), values,
				this.getIdColumnName() + " = " + entity.getId(), null);
		database.close();
	}

}
