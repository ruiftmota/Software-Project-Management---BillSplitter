package gps1920.g31.billsplitter.data;

import android.os.Parcel;
import android.os.Parcelable;

import gps1920.g31.request_lib.events_information.Expense;

public class ExpenseRepository implements Parcelable {
    private String title;
    private double value;

    public ExpenseRepository(String title, double value) {
        this.title = title;
        this.value = value;
    }

    public ExpenseRepository(Expense expense){
        this.title = expense.getName();
        this.value = expense.getValue();
    }

    protected ExpenseRepository(Parcel in) {
        title = in.readString();
        value = in.readDouble();
    }

    public static final Creator<ExpenseRepository> CREATOR = new Creator<ExpenseRepository>() {
        @Override
        public ExpenseRepository createFromParcel(Parcel in) {
            return new ExpenseRepository(in);
        }

        @Override
        public ExpenseRepository[] newArray(int size) {
            return new ExpenseRepository[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public double getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeDouble(value);
    }
}
