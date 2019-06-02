package com.multilingual.rupali.accesspoints.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.activities.TargetCustomersActivity;
import com.multilingual.rupali.accesspoints.constants.BundleArg;
import com.multilingual.rupali.accesspoints.R;

import static com.multilingual.rupali.accesspoints.constants.Tag.MY_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    Button targetCustomerButton;
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
        newCustButton=view.findViewById(R.id.new_cust_button);
        newToLocButton=view.findViewById(R.id.new_to_loc_button);
        targetCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                targetcustomerOnClick();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter the limit of customers.");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText limitET = new EditText(getContext());
        limitET.setHint("Limit");
        layout.addView(limitET);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String limitString=limitET.getText().toString();
                    int limit=Integer.parseInt(limitString);
                    Bundle bundle=new Bundle();
                    bundle.putInt(BundleArg.LIMIT,limit);
                    bundle.putInt(BundleArg.TARGET_CUST_TYPE, BundleArg.NEW_TO_LOCKER);
                    Intent intent=new Intent(getContext(), TargetCustomersActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    Log.w(MY_TAG, "entered value isn't Integer");
                    Toast.makeText(getContext(),"Please enter a valid integer with no spaces",Toast.LENGTH_SHORT).show();
                }

               // int limit=Integer.parseInt(limitET.getText().toString());
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

    private void newCustomerOnclick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter the limit of customers.");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText limitET = new EditText(getContext());
        limitET.setHint("Limit");
        layout.addView(limitET);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int limit=Integer.parseInt(limitET.getText().toString());
                    Bundle bundle=new Bundle();
                    bundle.putInt(BundleArg.LIMIT,limit);
                    bundle.putInt(BundleArg.TARGET_CUST_TYPE, BundleArg.NEW_CUSTOMERS);
                    Intent intent=new Intent(getContext(), TargetCustomersActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    Log.w(MY_TAG, "entered value isn't Integer");
                    Toast.makeText(getContext(),"Please enter a valid integer with no spaces",Toast.LENGTH_SHORT).show();
                }

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
                try {
                    int threshold = Integer.parseInt(thresholdET.getText().toString());
                    int limit=Integer.parseInt(limitET.getText().toString());
                    Bundle bundle=new Bundle();
                    bundle.putInt(BundleArg.THRESHOLD,threshold);
                    bundle.putInt(BundleArg.LIMIT,limit);
                    bundle.putInt(BundleArg.TARGET_CUST_TYPE, BundleArg.TARGET_CUSTOMERS);
                    Intent intent=new Intent(getContext(), TargetCustomersActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (NumberFormatException e) {
                    Log.w(MY_TAG, "entered value isn't Integer");
                    Toast.makeText(getContext(),"Please enter a valid integer with no spaces",Toast.LENGTH_SHORT).show();
                }

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
