package gps1920.g31.billsplitter.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.util.Objects;
import gps1920.g31.billsplitter.MainActivity;
import gps1920.g31.billsplitter.R;
import gps1920.g31.billsplitter.ui.show_event.ShowEventActivity;

public class MyDialog extends AppCompatDialogFragment {
    private Button confirm;
    private Button cancel;
    private TextView title;
    private TextView description;
    private int titleString;
    private int descString;
    private int listPosition;
    private String activity;

    public MyDialog(int titleString, int descString, int listPosition, String activity) {
        this.titleString = titleString;
        this.descString = descString;
        this.listPosition = listPosition;
        this.activity = activity;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirm_action, null);
        builder.setView(view);

        confirm = view.findViewById(R.id.btnDialogConfirm);
        cancel = view.findViewById(R.id.btnDialogCancel);
        title = view.findViewById(R.id.tvDialogTitle);
        description = view.findViewById(R.id.tvDialogDescription);

        title.setText(titleString);
        description.setText(descString);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equalsIgnoreCase("main"))
                {
                    if (titleString == R.string.dialog_delete_title)
                        ((MainActivity) Objects.requireNonNull(getActivity())).doDeleteClick(listPosition);
                    if(titleString == R.string.dialog_publish_title)
                        ((MainActivity) Objects.requireNonNull(getActivity())).doPublishClick(listPosition);
                    dismiss();
                }
                else if (activity.equalsIgnoreCase("show event"))
                {
                    if (titleString == R.string.dialog_delete_title)
                        ((ShowEventActivity) Objects.requireNonNull(getActivity())).doDeleteClick(listPosition);
                    if(titleString == R.string.dialog_publish_title)
                        ((ShowEventActivity) Objects.requireNonNull(getActivity())).doPublishClick();
                    dismiss();
                }

            }
        });

        return builder.create();
    }


}
