package com.co.ozan.callover.Utils.Holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.co.ozan.callover.R;

public class CallHolder extends RecyclerView.ViewHolder {

    private TextView contactNameTextView;
    private TextView contactNumberTextView;
    private ImageView contactAvatarImageView;
    private TextView callDuration;

    public CallHolder(@NonNull View itemView) {
        super(itemView);
        contactNameTextView = (TextView) itemView.findViewById(R.id.contactNameTextView);
        contactNumberTextView = (TextView) itemView.findViewById(R.id.contactNumberTextView);
        contactAvatarImageView = (ImageView) itemView.findViewById(R.id.contactImageView);
        callDuration = (TextView) itemView.findViewById(R.id.callDuration);
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

    public TextView getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(TextView callDuration) {
        this.callDuration = callDuration;
    }
}
