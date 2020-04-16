package gps1920.g31.request_lib.requests;

import java.util.ArrayList;

import gps1920.g31.request_lib.events_information.Expense;


public class CreateEventRequest implements Request
{
    private String userEmail;
    private String eventName;
    private ArrayList<String> participants;
    private ArrayList<Expense> expenses;

    public CreateEventRequest(String userEmail, String eventName, ArrayList<String> participants, ArrayList<Expense> expenses)
    {
        this.userEmail = userEmail;
        this.eventName = eventName;
        this.participants = participants;
        this.expenses = expenses;
    }

    public String getUserEmail() { return userEmail; }

    public String getEventName() { return eventName; }

    public ArrayList<String> getParticipants() { return participants; }

    public ArrayList<Expense> getExpenses() { return expenses; }
    
}