package zfani.assaf.bringg.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zfani.assaf.bringg.R;
import zfani.assaf.bringg.viewmodels.MainViewModel;

public class WorkAddressFragment extends Fragment {

    @BindView(R.id.tilAddress)
    TextInputLayout tilAddress;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_address, container, false);
        ButterKnife.bind(this, view);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        return view;
    }

    @OnClick(R.id.btnConfigure)
    void configureWorkAddress() {
        mainViewModel.configureWorkLocation(Objects.requireNonNull(tilAddress.getEditText()).getText().toString());
    }
}
