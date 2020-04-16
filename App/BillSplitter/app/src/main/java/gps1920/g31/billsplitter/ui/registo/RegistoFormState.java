package gps1920.g31.billsplitter.ui.registo;

import androidx.annotation.Nullable;

class RegistoFormState {
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer confirmPasswordError;
    @Nullable
    private Integer nameError;
    @Nullable
    private Integer name2Error;
    private boolean isDataValid;

    RegistoFormState(@Nullable Integer emailError,
                     @Nullable Integer passwordError, @Nullable Integer confirmPasswordError,
                     @Nullable Integer nameError, @Nullable Integer name2Error) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.confirmPasswordError = confirmPasswordError;
        this.name2Error = name2Error;
        this.nameError = nameError;
        this.isDataValid = false;
    }

    RegistoFormState(boolean isDataValid) {
        this.emailError = null;
        this.passwordError = null;
        this.nameError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getEmailError() {return emailError;}
    @Nullable
    Integer getPasswordError() {return passwordError;}
    @Nullable
    Integer getConfirmPasswordError() {return confirmPasswordError;}
    @Nullable
    Integer getNameError(){return nameError;}
    @Nullable
    Integer getName2Error() {return  name2Error;}

    boolean isDataValid() {
        return isDataValid;
    }

}
