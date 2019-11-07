package com.example.abo8a.jobconnector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by abo8a on 3/21/2018.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 6;
    // Database Name
    private static final String DATABASE_NAME =
            "jobConnector";

    private static final String TABLE_BASIC_INFO = "basic_enfo";
    private static final String TABLE_EDUCATION = "education";
    private static final String TABLE_EXPERIENCE = "experience";
    private static final String TABLE_LANGUAGES = "Languages";

    private static final String KEY_ID = "id";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_AGE = "age";
    private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_TARGET_WORK = "target_work";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_CITY = "city";
    private static final String KEY_IMAGE = "image";

    private static final String KEY_MAJOR = "major";
    private static final String KEY_DEGREE = "degree";
    private static final String KEY_UNI = "university_name";
    private static final String KEY_STARTING_DATE = "start_date";
    private static final String KEY_COMPLETION_DATE = "completion_date";


    private static final String KEY_JOB_IN = "job_in";
    private static final String KEY_EXPERINCE_DURATION = "experience_duration";
    private static final String KEY_COMPANY_NAME = "company_name";
    private static final String KEY_JOB_COUNTRY = "job_country";
    private static final String KEY_JOB_CITY = "job_city";
    private static final String KEY_DESCRIPTION = "description";

    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_LANGUAGE_LEVEL = "language_level";


    public DataBaseHandler(Context c) {

        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BASIC_INFO_TABLE =
                "CREATE TABLE " + TABLE_BASIC_INFO + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                        KEY_FNAME + " TEXT," + KEY_LNAME + " TEXT," + KEY_AGE + " INTEGER," + KEY_PH_NO + " TEXT," + KEY_EMAIL + " TEXT," +
                        KEY_TARGET_WORK + " TEXT," + KEY_COUNTRY + " TEXT," + KEY_CITY + " TEXT," + KEY_IMAGE + " TEXT" + ")";
        String CREATE_EDUCATION_TABLE =
                "CREATE TABLE " + TABLE_EDUCATION + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                        KEY_MAJOR + " TEXT," + KEY_DEGREE + " TEXT," + KEY_UNI + " TEXT," + KEY_STARTING_DATE + " TEXT," + KEY_COMPLETION_DATE + " TEXT" +
                        ")";
        String CREATE_EXPERIENCE_TABLE =
                "CREATE TABLE " + TABLE_EXPERIENCE + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                        KEY_JOB_IN + " TEXT," + KEY_EXPERINCE_DURATION + " int," + KEY_COMPANY_NAME + " TEXT," + KEY_JOB_COUNTRY + " TEXT," +
                        KEY_JOB_CITY + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")";

        String CREATE_LANGUAGES_TABLE =
                "CREATE TABLE " + TABLE_LANGUAGES + "(" + KEY_ID + " INTEGER ," +
                        KEY_LANGUAGE + " TEXT," + KEY_LANGUAGE_LEVEL + " TEXT" +
                        ")";

        db.execSQL(CREATE_BASIC_INFO_TABLE);
        db.execSQL(CREATE_EDUCATION_TABLE);
        db.execSQL(CREATE_EXPERIENCE_TABLE);
        db.execSQL(CREATE_LANGUAGES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BASIC_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDUCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPERIENCE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGES);

        onCreate(db);
    }

    public void updateBasic(Employee e) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, e.getFname());
        values.put(KEY_LNAME, e.getLname());
        values.put(KEY_AGE, e.getAge());
        values.put(KEY_PH_NO, e.getPh_nb());
        values.put(KEY_EMAIL, e.getEmail());
        values.put(KEY_TARGET_WORK, e.getWorkIn());
        values.put(KEY_COUNTRY, e.getCountry());
        values.put(KEY_CITY, e.getCity());
        values.put(KEY_IMAGE, e.getImage());

        db.update(TABLE_BASIC_INFO, values, KEY_ID + "=" + e.getId(), null);
        db.close();

    }

    public void updateExperience(Experience e) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_JOB_IN, e.getJob_In());
        values.put(KEY_EXPERINCE_DURATION, e.getExperience_duration());
        values.put(KEY_COMPANY_NAME, e.getCompany_name());
        values.put(KEY_JOB_COUNTRY, e.getJob_country());
        values.put(KEY_JOB_CITY, e.getJob_city());
        values.put(KEY_DESCRIPTION, e.getDescription());


        db.update(TABLE_EXPERIENCE, values, KEY_ID + "=" + e.getId(), null);
        db.close();

    }

    public void updateEducation(Education e) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MAJOR, e.getMajor());
        values.put(KEY_DEGREE, e.getDegree());
        values.put(KEY_UNI, e.getUniversity_name());
        values.put(KEY_STARTING_DATE, e.getStarting_date());
        values.put(KEY_COMPLETION_DATE, e.getCompletion_date());

        db.update(TABLE_EDUCATION, values, KEY_ID + "=" + e.getId(), null);
        db.close();

    }

    public void addBasic(Employee e) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, e.getId());
        values.put(KEY_FNAME, e.getFname());
        values.put(KEY_LNAME, e.getLname());
        values.put(KEY_AGE, e.getAge());
        values.put(KEY_PH_NO, e.getPh_nb());
        values.put(KEY_EMAIL, e.getEmail());
        values.put(KEY_TARGET_WORK, e.getWorkIn());
        values.put(KEY_COUNTRY, e.getCountry());
        values.put(KEY_CITY, e.getCity());
        values.put(KEY_IMAGE, e.getImage());

        db.insert(TABLE_BASIC_INFO, null, values);
        db.close();

    }

    public void addExperience(Experience e) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, e.getId());
        values.put(KEY_JOB_IN, e.getJob_In());
        values.put(KEY_EXPERINCE_DURATION, e.getExperience_duration());
        values.put(KEY_COMPANY_NAME, e.getCompany_name());
        values.put(KEY_JOB_COUNTRY, e.getJob_country());
        values.put(KEY_JOB_CITY, e.getJob_city());
        values.put(KEY_DESCRIPTION, e.getDescription());


        db.insert(TABLE_EXPERIENCE, null, values);
        db.close();

    }

    public void addEducation(Education e) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, e.getId());
        values.put(KEY_MAJOR, e.getMajor());
        values.put(KEY_DEGREE, e.getDegree());
        values.put(KEY_UNI, e.getUniversity_name());
        values.put(KEY_STARTING_DATE, e.getStarting_date());
        values.put(KEY_COMPLETION_DATE, e.getCompletion_date());

        db.insert(TABLE_EDUCATION, null, values);
        db.close();

    }

    public void addLanguage(Language l) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, l.getId());
        values.put(KEY_LANGUAGE, l.getLanguage());
        values.put(KEY_LANGUAGE_LEVEL, l.getLevel());
        db.insert(TABLE_LANGUAGES, null, values);
        db.close();

    }


    Employee getBasic(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BASIC_INFO, new String[]{KEY_ID,
                        KEY_FNAME, KEY_LNAME, KEY_AGE, KEY_PH_NO,
                        KEY_EMAIL, KEY_TARGET_WORK, KEY_COUNTRY, KEY_CITY, KEY_IMAGE}, KEY_ID + "=?",
                new String[]{id + ""}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Employee e = new Employee(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));

            return e;
        }
        db.close();
        return null;
    }

    Education getEducation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EDUCATION, new String[]{KEY_ID,
                        KEY_MAJOR, KEY_DEGREE, KEY_UNI, KEY_STARTING_DATE,
                        KEY_COMPLETION_DATE}, KEY_ID + "=?",
                new String[]{id + ""}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Education e = new Education(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));

            return e;
        }
        db.close();
        return null;
    }

    Experience getExperience(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPERIENCE, new String[]{KEY_ID,
                        KEY_JOB_IN, KEY_EXPERINCE_DURATION,  KEY_COMPANY_NAME,
                        KEY_JOB_COUNTRY, KEY_JOB_CITY, KEY_DESCRIPTION}, KEY_ID + "=?",
                new String[]{id + ""}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
          //  Experience e = new Experience(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));

          //  return e;
        }
        db.close();
        return null;
    }

