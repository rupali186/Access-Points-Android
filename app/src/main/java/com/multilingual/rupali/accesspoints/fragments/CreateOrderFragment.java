package com.multilingual.rupali.accesspoints.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.constants.StringConstants;
import com.multilingual.rupali.accesspoints.constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.adapters.AccessPointsAdapter;
import com.multilingual.rupali.accesspoints.api.AccessPointAPI;
import com.multilingual.rupali.accesspoints.api.MailAPI;
import com.multilingual.rupali.accesspoints.api.CouponApi;
import com.multilingual.rupali.accesspoints.api.OrderApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.config.APIClientMail;
import com.multilingual.rupali.accesspoints.config.APIClientAccessPts;
import com.multilingual.rupali.accesspoints.models.AccessPointAddress;
import com.multilingual.rupali.accesspoints.models.AcessPointDetail;
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.Coupon;
import com.multilingual.rupali.accesspoints.models.EditCoupon;
import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.models.Size;
import com.multilingual.rupali.accesspoints.response.AccessPointsResponse;
import com.multilingual.rupali.accesspoints.response.CouponResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static com.multilingual.rupali.accesspoints.constants.StringConstants.DeliveryMode.ACCESS_PTS;
import static com.multilingual.rupali.accesspoints.constants.Tag.MY_TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrderFragment extends Fragment {
    String couponID=null;
    String hno;
    String street, state, country, city, uname, landmark, pin, contactNo;
    EditText lengthET;
    EditText widthET;
    EditText heightET;
    EditText priceET;
    EditText weightET;
    TextView delDateTV;
    ImageView delDateIV;
    Spinner categorySpinner;
    Spinner productIdSpinner;
    Spinner delModeSpinner;
    Spinner paymentStatSpinner;
    Button createOrderButton;
    Button applyPromoCodeButton;
    RecyclerView accessRecyclerView;
    ArrayAdapter<CharSequence> categoryAdapter;
    ArrayAdapter<CharSequence> prodIdAdapter;
    ArrayAdapter<CharSequence> delModeAdapter;
    ArrayAdapter<CharSequence> payStatAdapter;
    AccessPointsAdapter adapter;
    Retrofit retrofit, retrofit2;
    String delMode,paymentStatus,oDate,delDate;
    Size size;
    String userEmail;
    Integer categoryId,productId;
    Double price,weight,discountOnPrice=0.0;
    SharedPreferences sharedPreferences;
    Retrofit  retrofitMail;
    ProgressListener mCallback;
    String range;
    Address address;
    AcessPointDetail accessPointDetail;
    ArrayList<AcessPointDetail> accessArrayList;
    boolean couponUsed=false;

    //for progress dialog
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
    public CreateOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_create_order, container, false);
        lengthET=view.findViewById(R.id.order_length);
        widthET=view.findViewById(R.id.order_width);
        heightET=view.findViewById(R.id.order_height);
        priceET=view.findViewById(R.id.order_price);
        weightET=view.findViewById(R.id.order_weight);
        delDateTV=view.findViewById(R.id.order_del_date);
        delDateIV=view.findViewById(R.id.dob_dropdown);
        createOrderButton=view.findViewById(R.id.create_order_button);
        applyPromoCodeButton=view.findViewById(R.id.promo_code_button);
        categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        productIdSpinner=view.findViewById(R.id.productid_spinner);
        delModeSpinner=view.findViewById(R.id.delivery_mode_spinner);
        accessRecyclerView = (RecyclerView) view.findViewById(R.id.access_point_recycler_view);
        paymentStatSpinner=view.findViewById(R.id.payment_status_spinner);
        categoryAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        prodIdAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.productIds, android.R.layout.simple_spinner_item);
        delModeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.deliveryModes, android.R.layout.simple_spinner_item);
        payStatAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.paymentStatuses, android.R.layout.simple_spinner_item);

        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prodIdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        delModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payStatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        productIdSpinner.setAdapter(prodIdAdapter);
        delModeSpinner.setAdapter(delModeAdapter);
        paymentStatSpinner.setAdapter(payStatAdapter);
        sharedPreferences=getActivity().getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        accessPointDetail = new AcessPointDetail();

        //retrofit variables
        retrofit= APIClient.getClient();
        retrofit2= APIClientAccessPts.getClient();
        retrofitMail = APIClientMail.getClientMail();

        //Listeners
        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder(view);
            }
        });
        applyPromoCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyPromoCodeOnClick();
            }
        });
        applyPromoCodeButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                applyPromoCodeOnLongClick();
                return true;
            }
        });
        delDateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delDateOnClick();
            }
        });
        size=new Size(0,0,0);
        delMode=new String();
        paymentStatus=new String("");
        oDate=new String();
        delDate=new String();
        street=sharedPreferences.getString(LoginSharedPref.USER_STREET,"");
        landmark = sharedPreferences.getString(LoginSharedPref.USER_LANDMARK,"");
        city=sharedPreferences.getString(LoginSharedPref.USER_CITY,"");
        state=sharedPreferences.getString(LoginSharedPref.USER_STATE,"");
        country=sharedPreferences.getString(LoginSharedPref.USER_COUNTRY,"");
        hno = sharedPreferences.getString(LoginSharedPref.USER_HNO,"");
        uname = sharedPreferences.getString(LoginSharedPref.USER_NAME,"");
        Integer pinn = sharedPreferences.getInt(LoginSharedPref.USER_PICODE,0);
        contactNo = sharedPreferences.getString(LoginSharedPref.USER_CONTACT_NO,"");
        userEmail=sharedPreferences.getString(LoginSharedPref.USER_EMAIL,"");

        address=new Address(hno,street,state,city,country,uname,landmark,pinn,contactNo);
        delModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = parentView.getItemAtPosition(position).toString();
                if(selectedItem.equals("access_points")) {
                    // do your stuff
                    showRangeDialogBox();
                }
                else{
                    accessPointDetail = new AcessPointDetail();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        return view;
    }

    private void applyPromoCodeOnLongClick() {
        if(couponUsed){
            try {
                mCallback.showProgress();
                Thread.sleep(2000);
                price+=discountOnPrice;
                priceET.setText(price+"");
                applyPromoCodeButton.setText("Apply Promo Code");
                applyPromoCodeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                couponUsed=false;
                mCallback.hideProgress();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchDiscount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter your Promo Code");
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText promoCodeET = new EditText(getContext());
        promoCodeET.setHint("Type your code here...");
        layout.addView(promoCodeET);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = promoCodeET.getText().toString();
                searchCouponCode(code,userEmail);

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

    private void applyPromoCodeOnClick() {
        if(!fetchData()){
            return;
        }
        if(!couponUsed&&delMode.equalsIgnoreCase(ACCESS_PTS)) {
            fetchDiscount();
        }else{
            if(!delMode.equalsIgnoreCase(ACCESS_PTS)){
                Toast.makeText(getContext(),"delivery mode should be access points to apply promo code." ,Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"You have already used a promo code on this order. Long click to remove the promo code.",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void searchCouponCode(String code,String user_email) {
        CouponApi couponApi=retrofit.create(CouponApi.class);
        Call<CouponResponse> couponResponseCall=couponApi.searchCouponCode(code,user_email);
        couponResponseCall.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if(response.isSuccessful()){
                    CouponResponse couponResponse = response.body();
                    Coupon coupon=couponResponse.coupon;
                    Date currentDate = Calendar.getInstance().getTime();
                    String formattedExpiryDate=coupon.getExpiry_date();
                    int discount=coupon.getDiscount();
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date expiryDate = dateFormat.parse(formattedExpiryDate);
                        if(currentDate.after(expiryDate)){
                            Toast.makeText(getContext(),"Your Coupon has been expired",Toast.LENGTH_SHORT).show();
                        }else if(coupon.getUsed()){
                            Toast.makeText(getContext(),"This Coupon code has already been used",Toast.LENGTH_SHORT).show();

                        }else{
                            discountOnPrice=(price*discount*1.0)/100;
                            price-=discountOnPrice;
                            priceET.setText(price+"");
                            applyPromoCodeButton.setText("You got a discount of "+discount+"% on this order. Final Amount to be paid: "+price);
                            couponUsed=true;
                            couponID = coupon.get_id();
                            applyPromoCodeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),"An exception occured while parsing discount.",Toast.LENGTH_SHORT).show();
                        Log.d(MY_TAG,"An exception occured while parsing discount."+e.getLocalizedMessage()+e.getMessage()+e.getCause());

                    }

                }else{
                    Toast.makeText(getContext(),"Make sure you entered an unused coupon code or check " +
                            "your network connection.",Toast.LENGTH_SHORT).show();
                    Log.d(MY_TAG,"Search coupon api failed"+response.code());

                }
            }
            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Toast.makeText(getContext(), " check your network connection.",Toast.LENGTH_SHORT).show();
                Log.d(MY_TAG,"Search coupon api failed"+t.getMessage());
            }
        });

    }

    private void updateCouponCode(String couponId){
        CouponApi couponApi=retrofit.create(CouponApi.class);
        EditCoupon editCoupon=new EditCoupon(true);
        Call<CouponResponse> couponResponseCall=couponApi.updateCoupon(couponId,editCoupon);
        couponResponseCall.enqueue(new Callback<CouponResponse>() {
            @Override
            public void onResponse(Call<CouponResponse> call, Response<CouponResponse> response) {
                if(response.isSuccessful()){
                    Log.d(MY_TAG,"Update coupon api sucess");
                }else{
                    Toast.makeText(getContext(), " check your network connection.",Toast.LENGTH_SHORT).show();
                    Log.d(MY_TAG,"Update coupon api failed");
                }
            }

            @Override
            public void onFailure(Call<CouponResponse> call, Throwable t) {
                Toast.makeText(getContext(), " check your network connection.",Toast.LENGTH_SHORT).show();
                Log.d(MY_TAG,"Update coupon api failed"+t.getMessage());
            }
        });

    }

    private void showRangeDialogBox() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CreateOrderFragment.this.getActivity());
        //  alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter Range");

        final EditText input = new EditText(CreateOrderFragment.this.getActivity());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input); // uncomment this line

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            range = input.getText().toString();
                            Integer rangeValid = Integer.parseInt(range);
                        } catch (NumberFormatException e) {
                            Log.w(MY_TAG, "entered value isn't Integer");
                            Toast.makeText(getContext(),"Please enter a valid integer with no spaces",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        accessArrayList=new ArrayList<>();
                        accessRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


                        adapter=new AccessPointsAdapter(getContext(), accessArrayList, new AccessPointsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                accessPointDetail= new AcessPointDetail(accessArrayList.get(position).getAddress());
                            }

                        }, new AccessPointsAdapter.OnItemLongClickListener() {
                            @Override
                            public void onItemLongClick(int position) {

                            }
                        });
                        accessRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                        accessRecyclerView.setAdapter(adapter);

                        mCallback.showProgress();
                         fetchAccessPoint();


                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void fetchAccessPoint() {

        String final_street = street+" "+landmark;
        AccessPointAddress addressAccess = new AccessPointAddress(final_street, city, state, country, range);
       // Toast.makeText(getContext(),address.getStreet(), Toast.LENGTH_LONG).show();
        AccessPointAPI accessPointAPI=retrofit2.create(AccessPointAPI.class);
        Call<AccessPointsResponse> accessResponseCall=accessPointAPI.getAccessPoints(addressAccess);
        accessResponseCall.enqueue(new Callback<AccessPointsResponse>() {
            @Override
            public void onResponse(Call<AccessPointsResponse> call, Response<AccessPointsResponse> response) {
                if(response.isSuccessful()) {
                    mCallback.hideProgress();
                    AccessPointsResponse accessResponse=response.body();
                    accessArrayList.clear();
                    ArrayList<AcessPointDetail> access=accessResponse.result;
                    if(access == null){
                        Toast.makeText(getContext(),"No Access Point Available", Toast.LENGTH_SHORT).show();
                        return;
                    }

                   accessArrayList.addAll(access);
                  adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Fetched sucessfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"My Orders success: Body: "+response.body()+"");
                }else{
                    mCallback.hideProgress();
                    Toast.makeText(getContext(),"No Access Point or check  your internet ", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "my orders fetch failed. Code: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<AccessPointsResponse> call, Throwable t) {
                 mCallback.hideProgress();
                Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, " get access point request submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
                        t.getLocalizedMessage()+"Cause: "+t.getCause());           }
        });
    }


    private void delDateOnClick() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
