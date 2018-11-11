package com.co.ozan.callover.Views;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.co.ozan.callover.Models.Call;
import com.co.ozan.callover.Models.Contact;
import com.co.ozan.callover.R;
import com.co.ozan.callover.Utils.Adapters.CallsAdapter;
import com.co.ozan.callover.Utils.Adapters.ContactsAdapter;
import com.co.ozan.callover.Utils.Functions.GeneralFunctions;
import com.co.ozan.callover.Utils.Services.ConnectionService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactsAdapter.OnItemClickListener, GeneralFunctions.OnContactStatusChanged, ContactsAdapter.OnItemLongClickListener, CallsAdapter.OnCallItemClickListener, CallsAdapter.OnCallItemLongClickListener {

    private LinearLayout mainLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mCallsAdapter;
    private ArrayList<Contact> allContacts;
    private ArrayList<Call> allCalls;
    private TextView emptyList;
    private TelecomManager tm;
    private int PERMISSION_ALL = 1;
    private boolean isContactsVisible = true;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.MANAGE_OWN_CALLS,
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.READ_PHONE_STATE
    };

    FloatingActionButton fab, fabHistory;
    GeneralFunctions generalFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tm = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);

        //UI components
        mainLayout = findViewById(R.id.mainLayout);
        allContacts = new ArrayList<>();
        allCalls = new ArrayList<>();
        generalFunctions = new GeneralFunctions(MainActivity.this, this);
        emptyList = (TextView) findViewById(R.id.empty_view);

        //Fab. to create new contact
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Show Create Contact Dialog
                generalFunctions.showContactCreationDialog();
            }
        });

        fabHistory = findViewById(R.id.fabHistory);
        fabHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContactsVisible) {

                    isContactsVisible = false;
                    fab.hide();
                    fabHistory.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_contacts));
                    updateContactsListWithHistory();

                } else {

                    updateContactsListWithContacts();
                    isContactsVisible = true;
                    fab.show();
                    fabHistory.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_history));

                }
            }
        });
        //Creating recylerview and adapter for contacts
        mRecyclerView = (RecyclerView) findViewById(R.id.mainListView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //Check do we have permission to fetch contacts
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        else
        {
            updateContactsListWithContacts();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    updateContactsListWithContacts();
                }
                break;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void updateContactsList() {
        allContacts.clear();
        allContacts.addAll(generalFunctions.getUserContacts());
        mAdapter.notifyDataSetChanged();
        if (generalFunctions.getUserContacts().size() < 1) {
            if (emptyList.getVisibility() == View.GONE) {
                emptyList.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            if (emptyList.getVisibility() == View.VISIBLE) {
                emptyList.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateCallsList() {
        //NO NEED NOW, maybe in future
        allCalls.clear();
        allCalls.addAll(generalFunctions.getContactHistory());
        mCallsAdapter.notifyDataSetChanged();
        if (generalFunctions.getContactHistory().size() < 1) {
            if (emptyList.getVisibility() == View.GONE) {
                emptyList.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            if (emptyList.getVisibility() == View.VISIBLE) {
                emptyList.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateContactsListWithContacts() {

        mRecyclerView.setAdapter(null);
        allContacts.clear();
        allContacts.addAll(generalFunctions.getUserContacts());
        mAdapter = new ContactsAdapter(allContacts, MainActivity.this, MainActivity.this, MainActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        if (generalFunctions.getUserContacts().size() < 1) {
            if (emptyList.getVisibility() == View.GONE) {
                emptyList.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            if (emptyList.getVisibility() == View.VISIBLE) {
                emptyList.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateContactsListWithHistory() {

        mRecyclerView.setAdapter(null);
        allCalls.clear();
        allCalls.addAll(generalFunctions.getContactHistory());
        mCallsAdapter = new CallsAdapter(allCalls, MainActivity.this, MainActivity.this, MainActivity.this);
        mRecyclerView.setAdapter(mCallsAdapter);
        mCallsAdapter.notifyDataSetChanged();

        if (generalFunctions.getContactHistory().size() < 1) {
            if (emptyList.getVisibility() == View.GONE) {
                emptyList.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else {
            if (emptyList.getVisibility() == View.VISIBLE) {
                emptyList.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onContactStatusChanged(String msg) {
        if (mainLayout != null) {
            updateContactsList();
            Snackbar.make(mainLayout, msg, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(Contact item) {
        Toast.makeText(MainActivity.this, "Calling: " + item.getContactNumber(), Toast.LENGTH_SHORT).show();
        if(checkAccountConnection(MainActivity.this)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
                        new ComponentName(this.getApplicationContext(), ConnectionService.class),
                        "call");
                PhoneAccount phoneAccount = PhoneAccount.builder(phoneAccountHandle, "call").setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER).build();
                tm.registerPhoneAccount(phoneAccount);
                Bundle extras = new Bundle();
                Uri uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, item.getContactNumber(), null);
                extras.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, uri);
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
                tm.addNewIncomingCall(phoneAccountHandle, extras);

            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "This PhoneAccountHandle is not enabled for this user!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemLongClick(Contact item) {
        generalFunctions.showContactOptionsDialog(item);
    }

    @Override
    public void onCallItemClick(Contact item, String duration) {
        Toast.makeText(MainActivity.this, "Calling: " + item.getContactNumber(), Toast.LENGTH_LONG).show();

        if(checkAccountConnection(MainActivity.this)) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                PhoneAccountHandle phoneAccountHandle = new PhoneAccountHandle(
                        new ComponentName(this.getApplicationContext(), ConnectionService.class),
                        "call");
                PhoneAccount phoneAccount = PhoneAccount.builder(phoneAccountHandle, "call").setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER).build();
                tm.registerPhoneAccount(phoneAccount);
                Bundle extras = new Bundle();
                Uri uri = Uri.fromParts(PhoneAccount.SCHEME_TEL, item.getContactNumber(), null);
                extras.putParcelable(TelecomManager.EXTRA_OUTGOING_CALL_EXTRAS, uri);
                extras.putParcelable(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandle);
                tm.addNewIncomingCall(phoneAccountHandle, extras);

            }
        }
        else
        {
            Toast.makeText(MainActivity.this, "This PhoneAccountHandle is not enabled for this user!", Toast.LENGTH_LONG).show();
        }
    }
    private boolean checkAccountConnection(Context context) {
        boolean isConnected = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                final List<PhoneAccountHandle> enabledAccounts = tm.getCallCapablePhoneAccounts();
                for (PhoneAccountHandle account : enabledAccounts) {
                    if (account.getComponentName().getClassName().equals(ConnectionService.class.getCanonicalName())) {
                        isConnected = true;
                        break;
                    }
                }
            }

        }
        return isConnected;
    }
    @Override
    public void onCallItemLongClick(Contact item, String duration) {
        //do something if user long click to duration log
    }
}
