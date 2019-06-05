package com.multilingual.rupali.accesspoints.fragments;


import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.EditUser;
import com.multilingual.rupali.accesspoints.models.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAccountDetailsFragment extends Fragment {
    TextView userEmailTv;
    TextView userDobTV;
    ImageView userDobIV;
    RadioGroup genderRadioGroup;
    EditText userNameET;
    EditText usrPhoneET;
    EditText userPhoneCodeET;
    EditText usrCountryET;
    EditText usrStateET;
    EditText usrCityET;
    EditText usrStreetET;
    EditText usrLandmarkET;
    EditText usrHnoET;
    EditText usrPincodeET;
    Button submitButton;
    SharedPreferences sharedPreferences;
    Retrofit retrofit;
    String dob="",gender="",userName="",phone="",phoneCode="",country="",state="",city="",landmark="",street="",hno="",
            pincode="",email="";
    Address address;
    ProgressListener mCallback;
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
    public EditAccountDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_account_details, container, false);
        userEmailTv=view.findViewById(R.id.user_email);
        userDobTV=view.findViewById(R.id.user_dob);
        userDobIV=view.findViewById(R.id.dob_dropdown);
        userNameET=view.findViewById(R.id.user_name);
        usrPhoneET=view.findViewById(R.id.user_phone);
        userPhoneCodeET=view.findViewById(R.id.user_phone_code);
        genderRadioGroup=view.findViewById(R.id.user_gender_radiogroup);
        usrCountryET=view.findViewById(R.id.user_country);
        usrStateET=view.findViewById(R.id.user_state);
        usrCityET=view.findViewById(R.id.user_city);
        usrLandmarkET=view.findViewById(R.id.user_landmark);
        usrHnoET=view.findViewById(R.id.user_hno);
        usrPincodeET=view.findViewById(R.id.user_pin_code);
        usrStreetET=view.findViewById(R.id.user_street);
        submitButton=view.findViewById(R.id.submit_user);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButtonOnClick();
            }
        });
        sharedPreferences=getActivity().getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        retrofit= APIClient.getClient();
        populateDataFromSharedPreferences();
        return view;
    }

    private void populateDataFromSharedPreferences() {
        email=sharedPreferences.getString(LoginSharedPref.USER_EMAIL,"");
        dob=sharedPreferences.getString(LoginSharedPref.USER_DOB,"");
        gender=sharedPreferences.getString(LoginSharedPref.USER_GENDER,"");
        userName=sharedPreferences.getString(LoginSharedPref.USER_NAME,"");
        phone=sharedPreferences.getString(LoginSharedPref.USER_CONTACT_NO,"").substring(4);
        phoneCode=sharedPreferences.getString(LoginSharedPref.USER_CONTACT_NO,"").substring(0,2);
        country=sharedPreferences.getString(LoginSharedPref.USER_COUNTRY,"");
        state=sharedPreferences.getString(LoginSharedPref.USER_STATE,"");
        city=sharedPreferences.getString(LoginSharedPref.USER_CITY,"");
        landmark=sharedPreferences.getString(LoginSharedPref.USER_LANDMARK,"");
        street=sharedPreferences.getString(LoginSharedPref.USER_STREET,"");
        hno=sharedPreferences.getString(LoginSharedPref.USER_HNO,"");
        pincode=sharedPreferences.getInt(LoginSharedPref.USER_PICODE,0)+"";
        userEmailTv.setText(email);
        userDobTV.setText(dob);
        int genderId=0;
        if(gender.equalsIgnoreCase("male")){
            genderId=R.id.male;
        }else  if(gender.equalsIgnoreCase("female")){
            genderId=R.id.female;
        } if(gender.equalsIgnoreCase("other")){
            genderId=R.id.other;
        }
        genderRadioGroup.check(genderId);
        userNameET.setText(userName);
        usrPhoneET.setText(phone);
        userPhoneCodeET.setText(phoneCode);
        usrCountryET.setText(country);
        usrStateET.setText(state);
        usrCityET.setText(country);
        usrLandmarkET.setText(landmark);
        usrStreetET.setText(street);
        usrHnoET.setText(hno);
        usrPincodeET.setText(pincode);
    }

    private void submitButtonOnClick() {
        if(!fetchData()){
            Toast.makeText(getContext(),"All the fields should be filled ",Toast.LENGTH_SHORT).show();
            return;
        }
        showConfirmSubmissionDialog();
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
                updateUser();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    private void updateUser() {
        mCallback.showProgress();
        ArrayList<String> contactNos=new ArrayList<>();
        ArrayList<Address> addresses=new ArrayList<>();
        contactNos.add(phone);
        addresses.add(address);
        String userId=sharedPreferences.getString(LoginSharedPref.USER_ID,"");
        if(email.isEmpty()||userId.isEmpty()){
            Toast.makeText(getContext(),"Email and UID is required. Make sure you are logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        User user=new User(email,userName,contactNos,dob,gender,addresses);
        String xAuth=sharedPreferences.getString(LoginSharedPref.USER_TOKEN,"");
        UserApi userApi=retrofit.create(UserApi.class);
        Call<EditUser> userResponse=userApi.updateUser(userId,xAuth,user);
        userResponse.enqueue(new Callback<EditUser>() {
            @Override
            public void onResponse(Call<EditUser> call, Response<EditUser> response) {
                if(response.isSuccessful()) {
                    EditUser editUser=response.body();
                    User user=editUser.user;
                    saveUserInSharedPref( user);
                    Toast.makeText(getContext(),"User Updated Successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"User update Success: Body: "+response.body()+" code: "+response.code());
                    getActivity().onBackPressed();
                }else{
                    mCallback.hideProgress();
                    Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "update post submitted to API failed."+response.body()+" code: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<EditUser> call, Throwable t) {
                mCallback.hideProgress();
                Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "update post submitted to API failed." +t.getMessage());
            }

        });
    }
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
    private Boolean fetchData() {
        userName=userNameET.getText().toString();
        phone=usrPhoneET.getText().toString();
        int genderCheckedRadioButtonId = genderRadioGroup.getCheckedRadioButtonId();
        if (genderCheckedRadioButtonId == -1) {
            return false;
        }
        else{

            RadioButton radioButton = (RadioButton) genderRadioGroup.findViewById(genderCheckedRadioButtonId);
            gender =radioButton.getText().toString();
        }
        phoneCode=userPhoneCodeET.getText().toString();
        country=usrCountryET.getText().toString();
        state=usrStateET.getText().toString();
        city=usrCityET.getText().toString();
        landmark=usrLandmarkET.getText().toString();
        street=usrStreetET.getText().toString();
        hno=usrHnoET.getText().toString();
        pincode=usrPincodeET.getText().toString();
        if(userName.isEmpty()||phone.isEmpty()||gender.isEmpty()||phoneCode.isEmpty()
                ||country.isEmpty()||state.isEmpty()||city.isEmpty()||landmark.isEmpty()||street.isEmpty()
                ||hno.isEmpty()||pincode.isEmpty()){
            return  false;
        }
        int phoneLength=phone.length();
        if(phoneLength!=10){
            Toast.makeText(getContext(),"Enter a valid phone Number",Toast.LENGTH_SHORT).show();
            return false;
        }
        int pinLength=pincode.length();
        if(pinLength!=6){
            Toast.makeText(getContext(),"Pincode is invalid",Toast.LENGTH_SHORT).show();
            return false;
        }
        address=new Address(hno,street,state,city,country,userName,landmark,Integer.parseInt(pincode),phone);
        phone=phoneCode+" "+phone;
        return  true;
    }
}
