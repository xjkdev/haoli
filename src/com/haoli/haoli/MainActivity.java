package com.haoli.haoli;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends Activity {
	
	//private View dialog_add_layout;
	private BookDatabaseHelper book_db;
	private TextView sumlabel;
	private ListView booklist;
	final int MODE_NEW = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //open Database
        book_db = new BookDatabaseHelper(getApplicationContext(),"Book",1);
    	sumlabel = (TextView)findViewById(R.id.sumlabel);
    	booklist = (ListView)findViewById(R.id.booklist);
    	Cursor query = book_db.getReadableDatabase().rawQuery("select * from book_table", null);
    	updatelist(query);
    	showsum(query);
    	query.close();
    }

    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(book_db != null)
    		book_db.close();
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
        	//call edit activity
        	Intent intent = new Intent();
        	intent.setClass(MainActivity.this,EditActivity.class);
        	intent.putExtra("mode", MODE_NEW);
        	startActivityForResult(intent,1);
        	
        	return true;
        case R.id.action_settings:  
        	book_db.getReadableDatabase().delete("book_table", null,null);
        	//insertitems("0","0","","");
        	Cursor query = book_db.getReadableDatabase().rawQuery("select * from book_table", null);
	    	updatelist(query);
	    	showsum(query);
	    	query.close();
        	return true;
        case R.id.action_refresh:

        	return true;
        default:
        	return super.onOptionsItemSelected(item);	
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode != 1)
    		return;
    	if(resultCode == RESULT_CANCELED) {
    		return;
    	}
    	else if(resultCode == RESULT_OK) {
    		Toast.makeText(this, data.getStringExtra("way"), Toast.LENGTH_SHORT).show();
    		
    		insertitems(data.getStringExtra("time"),data.getStringExtra("price"),data.getStringExtra("purpose"),data.getStringExtra("way"));
    		Cursor query = book_db.getReadableDatabase().rawQuery("select * from book_table", null);
	    	updatelist(query);
	    	showsum(query);
	    	query.close();
    		return;
    	}
    }
    
    private void insertitems(String time,String price,String purpose, String way) {
    	book_db.getReadableDatabase().execSQL("insert into book_table values(null, ?, ?, ?, ?)", 
				new String[]{time, price,purpose,way});
	}
    
    private double showsum(Cursor cursor){
    	double sum =0.0;
    	int priceColumnIndex = cursor.getColumnIndex("price");
    	
    	if(cursor.moveToFirst()) 
    		do {
    		sum += cursor.getDouble(priceColumnIndex);
    		}while(cursor.moveToNext());
    	sumlabel.setText(new DecimalFormat("0.00").format(sum));
    	return sum;
    }
    
    private void updatelist(Cursor cursor){
    	int priceColumnIndex = cursor.getColumnIndex("price");
    	int timeColumnIndex = cursor.getColumnIndex("time");
    	int purposeColumnIndex = cursor.getColumnIndex("purpose");
    	int wayColumnIndex = cursor.getColumnIndex("way");
    	final DecimalFormat format = new DecimalFormat();    	
    	format.setMaximumFractionDigits(2);
    	format.setMinimumFractionDigits(2);
    	format.setGroupingSize(3);
    	format.setRoundingMode(RoundingMode.FLOOR);
    	//int wayColumnIndex = cursor.getColumnIndex("purpose");
    	ArrayList<Map<String,Object>> mitems = new ArrayList<Map<String,Object>>();
    	if(cursor.moveToLast()) {
    		do {
    			Map<String,Object> item = new HashMap<String,Object>();
    			String way = cursor.getString(wayColumnIndex);
    			int drawable_id = R.drawable.cash;
    			int a = 0xff,r = 0x56,g = 0x77,b = 0xfc;
    			if(way.compareTo("alipay") == 0)
    				drawable_id = R.drawable.alipay;
    			else if (way.compareTo("cash") == 0)
    				drawable_id = R.drawable.cash;
    			else if (way.compareTo("card") == 0)
    				drawable_id = R.drawable.card;
    			
        		item.put("items_way_img", draw_way(drawable_id,a,r,g,b));//TODO:pic
        		item.put("items_time",cursor.getLong(timeColumnIndex));
        		item.put("items_purpose",cursor.getString(purposeColumnIndex));
        		item.put("items_price", format.format(cursor.getDouble(priceColumnIndex)));
        		mitems.add(item);
    		}while(cursor.moveToPrevious());
    	}
    	SimpleAdapter adapter = new SimpleAdapter(this,mitems,R.layout.items,
    			new String[]{"items_way_img","items_time","items_purpose","items_price"},
    			new int[]{R.id.items_way_img,R.id.items_time,R.id.items_purpose,R.id.items_price});
    	adapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Object data, String textRepresntation) {
				if((view instanceof ImageView) && (data instanceof Bitmap)) {
					ImageView imageView = (ImageView) view;
					Bitmap bm = (Bitmap) data;
					imageView.setImageBitmap(bm);
					return true;
				}
				return false;
			}
    		
    	});
    	booklist.setAdapter(adapter);
    }
    
    public Bitmap draw_way (int drawable,int a,int r, int g,int b) {
    	Bitmap way = BitmapFactory.decodeResource(getResources(), drawable);
    	int width , height ;
    	width = way.getWidth();
    	height = way.getHeight();
    	Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(result);
    	Paint p = new Paint();
    	p.setARGB(a,r,g,b);
    	canvas.drawCircle(width/2, height/2, (width+height)/4, p);
    	canvas.drawBitmap(way, 0, 0, p);
    	return result;
    }
}


