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

import java.util.Locale;
import java.util.Random;

import com.divergentthoughtsgames.colonies.event.AllOrganismsRequestedEvent;
import com.divergentthoughtsgames.colonies.event.AllOrganismsRequestedResponse;
import com.divergentthoughtsgames.colonies.event.GameEvent;
import com.divergentthoughtsgames.colonies.event.EventListener;
import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.NewColonyPlacedData;
import com.divergentthoughtsgames.colonies.event.NewColonyPlacedEvent;
import com.divergentthoughtsgames.colonies.event.OrganismBornData;
import com.divergentthoughtsgames.colonies.event.OrganismBornEvent;
import com.divergentthoughtsgames.colonies.event.OrganismDiedEvent;
import com.divergentthoughtsgames.colonies.event.OrganismHappinessChangedEvent;
import com.divergentthoughtsgames.colonies.event.OrganismInfo;
import com.divergentthoughtsgames.colonies.event.OrganismInfoRequestedEvent;
import com.divergentthoughtsgames.colonies.event.OrganismInfoRequestedResponse;
import com.divergentthoughtsgames.colonies.event.OrganismPoppedEvent;
import com.divergentthoughtsgames.colonies.logic.GridPosition;
import com.divergentthoughtsgames.colonies.logic.StaticAttributes;
import com.divergentthoughtsgames.colonies.logic.attributes.Happiness;
import com.divergentthoughtsgames.colonies.util.AnimationUtilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * The Activity that displays the game.
 * @author Christopher D Canfield
 */
public class GameActivity extends Activity implements EventListener
{
	/** The number of rows in the game grid. **/
	public static final int GRID_ROWS = 10;
	/** The number of columns in the game grid. **/
	public static final int GRID_COLUMNS = 7;
	
	// Bundle key indicating whether saved data should be loaded.
	public static final String LOAD_SAVED_DATA = "Load Saved Data";
	
	// Recommended by http://developer.android.com/reference/android/util/Log.html
	private static final String LOG_TAG = "GameActivity";
	
	// Lists the Organisms, represented by ImageViews, that are located on the grid.
	private ImageView[][] gridImageViews;
	
	// Variables related to placing new colonies.
	// Specifies whether the next touch should cause a colony to be placed.
	private boolean isPlacingColony = false;
	private StaticAttributes newColonyAttributes;
	private int numberOfOrganismsToPlace;
	
	// Specifies whether the next touch should cause an organism to pop.
	private boolean isPoppingOrganism = false;
	
	// Handler for the events.
	private Handler uiEventHandler;
	
	private App app;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Create the Handler.
		this.uiEventHandler = new Handler();
		
		this.app = (App)this.getApplication();
		
		this.gridImageViews = new ImageView[GRID_ROWS][GRID_COLUMNS];
		
		// Set the OnClickListeners for the Add New Colony clickables.
		ImageView addNewColonyImageView = (ImageView)findViewById(R.id.newColonyImage);
		TextView addNewColonyTextView = (TextView)findViewById(R.id.newColonyText);
		RelativeLayout addNewColonyLayout = (RelativeLayout)findViewById(R.id.newColonyLayout);
		addNewColonyImageView.setOnClickListener(new NewColonyListener());
		addNewColonyTextView.setOnClickListener(new NewColonyListener());
		addNewColonyLayout.setOnClickListener(new NewColonyListener());
		
