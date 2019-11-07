package com.example.abo8a.jobconnector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by abo8a on 12/11/2017.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME =
            "jobConnector";
    // Contacts table name
    private static final String TABLE_Employee = "employee";
    private static final String KEY_ID = "_id";
    private static final String KEY_FNAME = "_fname";
    private static final String KEY_LNAME = "_lname";
    private static final String KEY_AGE = "_age";
    private static final String KEY_PH_NO = "_phone_number";
    private static final String KEY_EMAIL= "_email";
    private static final String KEY_WORKIN = "_currentWork";
    private static final String KEY_COUNTRY = "_country";
    private static final String KEY_CITY = "_city";
    private static final String KEY_IMAGE = "_image";




    public DatabaseHandler(Context c) {

        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EMPOYEE_TABLE =
                "CREATE TABLE " + TABLE_Employee + "("+ KEY_ID + " INTEGER PRIMARY KEY," +
                        KEY_FNAME + " TEXT,"+KEY_LNAME + " TEXT,"+KEY_AGE + " INTEGER," + KEY_PH_NO + " INTEGER," + KEY_EMAIL + " TEXT"
                        +KEY_WORKIN + " TEXT,"+KEY_COUNTRY + " TEXT,"+KEY_CITY + " TEXT,"+KEY_IMAGE + " TEXT,"+ ")";
        db.execSQL(CREATE_EMPOYEE_TABLE);
    }
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Employee);
// Create tables again
        onCreate(db);
    }

    public void  updateContact(Employee e){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, e.getFname());
        values.put(KEY_LNAME, e.getLname());
        values.put(KEY_AGE, e.getAge());
        values.put(KEY_PH_NO, e.getPh_nb());
        values.put(KEY_EMAIL,e.getEmail());
        values.put(KEY_WORKIN, e.getWorkIn());
        values.put(KEY_COUNTRY, e.getCountry());
        values.put(KEY_CITY, e.getCity());
        values.put(KEY_IMAGE, e.getImage());


        db.update(TABLE_Employee, values,KEY_ID+"="+e.getId(),null);
        db.close();

    }

    void addEmployee(Employee e) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, e.getFname());
        values.put(KEY_LNAME, e.getLname());
        values.put(KEY_AGE, e.getAge());
        values.put(KEY_PH_NO, e.getPh_nb());
        values.put(KEY_EMAIL,e.getEmail());
        values.put(KEY_WORKIN, e.getWorkIn());
        values.put(KEY_COUNTRY, e.getCountry());
        values.put(KEY_CITY, e.getCity());
        values.put(KEY_IMAGE, e.getImage());
        db.insert(TABLE_Employee, null, values);
        db.close(); // Closing database connection
    }
    Employee getEmployee(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_Employee, new String[] { KEY_ID,
                        KEY_FNAME,KEY_LNAME,KEY_AGE, KEY_PH_NO ,KEY_EMAIL,KEY_WORKIN,KEY_COUNTRY,KEY_CITY,KEY_IMAGE}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if(cursor.moveToFirst())
        {

            Employee e = new Employee(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),Integer.parseInt(cursor.getString(3)), cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));

            db.close();
            return e;
        }

        return null;
    }






    public void deleteEmployee(Employee e)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Employee,KEY_ID+"="+e.getId(),null);
        db.close();
    }
}
