package server.event_manager;

import java.io.Serializable;
import java.util.ArrayList;

import gps.events_information.Expense;
import gps.events_information.EnumActionResult;
import gps.events_information.Event;

/**
* <h1>Event Manager</h1>
* This class encapsulates the event management logic from the
* communication module. It offers an interface to the communication
* module that matches the client interface to manipulate the data.
*
* @author  Ruben Marques
* @version 1.0
* @since   27-09-2019
*/
public class EventsManager implements Serializable
{
    private static final long serialVersionUID = -8006333088757143511L;
    
    private EventsManagerData eventsManagerData;

    /**
    * This constructor creates a new event manager that can be
    * used to manage the events logic and data.
    */
    public EventsManager()
    {
        eventsManagerData = new EventsManagerData();
    }

    /**
    * This method is used to log in an user in the system.
    *
    * @param email - User's email
    * @param password - User's password
    * @return EnumActionResult - The method returns an Enum value informing the success of the operation.
    */
    public EnumActionResult loginUser(String email, String password)
    {
        return eventsManagerData.loginUser(email, password);
    }

    /**
    * This method is used to register an user in the system.
    *
    * @param email - User's email
    * @param password - User's password
    * @param firstName - User's first name
    * @param lastName - User's last name
    * @return EnumActionResult - The method returns an Enum value informing the success of the operation.
    */
    public EnumActionResult registerUser(String email, String password, String firstName, String lastName)
    {
        return eventsManagerData.registerUser(email, password, firstName, lastName);
    }

    /**
    * This method is used to log out an user from the system.
    *
    * @param email - User's email
    * @return EnumActionResult - The method returns an Enum value informing the success of the operation.
    */
    public void logoutUser(String email)
    {
        eventsManagerData.logoutUser(email);
    }

    /**
    * This method is used to inform if an user exists in the system.
    *
    * @param email - User's email
    * @return EnumActionResult - The method returns an Enum value informing if the user exists or not.
    */
    public EnumActionResult userExists(String email)
    {
        return eventsManagerData.userExists(email);
    }

    /**
    * This method is used to create an event and add it to the database.
    *
    * @param eventName - The name of the event
    * @param creator - The email of the creator of the event
    * @param participants - A list with the emails of the participants in the event
    * @param expenses - A list with expenses the event will have in the beginning
    * @return EnumActionResult - The method returns an Enum value informing the success of the operation.
    */
    public EnumActionResult createEvent(String eventName, String creator, ArrayList<String> participants, ArrayList<Expense> expenses)
    {
        return eventsManagerData.createEvent(eventName, creator, participants, expenses);
    }

    /**
    * This method is used to edit an event in the database.
    *
    * @param eventName - The name of the event to edit
    * @param newName - The new name of the event
    * @param requester - The email of the requester of the modification
    * @param participants - The new list with the emails of the participants in the event
    * @param expenses - The new list with expenses in the event
    * @return EnumActionResult - The method returns an Enum value informing the success of the operation.
    */
    public EnumActionResult editEvent(String eventName, String newName, String requester, ArrayList<String> participants, ArrayList<Expense> expenses)
    {
        return eventsManagerData.editEvent(eventName, newName, requester, participants, expenses);
    }

    /**
    * This method is used to get the events where the user is inserted or is the creator
    *
    * @param email - The email of the user
    * @return ArrayList<Event> - The method returns a list with the events where the users is inserted or is the creator
    */
    public ArrayList<Event> getEventsFromUser(String email)
    {
        return eventsManagerData.getEventsFromUser(email);
    }

    /**
    * This method is used to delete an event from the database.
    *
    * @param eventName - The name of the event to delete
    * @param requester - The email of the requester of the deletion to verify if it's the creator of the event
    * @return EnumActionResult - The method returns an Enum value informing the success of the operation.
    */
    public EnumActionResult deleteEvent(String eventName, String requester)
    {
        return eventsManagerData.deleteEvent(eventName, requester);
    }

    /**
    * This method is used to set an event as published.
    *
    * @param eventName - The name of the event to publish
    * @param requester - The email of the requester to verify if it's the creator of the event
    * @return EnumActionResult - The method returns an Enum value informing the success of the operation.
    */
    public EnumActionResult setEventAsPublished(String eventName, String requester)
    {
        return eventsManagerData.setEventAsPublished(eventName, requester);
    }

    /**
    * This method is used to recover an user's password by email
    *
    * @param email - The email of the user wanting to recover the password
    */
    public void recoverPassword(String email)
    {
        eventsManagerData.recoverPassword(email);
    }
}
