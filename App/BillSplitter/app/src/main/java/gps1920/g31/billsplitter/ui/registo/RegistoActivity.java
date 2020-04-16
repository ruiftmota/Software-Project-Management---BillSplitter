package gps1920.g31.billsplitter.ui.registo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import gps1920.g31.billsplitter.MainActivity;
import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.ui.login.LoggedInUserView;
import gps1920.g31.billsplitter.ui.login.LoginActivity;

public class RegistoActivity extends AppCompatActivity {

    private RegistoViewModel registoViewModel;
    private RegistoThread registoThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registo);


        registoViewModel = ViewModelProviders.of(this, new RegistoViewModelFactory()).get(RegistoViewModel.class);

        final EditText emailEditText = findViewById(R.id.etRegistoEmail);
        final EditText firstNameEditText = findViewById(R.id.etRegistoFirstName);
        final EditText lastNameEditText = findViewById(R.id.etRegistoLastName);
        final EditText passwordEditText = findViewById(R.id.etRegistoPassword);
        final EditText confirmPasswordEditText = findViewById(R.id.etConfirmPass);
        final Button registoButton = findViewById(R.id.btnRegisto);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        if (emailEditText.getText().toString().isEmpty())
            emailEditText.setError("Campo por preencher");
        if (passwordEditText.getText().toString().isEmpty())
            passwordEditText.setError("Campo por preencher");
        if (firstNameEditText.getText().toString().isEmpty())
            firstNameEditText.setError("Campo por preecher");
        if (lastNameEditText.getText().toString().isEmpty())
            lastNameEditText.setError("Campo por preencher");


        registoViewModel.getRegistoFormState().observe(this, new Observer<RegistoFormState>()
        {
            @Override
            public void onChanged(@Nullable RegistoFormState registoFormState)
            {
                if (registoFormState == null)
                    return;
                if(registoFormState.isDataValid())
                    registoButton.setBackgroundResource(R.drawable.button_style);
                else
                    registoButton.setBackgroundResource(R.drawable.button_disable);

                registoButton.setPadding(60, 10, 60, 10);
                registoButton.setEnabled(registoFormState.isDataValid());
                if(registoFormState.getEmailError() != null)
                {
                    emailEditText.setError(getString(registoFormState.getEmailError()));
                }
                if(registoFormState.getNameError() != null)
                {
                    firstNameEditText.setError(getString(registoFormState.getNameError()));
                }
                if(registoFormState.getName2Error() != null)
                {
                    lastNameEditText.setError(getString(registoFormState.getName2Error()));
                }
                if(registoFormState.getPasswordError() != null)
                {
                    passwordEditText.setError(getString(registoFormState.getPasswordError()));
                }
                if(registoFormState.getConfirmPasswordError() != null)
                {
                    confirmPasswordEditText.setError(getString(registoFormState.getConfirmPasswordError()));
                }
            }
        });



        registoViewModel.getRegistoResult().observe(this, new Observer<RegistoResult>() {
            @Override
            public void onChanged(@Nullable RegistoResult registoResult) {
                if (registoResult == null)
                    return;
                loadingProgressBar.setVisibility(View.GONE);
                if (registoResult.getError() != null)
                    showRegistoFailed(registoResult.getError());
                if (registoResult.getSuccess() != null)
                    updateUiWithUser(registoResult.getSuccess());
                setResult(Activity.RESULT_OK);
                //TODO: Complete and destroy registo activity once successful
                //finish();
            }
        });



        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registoViewModel.registoDataChanged(emailEditText.getText().toString(),
                        firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                        passwordEditText.getText().toString(), confirmPasswordEditText.getText().toString());
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener);
        firstNameEditText.addTextChangedListener(afterTextChangedListener);
        lastNameEditText.addTextChangedListener(afterTextChangedListener);

        registoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                registoThread = new RegistoThread(registoViewModel,
                        firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

                registoThread.start();

                /*
                registoViewModel.registo(firstNameEditText.getText().toString(),
                        lastNameEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString());

                 */
            }
        });

    }

    @Override
    protected void onDestroy() {
        registoThread.interrupt();
        super.onDestroy();
    }

    private void updateUiWithUser(LoggedInUserView model) {
        // TODO : initiate successful logged in experience
        //Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email", model.getUserId());
        intent.putExtra("name", model.getName());
        intent.putExtra("surname", model.getSurname());
        startActivity(intent);

        registoThread.interrupt();
        finish();
    }

    private void showRegistoFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
        registoThread.interrupt();
        /*
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("email", "minado");
        intent.putExtra("name", "minado");
        intent.putExtra("surname", "minado");
        startActivity(intent);
        finish();
         */
    }

    public void jaTemConta(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

