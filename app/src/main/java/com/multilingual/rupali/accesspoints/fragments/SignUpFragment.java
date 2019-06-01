package com.multilingual.rupali.accesspoints.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.User;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    EditText emailET;
    EditText passET;
    EditText confirmPassET;
    EditText unameET;
    TextView dobtView;
    ImageView dobIView;
    EditText contactET;
    RadioGroup genderRgrp;
    EditText countryET;
    EditText stateET;
    EditText cityET;
    EditText landET;
    EditText streetET;
    EditText hnoET;
    EditText pinET;
    EditText phoneCodeET;
    Button submitButton;
    private int mYear, mMonth, mDay;
    Address address;
    String email,password,confirmPass,uname,dob,contactNo,gender,country,state,city,landmark,street,hno,pin,userPhoneCode,userId;
    Retrofit retrofit;
    SharedPreferences sharedPreferences;
    ProgressListener mCallback;
    public SignUpFragment() {
        // Required empty public constructor
    }

    public interface ProgressListener{
        void showProgress();
        void hideProgress();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback=(ProgressListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "must implement ProgressListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        emailET=view.findViewById(R.id.user_email);
        passET=view.findViewById(R.id.user_password);
        confirmPassET=view.findViewById(R.id.user_re_type_pass);
        unameET=view.findViewById(R.id.user_name);
        dobtView=view.findViewById(R.id.user_dob);
        dobIView=view.findViewById(R.id.dob_dropdown);
        contactET=view.findViewById(R.id.user_phone);
        genderRgrp=view.findViewById(R.id.user_gender_radiogroup);
        countryET=view.findViewById(R.id.user_country);
        stateET=view.findViewById(R.id.user_state);
        cityET=view.findViewById(R.id.user_city);
        landET=view.findViewById(R.id.user_landmark);
        streetET=view.findViewById(R.id.user_street);
        hnoET=view.findViewById(R.id.user_hno);
        pinET=view.findViewById(R.id.user_pin_code);
        submitButton=view.findViewById(R.id.submit_user);
        phoneCodeET=view.findViewById(R.id.user_phone_code);
        retrofit= APIClient.getClient();
        sharedPreferences=getActivity().getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        dobIView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dobOnClick();
            }
        });
        return view;
    }

    private void dobOnClick() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dob=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        dobtView.setText(dob);

                    }
                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void submitForm() {
        if(!fetchData()){
            Toast.makeText(getContext(),"All the fields should be filled ",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.contentEquals(confirmPass)){
            Toast.makeText(getContext(),"Password and confirm Password do not match!!!",Toast.LENGTH_SHORT).show();
            return;
        }
        showConfirmSubmissionDialog();
//        Toast.makeText(getContext(),"SignUpButton clicked",Toast.LENGTH_SHORT).show();
//        postDemoUser();
    }

    private void postUser() {
        mCallback.showProgress();
        ArrayList<String> contactNos=new ArrayList<>();
        ArrayList<Address> addresses=new ArrayList<>();
        contactNos.add(contactNo);
        addresses.add(address);
        User user=new User(email,password,uname,contactNos,dob,gender,addresses);
        UserApi userApi=retrofit.create(UserApi.class);
        Call<User> userResponse=userApi.createNewUser(user);
        userResponse.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user=response.body();
                    saveUserInSharedPref( user);
                    Toast.makeText(getContext(),"User Created Successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"User creted Success: Body: "+response.body()+"");
                    getActivity().onBackPressed();
                }else{
                    mCallback.hideProgress();
                    Toast.makeText(getContext(),"Please check your network connection or the User Already Exists", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "sign up post submitted to API failed."+response.code()+" Headers:"+
                            response.headers()+"Message: "+response.message()+"Call :"+call.toString()+response.toString());
                    Log.d(Tag.MY_TAG, "post submitted to API failed.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mCallback.hideProgress();
                Toast.makeText(getContext(),"Please check your network connection or the User Already Exists", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "post submitted to API failed." +t.getMessage());
            }

        });
    }
