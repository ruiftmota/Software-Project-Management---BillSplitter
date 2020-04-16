package gps1920.g31.billsplitter.ui.show_event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.data.ParticipantsRepository;
import gps1920.g31.billsplitter.data.model.ListParticipantsViewModel;

public class ParticipantAdapter extends RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>{
    ListParticipantsViewModel listParticipantsViewModel;
    public ParticipantAdapter(ListParticipantsViewModel listParticipantsViewModel) {
        this.listParticipantsViewModel = listParticipantsViewModel;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_participants, parent, false);
        ParticipantViewHolder pvh = new ParticipantViewHolder(layout);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        ParticipantsRepository participant = listParticipantsViewModel.getItem(position);
        holder.updateUI(participant.getEmail(), participant.isPago());
    }


    @Override
    public int getItemCount() {
        return listParticipantsViewModel.getSize();
    }

    class ParticipantViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        CheckBox cb;
        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_participant);
            cb = itemView.findViewById(R.id.checkboxParticipant);
        }

        public void updateUI(String name, boolean value){
            tv.setText(name);
            cb.setChecked(value);
        }
    }
}