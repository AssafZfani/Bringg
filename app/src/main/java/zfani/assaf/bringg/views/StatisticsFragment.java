package zfani.assaf.bringg.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.bringg.R;
import zfani.assaf.bringg.adapters.PresenceAdapter;
import zfani.assaf.bringg.viewmodels.MainViewModel;

public class StatisticsFragment extends Fragment {

    @BindView(R.id.rvPresences)
    RecyclerView rvPresences;
    @BindView(R.id.tvNoResults)
    View tvNoResults;
    private MainViewModel mainViewModel;
    private PresenceAdapter presenceAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        initView();
        return view;
    }

    private void initView() {
        rvPresences.setAdapter(presenceAdapter = new PresenceAdapter());
        mainViewModel.getPresenceList().observe(this, presences -> {
            tvNoResults.setVisibility(presences.isEmpty() ? View.VISIBLE : View.GONE);
            presenceAdapter.submitList(presences);
        });
    }
}
