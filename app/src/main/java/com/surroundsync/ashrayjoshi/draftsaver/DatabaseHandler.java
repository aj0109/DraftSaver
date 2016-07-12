package com.surroundsync.ashrayjoshi.draftsaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ashray Joshi on 30-Jun-16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "draftsManager";
    private static final String TABLE_DRAFTS = "draftsTable";
    private static final String KEY_ID = "id";
    private static final String KEY_DRAFTS = "drafts";
    private static final String KEY_IMAGE = "Image";
    private static final String KEY_LATITUDE ="lat";
    private static final String KEY_LONGITUDE ="long";

    public static ArrayList<Drafts> contactList;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_DRAFTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DRAFTS + " TEXT,"+KEY_IMAGE+" TEXT,"+KEY_LATITUDE+" TEXT,"+KEY_LONGITUDE+" TEXT"+ ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAFTS);

        // Create tables again
        onCreate(sqLiteDatabase);

    }

    public void addDraft(Drafts contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRAFTS, contact.getDrafts());
        values.put(KEY_IMAGE,""+Boolean.valueOf(contact.isImage()));
        Log.d("BOOL",""+Boolean.valueOf(contact.isImage()));
        values.put(KEY_LATITUDE,contact.getLatitude());
        values.put(KEY_LONGITUDE,contact.getLongitude());
        // Inserting Row
        db.insert(TABLE_DRAFTS, null, values);
        db.close(); // Closing database connection
    }


    public Drafts getDraft(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DRAFTS, new String[]{KEY_ID,
                        KEY_DRAFTS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Drafts contact = new Drafts(cursor.getString(0),
                cursor.getString(1),Boolean.valueOf(cursor.getString(2)),cursor.getString(3),cursor.getString(4));
        // return contact
        return contact;
    }

    public ArrayList<Drafts> getAllContacts() {
        contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DRAFTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Drafts contact = new Drafts();
                contact.setID(cursor.getString(0));
                contact.setDrafts(cursor.getString(1));
                contact.setImage(Boolean.parseBoolean(cursor.getString(2)));
                Log.d("BOOLE",""+Boolean.parseBoolean(cursor.getString(2)));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        Log.d("Contacts",""+contactList);

        return contactList;
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DRAFTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    public int updateContact(Drafts contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DRAFTS, contact.getDrafts());
        values.put(KEY_IMAGE,contact.isImage());

        // updating row
        return db.update(TABLE_DRAFTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }
    public void deleteContact(Drafts contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRAFTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }
}

