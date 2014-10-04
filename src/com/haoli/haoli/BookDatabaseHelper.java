package com.haoli.haoli;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
//DatabaseOpenHelper
public class BookDatabaseHelper extends SQLiteOpenHelper {
	public BookDatabaseHelper(Context context, String name, int version) {
		super(context,name,null,version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table book_table ("+
	"_id integer primary key autoincrement, "+ 
	"time integer(12), " +
	"price decimal(6,2), "+
	"purpose varchar(10), "+
	"way varchar(10))");
	}
	
	@Override  
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
        System.out.println("call update");  
    }  
}