		// Set the OnClickListener for the Pop button
		TextView popOrganismTextView = (TextView)findViewById(R.id.popOrganismText);
		popOrganismTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				GameActivity.this.isPoppingOrganism = true;
			}
		});
		
		setGridClickListeners();
		
		boolean loadSavedData = getIntent().hasExtra(LOAD_SAVED_DATA);
		if (loadSavedData)
		{
			this.app.loadGameState();
			this.app.startSimulation();
		}
		else
		{
			this.app.resetSimulation();
		}
		
		subscribeToEvents();
		
		if (loadSavedData)
		{
			this.app.getEventManager().notify(new AllOrganismsRequestedEvent());
		}
	}
	
	private void subscribeToEvents()
	{
		EventManager manager = this.app.getEventManager();
		manager.subscribe(OrganismBornEvent.ID, this);
		manager.subscribe(OrganismDiedEvent.ID, this);
		manager.subscribe(OrganismInfoRequestedResponse.ID, this);
		manager.subscribe(AllOrganismsRequestedResponse.ID, this);
		manager.subscribe(OrganismHappinessChangedEvent.ID, this);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		if (hasFocus)
		{
			this.app.getGameManager().unpause();
		}
		else
		{
			this.app.getGameManager().pause();
		}
	}
	
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		Log.d(LOG_TAG, "onActivityResult called: " + resultCode);
		if (resultCode == RESULT_OK && data.hasExtra(NewColonyActivity.NEW_COLONY_BUNDLE))
		{
			Log.d(LOG_TAG, "getting data from bundle");
			
			Bundle b = data.getBundleExtra(NewColonyActivity.NEW_COLONY_BUNDLE);
			
			StaticAttributes att = StaticAttributes.fromBundle(b);
			this.isPlacingColony = true;
			this.newColonyAttributes = att;
			this.numberOfOrganismsToPlace = b.getInt(StaticAttributes.NEW_COLONY_COUNT);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		
		boolean loadSavedData = getIntent().hasExtra(LOAD_SAVED_DATA);
		if (loadSavedData)
		{
			this.app.loadGameState();
			subscribeToEvents();
		}
		
		if (loadSavedData)
		{
			this.app.getEventManager().notify(new AllOrganismsRequestedEvent());
		}
		Log.d(LOG_TAG, "onPostCreate called");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}
	
	public Handler getUiEventHandler()
	{
		return this.uiEventHandler;
	}

	
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) 
	{
		// Additional info: http://developer.android.com/training/basics/activity-lifecycle/recreating.html
		
	    super.onSaveInstanceState(savedInstanceState);
	}
	
	
	@Override 
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		this.app.loadGameState();
		Log.d(LOG_TAG, "onRestoreInstanceState called");
	}
	
	@Override
	protected void onResume()
	{		
		this.app.startSimulation();
		
		super.onResume();
	}
	
	@Override
	protected void onPause()
	{
		this.app.stopSimulation();
		this.app.saveGameState();
		
		super.onPause();
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	
	@Override
	public void notify(GameEvent<?> event)
	{
		// TODO (2013-04-10): Handle these events.
		if (event instanceof OrganismInfoRequestedResponse)
		{
			OrganismInfoRequestedResponseEventProcessor processor = new OrganismInfoRequestedResponseEventProcessor();
			processor.setInfo(((OrganismInfoRequestedResponse)event).getData());
			this.uiEventHandler.post(processor);
			Log.d(LOG_TAG, "OrganismInfoRequestedResponseEvent received");
		}
		else if (event instanceof OrganismDiedEvent)
		{
			OrganismDiedEventProcessor processor = new OrganismDiedEventProcessor();
			processor.data = ((OrganismDiedEvent)event).getData();
			this.uiEventHandler.post(processor);
			
		}
		else if (event instanceof OrganismBornEvent)
		{
			OrganismBornEventProcessor processor = new OrganismBornEventProcessor();
			processor.data = ((OrganismBornEvent)event).getData();
			this.uiEventHandler.post(processor);
			Log.d(LOG_TAG, "OrganismBornEvent received");
		}
		else if (event instanceof OrganismHappinessChangedEvent)
		{
			OrganismHappinessChangedEventProcessor processor = 
					new OrganismHappinessChangedEventProcessor((OrganismHappinessChangedEvent)event);
			this.uiEventHandler.post(processor);
			Log.d(LOG_TAG, "OrganismHapinessChangedEvent received");
		}
		else if (event instanceof AllOrganismsRequestedResponse)
		{
			AllOrganismsRequestedResponseEventProcessor processor = 
					new AllOrganismsRequestedResponseEventProcessor((AllOrganismsRequestedResponse)event);
			this.uiEventHandler.post(processor);
			Log.d(LOG_TAG, "OrganismRequestedResponseEvent received");
		}
		else
		{
			throw new IllegalArgumentException("Unhandled event received by notify: " + event);
		}
	}
	
	/**
	 * Processes the OrganismBornEvent.
	 * @author Christopher D Canfield
	 */
	private class OrganismBornEventProcessor implements Runnable
	{
		public OrganismBornData data;
		
		@Override
		public void run()
		{
			final int animationResourceId = AnimationUtilities.getRandomGrowOrganismAnimationId();
			
			GridPosition loc = this.data.getLocation();
			ImageView v = GameActivity.this.gridImageViews[loc.getRow()][loc.getColumn()]; 
			v.setBackgroundResource(animationResourceId);
			v.getBackground().mutate().setColorFilter(this.data.getAttributes().getColor(), PorterDuff.Mode.MULTIPLY);
			
			// Vary the start times of the animations, to help ensure that they aren't all in sync, which does not look right.
			final Random rand = new Random();
			final int startDelay = rand.nextInt(2000);
			AnimationUtilities.startAnimationInFuture(GameActivity.this.uiEventHandler, v, startDelay);
			
			// Replace the growing animation with adult animation once the child animation has completed, plus some
			// additional time.
			final long DELAY_TIME_MILLIS = 500;
			final long adultAnimationDelay = 
					AnimationUtilities.getAnimationDuration((AnimationDrawable)v.getBackground()) + startDelay + DELAY_TIME_MILLIS;
			final int adultAnimationId = AnimationUtilities.getRandomContentOrganismAnimationId();
			
			AnimationUtilities.replaceAnimationInFuture(
					GameActivity.this.uiEventHandler, v, adultAnimationId, this.data.getAttributes().getColor(), adultAnimationDelay);
		}
	}
	
	
	private class OrganismDiedEventProcessor implements Runnable
	{
		public GridPosition data;
		
		@Override
		public void run()
		{
			GridPosition loc = this.data;
			ImageView v = GameActivity.this.gridImageViews[loc.getRow()][loc.getColumn()]; 
			v.setBackgroundResource(R.drawable.organism_dead);
			v.getBackground().setColorFilter(null);
		}
	}
	
	
	/**
	 * Processes the OrganismInfoRequestedResponseEvent.
	 * @author Christopher D Canfield
	 */
	private class OrganismInfoRequestedResponseEventProcessor implements Runnable
	{
		private OrganismInfo info;
		
		public void setInfo(OrganismInfo info)
		{
			if (info == null)
				throw new IllegalArgumentException("Argument 'info' cannot be null.");
			this.info = info;
		}
		
		@Override
		public void run()
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
		    LayoutInflater inflater = getLayoutInflater();
	
		    View view = inflater.inflate(R.layout.dialog_organism_info, null);
		    
		    TextView nameTextView = (TextView)view.findViewById(R.id.organismInfoName);
		    nameTextView.setText(this.info.getName());
		    
		    TextView colonyTextView = (TextView)view.findViewById(R.id.organismInfoColony);
		    colonyTextView.setText(this.info.getStaticAttributes().getColonyName());
		    
		    TextView happinessTextView = (TextView)view.findViewById(R.id.organismInfoHappiness);
		    happinessTextView.setText(this.info.getDynamicAttributes().getHappiness().toString());
		    
		    TextView crowdHappinessTextView = (TextView)view.findViewById(R.id.organismInfoCrowdHappiness);
		    crowdHappinessTextView.setText(this.info.getDynamicAttributes().getCrowdHappiness().toString());
		    
		    TextView ageTextView = (TextView)view.findViewById(R.id.organismInfoAge);
		    String age = String.format(Locale.getDefault(), "%1$,.1f",
		    		this.info.getDynamicAttributes().getAgeTicks() / (double)(GameManager.FRAMES_PER_SECOND) / 60.0) +
		    		" minutes";
		    ageTextView.setText(age);
		    
		    TextView childrenTextView = (TextView)view.findViewById(R.id.organismInfoChildren);
		    childrenTextView.setText(String.valueOf(this.info.getDynamicAttributes().getChildCount()));
		    
		    builder.setView(view);
		    Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			
			dialog.show();
		}
	}
	
	
	private class OrganismHappinessChangedEventProcessor implements Runnable
	{
		OrganismHappinessChangedEvent event;
		
		OrganismHappinessChangedEventProcessor(OrganismHappinessChangedEvent event)
		{
			this.event = event;
		}
		
		@Override
		public void run()
		{
			Log.d(LOG_TAG, "OrganismHappinessChangedEventProcessor entered");
			
			int animationResourceId;
			final OrganismInfo data = this.event.getData();
			final Happiness happiness = data.getDynamicAttributes().getHappiness();
			
			if (happiness == Happiness.Happy)
			{
				animationResourceId = AnimationUtilities.getRandomHappyOrganismAnimationId();
			}
			else if (happiness == Happiness.Unhappy)
			{
				animationResourceId = AnimationUtilities.getRandomUnhappyOrganismAnimationId();
			}
			else
			{
				animationResourceId = AnimationUtilities.getRandomContentOrganismAnimationId();
			}
		
			GridPosition loc = data.getGridPosition();
			ImageView v = GameActivity.this.gridImageViews[loc.getRow()][loc.getColumn()]; 
			Random rand = new Random();
			
			AnimationUtilities.replaceAnimationInFuture(
					GameActivity.this.uiEventHandler, 
					v, 
					animationResourceId, 
					data.getStaticAttributes().getColor(),
					rand.nextInt(4500));
			
			Log.d(LOG_TAG, "Updated organism appearance (Event): " + happiness.toString());
		}
	}
	

	private class AllOrganismsRequestedResponseEventProcessor implements Runnable
	{
		AllOrganismsRequestedResponse event;
		
		AllOrganismsRequestedResponseEventProcessor(AllOrganismsRequestedResponse event)
		{
			this.event = event;
		}
		
		@Override
		public void run()
		{
			final int animationResourceId = AnimationUtilities.getRandomContentOrganismAnimationId();
			final OrganismInfo data = this.event.getData();
			
			GridPosition loc = data.getGridPosition();
			ImageView v = GameActivity.this.gridImageViews[loc.getRow()][loc.getColumn()]; 
			v.setBackgroundResource(animationResourceId);
			v.getBackground().mutate().setColorFilter(data.getStaticAttributes().getColor(), PorterDuff.Mode.MULTIPLY);
			
			// Vary the start times of the animations, to help ensure that they aren't all in sync, which does not look right.
			final Random rand = new Random();
			final int startDelay = rand.nextInt(2000);
			AnimationUtilities.startAnimationInFuture(GameActivity.this.uiEventHandler, v, startDelay);	
		}
	}
	
	
	
	/**
	 * Sets the OnClickListeners for all grid elements.
	 */
	private void setGridClickListeners()
	{
		TableLayout layout = (TableLayout)findViewById(R.id.gameGridLayout);
		
		for (int row = 0; row < GRID_ROWS; ++row)
		{
			TableRow tr = (TableRow)layout.getChildAt(row);
			
			for (int column = 0; column < GRID_COLUMNS; ++column)
			{
				ImageView v = (ImageView)tr.getChildAt(column);
				
				v.setOnClickListener(new GridOnClickListener(row, column));
				v.setOnLongClickListener(new GridOnLongClickListener(row, column));
				
				this.gridImageViews[row][column] = v;
			}
		}
	}

	
	/**
	 * Listener for the New Colony button.
	 * @author Christopher D Canfield
	 */
	private class NewColonyListener implements OnClickListener
	{
		@Override
		public void onClick(View v) 
		{
			GameActivity.this.startActivityForResult(new Intent(getApplication(), NewColonyActivity.class), 1);
		}
	}
	
	/**
	 * Click Listener for the game grid.
	 * @author Christopher D Canfield
	 */
	private class GridOnClickListener implements OnClickListener 
	{
		private int clickedRow;
		private int clickedColumn;
		
		GridOnClickListener(int row, int column)
		{
			this.clickedRow = row;
			this.clickedColumn = column;
		}
		
		
		@Override 
		public void onClick(View v) 
		{
			Log.d(LOG_TAG, "Grid clicked: " + this.clickedRow + ", " + this.clickedColumn);
			
			if (GameActivity.this.isPlacingColony && 
					GameActivity.this.newColonyAttributes != null) 
			{
				GridPosition location = new GridPosition(this.clickedRow, this.clickedColumn);
				NewColonyPlacedData data = new NewColonyPlacedData(GameActivity.this.newColonyAttributes, 
						location, GameActivity.this.numberOfOrganismsToPlace, GameActivity.this.app.getGameManager().getGameTicks());
				
				GameActivity.this.isPlacingColony = false;
				GameActivity.this.newColonyAttributes = null;
				
				NewColonyPlacedEvent event = new NewColonyPlacedEvent(data);
				
				GameActivity.this.app.getEventManager().notify(event);
				
				GameActivity.this.isPoppingOrganism = false;
				
				Log.d(LOG_TAG, "NewColonyPlacedEvent fired");
			}
			else if (GameActivity.this.isPoppingOrganism)
			{
				GridPosition gp = new GridPosition(this.clickedRow, this.clickedColumn);
				OrganismPoppedEvent event = new OrganismPoppedEvent(gp);
				
				GameActivity.this.app.getEventManager().notify(event);
				
				v.setBackgroundResource(R.anim.organism_pop);
				((AnimationDrawable)(v.getBackground())).start();
				
				GameActivity.this.isPoppingOrganism = false;
				
//				TextView popOrganismTextView = (TextView)findViewById(R.id.popOrganismText);
//				popOrganismTextView.setTextColor(0x004A7F);
//				
//				GameActivity.this.uiEventHandler.postDelayed(new Runnable() {
//					@Override
//					public void run()
//					{
//						TextView popOrganismTextView = (TextView)findViewById(R.id.popOrganismText);
//						popOrganismTextView.setTextColor(0x33B5E5);
//					}
//				}, 300);
				
				Log.d(LOG_TAG, "OrganismPoppedEvent fired");
			}
		}
	}
	
	/**
	 * Long Click Listener for the game grid.
	 * @author Christopher D Canfield
	 */
	private class GridOnLongClickListener implements OnLongClickListener
	{
		private int clickedRow;
		private int clickedColumn;
		
		GridOnLongClickListener(int row, int column)
		{
			this.clickedRow = row;
			this.clickedColumn = column;
		}

		@Override
		public boolean onLongClick(View v)
		{
			if (GameActivity.this.gridImageViews[this.clickedRow][this.clickedColumn] != null)
			{
				GridPosition location = new GridPosition(this.clickedRow, this.clickedColumn);
				OrganismInfoRequestedEvent event = new OrganismInfoRequestedEvent(location);
				
				GameActivity.this.app.getEventManager().notify(event);
				return true;
			}
			
			return false;
		}
	}
}
