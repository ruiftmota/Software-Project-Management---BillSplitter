package gps1920.g31.billsplitter.data.model;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import gps1920.g31.billsplitter.data.ParticipantsRepository;

public class ListParticipantsViewModel extends ViewModel {
    ArrayList<ParticipantsRepository> list = new ArrayList<>();
    MutableLiveData<Boolean> changed = new MutableLiveData<>();

    public void setObserver(LifecycleOwner lo, Observer<Boolean> observer){
        changed.observe(lo, observer);
    }

    public void addItem (ParticipantsRepository participant){
        list.add(participant);
        changed.postValue(true);
    }

    public ArrayList<ParticipantsRepository> getList() {
        return list;
    }


    public void setItem(int pos, ParticipantsRepository str){
        list.set(pos, str);
    }

    public int getSize(){
        return list.size();
    }

    public void setList(ArrayList<ParticipantsRepository> list) {
        this.list = list;
    }

    public ParticipantsRepository getItem(int i){
        return list.get(i);
    }


}
