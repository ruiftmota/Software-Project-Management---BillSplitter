package gps1920.g31.billsplitter.data.model;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import gps1920.g31.billsplitter.data.ExpenseRepository;

public class ListExpensesViewModel extends ViewModel {
    ArrayList<ExpenseRepository> list = new ArrayList<>();
    MutableLiveData<Boolean> changed = new MutableLiveData<>();

    public void setObserver(LifecycleOwner lo, Observer<Boolean> observer){
        changed.observe(lo, observer);
    }

    public void addItem(String name, double value){
        list.add(new ExpenseRepository(name, value));
        changed.postValue(true);
    }

    public void setItem(int position, ExpenseRepository expense){
        list.set(position, expense);
    }

    public ArrayList<ExpenseRepository> getList() {
        return list;
    }

    public int getSize(){
        return list.size();
    }

    public ExpenseRepository getItem(int i){
        return list.get(i);
    }

    public void setList(ArrayList<ExpenseRepository> list) {
        this.list = list;
    }
}
