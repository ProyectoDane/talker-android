package ar.uba.fi.talker.dataSource;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.uba.fi.talker.dao.ConversationDAO;
import ar.uba.fi.talker.dto.TalkerDTO;

public class ConversationTalkerDataSource extends TalkerDataSource {

	@Override
	protected String getTableName() {
		return ResourceSQLiteHelper.CONVERSATION_TABLE;
	}
	
	@Override
	protected String getIdColumnName() {
		return ResourceSQLiteHelper.CONVERSATION_COLUMN_ID;
	}
	private final String[] allColumns = { ResourceSQLiteHelper.CONVERSATION_COLUMN_ID,
			ResourceSQLiteHelper.CONVERSATION_COLUMN_PATH,
			ResourceSQLiteHelper.CONVERSATION_COLUMN_NAME, 
			ResourceSQLiteHelper.CONVERSATION_COLUMN_SNAPSHOT };

	public ConversationTalkerDataSource(Context context) {
		super(context);
	}

	private ConversationDAO cursorToConversation(Cursor cursor) {
		ConversationDAO conversation = new ConversationDAO();
		conversation.setId(cursor.getInt(0));
		conversation.setPath(cursor.getString(1));
		conversation.setName(cursor.getString(2));
		conversation.setPathSnapshot(cursor.getString(3));
		return conversation;
	}

	@Override
	public ConversationDAO get(long id) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ ResourceSQLiteHelper.CONVERSATION_TABLE + " WHERE "
				+ ResourceSQLiteHelper.CONVERSATION_COLUMN_ID + " = " + id, null);
		cursor.moveToFirst();
		ConversationDAO conversationDAO = cursorToConversation(cursor);
		cursor.close();
		database.close();
		return conversationDAO;
	}

	@Override
	public List<TalkerDTO> getAll() {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		List<TalkerDTO> conversations = new ArrayList<TalkerDTO>();

		Cursor cursor = database.query(ResourceSQLiteHelper.CONVERSATION_TABLE,
				allColumns, null, null, null, null, ResourceSQLiteHelper.CONVERSATION_COLUMN_ID + " DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ConversationDAO conversationDAO = cursorToConversation(cursor);
			conversations.add(conversationDAO);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return conversations;
	}

	@Override
	public long add(TalkerDTO entity) {
		ConversationDAO conversationDAO = null;
		if (entity instanceof ConversationDAO) {
			conversationDAO = (ConversationDAO) entity;
		} else {
			throw new InvalidParameterException();
		}
		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.CONVERSATION_COLUMN_PATH, entity.getPath());
		values.put(ResourceSQLiteHelper.CONVERSATION_COLUMN_NAME, entity.getName());
		values.put(ResourceSQLiteHelper.CONVERSATION_COLUMN_SNAPSHOT, conversationDAO.getPathSnapshot());
		long insertId = database.insert(ResourceSQLiteHelper.CONVERSATION_TABLE, null, values);
		database.close();
		return insertId;
	}

	@Override
	public void update(TalkerDTO entity) {
		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.CONVERSATION_COLUMN_NAME, entity.getName());
		database.update(ResourceSQLiteHelper.CONVERSATION_TABLE, values,
				ResourceSQLiteHelper.CONVERSATION_COLUMN_ID + " = " + entity.getId(), null);
		database.close();
	}

}
