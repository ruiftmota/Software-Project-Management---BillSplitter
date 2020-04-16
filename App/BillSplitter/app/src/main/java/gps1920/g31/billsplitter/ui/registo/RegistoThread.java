package gps1920.g31.billsplitter.ui.registo;

public class RegistoThread extends Thread {
    private RegistoViewModel registoViewModel;
    private  String name;
    private String surname;
    private String email;
    private String password;

    public RegistoThread(RegistoViewModel registoViewModel, String name, String surname, String email, String password) {
        this.registoViewModel = registoViewModel;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    @Override
    public void run() {
        registoViewModel.registo(name, surname, email, password);

        super.run();
    }
}
