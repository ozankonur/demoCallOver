package com.co.ozan.callover.Utils.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.util.Log;
import android.widget.Toast;

public class ConnectionService extends android.telecom.ConnectionService {

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {

        Log.d("COVER","Outgoing Created");

        return super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request);
    }

    @Override
    public void onCreateOutgoingConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.d("COVER","Outgoing Failed");

        super.onCreateOutgoingConnectionFailed(connectionManagerPhoneAccount, request);
    }

    @Override
    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.d("COVER","Incoming Created");
        ConnectionOver connectionOver = new ConnectionOver();
        connectionOver.setInitializing();
        return connectionOver;
    }

    @Override
    public void onCreateIncomingConnectionFailed(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Log.d("COVER","Incoming failed");

        super.onCreateIncomingConnectionFailed(connectionManagerPhoneAccount, request);
    }

    public class ConnectionOver extends Connection
    {
        public ConnectionOver() {
            setConnectionProperties(PROPERTY_SELF_MANAGED);
            setAudioModeIsVoip(true);
        }

        @Override
        public void onShowIncomingCallUi() {
            super.onShowIncomingCallUi();
            Log.d("COVER","Incoming Call");
        }

        @Override
        public void onAnswer() {
            super.onAnswer();
            Log.d("COVER","Answered Call");
        }

        @Override
        public void onReject() {
            super.onReject();
            Log.d("COVER","Rejected Call");
        }

    }
}
