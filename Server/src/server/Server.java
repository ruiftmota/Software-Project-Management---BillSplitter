package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.Scanner;

import server.event_manager.EventsManager;


    /**
     * 
     * <h1>Server</h1>
     * This class takes care the admin direct commands such as shutdown
     * and launches a thread to receive clients
     * 
     * @author  Rui Mota
     * @version 1.0
     * @since   27-09-2019
    */
public class Server
{
    private ServerSocket serverSocket;
    private int serverPort;
    private boolean keepAlive = true;

    private EventsManager eventsManager;
    private int serverPortUDP = 7500;



    public Server(int serverPort, EventsManager eventsManager)
    {
        this.serverPort = serverPort;
        if(eventsManager != null){
            this.eventsManager = eventsManager;
        }else{
            this.eventsManager = new EventsManager();
        }
    }

    

    public void start()
    {
        try
        {
            serverSocket = new ServerSocket(serverPort);
        }
        catch(IOException e)
        {
            System.out.println("Port 6500 occupied.");
            System.err.println(e.getStackTrace());
            return;
        }
        
 
        Thread threadIP = new Thread(new Runnable(){
        
                @Override
                public void run() {
                    try{
			            System.out.println("Ready to giveout IP");
                        DatagramSocket datagramSocket = new DatagramSocket(serverPortUDP);
                        datagramSocket.setSoTimeout(5000);
                        while(keepAlive){
                            DatagramPacket packet = new DatagramPacket(new byte[0], 0);
                            datagramSocket.receive(packet);
                            String portTCP = new String(Integer.toString(serverPort));
                            
                            datagramSocket.send(new DatagramPacket(portTCP.getBytes(), portTCP.getBytes().length, packet.getAddress(), packet.getPort()));
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    
                    
                }
        });
        threadIP.start();

        ThreadTakeClients takeClients = new ThreadTakeClients(serverSocket, eventsManager);
        takeClients.start();

        Scanner sc = new Scanner(System.in);
        String command;

        while(keepAlive == true)
        {
            System.out.println("Insert a command...");
            command = sc.next();
            
            if(command.equals("shutdown"))
            {
                takeClients.setKeepAlive(false);
                keepAlive = false;
                System.out.println("Shutting the server down... Wait a few seconds.");
            }
        }


        sc.close();

        try {
            takeClients.join();    
        } catch (InterruptedException e) {
            System.err.println("Interrompido enquanto espera join da takeClients");
            System.err.println(e.getStackTrace());
        }
        
    }



    public static EventsManager loadData(){
        String fileName = "SavedData.bin";
        EventsManager eventsManager = null;
        try{
            ObjectInputStream is = new ObjectInputStream(new FileInputStream(fileName));
            eventsManager = (EventsManager) is.readObject();
            is.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch(ClassNotFoundException x){
            x.printStackTrace();
        }
        return eventsManager;
    }


    public static void saveData(EventsManager eventsManager){
        String fileName = "SavedData.bin";
        try{
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(fileName));
            os.writeObject(eventsManager);
            os.close();
        } catch(FileNotFoundException e){
            System.out.println("No file found to load from.");
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) 
    {
        int serverPort = 6500;
	EventsManager eventsManager;
        if(args.length == 1)
        {
	    if(args[0].equals("load"))
		eventsManager = Server.loadData();
	    else
	    {
		System.out.println("Invalid file! \n Exiting...");
		return;
	    }
        }
	else
	{
	     eventsManager = new EventsManager();      	
	}

        
        
        Server server = new Server(serverPort, eventsManager);
        server.start();

        Server.saveData(eventsManager);
    }
}