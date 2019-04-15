package com.multilingual.rupali.accesspoints.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.multilingual.rupali.accesspoints.Activities.TargetCustomersActivity;
import com.multilingual.rupali.accesspoints.Constants.BundleArg;
import com.multilingual.rupali.accesspoints.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    
    Button targetCustomerButton;
    Button sendMailButton;
    Button newCustButton;
    Button newToLocButton;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        targetCustomerButton=view.findViewById(R.id.target_customer);
        sendMailButton=view.findViewById(R.id.send_mail);
        newCustButton=view.findViewById(R.id.new_cust_button);
        newToLocButton=view.findViewById(R.id.new_to_loc_button);
        targetCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetcustomerOnClick();
            }
        });
        sendMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMailOnClick();
            }
        });
        newCustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCustomerOnclick();
            }
        });
        newToLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newToLocOnClick();
            }
        });
        return view;
        
    }

    private void newToLocOnClick() {
    }

    private void newCustomerOnclick() {
    }

    private void sendMailOnClick() {
    }

    private void targetcustomerOnClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter the del failures threshold and limit of customers.");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText thresholdET = new EditText(getContext());
        thresholdET.setHint("Threshold");
        layout.addView(thresholdET);

        final EditText limitET = new EditText(getContext());
        limitET.setHint("Limit");
        layout.addView(limitET);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int threshold = Integer.parseInt(thresholdET.getText().toString());
                int limit=Integer.parseInt(limitET.getText().toString());
                Bundle bundle=new Bundle();
                bundle.putInt(BundleArg.THRESHOLD,threshold);
                bundle.putInt(BundleArg.LIMIT,limit);
                Intent intent=new Intent(getContext(), TargetCustomersActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

}
