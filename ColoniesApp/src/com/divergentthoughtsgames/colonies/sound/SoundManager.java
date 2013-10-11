package com.divergentthoughtsgames.colonies.sound;

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

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;

import com.divergentthoughtsgames.colonies.GameManager;
import com.divergentthoughtsgames.colonies.event.EventListener;
import com.divergentthoughtsgames.colonies.event.EventManager;
import com.divergentthoughtsgames.colonies.event.GameEvent;
import com.divergentthoughtsgames.colonies.event.GamePausedEvent;
import com.divergentthoughtsgames.colonies.event.GameUnpausedEvent;


/**
 * Plays sounds and music based on events received from the
 * EventManager. Not yet implemented as of 2013-05-02.
 * @author Christopher D Canfield
 */
public class SoundManager implements EventListener
{
	private transient MediaPlayer mediaPlayer;
	
	private boolean isPaused = false;
	
	private Context context;
	
	private final GameManager gameManager;
	
//	private static final String[] titleMusic = { R.raw.title_song_0, R.raw.title_song_1 };
//	private static final String[] gameMusic = { android.R.raw.game_song_0, android.R.raw.game_song_1, R.raw.game_song_2 };
	
//	private int lastTitleSong = -1;
	private int lastGameSong = -1;
	
	public SoundManager(GameManager gameManager)
	{
//		this.mediaPlayer = new MediaPlayer();
		
		this.gameManager = gameManager;
		
		EventManager eventManager = this.gameManager.getEventManager();
		eventManager.subscribe(GamePausedEvent.ID, this);
		eventManager.subscribe(GameUnpausedEvent.ID, this);
	}

	@Override
	public void notify(GameEvent<?> event)
	{
		// TODO (2013-03-29): implement the sound manager.
//		if (eventType == StartupEvent.class)
//		{
//			this.context = ((StartupEvent)event).getData();
//			
//			this.mediaPlayer = new MediaPlayer();
//			this.mediaPlayer.setOnPreparedListener(new StartupOnPreparedListener());
////			this.mediaPlayer.setDataSource(this.context, getUri(R.raw.song_0));
//			this.mediaPlayer.prepareAsync();
//		}
//		else if (eventType == ShutdownEvent.class)
//		{
//			if (this.mediaPlayer != null)
//			{
//				this.mediaPlayer.release();
//				this.mediaPlayer = null;
//			}
//		}
		if (event instanceof GamePausedEvent)
		{
//			if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying())
//			{
//				// TODO (2013-04-01): should the MediaPlayer be released on pause?
//				this.mediaPlayer.stop();
//			}
//			this.isPaused = true;
		}
		else if (event instanceof GameUnpausedEvent)
		{
//			if (this.mediaPlayer != null && !this.mediaPlayer.isPlaying())
//			{
//				this.mediaPlayer.start();
//			}
//			this.isPaused = false;
		}
		else
		{
			throw new IllegalArgumentException(
					"Invalid Event type provided to SoundManager.notify(): " + event.toString() + ".");
		}
	}
	
//	private Uri getUri(int resourceId)
//	{
//		return Uri.parse("android.resource://" + this.context.getPackageName() + "/" + resourceId);
//	}
//	
	
	private class StartupOnPreparedListener implements OnPreparedListener
	{
		@Override
		public void onPrepared(MediaPlayer mp)
		{
			mp.start();
			++SoundManager.this.lastGameSong;
		}
	}
	
	private class NextSongListener implements OnCompletionListener
	{
		@Override
		public void onCompletion(MediaPlayer mp)
		{
//			if ((SoundManager.this.lastGameSong + 1) == SoundManager.gameMusic.length)
//			{
//				SoundManager.this.lastGameSong = 0;
//			}
//			mp.setDataSource(SoundManager.this.context, getUri(SoundManager.this.lastGameSong));
			++SoundManager.this.lastGameSong;
		}	
	}
}
