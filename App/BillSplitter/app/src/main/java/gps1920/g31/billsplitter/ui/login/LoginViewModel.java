package gps1920.g31.billsplitter.ui.login;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.data.LoginRepository;
import gps1920.g31.billsplitter.data.model.LoggedInUser;
import gps1920.g31.request_lib.events_information.EnumActionResult;

public class LoginViewModel extends ViewModel {
    private static final String TAG = "LoginViewModel";

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        // can be launched in a separate asynchronous job
        Log.i(TAG, "login: TESTE 1");
        LoggedInUser data = loginRepository.login(email, password);
        Log.i(TAG, "login: TESTE 5");
        if (data != null) {
            Log.i(TAG, "login: TESTE USER != NULL");
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUserId(),data.getName(),data.getSurname())));
        } else {
            Log.i(TAG, "login: TESTE USER == NULL");
            if (loginRepository.getErrorType()== EnumActionResult.UNVALID_PASSWORD)
                loginResult.setValue(new LoginResult(R.string.invalid_password));
            loginResult.setValue(new LoginResult(R.string.authentication));
        }
    }

    public void loginDataChanged(String email, String password) {
        if (!isEmailValid(email)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (isPasswordValid(password)==1) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password_lenght));
        } else if (isPasswordValid(password)==2) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password_white_space));
        } else if (isPasswordValid(password)==3) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password_content));
        } else{
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }

            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    // A placeholder password validation check
    private int isPasswordValid(String password) {

        //String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,16}";
        String patternExtraAlphanumeric = "(?=.*[@#$%^&+=_.,:;'?!*<>]).{0,}";
        String patternNoWhiteSpace = "(?=\\S+$).{0,}";
        if(password.trim().length() < 8)
            return 1;
        else if (!password.matches(patternNoWhiteSpace))
            return 2;
        else if (!password.matches(patternExtraAlphanumeric))
            return 3;
        else
            return 0;
    }
}
