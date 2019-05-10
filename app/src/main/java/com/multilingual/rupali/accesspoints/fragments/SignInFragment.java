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
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.SignInUser;
import com.multilingual.rupali.accesspoints.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {
    EditText emailET;
    EditText passET;
    Button signInButton;
    Address address;
    String email,password;
    Retrofit retrofit;
    SharedPreferences sharedPreferences;
    SignInFragment.ProgressListener mCallback;

    public interface ProgressListener{
        void showProgress();
        void hideProgress();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback=(SignInFragment.ProgressListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+ "must implement ProgressListener");
        }
    }
    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sign_in, container, false);
        emailET=view.findViewById(R.id.sign_in_email);
        passET=view.findViewById(R.id.sign_in_password);
        signInButton=view.findViewById(R.id.sign_in_button);
        retrofit= APIClient.getClient();
        sharedPreferences=getActivity().getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInOnClick(view);
            }
        });
        return  view;
    }

    private void signInOnClick(View view) {
        email=emailET.getText().toString();
        password=passET.getText().toString();
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(getContext(),"All the fields are required",Toast.LENGTH_SHORT).show();
            return;
        }
        showConfirmSubmissionDialog();
    }
    private void showConfirmSubmissionDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());

        builder.setMessage("You have checked the details before Signing In?");
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
                logInUser();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void logInUser() {
        mCallback.showProgress();
        UserApi userApi=retrofit.create(UserApi.class);
        SignInUser user=new SignInUser(email,password);
        Call<User> userResponse=userApi.loginUser(user);
        userResponse.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    User user=response.body();
                    saveUserInSharedPref( user);
                    Toast.makeText(getContext(),"Logged In Successfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Logged in success: Body: "+response.body()+"");
                    //mCallback.hideProgress();
                    getActivity().onBackPressed();
                }else{
                    mCallback.hideProgress();
                    Toast.makeText(getContext(),"Please enter the correct details", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "sign in post submitted to API failed.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mCallback.hideProgress();
                Toast.makeText(getContext(),"Sign In failed. Check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "sign in post submitted to API failed." +t.getMessage());
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
        Log.d(Tag.MY_TAG,"save in sharedPref Sucess:  logged in user");

    }

}
