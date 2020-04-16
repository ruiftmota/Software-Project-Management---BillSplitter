package gps1920.g31.billsplitter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import gps1920.g31.billsplitter.data.EventRepository;
import gps1920.g31.billsplitter.data.ParticipantsRepository;
import gps1920.g31.billsplitter.data.model.ListViewModel;
import gps1920.g31.billsplitter.data.model.LoggedInUser;
import gps1920.g31.billsplitter.threads.ThreadForRequests;
import gps1920.g31.billsplitter.ui.MyDialog;
import gps1920.g31.billsplitter.ui.create_event.CreateEventActivity;
import gps1920.g31.billsplitter.ui.edit_event.EditEventActivity;
import gps1920.g31.billsplitter.ui.login.LoginActivity;
import gps1920.g31.billsplitter.ui.show_event.ShowEventActivity;


/**
 * <h1>List of all events in which the user participates!</h1>
 * This class shows all events in which the user is integrated
 *
 * @author  Paulo Dias
 * @version 1.0
 * @since   27-09-2019
 */

public class MainActivity extends AppCompatActivity {
    ListViewModel listViewModel;
    RecyclerView recyclerView;
    TextView tvEmptyList;
    LoggedInUser lUser;
    ParticipantsRepository user;
    ThreadForRequests threadForRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();

