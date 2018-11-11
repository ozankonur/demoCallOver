package com.co.ozan.callover.Models;

public class Call {
    Contact calledContact;
    String callTime;

    public Call(Contact calledContact, String callTime) {
        this.calledContact = calledContact;
        this.callTime = callTime;
    }

    public Contact getCalledContact() {
        return calledContact;
    }

    public void setCalledContact(Contact calledContact) {
        this.calledContact = calledContact;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }
}
