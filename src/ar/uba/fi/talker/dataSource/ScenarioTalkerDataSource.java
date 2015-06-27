package ar.uba.fi.talker.dataSource;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ar.uba.fi.talker.dao.ScenarioDAO;

public class ScenarioTalkerDataSource extends TalkerDataSource<ScenarioDAO> {

	private String[] allColumns = { ResourceSQLiteHelper.SCENARIO_COLUMN_ID,
			ResourceSQLiteHelper.SCENARIO_COLUMN_PATH,
			ResourceSQLiteHelper.SCENARIO_COLUMN_NAME };
	
	public ScenarioTalkerDataSource(Context context) {
		super(context);
	}
	
	@Override
	public long add(ScenarioDAO scenario) {
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.SCENARIO_COLUMN_PATH, scenario.getPath());
		values.put(ResourceSQLiteHelper.SCENARIO_COLUMN_NAME, scenario.getName());
		
		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		long insertId = database.insert(ResourceSQLiteHelper.SCENARIO_TABLE, null, values);
		database.close();
		return insertId;
	}
	
	@Override
	public ScenarioDAO get(long id) {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.SCENARIO_TABLE, allColumns,
				ResourceSQLiteHelper.SCENARIO_COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		ScenarioDAO scenario = cursorToScenario(cursor);
		cursor.close();
		database.close();
		return scenario;
	}
	
	@Override
	public void update(ScenarioDAO scenario) {
		ContentValues values = new ContentValues();
		values.put(ResourceSQLiteHelper.SCENARIO_COLUMN_NAME, scenario.getName());

		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		database.update(ResourceSQLiteHelper.SCENARIO_TABLE, values,
				ResourceSQLiteHelper.SCENARIO_COLUMN_ID + " = " + scenario.getId(), null);
		database.close();
	}
	
	@Override
	public void delete(ScenarioDAO scenario) {
		SQLiteDatabase database = getDbHelper().getWritableDatabase();
		database.delete(ResourceSQLiteHelper.SCENARIO_TABLE,
				ResourceSQLiteHelper.SCENARIO_COLUMN_ID + " = " + scenario.getId(), null);
		database.close();
	}

	@Override
	public List<ScenarioDAO> getAll() {
		List<ScenarioDAO> images = new ArrayList<ScenarioDAO>();

		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.query(ResourceSQLiteHelper.SCENARIO_TABLE,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ScenarioDAO scenario = cursorToScenario(cursor);
			images.add(scenario);
			cursor.moveToNext();
		}
		cursor.close();
		database.close();
		return images;
	}

	private ScenarioDAO cursorToScenario(Cursor cursor) {
		ScenarioDAO scenario = new ScenarioDAO();
		scenario.setId(cursor.getInt(0));
		scenario.setPath(cursor.getString(1));
		scenario.setName(cursor.getString(2));
		return scenario;
	}

	public long getLastId() {
		SQLiteDatabase database = getDbHelper().getReadableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ ResourceSQLiteHelper.SCENARIO_TABLE + " ORDER BY "
				+ ResourceSQLiteHelper.SCENARIO_COLUMN_ID + " DESC LIMIT 1", null);
		cursor.moveToFirst();
		ScenarioDAO scenario = cursorToScenario(cursor);
		cursor.close();
		database.close();
		return scenario.getId();
	}

}
