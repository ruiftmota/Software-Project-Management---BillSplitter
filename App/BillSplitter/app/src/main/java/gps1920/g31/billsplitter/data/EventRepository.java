package gps1920.g31.billsplitter.data;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import gps1920.g31.request_lib.events_information.Event;
import gps1920.g31.request_lib.events_information.Expense;
import gps1920.g31.request_lib.events_information.User;

public class EventRepository implements Parcelable {
    private String title;
    private ArrayList<ExpenseRepository> expenses;
    private ArrayList<ParticipantsRepository> participants;
    private ParticipantsRepository creator;
    private boolean publish;

    public EventRepository(String title, ParticipantsRepository creator, ArrayList<ParticipantsRepository> participants, ArrayList<ExpenseRepository> expenses) {
        this.title = title;
        this.creator = creator;
        this.participants = participants;
        this.expenses = expenses;
        this.publish = false;
    }

    public EventRepository(Event event){
        this.title = event.getName();
        this.creator = new ParticipantsRepository(event.getAdministrator());
        ParticipantsRepository participantsRepository;
        for (User user : event.getParticipants())
        {
            participantsRepository = new ParticipantsRepository(user);
            this.participants.add(participantsRepository);
        }
        ExpenseRepository expenseRepository;
        for (Expense expense : event.getExpenses())
        {
            expenseRepository = new ExpenseRepository(expense);
            this.expenses.add(expenseRepository);
        }
    }


    protected EventRepository(Parcel in) {
        title = in.readString();
        expenses = in.createTypedArrayList(ExpenseRepository.CREATOR);
        participants = in.createTypedArrayList(ParticipantsRepository.CREATOR);
        creator = in.readParcelable(ParticipantsRepository.class.getClassLoader());
        publish = in.readByte() != 0;
    }

    public static final Creator<EventRepository> CREATOR = new Creator<EventRepository>() {
        @Override
        public EventRepository createFromParcel(Parcel in) {
            return new EventRepository(in);
        }

        @Override
        public EventRepository[] newArray(int size) {
            return new EventRepository[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public ParticipantsRepository getCreator() {
        return creator;
    }

    public ArrayList<ParticipantsRepository> getParticipants() {
        return participants;
    }

    public int getNumberOfParticipants() {
        return participants.size() + 1;
    }

    public ArrayList<ExpenseRepository> getExpenses() {
        return expenses;
    }

    public void setPublish() {
        this.publish = true;
    }

    public boolean getPublish() {
        return publish;
    }

    public double getValue() {
        double value = 0;
        for (ExpenseRepository expense : expenses) {
            value += expense.getValue();
        }
        return value;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeTypedList(expenses);
        dest.writeTypedList(participants);
        dest.writeParcelable(creator, flags);
        dest.writeByte((byte) (publish ? 1 : 0));
    }
}

