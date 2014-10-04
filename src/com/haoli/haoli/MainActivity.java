package com.haoli.haoli;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.Context;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.database.sqlite.SQLiteDatabase.CursorFactory;
//import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;




@SuppressLint("SimpleDateFormat") public class MainActivity extends Activity {
	
	//private View dialog_add_layout;
	private BookDatabaseHelper helper;
	private TextView sumlabel;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //open Database
    	helper = new BookDatabaseHelper(getApplicationContext(),"Book",1);
    	sumlabel = (TextView)findViewById(R.id.sumlabel);
    	showsum(helper.getReadableDatabase().rawQuery("select * from book_table", null));
    }

    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	//if(book_db != null && book_db.isOpen())
    	//	book_db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
        case R.id.action_add:
        	LayoutInflater inflater = this.getLayoutInflater();
        	final View view = (View) inflater.inflate(R.layout.dialog_add, null);
        	//dialog_add_layout =  inflater.inflate(R.layout.dialog_add,null);
        	//dialog_add_time.setText(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
        	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        	builder.setTitle(R.string.dialog_title);
        	builder.setView(view);
        	builder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Todo:insertdata
					EditText dialog_add_price=(EditText)view.findViewById(R.id.dialog_add_price);
					EditText dialog_add_time=(EditText)view.findViewById(R.id.dialog_add_time);
		        	String time=dialog_add_time.getText().toString();
		        	String price = dialog_add_price.getText().toString();
					insertitems(time,price,"","");
		        	//insertitems("12","12","","");
					showsum(helper.getReadableDatabase().rawQuery("select * from book_table", null));
				}
			});
        	builder.setNegativeButton(R.string.cancel,null);
        	AlertDialog alert = builder.create();
        	alert.show();
        	
        	return true;
        case R.id.action_settings:  
        	helper.getReadableDatabase().delete("book_table", null,null);
        	insertitems("0","0","","");
        	showsum(helper.getReadableDatabase().rawQuery("select * from book_table", null));
        	return true;
        case R.id.action_refresh:
        	showsum(helper.getReadableDatabase().rawQuery("select * from book_table", null));
        	return true;
        default:
        	return super.onOptionsItemSelected(item);	
        }
    }
    
    private void insertitems(String time,String price,String purpose, String way) {
		helper.getReadableDatabase().execSQL("insert into book_table values(null, ?, ?, ?, ?)", 
				new String[]{time, price,purpose,way});
	}
    
    private void showsum(Cursor cursor){
    	double sum =0.0;
    	cursor.moveToFirst();
    	int priceColumnIndex = cursor.getColumnIndex("price");
    	while(cursor.moveToNext()){
    		sum += cursor.getFloat(priceColumnIndex);
    	}
    	sumlabel.setText(new DecimalFormat("#.00").format(sum));
    }
}


