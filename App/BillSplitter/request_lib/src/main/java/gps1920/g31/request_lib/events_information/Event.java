package gps1920.g31.request_lib.events_information;

import java.util.ArrayList;
import java.io.Serializable;

public class Event implements Serializable
{
    private static final long serialVersionUID = 1234567891011121314L;

    private String name;
    private User administrator;
    private ArrayList<User> participants;
    private ArrayList<Expense> expenses;

    private boolean published = false;

    public Event(String name, User administrator, ArrayList<User> participants, ArrayList<Expense> expenses)
    {
        this.name = name;
        this.administrator = administrator;
        this.participants = participants;
        this.expenses = expenses;
    }

    public Event(Event event)
    {
        this.name = event.name;
        this.administrator = new User(event.administrator);
        for(int i = 0; i < event.participants.size(); i++)
        {
            this.participants.add(new User(event.participants.get(i)));
        }
        this.expenses = event.expenses;
    }

    public String getName()
    {
        return name;
    }

    public boolean isPublished()
    {
        return published;
    }

    public User getAdministrator()
    {
        return administrator;
    }

    public ArrayList<User> getParticipants()
    {
        return participants;
    }

    public ArrayList<Expense> getExpenses()
    {
        return expenses;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setExpenses(ArrayList<Expense> expenses)
    {
        this.expenses = expenses;
    }

    public void setParticipants(ArrayList<User> participants)
    {
        this.participants = participants;
    }

    public void setEventAsPublished()
    {
        this.published = true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == null) { return false; }

        if(obj instanceof Event == false) { return false; }

        Event event = (Event)obj;

        if(this.name.equals(event.getName())) { return true; }

        return false;
    }
}