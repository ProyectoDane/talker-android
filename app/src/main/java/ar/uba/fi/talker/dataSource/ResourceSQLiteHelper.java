package ar.uba.fi.talker.dataSource;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ar.uba.fi.talker.R;

public class ResourceSQLiteHelper extends SQLiteOpenHelper {

	//SQL sentence to create Images table
    
	private final Context context;
	private static final String DATABASE_NAME = "talker.db";
	private static final int DATABASE_VERSION = 1;
	
	//SQLite does not have a separate Boolean storage class. Instead, Boolean values are stored as integers 0 (false) and 1 (true).
	public static int FALSE = 0;
	public static int TRUE = 1;
	
	private static int CATEGORY_ID = 1;
	
	//SCENARIO TABLE
	public static final String SCENARIO_TABLE = "scenario";
	public static final String SCENARIO_COLUMN_ID = "id";
	public static final String SCENARIO_COLUMN_PATH = "path";
	public static final String SCENARIO_COLUMN_NAME = "name";

	//CATEGORY TABLE
	public static final String CATEGORY_TABLE = "category";
	public static final String CATEGORY_COLUMN_ID = "id";
	public static final String CATEGORY_COLUMN_NAME = "name";
	public static final String CATEGORY_COLUMN_IS_CONTACT = "is_contact_category";
	
	//IMAGE TABLE
	public static final String IMAGE_TABLE = "image";
	public static final String IMAGE_COLUMN_ID = "id";
	public static final String IMAGE_COLUMN_IDCATEGORY = "id_category";
	public static final String IMAGE_COLUMN_PATH = "path";
	public static final String IMAGE_COLUMN_NAME = "name";
	public static final String IMAGE_COLUMN_IS_CONTACT = "is_contact";
	
	//CONVERSATION TABLE
	public static final String CONVERSATION_TABLE = "conversation";
	public static final String CONVERSATION_COLUMN_ID = "id";
	public static final String CONVERSATION_COLUMN_PATH = "path";
	public static final String CONVERSATION_COLUMN_NAME = "name";
	public static final String CONVERSATION_COLUMN_SNAPSHOT = "path_snapshot";
	
	//CONTACT TABLE
	public static final String CONTACT_TABLE = "contact";
	public static final String CONTACT_COLUMN_ID = "id";
	public static final String CONTACT_COLUMN_IMAGE_ID = "id_image";
	public static final String CONTACT_COLUMN_ADDRESS = "address";
	public static final String CONTACT_COLUMN_PHONE = "phone";
	
	public ResourceSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	
	public boolean checkDataBase() {
		File database = context.getDatabasePath(DATABASE_NAME);

		if (!database.exists()) {
			return false;
		}
		return true;
	}

