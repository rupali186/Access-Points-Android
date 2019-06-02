package com.multilingual.rupali.accesspoints.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multilingual.rupali.accesspoints.constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.User;

import java.util.ArrayList;

public class TargetcustomerAdapter extends RecyclerView.Adapter<TargetcustomerAdapter.ViewHolder> {
    Context context;
    ArrayList<User> users;
    OnItemClickListener listener;

    public TargetcustomerAdapter(Context context, ArrayList<User> users, OnItemClickListener listener) {
        this.context = context;
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.target_customer_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final User user =users.get(position);
        holder.email.setText(user.getEmail());
        Address address=user.getAddress().get(0);
        holder.content.setText("Username: "+ user.getU_name()+"\n"
                +"Numorders: "+user.getNum_orders()+"\n"
                +"Account Creation Date: "+user.getAcc_creation_date()+"\n"
                +"Phone No.: "+user.getPhone_no().get(0)+"\n"
                +"Dob: "+user.getDob()+"\n"
                +"Gender: "+user.getGender()+"\n"
                +"Last Order Date: "+user.getLast_order_date()+"\n"
                +"Delivery Failure Number: "+user.getDel_failures_no()+"\n"
                +"Address: "+address.getH_no()+", "+address.getStreet()+", "+address.getLandmark()+", "+address.getCity()
                +", "+address.getState()+", "+address.getCountry()+"\n"
                +"Locker Used: "+user.getLocker_used()+"\n"
        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag.MY_TAG,user.getExpanded()+"");
                if (user.getExpanded()){
                    user.setExpanded(false);
                    holder.content.setMaxLines(4);
                }
                else{
                    user.setExpanded(true);
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                }

                listener.onItemCilck(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public interface OnItemClickListener{
        void onItemCilck(int position);
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView email;
        TextView content;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            email=itemView.findViewById(R.id.customer_username);
            content=itemView.findViewById(R.id.customer_content);
        }
    }
}
