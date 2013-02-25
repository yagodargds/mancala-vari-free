package rf.yagodar.manqala.free.database;
//TODO DOC
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Класс наследуется от <code>SQLiteOpenHelper</code>. Обеспечивает работу с базой данных приложения.
 * <br /><br />
 * Для каждой таблицы базы свой инстанс, наследуемый от данного класса. Напрямую объект данного класса получить нельзя.
 * <br /><br />
 * Название базы данных хранится в статической переменной <code>DATABASE_NAME</code>.
 * <br /><br /> 
 * При изменении архитектуры базы данных, нужно менять её версию (хранится в статической переменной <code>DATABASE_VERSION</code>), 
 * чтобы существующая на устройстве база данных переустановилась.
 * 
 * @author Yagodar
 * @version 1.0.0, 05.12.2012
 * @see SQLiteOpenHelper
 */
public class DBProvider extends SQLiteOpenHelper {	
	/**
	 * Вызывается при создании либо изменении версии базы данных. 
	 * <br /><br />
	 * Создаёт пустые таблицы базы данных.
	 * <br /><br />
	 * При добавлении новой таблицы необходимо добавить в этот метод SQL запрос создания этой таблицы. 
	 */
	@Override	
	public void onCreate(SQLiteDatabase db) {
		ManqalaCharactersDBManager.getInstance().onCreating(db);
		ManqalaCombatVariDBManager.getInstance().onCreating(db);
	}

	/**
	 * Вызывается при повышении версии базы данных.
	 * <br /><br />
	 * Удаляет все таблицы базы и вызывает <code>onCreate(SQLiteDatabase)</code>.
	 * <br /><br />
	 * При добавлении новой таблицы необходимо добавить в этот метод SQL запрос удаления этой таблицы. 
	 * 
	 * @see #onCreate(SQLiteDatabase)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
	}	
	
	/**
	 * Вызывается при понижении версии базы данных.
	 * <br /><br />
	 * Вызывает <code>onUpgrade(SQLiteDatabase)</code>.
	 * 
	 * @see #onUpgrade(SQLiteDatabase, int, int)
	 */
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	
	public static void createNewInstance(Context context) {
		INSTANCE = new DBProvider(context);
	}
	
	public static DBProvider getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Возвращает сохранённый Context приложения.
	 * 
	 * @return сохранённый Context приложения.
	 */
	protected Context getAppContext() {
		return context;
	}
	
	/**
	 * Конструктор используется только для создания наследуемых от данного класса экземпляров. 
	 * Передаваемый context приложения сохраняется.
	 * 
	 * @param context Context приложения. Необходим для подключении/создании базы данных именно этого приложения. Не может быть null.
	 * @see SQLiteOpenHelper#SQLiteOpenHelper(Context, String, android.database.sqlite.SQLiteDatabase.CursorFactory, int)
	 */
	private DBProvider(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}	
	
	private Context context;
	
	private static DBProvider INSTANCE = null;
	private final static String DATABASE_NAME = "manqala_vari.db";
	private final static int DATABASE_VERSION = 1;
}
