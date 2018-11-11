package com.co.ozan.callover.Utils.Holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.co.ozan.callover.R;

public class ContactHolder extends RecyclerView.ViewHolder {

    private TextView contactNameTextView;
    private TextView contactNumberTextView;
    private ImageView contactAvatarImageView;
    private ImageView contactCallorMessageImageView;

    public ContactHolder(@NonNull View itemView) {
        super(itemView);
        contactNameTextView = (TextView) itemView.findViewById(R.id.contactNameTextView);
        contactNumberTextView = (TextView) itemView.findViewById(R.id.contactNumberTextView);
        contactAvatarImageView = (ImageView) itemView.findViewById(R.id.contactImageView);
        contactCallorMessageImageView = (ImageView) itemView.findViewById(R.id.ContactCallImageView);
    }

    public TextView getContactNameTextView() {
        return contactNameTextView;
    }

    public void setContactNameTextView(TextView contactNameTextView) {
        this.contactNameTextView = contactNameTextView;
    }

    public TextView getContactNumberTextView() {
        return contactNumberTextView;
    }

    public void setContactNumberTextView(TextView contactNumberTextView) {
        this.contactNumberTextView = contactNumberTextView;
    }

    public ImageView getContactAvatarImageView() {
        return contactAvatarImageView;
    }

    public void setContactAvatarImageView(ImageView contactAvatarImageView) {
        this.contactAvatarImageView = contactAvatarImageView;
    }

    public ImageView getContactCallorMessageImageView() {
        return contactCallorMessageImageView;
    }

    public void setContactCallorMessageImageView(ImageView contactCallorMessageImageView) {
        this.contactCallorMessageImageView = contactCallorMessageImageView;
    }
}