	boolean isTableExists(SQLiteDatabase db, String tableName) {
		if (tableName == null || db == null || !db.isOpen()) {
			return false;
		}
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] { "table", tableName });
		if (!cursor.moveToFirst()) {
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();
		return count > 0;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Execute SQL sentence to create table
		if (checkDataBase()) {
			if (!isTableExists(db, SCENARIO_TABLE)) {
				db.execSQL("CREATE TABLE " + SCENARIO_TABLE + " ( "
						+ SCENARIO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ SCENARIO_COLUMN_PATH + " TEXT, "
						+ SCENARIO_COLUMN_NAME + " TEXT)");
				for (int i = 0; i < mThumbIdsScenario.length; i++) {
					// Generate and insert default data
					int idCode = mThumbIdsScenario[i];
					String name = context.getResources().getString(mThumbTextsScenario[i]);

					db.execSQL("INSERT INTO " + SCENARIO_TABLE + " ( "
							+ SCENARIO_COLUMN_PATH + " , " + SCENARIO_COLUMN_NAME + " ) "
							+ " VALUES (" + idCode + ", '" + name + "')");
				}
			}
			
			if (!isTableExists(db, CATEGORY_TABLE)) {
				if (!isTableExists(db, CATEGORY_TABLE)) {
					db.execSQL("CREATE TABLE " + CATEGORY_TABLE + " ( "
							+ CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
							+ IMAGE_COLUMN_NAME + " TEXT, "
							+ CATEGORY_COLUMN_IS_CONTACT + " INTEGER DEFAULT 0)");
				}
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"OBJETOS PERSONALES\")");
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"COMESTIBLES\")");
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"ANIMALES\")");
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"VARIOS\")");
				
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " , " + CATEGORY_COLUMN_IS_CONTACT + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"FAMILIA\", "+ TRUE +")");
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " , " + CATEGORY_COLUMN_IS_CONTACT + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"AMIGOS\", "+ TRUE +")");
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " , " + CATEGORY_COLUMN_IS_CONTACT + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"FAMOSOS\", "+ TRUE +")");
				db.execSQL("INSERT INTO " + CATEGORY_TABLE + " ( " + CATEGORY_COLUMN_ID + " , " + CATEGORY_COLUMN_NAME + " , " + CATEGORY_COLUMN_IS_CONTACT + " ) "
						+ " VALUES (" + CATEGORY_ID++ + ",\"PROFESIONALES\", "+ TRUE +")");

			}

			if (!isTableExists(db, IMAGE_TABLE)) {
				db.execSQL("CREATE TABLE " + IMAGE_TABLE + " ( "
						+ IMAGE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ IMAGE_COLUMN_PATH + " TEXT, "
						+ IMAGE_COLUMN_NAME + " TEXT, "
						+ IMAGE_COLUMN_IDCATEGORY + " INTEGER,"
						+ " FOREIGN KEY(" + IMAGE_COLUMN_IDCATEGORY
						+ ") REFERENCES " + CATEGORY_TABLE + "("
						+ CATEGORY_COLUMN_ID + ") ON DELETE CASCADE)");
				
				populateImageCategory(db, mThumbIdsImagesForCateg1, mThumbTextsImagesForCateg1, 1, Boolean.FALSE);
				populateImageCategory(db, mThumbIdsImagesForCateg2, mThumbTextsImagesForCateg2, 2, Boolean.FALSE);
				populateImageCategory(db, mThumbIdsImagesForCateg3, mThumbTextsImagesForCateg3, 3, Boolean.FALSE);
				populateImageCategory(db, mThumbIdsImagesForCateg4, mThumbTextsImagesForCateg4, 4, Boolean.FALSE);
			}
			
			if (!isTableExists(db, CONVERSATION_TABLE)) {
				db.execSQL("CREATE TABLE " + CONVERSATION_TABLE + " ( "
						+ CONVERSATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ CONVERSATION_COLUMN_PATH + " TEXT, "
						+ CONVERSATION_COLUMN_NAME + " TEXT, "
						+ CONVERSATION_COLUMN_SNAPSHOT + " TEXT )");
			}

			if (!isTableExists(db, CONTACT_TABLE)) {
				db.execSQL("CREATE TABLE " + CONTACT_TABLE + " ( "
						+ CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						+ CONTACT_COLUMN_IMAGE_ID + " INTEGER , "
						+ CONTACT_COLUMN_PHONE + " TEXT, "
						+ CONTACT_COLUMN_ADDRESS + " TEXT,"
						+ " FOREIGN KEY("
						+ CONTACT_COLUMN_IMAGE_ID + ") REFERENCES "
						+ IMAGE_TABLE + "(" + IMAGE_COLUMN_ID
						+ ") ON DELETE CASCADE)");

				
				populateImageCategory(db, mThumbIdsImagesForCateg5, mThumbTextsImagesForCateg5, 5, Boolean.TRUE);
				populateImageCategory(db, mThumbIdsImagesForCateg6, mThumbTextsImagesForCateg6, 6, Boolean.FALSE);
				populateImageCategory(db, mThumbIdsImagesForCateg7, mThumbTextsImagesForCateg7, 7, Boolean.FALSE);
				populateContactCategory(db, 21);
				populateImageCategory(db, mThumbIdsImagesForCateg8, mThumbTextsImagesForCateg8, 8, Boolean.FALSE);
			}
		}
	}

	private void populateContactCategory(SQLiteDatabase db, int categId) {
			db.execSQL("INSERT INTO " + CONTACT_TABLE + " ( "
					+ CONTACT_COLUMN_IMAGE_ID + " ) "
					+ " VALUES (" + categId + ")");
	}

	private void populateImageCategory(SQLiteDatabase db, Integer[] mThumbIdsImagesForCateg, Integer[] mThumbTextsImagesForCateg, int categId, boolean isContact) {
		for (int i = 0; i < mThumbIdsImagesForCateg.length; i++) {
			int idCode = mThumbIdsImagesForCateg[i];
			String name = context.getResources().getString(mThumbTextsImagesForCateg[i]);
			db.execSQL("INSERT INTO " + IMAGE_TABLE + " ( "
					+ IMAGE_COLUMN_PATH + " , " 
					+ IMAGE_COLUMN_IDCATEGORY + " , " + IMAGE_COLUMN_NAME + " ) "
					+ " VALUES (" + idCode + ", " + categId + ", '" + name + "')");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ResourceSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + SCENARIO_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORY_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CONVERSATION_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CONTACT_TABLE);
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		db.execSQL("PRAGMA foreign_keys=ON");
	}
	
	/*IMAGES OF SCENARIO*/
	private static Integer[] mThumbIdsScenario = {
		R.drawable.blanco,
		R.drawable.esc_1,
		R.drawable.esc_2,
		R.drawable.esc_3,
		R.drawable.esc_4,
		R.drawable.esc_5,
		R.drawable.esc_6,
		R.drawable.esc_7,
		R.drawable.esc_8
	};
	
	private static Integer[] mThumbTextsScenario = {
		R.string.blanco,
		R.string.esc_1,
		R.string.esc_2,
		R.string.esc_3,
		R.string.esc_4,
		R.string.esc_5,
		R.string.esc_6,
		R.string.esc_7,
		R.string.esc_8
	};
	
	/*IMAGES OF IMAGE CATEGORIES*/
	private static Integer[] mThumbIdsImagesForCateg1 = {
		R.drawable.obj_1,
		R.drawable.obj_2,
		R.drawable.obj_3,
		R.drawable.obj_4,
		R.drawable.obj_5
	};
	private static Integer[] mThumbTextsImagesForCateg1 = {
		R.string.obj_1,
		R.string.obj_2,
		R.string.obj_3,
		R.string.obj_4,
		R.string.obj_5
	};
	private static Integer[] mThumbIdsImagesForCateg2 = {
		R.drawable.com_1,
		R.drawable.com_2,
		R.drawable.com_3,
		R.drawable.com_4,
		R.drawable.com_5
	};
	private static Integer[] mThumbTextsImagesForCateg2 = {
		R.string.com_1,
		R.string.com_2,
		R.string.com_3,
		R.string.com_4,
		R.string.com_5
	};
	private static Integer[] mThumbIdsImagesForCateg3 = {
		R.drawable.ani_1,
		R.drawable.ani_2,
		R.drawable.ani_3,
		R.drawable.ani_4,
		R.drawable.ani_5
	};
	private static Integer[] mThumbTextsImagesForCateg3 = {
		R.string.ani_1,
		R.string.ani_2,
		R.string.ani_3,
		R.string.ani_4,
		R.string.ani_5
	};
	private static Integer[] mThumbIdsImagesForCateg4 = {
		R.drawable.var_1,
		R.drawable.var_2,
		R.drawable.var_3,
		R.drawable.var_4,
		R.drawable.var_5
	};
	private static Integer[] mThumbTextsImagesForCateg4 = {
		R.string.var_1,
		R.string.var_2,
		R.string.var_3,
		R.string.var_4,
		R.string.var_5
	};
	
	
	/*IMAGES OF CONTACT CATEGORIES*/
	private static Integer[] mThumbIdsImagesForCateg5 = {
	};
	private static Integer[] mThumbTextsImagesForCateg5 = {
	};
	private static Integer[] mThumbIdsImagesForCateg6 = {
	};
	private static Integer[] mThumbTextsImagesForCateg6 = {
	};
	private static Integer[] mThumbIdsImagesForCateg7 = {
		R.drawable.famo_1
	};
	private static Integer[] mThumbTextsImagesForCateg7 = {
		R.string.famo_1
	};
	private static Integer[] mThumbIdsImagesForCateg8 = {
	};
	private static Integer[] mThumbTextsImagesForCateg8 = {
	};
	
}
