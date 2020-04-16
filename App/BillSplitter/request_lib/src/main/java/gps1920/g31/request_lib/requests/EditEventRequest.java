package gps1920.g31.request_lib.requests;


import java.util.ArrayList;

import gps1920.g31.request_lib.events_information.Expense;


public class EditEventRequest implements Request
{
    private String email;
    private ArrayList<String> participants;
    private ArrayList<Expense> expenses;
    private String newName;
    private String oldName;
    
    public EditEventRequest(String email, String newName, String oldName, ArrayList<String> participants, ArrayList<Expense> expenses)
    {
        this.email = email;
        this.participants = participants;
        this.expenses = expenses;
        this.newName = newName;
        this.oldName = oldName;
    }

    public String getEmail(){ return email; }
    public ArrayList<String> getParticipants(){ return participants; }
    public ArrayList<Expense> getExpenses(){ return expenses; }
    public String getNewName() { return newName; }
    public String getOldName() { return oldName; }
    
}