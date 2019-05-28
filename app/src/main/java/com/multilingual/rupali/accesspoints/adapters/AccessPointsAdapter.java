package com.multilingual.rupali.accesspoints.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.multilingual.rupali.accesspoints.Constants.Tag;
import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.models.AccessPointAddress;
import com.multilingual.rupali.accesspoints.models.AcessPointDetail;
import com.multilingual.rupali.accesspoints.models.Order;

import java.util.ArrayList;

public class AccessPointsAdapter extends RecyclerView.Adapter<AccessPointsAdapter.ViewHolder>{
    Context context;
    ArrayList<AcessPointDetail> accessPoints;
    AccessPointsAdapter.OnItemClickListener listener;
    AccessPointsAdapter.OnItemLongClickListener onItemLongClickListener;

    public AccessPointsAdapter(Context context, ArrayList<AcessPointDetail> accessPoints, AccessPointsAdapter.OnItemClickListener listener, AccessPointsAdapter.OnItemLongClickListener onItemLongClickListener) {
        this.context = context;
        this.accessPoints = accessPoints;
        this.listener = listener;
        this.onItemLongClickListener=onItemLongClickListener;
    }

    @NonNull
    @Override
    public AccessPointsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.single_access_point,parent,false);
        AccessPointsAdapter.ViewHolder holder=new AccessPointsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AccessPointsAdapter.ViewHolder holder, final int position) {
        final AcessPointDetail accessPoint = accessPoints.get(position);
        holder.address.setText(accessPoint.getAddress());
//        holder.state.setText(accessPoint.getState());
//        holder.city.setText(accessPoint.getCity());
//        holder.country.setText(accessPoint.getCountry());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemLongClickListener.onItemLongClick(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return accessPoints.size();
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView address;
//        TextView city;
//        TextView state;
//        TextView country;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            address=itemView.findViewById(R.id.street_id_tv);
//            city=itemView.findViewById(R.id.city_id_tv);
//            state=itemView.findViewById(R.id.state_id_tv);
//            country=itemView.findViewById(R.id.country_id_tv);
        }
    }
}
