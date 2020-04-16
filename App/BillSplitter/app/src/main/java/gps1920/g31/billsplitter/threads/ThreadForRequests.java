package gps1920.g31.billsplitter.threads;

import java.util.ArrayList;

import gps1920.g31.billsplitter.data.EventRepository;
import gps1920.g31.billsplitter.data.server_interface.ServerInterface;
import gps1920.g31.request_lib.events_information.Event;

public class ThreadForRequests extends Thread {
    private String email;

    public ThreadForRequests(String email) {
        this.email = email;
    }

    @Override
    public void run() {

        super.run();
    }

    public ArrayList<EventRepository> getListOfEvents ()
    {
        ArrayList<Event> list;
        ArrayList<EventRepository> eventList = new ArrayList<>();

        list = ServerInterface.getEventsFromUser(email);
        EventRepository eventRepository;
        for (Event ev : list) {
            eventRepository = new EventRepository(ev);
            eventList.add(eventRepository);
        }
        return eventList;
    }
}
