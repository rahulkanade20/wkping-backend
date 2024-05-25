package com.wk.ping.dto;

public class TeamRegDTO {
    String team_name;
    String link;
    String linkName;

    public TeamRegDTO(String team_name, String link, String linkName) {
        this.team_name = team_name;
        this.link = link;
        this.linkName = linkName;
    }

    public TeamRegDTO() {

    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
}
