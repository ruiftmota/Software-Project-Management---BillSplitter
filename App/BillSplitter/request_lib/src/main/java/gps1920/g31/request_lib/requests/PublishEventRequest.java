package gps1920.g31.request_lib.requests;



public class PublishEventRequest implements Request
{
    String email;
    String name;

    public PublishEventRequest(String email, String name) 
    {
        this.email = email;
        this.name = name;
    }

    public String getEmail() { return email; }

    public String getName() { return name; }

}