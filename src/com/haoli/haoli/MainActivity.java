package com.haoli.haoli;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
//import android.widget.Toast;


public class MainActivity extends Activity {
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        	LayoutInflater layoutinflater = getLayoutInflater();
        	View layout = layoutinflater.inflate(R.layout.dialog_add,(ViewGroup)findViewById(R.id.dialog_edit_add));
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.dialog_title)
        	.setView(layout)
        	.setPositiveButton(R.string.okay, null)//TODO:OK
        	.setNegativeButton(R.string.cancel,null)
        	.show();
        	return true;
        case R.id.action_settings:  
        	return true;
        default:
        	return super.onOptionsItemSelected(item);	
        }
        //return super.onOptionsItemSelected(item);
    }

}
