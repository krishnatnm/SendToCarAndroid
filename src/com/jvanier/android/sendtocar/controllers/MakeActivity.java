package com.jvanier.android.sendtocar.controllers;

import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jvanier.android.sendtocar.R;
import com.jvanier.android.sendtocar.downloaders.CarListManager;
import com.jvanier.android.sendtocar.models.CarList;
import com.jvanier.android.sendtocar.models.CarProvider;
import com.jvanier.android.sendtocar.models.RecentVehicle;
import com.jvanier.android.sendtocar.models.RecentVehicleList;

public class MakeActivity extends ActionBarActivity {
	public static final int PICK_MAKE = 0;

	public static final String EXTRA_TYPE = "type";
	public static final String TYPE_RECENT_VEHICLE = "recentVehicle";
	public static final String TYPE_MAKE = "make";

	public static final String EXTRA_PROVIDER = "provider";
	public static final String EXTRA_RECENT_VEHICLE = "recentVehicle";

	private CardView recentVehiclesCard;
	private TextView recentVehiclesLabel;
	private ViewGroup recentVehiclesCardContainer;
	private ViewGroup makesCardContainer;

	private RecentVehicleList recentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_activity);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		setupRecentVehicles();
		setupMakes();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class RecentVehicleClickListener implements OnClickListener {
		private RecentVehicle vehicle;

		public RecentVehicleClickListener(RecentVehicle vehicle) {
			this.vehicle = vehicle;
		}

		@Override
		public void onClick(View v) {
			MakeActivity.this.selectRecentVehicle(vehicle);
		}
	}

	private class RecentVehicleDiscardClickListener implements OnClickListener {
		private RecentVehicle vehicle;

		public RecentVehicleDiscardClickListener(RecentVehicle vehicle) {
			this.vehicle = vehicle;
		}

		@Override
		public void onClick(View v) {
			MakeActivity.this.discardRecentVehicle(vehicle);
		}
	}

	private void setupRecentVehicles() {
		recentVehiclesLabel = (TextView) findViewById(R.id.recentVehiclesLabel);
		recentVehiclesCard = (CardView) findViewById(R.id.recentVehiclesCard);
		recentVehiclesCardContainer = (ViewGroup) findViewById(R.id.recentVehiclesCardContainer);

		recentList = RecentVehicleList.sharedInstance();

		boolean first = true;
		for (int i = 0; i < recentList.size(); i++) {
			if (!first) {
				getLayoutInflater().inflate(R.layout.card_divider, recentVehiclesCardContainer, true);
			}
			first = false;

			RecentVehicle vehicle = recentList.getRecentVehicle(i);

			View recentVehicleItem = getLayoutInflater().inflate(R.layout.recent_vehicle_item, recentVehiclesCardContainer, false);
			TextView makeView = (TextView) recentVehicleItem.findViewById(android.R.id.text1);
			TextView accountView = (TextView) recentVehicleItem.findViewById(android.R.id.text2);

			makeView.setText(vehicle.make);
			accountView.setText(vehicle.account);

			recentVehicleItem.findViewById(R.id.recentVehicleButton).setOnClickListener(new RecentVehicleClickListener(vehicle));

			recentVehicleItem.findViewById(R.id.discardButton).setOnClickListener(new RecentVehicleDiscardClickListener(vehicle));

			recentVehiclesCardContainer.addView(recentVehicleItem);
		}

		// Hide section if there are no recent vehicles
		if (recentList.size() == 0) {
			hideRecentVehicles();
		}
	}

	private void hideRecentVehicles() {
		recentVehiclesLabel.setVisibility(View.GONE);
		recentVehiclesCard.setVisibility(View.GONE);
	}

	private class MakeClickListener implements OnClickListener {
		private CarProvider provider;

		public MakeClickListener(CarProvider provider) {
			this.provider = provider;
		}

		@Override
		public void onClick(View v) {
			MakeActivity.this.selectMake(provider);
		}
	}

	private void setupMakes() {
		makesCardContainer = (ViewGroup) findViewById(R.id.makesCardContainer);

		String country = Locale.getDefault().getCountry().toLowerCase(Locale.US);

		CarList carList = CarListManager.sharedInstance().getCarList();
		List<CarProvider> carListDisplayed = carList.asList();

		boolean first = true;
		for (int i = 0; i < carListDisplayed.size(); i++) {
			CarProvider provider = carListDisplayed.get(i);

			if (provider.isCountrySupported(country)) {
				if (!first) {
					getLayoutInflater().inflate(R.layout.card_divider, makesCardContainer, true);
				}
				first = false;

				View makeItem = getLayoutInflater().inflate(R.layout.make_item, makesCardContainer, false);
				TextView makeView = (TextView) makeItem.findViewById(android.R.id.text1);
				makeView.setText(provider.make);

				makeItem.setOnClickListener(new MakeClickListener(provider));

				makesCardContainer.addView(makeItem);
			}
		}
	}

	public void selectRecentVehicle(RecentVehicle vehicle) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_TYPE, TYPE_RECENT_VEHICLE);
		intent.putExtra(EXTRA_RECENT_VEHICLE, vehicle);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void selectMake(CarProvider provider) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_TYPE, TYPE_MAKE);
		intent.putExtra(EXTRA_PROVIDER, provider);
		setResult(RESULT_OK, intent);
		finish();
	}

	public void discardRecentVehicle(RecentVehicle vehicle) {
		int oldPosition = recentList.removeRecentVehicle(vehicle);
		if (oldPosition >= 0) {
			recentList.saveToCache(this);

			if (recentList.size() == 0) {
				hideRecentVehicles();
			} else {
				// Remove the cell and the divider line below
				View v1 = recentVehiclesCardContainer.getChildAt(2 * oldPosition);
				View v2 = recentVehiclesCardContainer.getChildAt(2 * oldPosition + 1);
				if (v1 != null) {
					recentVehiclesCardContainer.removeView(v1);
				}
				if (v2 != null) {
					recentVehiclesCardContainer.removeView(v2);
				}
			}
		}
	}
}
