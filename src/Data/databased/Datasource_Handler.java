package Data.databased;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.View.OnClickListener;

public class Datasource_Handler {

	private SQLiteDatabase database;
	private Database_Helper dbHelper;

	public Datasource_Handler(Context context) {
		dbHelper = new Database_Helper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public boolean insert_details_day(String layout_id, String date,
			long timestamp) {
		ContentValues values = new ContentValues();
		values.put(Database_Helper.COLUMN_LAYOUT_ID, layout_id);
		values.put(Database_Helper.COLUMN_DATE, date);
		values.put(Database_Helper.COLUMN_TIMESTAMP, timestamp);

		database.insert(Database_Helper.TABLE_TIME_TRACK, null, values);
		return true;
	}

	public boolean update_details_day(int column_id, String layout_id,
			String date, long timestamp) {
		ContentValues values = new ContentValues();
		values.put(Database_Helper.COLUMN_LAYOUT_ID, layout_id);
		values.put(Database_Helper.COLUMN_DATE, date);
		values.put(Database_Helper.COLUMN_TIMESTAMP, timestamp);

		database.update(Database_Helper.TABLE_TIME_TRACK, values,
				dbHelper.COLUMN_ID + " like ('%" + column_id + "%')", null);
		return true;
	}

	public Cursor get_all_slots(String layout) {
		// Log.d(" dddd", "" + date + " " + truck_id);
		Cursor cur = database.query(true, dbHelper.TABLE_TIME_TRACK,
				new String[] { dbHelper.COLUMN_LAYOUT_ID, dbHelper.COLUMN_DATE,
						dbHelper.COLUMN_TIMESTAMP }, dbHelper.COLUMN_LAYOUT_ID
						+ " like ('%" + layout + "%')", null, null, null,
				dbHelper.COLUMN_ID + " DESC", null);
		if (!cur.isAfterLast()) {
			cur.moveToFirst();

		}

		return cur;

	}

	public Cursor get_all_slots_with_data(String layout_id, String date) {
		Cursor cur = database.query(true, dbHelper.TABLE_TIME_TRACK,
				new String[] { dbHelper.COLUMN_LAYOUT_ID, dbHelper.COLUMN_DATE,
						dbHelper.COLUMN_TIMESTAMP, dbHelper.COLUMN_ID, },
				dbHelper.COLUMN_LAYOUT_ID + " = '" + layout_id + "' AND "
						+ dbHelper.COLUMN_DATE + " = '" + date + "'", null,
				null, null, dbHelper.COLUMN_ID + " DESC", null);

		if (!cur.isAfterLast()) {
			cur.moveToFirst();

		}
		return cur;

	}

	// Second table

	public boolean insert_details_user(String User_Name, String First_Name,
			String Last_Name, String Email, String Password,
			String Confirm_Password, String picturePath) {
		ContentValues values = new ContentValues();
		values.put(Database_Helper.USER_NAME, User_Name);
		values.put(Database_Helper.FIRST_NAME, First_Name);
		values.put(Database_Helper.LAST_NAME, Last_Name);
		values.put(Database_Helper.EMAIL, Email);
		values.put(Database_Helper.PASSWORD, Password);
		values.put(Database_Helper.CONFIRM_PASSWORD, Password);
		values.put(Database_Helper.USER_IMAGE, picturePath);

		database.insert(Database_Helper.TABLE_USER_INFO, null, values);
		return true;
	}

	public Cursor get_all_data(String Email, String Password) {
		Cursor cur = database.query(true, dbHelper.TABLE_USER_INFO,
				new String[] { dbHelper.USER_NAME, dbHelper.EMAIL,
						dbHelper.PASSWORD, dbHelper.USER_IMAGE },
				dbHelper.USER_NAME + " = '" + Email + "' OR " + dbHelper.EMAIL
						+ " = '" + Email + "' AND " + dbHelper.PASSWORD + "= '"
						+ Password + "'", null, null, null, null, null);

		if (!cur.isAfterLast()) {
			cur.moveToFirst();

		}
		return cur;

	}

	public Cursor get_image(String Email) {
		Cursor cur = database.query(true, dbHelper.TABLE_USER_INFO,
				new String[] { dbHelper.USER_NAME, dbHelper.EMAIL,
						dbHelper.PASSWORD, dbHelper.USER_IMAGE },
				dbHelper.USER_NAME + " = '" + Email + "' OR " + dbHelper.EMAIL
						+ " = '" + Email + "' ", null, null, null, null, null);

		if (!cur.isAfterLast()) {
			cur.moveToFirst();

		}
		return cur;

	}

	public Cursor check_all_email(String Email) {
		Cursor cur = database.query(true, dbHelper.TABLE_USER_INFO,
				new String[] { dbHelper.EMAIL, }, dbHelper.EMAIL + " = '"
						+ Email + "'", null, null, null, null, null);

		if (!cur.isAfterLast()) {
			cur.moveToFirst();

		}
		return cur;
	}

	public Cursor check_all_user_name(String User_Name) {
		Cursor cur = database.query(true, dbHelper.TABLE_USER_INFO,
				new String[] { dbHelper.USER_NAME, }, dbHelper.USER_NAME
						+ " = '" + User_Name + "'", null, null, null, null,
				null);

		if (!cur.isAfterLast()) {
			cur.moveToFirst();

		}
		return cur;
	}

	public Cursor check_all_Data(String Email, String Password) {
		Cursor cur = database.query(true, dbHelper.TABLE_USER_INFO,
				new String[] { dbHelper.USER_NAME, dbHelper.FIRST_NAME,
						dbHelper.LAST_NAME, dbHelper.EMAIL, dbHelper.PASSWORD,
						dbHelper.CONFIRM_PASSWORD, dbHelper.USER_IMAGE },
				dbHelper.USER_NAME + " = '" + Email + "' OR " + dbHelper.EMAIL
						+ " = '" + Email + "' AND " + dbHelper.PASSWORD + "='"
						+ Password + "'", null, null, null, null, null);

		if (!cur.isAfterLast()) {
			cur.moveToFirst();
		}
		return cur;

	}

	public boolean update_details_user(String First_Name, String Last_Name,
			String Email, String Password, String Confirm_Password,
			String picturePath) {
		ContentValues values = new ContentValues();
		values.put(Database_Helper.FIRST_NAME, First_Name);
		values.put(Database_Helper.LAST_NAME, Last_Name);
		values.put(Database_Helper.EMAIL, Email);
		values.put(Database_Helper.PASSWORD, Password);
		values.put(Database_Helper.CONFIRM_PASSWORD, Password);
		values.put(Database_Helper.USER_IMAGE, picturePath);

		database.update(Database_Helper.TABLE_USER_INFO, values, dbHelper.EMAIL
				+ " = '" + Email + "'", null);
		return true;
	}

}