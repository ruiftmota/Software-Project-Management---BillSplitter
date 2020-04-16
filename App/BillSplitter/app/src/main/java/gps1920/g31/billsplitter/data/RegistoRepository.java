package gps1920.g31.billsplitter.data;

import gps1920.g31.billsplitter.data.model.LoggedInUser;
import gps1920.g31.billsplitter.data.server_interface.ServerInterface;
import gps1920.g31.request_lib.events_information.EnumActionResult;

public class RegistoRepository {
    private static volatile RegistoRepository instance;

    private ServerInterface serverInterface;
    private EnumActionResult errorType;


    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private RegistoRepository( ) {
    }

    public static RegistoRepository getInstance( ) {
        if (instance == null) {
            instance = new RegistoRepository();
        }
        return instance;
    }


    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser registo(String name, String surname, String email, String password) {

        EnumActionResult sucess = ServerInterface.register(name, surname, email, password);
        if (sucess == EnumActionResult.REGISTER_SUCCESSFULL) {
            user = new LoggedInUser(email,name, surname);
        }
        else
        {
            errorType = sucess;
        }
        return user;
    }

    public EnumActionResult getErrorType() {
        return errorType;
    }
}
