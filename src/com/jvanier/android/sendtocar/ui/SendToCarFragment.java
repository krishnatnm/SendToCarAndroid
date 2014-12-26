package com.jvanier.android.sendtocar.ui;

import java.text.MessageFormat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jvanier.android.sendtocar.R;

public class SendToCarFragment extends Fragment {

    private TextView helpButton;
	private TextView issueButton;
	private View issueGroup;

	private String helpButtonTemplateText;
	private String issueButtonTemplateText;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sendtocar_fragment, container, false);

        setupMakeSpinner(rootView);
        setupVehicleHelp(rootView);
        
        Intent intent = new Intent(getActivity(), MakeActivity.class);
        startActivity(intent);
        
        return rootView;
    }
	
	private void setupMakeSpinner(View rootView)
	{
        Spinner makeSpinner = (Spinner) rootView.findViewById(R.id.makeSpinner);

		/*
		ArrayList<CarProvider> carsList = new ArrayList<CarProvider>();
		CarProvider fake = new CarProvider();
		fake.make = getString(R.string.choose_make);
		fake.type = 0;
		carsList.add(fake);
		

		ArrayAdapter<CarProvider> adapter = new ArrayAdapter<CarProvider>(this,
				android.R.layout.simple_spinner_item, carsList.toArray(new CarProvider[carsList.size()]));
				*/

		String[] carsList = new String[3];
		carsList[0] = getString(R.string.choose_make);
		carsList[1] = "Ford";
		carsList[2] = "Toyota";

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.make_spinner_item, carsList);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		makeSpinner.setAdapter(adapter);
	}
	
	private void setupVehicleHelp(View rootView) {
		helpButton = (TextView) rootView.findViewById(R.id.vehicleHelp);
		helpButtonTemplateText = helpButton.getText().toString();

		issueButton = (TextView) rootView.findViewById(R.id.vehicleIssue);
		issueButtonTemplateText = issueButton.getText().toString();
		issueGroup = rootView.findViewById(R.id.vehicleIssueGroup);
		
		updateVehicleHelpButtons(getResources().getText(R.string.vehicle_lowercase).toString());
		showVehicleIssueButton(true);
	}
	
	private void updateVehicleHelpButtons(String make) {
		helpButton.setText(MessageFormat.format(helpButtonTemplateText, make));
		issueButton.setText(MessageFormat.format(issueButtonTemplateText, make));
	}
	
	private void showVehicleIssueButton(boolean show) {
		issueGroup.setVisibility(show ? View.VISIBLE : View.GONE);
	}
}
