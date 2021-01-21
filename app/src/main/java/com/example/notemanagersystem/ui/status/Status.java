package com.example.notemanagersystem.ui.status;

public class Status {

    private String name = "";
    private String createDate = "";
    private String userId = "";

    public Status()
    {

    }
    public Status(String name, String createDate)
    {
        this.name = name;
        this.createDate = createDate;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return (name);
    }

    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }
    public String getCreateDate()
    {
        return (createDate);
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }
    public String getUserId()
    {
        return (userId);
    }

    @Override
    public String toString()
    {
        return(getName());
    }
}