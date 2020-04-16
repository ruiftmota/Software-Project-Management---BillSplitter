package gps1920.g31.request_lib.requests;



public class DeleteEventRequest implements Request
{
    private String email;
    private String eventName;

    public DeleteEventRequest(String email, String eventName) 
    {
        this.email = email;
        this.eventName = eventName;
    }

    public String getEmail() { return email; }

    public String getEventName() { return eventName; }
    
}