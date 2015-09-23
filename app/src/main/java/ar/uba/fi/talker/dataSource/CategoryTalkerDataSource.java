package ar.uba.fi.talker.dataSource;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.uba.fi.talker.dao.CategoryDAO;
import ar.uba.fi.talker.dao.ImageDAO;
import ar.uba.fi.talker.dto.TalkerDTO;

public class CategoryTalkerDataSource extends TalkerDataSource {
	
	public CategoryTalkerDataSource(Context context) {
		super(context);
	}

	@Override
	protected String getTableName() {
		return ResourceSQLiteHelper.CATEGORY_TABLE;
	}
	
	@Override
	protected String getIdColumnName() {
		return ResourceSQLiteHelper.CATEGORY_COLUMN_ID;
	}
	
	public CategoryDAO createCategory(String name, int isContactCategory) {
		
		CategoryDAO categoryDAO = new CategoryDAO();
		categoryDAO.setName(name);
		categoryDAO.setContactoCategory(isContactCategory);
		
		long id = this.add(categoryDAO);
		categoryDAO.setId(id);
		return categoryDAO;
	}

	private CategoryDAO cursorToCategory(Cursor cursor) {
		CategoryDAO category = new CategoryDAO();
		category.setId(cursor.getInt(0));
		category.setName(cursor.getString(1));
		return category;
	}
	
	public List<CategoryDAO> getImageCategories() {
		List<CategoryDAO> categories = new ArrayList<CategoryDAO>();

		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.CATEGORY_TABLE, new String[]{"*"},
				ResourceSQLiteHelper.CATEGORY_COLUMN_IS_CONTACT + " = " + ResourceSQLiteHelper.FALSE, null, null, null, 
				ResourceSQLiteHelper.CATEGORY_COLUMN_NAME + " ASC ", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryDAO category = cursorToCategory(cursor);
			ImageDAO image = getFirstImagesForCategory(category.getId());
			category.setImage(image);
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return categories;
	}

	public ImageDAO getFirstImagesForCategory(long keyId) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.IMAGE_TABLE, new String[]{"*"},
				ResourceSQLiteHelper.IMAGE_COLUMN_IDCATEGORY + " = " + keyId, null, null, null, 
				ResourceSQLiteHelper.IMAGE_COLUMN_ID, "1");
		cursor.moveToFirst();
		
		ImageDAO image = null;
		if (!cursor.isAfterLast()) {
			image = cursorToImages(cursor);
		}
		cursor.close();
		database.close();
		return image;
	}
	
	private ImageDAO cursorToImages(Cursor cursor) {
		ImageDAO image = new ImageDAO();
		image.setId(cursor.getInt(0));
		image.setPath(cursor.getString(1));
		image.setName(cursor.getString(2));
		image.setIdCategory(cursor.getInt(3));
		return image;
	}

	@Override
	public CategoryDAO get(long id) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.CATEGORY_TABLE, new String[]{"*"},
				ResourceSQLiteHelper.CATEGORY_COLUMN_ID + " = " + id, null, null, null, 
				ResourceSQLiteHelper.CATEGORY_COLUMN_ID, "1");
		cursor.moveToFirst();
		CategoryDAO category = cursorToCategory(cursor);
		cursor.close();
		database.close();
		return category;
	}

	@Override
	public List<TalkerDTO> getAll() {
		List<TalkerDTO> categories = new ArrayList<TalkerDTO>();

		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.CATEGORY_TABLE, new String[] { "*" },
				ResourceSQLiteHelper.CATEGORY_COLUMN_IS_CONTACT + " = " + ResourceSQLiteHelper.TRUE, null, null, null,
				ResourceSQLiteHelper.CATEGORY_COLUMN_ID, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryDAO category = cursorToCategory(cursor);
			ImageDAO image = getFirstImagesForCategory(category.getId());
			category.setImage(image);
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return categories;
	}

	public List<CategoryDAO> getAllContacts() {
		List<CategoryDAO> categories = new ArrayList<CategoryDAO>();

		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.CATEGORY_TABLE, new String[] { "*" },
				ResourceSQLiteHelper.CATEGORY_COLUMN_IS_CONTACT + " = " + ResourceSQLiteHelper.TRUE, null, null, null,
				ResourceSQLiteHelper.CATEGORY_COLUMN_ID, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CategoryDAO category = cursorToCategory(cursor);
			ImageDAO image = getFirstImagesForCategory(category.getId());
			category.setImage(image);
			categories.add(category);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return categories;
	}
	
	@Override
	public long add(TalkerDTO entity) {
		CategoryDAO categoryDAO = null;
		if (entity instanceof CategoryDAO) {
			categoryDAO = (CategoryDAO) entity;
		} else {
			throw new InvalidParameterException();
		}
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.CATEGORY_COLUMN_NAME, categoryDAO.getName());
		values.put(ResourceSQLiteHelper.CATEGORY_COLUMN_IS_CONTACT, categoryDAO.isContactCategory());

		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		long insertId = database.insert(ResourceSQLiteHelper.CATEGORY_TABLE, null, values);
		database.close();
		return insertId;
	}

	@Override
	public void update(TalkerDTO entity) {
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.CATEGORY_COLUMN_NAME, entity.getName());

		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		database.update(ResourceSQLiteHelper.CATEGORY_TABLE, values,
				ResourceSQLiteHelper.CATEGORY_COLUMN_ID + " = " + entity.getId(), null);
		database.close();
	}

}