//    private void postDemoUser() {
////        mCallback.showProgress();
//        Address address;
//        String password="abcdefg",confirmPass="abcdefg",uname="demouser",dob= dob=01 + "-" + (10 + 1) + "-" +2014,
//                contactNo="8750678199",gender="female",country="india",state="haryana",city="xvghd",landmark="shdjjdd",
//                street="sector-11",hno="123",pin="121001",userPhoneCode="+91";
//        address=new Address(hno,street,state,city,country,uname,landmark,Integer.parseInt(pin),contactNo);
//        contactNo=userPhoneCode+" "+contactNo;
//        ArrayList<String> contactNos=new ArrayList<>();
//        ArrayList<Address> addresses=new ArrayList<>();
//        contactNos.add(contactNo);
//        addresses.add(address);
//        for(int i=0;i<10000;i++) {
//            User user = new User("abcd"+i+"@gmail.com", password, uname, contactNos, dob, gender, addresses);
//            UserApi userApi = retrofit.create(UserApi.class);
//            Call<User> userResponse = userApi.createNewUser(user);
//            final int finalI = i;
//            userResponse.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    if (response.isSuccessful()) {
////                        User user = response.body();
////                        saveUserInSharedPref(user);
////                        Toast.makeText(getContext(), "User Created Successfully.", Toast.LENGTH_SHORT).show();
//                        Log.d(Tag.MY_TAG, "User"+ finalI +"creted Success: Body: " + response.body() + "");
////                        getActivity().onBackPressed();
//                    } else {
////                        mCallback.hideProgress();
//                        Toast.makeText(getContext(), "Please check your network connection or the User Already Exists", Toast.LENGTH_SHORT).show();
//                        Log.d(Tag.MY_TAG, "sign up post submitted to API failed." + response.code() + " Headers:" +
//                                response.headers() + "Message: " + response.message() + "Call :" + call.toString() + response.toString());
//                        Log.d(Tag.MY_TAG, "post submitted to API failed.");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
////                    mCallback.hideProgress();
//                    Toast.makeText(getContext(), "Please check your network connection or the User Already Exists", Toast.LENGTH_SHORT).show();
//                    Log.d(Tag.MY_TAG, "post submitted to API failed." + t.getMessage());
//                }
//
//            });
////            mCallback.hideProgress();
//        }
//    }
    private void saveUserInSharedPref(User user) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Address uAd=user.getAddress().get(0);
        editor.putString(LoginSharedPref.USER_EMAIL,user.getEmail());
        editor.putString(LoginSharedPref.USER_ID,user.get_id());
        editor.putString(LoginSharedPref.USER_NAME,user.getU_name());
        editor.putString(LoginSharedPref.USER_DOB,user.getDob());
        editor.putString(LoginSharedPref.USER_CONTACT_NO,user.getPhone_no().get(0));
        editor.putString(LoginSharedPref.USER_GENDER,user.getGender());
        editor.putString(LoginSharedPref.USER_COUNTRY, uAd.getCountry());
        editor.putString(LoginSharedPref.USER_STATE, uAd.getState());
        editor.putString(LoginSharedPref.USER_CITY,uAd.getCity());
        editor.putString(LoginSharedPref.USER_LANDMARK,uAd.getLandmark());
        editor.putString(LoginSharedPref.USER_STREET,uAd.getStreet());
        editor.putString(LoginSharedPref.USER_HNO,uAd.getH_no());
        editor.putInt(LoginSharedPref.USER_PICODE,uAd.getPincode());
        editor.putString(LoginSharedPref.USER_ACC_CREATION_DATE,user.getAcc_creation_date());
        editor.putString(LoginSharedPref.USER_TOKEN,user.getTokens().get(0).getToken());
        editor.putBoolean(LoginSharedPref.LOGGED_IN,true);
        editor.commit();
        mCallback.hideProgress();
        Log.d(Tag.MY_TAG,"save in sharedPref Sucess:  new user");

    }


    private void showConfirmSubmissionDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setMessage("Are you sure you want to submit?");
        builder.setTitle("Confirm Submission !");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                postUser();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private Boolean fetchData() {
        email=emailET.getText().toString();
        password=passET.getText().toString();
        uname=unameET.getText().toString();
        contactNo=contactET.getText().toString();
        int genderCheckedRadioButtonId = genderRgrp.getCheckedRadioButtonId();
        if (genderCheckedRadioButtonId == -1) {
            return false;
        }
        else{

            RadioButton radioButton = (RadioButton) genderRgrp.findViewById(genderCheckedRadioButtonId);
            gender =radioButton.getText().toString();
        }
        userPhoneCode=phoneCodeET.getText().toString();
        country=countryET.getText().toString();
        state=stateET.getText().toString();
        city=cityET.getText().toString();
        landmark=landET.getText().toString();
        street=streetET.getText().toString();
        hno=hnoET.getText().toString();
        pin=pinET.getText().toString();
        confirmPass=confirmPassET.getText().toString();
        if(email.isEmpty()||password.isEmpty()||uname.isEmpty()||contactNo.isEmpty()||gender.isEmpty()
        ||country.isEmpty()||state.isEmpty()||city.isEmpty()||landmark.isEmpty()||street.isEmpty()||userPhoneCode.isEmpty()
        ||hno.isEmpty()||pin.isEmpty()||confirmPass.isEmpty()){
            return  false;
        }
        int phoneLength=contactNo.length();
        if(phoneLength!=10){
            Toast.makeText(getContext(),"Enter a valid phone Number",Toast.LENGTH_SHORT).show();
            return false;
        }
        int pinLength=pin.length();
        if(pinLength!=6){
            Toast.makeText(getContext(),"Pincode is invalid",Toast.LENGTH_SHORT).show();
            return false;
        }
        address=new Address(hno,street,state,city,country,uname,landmark,Integer.parseInt(pin),contactNo);
        contactNo=userPhoneCode+" "+contactNo;
        return  true;
    }

}
