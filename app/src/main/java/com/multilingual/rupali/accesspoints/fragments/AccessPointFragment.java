package com.multilingual.rupali.accesspoints.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.multilingual.rupali.accesspoints.Constants.BundleArg;
import com.multilingual.rupali.accesspoints.Constants.LoginSharedPref;
import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.adapters.AccessPointsAdapter;
import com.multilingual.rupali.accesspoints.adapters.OrderRecyclerAdapter;
import com.multilingual.rupali.accesspoints.api.AccessPointAPI;
import com.multilingual.rupali.accesspoints.api.OrderApi;
import com.multilingual.rupali.accesspoints.config.APIClient;
import com.multilingual.rupali.accesspoints.config.APIClient2;
import com.multilingual.rupali.accesspoints.models.AccessPointAddress;
import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.response.AccessPointsResponse;
import com.multilingual.rupali.accesspoints.response.OrderResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class AccessPointFragment extends DialogFragment {
//    RecyclerView mRecyclerView;
//    AccessPointsAdapter adapter;
//        // this method create view for your Dialog
//    TextView toolbarTextView;
//    ProgressBar progressBar;
//    ConstraintLayout accessContent;
//    ArrayList<AccessPointAddress> accessArrayList;
//    SharedPreferences sharedPreferences;
//    AccessPointFragment.ProgressListener mCallback;
//    Retrofit retrofit;
//    String range;
//    int fetchAccessType= BundleArg.ACCESS_POINTS;
//    public interface ProgressListener{
//        void showProgress();
//        void hideProgress();
//    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        try {
////            mCallback=(AccessPointFragment.ProgressListener)context;
////        }catch(ClassCastException e){
////            throw new ClassCastException(context.toString()+ "must implement ProgressListener");
////        }
//    }
//    public AccessPointFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//             range = getArguments().getString("range");
//        //inflate layout with recycler view
//            View v = inflater.inflate(R.layout.fragment_access_point_list, container, false);
//            mRecyclerView = (RecyclerView) v.findViewById(R.id.access_point_recycler_view);
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            accessArrayList=new ArrayList<>();
//            adapter=new AccessPointsAdapter(getContext(), accessArrayList, new AccessPointsAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//
//                }
//
//        }, new AccessPointsAdapter.OnItemLongClickListener() {
//                @Override
//                public void onItemLongClick(int position) {
//
//                }
//        });
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
//        mRecyclerView.setAdapter(adapter);
//        sharedPreferences=getActivity().getSharedPreferences(LoginSharedPref.SHARED_PREF_NAME,MODE_PRIVATE);
//        retrofit= APIClient2.getClient();
//        return  v;
//        }
//    @Override
//    public void onStart() {
//        Bundle bundle = this.getArguments();
//
//        fetchAccessPoint();
//        super.onStart();
//    }
//
//    private void fetchAccessPoint() {
//      //  mCallback.showProgress();
//        String street=sharedPreferences.getString(LoginSharedPref.USER_STREET,"");
//        String landmaark = sharedPreferences.getString(LoginSharedPref.USER_LANDMARK,"");
//        String city=sharedPreferences.getString(LoginSharedPref.USER_CITY,"");
//        String state=sharedPreferences.getString(LoginSharedPref.USER_STATE,"");
//        String country=sharedPreferences.getString(LoginSharedPref.USER_COUNTRY,"");
//        String final_street = street+" "+landmaark;
//        AccessPointAddress address = new AccessPointAddress(final_street, city, state, country, range);
//        AccessPointAPI accessPointAPI=retrofit.create(AccessPointAPI.class);
//        Call<AccessPointsResponse> accessResponseCall=accessPointAPI.getAccessPoints(address);
//        accessResponseCall.enqueue(new Callback<AccessPointsResponse>() {
//            @Override
//            public void onResponse(Call<AccessPointsResponse> call, Response<AccessPointsResponse> response) {
//                if(response.isSuccessful()) {
//                   // mCallback.hideProgress();
//                    AccessPointsResponse accessResponse=response.body();
//                    ArrayList<AccessPointAddress> access=accessResponse.accessPoints;
//                    accessArrayList.clear();
//                    accessArrayList.addAll(access);
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(getContext(),"Fetched sucessfully.", Toast.LENGTH_SHORT).show();
//                    Log.d(Tag.MY_TAG,"My Orders success: Body: "+response.body()+"");
//                }else{
//                    //mCallback.hideProgress();
//                    Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
//                    Log.d(Tag.MY_TAG, "my orders fetch failed. Code: "+response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<AccessPointsResponse> call, Throwable t) {
//               // mCallback.hideProgress();
//                Toast.makeText(getContext(),"Please check your network connection.", Toast.LENGTH_SHORT).show();
//                Log.d(Tag.MY_TAG, " get access point request submitted to API failed. Message: " +t.getMessage()+"Local msg: "+
//                        t.getLocalizedMessage()+"Cause: "+t.getCause());           }
//        });
//    }
//

}
