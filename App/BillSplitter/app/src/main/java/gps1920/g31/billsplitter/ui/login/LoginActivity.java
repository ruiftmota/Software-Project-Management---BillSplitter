package gps1920.g31.billsplitter.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;

import gps1920.g31.billsplitter.MainActivity;
import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.data.model.LoggedInUser;
import gps1920.g31.billsplitter.ui.registo.RegistoActivity;


public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private LoginThread loginThread;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText emailEditText = findViewById(R.id.etLoginEmail);
        final EditText passwordEditText = findViewById(R.id.etLoginPassword);
        final Button loginButton = findViewById(R.id.btnLoginEntrar);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        if (emailEditText.getText().toString().isEmpty())
            emailEditText.setError("Campo por preencher");
        if (passwordEditText.getText().toString().isEmpty())
            passwordEditText.setError("Campo por preencher");

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                if(loginFormState.isDataValid()) {
                    loginButton.setBackgroundResource(R.drawable.button_style);
                } else
                {
                    //loginButton.setBackgroundColor(Color.argb(43, 28,83,236));
                    loginButton.setBackgroundResource(R.drawable.button_disable);
                }

                loginButton.setPadding(60, 10, 60, 10);
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getEmailError() != null) {
                    emailEditText.setError(getString(loginFormState.getEmailError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(emailEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                 loginThread = new LoginThread(loginViewModel,
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

                loginThread.start();
                /*
                loginViewModel.login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

                 */
            }
        });
    }

    @Override
    protected void onDestroy() {
        loginThread.interrupt();
        super.onDestroy();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        // TODO : initiate successful logged in experience

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email", model.getUserId());
        intent.putExtra("name", model.getName());
        intent.putExtra("surname", model.getSurname());
        startActivity(intent);


        loginThread.interrupt();
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        loginThread.interrupt();

        //TODO: secção completamente MINADA!!!
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email", "minado");
        intent.putExtra("name", "minado");
        intent.putExtra("surname", "minado");
        startActivity(intent);
        finish();

         */


    }

    public void novoRegisto(View view) {
        Intent intent = new Intent(this, RegistoActivity.class);
        startActivity(intent);
        finish();
    }

}
