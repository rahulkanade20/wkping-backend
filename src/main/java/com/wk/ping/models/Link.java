package com.wk.ping.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="links")
public class Link {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="team_id")
    private long team_id;

    @Column(name="name_for_link")
    private String linkName;

    @Column(name="link")
    private String link;

    @Column(name="status_code")
    private int status_code;

    @Column(name="last_ping_time")
    private LocalDateTime lastPingTime;

    public Link(long id, long team_id, String linkName, String link, int status_code, LocalDateTime localDateTime) {
        this.id = id;
        this.team_id = team_id;
        this.linkName = linkName;
        this.link = link;
        this.status_code = status_code;
        this.lastPingTime = localDateTime;
    }

    public Link() {

    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", team_id=" + team_id +
                ", linkName='" + linkName + '\'' +
                ", link='" + link + '\'' +
                ", status_code=" + status_code +
                ", localDateTime=" + lastPingTime +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(long team_id) {
        this.team_id = team_id;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public LocalDateTime getLastPingTime() {
        return lastPingTime;
    }

    public void setLastPingTime(LocalDateTime lastPingTime) {
        this.lastPingTime = lastPingTime;
    }
}
