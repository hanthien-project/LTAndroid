package com.example.notemanagersystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;

import com.example.notemanagersystem.ui.category.Category;
import com.example.notemanagersystem.ui.note.Note;
import com.example.notemanagersystem.ui.priority.Priority;
import com.example.notemanagersystem.ui.status.Status;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context)  {
        super(context, "Login.db", null,15);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(email text primary key, password text);");
        db.execSQL("Create table profile(id integer primary key AUTOINCREMENT NOT NULL, email text , firstname text, lastname text);");
        db.execSQL("Create table category (id integer primary key AUTOINCREMENT NOT NULL, email text, name text, createdate text);");
        db.execSQL("Create table priority (id integer primary key AUTOINCREMENT NOT NULL, email text, name text, createdate text);");
        db.execSQL("Create table status (id integer primary key AUTOINCREMENT NOT NULL, email text, name text, createdate text);");
        db.execSQL("Create table note (id integer primary key AUTOINCREMENT NOT NULL, email text, name text, category text, priority text, status text, plandate text, createdate, text);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists profile");
        db.execSQL("drop table if exists category");
        db.execSQL("drop table if exists priority");
        db.execSQL("drop table if exists status");
        db.execSQL("drop table if exists note");
        onCreate(db);
    }

    public boolean insertAccout(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = db.insert("user", null, contentValues);
        if (ins == -1)
            return false;
        return true;
    }
    //Kiểm tra email tồn tại hay không
    public boolean checkEmail(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email =?", new String[]{email});
        if (cursor.getCount() == 0)
            return false;
        return true;
    }


    //Kiểm tra password nhập có đúng với password hiện tại hay không
    public boolean checkCurrentPassowrd(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select password from user where email =?", new String[]{email});
        while (cursor.moveToNext()) {
            String currentPassword = cursor.getString(0);   //0 is the number of id column in your database table
            if (currentPassword.equals(password))
                return true;
        }
        return false;
    }

    //get Profile
    public List<String> getAllDataProfile(String email) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from profile where email = ?", new String[]{email});
        while (cursor.moveToNext()) {
            String firstname = cursor.getString(2);   //0 is the number of id column in your database table
            String lastname = cursor.getString(3);
            list.add(firstname);
            list.add(lastname);
        }
        return list;
    }

    //--------------------------------------PROFILE-----------------------------------------------//

    //Insert profile
    public boolean insertProfile(String email, String firstname, String lastname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        long ins = db.insert("profile", null, contentValues);
        if (ins == -1)
            return false;
        return true;
    }

    //---------------------------------------NOTE-------------------------------------------------//
    //insert Category, Priority, Status
    public boolean insert3Value(String table_name, String email, String name, String createdate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("name", name);
        contentValues.put("createdate", createdate);
        long ins = db.insert(table_name, null, contentValues);
        if (ins == -1)
            return false;
        return true;
    }

    // insert Status
    public boolean insertNote(String email, String name, String category, String priority, String status, String plandate, String createdate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("name", name);
        contentValues.put("category", category);
        contentValues.put("priority", priority);
        contentValues.put("status", status);
        contentValues.put("plandate", plandate);
        contentValues.put("createdate", createdate);
        long ins = db.insert("note", null, contentValues);
        if (ins == -1)
            return false;
        return true;
    }

    public void deleteCategoryByName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("category", "name = ?", new String[]{(name)});
    }
    public void deletePriorityByName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("priority", "name = ?", new String[]{(name)});
    }
    public void deleteStatusByName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("status", "name = ?", new String[]{(name)});
    }
    public void deleteNoteByName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("note", "name = ?", new String[]{(name)});
    }



    //Lấy dữ liệu Category
    public List<Category> getAllDataCategory(String email) {
        List<Category> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from category where email = ?", new String[]{email});
        while(cursor.moveToNext()) {
            String name = cursor.getString(2);   //0 is the number of id column in your database table
            String createdate = cursor.getString(3);
            Category category = new Category(name, createdate);
            categoryList.add(category);
        }
        return categoryList;
    }

    //Lấy dữ liệu Priority
    public List<Priority> getAllDataPriority(String email) {
        List<Priority> priorityList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from priority where email = ?", new String[]{email});
        while(cursor.moveToNext()) {
            String name = cursor.getString(2);   //0 is the number of id column in your database table
            String createdate = cursor.getString(3);
            Priority priority = new Priority(name, createdate);
            priorityList.add(priority);
        }
        return priorityList;
    }

    //Lấy dữ liệu Status
    public List<Status> getAllDataStatus(String email) {
        List<Status> statusList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from status where email = ?", new String[]{email});
        while(cursor.moveToNext()) {
            String name = cursor.getString(2);   //0 is the number of id column in your database table
            String createdate = cursor.getString(3);
            Status status = new Status(name, createdate);
            statusList.add(status);
        }
        return statusList;
    }

    //Lấy dữ liệu Note
    public List<Note> getAllDataNote(String email) {
        List<Note> noteList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from note where email = ?", new String[]{email});
        while(cursor.moveToNext()) {
            String name = cursor.getString(2);   //0 is the number of id column in your database table
            String category = cursor.getString(3);
            String priority = cursor.getString(4);
            String status = cursor.getString(5);
            String plandate = cursor.getString(6);
            String createdate = cursor.getString(7);
            Note note = new Note(name, category, priority, status, plandate, createdate);
            noteList.add(note);
        }
        return noteList;
    }

    //Lay số lượng note bằng status
    //Lấy dữ liệu Note
    public int getValueByStatus(String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from note where status = ?", new String[]{status});
        return cursor.getCount();
    }

    //Đổi Profile
    public boolean changeProfile(String email, String firstname, String lastname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        long ins = db.update("profile", contentValues, "email = ?", new String[]{email});
        if (ins == -1)
            return false;
        return true;
    }

    //Đổi Password
    public boolean changePassword(String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long ins = db.update("user", contentValues, "email = ?", new String[]{email});
        if (ins == -1)
            return false;
        return true;
    }

    public void updateCategoryByName(String cname, String email, String name, String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("name", name);
        contentValues.put("date", date);
        db.update("category", contentValues, "name = ?", new String[]{cname});
    }

}