package gps1920.g31.request_lib.requests;



public class LoginRequest implements Request
{
    private String email;
    private String password;

    public LoginRequest(String email, String password)
    {
        this.email = email;
        this.password = password;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }
}