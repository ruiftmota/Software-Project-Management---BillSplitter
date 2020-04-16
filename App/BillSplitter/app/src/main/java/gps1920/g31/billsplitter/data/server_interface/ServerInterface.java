package gps1920.g31.billsplitter.data.server_interface;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import gps1920.g31.request_lib.events_information.*;
import gps1920.g31.request_lib.requests.*;

/**
 * <h1>Communication on the client side!</h1>
 * This class takes care of all the communication on the client side
 * Every option on the client app that has any connection to
 * the server has its function here.
 *
 * @author  Rui Mota
 * @version 1.0
 * @since   27-09-2019
*/


public class ServerInterface
{


    private static Integer serverPortUDP = 7500;

    private static Integer serverPortTCP;
    private static String serverIP = null;
    private static Integer timeoutUDP = 3000;

    private static Socket socket;
    private static ObjectOutputStream oOS;
    private static ObjectInputStream oIS;




    /**
     * This method is used to ask if the user email and
     * password are registered and to establish the user as
     * connected
     * @param email This is the email of the user
     * @param password  This is the password of the user
     * @return EnumActionResult This returns an Enum with the resullt of the request.
    */
    public static EnumActionResult login(String email, String password)
    {
        LoginRequest login_request = new LoginRequest(email, password);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();
            

            String stringRequest = readyRequest((Request)login_request);

            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(login_request);
            oOS.writeUnshared("CreateEventRequest");
            */
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            String response = (String) oIS.readObject();
            
            closeSocket();

            return convertStringToEnum(response);

            
        }
        catch(Exception e)
        {
            try
            {
                closeSocket();
            }
            catch(IOException a)
            {
                a.printStackTrace();
            }
            return EnumActionResult.CONNECTION_ERROR;
        }
        
    }





    /**
     * This method is used to ask the server for a new user account.
     * 
     * @param first_name This is the first name of the user
     * @param last_name This is the last name of the user
     * @param email This is the email the user has choosen to associate with the account
     * @param password  This is the account password choosen by the user
     * @return EnumActionResult This returns an Enum with the resullt of the registration.
    */
    public static EnumActionResult register(String first_name, String last_name, String email, String password)
    {
        RegisterRequest register_request = new RegisterRequest(first_name, last_name, email, password);

        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();
            

            String stringRequest = readyRequest((Request)register_request);

            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(register_request);
            oOS.writeUnshared("RegisterRequest");
            */
            
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            String response = (String) oIS.readObject();
            
            closeSocket();

            return convertStringToEnum(response);
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }
            
            return EnumActionResult.CONNECTION_ERROR;
        }

    }






    /**
     * This method is used to ask the server to create a new event
     * 
     * @param email This is the email that identifies the user
     * @param event_name This is the name choosen to give to the event
     * @param participants This is the list of emails of the other participants that are included in the event
     * @return EnumActionResult This returns an Enum with the result of the event creation
    */
    public static EnumActionResult createEvent(String email, String event_name, ArrayList<String> participants, ArrayList<Expense> expenses)
    {
        CreateEventRequest createEventRequest = new CreateEventRequest(email, event_name, participants, expenses);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();


            String stringRequest = readyRequest((Request)createEventRequest);
            
            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(createEventRequest);
            oOS.writeUnshared("CreateEventRequest");
            */
            
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            String response = (String) oIS.readObject();
            
            closeSocket();

            return convertStringToEnum(response);
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }
            
            return EnumActionResult.CONNECTION_ERROR;
        }

    }





    /**
     * This method is used to ask if a certain user is already registered.
     * 
     * @param email This is the email that identifies the user
     * @return EnumActionResult This returns an Enum with the result of the users existance
    */
    public static EnumActionResult verifyUser(String email)
    {
        VerifyUserExistsRequest verifyUserExistsRequest = new VerifyUserExistsRequest(email);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();


            String stringRequest = readyRequest((Request)verifyUserExistsRequest);

            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(createEventRequest);
            oOS.writeUnshared("VerifyUserExistsRequest");
            */

            oOS.writeUnshared(stringRequest);
            oOS.flush();
            String response = (String) oIS.readObject();
            
            closeSocket();

            return convertStringToEnum(response);
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }
            
            return EnumActionResult.CONNECTION_ERROR;
        }

    }






    /**
     * This method is used to ask the server to delete an existing event
     * 
     * @param email This is the email that identifies the user that is trying to delete the event
     * @param event_name This is the name of the event to delete
     * @return EnumActionResult This returns an Enum with the result of the event erasure
    */
    public static EnumActionResult deleteEvent(String email, String event_name)
    {
        DeleteEventRequest deleteEventRequest = new DeleteEventRequest(email, event_name);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();
            

            String stringRequest = readyRequest((Request)deleteEventRequest);
        
            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(deleteEventRequest);
            oOS.writeUnshared("DeleteEventRequest");
            */
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            String response = (String) oIS.readObject();
            
            closeSocket();

            return convertStringToEnum(response);
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }
            
            return EnumActionResult.CONNECTION_ERROR;
        }
    }






    /**
     * This method is used to ask the server to edit an existing event and must 
     * receive the previous name of the event so that it can be identified
     * in order to be edited
     * 
     * @param email This is the email that identifies the user that is trying to edit the event
     * @param newName This is the new name choosen to give to the event.
     * If none intended the old one must be passed here as well
     * @param oldName This is the name had before trying to be edited
     * @param participants This is the list of all the participants that are 
     * supposed to be included after the event's edition
     * @param expenses This is the list of all the expenses to be included in the event after
     * edition
     * @return EnumActionResult This returns an Enum with the result of the event edition
    */
    public static EnumActionResult editEvent(String email, String newName, String oldName, ArrayList<String> participants, ArrayList<Expense> expenses)
    {
        EditEventRequest editEventRequest = new EditEventRequest(email, newName, oldName, participants, expenses);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();


            String stringRequest = readyRequest((Request)editEventRequest);

            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(editEventRequest);
            oOS.writeUnshared("EditEventRequest");
            */
            
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            String response = (String) oIS.readObject();
            
            closeSocket();

            return convertStringToEnum(response);
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }
            
            return EnumActionResult.CONNECTION_ERROR;
        }
    }






    /**
     * This method is used to ask the server for all the server for all
     * the events related to one user
     * 
     * @param email This is the email that identifies the user
     * @return ArrayList<Event> This returns an ArrayList with all the user's events 
    */
    public static ArrayList<Event> getEventsFromUser(String email)
    {
        GetEventsFromUserRequest getEventsFromUserRequest = new GetEventsFromUserRequest(email);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();
            
            
            String stringRequest = readyRequest((Request)getEventsFromUserRequest);
            
            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(getEventsFromUserRequest);
            oOS.writeUnshared("GetEventsFromUserRequest");
            */

            oOS.writeUnshared(stringRequest);
            oOS.flush();
            String response = (String) oIS.readObject();
            Integer numberOfObjects = new Integer(response);

            ArrayList<Event> eventList = new ArrayList<Event>(numberOfObjects); 

            Gson gson = new Gson();

            for(int i=0 ; i<numberOfObjects ; i++){
                response = (String)oIS.readObject();
                eventList.add(gson.fromJson(response, Event.class));
            }
            
            closeSocket();
            return eventList;
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }

            return null;
            
        }
    }






    /**
     * This method is used to ask the server to publish a certain event
     * 
     * @param email This is the email that identifies the user that is trying to publish the event
     * @param name This is the new name of the event to be published
     * @return EnumActionResult This returns a boolean with either or not the event was published
    */
    public static Boolean setPublishEvent(String email, String name)
    {
        PublishEventRequest publishEventRequest = new PublishEventRequest(email, name);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();
            
            
            String stringRequest = readyRequest((Request)publishEventRequest);
            
            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(publishEvent);
            oOS.writeUnshared("PublishEventRequest");
            */
            
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            Boolean response = (Boolean) oIS.readObject();
            
            closeSocket();

            return response;
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }

            return null;
            
        }
    }






    /**
     * This method is used to tell the server that a user is disconnecting
     * 
     * @param email This is the email that identifies the user that wants to log out
    */
    public static void logOut(String email)
    {
        LogOutRequest logOutRequest = new LogOutRequest(email);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();
            
            
            String stringRequest = readyRequest((Request)logOutRequest);
            
            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(logOutRequest);
            oOS.writeUnshared("LogOutRequest");
            */
            
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            
            closeSocket();

        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }
            
        }
    }






    /**
     * This method is used to ask the server to for an email with information about his account
     * 
     * @param email This is the email that identifies the user that is trying to recover his password
    */
    public static void recoverPassword(String email)
    {
        RecoverPasswordRequest recoverPasswordRequest = new RecoverPasswordRequest(email);
        
        if(ServerInterface.serverIP == null)
            findServerIP();

        try
        {
            readySocket();
            
            String stringRequest = readyRequest((Request)recoverPasswordRequest);


            /*
            Gson gson = new Gson();
            String stringRequest = gson.toJSon(recoverPasswordRequest);
            oOS.writeUnshared("RecoverPasswordRequest");
            */
            oOS.writeUnshared(stringRequest);
            oOS.flush();
            
            closeSocket();
            
        }
        catch(Exception e)
        {
            try{
                closeSocket();
            }catch(IOException a){
                a.printStackTrace();
            }
            
        }
    }

    

    



    /**
     * This method is used to search the servers IP in the same network as the clients
     * It stores all need identification in static class attributes so that it can later
     * be used to establish a connection to the server 
     * 
    */
    private static void findServerIP(){
        while(true){
            try {
                DatagramSocket dS = new DatagramSocket();
                dS.setSoTimeout(timeoutUDP);
                DatagramPacket packet = new DatagramPacket(new byte[0], 0, InetAddress.getByName("255.255.255.255"), serverPortUDP);
                dS.send(packet);
                packet = new DatagramPacket(new byte[256], 256);
                dS.receive(packet);
                serverIP = packet.getAddress().getHostName();
                String serverPortText = new String(packet.getData(),0, packet.getLength());
                serverPortTCP = new Integer(serverPortText);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     * This method is used to convert a string to it's respective Enum value
     * 
     * @param response This is the string tha is supposed to be returned as its Enum value
     * @return EnumActionResult This is the Enum with the value of the response param
    */
    private static EnumActionResult convertStringToEnum(String response)
    {
        for(EnumActionResult resp : EnumActionResult.values())
        {
            if(resp.getValue().compareToIgnoreCase(response) == 0)
            {
                return resp;
            }
        }
        return null;
    }





    /**
     * This method is used to establish the connection to the server each time there
     * is something to be asked to the server. 
     * Stores the values in static class parameters so that they may be used in the rest
     * of the function that invoques this method
     * 
    */
    private static void readySocket() throws IOException{
        socket = new Socket(serverIP, serverPortTCP);
        oOS= new ObjectOutputStream(socket.getOutputStream());
        oIS = new ObjectInputStream(socket.getInputStream());
    }




    /**
     * This method is used to close the socket and its streams after the communication
     * establish has finished it's purpose
    */
    private static void closeSocket()throws IOException{
        oOS.close();
        oIS.close();
        socket.close();
    }




    /**
     * This method prepares all the request to be sent to the server
     * 
     * @param r This is the request to be prepared
     * @return String This is the Json string to be serialized and sent
    */
    private static String readyRequest(Request r){
        Gson gson;
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Request.class, new RequestAdapter());
        gson = builder.create();
        String stringRequest = gson.toJson(r, Request.class);
        return stringRequest;
    }

}