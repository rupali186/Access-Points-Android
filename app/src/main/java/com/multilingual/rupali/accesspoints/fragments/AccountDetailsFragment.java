package com.multilingual.rupali.accesspoints.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.R;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountDetailsFragment extends Fragment {
    TextView emailTV;
    TextView unameTV;
    TextView dobTV;
    TextView contactTV;
    TextView genderTV;
    TextView countryTV;
    TextView stateTV;
    TextView cityTV;
    TextView landTV;
    TextView streetTV;
    TextView hnoTV;
    TextView pinTV;
    String email,uname,dob,contactNo,gender,country,state,city,landmark,street,hno,pin;
    SharedPreferences sharedPreferences;

    public AccountDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account_details, container, false);
        emailTV=view.findViewById(R.id.user_email);
        unameTV=view.findViewById(R.id.user_name);
        dobTV=view.findViewById(R.id.user_dob);
        contactTV=view.findViewById(R.id.user_phone);
        genderTV=view.findViewById(R.id.user_gender);
        countryTV=view.findViewById(R.id.user_country);
        stateTV=view.findViewById(R.id.user_state);
        cityTV=view.findViewById(R.id.user_city);
        landTV=view.findViewById(R.id.user_landmark);
        streetTV=view.findViewById(R.id.user_street);
        hnoTV=view.findViewById(R.id.user_hno);
        pinTV=view.findViewById(R.id.user_pin_code);
        sharedPreferences=getActivity().getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        populateDataFromSharedPreferences();
        return  view;
    }

    private void populateDataFromSharedPreferences() {
        email=sharedPreferences.getString(LoginSharedPref.USER_EMAIL,"");
        uname=sharedPreferences.getString(LoginSharedPref.USER_NAME,"");
        dob=sharedPreferences.getString(LoginSharedPref.USER_DOB,"");
        contactNo=sharedPreferences.getString(LoginSharedPref.USER_CONTACT_NO,"");
        gender=sharedPreferences.getString(LoginSharedPref.USER_GENDER,"");
        country=sharedPreferences.getString(LoginSharedPref.USER_COUNTRY,"");
        state=sharedPreferences.getString(LoginSharedPref.USER_STATE,"");;
        city=sharedPreferences.getString(LoginSharedPref.USER_CITY,"");;
        landmark=sharedPreferences.getString(LoginSharedPref.USER_LANDMARK,"");;
        street=sharedPreferences.getString(LoginSharedPref.USER_STREET,"");;
        hno=sharedPreferences.getString(LoginSharedPref.USER_HNO,"");
        pin=sharedPreferences.getInt(LoginSharedPref.USER_PICODE,0)+"";
        emailTV.setText(email);
        unameTV.setText(uname);
        dobTV.setText(dob);
        contactTV.setText(contactNo);
        genderTV.setText(gender);
        countryTV.setText("Country: "+country);
        stateTV.setText("State: "+state);
        cityTV.setText("City: "+city);
        landTV.setText("Landmark: "+landmark);
        streetTV.setText("Street: "+street);
        hnoTV.setText("H-no./ Flat No. : "+hno);
        pinTV.setText("Pincode: "+pin);
    }

}
