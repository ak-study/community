package com.ak.community.model;

public class User {
    private Long id;
    private String name;
    private String account_id;
    private String token;
    private Long gmt_create;
    private Long gmt_modified;
    private String avatar_url;

    public String getAvatarUrl() {
        return avatar_url;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatar_url = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountid() {
        return account_id;
    }

    public void setAccountid(String accountid) {
        this.account_id = accountid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getGmtCreate() {
        return gmt_create;
    }

    public void setGmtCreate(Long gmtCreate) {
        this.gmt_create = gmtCreate;
    }

    public Long getGmtModified() {
        return gmt_modified;
    }

    public void setGmtModified(Long gmtModified) {
        this.gmt_modified = gmtModified;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", accountid='" + account_id + '\'' +
                ", token='" + token + '\'' +
                ", gmtCreate=" + gmt_create +
                ", gmtModified=" + gmt_modified +
                ", avatarUrl='" + avatar_url + '\'' +
                '}';
    }
}
