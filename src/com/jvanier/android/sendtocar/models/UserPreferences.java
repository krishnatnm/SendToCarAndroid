package com.jvanier.android.sendtocar.models;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserPreferences {
	private boolean debug;
	private boolean tutorialShown;
	private String country;
	private boolean hideFordHint;

	public boolean isDebug() {
		return debug;
	}

	public UserPreferences setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public boolean isTutorialShown() {
		return tutorialShown;
	}

	public UserPreferences setTutorialShown(boolean tutorialShown) {
		this.tutorialShown = tutorialShown;
		return this;
	}

	public String getCountry() {
		if(country == null) {
			return Locale.getDefault().getCountry().toLowerCase(Locale.US);
		} else {
			return country;
		}
	}

	public UserPreferences setCountry(String country) {
		this.country = country;
		return this;
	}

	public boolean hideFordHint() {
		return hideFordHint;
	}

	public UserPreferences setHideFordHint(boolean hideFordHint) {
		this.hideFordHint = hideFordHint;
		return this;
	}

	private static final UserPreferences INSTANCE = new UserPreferences();

	private static final String KEY_DEBUG = "debug";
	private static final String KEY_TUTORIAL_SHOWN = "tutorialShown";
	private static final String KEY_COUNTRY = "country";
	private static final String KEY_HIDE_FORD_HINT = "hideFordHint";

	public static UserPreferences sharedInstance() {
		return INSTANCE;
	}

	// Make singleton constructor private
	private UserPreferences() {
	}

	public void load(Context context) {
		SharedPreferences settings = getPreferences(context);
		debug = settings.getBoolean(KEY_DEBUG, false);
		tutorialShown = settings.getBoolean(KEY_TUTORIAL_SHOWN, false);
		country = settings.getString(KEY_COUNTRY, null);
		hideFordHint = settings.getBoolean(KEY_HIDE_FORD_HINT, false);
	}

	public void save(Context context) {
		SharedPreferences.Editor settingsEditor = getPreferences(context).edit();
		settingsEditor.putBoolean(KEY_DEBUG, debug);
		settingsEditor.putBoolean(KEY_TUTORIAL_SHOWN, tutorialShown);
		if(country != null) {
			settingsEditor.putString(KEY_COUNTRY, country);
		}
		settingsEditor.putBoolean(KEY_HIDE_FORD_HINT, hideFordHint);
		settingsEditor.commit();
	}

	private SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

}
