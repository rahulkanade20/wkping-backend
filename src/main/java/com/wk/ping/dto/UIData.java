package com.wk.ping.dto;

import java.time.LocalDateTime;

public class UIData {
    private String teamName;

    private LocalDateTime lastUpdatedTime;

    private String status;

    private String name_of_url;

    private String url;

    private int status_code;

    public UIData(String teamName, String name_of_url, String url, String status, int status_code, LocalDateTime lastUpdatedTime) {
        this.teamName = teamName;
        this.lastUpdatedTime = lastUpdatedTime;
        this.status = status;
        this.name_of_url = name_of_url;
        this.url = url;
        this.status_code = status_code;
    }

    public UIData() {

    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public LocalDateTime getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(LocalDateTime lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName_of_url() {
        return name_of_url;
    }

    public void setName_of_url(String name_of_url) {
        this.name_of_url = name_of_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