//    Language getLanguage(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_LANGUAGES, new String[]{KEY_ID,
//                        KEY_LANGUAGE, KEY_LANGUAGE_LEVEL}, KEY_ID + "=?",
//                new String[]{id + ""}, null, null, null, null);
//        if (cursor != null && cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            Language e = new Language(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
//
//            return e;
//        }
//        db.close();
//        return null;
//    }

    ArrayList<Language> getAllLanguages(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LANGUAGES, new String[]{KEY_ID,
                KEY_LANGUAGE, KEY_LANGUAGE_LEVEL}, KEY_ID + "=?",
                new String[]{id + ""}, null, null, null, null);

        ArrayList<Language> L = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do{

                Language l = new Language(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
                System.out.println("---------"+cursor.getString(0)+"----"+cursor.getString(1)+"--"+cursor.getString(2));
                L.add(l);

            }
            while(cursor.moveToNext());
        }
        for(int j=0;j<L.size();j++)
            System.out.println("--- from handler---"+L.get(j).getLanguage()+"\n");


        return L;
    }


//    ArrayList<String> getAllLanguages(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE_LANGUAGES, new String[]{KEY_ID,
//                        KEY_LANGUAGE, KEY_LANGUAGE_LEVEL}, KEY_ID + "=?",
//                new String[]{id + ""}, null, null, null, null);
//
//        ArrayList<String> L = new ArrayList<>();
//        if(cursor.moveToFirst())
//        {
//            do{
//
//                //Language l = new Language(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
//                System.out.println("---------"+cursor.getString(0)+"----"+cursor.getString(1)+"--"+cursor.getString(2));
//                L.add(cursor.getString(1));
//
//            }
//            while(cursor.moveToNext());
//        }
//        for(int j=0;j<L.size();j++)
//            System.out.println("--- from handler---"+L.get(j)+"\n");
//
//
//        return L;
//    }


}
