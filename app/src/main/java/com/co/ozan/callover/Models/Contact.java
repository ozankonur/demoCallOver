package com.co.ozan.callover.Models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class Contact {
    String contactName;
    String contactNumber;
    Drawable contactAvatar;

    public Contact(String contactName, String contactNumber, Drawable contactAvatar) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this.contactAvatar = contactAvatar;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Drawable getContactAvatar() {
        return contactAvatar;
    }

    public void setContactAvatar(Drawable contactAvatar) {
        this.contactAvatar = contactAvatar;
    }
}
