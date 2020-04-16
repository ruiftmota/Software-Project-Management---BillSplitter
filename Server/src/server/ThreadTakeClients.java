package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import server.event_manager.EventsManager;



/**
     * 
     * <h1>Receiving the client!</h1>
     * This class receives the client and invokes a new thread
     * ThreadTakeRequest that specificaly takes that clients request
     * 
     * @author  Rui Mota
     * @version 1.0
     * @since   29-09-2019
    */
public class ThreadTakeClients extends Thread
{
    private EventsManager eventsManager;

    private ServerSocket serverSocket;
    private boolean keepAlive = true;
    private Socket socket;
    private int timeout = 5000;



    public ThreadTakeClients(ServerSocket serverSocket, EventsManager eventsManager)
    { 
        this.serverSocket = serverSocket;
        this.eventsManager = eventsManager;
    }

    


    @Override
    public void run() 
    {
	System.out.println("Ready to take clients requests!");
        try 
        {
            serverSocket.setSoTimeout(timeout);
        }
        catch (IOException e) 
        {
            System.err.println("unable to establish timeout");
        }
        while(keepAlive)
        {
            try 
            {
                socket = serverSocket.accept();
                ThreadTakeRequest threadTakeRequest = new ThreadTakeRequest(socket, eventsManager);
                threadTakeRequest.setDaemon(true);
                threadTakeRequest.start();
                
                
            } 
            catch (IOException e) 
            {
                continue;
            }
            
        }

       try {
           serverSocket.close();
       } catch (IOException e) {
           System.err.println("Unable to close serverSocket before quiting.");
       }

    }


    
    public void setKeepAlive(boolean keepAlive){ this.keepAlive = keepAlive; }
}