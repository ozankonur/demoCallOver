package com.co.ozan.callover.Utils.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.co.ozan.callover.Models.Call;
import com.co.ozan.callover.Models.Contact;
import com.co.ozan.callover.R;
import com.co.ozan.callover.Utils.Holders.CallHolder;
import com.co.ozan.callover.Utils.Holders.ContactHolder;

import java.util.ArrayList;

public class CallsAdapter extends RecyclerView.Adapter<CallHolder> {

    private ArrayList<Call> callArrayList;
    private Context context;
    private OnCallItemClickListener listener;
    private OnCallItemLongClickListener longPressListener;

    public interface OnCallItemClickListener {
        void onCallItemClick(Contact item,String duration);
    }
    public interface OnCallItemLongClickListener {
        void onCallItemLongClick(Contact item,String duration);
    }
    public CallsAdapter(ArrayList<Call> callArrayList, Context context, OnCallItemClickListener listener , OnCallItemLongClickListener longPressListener) {
        this.callArrayList = callArrayList;
        this.context = context;
        this.listener = listener;
        this.longPressListener = longPressListener;
    }

    @NonNull
    @Override
    public CallHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.calls_list_item, viewGroup, false);
        return new CallHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallHolder callHolder, final int i) {
        callHolder.getContactNameTextView().setText(callArrayList.get(i).getCalledContact().getContactName());
        callHolder.getContactNumberTextView().setText(callArrayList.get(i).getCalledContact().getContactNumber());
        callHolder.getCallDuration().setText(callArrayList.get(i).getCallTime());
        callHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCallItemClick(callArrayList.get(i).getCalledContact(),callArrayList.get(i).getCallTime());
            }
        });

        callHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longPressListener.onCallItemLongClick(callArrayList.get(i).getCalledContact(),callArrayList.get(i).getCallTime());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return callArrayList.size();
    }
}
