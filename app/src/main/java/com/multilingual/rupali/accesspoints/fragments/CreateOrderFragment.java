package com.multilingual.rupali.accesspoints.fragments;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Constants.BundleArg;
import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.Constants.StringConstants;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.adapters.AccessPointsAdapter;
import com.multilingual.rupali.accesspoints.api.AccessPointAPI;
import com.multilingual.rupali.accesspoints.api.OrderApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.config.APIClient2;
import com.multilingual.rupali.accesspoints.models.AccessPointAddress;
import com.multilingual.rupali.accesspoints.models.AcessPointDetail;
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.models.Size;
import com.multilingual.rupali.accesspoints.response.AccessPointsResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateOrderFragment extends Fragment {
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
    ArrayAdapter<CharSequence> categoryAdapter;
    ArrayAdapter<CharSequence> prodIdAdapter;
    ArrayAdapter<CharSequence> delModeAdapter;
    ArrayAdapter<CharSequence> payStatAdapter;
    String delMode,paymentStatus,oDate,delDate;
    Size size;
    Integer price,categoryId,productId,weight;
    SharedPreferences sharedPreferences;
    Retrofit retrofit, retrofit2;
    ProgressListener mCallback;
    String range;
    Address address;
    AcessPointDetail accessPointDetail;
    RecyclerView mRecyclerView;
    AccessPointsAdapter adapter;
    TextView toolbarTextView;
    ProgressBar progressBar;
    ConstraintLayout accessContent;
    ArrayList<AcessPointDetail> accessArrayList;
    int fetchAccessType= BundleArg.ACCESS_POINTS;

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
        categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);
        productIdSpinner=view.findViewById(R.id.productid_spinner);
        delModeSpinner=view.findViewById(R.id.delivery_mode_spinner);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.access_point_recycler_view);
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
        retrofit2= APIClient2.getClient();

        //Listeners
        createOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createOrder(view);
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

        address=new Address(hno,street,state,city,country,uname,landmark,pinn,contactNo);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        adapter=new AccessPointsAdapter(getContext(), accessArrayList, new AccessPointsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

        }, new AccessPointsAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                View view = mRecyclerView.findViewHolderForAdapterPosition(position).itemView;
                // accessPointDetail = accessArrayList.get(position);
                accessPointDetail= new AcessPointDetail(accessArrayList.get(position).getAddress());

                //address.se

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        mRecyclerView.setAdapter(adapter);
        delModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==2){
                    showRangeDialogBox();
                }
                else{
                    accessPointDetail = new AcessPointDetail();
                }

            }//);
        //}
           // }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        return view;
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
                         range = input.getText().toString();

                        accessArrayList=new ArrayList<>();

                        mCallback.showProgress();
                         fetchAccessPoint();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("range", range);
//                        // set Fragmentclass Arguments
//                        AccessPointFragment fragobj = new AccessPointFragment();
//                        FragmentManager manager = CreateOrderFragment.this.getActivity().getFragmentManager();
//                        fragobj.setArguments(bundle);
//                        FragmentTransaction transaction = manager.beginTransaction();
//                        transaction.replace(R.id.container_main, fragobj, "Frag");
//                        transaction.commit();


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
                    Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getContext(),accessPointDetail.getAddress(), Toast.LENGTH_LONG).show();
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
                    Order order=response.body();
                    Toast.makeText(getContext(),"Order Created Successfully. ", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"Order creted Success: Body: "+response.body()+" id: "+order.get_id());
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
        size.setLength(Integer.parseInt(lengthET.getText().toString()));
        size.setWidth(Integer.parseInt(widthET.getText().toString()));
        size.setHeight(Integer.parseInt(heightET.getText().toString()));
        price=Integer.parseInt(priceET.getText().toString());
        productId=Integer.parseInt(productIdSpinner.getSelectedItem().toString());
        weight=Integer.parseInt(weightET.getText().toString());
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
                delMode.equals(StringConstants.DeliveryMode.ACCESS_PTS))){
            Toast.makeText(getContext(),"Payment status should be paid for store delivery and access points." +
                    "!!",Toast.LENGTH_SHORT).show();
            return  false;
        }
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        oDate = df.format(date);
        return  true;
    }


}
