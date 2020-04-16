package gps1920.g31.billsplitter.ui.show_event;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.data.EventRepository;
import gps1920.g31.billsplitter.data.ExpenseRepository;
import gps1920.g31.billsplitter.data.ParticipantsRepository;
import gps1920.g31.billsplitter.data.model.ListExpensesViewModel;
import gps1920.g31.billsplitter.data.model.ListParticipantsViewModel;
import gps1920.g31.billsplitter.ui.MyDialog;
import gps1920.g31.billsplitter.ui.edit_event.EditEventActivity;

public class ShowEventActivity extends AppCompatActivity {
    EventRepository event;
    ParticipantsRepository user;
    TextView tvTitle;
    TextView tvTotalParticipants;
    TextView tvExpense;
    CheckBox cbPaid;
    TextView tvLock;
    ImageButton btnPublish;
    ImageButton btnDelete;
    ImageButton btnEdit;
    boolean eventEdited;
    int position;
    boolean published;

    RecyclerView rvExpenses;
    ListExpensesViewModel listExpensesViewModel;

    RecyclerView rvParticipants;
    ListParticipantsViewModel listParticipantsViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        eventEdited = false;
        Intent intent = getIntent();
        event = intent.getParcelableExtra("item");
        user = intent.getParcelableExtra("user");
        position = intent.getIntExtra("position", -1);
        published = event.getPublish();


        //Apresentar titulo do evento
        tvTitle = findViewById(R.id.tvEventTitle);
        tvTitle.setText(event.getTitle());

        //Apresentar numero total de participantes, incluindo o criador
        tvTotalParticipants = findViewById(R.id.tvTotalParticipants);
        String str = event.getNumberOfParticipants() + "\nPessoas";
        tvTotalParticipants.setText(str);

        //Apresentar o total da despesa em €
        tvExpense = findViewById(R.id.tvTotalExpense);


        cbPaid = findViewById(R.id.checkBoxPaid);
        tvLock = findViewById(R.id.tvLock);
        btnPublish = findViewById(R.id.btnPublish);
        btnDelete = findViewById(R.id.btnDeleteEvent);
        btnEdit = findViewById(R.id.btnEditEvent);


        //Apresentar dados exclusivos do criador:
        if(event.getCreator().getEmail().equalsIgnoreCase(user.getEmail()))
        {
            cbPaid.setVisibility(View.GONE);
            String strTotalExpense = "Despesa:\n" + event.getValue() + "€";
            tvExpense.setText(strTotalExpense);
        }
        else
        {
            double individualExpense = event.getValue() / (double)event.getNumberOfParticipants();
            String strIndividualExpense = "Valor a pagar:\n" + individualExpense + "€";
            tvExpense.setText(strIndividualExpense);
            tvLock.setVisibility(View.GONE);
            btnPublish.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);

        }
        if (published)
        {
            tvLock.setVisibility(View.GONE);
            btnPublish.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(R.string.dialog_delete_title, R.string.dialog_description, position);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);
                intent.putExtra("item", event);
                intent.putExtra("user", user);
                startActivityForResult(intent, 4);
            }
        });

        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(R.string.dialog_publish_title, R.string.dialog_description, position);
            }
        });

        rvExpenses = findViewById(R.id.rvShowExpenses);
        listExpensesViewModel = ViewModelProviders.of(this).get(ListExpensesViewModel.class);

        rvExpenses.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        rvExpenses.setAdapter(new ExpenseAdapter());

        listExpensesViewModel.setObserver(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                rvExpenses.requestLayout();
            }
        });


        for (ExpenseRepository exR: event.getExpenses())
        {
            listExpensesViewModel.addItem(exR.getTitle(),exR.getValue());
        }

        listParticipantsViewModel = ViewModelProviders.of(this).get(ListParticipantsViewModel.class);

        for (ParticipantsRepository partR: event.getParticipants())
        {
            listParticipantsViewModel.addItem(partR);
        }


    }
    public void openDialog (int title, int description, int position) {
        MyDialog dialog = new MyDialog(title, description, position, "show event");
        dialog.show(getSupportFragmentManager(),"dialogAlert");
    }

    public void doDeleteClick(int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("position", position);
        setResult(3, resultIntent);
        finish();
    }

    public void doPublishClick() {
        tvLock.setVisibility(View.GONE);
        btnPublish.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);
        event.setPublish();
        eventEdited = true;
    }

    public void onParticipants(View view) {
        ParticipantsDialog pDialog = new ParticipantsDialog(listParticipantsViewModel, user.getEmail());
        pDialog.show(getSupportFragmentManager(), "participantsDialog");
    }



    class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>{

        @NonNull
        @Override
        public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_list_show_expenses, parent, false);

            ExpenseViewHolder evh = new ExpenseViewHolder(layout);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
            ExpenseRepository expense = listExpensesViewModel.getItem(position);

            holder.updateUI(expense.getTitle(),expense.getValue() + "€", String.valueOf(position + 1));
        }

        @Override
        public int getItemCount() {
            return listExpensesViewModel.getSize();
        }

        class ExpenseViewHolder extends RecyclerView.ViewHolder{
            TextView tvName, tvValue, tvNumber;

            public ExpenseViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tvExpenseName);
                tvNumber = itemView.findViewById(R.id.tvExpenseNumber);
                tvValue = itemView.findViewById(R.id.tvExpenseValue);
            }

            public void updateUI(String name, String value, String number){
                 tvName.setText(name);
                 tvValue.setText(value);
                 tvNumber.setText(number);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4)
        {
            if (resultCode == RESULT_OK)
            {
                assert data != null;
                EventRepository event = data.getParcelableExtra("evento");
                this.event = event;
                assert event != null;
                listExpensesViewModel.setList(event.getExpenses());
                listParticipantsViewModel.setList(event.getParticipants());
                Objects.requireNonNull(rvExpenses.getAdapter()).notifyDataSetChanged();
                tvTitle.setText(event.getTitle());
                if(!event.getCreator().getEmail().equalsIgnoreCase(user.getEmail()))
                {
                    double individualExpense = event.getValue() / (double)event.getNumberOfParticipants();
                    String strIndividualExpense = "Valor a pagar:\n" + individualExpense + "€";
                    tvExpense.setText(strIndividualExpense);
                }
                String strTotalExpense = "Despesa:\n" + event.getValue() + "€";
                tvExpense.setText(strTotalExpense);
                String str = event.getNumberOfParticipants() + "\nPessoas";
                tvTotalParticipants.setText(str);
                eventEdited = true;
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (eventEdited) {

            Intent resultIntent = new Intent();
            resultIntent.putExtra("evento", event);
            resultIntent.putExtra("position", position);
            setResult(RESULT_OK, resultIntent);
        }
        super.onBackPressed();
    }
}
