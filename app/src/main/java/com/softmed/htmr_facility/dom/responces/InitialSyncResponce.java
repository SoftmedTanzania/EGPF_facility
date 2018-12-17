package com.softmed.htmr_facility.dom.responces;

import java.io.Serializable;
import java.util.List;

/**
 * Created by issy on 12/8/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class InitialSyncResponce implements Serializable {

    private List<ReferalResponce> initialSyncData;

    public InitialSyncResponce(){}

    public List<ReferalResponce> getInitialSyncData() {
        return initialSyncData;
    }

    public void setInitialSyncData(List<ReferalResponce> initialSyncData) {
        this.initialSyncData = initialSyncData;
    }
}
