/*
 * Android SDK for Piwik
 *
 * @link https://github.com/piwik/piwik-android-sdk
 * @license https://github.com/piwik/piwik-sdk-android/blob/master/LICENSE BSD-3 Clause
 */

package org.piwik.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.net.MalformedURLException;


public class Piwik {
    public static final String LOGGER_PREFIX = "PIWIK:";
    public static final String PREFERENCE_FILE_NAME = "org.piwik.sdk";
    public static final String PREFERENCE_KEY_OPTOUT = "piwik.optout";
    private final Context mContext;
    private boolean mOptOut = false;

    private static Piwik sInstance;
    private final SharedPreferences mSharedPreferences;

    public static synchronized Piwik getInstance(Context context) {
        if (sInstance == null)
            sInstance = new Piwik(context);
        return sInstance;
    }

    private Piwik(Context context) {
        mContext = context.getApplicationContext();
        mSharedPreferences = getContext().getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        mOptOut = getSharedPreferences().getBoolean(PREFERENCE_KEY_OPTOUT, false);
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * @param trackerUrl (required) Tracking HTTP API endpoint, for example, http://your-piwik-domain.tld/piwik.php
     * @param siteId     (required) id of site
     * @return Tracker object
     * @throws RuntimeException if the supplied Piwik-Tracker URL is incompatible
     */
    public synchronized Tracker newTracker(@NonNull String trackerUrl, int siteId) throws MalformedURLException {
        return new Tracker(trackerUrl, siteId, this);
    }

    /**
     * Use this to disable Piwik, e.g. if the user opted out of tracking.
     * Piwik will persist the choice and remain disable on next instance creation.<p>
     * The choice is stored in {@link #PREFERENCE_FILE_NAME} under the key {@link #PREFERENCE_KEY_OPTOUT}.
     *
     * @param optOut true to disable reporting
     */
    public void setOptOut(boolean optOut) {
        mOptOut = optOut;
        getSharedPreferences().edit().putBoolean(PREFERENCE_KEY_OPTOUT, optOut).apply();
    }

    /**
     * @return true if Piwik is currently disabled
     */
    public boolean isOptOut() {
        return mOptOut;
    }

    public String getApplicationDomain() {
        return getContext().getPackageName();
    }

    /**
     * Returns the shared preferences used by Piwik that are stored under {@link #PREFERENCE_FILE_NAME}
     *
     * @return Piwik's SharedPreferences instance
     */
    public SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

}
