package ar.uba.fi.talker.dataSource;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import ar.uba.fi.talker.dto.TalkerDTO;

public abstract class TalkerDataSource {
	
	private ResourceSQLiteHelper dbHelper;
	
	public TalkerDataSource(Context context) {
		this.dbHelper = new ResourceSQLiteHelper(context);
	}

	protected ResourceSQLiteHelper getDbHelper() {
		return dbHelper;
	}
	
	/**
	 * Returns an entity if exists, null otherwise.
	 * 
	 * @param id
	 * @return TalkerDTO entity
	 */
	public abstract TalkerDTO get(long id);
	
	/**
	 * Returns all entities of T.
	 * 
	 * @return List<T>
	 */
	public abstract List<TalkerDTO> getAll();

	/**
	 * Adds new entity.
	 * 
	 * @param object
	 * @return long new id
	 */
	public abstract long add(TalkerDTO entity);

	/**
	 * Updates entity.
	 * 
	 * @param entity
	 */
	public abstract void update(TalkerDTO entity);
	
	/**
	 * Deletes entity.
	 * 
	 * @param entity to delete
	 */
	public void delete(long entityId) {
		SQLiteDatabase database = getDbHelper().getWritableDatabase();
			database.delete(this.getTableName(),
					this.getIdColumnName() + " = " + entityId, null);
			database.close();
	}

	protected abstract String getTableName();
	
	protected abstract String getIdColumnName();
}
