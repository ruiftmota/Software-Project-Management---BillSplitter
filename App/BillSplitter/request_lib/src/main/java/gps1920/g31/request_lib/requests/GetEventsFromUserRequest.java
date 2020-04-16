package gps1920.g31.request_lib.requests;


public class GetEventsFromUserRequest implements Request
{
    private String email;

    public GetEventsFromUserRequest(String email) 
    {
        this.email = email;
    }

    public String getEmail(){ return email; }

    
}