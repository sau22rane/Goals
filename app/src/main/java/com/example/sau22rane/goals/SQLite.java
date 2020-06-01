package com.example.sau22rane.goals;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SQLite extends SQLiteOpenHelper {
    private SQLiteDatabase db;
        public SQLite(@Nullable Context context, @Nullable String name, String table, int version) {
        super(context, name, null, version);
            Log.d("TAG","works");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TAG","OnCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("TAG","OnUpgrade");
    }

    void create_goal_table(){
        db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS GOAL(ID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, description VARCHAR, time VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS RITUAL(ID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, time VARCHAR, date VARCHAR, rating VARCHAR, goal VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS WORKDONE(ID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, time VARCHAR, date VARCHAR, goal VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS EXPECTATION(ID INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, time VARCHAR, date VARCHAR, goal VARCHAR);");
    }
    boolean add_goal(String title, String description, String time){
        db = this.getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("title",title);
        mContentValues.put("description",description);
        mContentValues.put("time",time);
        long result = db.insert("GOAL", null,mContentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    boolean add_expectation(String title, String time, String date, String goal){
        db = this.getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("title",title);
        mContentValues.put("time",time);
        mContentValues.put("date",date);
        mContentValues.put("goal",goal);
        long result = db.insert("EXPECTATION", null,mContentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    boolean add_ritual(String title, String time, String date, String rating, String goal){
        db = this.getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("title",title);
        mContentValues.put("time",time);
        mContentValues.put("date",date);
        mContentValues.put("rating",rating);
        mContentValues.put("goal",goal);
        long result = db.insert("RITUAL", null,mContentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    boolean add_workdone(String title, String time, String date, String goal){
        db = this.getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put("title",title);
        mContentValues.put("time",time);
        mContentValues.put("date",date);
        mContentValues.put("goal",goal);
        long result = db.insert("WORKDONE", null,mContentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    Cursor get_data(String table){
        db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+table,null);
        return res;
    }
    void delete_row(String table, String title){
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+table+" WHERE title LIKE('"+title+"');");
    }
    void delete_goal(String title){
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM GOAL WHERE title LIKE('"+title+"');");
        db.execSQL("DELETE FROM EXPECTATION WHERE goal LIKE('"+title+"');");
        db.execSQL("DELETE FROM RITUAL WHERE goal LIKE('"+title+"');");
        db.execSQL("DELETE FROM WORKDONE WHERE goal LIKE('"+title+"');");
    }
    void delete_table(String table){
        db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+table+";");
    }
    void update(String table, String title, String new_title, String time, String date){
        db = this.getWritableDatabase();
        db.execSQL("UPDATE "+table+" SET title = '"+new_title+"', time = '"+time+"', date = '"+date+"' WHERE title like('"+title+"');");
    }
    void update_goal(String table, String title, String new_title, String description, String time){
        db = this.getWritableDatabase();
        db.execSQL("UPDATE "+table+" SET title = '"+new_title+"', time = '"+time+"', description = '"+description+"' WHERE title like('"+title+"');");
        db.execSQL("UPDATE WORKDONE SET goal = '"+new_title+"' WHERE goal like('"+title+"');");
        db.execSQL("UPDATE EXPECTATION SET goal = '"+new_title+"' WHERE goal like('"+title+"');");
        db.execSQL("UPDATE RITUAL SET goal = '"+new_title+"' WHERE goal like('"+title+"');");
    }

    void update(String table, String title, String new_title, String time, String date, String rating){
        db = this.getWritableDatabase();
        db.execSQL("UPDATE "+table+" SET title = '"+new_title+"', time = '"+time+"', date = '"+date+"', rating = '"+rating+"' WHERE title like('"+title+"');");
    }
}
