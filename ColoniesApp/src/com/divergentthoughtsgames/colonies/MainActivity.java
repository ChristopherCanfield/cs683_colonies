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

import com.divergentthoughtsgames.colonies.R;
import com.divergentthoughtsgames.colonies.util.AnimationUtilities;
import com.divergentthoughtsgames.colonies.util.UiUtilities;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * The main menu.
 * @author Christopher D. Canfield
 */
public class MainActivity extends Activity
{
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.handler = new Handler();
		
		// Set animations (but don't play them yet) and color filters.
		
		ImageView org0View = (ImageView)findViewById(R.id.mainMenuOrganism0);
		org0View.setBackgroundResource(AnimationUtilities.getRandomContentOrganismAnimationId());
		org0View.getBackground().mutate().setColorFilter(UiUtilities.getRandomColor(), PorterDuff.Mode.MULTIPLY);
		
		ImageView org1View = (ImageView)findViewById(R.id.mainMenuOrganism1);
		org1View.setBackgroundResource(AnimationUtilities.getRandomContentOrganismAnimationId());
		org1View.getBackground().mutate().setColorFilter(UiUtilities.getRandomColor(), PorterDuff.Mode.MULTIPLY);
		
		ImageView org2View = (ImageView)findViewById(R.id.mainMenuOrganism2);
		org2View.setBackgroundResource(AnimationUtilities.getRandomContentOrganismAnimationId());
		org2View.getBackground().mutate().setColorFilter(UiUtilities.getRandomColor(), PorterDuff.Mode.MULTIPLY);
		
		ImageView org3View = (ImageView)findViewById(R.id.mainMenuOrganism3);
		org3View.setBackgroundResource(AnimationUtilities.getRandomContentOrganismAnimationId());
		org3View.getBackground().mutate().setColorFilter(UiUtilities.getRandomColor(), PorterDuff.Mode.MULTIPLY);
		
		ImageView org4View = (ImageView)findViewById(R.id.mainMenuOrganism4);
		org4View.setBackgroundResource(AnimationUtilities.getRandomContentOrganismAnimationId());
		org4View.getBackground().mutate().setColorFilter(UiUtilities.getRandomColor(), PorterDuff.Mode.MULTIPLY);
		
		// Continue button.
		((Button)findViewById(R.id.mainMenuContinueButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle b = new Bundle();
				b.putBoolean(GameActivity.LOAD_SAVED_DATA, true);
				Intent intent = new Intent(getApplication(), GameActivity.class);
				intent.putExtras(b);
				MainActivity.this.startActivity(intent);
			}
		});
				
		// New button.
		((Button)findViewById(R.id.mainMenuNewButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.startActivity(new Intent(getApplication(), GameActivity.class));
			}
		});
		
		// How to Play button.
		((Button)findViewById(R.id.mainMenuHowToPlayButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.startActivity(new Intent(getApplication(), HowToPlayActivity.class));
			}
		});
		
		// Statistics button.
		((Button)findViewById(R.id.mainMenuStatsButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.startActivity(new Intent(getApplication(), StatisticsActivity.class));
			}
		});
		
		// Settings button.
//		((Button)findViewById(R.id.mainMenuSettingsButton)).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				MainActivity.this.startActivity(new Intent(getApplication(), SettingsActivity.class));
//				MainActivity.this.finish();
//			}
//		});
	}
	
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		
		if (hasFocus)
		{
			ImageView org0View = (ImageView)findViewById(R.id.mainMenuOrganism0);
			((AnimationDrawable) org0View.getBackground()).start();
			
			// Delay the animation start times to help ensure that the animations don't end up synchronized, which
			// looks odd.
			AnimationUtilities.startAnimationInFuture(this.handler, (ImageView)findViewById(R.id.mainMenuOrganism1), 250);
			AnimationUtilities.startAnimationInFuture(this.handler, (ImageView)findViewById(R.id.mainMenuOrganism2), 500);
			AnimationUtilities.startAnimationInFuture(this.handler, (ImageView)findViewById(R.id.mainMenuOrganism3), 750);
			AnimationUtilities.startAnimationInFuture(this.handler, (ImageView)findViewById(R.id.mainMenuOrganism4), 1000);
		}
	}
	
	
	
}
