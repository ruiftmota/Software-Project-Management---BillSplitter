package gps1920.g31.request_lib.requests;

public class RecoverPasswordRequest implements Request
{
    String email;

    public RecoverPasswordRequest(String email) 
    {
        this.email = email;
    }

    public String getEmail() { return email; }
    
}