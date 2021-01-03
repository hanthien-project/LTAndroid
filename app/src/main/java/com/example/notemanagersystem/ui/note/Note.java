package com.example.notemanagersystem.ui.note;


public class Note {

    private String name = "";
    private String category = "";
    private String priority = "";
    private String status = "";
    private String plandate = "";
    private String createdate = "";


    public Note()
    {

    }
    public Note(String name, String category, String priority, String status, String plandate, String createdate)
    {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.plandate = plandate;
        this.createdate = createdate;
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

    public void setPlandate(String plandate)
    {
        this.plandate = plandate;
    }
    public String getPlandate()
    {
        return (plandate);
    }

    public void setCreatedate(String createdate)
    {
        this.createdate = createdate;
    }
    public String getCreatedate()
    {
        return (createdate);
    }

    @Override
    public String toString()
    {
        return(getName());
    }
}