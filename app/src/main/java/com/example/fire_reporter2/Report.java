package com.example.fire_reporter2;

public class Report {
    String id, reporter_id, status, date_time;

    public Report() {}

    public Report(String id, String reporter_id, String status, String date_time) {
        this.id = id;
        this.reporter_id = reporter_id;
        this.status = status;
        this.date_time = date_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReporter_id() {
        return reporter_id;
    }

    public void setReporter_id(String reporter_id) {
        this.reporter_id = reporter_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
