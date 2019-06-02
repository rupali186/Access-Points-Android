package com.multilingual.rupali.accesspoints.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.constants.BundleArg;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.models.User;

import java.util.ArrayList;
import java.util.Calendar;


import static com.multilingual.rupali.accesspoints.constants.Tag.MY_TAG;

public class CouponMailActivity extends AppCompatActivity {
    ImageView expDateIV;
    TextView expDateTV, discountPercentTV;
    EditText discountET;
    String expDate;
    int targetCustType;
    Integer discount;
    ConstraintLayout targetCustContent;
    ArrayList<User> userArrayList;
    Button sendMailBT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_mail);
        Bundle extras = getIntent().getExtras();
        targetCustType = extras.getInt(BundleArg.TARGET_CUST_TYPE);

        expDate = new String();
        discountPercentTV = findViewById(R.id.discount_percent_tv);
        sendMailBT = findViewById(R.id.send_mail_button);
        discountET = findViewById(R.id.discount_percent);
        expDateTV=findViewById(R.id.coupon_exp_date);
        expDateIV=findViewById(R.id.expiry_dropdown);
        targetCustContent = findViewById(R.id.mail_activity_content);
        setCustType();
        expDateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expDateOnClick();
            }
        });
        sendMailBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMailOnClick();
            }
        });
    }

    private void setCustType() {
        if(targetCustType== BundleArg.TARGET_CUSTOMERS) {
            discountPercentTV.setText("Discountfor Target Customers");
        }else if(targetCustType==BundleArg.NEW_TO_LOCKER){
            discountPercentTV.setText("Discount for New Locker Customers");

        }else if(targetCustType==BundleArg.NEW_CUSTOMERS){
            discountPercentTV.setText("Discount for New Customers");
        }
    }

    private void sendMailOnClick() {
        if(!fetchData()){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra("DISCOUNT",discount);
        intent.putExtra("EXPIRY_DATE", expDate);
        setResult(2,intent);
        finish();//finishing activity
    }

    private void expDateOnClick() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(CouponMailActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
//                        delDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                         expDate=year+"-"+(monthOfYear + 1) + "-"+dayOfMonth;
                        expDateTV.setText(expDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private boolean fetchData(){
        try{
            discount=Integer.parseInt(discountET.getText().toString());
        }catch (NumberFormatException e){
            Toast.makeText(CouponMailActivity.this, "entered value isn't Integer" +
                    "!!", Toast.LENGTH_SHORT).show();
            Log.w(MY_TAG, "entered value isn't Integer");
            return false;
        }
        if(expDate.isEmpty()){
            Toast.makeText(CouponMailActivity.this, "payment status is required!!" +
                    "!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        setResult(-1);
        finish();
    }



}
