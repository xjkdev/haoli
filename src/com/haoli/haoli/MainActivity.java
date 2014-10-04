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
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
//import android.widget.ListView;




@SuppressLint("SimpleDateFormat") public class MainActivity extends Activity {
	private EditText dialog_add_price;
	private EditText dialog_add_time;
	//private ListView listview;
	//private SQLiteDatabase book_db;
	private View dialog_add_layout;
	private BookDatabaseHelper helper;
	private TextView sumlabel;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //LayoutInflater layoutinflater = getLayoutInflater();      
        dialog_add_layout = getLayoutInflater().inflate(R.layout.dialog_add,(ViewGroup)findViewById(R.id.dialog_add));
    	dialog_add_price=(EditText)findViewById(R.id.dialog_add_price);
    	dialog_add_time=(EditText)findViewById(R.id.dialog_add_time);
        //open Database
    	helper = new BookDatabaseHelper(getApplicationContext(),"Book",1);
    	sumlabel = (TextView)findViewById(R.id.sumlabel);
    	
        //book_db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()+"/book.db",null);
    }

    //private void showinlistview(Cursor cursor){
    //	listview.set
    //}
    
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
        	  	
        	dialog_add_time.setText(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.dialog_title)
        	.setView(dialog_add_layout)
        	.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Todo:insertdata	
					insertitems(dialog_add_time.getText().toString(),dialog_add_price.getText().toString(),"","");
					showsum(helper.getReadableDatabase().rawQuery("select * from book_table", null));
				}
			})
        	.setNegativeButton(R.string.cancel,null)
        	.show();
        	
        	return true;
        case R.id.action_settings:  
        	return true;
        default:
        	return super.onOptionsItemSelected(item);	
        }
    }
    
    private void insertitems(String time,String price,String purpose, String way) {
		
		helper.getReadableDatabase().execSQL("insert into news_table values(null, ?, ?, ?, ?)", 
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


