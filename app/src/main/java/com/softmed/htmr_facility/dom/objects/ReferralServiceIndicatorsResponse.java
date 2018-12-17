package com.softmed.htmr_facility.dom.objects;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by issy on 19/01/2018.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class ReferralServiceIndicatorsResponse implements Serializable {

    @SerializedName("serviceId")
    private long serviceId;

    @SerializedName("serviceName")
    private String serviceName;

    @SerializedName("category")
    private String category;

    @SerializedName("isActive")
    private boolean isActive;

    @SerializedName("indicators")
    private List<ReferralIndicator> indicators;

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ReferralIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<ReferralIndicator> indicators) {
        this.indicators = indicators;
    }

}
