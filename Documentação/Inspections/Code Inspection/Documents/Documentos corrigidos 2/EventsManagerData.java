package server.event_manager;

import java.io.Serializable;
import java.util.ArrayList;

import gps.events_information.Event;
import gps.events_information.User;
import gps.events_information.EnumActionResult;
import gps.events_information.Expense;

/**
* <h1>Event Manager Data</h1>
* This class encapsulates the event management data from the
* Event Manager class. It offers an interface to the Event Manager class
* to manipulate the data
*
* @author  Ruben Marques
* @version 1.0
* @since   27-09-2019
*/
public class EventsManagerData implements Serializable
{
    private static final long serialVersionUID = 1966661767010825702L;
    
    private ArrayList<Event> events;
    private ArrayList<User> registeredUsers;
    private ArrayList<User> loggedInUsers;

    /**
    * This constructor creates a new event manager data that can be
    * used by the EventManager class to manipulate the data.
    */
    public EventsManagerData()
    {
        events = new ArrayList<Event>();
        registeredUsers = new ArrayList<User>();
        loggedInUsers = new ArrayList<User>();
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
        if(isUserRegistered(email) == false)
        {
            return EnumActionResult.UNEXISTANT_USER;
        }

        if(isUserLoggedIn(email) == true)
        {
            return EnumActionResult.USER_ALREADY_LOGGED_IN;
        }
        
        if(isPasswordValid(email, password) == true)
        {
            loggedInUsers.add(getUser(email));
            return EnumActionResult.LOGIN_SUCCESSFULL;
        }
        else
        {
            return EnumActionResult.LOGIN_REFUSED;
        }
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
        if(isUserRegistered(email) == true)
        {
            return EnumActionResult.REGISTER_REFUSED;
        }

        registeredUsers.add(new User(email, password, firstName, lastName));
        return EnumActionResult.REGISTER_SUCCESSFULL;
    }

    /**
    * This method is used to log out an user from the system.
    *
    * @param email - User's email
    */
    public void logoutUser(String email)
    {
        User user = getUser(email);

        loggedInUsers.remove(user);
    }

    /**
    * This method is used to inform if an user exists in the system.
    *
    * @param email - User's email
    * @return EnumActionResult - The method returns an Enum value informing if the user exists or not.
    */
    public EnumActionResult userExists(String email)
    {
        if(isUserRegistered(email) == true)
        {
            return EnumActionResult.USER_EXISTS;
        }
        else
        {
            return EnumActionResult.UNEXISTANT_USER; 
        }
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
        User admin = getUser(creator);

        if(admin == null) { return EnumActionResult.UNEXISTANT_USER; }

        if(eventsExists(eventName) == true) { return EnumActionResult.EVENT_ALREADY_EXISTS; }

        events.add(new Event(eventName, admin, getUsers(participants), expenses));

        return EnumActionResult.EVENT_CREATION_SUCCESSFULL;
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
        if(eventsExists(eventName) == false) { return EnumActionResult.UNEXISTANT_EVENT; }

        if(isUserAdmin(eventName, requester)) { return EnumActionResult.NOT_ADMIN; }

        Event event = getEvent(eventName);

        if(event.isPublished() == true) { return EnumActionResult.EVENT_IS_PUBLISHED; }

        event.setName(newName);
        event.setExpenses(expenses);
        event.setParticipants(getUsers(participants));

        return EnumActionResult.EDITION_SUCCESSFULL;
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
        if(eventsExists(eventName) == false) { return EnumActionResult.UNEXISTANT_EVENT; }

        Event event = getEvent(eventName);

        if(isUserAdmin(eventName, requester) == false) { return EnumActionResult.NOT_ADMIN; }

        events.remove(event);

        return EnumActionResult.DELETED_SUCCESSFULLY;
    }

    /**
    * This method is used to get the events where the user is inserted or is the creator
    *
    * @param email - The email of the user
    * @return ArrayList<Event> - The method returns a list with the events where the users is inserted or is the creator
    */
    public ArrayList<Event> getEventsFromUser(String email)
    {
        if(isUserRegistered(email) == false) { return null; }

        ArrayList<Event> userEvents = new ArrayList<Event>();

        for(int i = 0; i < events.size(); i++)
        {
            if(isUserAdmin(events.get(i).getName(), email) == true || isUserParticipant(events.get(i).getName(), email) == true)
            {
                userEvents.add(events.get(i));
            }
        }

        return userEvents;
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
        if(eventsExists(eventName) == false) { return EnumActionResult.UNEXISTANT_EVENT; }

        if(isUserAdmin(eventName, requester) == false) { return EnumActionResult.NOT_ADMIN; }

        Event event = getEvent(eventName);

        event.setEventAsPublished();

        return EnumActionResult.EVENT_IS_PUBLISHED;
    }

    /**
    * This method is used to recover an user's password by email
    *
    * @param email - The email of the user wanting to recover the password
    */
    public void recoverPassword(String email)
    {
        
    }

    /**
     * 
     * @param email
     * @return
     */
    private boolean isUserRegistered(String email)
    {
        for(User user : registeredUsers)
        {
            if(user.getEmail().equals(email) == true)
            {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 
     * @param email
     * @return
     */
    private boolean isUserLoggedIn(String email)
    {
        for(User user : loggedInUsers)
        {
            if(user.getEmail().equals(email))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param email
     * @param password
     * @return
     */
    private boolean isPasswordValid(String email, String password)
    {
       
        for(User user : registeredUsers)
        {
            if(user.getEmail().equals(email) && registeredUsers.get(i).getPassword().equals(password))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param email
     * @return
     */
    private User getUser(String email)
    {
        for(User user : registeredUsers)
        {
            if(user.getEmail().equals(email))
            {
                return user;
            }
        }

        return null;
    }

    /**
     * 
     * @param usersEmails
     * @return
     */
    private ArrayList<User> getUsers(ArrayList<String> usersEmails)
    {
        ArrayList<User> users = new ArrayList<User>();

        for(String userEmail : usersEmails)
        {
            users.add(getUser(userEmail));
        }

        return users;
    }

    /**
     * 
     * @param eventName
     * @return
     */
    private Event getEvent(String eventName)
    {
        for(Event event : events)
        {
            if(event.getName().equals(eventName))
            {
                return event;
            }
        }

        return null;
    }

    /**
     * 
     * @param name
     * @return
     */
    private boolean eventExists(String name)
    {
        for(Event event : events)
        {
            if(event.getName().equals(name))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param eventName
     * @param email
     * @return
     */
    private boolean isUserAdmin(String eventName, String email)
    {
        Event event = getEvent(eventName);

        if(event.getAdministrator().getEmail().equals(email))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 
     * @param eventName
     * @param email
     * @return
     */
    private boolean isUserParticipant(String eventName, String email)
    {
        Event event = getEvent(eventName);

        ArrayList<User> participants = event.getParticipants();

        for(User user : participants)
        {
            if(user.getEmail().equals(email))
            {
                return true;
            }
        }

        return false;
    }
}