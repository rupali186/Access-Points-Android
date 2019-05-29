package com.multilingual.rupali.accesspoints.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Constants.BundleArg;
import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.api.OrderApi;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.models.EditOrder;
import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.models.User;
import com.multilingual.rupali.accesspoints.response.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OrderEditActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ConstraintLayout contentOrder;
    Spinner orderStatusSpinner;
    TextView toolbarTextView;
    Button updateOrderButton;
    ArrayAdapter orderStatusAdapter;
    Retrofit retrofit;
    String orderStatus="";
    Bundle bundle;
    Intent intent;
    String orderID="";
    String oldOrderStatus="";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarTextView=findViewById(R.id.order_edit_act_toolbar);
        toolbarTextView.setText("Edit Order");
        progressBar=findViewById(R.id.order_edit_progress_bar);
        contentOrder=findViewById(R.id.content_order_edit);
        updateOrderButton=findViewById(R.id.edit_order_button);
        orderStatusSpinner=findViewById(R.id.order_status_edit_spinner);
        orderStatusAdapter = ArrayAdapter.createFromResource(OrderEditActivity.this,
                R.array.orderStatus, android.R.layout.simple_spinner_item);
        orderStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderStatusSpinner.setAdapter(orderStatusAdapter);
        orderStatus=new String();
        orderID=new String();
        updateOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOrder();
            }
        });
        sharedPreferences=getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        retrofit= APIClient.getClient();
        intent=getIntent();
        bundle=intent.getExtras();
    }

    private void updateOrder() {
        orderStatus=orderStatusSpinner.getSelectedItem().toString();
        if(orderStatus.isEmpty()){
            Toast.makeText(this,"Please select the order status",Toast.LENGTH_SHORT).show();
            return;
        }
        orderID=bundle.getString(BundleArg.ORDER_ID,"");
        oldOrderStatus=bundle.getString(BundleArg.ORDER_STATUS,"");
        if(orderID.isEmpty()){
            Toast.makeText(this,"Order Id is required.",Toast.LENGTH_SHORT).show();
            return;
        }else if(oldOrderStatus.equalsIgnoreCase(orderStatus)){
            Toast.makeText(this,"Order status is already "+oldOrderStatus+". Please select a new one to update it.",Toast.LENGTH_SHORT).show();
            return;
        }
        final String xAuth=sharedPreferences.getString(LoginSharedPref.USER_TOKEN,"");
        if(xAuth.isEmpty()){
            Toast.makeText(this,"User must be logged in.",Toast.LENGTH_SHORT).show();
            return;
        }
        showProgress();
        OrderApi orderApi=retrofit.create(OrderApi.class);
        EditOrder editOrder=new EditOrder(orderStatus);
        Call<Order> orderCall=orderApi.updateOrder(orderID,xAuth,editOrder);
        orderCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()) {
                    hideProgress();
                    Toast.makeText(OrderEditActivity.this,"Order status updated sucessfully. ", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Order status update success");
                    onBackPressed();

                }else{
                    hideProgress();
                    Toast.makeText(OrderEditActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "Order status update failed. Code: "+response.code()+response.message());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderEditActivity.this,"Please check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "Order status update failed. Code: Message: " +t.getMessage()+"Local msg: "+
                        t.getLocalizedMessage()+"cause: "+t.getCause());
            }
        });

    }
    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        contentOrder.setVisibility(View.INVISIBLE);
    }
    private void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        contentOrder.setVisibility(View.VISIBLE);
    }

}
