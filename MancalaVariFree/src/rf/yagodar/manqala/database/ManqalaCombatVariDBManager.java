package rf.yagodar.manqala.database;
//TODO DOC
import rf.yagodar.manqala.logic.combat.ManqalaCombatVari;
import rf.yagodar.manqala.logic.model.animated.ManqalaCharacter;
import rf.yagodar.manqala.logic.model.combat.ManqalaCell;
import rf.yagodar.manqala.logic.parameters.SVari;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Класс наследуется от <code>DBProvider</code>. Обеспечивает работу с <b>таблицей сохранения состояния игры Manqala Vari</b> базы данных приложения.
 * 
 * @author Yagodar
 * @version 1.0.0, 05.12.2012
 * @see DBProvider
 */
public class ManqalaCombatVariDBManager {	
	/**
	 * Возвращает единственный экзкемпляр данного класса.
	 * <br /><br />
	 * Вызывает метод <code>DBProvider.getManqalaCombatVariInstance(Context)</code>
	 * 
	 * @param context Context приложения. Необходим для подключении/создании базы данных именно этого приложения. Не может быть null.
	 * @return единственный экзкемпляр данного класса для работы с таблицей состояния игры Manqala Vari.
	 * @see DBProvider#getManqalaCombatVariInstance(Context)
	 */
	public static ManqalaCombatVariDBManager getInstance() {		
		if(INSTANCE == null) {
			INSTANCE = new ManqalaCombatVariDBManager();
		}
		
		return INSTANCE;
	}
	
	/**
	 * Проверяет сохранено ли состояние игры Manqala Vari в базе данных.
	 * 
	 * @return <code>true</code> состояние игры Manqala Vari сохранено в базе данных.<br />
	 * 			<code>false</code> состояние игры Manqala Vari <b>не</b> сохранено в базе данных.
	 */
	public boolean isManqalaCombatVariSaved() {
		boolean result = false;
		
		SQLiteDatabase db = null;
		Cursor cs = null;

		try {
			db = DBProvider.getInstance().getReadableDatabase();
			cs = db.query(tableName, null, primaryKeyWhereClause, null, null, null, null);
			if(cs.moveToNext()) {
				result = true;
			}
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while checking saved ManqalaCombatVari!", sqle);
		}		
		finally {
			if(cs != null) {
				cs.close();
			}
		}

		return result;
	}
	
	/**
	 * Возвращает сохранённое в базе данных состояние игры Manqala Vari.
	 * <br /><br />
	 * Если в базе ничего не сохранено, то возвратит <code>null</code>.
	 * <br /><br />
	 * Если возникнет исключение, то возвратит <code>null</code>.
	 * 
	 * @return <code>ManqalaCombatVari</code> экземпляр схватки с определёнными в базе данных начальными параметрами. Схватка возвратится уже запущенной.
	 * @see ManqalaCombatVari
	 */
	public ManqalaCombatVari getSavedManqalaCombatVari() {		
		ManqalaCombatVari manqalaCombatVari = null;
		
		SQLiteDatabase db = null;
		Cursor cs = null;

		try {
			db = DBProvider.getInstance().getReadableDatabase();
			cs = db.query(tableName, null, primaryKeyWhereClause, null, null, null, null);
			if(cs.moveToNext()) {
				byte[] firstOpponentCellsGrains = new byte[SVari.CELLS_COUNT + 1];
				byte[] secondOpponentCellsGrains = new byte[SVari.CELLS_COUNT + 1];
				
				String[] splittedFirstOpponentCellsGrainsStr = cs.getString(TableColumns.FIRST_OPPONENT_CELLS.ordinal()).split(opponentCellsSeparator);				
				for (int i = 0; i < firstOpponentCellsGrains.length; i++) {
					firstOpponentCellsGrains[i] = Byte.parseByte(splittedFirstOpponentCellsGrainsStr[i]);
				}
				
				String[] splittedSecondOpponentCellsGrainsStr = cs.getString(TableColumns.SECOND_OPPONENT_CELLS.ordinal()).split(opponentCellsSeparator);				
				for (int i = 0; i < secondOpponentCellsGrains.length; i++) {
					secondOpponentCellsGrains[i] = Byte.parseByte(splittedSecondOpponentCellsGrainsStr[i]);
				}
				
				manqalaCombatVari = new ManqalaCombatVari(cs.getInt(TableColumns.SURFACE_VIEW_KEY.ordinal()), 
						ManqalaCharactersDBManager.getInstance().getCharacter(cs.getInt(TableColumns.FIRST_OPPONENT_ID.ordinal())), 
						ManqalaCharactersDBManager.getInstance().getCharacter(cs.getInt(TableColumns.SECOND_OPPONENT_ID.ordinal())), 
						firstOpponentCellsGrains, 
						secondOpponentCellsGrains);
				
				manqalaCombatVari.setCompanyState(cs.getInt(TableColumns.COMPANY_STATE.ordinal()));
				
				if(cs.getInt(TableColumns.IS_DRAW.ordinal()) == 0) {
					ManqalaCharacter winner = ManqalaCharactersDBManager.getInstance().getCharacter(cs.getInt(TableColumns.WINNER_OPPONENT_ID.ordinal()));
					if(winner != null) {
						manqalaCombatVari.setWinner(winner);
						manqalaCombatVari.stop();
					}
					else {
						manqalaCombatVari.start(cs.getInt(TableColumns.WALKETH_OPPONENT_TYPE_ID.ordinal()));
					}
				}
				else {
					manqalaCombatVari.setDraw(true);
					manqalaCombatVari.stop();
				}
			}
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while resoring saved ManqalaCombatVari!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while resoring saved ManqalaCombatVari!", e);
		}
		finally {
			if(cs != null) {
				cs.close();
			}
		}
		
		return manqalaCombatVari;
	}
	
