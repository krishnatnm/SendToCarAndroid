package com.jvanier.android.sendtocar.controllers.commands;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.jvanier.android.sendtocar.R;
import com.jvanier.android.sendtocar.common.Mixpanel;
import com.jvanier.android.sendtocar.common.Constants;

public class WriteEmailToDeveloper implements Command {
	@Override
	public void perfrom(Context context) {
		Intent intent = new Intent(Intent.ACTION_VIEW); 
		intent.setData(Uri.parse("mailto:" + Constants.DEVELOPER_EMAIL)); 
		try {
			context.startActivity(intent);
			
			JSONObject props = new JSONObject();
			try {
				props.put("Action", "Email developer");
			} catch (JSONException e) {
			}
			Mixpanel.sharedInstance().track("Perform info dialog action", props);
		}
		catch(ActivityNotFoundException e)
		{
			Toast toast = Toast.makeText(context, R.string.errorNoApp, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}