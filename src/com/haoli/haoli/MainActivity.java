package com.haoli.haoli;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;




@SuppressLint("SimpleDateFormat") public class MainActivity extends Activity {
	
	private BookDatabaseHelper book_db;
	private TextView sumlabel;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //open Database
    	book_db = new BookDatabaseHelper(getApplicationContext(),"Book",1);
    	sumlabel = (TextView)findViewById(R.id.sumlabel);
    	showsum(book_db.getReadableDatabase().rawQuery("select * from book_table", null));
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
        	EditText dialog_add_time=(EditText)view.findViewById(R.id.dialog_add_time);
        	dialog_add_time.setText(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
        	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        	builder.setTitle(R.string.dialog_title);
        	builder.setView(view);
        	builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//insert data to database
					EditText dialog_add_time=(EditText)view.findViewById(R.id.dialog_add_time);
					EditText dialog_add_price=(EditText)view.findViewById(R.id.dialog_add_price);
					EditText dialog_add_purpose = (EditText) view.findViewById(R.id.dialog_add_purpose);
					EditText dialog_add_way = (EditText) view.findViewById(R.id.dialog_add_way);
		        	String time=dialog_add_time.getText().toString();
		        	String price = dialog_add_price.getText().toString();
		        	String purpose =dialog_add_purpose.getText().toString();
		        	String way = dialog_add_way.getText().toString();
					insertitems(time,price,purpose,way);
					showsum(book_db.getReadableDatabase().rawQuery("select * from book_table", null));
				}
			});
        	builder.setNegativeButton(android.R.string.cancel,null);
        	AlertDialog alert = builder.create();
        	alert.show();
        	
        	return true;
        case R.id.action_settings:  
        	book_db.getReadableDatabase().delete("book_table", null,null);
        	insertitems("0","0","","");
        	showsum(book_db.getReadableDatabase().rawQuery("select * from book_table", null));
        	return true;
        case R.id.action_refresh:
        	showsum(book_db.getReadableDatabase().rawQuery("select * from book_table", null));
        	return true;
        default:
        	return super.onOptionsItemSelected(item);	
        }
    }
    
    private void insertitems(String time,String price,String purpose, String way) {
    	book_db.getReadableDatabase().execSQL("insert into book_table values(null, ?, ?, ?, ?)", 
				new String[]{time, price,purpose,way});
	}
    
    private void showsum(Cursor cursor){
    	double sum =0.0;
    	cursor.moveToFirst();
    	int priceColumnIndex = cursor.getColumnIndex("price");
    	while(cursor.moveToNext()){
    		sum += cursor.getFloat(priceColumnIndex);
    	}
    	sumlabel.setText(new DecimalFormat("0.00").format(sum));
    }
}