	/**
	 * Сохраняет в базу данных переданное состояние игры Manqala Vari.
	 * <br /><br />
	 * Заменяет/создаёт единственную строку в таблице, представленной данным классом.
	 * 
	 * @param manqalaCombatVari схватка, которую необходимо сохранить в базе данных.
	 * @see ManqalaCombatVari
	 */
	public void saveManqalaCombatVari(ManqalaCombatVari manqalaCombatVari) {
		SQLiteDatabase db = null;
		
		try {
			db = DBProvider.getInstance().getWritableDatabase();
						
			ManqalaCharacter firstOpponent = manqalaCombatVari.getOpponents().getFirstOpponent();
			ManqalaCharacter secondOpponent = manqalaCombatVari.getOpponents().getSecondOpponent();
			
			ContentValues values = new ContentValues();
			values.put(TableColumns.PRIMARY_KEY.getColumnName(), 0);
			values.put(TableColumns.COMPANY_STATE.getColumnName(), manqalaCombatVari.getCompanyState());
			values.put(TableColumns.SURFACE_VIEW_KEY.getColumnName(), manqalaCombatVari.getSVKey());
			values.put(TableColumns.WALKETH_OPPONENT_TYPE_ID.getColumnName(), manqalaCombatVari.getWalkethOpponentType().ordinal());
			values.put(TableColumns.FIRST_OPPONENT_ID.getColumnName(), firstOpponent.getCharId());
			values.put(TableColumns.SECOND_OPPONENT_ID.getColumnName(), secondOpponent.getCharId());
			
			if(manqalaCombatVari.getWinner() != null) {
				values.put(TableColumns.WINNER_OPPONENT_ID.getColumnName(), manqalaCombatVari.getWinner().getCharId());
			}
			else {
				values.put(TableColumns.WINNER_OPPONENT_ID.getColumnName(), -1);
			}
			
			values.put(TableColumns.IS_DRAW.getColumnName(), manqalaCombatVari.isDraw() ? 1 : 0);
			
			String firstOpponentCellsGrainsStr = "";
			for (ManqalaCell cell : manqalaCombatVari.getGameBoard().getGameBoardCells().getOpponentCells(firstOpponent).getCells()) {
				firstOpponentCellsGrainsStr += cell.getGrainsCount() + opponentCellsSeparator;
			}			
			values.put(TableColumns.FIRST_OPPONENT_CELLS.getColumnName(), firstOpponentCellsGrainsStr);
			
			String secondOpponentCellsGrainsStr = "";
			for (ManqalaCell cell : manqalaCombatVari.getGameBoard().getGameBoardCells().getOpponentCells(secondOpponent).getCells()) {
				secondOpponentCellsGrainsStr += cell.getGrainsCount() + opponentCellsSeparator;
			}
			values.put(TableColumns.SECOND_OPPONENT_CELLS.getColumnName(), secondOpponentCellsGrainsStr);
			
			db.replace(tableName, null, values);
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while saving ManqalaCombatVari!", sqle);
		}
		catch(Exception e) {
			Log.e(logTag, "Exception while saving ManqalaCombatVari!", e);
		}
	}
	
