package gps1920.g31.request_lib.requests;

public class RegisterRequest implements Request
{
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public RegisterRequest(String firstName, String lastName, String email, String password)
    {
        this.firstName = firstName;
        this.email = email;
        this.lastName = lastName;
        this.password = password;
    }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getEmail(){ return email; }

    public String getPassword() { return password; }
}