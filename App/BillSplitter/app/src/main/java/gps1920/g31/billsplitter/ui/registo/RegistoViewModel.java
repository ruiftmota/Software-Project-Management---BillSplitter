package gps1920.g31.billsplitter.ui.registo;

import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.data.LoginRepository;
import gps1920.g31.billsplitter.data.RegistoRepository;
import gps1920.g31.billsplitter.data.model.LoggedInUser;
import gps1920.g31.billsplitter.ui.login.LoggedInUserView;


class RegistoViewModel extends ViewModel {
    private MutableLiveData<RegistoFormState> registoFormState = new MutableLiveData<>();
    private MutableLiveData<RegistoResult> registoResult = new MutableLiveData<>();
    private RegistoRepository registoRepository;

    RegistoViewModel(RegistoRepository registoRepository)
    {
        this.registoRepository =registoRepository;
    }
    LiveData<RegistoFormState> getRegistoFormState()
    {
        return registoFormState;
    }
    LiveData<RegistoResult>  getRegistoResult()
    {
        return registoResult;
    }

    public void registo(String name, String surname, String email, String password) {
        // can be launched in a separate asynchronous job
        LoggedInUser data = registoRepository.registo(name, surname, email, password);
        if (data != null) {
            registoResult.setValue(new RegistoResult(new LoggedInUserView(data.getUserId(),data.getName(),data.getSurname())));
        } else {
            
            registoResult.setValue(new RegistoResult(R.string.authentication));
        }
    }


    public void registoDataChanged(String email, String firstName, String lastName, String password, String confirmPassword) {

        //Validação do firstName
         if (!isNameValid(firstName)){
            registoFormState.setValue(new RegistoFormState(null, null,
                    null, R.string.invalid_username, null));
        }

        //Validação do lastName
        else if (!isNameValid(lastName)){
            registoFormState.setValue(new RegistoFormState(null, null,
                    null, null, R.string.invalid_username));
        }

        //Validação do email
        else if (!isEmailValid(email)) {
             registoFormState.setValue(new RegistoFormState(R.string.invalid_email, null,
                     null, null, null));
         }

        //Validação da password
        else if (isPasswordValid(password)==1) {
            registoFormState.setValue(new RegistoFormState(null, R.string.invalid_password_lenght,
                    null, null, null));
        } else if (isPasswordValid(password)==2) {
            registoFormState.setValue(new RegistoFormState(null, R.string.invalid_password_lenght,
                    null, null, null));
        } else if (isPasswordValid(password)==3) {
            registoFormState.setValue(new RegistoFormState(null, R.string.invalid_password_lenght,
                    null, null, null));
        }

        //Validação da confirmPassword
        else if (!isConfirmPasswordValid(confirmPassword, password)) {
            registoFormState.setValue(new RegistoFormState(null, null,
                    R.string.invalid_confirm_password, null, null));
        } else{
            registoFormState.setValue(new RegistoFormState(true));
        }
    }


    private boolean isEmailValid(String username) {
        if (username == null) {
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private boolean isNameValid(String lastName) {
        return lastName != null && lastName.trim().length() <= 12;
    }

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

    private boolean isConfirmPasswordValid(String confirmPassword, String password) {
        return confirmPassword != null && confirmPassword.equalsIgnoreCase(password);
    }
}

class RegistoResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    RegistoResult(@Nullable Integer error) {
        this.error = error;
    }

    RegistoResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
