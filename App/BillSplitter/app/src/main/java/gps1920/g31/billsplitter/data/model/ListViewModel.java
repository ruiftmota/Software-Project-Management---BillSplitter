package gps1920.g31.billsplitter.data.model;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import gps1920.g31.billsplitter.data.EventRepository;
import gps1920.g31.billsplitter.data.ExpenseRepository;
import gps1920.g31.billsplitter.data.ParticipantsRepository;

public class ListViewModel extends ViewModel {
    ArrayList<EventRepository> list = new ArrayList<>();
    MutableLiveData<Boolean> changed = new MutableLiveData<>();

    public void setObserver(LifecycleOwner lo, Observer<Boolean> observer){
        changed.observe(lo, observer);
    }

    public void addItem (String title, ParticipantsRepository creator, ArrayList<ParticipantsRepository> participants, ArrayList<ExpenseRepository> expenses){
        list.add(new EventRepository(title, creator, participants, expenses));
        changed.postValue(true);
    }

    public void setItem (int position, EventRepository event){
        list.set(position, event);
        changed.postValue(true);
    }

    public void setPublish(int position){
        list.get(position).setPublish();
    }

    public void removeItem (int position){
        list.remove(position);
        changed.postValue(true);
    }


    public int getSize(){
        return list.size();
    }

    public ArrayList<EventRepository> getList() {
        return list;
    }

    public EventRepository getItem(int i){
        return list.get(i);
    }

    public void setList(ArrayList<EventRepository> list) {
        this.list = list;
    }
}
