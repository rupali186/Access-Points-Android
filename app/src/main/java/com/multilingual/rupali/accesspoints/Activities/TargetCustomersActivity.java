package com.multilingual.rupali.accesspoints.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Constants.BundleArg;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.adapters.TargetcustomerAdapter;
import com.multilingual.rupali.accesspoints.api.CouponApi;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.models.Coupon;
import com.multilingual.rupali.accesspoints.models.SignInUser;
import com.multilingual.rupali.accesspoints.models.User;
import com.multilingual.rupali.accesspoints.response.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TargetCustomersActivity extends AppCompatActivity {
    RecyclerView targetCustRecyclerView;
    TextView toolbarTextView;
    ProgressBar progressBar;
    ConstraintLayout targetCustContent;
    ArrayList<User> userArrayList;
    TargetcustomerAdapter targetcustomerAdapter;
    Retrofit retrofit;
    int threshold,limit;
    int targetCustType=BundleArg.TARGET_CUSTOMERS;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_customers);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbarTextView=findViewById(R.id.target_cust_act_toolbar);
        setSupportActionBar(toolbar);
        toolbarTextView.setText("Target Customers");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        targetCustRecyclerView=findViewById(R.id.target_cus_recycler_view);
        targetCustContent=findViewById(R.id.content_target_cust);
        progressBar=findViewById(R.id.target_cust_progress_bar);
        userArrayList=new ArrayList<>();
        targetcustomerAdapter=new TargetcustomerAdapter(this, userArrayList, new TargetcustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemCilck(int position) {
            }
        });
        targetCustRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        targetCustRecyclerView.setAdapter(targetcustomerAdapter);
        retrofit= APIClient.getClient();
        Intent intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null){
            fetchDataFromBundle();
        }

    }

    private void fetchDataFromBundle() {
        targetCustType=bundle.getInt(BundleArg.TARGET_CUST_TYPE);
        limit=bundle.getInt(BundleArg.LIMIT);
        if(targetCustType==BundleArg.TARGET_CUSTOMERS) {
            toolbarTextView.setText("Target Customers");
            threshold = bundle.getInt(BundleArg.THRESHOLD);
            fetchTargetCustomers();
        }else if(targetCustType==BundleArg.NEW_TO_LOCKER){
            toolbarTextView.setText("New Locker Customers");
            fetchNewToLockers();
        }else if(targetCustType==BundleArg.NEW_CUSTOMERS){
            toolbarTextView.setText("New Customers");
            fetchNewCustomers();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_target_customers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendMail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMail() {

    }

    private void fetchNewCustomers() {
        showProgress();
        UserApi userApi=retrofit.create(UserApi.class);
        Call<UserResponse> userResponse=userApi.getNewUsers(limit);
        userResponse.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                hideProgress();
                if(response.isSuccessful()) {
                    UserResponse userResponse1=response.body();
                    ArrayList<User> users=userResponse1.users;
                    userArrayList.clear();
                    userArrayList.addAll(users);
                    targetcustomerAdapter.notifyDataSetChanged();
                    Toast.makeText(TargetCustomersActivity.this,"Fetched sucessfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Target Customers success: Body: "+response.body()+"");
                }else{
                    Toast.makeText(TargetCustomersActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "target customers fetch failed. Code: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(TargetCustomersActivity.this,"Target customers fetch failed. Check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "target customers post submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
                        t.getLocalizedMessage()+"Ccause: "+t.getCause());
            }
        });
    }

    private void fetchNewToLockers() {
        showProgress();
        UserApi userApi=retrofit.create(UserApi.class);
        Call<UserResponse> userResponse=userApi.getNewLockerUsers(limit);
        userResponse.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()) {
                    hideProgress();
                    UserResponse userResponse1=response.body();
                    ArrayList<User> users=userResponse1.users;
                    userArrayList.clear();
                    userArrayList.addAll(users);
                    targetcustomerAdapter.notifyDataSetChanged();
                    Toast.makeText(TargetCustomersActivity.this,"Fetched sucessfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Target Customers success: Body: "+response.body()+"");
                }else{
                    hideProgress();
                    Toast.makeText(TargetCustomersActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "target customers fetch failed. Code: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(TargetCustomersActivity.this,"Target customers fetch failed. Check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "target customers post submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
                        t.getLocalizedMessage()+"Ccause: "+t.getCause());            }
        });
    }

    private void fetchTargetCustomers() {
        showProgress();
        UserApi userApi=retrofit.create(UserApi.class);
        Call<UserResponse> userResponse=userApi.getTargetUsers(limit,threshold);
        userResponse.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()) {
                    hideProgress();
                    UserResponse userResponse1=response.body();
                    ArrayList<User> users=userResponse1.users;
                    userArrayList.clear();
                    userArrayList.addAll(users);
                    targetcustomerAdapter.notifyDataSetChanged();
                    Toast.makeText(TargetCustomersActivity.this,"Fetched sucessfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Target Customers success: Body: "+response.body()+"");
                }else{
                    hideProgress();
                    Toast.makeText(TargetCustomersActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "target customers fetch failed. Code: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                hideProgress();
                Toast.makeText(TargetCustomersActivity.this,"Target customers fetch failed. Check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "target customers post submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
                        t.getLocalizedMessage()+"Ccause: "+t.getCause());            }
        });

    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        targetCustContent.setVisibility(View.INVISIBLE);
    }
    private void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        targetCustContent.setVisibility(View.VISIBLE);
    }


}
