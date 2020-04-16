package gps1920.g31.billsplitter.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String name;
    private String surname;
    private String displayName;

    public LoggedInUser(String userId, String firstName, String lastName) {
        this.userId = userId;
        this.name = firstName;
        this.surname = lastName;
        this.displayName = firstName + " " + lastName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
