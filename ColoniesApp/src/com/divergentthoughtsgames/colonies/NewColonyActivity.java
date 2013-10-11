package com.divergentthoughtsgames.colonies;

/*
Copyright 2013 Christopher D. Canfield


This file is part of Colonies.

Colonies is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Colonies is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Colonies.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.util.Random;

import com.divergentthoughtsgames.colonies.logic.StaticAttributes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

/**
 * The create new colony activity screen.
 * @author Christopher D. Canfield
 */
public class NewColonyActivity extends Activity
{
	/** 
	 * The key for the new colony bundle, which is passed back to the GameActivity
	 * when the user creates a new colony. 
	 **/
	public static final String NEW_COLONY_BUNDLE = "NewColonyBundle"; 


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_colony);
		
		// Add button listeners.
		Button buttonCreate = (Button)findViewById(R.id.newcolony_createButton);
		buttonCreate.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Bundle bundle = NewColonyActivity.this.newColonyInputToBundle();
				Intent intent = new Intent(getApplication(), GameActivity.class);
				intent.putExtra(NEW_COLONY_BUNDLE, bundle);
				
				NewColonyActivity.this.setResult(RESULT_OK, intent);
				NewColonyActivity.this.finish();
			}
		});
		
		
		Button buttonCancel = (Button)findViewById(R.id.newcolony_cancelButton);
		buttonCancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// new Intent(getApplication(), GameActivity.class)
				NewColonyActivity.this.setResult(RESULT_CANCELED, new Intent());
				NewColonyActivity.this.finish();
			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
//		switch (item.getItemId())
//		{
//		case android.R.id.home:
//			// This ID represents the Home or Up button. In the case of this
//			// activity, the Up button is shown. Use NavUtils to allow users
//			// to navigate up one level in the application structure. For
//			// more details, see the Navigation pattern on Android Design:
//			//
//			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
//			//
//			// TODO: If Settings has multiple levels, Up should navigate up
//			// that hierarchy.
//			NavUtils.navigateUpFromSameTask(this);
//			return true;
//		}
		return super.onOptionsItemSelected(item);
	}
	
	
	/**
	 * Returns the input as a bundle.
	 * @return The input as a bundle.
	 */
	private Bundle newColonyInputToBundle()
	{
		Bundle bundle = new Bundle();
		
		String name = ((EditText)findViewById(R.id.newcolony_nameText)).getText().toString();
		if (name.toString().length() == 0)
		{
			name = getRandomColonyName();
		}
		bundle.putString(StaticAttributes.NEW_COLONY_NAME, name);
		
		bundle.putInt(StaticAttributes.NEW_COLONY_CROWD_PREF, 
				((SeekBar)findViewById(R.id.newcolony_crowdPreferenceSeekBar)).getProgress() + 1);
		bundle.putInt(StaticAttributes.NEW_COLONY_HEALTH, 
				((SeekBar)findViewById(R.id.newcolony_maxHealthSeekBar)).getProgress() + 1);
		bundle.putInt(StaticAttributes.NEW_COLONY_HEAT_PREF, 
				((SeekBar)findViewById(R.id.newcolony_heatPreferenceSeekBar)).getProgress() + 1);
		bundle.putInt(StaticAttributes.NEW_COLONY_COUNT, 
				((SeekBar)findViewById(R.id.newcolony_initialOrganismsCountSeekBar)).getProgress() + 2);
		bundle.putInt(StaticAttributes.NEW_COLONY_MAX_AGE, 
				((SeekBar)findViewById(R.id.newcolony_maxAgeSeekBar)).getProgress() + 1);
		bundle.putInt(StaticAttributes.NEW_COLONY_REPRODUCTION_FREQ,
				((SeekBar)findViewById(R.id.newcolony_reproductiveFrequencySeekBar)).getProgress() + 1);
		
		return bundle;
	}
	
	/**
	 * Gets a random name for the Colony.
	 * @return A random name for the Colony.
	 */
	private static String getRandomColonyName()
	{
		Random rand = new Random();
		return "Colony " + rand.nextInt(1000);
	}
}