        //Autenticação
        if(extras !=null)
        {
            lUser = new LoggedInUser(extras.getString("email"), extras.getString("name"), extras.getString("surname"));
            user = new ParticipantsRepository(lUser.getName(), lUser.getSurname(), lUser.getUserId());
        }
        if(lUser == null)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }


        //Inicio da apresentação da lista de eventos
        tvEmptyList = findViewById(R.id.tvEmptyList);
        recyclerView = findViewById(R.id.rvList);
        listViewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        if (listViewModel.getSize()==0)
        {
            tvEmptyList.setVisibility(View.VISIBLE);
        }
        else
            tvEmptyList.setVisibility(View.INVISIBLE);


        //Pedido para obter lista de eventos em que o user pertence
        threadForRequests = new ThreadForRequests(lUser.getUserId());
        threadForRequests.start();
        listViewModel.setList(threadForRequests.getListOfEvents());

        /*
        //TODO: Isto é apenas para efeitos de teste
        if (listViewModel.getSize()==0)
        {
            //Exemplo de lista de participants
            ArrayList<ParticipantsRepository> participantsTeste = new ArrayList<>();
            ParticipantsRepository part1 = new ParticipantsRepository("gps@gmail.com");
            ParticipantsRepository part2 = new ParticipantsRepository("gps@gmail2.com");
            ParticipantsRepository part3 = new ParticipantsRepository("gps@gmail3.com");
            ParticipantsRepository part4 = new ParticipantsRepository("gps@gmail4.com");


            participantsTeste.add(part1); participantsTeste.add(part2); participantsTeste.add(part3); participantsTeste.add(part4);

            //Exemplo de despesas
            ArrayList<ExpenseRepository> expenses = new ArrayList<>();
            ExpenseRepository ex1 = new ExpenseRepository("nomeDespesa1",35.5);
            ExpenseRepository ex2 = new ExpenseRepository("nomeDespesa2",100);
            ExpenseRepository ex3 = new ExpenseRepository("nomeDespesa3",20);
            expenses.add(ex1); expenses.add(ex2); expenses.add(ex3); expenses.add(ex3);

            //Exemplo de participante
            ParticipantsRepository creatorTeste = new ParticipantsRepository("Ja","Foste","jafosteteste@gps.com");
            listViewModel.addItem("Evento de Teste", creatorTeste, participantsTeste, expenses);

            //Exemmplo de criador
            listViewModel.addItem("Evento de Teste Criador", user, participantsTeste, expenses);
        }

         */


        // Criação de um novo evento
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), CreateEventActivity.class);
                intent.putExtra("user", user);
                startActivityForResult(intent, 1);

            }
        });


        //Criação da lista de eventos
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new MyRVAdapter());
        listViewModel.setObserver(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                recyclerView.requestLayout();
            }
        });


    }


    //Método para abrir uma janela de dialogo
    public void openDialog (int title, int description, int position) {
        MyDialog dialog = new MyDialog(title, description, position, "main");
        dialog.show(getSupportFragmentManager(),"dialogAlert");
    }

    //Método para eliminar um evento da lista
    public void doDeleteClick(int position) {
        Toast.makeText(getApplicationContext(), "Apagado", Toast.LENGTH_SHORT).show();
        listViewModel.removeItem(position);

        recyclerView.getAdapter().notifyDataSetChanged();
    }

    //Método para publicar um evento da lista
    public void doPublishClick(int position) {
        Toast.makeText(getApplicationContext(), "Publicado", Toast.LENGTH_SHORT).show();
        listViewModel.setPublish(position);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    //Adaptador da RecyclerView para a lista de eventos
    class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View layout = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_list_event,parent,false);

            MyViewHolder mvh = new MyViewHolder(layout);
            return mvh;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            EventRepository event = listViewModel.getItem(position);
            boolean creator = true;
            if (!event.getCreator().getEmail().equalsIgnoreCase(lUser.getUserId()))
                creator = false;

            holder.updateUI(event.getTitle(),
                    event.getValue(),
                    event.getNumberOfParticipants(),
                    creator);


        }

        @Override
        public int getItemCount() {
            return listViewModel.getSize();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tvTitle, tvMessage, tvUsersNumber, tvLock;
            ImageButton btnDelete, btnEdit, btnPublish;
            public MyViewHolder(@NonNull View itemView){
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvMessage = itemView.findViewById(R.id.tvValue);
                tvUsersNumber = itemView.findViewById(R.id.tvTotalParticipntsCardView);
                tvLock = itemView.findViewById(R.id.tvLockList);
                btnEdit = itemView.findViewById(R.id.btnEdit);
                btnDelete = itemView.findViewById(R.id.btnDelete);
                btnPublish = itemView.findViewById(R.id.btnPublishList);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ShowEventActivity.class);
                        intent.putExtra("item", listViewModel.getItem(getAdapterPosition()));
                        intent.putExtra("position", getAdapterPosition());
                        intent.putExtra("user", user);
                        startActivityForResult(intent, 3);
                    }
                });


                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog(R.string.dialog_delete_title, R.string.dialog_description, getAdapterPosition());

                    }
                });

                btnPublish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openDialog(R.string.dialog_publish_title, R.string.dialog_description, getAdapterPosition());
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), EditEventActivity.class);
                        intent.putExtra("item", listViewModel.getItem(getAdapterPosition()));
                        intent.putExtra("position", getAdapterPosition());
                        intent.putExtra("user", user);
                        startActivityForResult(intent, 2);
                    }
                });
            }

            //Método para atualizar os itens da Recycler View
            public  void updateUI(String t, double m, double nUsers, boolean creator){

                if (!creator)
                {
                    btnDelete.setVisibility(View.GONE);
                    btnEdit.setVisibility(View.GONE);
                    tvLock.setVisibility(View.GONE);
                    btnPublish.setVisibility(View.GONE);
                    tvMessage.setText("Valor a pagar:\n" + m/nUsers);
                }
                else if(listViewModel.getItem(getAdapterPosition()).getPublish())
                {
                    btnEdit.setVisibility(View.GONE);
                    tvLock.setVisibility(View.GONE);
                    btnPublish.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.VISIBLE);
                    tvMessage.setText("Despesa:\n" + m);

                }
                else
                {
                    btnEdit.setVisibility(View.VISIBLE);
                    tvLock.setVisibility(View.VISIBLE);
                    btnPublish.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.VISIBLE);
                    tvMessage.setText("Despesa:\n" + m);
                }




                tvTitle.setText(t);
                tvUsersNumber.setText((int)nUsers + "\nPessoas");
                tvEmptyList.setVisibility(View.INVISIBLE);

            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                assert data != null;
                EventRepository event = data.getParcelableExtra("evento");

                assert event != null;
                listViewModel.addItem(event.getTitle(), event.getCreator(), event.getParticipants(), event.getExpenses());

            }
        }

        if (requestCode == 2)
        {
            if (resultCode == RESULT_OK)
            {
                assert data != null;
                int position = data.getIntExtra("position", -1);
                EventRepository event = data.getParcelableExtra("evento");
                listViewModel.setItem(position, event);
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            }
        }
        if (requestCode == 3)
        {
            if (resultCode == RESULT_OK)
            {
                assert data != null;
                int position = data.getIntExtra("position", -1);
                EventRepository event = data.getParcelableExtra("evento");
                listViewModel.setItem(position, event);
                Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
            }
            else if(resultCode == 3)
            {
                assert data != null;
                int position = data.getIntExtra("position", -1);
                doDeleteClick(position);
            }

        }
    }

}
