package gps1920.g31.billsplitter.ui.show_event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import gps1920.g31.billsplitter.MainActivity;
import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.data.model.ListParticipantsViewModel;

public class ParticipantsDialog extends AppCompatDialogFragment {
    TextView tv;
    RecyclerView rv;
    ListParticipantsViewModel listParticipantsViewModel;
    String creator;

    public ParticipantsDialog(ListParticipantsViewModel list, String creator) {
        listParticipantsViewModel = list;
        this.creator = creator;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_participants_list, null);
        builder.setView(view);

        tv = view.findViewById(R.id.item_creator);
        tv.setText(creator);
        rv = view.findViewById(R.id.rvFragParticipants);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new ParticipantAdapter(listParticipantsViewModel));

        return builder.create();
    }


}
