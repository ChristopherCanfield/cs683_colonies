package com.divergentthoughtsgames.colonies.util;

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

import com.divergentthoughtsgames.colonies.R;

import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ImageView;

/**
 * A collection of utility classes for working with animations.
 * @author Christopher D. Canfield
 */
public final class AnimationUtilities
{
	private AnimationUtilities() {}
	
	
	/**
	 * Starts an ImageView animation at the specified point in the future.
	 * @param handler The Handler for the Activity in which the animation will run.
	 * @param imageView An ImageView that has a background that can be casted to AnimationDrawable.
	 * @param millisFromNow The number of milliseconds to wait before starting this animation. 
	 */
	public static void startAnimationInFuture(final Handler handler, final ImageView imageView, long millisFromNow)
	{
		startAnimationInFuture(handler, (AnimationDrawable)imageView.getBackground(), millisFromNow);
	}
	
	/**
	 * Starts an AnimationDrawable animation at the specified point in the future.
	 * @param handler The Handler for the Activity in which the animation will run.
	 * @param drawable The AnimationDrawable that will have its animation started.
	 * @param millisFromNow The number of milliseconds to wait before starting this animation. 
	 */
	public static void startAnimationInFuture(final Handler handler, final AnimationDrawable drawable, long millisFromNow)
	{
		handler.postAtTime(new Runnable() 
		{
			@Override
			public void run()
			{
				drawable.start();
			}
		}, SystemClock.uptimeMillis() + millisFromNow);
	}
	
	/**
	 * Replaces an ImageView's background AnimationDrawable with the specified resource id in the
	 * future. Calls start() on the AnimationDrawable once it has been replaced. 
	 * @param handler The Handler for the Activity in which the animation will run.
	 * @param imageView An ImageView that has a background that can be casted to AnimationDrawable.
	 * @param animationResourceId The animation's resource id.
	 * @param color The color to apply to the AnimationDrawable. 
	 * @param millisFromNow The number of milliseconds to wait before replacing the animation. 
	 */
	public static void replaceAnimationInFuture(final Handler handler, final ImageView imageView, 
			final int animationResourceId, final int color, long millisFromNow)
	{
		handler.postAtTime(new Runnable() 
		{
			@Override
			public void run()
			{
				imageView.setBackgroundResource(animationResourceId);
				imageView.getBackground().mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
				((AnimationDrawable)(imageView.getBackground())).start();
			}
		}, SystemClock.uptimeMillis() + millisFromNow);
	}
	
	
	/**
	 * Gets a resource ID for a random Organism growth animation.
	 * @return An animation resource ID.
	 */
	public static int getRandomGrowOrganismAnimationId()
	{
		Random rand = new Random();
		int randNumber = rand.nextInt(4);
		
		if (randNumber == 0)
		{
			return R.anim.organism_grow_0;
		}
		else if (randNumber == 1)
		{
			return R.anim.organism_grow_1;
		}
		else if (randNumber == 2)
		{
			return R.anim.organism_grow_2;
		}
		else
		{
			return R.anim.organism_grow_3;
		}
	}
	
	/**
	 * Gets the resource ID for a random content happiness Organism animation.
	 * @return An animation resource ID.
	 */
	public static int getRandomContentOrganismAnimationId()
	{
		Random rand = new Random();
		int randNumber = rand.nextInt(4);
		
		if (randNumber == 0)
		{
			return R.anim.organism_content_active;
		}
		else if (randNumber == 1)
		{
			return R.anim.organism_content_calm;
		}
		else if (randNumber == 2)
		{
			return R.anim.organism_content_long_0;
		}
		else
		{
			return R.anim.organism_content_long_1;
		}
	}
	
	/**
	 * Gets the resource ID for a random happy happiness Organism animation.
	 * @return An animation resource ID.
	 */
	public static int getRandomHappyOrganismAnimationId()
	{
		Random rand = new Random();
		int randNumber = rand.nextInt(4);
		
		if (randNumber == 0)
		{
			return R.anim.organism_happy_active;
		}
		else if (randNumber == 1)
		{
			return R.anim.organism_happy_calm;
		}
		else if (randNumber == 2)
		{
			return R.anim.organism_happy_long_0;
		}
		else
		{
			return R.anim.organism_happy_long_1;
		}
	}
	
	/**
	 * Gets the resource ID for a random unhappy happiness Organism animation.
	 * @return An animation resource ID.
	 */
	public static int getRandomUnhappyOrganismAnimationId()
	{
		Random rand = new Random();
		int randNumber = rand.nextInt(4);
		
		if (randNumber == 0)
		{
			return R.anim.organism_unhappy_active;
		}
		else if (randNumber == 1)
		{
			return R.anim.organism_unhappy_long_1;
		}
		else if (randNumber == 2)
		{
			return R.anim.organism_unhappy_long_0;
		}
		else
		{
			return R.anim.organism_unhappy_calm;
		}
	}
	
	/**
	 * Gets the duration of the frames of animation in an AnimationDrawable, in milliseconds.
	 * @param drawable
	 * @return The duration of the frames in the AnimationDrawable, in milliseconds.
	 */
	public static long getAnimationDuration(AnimationDrawable drawable)
	{
		long duration = 0;
		for (int i = 0; i < drawable.getNumberOfFrames(); ++i)
		{
			duration += drawable.getDuration(i);
		}
		return duration;
	}
}
