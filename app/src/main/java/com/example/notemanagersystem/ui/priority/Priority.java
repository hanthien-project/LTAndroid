package com.example.notemanagersystem.ui.priority;


public class Priority {

    private String name = "";
    private String date = "";

    public Priority()
    {

    }
    public Priority(String name)
    {
        this.name = name;
    }
    public Priority(String name, String date)
    {
        this.name = name;
        this.date = date;
    }


    public void setName(String name)
    {
        this.name = name;
    }
    public String getName()
    {
        return (name);
    }


    public void setDate(String date)
    {
        this.date = date;
    }
    public String getDate()
    {
        return (date);
    }
    @Override
    public String toString()
    {
        return(getName());
    }
}