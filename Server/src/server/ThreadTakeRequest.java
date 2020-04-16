package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gps.events_information.EnumActionResult;
import gps.events_information.Event;
import gps.requests.*;
import server.event_manager.EventsManager;



    /**
     * 
     * <h1>Taking the client request!</h1>
     * This class takes care of reading the request from the client, 
     * checking the kind of request it is, invoking the respective EventManager
     * method, and sending the EventManager answer back to the client after
     * having it prepared
     * 
     * @author  Rui Mota
     * @version 1.0
     * @since   29-09-2019
    */
public class ThreadTakeRequest extends Thread
{
    private Socket socket;

    private EventsManager eventsManager;

    private Gson gson;

    private ObjectOutputStream oOS;
    private ObjectInputStream oIS;




    public ThreadTakeRequest(Socket socket, EventsManager eventsManager){ 
        this.socket = socket; 
        this.eventsManager = eventsManager;
    }




    
    @Override
    public void run() 
    {
	System.out.println("Client request taken");
        gson = prepareGsonForRequests();
        try 
        {
            oOS = new ObjectOutputStream(socket.getOutputStream());
            oIS = new ObjectInputStream(socket.getInputStream());


            String stringRequest = (String)oIS.readObject();
            Request request = gson.fromJson(stringRequest, Request.class);
            String answer = "";
            EnumActionResult enumAnswer = EnumActionResult.CONNECTION_ERROR;
            
            if(request instanceof CreateEventRequest == true)
            {
                CreateEventRequest cER = (CreateEventRequest) request;
                enumAnswer = eventsManager.createEvent(cER.getEventName(), cER.getUserEmail(), cER.getParticipants(), cER.getExpenses());
            }
            else if(request instanceof DeleteEventRequest == true)
            {
                DeleteEventRequest dER = (DeleteEventRequest) request;
                enumAnswer = eventsManager.deleteEvent(dER.getEventName(), dER.getEmail());
            }
            else if(request instanceof EditEventRequest == true)
            {
                EditEventRequest eER = (EditEventRequest) request;
                enumAnswer = eventsManager.editEvent(eER.getOldName(), eER.getNewName(), eER.getEmail(), eER.getParticipants(), eER.getExpenses());
            }
            else if(request instanceof GetEventsFromUserRequest == true)
            {
                GetEventsFromUserRequest gEFUR = (GetEventsFromUserRequest) request;
                ArrayList<Event> eventsAnswer = eventsManager.getEventsFromUser(gEFUR.getEmail());
                oOS.writeUnshared(new Integer(eventsAnswer.size()));
                oOS.flush();
                for(Event event : eventsAnswer){
                    writeAnswerInJson(event);
                }
                
                return;
            }
            else if(request instanceof LoginRequest == true)
            {
                LoginRequest lR = (LoginRequest) request;
                enumAnswer = eventsManager.loginUser(lR.getEmail(), lR.getPassword());
            }
            else if(request instanceof RegisterRequest == true)
            {
                RegisterRequest rR = (RegisterRequest) request;
                enumAnswer = eventsManager.registerUser(rR.getEmail(), rR.getPassword(), rR.getFirstName(), rR.getLastName());
            }
            else if(request instanceof PublishEventRequest == true)
            {
                PublishEventRequest pER = (PublishEventRequest) request;
                enumAnswer = eventsManager.setEventAsPublished(pER.getName(), pER.getEmail());
            }
            else if(request instanceof LogOutRequest == true)
            {
                LogOutRequest lOR = (LogOutRequest) request;
                eventsManager.logoutUser(lOR.getEmail());
                return;
            }
            else if(request instanceof RecoverPasswordRequest == true)
            {
                RecoverPasswordRequest rPR = (RecoverPasswordRequest) request;
                eventsManager.recoverPassword(rPR.getEmail());
                return;
            }
            else if(request instanceof VerifyUserExistsRequest == true)
            {
                VerifyUserExistsRequest vUER = (VerifyUserExistsRequest) request;
                enumAnswer = eventsManager.userExists(vUER.getEmail());
            }
    
            answer = convertEnumResponseFromServerToString(enumAnswer);
            writeAnswerInJson(answer);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Unable to take client request!");
            System.err.println(e.getStackTrace());
        }finally{
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Unable to close socket after taking client's request.");
                System.err.println(e.getStackTrace());
            }
        }
        
                

    }






    /**
     * This method prepares the Gson object so that it can read any kind of request and 
     * store it in a simple Request reference
     * 
     * @return Gson returns the object that can read the Json string of all the requests
     * that inherit Request properties
    */
    private Gson prepareGsonForRequests()
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Request.class, new RequestAdapter());
        return builder.create();
    }






    /**
     * This method receives an Enum with the answer to be sent to the client and converts
     * it to a String so that it can be written as a String
     * 
     * @param EnumActionResult The EnumActionResult to be converted
     * @return String A string with the answer ready to be sent
    */
    private String convertEnumResponseFromServerToString(EnumActionResult a)
    {
        return a.getValue();
    }





    /**
     * This method prepares receives an object with the answer to be sent to the client
     * and writes it to the output stream of the socket
     * 
     * @param Object The answer to be sent to the client
    */
    private void writeAnswerInJson(Object answer) throws IOException
    {
        gson.toJson(answer);
        oOS.writeUnshared(answer);
        oOS.flush();
    }


}