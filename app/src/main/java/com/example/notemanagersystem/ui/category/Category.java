package com.example.notemanagersystem.ui.category;

public class Category {

    private String name = "";
    private String date = "";

    public Category()
    {

    }
    public Category(String name)
    {
        this.name = name;
    }
    public Category(String name, String date)
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