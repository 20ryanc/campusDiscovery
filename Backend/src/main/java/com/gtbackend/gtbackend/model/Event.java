package com.gtbackend.gtbackend.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "event_table")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String title;

    @NotBlank
    private String email;
    @Lob
    private String description;
    private String location;
    private String time;
    private int capacity;

    private boolean inviteOnly;

    public Event(){

    }

    public Event(String title, String email, String description, String location, String time) {
        this.title = title;
        this.email = email;
        this.description = description;
        this.location = location;
        this.time = time;
        this.inviteOnly = false; //default invite only status
        this.capacity = 10; //default capacity
    }

    public Event(String title, String email, String description, String location, String time, boolean inviteOnly, int capacity) {
        this.title = title;
        this.email = email;
        this.description = description;
        this.location = location;
        this.time = time;
        this.inviteOnly = inviteOnly;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isInviteOnly() {
        return inviteOnly;
    }

    public void setInviteOnly(boolean inviteOnly) {
        this.inviteOnly = inviteOnly;
    }
}
