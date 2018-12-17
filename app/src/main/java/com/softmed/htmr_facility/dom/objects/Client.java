package com.softmed.htmr_facility.dom.objects;

import java.io.Serializable;

/**
 * Created by issy on 11/17/17.
 */

public class Client implements Serializable{

    private String clientName;

    public Client(){
    }

    public Client(String mName){
        clientName = mName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
