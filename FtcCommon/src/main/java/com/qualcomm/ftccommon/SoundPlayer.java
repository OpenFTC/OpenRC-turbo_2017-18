/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.qualcomm.ftccommon;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.RawRes;

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * {@link SoundPlayer} is a simple utility class that plays sounds on the phone. The class
 * is typically used through its singleton instance.
 *
 * @see SoundPlayer#play(Context, int)
 */
@SuppressWarnings("javadoc")
public class SoundPlayer implements SoundPool.OnLoadCompleteListener
    {
    //----------------------------------------------------------------------------------------------
    // State
    //----------------------------------------------------------------------------------------------

    public static final String TAG = "SoundPlayer";
    public static final boolean DEBUG = false;

    protected static SoundPlayer theInstance = new SoundPlayer(3, 6);
    public static SoundPlayer getInstance()
        {
        return theInstance;
        }

    protected SoundPool            soundPool;
    protected @RawRes volatile int currentlyLoading;
    protected long                 msFinishPlaying;
    protected LoadedSoundCache     loadedSounds;
    protected ExecutorService      executorService;
    protected Looper               looper;
    protected Context              context;
    protected SharedPreferences    sharedPreferences;
    protected float                soundOnLevel = 1.0f;
    protected float                soundOffLevel = 0.0f;

    //----------------------------------------------------------------------------------------------
    // Construction
    //----------------------------------------------------------------------------------------------

    /**
     * Instantiates a new sound player.
     *
     * @param simultaneousStreams the number of sounds that can simultaneously play from this player.
     *                            If one, then playing any new sound interrupts the playing of a
     *                            a previous sound
     * @param cacheSize           the maximum size of the cache of loaded sounds.
     */
    public SoundPlayer(int simultaneousStreams, int cacheSize)
        {
        soundPool = new SoundPool(simultaneousStreams, AudioManager.STREAM_MUSIC, /*quality*/0); // can't use SoundPool.Builder on KitKat
        loadedSounds = new LoadedSoundCache(cacheSize);
        currentlyLoading = 0;
        msFinishPlaying = 0;
        executorService = null;
        looper = null;
        context = AppUtil.getInstance().getApplication();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //
        startup();
        }

    public void close()
        {
        shutdown();
        }

    protected void startup()
        {
        if (this.executorService == null)
            {
            this.executorService = ThreadPool.newFixedThreadPool(2, "SoundPlayer");

            // Use one of those threads to run a Looper() that will ONLY see
            // load completion callbacks from the SoundPool.
            this.executorService.execute(new Runnable()
                {
                @Override public void run()
                    {
                    Thread.currentThread().setName("SoundPlayer looper");
                    Looper.prepare();
                    looper = Looper.myLooper();
                    soundPool.setOnLoadCompleteListener(SoundPlayer.this);
                    Looper.loop();  // doesn't return until we tell it to quit()
                    }
                });

            // Label the other thread
            this.executorService.execute(new Runnable()
                {
                @Override public void run()
                    {
                    Thread.currentThread().setName("SoundPlayer");
                    }
                });

            // Wait for us to learn the looper
            while (looper==null)
                {
                Thread.yield();
                }
            }
        }

    protected void shutdown()
        {
        if (this.executorService != null)
            {
            if (looper != null) looper.quit();
            this.executorService.shutdownNow();
            ThreadPool.awaitTerminationOrExitApplication(this.executorService, 5, TimeUnit.SECONDS, "SoundPool", "internal error");
            this.executorService = null;
            this.looper = null;
            }
        }

    //----------------------------------------------------------------------------------------------
    // Operations
    //----------------------------------------------------------------------------------------------

    /**
     * Asynchronously loads the indicated sound from its resource (if not already loaded), then
     * initiates its play once any current sound is finished playing.
     *
     * @param context   the context in which resId is to be interpreted
     * @param resId     the resource id of the raw resource containing the sound.
     */
    synchronized public void play(final Context context, @RawRes final int resId)
        {
        play(context, resId, true);
        }

    /**
     * Asynchronously loads the indicated sound from its resource (if not already loaded), then
     * initiates its play, optionally waiting for any currently playing sound to finish first.
     *
     * @param context           the context in which resId is to be interpreted
     * @param resId             the resource id of the raw resource containing the sound.
     * @param waitForCompletion whether to wait for any current sound to finish playing first or not
     */
    synchronized public void play(final Context context, @RawRes final int resId, final boolean waitForCompletion)
        {
        // Ignore impossible ids
        if (resId==0) return;

        this.executorService.execute(new Runnable()
            {
            @Override public void run()
                {
                try {
                    loadAndPlay(context, resId, waitForCompletion);
                    }
                catch (Exception e)
                    {
                    RobotLog.ee(TAG, e, "exception playing sound; ignored");
                    }
                }
            });
        }

    /**
     * Loads the requested sound if necessary, then (eventually) plays it.
     * Note: this always runs on our dedicate executor thread. We do that because
     * that allows us to have a looper that *only* sees load completions, which will
     * prevent us from accepting new play requests while we're waiting for loads to
     * complete.
     */
    protected void loadAndPlay(Context context, @RawRes int resourceId, boolean waitForCompletion)
        {
        SoundInfo soundInfo = loadedSounds.get(resourceId);
        if (soundInfo == null)
            {
            // Figure out how long the sound is. We do this before we load so
            // as to avoid potential conflicts with the method of determining duration
            int msDuration = getMsDuration(context, resourceId);

            // Ask to load the sound
            currentlyLoading = resourceId;
            try {
                int sampleId = soundPool.load(context, resourceId, 1);
                if (sampleId != 0)
                    {
                    // Remember the sound for next time
                    soundInfo = new SoundInfo(resourceId, sampleId, msDuration);
                    loadedSounds.put(resourceId, soundInfo);

                    if (DEBUG) RobotLog.vv(TAG, "loadAndPlay(res=0x%08x samp=%d)...", resourceId, sampleId);
                    waitForLoadCompletion();
                    if (DEBUG) RobotLog.vv(TAG, "...loaded");

                    // Play the sound
                    playLoadedSound(soundInfo, waitForCompletion);
                    }
                else
                    {
                    RobotLog.ee(TAG, "unable to load sound resource 0x%08x", resourceId);
                    }
                }
            finally
                {
                currentlyLoading = 0;
                }
            }
        else
            {
            // Update the MRU notion of which sounds have been recently used
            loadedSounds.noteSoundUsage(soundInfo);

            // Play it for me, Sam.
            playLoadedSound(soundInfo, waitForCompletion);
            }
        }

    protected int getMsDuration(Context context, @RawRes int resourceId)
        {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceId);
        int msDuration = mediaPlayer.getDuration();
        mediaPlayer.release();
        if (DEBUG) RobotLog.vv(TAG, "duration(res=0x%08x)=%d", resourceId, msDuration);
        return msDuration;
        }

    protected void waitForLoadCompletion()
        {
        // Wait for the load to finish
        while (currentlyLoading != 0)
            {
            Thread.yield();
            }
        }

    protected void playLoadedSound(SoundInfo soundInfo, boolean waitForCompletion)
        {
        if (soundInfo != null)
            {
            if (DEBUG) RobotLog.vv(TAG, "playLoadedSound(%d)", soundInfo.sampleId);

            boolean soundOn = sharedPreferences.getBoolean(context.getString(R.string.pref_sound_on_off), true);
            float volume = soundOn ? soundOnLevel : soundOffLevel;

            if (waitForCompletion)
                {
                // Wait for the current sound to finish playing.
                long msNow = getCurrentMilliseconds();
                long msDelay = Math.max(0, msFinishPlaying-msNow);
                try { Thread.sleep(msDelay); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                }

            // We try to successfully play for a little while before we eventually give up
            long msTry = 1000;
            long msDeadline = getCurrentMilliseconds() + msTry;
            while (true)
                {
                long msNow = getCurrentMilliseconds();
                if (msNow >= msDeadline)
                    {
                    break;
                    }

                long msStart = msNow;
                int streamId = soundPool.play(soundInfo.sampleId, /*leftVol*/volume, /*rightVol*/volume, /*priority*/1, /*loop*/0, /*rate*/1.0f);
                if (streamId != 0)
                    {
                    // Started playing. Yay!
                    soundInfo.msLastPlay = msStart;
                    msFinishPlaying = Math.max(msFinishPlaying, msStart + soundInfo.msDuration);
                    return;
                    }

               /* if (soundInfo.msLastPlay==0)
                    {
                    // Give it a moment for the onLoadComplete call to return and report internally as READY
                    if (DEBUG) RobotLog.vv(TAG, "waiting for successful play");
                    Thread.yield();
                    }
                else*/
                    break;  // don't repeat earlier problems
                }

            RobotLog.vv(TAG, "Abandoning play attempt res=0x%08x samp=%d", soundInfo.resourceId, soundInfo.sampleId);
            }
        }

    protected long getCurrentMilliseconds()
        {
        return System.nanoTime() / ElapsedTime.MILLIS_IN_NANO;
        }

    /**
     * Called when a sound has completed loading.
     *
     * @param soundPool SoundPool object from the load() method
     * @param sampleId the sample ID of the sound loaded.
     * @param status the status of the load operation (0 = success)
     */
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
        {
        if (DEBUG) RobotLog.vv(TAG, "onLoadComplete(res=0x%08x samp=%d)=%d", currentlyLoading, sampleId, status);
        currentlyLoading = 0;
        }

    //----------------------------------------------------------------------------------------------
    // Types
    //----------------------------------------------------------------------------------------------

    protected class SoundInfo
        {
        public @RawRes int      resourceId;
        public         int      sampleId;
        public         long     msDuration;
        public         long     msLastPlay;

        public SoundInfo(@RawRes int resourceId, int sampleId, int msDuration)
            {
            this.resourceId = resourceId;
            this.sampleId = sampleId;
            this.msDuration = msDuration;
            this.msLastPlay = 0;
            }
        }

    /**
     * {@link LoadedSoundCache} keeps track of loaded sounds, mapping sound resource id to loaded
     * sound id. It keeps track of which sounds have been recently used, and unloads neglected
     * songs when a configured capacity of loaded sounds has been reached.
     */
    protected class LoadedSoundCache extends LinkedHashMap<Integer, SoundInfo>
        {
        static final float loadFactor = 0.75f;

        final int capacity;         // max number of cached sounds
        boolean unloadOnRemove;     // whether we should unload a sound when it's removed

        LoadedSoundCache(int capacity)
            {
            super((int)Math.ceil(capacity / loadFactor) + 1, loadFactor, true);
            this.capacity = capacity;
            this.unloadOnRemove = true;
            }

        /** update the fact that this key has been just used, again */
        public void noteSoundUsage(SoundInfo soundInfo)
            {
            // We're updating the MRU, we don't want to unload the sound during the remove() below.
            unloadOnRemove = false;
            try {
                // Make this key most recently used
                this.remove(soundInfo.resourceId);
                put(soundInfo.resourceId, soundInfo);
                }
            finally
                {
                unloadOnRemove = true;
                }
            }

        @Override
        protected boolean removeEldestEntry(Entry<Integer, SoundInfo> eldest)
            {
            return size() > capacity;
            }

        @Override
        public SoundInfo remove(Object key)
            {
            SoundInfo soundInfo = super.remove(key);
            if (unloadOnRemove)
                {
                if (soundInfo != null)
                    {
                    if (DEBUG) RobotLog.vv(TAG, "unloading sound 0x%08x", (Integer)key);
                    soundPool.unload(soundInfo.sampleId);
                    }
                }
            return soundInfo;
            }
        }

    }
