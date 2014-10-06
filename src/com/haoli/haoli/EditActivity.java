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
//import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.TextView;
//import android.widget.ListView;

@SuppressLint("SimpleDateFormat")
public class EditActivity extends Activity {
	
	private TextView edit_date;
	private TextView edit_time;
	private TextView edit_price;
	private RadioGroup ways_group;
	//private ListView list_purpose;
	private Intent intent;	
	private int mode;
	private String chosenway;
	protected boolean ischecked;
	
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
		//initialize widgets
		edit_date = (TextView)findViewById(R.id.edit_date);
		edit_time = (TextView)findViewById(R.id.edit_time);
		edit_price = (TextView)findViewById(R.id.edit_price);
		ways_group = (RadioGroup)findViewById(R.id.ways_group); 
		//list_purpose = (ListView)findViewById(R.id.list_purpose);
		intent = getIntent();
		this.mode = intent.getIntExtra("mode", 1);
		this.ischecked = false;
		
		LinearLayout action_ok = (LinearLayout)findViewById(R.id.edit_action_ok);
		LinearLayout action_cancel = (LinearLayout)findViewById(R.id.edit_action_cancel);
		action_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
		
		action_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				passintent();
			}
		});
		
		ways_group.setOnCheckedChangeListener(new OnCheckedChangeListener () {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ischecked = true;
				switch(checkedId){
				case R.id.radioButton_cash:
					chosenway="cash";
					break;
				case R.id.radioButton_card:
					chosenway="card";
					break;
				case R.id.radioButton_alipay:
					chosenway="alipay";
					break;
				case R.id.radioButton_borrow:
					chosenway="borrow";//TODO:borrow from who?
					break;				
				}
				return;
			}
		});
		
		if(mode == 1) {
			edit_date.setText(new SimpleDateFormat(format_date_toread).format(new Date()));
			edit_time.setText(new SimpleDateFormat(format_time_toread).format(new Date()));
			edit_price.setText("0.00");
			//TODO:initialize ListView
			//TODO:Price setting dialog
			
		}
		//TODO:edit mode
		
	}
	
	public void onWayButtonClicked(View view) {
		this.ischecked = ((RadioButton)view).isChecked();
		
		if(this.ischecked) {
			switch(view.getId()){
			case R.id.radioButton_cash:
				chosenway="cash";
				break;
			case R.id.radioButton_card:
				chosenway="card";
				break;
			case R.id.radioButton_alipay:
				chosenway="alipay";
				break;
			case R.id.radioButton_borrow:
				chosenway="borrow";//TODO:borrow from who?
				break;
			
			}
			
		}
	}
	
	public void passintent() {
		if(ischecked){
			Intent result = new Intent();
			result.putExtra("time",new SimpleDateFormat(format_date_tostore+format_time_tostore).format(new Date()));//TODO:changetime
			result.putExtra("price", "12.00");
			result.putExtra("purpose", "undef");
			result.putExtra("way", chosenway);
			setResult(RESULT_OK,result);
			finish();
		}
		else
			 Toast.makeText(this, "Unable to finish", Toast.LENGTH_SHORT).show();
			
	}
}