	/**
	 * Удаляет единственную строку в данной таблице - сохранённое состояние игры Manqala Vari.
	 */
	public void deleteSavedManqalaCombatVari() {
		SQLiteDatabase db = null;

		try {
			db = DBProvider.getInstance().getWritableDatabase();
			db.delete(tableName, primaryKeyWhereClause, null);
		}
		catch(SQLiteException sqle) {
			Log.e(logTag, "SQLiteException while deleting ManqalaCombatVari!", sqle);
		}
	}
	
	protected void onCreating(SQLiteDatabase db) {
		db.execSQL(getDropTableStr());
		db.execSQL(getCreateTableStr());
	}
	
	/**
	 * Возврашает строку - SQL запрос создания таблицы, представленной данным классом.
	 * 
	 * @return строка - SQL запрос создания таблицы, представленной данным классом.
	 */
	protected String getCreateTableStr() {
		return "CREATE TABLE " + tableName + " ("
				+ TableColumns.PRIMARY_KEY.getColumnName() + " INTEGER PRIMARY KEY DEFAULT 0, "
				+ TableColumns.COMPANY_STATE.getColumnName() + " INTEGER NOT NULL, "
				+ TableColumns.SURFACE_VIEW_KEY.getColumnName() + " INTEGER NOT NULL, "
                + TableColumns.WALKETH_OPPONENT_TYPE_ID.getColumnName() + " INTEGER NOT NULL, "
                + TableColumns.FIRST_OPPONENT_ID.getColumnName() + " INTEGER NOT NULL, "
                + TableColumns.SECOND_OPPONENT_ID.getColumnName() + " INTEGER NOT NULL, "
                + TableColumns.WINNER_OPPONENT_ID.getColumnName() + " INTEGER NOT NULL, "
                + TableColumns.IS_DRAW.getColumnName() + " INTEGER NOT NULL, "
                + TableColumns.FIRST_OPPONENT_CELLS.getColumnName() + " TEXT NOT NULL, "
                + TableColumns.SECOND_OPPONENT_CELLS.getColumnName() + " TEXT NOT NULL"
                + ");";
	}
	
	/**
	 * Возврашает строку - SQL запрос удаления таблицы, представленной данным классом.
	 * 
	 * @return строка - SQL запрос удаления таблицы, представленной данным классом.
	 */
	protected String getDropTableStr() {
		return "DROP TABLE IF EXISTS " + tableName;
	}
	
	/**
	 * Конструктор данного класса. 
	 * Передаваемый context приложения сохраняется.
	 * 
	 * @param context Context приложения. Необходим для подключении/создании базы данных именно этого приложения. Не может быть null.
	 * @see DBProvider#DBHelper(Context)
	 */
	private ManqalaCombatVariDBManager() {
		logTag = ManqalaCombatVariDBManager.class.getSimpleName();
		opponentCellsSeparator = ",";
		primaryKeyWhereClause = TableColumns.PRIMARY_KEY.getColumnName() + " = 0";
		tableName = "manqala_combat_vari_state";
    }
	
	private enum TableColumns {
		PRIMARY_KEY("primary_key"),
		COMPANY_STATE("company_state"),
		SURFACE_VIEW_KEY("s_v_key"),
		WALKETH_OPPONENT_TYPE_ID("walketh_opponent_type_id"),
		FIRST_OPPONENT_ID("first_id"),
		SECOND_OPPONENT_ID("second_id"),
		WINNER_OPPONENT_ID("winner_id"),
		IS_DRAW("is_draw"),
		FIRST_OPPONENT_CELLS("first_cells"),
		SECOND_OPPONENT_CELLS("second_cells");
		
		TableColumns(String columnName) {
			this.columnName = columnName;
		}
		
		public String getColumnName() {
			return columnName;
		}
		
		private final String columnName;
	}
	
	private final String logTag;
	private final String opponentCellsSeparator;
	private final String primaryKeyWhereClause;
	private final String tableName;
	
	private static ManqalaCombatVariDBManager INSTANCE = null;
}
