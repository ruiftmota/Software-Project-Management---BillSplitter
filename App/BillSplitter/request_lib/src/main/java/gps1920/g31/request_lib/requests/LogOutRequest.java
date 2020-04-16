package gps1920.g31.request_lib.requests;



public class LogOutRequest implements Request
{
    String email;

    public LogOutRequest(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
    }
    
}