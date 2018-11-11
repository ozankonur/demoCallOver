package com.co.ozan.callover.Utils.Functions;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.co.ozan.callover.Models.Call;
import com.co.ozan.callover.Models.Contact;
import com.co.ozan.callover.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class GeneralFunctions {

    Context context;

    private int PERMISSION_ = 1;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_CALL_LOG
    };


    public ArrayList<Call> getContactHistory() {
        ArrayList<Call> callsHistory = new ArrayList<Call>();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, PERMISSION_);
        }
        Cursor callLog = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI, null, null, null,
                android.provider.CallLog.Calls.DATE + " DESC");

        int cid = callLog.getColumnIndex(CallLog.Calls._ID);
        int cName = callLog.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int cNumber = callLog.getColumnIndex(CallLog.Calls.NUMBER);
        int cDuration = callLog.getColumnIndex(CallLog.Calls.DURATION);

        // looping call log cursor object
        while (callLog.moveToNext()) {

            String mId = callLog.getString(cid);
            String mName = callLog.getString(cName);
            String mNumber = callLog.getString(cNumber);
            String mDuration = callLog.getString(cDuration);
            callsHistory.add(new Call(new Contact(mName,mNumber,context.getResources().getDrawable(android.R.drawable.sym_def_app_icon)),formatSecond(mDuration)));
        }
        return callsHistory;
    }

    public interface OnContactStatusChanged {
        void onContactStatusChanged(String msg);
    }

    private OnContactStatusChanged contactStatusListener;

    public GeneralFunctions(Context context, OnContactStatusChanged contactStatusListener) {
        this.context = context;
        this.contactStatusListener = contactStatusListener;
    }

    public ArrayList<Contact> getUserContacts() {

        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            contacts.add(new Contact(name, phoneNumber, context.getResources().getDrawable(android.R.drawable.sym_def_app_icon)));
        }
        phones.close();
        return contacts;
    }


    public void showContactOptionsDialog(final Contact contact) {
        final Dialog dialogOptions = new Dialog(context);
        dialogOptions.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOptions.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogOptions.setCancelable(true);
        dialogOptions.setContentView(R.layout.contact_options_dialog);

        final TextView contactEditText = (TextView) dialogOptions.findViewById(R.id.textViewContactEdit);
        contactEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactEditDialog(contact);
                dialogOptions.dismiss();
            }
        });
        final TextView contactNumberText = (TextView) dialogOptions.findViewById(R.id.textViewContactDelete);
        contactNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure to delete " + contact.getContactName() + " ?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteContact(contact.getContactNumber(), contact.getContactName());
                            contactStatusListener.onContactStatusChanged("Contact " + contact.getContactName() + " successfully deleted.");
                            dialog.dismiss();
                            dialogOptions.dismiss();
                        } catch (Exception e) {
                        }

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });
        dialogOptions.show();
    }

    public void showContactEditDialog(final Contact contact) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.edit_contact_dialog);

        final EditText contactNameText = (EditText) dialog.findViewById(R.id.editTextName);
        contactNameText.setText(contact.getContactName());
        final EditText contactNumberText = (EditText) dialog.findViewById(R.id.editTextNumber);
        contactNumberText.setText(contact.getContactNumber());

        final TextView textViewCancelContactDialog = (TextView) dialog.findViewById(R.id.textViewCancelContactDialog);
        textViewCancelContactDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        FloatingActionButton fabCreateContact = (FloatingActionButton) dialog.findViewById(R.id.fabCreateContact);
        fabCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputOk(contactNameText) && isInputOk(contactNumberText)) {
                    try {
                        deleteContact(contact.getContactNumber(), contact.getContactName());
                        createContact(contactNameText.getText().toString(), contactNumberText.getText().toString());
                        contactStatusListener.onContactStatusChanged("Contact " + contactNameText.getText().toString() + " successfully edited.");
                        dialog.dismiss();
                    } catch (Exception e) {
                        Snackbar.make(v, "Could not edit contact", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "Wrong Name or Number", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    public void showContactCreationDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.create_contact_dialog);

        final EditText contactNameText = (EditText) dialog.findViewById(R.id.editTextName);
        final EditText contactNumberText = (EditText) dialog.findViewById(R.id.editTextNumber);
        final TextView textViewCancelContactDialog = (TextView) dialog.findViewById(R.id.textViewCancelContactDialog);
        textViewCancelContactDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        FloatingActionButton fabCreateContact = (FloatingActionButton) dialog.findViewById(R.id.fabCreateContact);
        fabCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputOk(contactNameText) && isInputOk(contactNumberText)) {
                    try {
                        createContact(contactNameText.getText().toString(), contactNumberText.getText().toString());
                        contactStatusListener.onContactStatusChanged("Contact " + contactNameText.getText().toString() + " successfully created.");
                        dialog.dismiss();
                    } catch (Exception e) {
                        Snackbar.make(v, "Could not create contact", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(v, "Wrong Name or Number", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        dialog.show();
    }

    private void createContact(String DisplayName, String MobileNumber) throws Exception {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //Name
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //create a new contact
        context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
    }

    public boolean deleteContact(String phone, String name) throws Exception {
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        Cursor cur = context.getContentResolver().query(contactUri, null, null, null, null);
        if (cur.moveToFirst()) {
            do {
                if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equalsIgnoreCase(name)) {
                    String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                    context.getContentResolver().delete(uri, null, null);
                    return true;
                }

            } while (cur.moveToNext());
        }
        return false;
    }

    private boolean isInputOk(EditText editText) {
        if (editText != null) {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private String formatSecond(String sec)
    {
        Date d = new Date(Integer.parseInt(sec) * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }
}
