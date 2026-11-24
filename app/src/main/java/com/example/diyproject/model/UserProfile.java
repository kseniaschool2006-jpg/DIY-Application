package com.example.diyproject.model;

public class UserProfile {
    private  int id;
    private  String name;
    private  String username;
    private  int avatarResId;
    private int followersCount;
    private  int followingCount;
    private boolean subscribed;

    public UserProfile(int id, String name, String username, int avatarResId, int followersCount, int followingCount, boolean subscribed) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.avatarResId = avatarResId;
        this.followersCount = followersCount;
        this.followingCount = followingCount;
        this.subscribed = subscribed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAvatarResId() {
        return avatarResId;
    }

    public void setAvatarResId(int avatarResId) {
        this.avatarResId = avatarResId;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }
}
