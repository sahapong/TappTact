package com.example.jobs.senior1;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jobs on 10-Feb-17.
 */
public class ViewResponse {

    @SerializedName("TotalView")
    private int TotalView;
    public int getTotalView() {
        return TotalView;
    }

    @SerializedName("TotalSent")
    private String TotalSent;
    public String getTotalSent() {
        return TotalSent;
    }


    @SerializedName("WeeklyView")
    private int WeeklyView;
    public int getWeeklyView() {
        return WeeklyView;
    }

    @SerializedName("WeeklySent")
    private String WeeklySent;
    public String getWeeklySent() {
        return WeeklySent;
    }
}
