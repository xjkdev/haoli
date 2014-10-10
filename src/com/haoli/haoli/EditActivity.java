package com.haoli.haoli;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
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
		
		edit_price.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				show_dialog_add_price(0.00);
			}
		});
		
		//((TextView) findViewById(R.id.dialog_edit_price_hint)).setOnClickListener(new OnClickListener(){
		//	@Override
		//	public void onClick(View v) {
			//	show_dialog_add_price(0.00);
			//}
		//});
		
		if(mode == 1) {
			edit_date.setText(new SimpleDateFormat(format_date_toread).format(new Date()));
			edit_time.setText(new SimpleDateFormat(format_time_toread).format(new Date()));
			edit_price.setText("0.00");
			show_dialog_add_price(0.00);
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
			result.putExtra("price", edit_price.getText().toString());
			result.putExtra("purpose", "undef");
			result.putExtra("way", chosenway);
			setResult(RESULT_OK,result);
			finish();
		}
		else
			 Toast.makeText(this, "Unable to finish", Toast.LENGTH_SHORT).show();
			
	}
	
	public void show_dialog_add_price(double Default) {
		LayoutInflater inflater = this.getLayoutInflater();
		final View view = (View) inflater.inflate(R.layout.dialog_add_price, null);
		final TextView dialog_edit_price = (TextView) view.findViewById(R.id.dialog_edit_price);
		((TextView) view.findViewById(R.id.number1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(1,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(1,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number2)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(2,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number3)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(3,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number4)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(4,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number5)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(5,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number6)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(6,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number7)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(7,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number8)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(8,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number9)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(9,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number0)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(0,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.number00)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(10,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.button_del)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog_add_number(-1,dialog_edit_price);
			}
		});
		
		((TextView) view.findViewById(R.id.button_del)).setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				dialog_add_number(-2,dialog_edit_price);
				return true;
			}
		});
		
		//dialog_edit_price.setText(new DecimalFormat("0.00").format(Default));
		dialog_edit_price.setText(edit_price.getText());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_add_price_title);
		builder.setView(view);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
								public void onClick(DialogInterface dialog, int which) {
								TextView dialog_edit_price = (TextView) view.findViewById(R.id.dialog_edit_price);
								changeEditPrice(dialog_edit_price.getText().toString());
							}
						});
		builder.setNegativeButton(android.R.string.cancel,null);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void changeEditPrice(String text) {
		edit_price.setText(text);
	}
	
	public void dialog_add_number (int num, TextView tochange) {
		double before;
		try {
			before = new DecimalFormat("0.00").parse(tochange.getText().toString()).doubleValue();
		} catch (ParseException e) {
			before = 0.00;
		}
		if(before < 99999.99) {
		if(num != 10 && num != -1)
			before = before * 10 + num*0.01;
		else if(num == 10)
			before *= 100;
		}
		if(num == -1) {
			before = (double) ((long)(before*10)/100.0);
		}
		if(num == -2) {
			before = 0.00;
		}
		tochange.setText( new DecimalFormat("0.00").format(before));
	}
}
