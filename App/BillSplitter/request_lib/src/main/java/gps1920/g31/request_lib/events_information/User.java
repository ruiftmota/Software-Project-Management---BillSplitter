package gps1920.g31.request_lib.events_information;

import java.io.Serializable;

public class User implements Serializable
{
    private static final long serialVersionUID = 1234567891234567890L;

    private String email;
    private String password;
    private String first_name;
    private String last_name;

    public User(String email, String password, String first_name, String last_name)
    {
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public User(User user)
    {
        this.email = user.email;
        this.first_name = user.first_name;
        this.last_name = user.last_name;
        this.password = new String("************");
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFirst_name()
    {
        return first_name;
    }

    public String getLast_name()
    {
        return last_name;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof User == true)
        {
            User user = (User)obj;

            if(this.email.equals(user.getEmail()))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}