package com.haoli.haoli;

import java.util.HashMap;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class purpose_adapter extends BaseAdapter {
	private Context context;
	private List<String> purpose_List;
	public HashMap<String,Boolean>states = new HashMap<String,Boolean>();
	public String chosen;
	
	public purpose_adapter (Context context, List<String> purpose_List) {
		this.context = context;
		this.purpose_List = purpose_List;
	}
	
	@Override
	public int getCount() {
		return purpose_List.size();
	}
	
	@Override
	public Object getItem(int position) {
		return purpose_List.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(final int position,View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.purpose_items, null);
			holder = new ViewHolder();
			holder.background =(LinearLayout) convertView.findViewById(R.id.purpose_items);
			holder.purpose = (TextView) convertView.findViewById(R.id.purpuse_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RadioButton radio = (RadioButton) convertView.findViewById(R.id.purpose_radiobtn);
		holder.rdBtn = radio;
		holder.purpose.setText(purpose_List.get(position));
		
		holder.rdBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				for(String key:states.keySet()){
					states.put(key, false);
				}
				states.put(String.valueOf(position), radio.isChecked());
				chosen=String.valueOf(position);
				purpose_adapter.this.notifyDataSetChanged();
			}
		});
		
		boolean res = false;
		if(states.get(String.valueOf(position))==null || states.get(String.valueOf(position)) == false) {
			res = false;
			states.put(String.valueOf(position), false);
		} else {
			res = true;
		}
		
		holder.rdBtn.setChecked(res);
		return convertView;
	}
	
	static class ViewHolder {
		LinearLayout background;
		TextView purpose;
		RadioButton rdBtn;
	}
}
