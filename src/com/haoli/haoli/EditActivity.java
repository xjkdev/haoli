package com.haoli.haoli;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.TextView;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat")
public class EditActivity extends Activity {
	
	private TextView edit_date;
	private TextView edit_time;
	private TextView edit_price;
	//private ListView list_purpose;
	private Intent intent;	
	private int mode;
	
	final int MODE_NEW = 1;
	final String format_date_toread = "yyyy/MM/dd";
	final String format_time_toread = "hh:mm";
	final String format_date_tostore = "yyyyMMdd";
	final String format_time_tostore = "hhmm";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View custom_actionbar = LayoutInflater.from(this).inflate(R.layout.edit_custom_actionbar, null);
		ActionBar actionbar = getActionBar();
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
		            ActionBar.LayoutParams.MATCH_PARENT,
		            ActionBar.LayoutParams.MATCH_PARENT,
		            Gravity.CENTER);
		actionbar.setCustomView(custom_actionbar,lp);		
		actionbar.setDisplayShowHomeEnabled(false);		
		actionbar.setDisplayShowTitleEnabled(false);
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar.setDisplayShowCustomEnabled(true);
		setContentView(R.layout.activity_edit);
		//init widgets
		edit_date = (TextView)findViewById(R.id.edit_date);
		edit_time = (TextView)findViewById(R.id.edit_time);
		edit_price = (TextView)findViewById(R.id.edit_price);
		//list_purpose = (ListView)findViewById(R.id.list_purpose);
		intent = getIntent();
		mode = intent.getIntExtra("mode", 1);
		
		//LinearLayout action_ok = (LinearLayout)findViewById(R.id.edit_action_ok);
		LinearLayout action_cancel = (LinearLayout)findViewById(R.id.edit_action_cancel);
		action_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
		
		
		if(mode == 1) {
			edit_date.setText(new SimpleDateFormat(format_date_toread).format(new Date()));
			edit_time.setText(new SimpleDateFormat(format_time_toread).format(new Date()));
			edit_price.setText("0.00");
		}
		//TODO:edit mode
		
	}
}
