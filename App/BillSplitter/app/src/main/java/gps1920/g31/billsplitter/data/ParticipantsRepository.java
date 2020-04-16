package gps1920.g31.billsplitter.data;

import android.os.Parcel;
import android.os.Parcelable;

import gps1920.g31.request_lib.events_information.User;

public class ParticipantsRepository implements Parcelable {
    private String name;
    private String surname;
    private String email;
    private boolean pago;

    public ParticipantsRepository(String name, String surname, String email)
    {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.pago = false;
    }

    public ParticipantsRepository(User user)
    {
        this.name = user.getFirst_name();
        this.surname = user.getLast_name();
        this.email = user.getEmail();
    }
    public ParticipantsRepository(String email){
        this.email = email;
        this.pago = false;
    }


    protected ParticipantsRepository(Parcel in) {
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        pago = in.readByte() != 0;
    }

    public static final Creator<ParticipantsRepository> CREATOR = new Creator<ParticipantsRepository>() {
        @Override
        public ParticipantsRepository createFromParcel(Parcel in) {
            return new ParticipantsRepository(in);
        }

        @Override
        public ParticipantsRepository[] newArray(int size) {
            return new ParticipantsRepository[size];
        }
    };

    public String getFullName()
    {
        return name + " " + surname;
    }

    public String getEmail()
    {
        return email;
    }

    public boolean isPago() {
        return pago;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(email);
        dest.writeByte((byte) (pago ? 1 : 0));
    }
}
