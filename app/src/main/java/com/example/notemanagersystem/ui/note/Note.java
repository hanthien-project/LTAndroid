package com.example.notemanagersystem.ui.note;


public class Note {

    private String name = "";
    private String category = "";
    private String priority = "";
    private String status = "";
    private String planDate = "";
    private String createDate = "";
    private String userId = "";


    public Note()
    {

    }
    public Note(String name, String category, String priority, String status, String planDate, String createDate)
    {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.planDate = planDate;
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

    public void setCategory(String category)
    {
        this.category = category;
    }
    public String getCategory()
    {
        return (category);
    }

    public void setPriority(String priority)
    {
        this.priority = priority;
    }
    public String getPriority()
    {
        return (priority);
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    public String getStatus()
    {
        return (status);
    }

    public void setPlanDate(String planDate)
    {
        this.planDate = planDate;
    }
    public String getPlanDate()
    {
        return (planDate);
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