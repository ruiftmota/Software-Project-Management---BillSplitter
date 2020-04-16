package gps1920.g31.billsplitter.ui.login;


import android.util.Log;

public class LoginThread extends Thread {
    private static final String TAG = "LoginThread";
    LoginViewModel loginViewModel;
    String email;
    String password;

    public LoginThread(LoginViewModel loginViewModel, String email, String password) {
        this.loginViewModel = loginViewModel;
        this.email = email;
        this.password = password;
        Log.i(TAG, "LoginThread: TESTE inicio da thread");
    }

    @Override
    public void run() {
        Log.i(TAG, "run: TESTE arranque da thread");
        loginViewModel.login(email, password);
    }
}
