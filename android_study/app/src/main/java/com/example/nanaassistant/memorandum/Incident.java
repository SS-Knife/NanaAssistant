package com.example.nanaassistant.memorandum;

public class Incident {
    private String remindtime;
    private String settime;
    private String duration;
    private String title;
    private String detail;
    private Boolean isimportant;
    private Boolean isremind;

    public Incident(){

    }

    public Incident(String remindtime, String settime, String duration, String title, String detail, Boolean isimportant, Boolean isremind) {
        this.remindtime = remindtime;
        this.settime = settime;
        this.duration = duration;
        this.title = title;
        this.detail = detail;
        this.isimportant = isimportant;
        this.isremind = isremind;
    }

    public Boolean getIsremind() {
        return isremind;
    }

    public String getRemindtime() {
        return remindtime;
    }

    public String getSettime() {
        return settime;
    }

    public String getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public Boolean getIsimportant() {
        return isimportant;
    }

    public void setRemindtime(String remindtime) {
        this.remindtime = remindtime;
    }

    public void setSettime(String settime) {
        this.settime = settime;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setIsimportant(Boolean isimportant) {
        this.isimportant = isimportant;
    }

    public void setIsremind(Boolean isremind) {
        this.isremind = isremind;
    }
}
