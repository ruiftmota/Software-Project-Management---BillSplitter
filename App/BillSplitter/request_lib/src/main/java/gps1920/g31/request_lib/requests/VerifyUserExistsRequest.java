package gps1920.g31.request_lib.requests;


public class VerifyUserExistsRequest implements Request
{
    private String email;

    public VerifyUserExistsRequest(String email)
    {
        this.email = email;
    }

    public String getEmail(){ return email; }
}