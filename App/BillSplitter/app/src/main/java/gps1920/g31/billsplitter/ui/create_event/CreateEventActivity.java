package gps1920.g31.billsplitter.ui.create_event;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.data.EventRepository;
import gps1920.g31.billsplitter.data.ExpenseRepository;
import gps1920.g31.billsplitter.data.ParticipantsRepository;
import gps1920.g31.billsplitter.data.model.ListExpensesViewModel;
import gps1920.g31.billsplitter.data.model.ListParticipantsViewModel;


public class CreateEventActivity extends AppCompatActivity {
    RecyclerView rvParticipants;
    RecyclerView rvExpenses;
    ListParticipantsViewModel listParticipantsViewModel;
    ListExpensesViewModel listExpensesViewModel;

    EditText etNomeEvento;
    Button btnConfirmar;
    ImageButton btnMaisParticipantes;
    ImageButton btnMaisDespesas;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);


        etNomeEvento = findViewById(R.id.etNomeEvento);

        btnConfirmar = findViewById(R.id.btnNovoEvento);
        btnMaisParticipantes = findViewById(R.id.btnMaisParticipantes);
        btnMaisDespesas = findViewById(R.id.btnMaisDespesas);



        if (etNomeEvento.getText().toString().isEmpty())
            etNomeEvento.setError("Campo por preencher");

        etNomeEvento.addTextChangedListener(createEventTextWatcher);


        rvParticipants = findViewById(R.id.rvListParticipants);
        rvExpenses = findViewById(R.id.rvListExpenses);
        listParticipantsViewModel = ViewModelProviders.of(this).get(ListParticipantsViewModel.class);
        listExpensesViewModel = ViewModelProviders.of(this).get(ListExpensesViewModel.class);

        rvParticipants.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvParticipants.setAdapter(new ParticipantsRVAdapter());

        listParticipantsViewModel.setObserver(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                rvParticipants.requestLayout();
            }
        });
        listParticipantsViewModel.addItem(new ParticipantsRepository(""));



        btnMaisParticipantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listParticipantsViewModel.addItem(new ParticipantsRepository(""));
            }
        });

        rvExpenses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvExpenses.setAdapter(new ExpensesRVAdapter());

        listExpensesViewModel.setObserver(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                rvExpenses.requestLayout();
            }
        });
        listExpensesViewModel.addItem("", 0);


        btnMaisDespesas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listExpensesViewModel.addItem("", 0);
            }
        });

    }

    private  TextWatcher createEventTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (etNomeEvento.getText().toString().isEmpty())
                etNomeEvento.setError("Campo por preencher");

            dataEntered();

        }



        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void dataEntered()
    {
        if (etNomeEvento.getText().toString().isEmpty() ||
                listExpensesViewModel.getItem(0).getTitle().isEmpty() ||
                listParticipantsViewModel.getItem(0).getEmail().equalsIgnoreCase("") ||
                String.valueOf(listExpensesViewModel.getItem(0).getValue()).isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(listParticipantsViewModel.getItem(0).getEmail()).matches())
        {
            btnConfirmar.setEnabled(false);
            btnConfirmar.setBackgroundResource(R.drawable.button_disable);
            btnConfirmar.setPadding(60, 10, 60, 10);
        }
        else
        {
            btnConfirmar.setEnabled(true);
            btnConfirmar.setBackgroundResource(R.drawable.button_style);
            btnConfirmar.setPadding(60, 10, 60, 10);
        }
    }

    class ParticipantsRVAdapter extends RecyclerView.Adapter<ParticipantsRVAdapter.ParticipantsViewHolder>{


        @NonNull
        @Override
        public ParticipantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_list_add_participants, parent, false);
            ParticipantsViewHolder pvh = new ParticipantsViewHolder(layout);
            return pvh;
        }

        @Override
        public void onBindViewHolder(@NonNull ParticipantsRVAdapter.ParticipantsViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return listParticipantsViewModel.getSize();
        }

        public class ParticipantsViewHolder extends RecyclerView.ViewHolder {
            EditText etPart;
            public ParticipantsViewHolder(@NonNull View itemView) {
                super(itemView);
                etPart = itemView.findViewById(R.id.etItemEnderecoParticipante);
                if (listParticipantsViewModel.getItem(0).getEmail().equalsIgnoreCase(""))
                    etPart.setError("Campo por preencher");

                etPart.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String participantAdress = etPart.getText().toString();
                        if (!Patterns.EMAIL_ADDRESS.matcher(participantAdress).matches() && !etPart.getText().toString().isEmpty())
                            etPart.setError("Endereço eletronico invalido");
                        listParticipantsViewModel.setItem(getAdapterPosition(), new ParticipantsRepository(etPart.getText().toString()));
                        if (listParticipantsViewModel.getItem(0).getEmail().equalsIgnoreCase("") && getAdapterPosition()==0)
                            etPart.setError("Campo por preencher");

                        dataEntered();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                etPart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus && Patterns.EMAIL_ADDRESS.matcher(etPart.getText().toString()).matches())
                            Toast.makeText(getApplicationContext(),"Pedido enviado", Toast.LENGTH_LONG).show();
                        //pedir verificação
                        //TODO: enviar pedido de verificação ao servidor
                    }
                });
            }


        }
    }

    class ExpensesRVAdapter extends RecyclerView.Adapter<ExpensesRVAdapter.ExpensesViewHolder>{


        @NonNull
        @Override
        public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_list_add_expenses, parent, false);
            ExpensesViewHolder pvh = new ExpensesViewHolder(layout);
            return pvh;
        }

        @Override
        public void onBindViewHolder(@NonNull ExpensesRVAdapter.ExpensesViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return listExpensesViewModel.getSize();
        }

        public class ExpensesViewHolder extends RecyclerView.ViewHolder {
            EditText etExpense;
            EditText etValue;
            public ExpensesViewHolder(@NonNull View itemView) {
                super(itemView);
                etExpense = itemView.findViewById(R.id.etItemNomeDespesa);
                etValue = itemView.findViewById(R.id.etItemValorDespesa);
                if (listExpensesViewModel.getItem(0).getTitle().equalsIgnoreCase(""))
                    etExpense.setError("Campo por preencher");
                if (listExpensesViewModel.getItem(0).getValue() == 0)
                    etValue.setError("Campo por preencher");
                etExpense.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        if (etExpense.getText().toString().isEmpty())
                            etExpense.setError("Campo por preencher");
                        if (!etValue.getText().toString().isEmpty() && !etExpense.getText().toString().isEmpty())
                            listExpensesViewModel.setItem(getAdapterPosition(), new ExpenseRepository(etExpense.getText().toString(), Double.parseDouble(etValue.getText().toString())));
                        else
                            listExpensesViewModel.setItem(getAdapterPosition(), new ExpenseRepository("", 0));
                        dataEntered();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                etValue.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (etValue.getText().toString().isEmpty())
                            etValue.setError("Campo por preencher");
                        if (!etExpense.getText().toString().isEmpty() && !etValue.getText().toString().isEmpty())
                            listExpensesViewModel.setItem(getAdapterPosition(), new ExpenseRepository(etExpense.getText().toString(), Double.parseDouble(etValue.getText().toString())));
                        else
                            listExpensesViewModel.setItem(getAdapterPosition(), new ExpenseRepository("", 0));
                        dataEntered();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

            }


        }
    }


    public void onConfirmar(View view) {

        //TODO: Enviar ao servidor


        Intent intent = getIntent();
        ParticipantsRepository creator = intent.getParcelableExtra("user");


        ArrayList<ParticipantsRepository> participants = listParticipantsViewModel.getList();
        int i = 0;
        while(i<participants.size()){
            if (!Patterns.EMAIL_ADDRESS.matcher(participants.get(i).getEmail()).matches()) {
                participants.remove(i);
            }
            else
                i++;
        }

        ArrayList<ExpenseRepository> expenses = listExpensesViewModel.getList();

        i = 0;
        while(i<expenses.size()){
            if (expenses.get(i).getTitle().equalsIgnoreCase("") || expenses.get(i).getValue() == 0) {
                expenses.remove(i);
            }
            else
                i++;
        }
        EventRepository event = new EventRepository(etNomeEvento.getText().toString(), creator, participants, expenses);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("evento", event);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

