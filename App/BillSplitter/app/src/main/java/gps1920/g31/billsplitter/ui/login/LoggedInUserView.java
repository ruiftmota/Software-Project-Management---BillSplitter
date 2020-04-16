package gps1920.g31.billsplitter.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private String userId;
    private String name;
    private String surname;
    private String displayName;

    public LoggedInUserView(String userId, String firstName, String lastName) {
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
