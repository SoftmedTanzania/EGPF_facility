package com.softmed.htmr_facility.dom.responces;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import com.softmed.htmr_facility.dom.objects.Locations;
import com.softmed.htmr_facility.dom.objects.Team;
import com.softmed.htmr_facility.dom.objects.User;

/**
 * Created by issy on 11/23/17.
 */

public class LoginResponse implements Serializable{

    @SerializedName("locations")
    private Locations locations;

    @SerializedName("team")
    private Team team;

    @SerializedName("time")
    private Time time;

    @SerializedName("user")
    private User user;

    public LoginResponse(){}

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class Time implements Serializable{

        @SerializedName("time")
        private String time;

        @SerializedName("timeZone")
        private String timeZone;

        public Time() {}

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }
    }

}
