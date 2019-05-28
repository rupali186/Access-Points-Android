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
import com.multilingual.rupali.accesspoints.models.Address;
import com.multilingual.rupali.accesspoints.models.Order;
import com.multilingual.rupali.accesspoints.models.User;

import java.util.ArrayList;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.ViewHolder> {
    Context context;
    ArrayList<Order> orders;
    OnItemClickListener listener;
    OnItemLongClickListener onItemLongClickListener;

    public OrderRecyclerAdapter(Context context, ArrayList<Order> orders, OrderRecyclerAdapter.OnItemClickListener listener,OnItemLongClickListener onItemLongClickListener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
        this.onItemLongClickListener=onItemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.order_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Order order =orders.get(position);
        holder.orderId.setText(order.get_id());
        String address;
        if(order.getAccess_pt_address()==null){
            address = order.getAddress().getH_no()+" "+ order.getAddress().getStreet() +" "+ order.getAddress().getCity() + " "+ order.getAddress().getState();

        }
        else{
            address = order.getAccess_pt_address().getAddress();
        }
        holder.content.setText("Order Date: "+ order.getO_date()+"\n"
                +"Delivery Date: "+order.getDel_date()+"\n"
                +"Status: "+order.getStatus()+"\n"
                +"Price: "+order.getPrice()+"\n"
                +"Weight: "+order.getWeight()+"\n"
                +"Length: "+order.getSize().getLength()+"\n"
                +"Width: "+order.getSize().getWidth()+"\n"
                +"Height: "+order.getSize().getHeight()+"\n"
                +"Category Id: "+order.getCategory_id()+"\n"
                +"Product Id: "+order.getProduct_id()+"\n"
                +"User Id: "+order.getUser_id()+"\n"
                + "Address: " + address+"\n"
        );
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Tag.MY_TAG,order.isExpanded()+"");
                if (order.isExpanded()){
                    order.setExpanded(false);
                    holder.content.setMaxLines(4);
                }
                else{
                    order.setExpanded(true);
                    holder.content.setMaxLines(Integer.MAX_VALUE);
                }

                listener.onItemCilck(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onItemLongClickListener.onItemLongCilck(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public interface OnItemClickListener{
        void onItemCilck(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongCilck(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView orderId;
        TextView content;
        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            orderId=itemView.findViewById(R.id.order_id_textview);
            content=itemView.findViewById(R.id.order_content_tv);
        }
    }
}
