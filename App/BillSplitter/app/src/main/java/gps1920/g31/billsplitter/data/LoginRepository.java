package gps1920.g31.billsplitter.data;

import android.util.Log;

import gps1920.g31.billsplitter.data.model.LoggedInUser;

import gps1920.g31.billsplitter.data.server_interface.ServerInterface;
import gps1920.g31.request_lib.events_information.EnumActionResult;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static final String TAG = "LoginRepository";
    private static volatile LoginRepository instance;

    private EnumActionResult errorType;


    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository() {
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    /*
    public void logout() {
        user = null;
        serverInterface.logout();
    }
    */


    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser login(String email, String password) {

        Log.i(TAG, "login: TESTE 2");
        EnumActionResult sucess = ServerInterface.login(email, password);
        if (sucess == EnumActionResult.LOGIN_SUCCESSFULL) {
            //TODO: pedir dados
            //user = ServerInterface.getUser;
            Log.i(TAG, "login: TESTE 3");
        }
        else
        {
            Log.i(TAG, "login: TESTE 3 (ERRO)");
            errorType = sucess;
        }
        Log.i(TAG, "login: TESTE 4 return");
        return user;
    }

    public EnumActionResult getErrorType() {
        return errorType;
    }
}
