package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import adapter.AppIDPojo;
import adapter.RegIDPojo;
import adapter.ViewPojo;

public class DbHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "contactsManager";

	// Contacts table name
	private static final String TABLE_CONTACTS = "contacts";
	private static final String TABLE_REG = "TABLEREG";
	private static final String TABLE_VIEW = "TABLEVIEW";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";

	private static final String KEY_REGID = "id";
	private static final String KEY_REGNAME = "name";

	private static final String KEY_VIEWID = "id";
	private static final String KEY_VIEWNAME = "name";


	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT"
				+   ")";
		String CREATE_SET_REG = "CREATE TABLE " + TABLE_REG + "("
				+ KEY_REGID + " INTEGER PRIMARY KEY," + KEY_REGNAME + " TEXT"
				+   ")";
		String CREATE_SET_VIEW = "CREATE TABLE " + TABLE_VIEW + "("
				+ KEY_VIEWID + " INTEGER PRIMARY KEY," + KEY_VIEWNAME + " TEXT"
				+   ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
		db.execSQL(CREATE_SET_REG);
		db.execSQL(CREATE_SET_VIEW);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REG);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW);
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	public  void addContact(AppIDPojo contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, contact.getAppID()); // Contact Name


		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}


	public  void setReg(RegIDPojo contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_REGNAME, contact.getAppID()); // Contact Name


		// Inserting Row
		db.insert(TABLE_REG, null, values);
		db.close(); // Closing database connection
	}
	public  void SetView(ViewPojo contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_VIEWNAME, contact.getAppID()); // Contact Name


		// Inserting Row
		db.insert(TABLE_VIEW, null, values);
		db.close(); // Closing database connection
	}
	// Getting single contact
	public AppIDPojo getContact(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
						KEY_NAME}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		AppIDPojo contact = new AppIDPojo(1,
				"N");
		if (cursor != null && cursor.moveToFirst()) {


			contact = new AppIDPojo(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1));

		}
		// return contact
		return contact;

	}


	public RegIDPojo getReg(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_REG, new String[] { KEY_REGID,
						KEY_REGNAME}, KEY_REGID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		RegIDPojo rgpojo = new RegIDPojo(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1));
		// return contact
		return rgpojo;
	}



	public List<RegIDPojo> getAllReg() {
		List<RegIDPojo> contactList = new ArrayList<RegIDPojo>();
		// Select All Query


		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(TABLE_REG, null, null,
				null, null, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				RegIDPojo contact = new RegIDPojo();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setAppid(cursor.getString(1));

				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}


    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("delete  from "+ TABLE_CONTACTS);

        db.close();
    }

	public List<AppIDPojo> getAllContacts() {
		List<AppIDPojo> contactList = new ArrayList<AppIDPojo>();
		// Select All Query


		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.query(TABLE_CONTACTS, null, null,
				null, null, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				AppIDPojo contact = new AppIDPojo();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setAppid(cursor.getString(1));

				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		return contactList;
	}

}