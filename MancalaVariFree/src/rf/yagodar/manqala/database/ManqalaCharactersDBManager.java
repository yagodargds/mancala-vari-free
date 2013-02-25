package rf.yagodar.manqala.database;

import rf.yagodar.manqala.R;
import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.model.animated.ManqalaMonster;
import rf.yagodar.manqala.logic.model.animated.ManqalaPlayer;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class ManqalaCharactersDBManager {
	public static ManqalaCharactersDBManager getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new ManqalaCharactersDBManager();
		}
		
		return INSTANCE;
	}
	
	public long addCharacter(String name, boolean isMonster, boolean isMaster) {
		SQLiteDatabase db = null;
		
		try {
			db = DBProvider.getInstance().getWritableDatabase();
						
			ContentValues values = new ContentValues();
			values.put(TableColumns.CHAR_NAME.getColumnName(), name);
			values.put(TableColumns.IS_MONSTER.getColumnName(), isMonster ? 1 : 0);
			values.put(TableColumns.IS_MASTER.getColumnName(), isMaster ? 1 : 0);
						
			return db.insert(tableName, null, values);
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while addCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while addCharacter!", e);
		}
		
		return -1;
	}
		
	public ManqalaCharacter getCharacter(int id) {
		SQLiteDatabase db = null;
		Cursor cs = null;

		try {
			db = DBProvider.getInstance().getReadableDatabase();
			cs = db.query(tableName, null, TableColumns.CHAR_ID.getColumnName() + " = " + id, null, null, null, null);
			if(cs.moveToNext()) {
				if(cs.getInt(TableColumns.IS_MONSTER.ordinal()) == 1) {
					return new ManqalaMonster(id, cs.getString(TableColumns.CHAR_NAME.ordinal()));
				}
				else {
					return new ManqalaPlayer(id, cs.getString(TableColumns.CHAR_NAME.ordinal()));
				}
			}
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while restoring ManqalaCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while restoring ManqalaCharacter!", e);
		}
		finally {
			if(cs != null) {
				cs.close();
			}
		}
		
		return null;
	}
	
	public ManqalaCharacter getCharacter(String name) {
		SQLiteDatabase db = null;
		Cursor cs = null;

		try {
			db = DBProvider.getInstance().getReadableDatabase();
			cs = db.query(tableName, null, TableColumns.CHAR_NAME.getColumnName() + " = '" + name + "'", null, null, null, null);
			if(cs.moveToNext()) {
				if(cs.getInt(TableColumns.IS_MONSTER.ordinal()) == 1) {
					return new ManqalaMonster(cs.getInt(TableColumns.CHAR_ID.ordinal()), name);
				}
				else {
					return new ManqalaPlayer(cs.getInt(TableColumns.CHAR_ID.ordinal()), name);
				}
			}
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while restoring ManqalaCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while restoring ManqalaCharacter!", e);
		}
		finally {
			if(cs != null) {
				cs.close();
			}
		}
		
		return null;
	}
	
	public ManqalaCharacter getMaster() {
		SQLiteDatabase db = null;
		Cursor cs = null;

		try {
			db = DBProvider.getInstance().getReadableDatabase();
			cs = db.query(tableName, null, TableColumns.IS_MASTER.getColumnName() + " = 1", null, null, null, null);
			if(cs.moveToNext()) {
				if(cs.getInt(TableColumns.IS_MONSTER.ordinal()) == 1) {
					return new ManqalaMonster(cs.getInt(TableColumns.CHAR_ID.ordinal()), cs.getString(TableColumns.CHAR_NAME.ordinal()));
				}
				else {
					return new ManqalaPlayer(cs.getInt(TableColumns.CHAR_ID.ordinal()), cs.getString(TableColumns.CHAR_NAME.ordinal()));
				}
			}
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while restoring ManqalaCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while restoring ManqalaCharacter!", e);
		}
		finally {
			if(cs != null) {
				cs.close();
			}
		}
		
		return null;
	}
	
	public int getMasterCompanyState() {
		SQLiteDatabase db = null;
		Cursor cs = null;

		try {
			db = DBProvider.getInstance().getReadableDatabase();
			cs = db.query(tableName, null, TableColumns.IS_MASTER.getColumnName() + " = 1", null, null, null, null);
			if(cs.moveToNext()) {
				return cs.getInt(TableColumns.COMPANY_STATE.ordinal());
			}
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while restoring ManqalaCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while restoring ManqalaCharacter!", e);
		}
		finally {
			if(cs != null) {
				cs.close();
			}
		}
		
		return 0;
	}
	
	public void incMasterCompanyState() {
		SQLiteDatabase db = null;

		try {
			db = DBProvider.getInstance().getWritableDatabase();
			
			ManqalaCharacter master = getMaster();
			
			ContentValues values = new ContentValues();
			values.put(TableColumns.CHAR_ID.getColumnName(), master.getCharId());
			values.put(TableColumns.CHAR_NAME.getColumnName(), master.getCharName());
			values.put(TableColumns.COMPANY_STATE.getColumnName(), getMasterCompanyState() + 1);
			values.put(TableColumns.IS_MONSTER.getColumnName(), 0);
			values.put(TableColumns.IS_MASTER.getColumnName(), 1);
			
			db.replace(tableName, null, values);
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while restoring ManqalaCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while restoring ManqalaCharacter!", e);
		}
	}
	
	public String getMasterName() {
		SQLiteDatabase db = null;
		Cursor cs = null;

		try {
			db = DBProvider.getInstance().getReadableDatabase();
			cs = db.query(tableName, null, TableColumns.IS_MASTER.getColumnName() + " = 1", null, null, null, null);
			if(cs.moveToNext()) {
				return cs.getString(TableColumns.CHAR_NAME.ordinal());
			}
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while restoring ManqalaCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while restoring ManqalaCharacter!", e);
		}
		finally {
			if(cs != null) {
				cs.close();
			}
		}
		
		return null;
	}
	
	protected void onCreating(SQLiteDatabase db) {
		db.execSQL(getDropTableStr());
		db.execSQL(getCreateTableStr());
		
		String[] characters = DBProvider.getInstance().getAppContext().getResources().getStringArray(R.array.preset_player_names);
		for(int i = 0; i < characters.length; i++) {
			addCharacter(db, characters[i], false, false);
		}
		
		characters = DBProvider.getInstance().getAppContext().getResources().getStringArray(R.array.monster_names);
		for(int i = 0; i < characters.length; i++) {
			addCharacter(db, characters[i], true, false);
		}
	}

	protected String getCreateTableStr() {
		return "CREATE TABLE " + tableName + " ("
				+ TableColumns.CHAR_ID.getColumnName() + " INTEGER PRIMARY KEY, "
                + TableColumns.CHAR_NAME.getColumnName() + " TEXT NOT NULL, "
                + TableColumns.COMPANY_STATE.getColumnName() + " INTEGER DEFAULT 0, "
                + TableColumns.IS_MONSTER.getColumnName() + " INTEGER NOT NULL, "
                + TableColumns.IS_MASTER.getColumnName() + " INTEGER NOT NULL"
                + ");";
	}

	protected String getDropTableStr() {
		return "DROP TABLE IF EXISTS " + tableName;
	}
	
	private ManqalaCharactersDBManager() {
		super();
		logTag = ManqalaCharactersDBManager.class.getSimpleName();
		tableName = "manqala_characters";
	}
	
	private void addCharacter(SQLiteDatabase db, String name, boolean isMonster, boolean isMaster) {
		try {
			ContentValues values = new ContentValues();
			values.put(TableColumns.CHAR_NAME.getColumnName(), name);
			values.put(TableColumns.IS_MONSTER.getColumnName(), isMonster ? 1 : 0);
			values.put(TableColumns.IS_MASTER.getColumnName(), isMaster ? 1 : 0);
						
			db.insert(tableName, null, values);
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while addCharacter!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while addCharacter!", e);
		}
	}
	
	private enum TableColumns {
		CHAR_ID("char_id"),
		CHAR_NAME("char_name"),
		COMPANY_STATE("company_state"),
		IS_MONSTER("is_monster"),
		IS_MASTER("is_master");
		
		TableColumns(String columnName) {
			this.columnName = columnName;
		}
		
		public String getColumnName() {
			return columnName;
		}
		
		private final String columnName;
	}
	
	private final String logTag;
	private final String tableName;
	
	private static ManqalaCharactersDBManager INSTANCE = null; 
}
