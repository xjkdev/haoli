package com.haoli.haoli;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class EditActivity extends Activity {
	
	private TextView edit_date;
	private TextView edit_time;
	private TextView edit_price;
	private RadioGroup ways_group;
	private ListView list_purpose;
	private Intent intent;	
	private int mode;
	private String chosen_way;
	private String chosen_purpose;
	private List<String> purposes;
	protected boolean ischecked;
	
	final int MODE_NEW = 1;
	final String format_date_toread = "yyyy/MM/dd";
	final String format_time_toread = "hh:mm";
	//final String format_date_tostore = "yyyyMMdd";
	//final String format_time_tostore = "hhmm";
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
		list_purpose = (ListView)findViewById(R.id.list_purpose);
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
					chosen_way="cash";
					break;
				case R.id.radioButton_card:
					chosen_way="card";
					break;
				case R.id.radioButton_alipay:
					chosen_way="alipay";
					break;
				case R.id.radioButton_borrow:
					chosen_way="borrow";//TODO:borrow from who?
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
		
		((TextView) findViewById(R.id.dialog_edit_price_hint)).setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				show_dialog_add_price(0.00);
			}
		});
		
		edit_date.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					dialog_edit_date ();
				} catch (ParseException e){;}
			}
		});
			
		purposes = new ArrayList<String>();
		purposes.add(this.getString(R.string.food));
		purposes.add(this.getString(R.string.transport));
		purposes.add(this.getString(R.string.entertain));
		purposes.add(this.getString(R.string.Else));//TODO:d
		purpose_adapter list_adapter;
		list_adapter = new purpose_adapter(this, purposes);
		list_purpose.setAdapter(list_adapter);
		
		if(mode == 1) {
			edit_date.setText(new SimpleDateFormat(format_date_toread).format(new Date()));
			edit_time.setText(new SimpleDateFormat(format_time_toread).format(new Date()));
			edit_price.setText(new DecimalFormat("0.00").format(0.00));
			show_dialog_add_price(0.00);			
		}
		//TODO:edit mode
		
	}
	
	public void passintent() {
		if(ischecked){
			Intent result = new Intent();
			String date = edit_date.getText().toString();
			date=date.replaceAll("/", "");
			String time = edit_time.getText().toString();
			time = time.replace(":", "");
			
			for(int i=0, j=list_purpose.getCount();i<j;i++) {
				View child = list_purpose.getChildAt(i);
				RadioButton btn = (RadioButton) child.findViewById(R.id.purpose_radiobtn);
				if(btn.isChecked())
					chosen_purpose = purposes.get(i);
			}
			
			result.putExtra("time",date+time);
			result.putExtra("price", edit_price.getText().toString());
			result.putExtra("purpose", chosen_purpose);
			result.putExtra("way", chosen_way);
			setResult(RESULT_OK,result);
			finish();
		} else {
			 Toast.makeText(this, "Unable to finish", Toast.LENGTH_SHORT).show();
		}
			
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
		if(num != 10 && num != -1) {
			before = before * 10 + num*0.01;
			if(before > 999999.99)
				before=deletenum(before);
		}
		else if(num == 10) {
			before *= 100;
			if(before > 999999.99)
				before=deletenum(deletenum(before));
		}
		if(num == -1) {
			before = deletenum(before);
		}
		if(num == -2) {
			before = 0.00;
		}
		tochange.setText(new DecimalFormat("0.00").format(before));
	}
	
	public final double deletenum (double before) {
		return (double) ((long)(before*10)/100.0);
	}
	
	public void dialog_edit_date () throws ParseException {
		Date date =new Date();
		
		DatePickerDialog dialog = new DatePickerDialog(this,
				new OnDateSetListener() {			
			@Override
			public void onDateSet(DatePicker view, int year, int month, int day) {
				Date date =new Date();
				int now_year=9999,now_month=13,now_day=32;
				try {
					now_year = new DecimalFormat("0000").parse(new SimpleDateFormat("yyyy").format(date)).intValue();
					now_month = new DecimalFormat("00").parse(new SimpleDateFormat("MM").format(date)).intValue()-1;
					now_day = new DecimalFormat("00").parse(new SimpleDateFormat("dd").format(date)).intValue();
				} catch (ParseException e) {;}
				if(year > now_year || month > now_month || day > now_day) {
					Toast.makeText(EditActivity.this, "I believe you can't see what future will be. ", Toast.LENGTH_SHORT).show();
					return;
				}
				edit_date.setText(new DecimalFormat("0000").format(year) + '/' +  new DecimalFormat("00").format(month+1) +'/' + new DecimalFormat("00").format(day));
			}			
		},
		new DecimalFormat("0000").parse(new SimpleDateFormat("yyyy").format(date)).intValue(),
		new DecimalFormat("00").parse(new SimpleDateFormat("MM").format(date)).intValue()-1,
		new DecimalFormat("00").parse(new SimpleDateFormat("dd").format(date)).intValue());
		dialog.show();		
	}
	
	public void dialog_edit_time () throws ParseException {
		Date date = new Date();
		TimePickerDialog dialog = new TimePickerDialog(
				this,
				new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker arg0, int hour, int minute) {
						edit_date.setText(new DecimalFormat("00").format(hour) + ':' +  new DecimalFormat("00").format(minute)); 
					}
					
				},
				new DecimalFormat("00").parse(new SimpleDateFormat("hh").format(date)).intValue(),
				new DecimalFormat("00").parse(new SimpleDateFormat("mm").format(date)).intValue(),
				false);
		dialog.show();
	}
	
}
