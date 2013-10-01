package Data.databased;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database_Helper extends SQLiteOpenHelper {

	public static final String TABLE_TIME_TRACK = "TimeSpent";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LAYOUT_ID = "_layout_id";
	public static final String COLUMN_DATE = "_date";
	public static final String COLUMN_TIMESTAMP = "_time";

	public static final String TABLE_USER_INFO = "user_info";
	public static final String USER_NAME = "user_name";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String CONFIRM_PASSWORD = "confirm_password";
	public static final String USER_IMAGE = "user_image";

	private static final String DATABASE_NAME = "gamerpoint_data.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_TIME_TRACK + "( " + COLUMN_ID
			+ " integer primary key autoincrement," + COLUMN_LAYOUT_ID + ","
			+ COLUMN_DATE + "," + COLUMN_TIMESTAMP + " long not null)";

	// second table
	private static final String DATABASE_CREATE_SECOND = "create table "
			+ TABLE_USER_INFO + "( " + USER_NAME + "," + FIRST_NAME + ","
			+ LAST_NAME + "," + EMAIL + "," + PASSWORD + "," + CONFIRM_PASSWORD
			+ "," + USER_IMAGE + ")";

	public Database_Helper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE_SECOND);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(Database_Helper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_TIME_TRACK);
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_USER_INFO);

		onCreate(db);
	}

}
