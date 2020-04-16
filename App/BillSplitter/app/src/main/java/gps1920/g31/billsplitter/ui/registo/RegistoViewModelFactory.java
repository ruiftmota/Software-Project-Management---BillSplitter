package gps1920.g31.billsplitter.ui.registo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import gps1920.g31.billsplitter.data.LoginRepository;
import gps1920.g31.billsplitter.data.RegistoRepository;
import gps1920.g31.billsplitter.data.server_interface.ServerInterface;

class RegistoViewModelFactory implements ViewModelProvider.Factory
{
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        if (modelClass.isAssignableFrom(RegistoViewModel.class)) {
            return (T) new RegistoViewModel(RegistoRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }

    }
}