//                        delDate=dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        delDate=year+"-"+(monthOfYear + 1) + "-"+dayOfMonth;
                        delDateTV.setText(delDate);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void createOrder(View view) {
        if(!fetchData()){
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
                postOrder();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void postOrder() {
        Order order;
        mCallback.showProgress();

        if(accessPointDetail.getAddress()== null){
             order=new Order(size,price,categoryId,productId,paymentStatus,delDate, delMode,weight, address);
        }
        else{
             order=new Order(size,price,categoryId,productId,paymentStatus,delDate, delMode,weight, address, accessPointDetail);
            // Log
        }

        Log.d(Tag.MY_TAG+" order: ","size: "+size+" price: "+price+" delDate: "+delDate+ " categoryId: "
        +categoryId+" productId: "+productId+" weight: "+weight+" delmode: "+delMode+" paymentstatus: "+paymentStatus);
        OrderApi orderApi=retrofit.create(OrderApi.class);
        final String xAuth=sharedPreferences.getString(LoginSharedPref.USER_TOKEN,"");
        Call<Order> orderResponse=orderApi.createNewOrder(xAuth,order);
        orderResponse.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if(response.isSuccessful()) {
                    if(couponUsed){
                        updateCouponCode(couponID);
                    }

                    Order order=response.body();
                    Toast.makeText(getContext(),"Order Created Successfully. ", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Order creted Success: Body: "+response.body()+" id: "+order.get_id());
                    MailAPI mailApi = retrofitMail.create(MailAPI.class);
                    Call<Order> mailResponse=mailApi.sendOrderDetail(order);
                    mailResponse.enqueue(new Callback<Order>() {

                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            if(response.isSuccessful()){
                                Log.d(Tag.MY_TAG,"Mail sent successfully "+response.body()+"");
                            }else{
                                Log.d(Tag.MY_TAG, "mail sending failed "+response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Log.d(Tag.MY_TAG, "Mail sending failed. Message: " +t.getMessage()+"Local msg: "+
                                    t.getLocalizedMessage()+"Ccause: "+t.getCause());
                        }
                    });
                    mCallback.hideProgress();
                    getActivity().onBackPressed();
                }else{
                    mCallback.hideProgress();
                    Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "order post submitted to API failed."+response.code()+" Headers:"+
                            response.headers()+"Message: "+response.message()+"x Auth: "+xAuth+"Call :"+call.toString()+response.toString());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                mCallback.hideProgress();
                Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "order post submitted to API failed." +t.getMessage());
            }
        });
    }

    private boolean fetchData() {
//        userId=sharedPreferences.getString(LoginSharedPref.USER_ID,"");
        delMode=delModeSpinner.getSelectedItem().toString();
        paymentStatus=paymentStatSpinner.getSelectedItem().toString();
        categoryId=categorySpinner.getSelectedItemPosition()+1;
        try{
            size.setLength(Integer.parseInt(lengthET.getText().toString()));
            size.setWidth(Integer.parseInt(widthET.getText().toString()));
            size.setHeight(Integer.parseInt(heightET.getText().toString()));
            price=Double.parseDouble(priceET.getText().toString());
            productId=Integer.parseInt(productIdSpinner.getSelectedItem().toString());
            weight=Double.parseDouble(weightET.getText().toString());
        }catch (NumberFormatException e){
            Log.w(MY_TAG, "entered value isn't Integer");
        }

//        if(userId.isEmpty()){
//            Toast.makeText(getContext(),"user should be logged In" +
//                    "!!",Toast.LENGTH_SHORT).show();
//            return false;
//        }else
        if(delMode.isEmpty()) {
            Toast.makeText(getContext(), "DelMode is required!!" +
                    "!!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(paymentStatus.isEmpty()){
            Toast.makeText(getContext(), "payment status is required!!" +
                    "!!", Toast.LENGTH_SHORT).show();
            return false;
        }else if(delDate.isEmpty()){
            Toast.makeText(getContext(),"Del date is required!!",Toast.LENGTH_SHORT).show();
            return  false;
        }
        if(categoryId<=0||productId<=0||
                price<=0||size.getLength()<=0||size.getWidth()<=0||size.getHeight()<=0||
                weight<=0){
            Toast.makeText(getContext(),"Check if size is <=0 or ids are negative" +
                    "!!",Toast.LENGTH_SHORT).show();
            return  false;
        }
        if(paymentStatus.equals(StringConstants.PaymentStatus.UNPAID)&&(delMode.equals(StringConstants.DeliveryMode.STORE_DEL)||
                delMode.equals(ACCESS_PTS))){
            Toast.makeText(getContext(),"Payment status should be paid for store delivery and access points." +
                    "!!",Toast.LENGTH_SHORT).show();
            return  false;
        }
        if(delMode.equals(ACCESS_PTS) && accessPointDetail.getAddress() == null ){
            Toast.makeText(getContext(),"Choose an access point" +
                    "!!",Toast.LENGTH_SHORT).show();
            return  false;

        }
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        oDate = df.format(date);
        return  true;
    }


}
