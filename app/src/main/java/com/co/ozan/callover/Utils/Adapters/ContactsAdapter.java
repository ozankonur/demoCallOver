package com.co.ozan.callover.Utils.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.co.ozan.callover.Models.Contact;
import com.co.ozan.callover.R;
import com.co.ozan.callover.Utils.Holders.ContactHolder;

import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactHolder> {

    private ArrayList<Contact> contactArrayList;
    private Context context;
    private OnItemClickListener listener;
    private OnItemLongClickListener longPressListener;

    public interface OnItemClickListener {
        void onItemClick(Contact item);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(Contact item);
    }
    public ContactsAdapter(ArrayList<Contact> contactArrayList, Context context, OnItemClickListener listener , OnItemLongClickListener longPressListener) {
        this.contactArrayList = contactArrayList;
        this.context = context;
        this.listener = listener;
        this.longPressListener = longPressListener;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.contacts_list_item, viewGroup, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder contactHolder, final int i) {
        contactHolder.getContactNameTextView().setText(contactArrayList.get(i).getContactName());
        contactHolder.getContactNumberTextView().setText(contactArrayList.get(i).getContactNumber());
        contactHolder.getContactCallorMessageImageView().setImageResource(R.drawable.ic_message);
        contactHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(contactArrayList.get(i));
            }
        });

        contactHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longPressListener.onItemLongClick(contactArrayList.get(i));
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }
}
