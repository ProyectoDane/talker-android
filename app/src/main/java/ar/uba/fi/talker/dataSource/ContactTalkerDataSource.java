package ar.uba.fi.talker.dataSource;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.uba.fi.talker.dao.ContactDAO;
import ar.uba.fi.talker.dto.TalkerDTO;

public class ContactTalkerDataSource extends TalkerDataSource {
	
	public ContactTalkerDataSource(Context context) {
		super(context);
	}

	private ContactDAO cursorToContact(Cursor cursor) {
		ContactDAO contact = new ContactDAO();
		contact.setId(cursor.getLong(0));
		contact.setImageId(cursor.getLong(1));
		contact.setPhone(cursor.getString(2));
		contact.setAddress(cursor.getString(3));
		return contact;
	}
	
	
	public ContactDAO getContactByImageID(long keyId) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ ResourceSQLiteHelper.CONTACT_TABLE + " WHERE "
				+ ResourceSQLiteHelper.CONTACT_COLUMN_IMAGE_ID + " = " + keyId, null);
		cursor.moveToFirst();
		ContactDAO contact = cursorToContact(cursor);
		cursor.close();
		database.close();
		return contact;
	}

	@Override
	public TalkerDTO get(long id) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ ResourceSQLiteHelper.CONTACT_TABLE + " WHERE "
				+ ResourceSQLiteHelper.CONTACT_COLUMN_ID + " = " + id, null);
		cursor.moveToFirst();
		ContactDAO contact = cursorToContact(cursor);
		cursor.close();
		database.close();
		return contact;
	}


	@Override
	public List<TalkerDTO> getAll() {
		List<TalkerDTO> contacts = new ArrayList<TalkerDTO>();
		
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ ResourceSQLiteHelper.CONTACT_TABLE, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ContactDAO contact = cursorToContact(cursor);
			contacts.add(contact);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return contacts;
	}


	@Override
	public long add(TalkerDTO entity) {
		ContactDAO contact = null;
		if (entity instanceof ContactDAO) {
			contact = (ContactDAO) entity;
		} else {
			throw new InvalidParameterException();
		}

		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.CONTACT_COLUMN_IMAGE_ID, contact.getImageId());
		values.put(ResourceSQLiteHelper.CONTACT_COLUMN_PHONE, contact.getPhone());
		values.put(ResourceSQLiteHelper.CONTACT_COLUMN_ADDRESS, contact.getAddress());
		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		long insertId = database.insert(ResourceSQLiteHelper.CONTACT_TABLE, null, values);
		database.close();
		contact.setId(insertId);
		return insertId;
	}


	@Override
	public void update(TalkerDTO entity) {
		ContactDAO contact = null;
		if (entity instanceof ContactDAO) {
			contact = (ContactDAO) entity;
		} else {
			throw new InvalidParameterException();
		}
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.CONTACT_COLUMN_PHONE, contact.getPhone());
		values.put(ResourceSQLiteHelper.CONTACT_COLUMN_ADDRESS, contact.getAddress());

		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		database.update(this.getTableName(), values,
				this.getIdColumnName() + " = " + contact.getId(), null);
		database.close();
	}


	@Override
	protected String getTableName() {
		return ResourceSQLiteHelper.CONTACT_TABLE;
	}


	@Override
	protected String getIdColumnName() {
		return ResourceSQLiteHelper.CONTACT_COLUMN_ID;
	}
}

