package com.multilingual.rupali.accesspoints.fragments;


import android.content.Context;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Activities.OrderEditActivity;
import com.multilingual.rupali.accesspoints.Activities.TargetCustomersActivity;
import com.multilingual.rupali.accesspoints.Constants.BundleArg;
import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.adapters.OrderRecyclerAdapter;
import com.multilingual.rupali.accesspoints.adapters.TargetcustomerAdapter;
import com.multilingual.rupali.accesspoints.api.OrderApi;
import com.multilingual.rupali.accesspoints.api.UserApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.models.User;
import com.multilingual.rupali.accesspoints.response.OrderResponse;
import com.multilingual.rupali.accesspoints.response.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {
    RecyclerView orderRecyclerView;
    TextView toolbarTextView;
    ProgressBar progressBar;
    ConstraintLayout orderContent;
    ArrayList<Order> orderArrayList;
    OrderRecyclerAdapter orderRecyclerAdapter;
    SharedPreferences sharedPreferences;
    ProgressListener mCallback;
    Retrofit retrofit;
    int fetchOrdersType=BundleArg.MY_ORDERS;
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
    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_orders, container, false);
        orderRecyclerView=view.findViewById(R.id.orders_recycler_view);
        orderContent=view.findViewById(R.id.orders_fragment);
        orderArrayList=new ArrayList<>();
        orderRecyclerAdapter=new OrderRecyclerAdapter(getContext(), orderArrayList, new OrderRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemCilck(int position) {

            }
        }, new OrderRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongCilck(int position) {
                onOrderLongClick(position);
            }
        });
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        orderRecyclerView.setAdapter(orderRecyclerAdapter);
        sharedPreferences=getActivity().getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
        retrofit= APIClient.getClient();
        return  view;
    }

    @Override
    public void onStart() {
        Bundle bundle = this.getArguments();
        fetchOrdersType=bundle.getInt(BundleArg.FETCH_ORDERS_TYPE);
        if(fetchOrdersType==BundleArg.ALL_ORDERS){
            fetchAllOrders();
        }else{
            fetchMyOrders();
        }
        super.onStart();
    }

    private void onOrderLongClick(int position) {
        if(fetchOrdersType==BundleArg.ALL_ORDERS){
            Toast.makeText(getContext(),"You can only edit your orders. Edit orders in my orders section.",Toast.LENGTH_SHORT).show();
            return;
        }
        String orderId=orderArrayList.get(position).get_id();
        String status=orderArrayList.get(position).getStatus();
        if(orderId.isEmpty()){
            Toast.makeText(getContext(),"Order id is required to edit the order.",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(getContext(), OrderEditActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString(BundleArg.ORDER_ID,orderId);
        bundle.putString(BundleArg.ORDER_STATUS,status);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void fetchAllOrders() {
        mCallback.showProgress();
        OrderApi orderApi=retrofit.create(OrderApi.class);
        Call<OrderResponse> orderResponseCall=orderApi.getOrders();
        orderResponseCall.enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if(response.isSuccessful()) {
                    mCallback.hideProgress();
                    OrderResponse orderResponse=response.body();
                    ArrayList<Order> orders=orderResponse.orders;
                    Log.d(Tag.MY_TAG,orders+"");
                    orderArrayList.clear();
                    orderArrayList.addAll(orders);
                    orderRecyclerAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(),"Fetched sucessfully.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG,"All Orders success: Body: "+response.body()+"");
                    mCallback.hideProgress();
                }else{
                    mCallback.hideProgress();
                    Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                    Log.d(Tag.MY_TAG, "all orders fetch failed. Code: "+response.code());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                mCallback.hideProgress();
                Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                Log.d(Tag.MY_TAG, "all orders get request submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
                        t.getLocalizedMessage()+"Ccause: "+t.getCause());           }
        });
    }

    private void fetchMyOrders() {
        mCallback.showProgress();
        final String xAuth=sharedPreferences.getString(LoginSharedPref.USER_TOKEN,"");
        OrderApi orderApi=retrofit.create(OrderApi.class);
        Call<OrderResponse> orderResponseCall=orderApi.getMyOrders(xAuth);
       orderResponseCall.enqueue(new Callback<OrderResponse>() {
           @Override
           public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
               if(response.isSuccessful()) {
                   mCallback.hideProgress();
                   OrderResponse orderResponse=response.body();
                   ArrayList<Order> orders=orderResponse.orders;
                   orderArrayList.clear();
                   orderArrayList.addAll(orders);
                   orderRecyclerAdapter.notifyDataSetChanged();
                   Toast.makeText(getContext(),"Fetched sucessfully.", Toast.LENGTH_SHORT).show();
                   Log.d(Tag.MY_TAG,"My Orders success: Body: "+response.body()+"");
               }else{
                   mCallback.hideProgress();
                   Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
                   Log.d(Tag.MY_TAG, "my orders fetch failed. Code: "+response.code());
               }
           }

           @Override
           public void onFailure(Call<OrderResponse> call, Throwable t) {
               mCallback.hideProgress();
               Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
               Log.d(Tag.MY_TAG, "my orders get request submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
                       t.getLocalizedMessage()+"Ccause: "+t.getCause());           }
       });
    }

}
