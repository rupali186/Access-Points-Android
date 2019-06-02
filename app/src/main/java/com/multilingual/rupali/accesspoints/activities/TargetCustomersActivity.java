package com.multilingual.rupali.accesspoints.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
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

import com.multilingual.rupali.accesspoints.constants.BundleArg;
import com.multilingual.rupali.accesspoints.constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.adapters.TargetcustomerAdapter;
import com.multilingual.rupali.accesspoints.api.CouponApi;
import com.multilingual.rupali.accesspoints.api.MailAPI;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.config.APIClientMail;
import com.multilingual.rupali.accesspoints.models.Coupon;
import com.multilingual.rupali.accesspoints.models.CouponMail;
import com.multilingual.rupali.accesspoints.response.CouponResponse;
import com.multilingual.rupali.accesspoints.models.User;
import com.multilingual.rupali.accesspoints.response.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TargetCustomersActivity extends AppCompatActivity {
    RecyclerView targetCustRecyclerView;
    TextView toolbarTextView;
    ProgressBar progressBar;
    ConstraintLayout targetCustContent;
    ArrayList<User> userArrayList;
    ArrayList<User> users;
    TargetcustomerAdapter targetcustomerAdapter;
    Retrofit retrofit, retrofitMail;
    int threshold,limit, discount;
    String expiry_date;
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
        retrofitMail = APIClientMail.getClientMail();
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

    @SuppressLint("RestrictedApi")
    private void sendMail() {
        showProgress();
        Bundle bundle = new Bundle();
        bundle.putInt(BundleArg.TARGET_CUST_TYPE, targetCustType);
        Intent intent=new Intent(TargetCustomersActivity.this, CouponMailActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, 2, bundle);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode==2)
        {
            expiry_date=data.getStringExtra("EXPIRY_DATE");
            discount = data.getIntExtra("DISCOUNT", 0);
            final CouponApi couponApi=retrofit.create(CouponApi.class);
            for(int i=0;i<userArrayList.size();i++){
                final String user_email=userArrayList.get(i).getEmail();
                final String name = userArrayList.get(i).getU_name();
//                Toast.makeText(TargetCustomersActivity.this,user_email+discount+expiry_date, Toast.LENGTH_SHORT).show();
                final Coupon coupon=new Coupon(discount, user_email, expiry_date);
                Call<CouponResponse> couponResponse=couponApi.createCouponCode(coupon);
                couponResponse.enqueue(new Callback<CouponResponse>() {
                    @Override
                    public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                        if(response.isSuccessful()){
                            MailAPI mailApi = retrofitMail.create(MailAPI.class);
                            CouponResponse couponResponse = response.body();
                            Coupon coupon1=couponResponse.coupon;
//                            Toast.makeText(TargetCustomersActivity.this,coupon1.getCode()+" ", Toast.LENGTH_SHORT).show();

                            //send coupon mail
                            CouponMail couponMail = new CouponMail(user_email,name, coupon1.getCode());
                            Call<CouponMail> couponResponseMail=mailApi.sendCoupon(couponMail);
                            couponResponseMail.enqueue(new Callback<CouponMail>() {
                                @Override
                                public void onResponse(Call<CouponMail> call, Response<CouponMail> responseMail) {
                                    hideProgress();
                                    if(responseMail.isSuccessful()){
                                        Log.d(Tag.MY_TAG,"Mail sent successfully "+responseMail.body()+"");
                                    }else{
                                        Log.d(Tag.MY_TAG, "mail sending failed "+responseMail.code());
                                    }
                                }

                                @Override
                                public void onFailure(Call<CouponMail> call, Throwable t) {
                                    hideProgress();
                                    Log.d(Tag.MY_TAG, "Mail sending failed. Message: " +t.getMessage()+"Local msg: "+
                                            t.getLocalizedMessage()+"Ccause: "+t.getCause());
                                }
                            });
                            Toast.makeText(TargetCustomersActivity.this,"Coupon generated sucessfully.", Toast.LENGTH_SHORT).show();
                            Log.d(Tag.MY_TAG,"Coupon code success: Body: "+response.body()+"");
                        }else{
                            Toast.makeText(TargetCustomersActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                            Log.d(Tag.MY_TAG, "coupon code post failed. Code: "+response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<CouponResponse> call, Throwable t) {
                        hideProgress();
                        Toast.makeText(TargetCustomersActivity.this,"Coupon can't be generated. Check your network connection.", Toast.LENGTH_SHORT).show();
                        Log.d(Tag.MY_TAG, "coupon code post submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
                                t.getLocalizedMessage()+"Ccause: "+t.getCause());
                    }
                });
            }
        }
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
                    users = new ArrayList<>();
                    users=userResponse1.users;
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
                    users = new ArrayList<>();
                    users=userResponse1.users;
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
                    users = new ArrayList<>();
                    users=userResponse1.users;
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

    @Override
    protected void onResume() {
        super.onResume();
        userArrayList.clear();
        if(users!=null){
            userArrayList.addAll(users);
        }

        targetCustRecyclerView.setAdapter(targetcustomerAdapter);
        targetcustomerAdapter.notifyDataSetChanged();
    }
}
