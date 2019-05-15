package zfani.assaf.bringg.adapters;

import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.bringg.R;
import zfani.assaf.bringg.models.Presence;

public class PresenceAdapter extends ListAdapter<Presence, PresenceAdapter.PresenceViewHolder> {

    public PresenceAdapter() {
        super(new DiffUtil.ItemCallback<Presence>() {
            @Override
            public boolean areItemsTheSame(@NonNull Presence oldItem, @NonNull Presence newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Presence oldItem, @NonNull Presence newItem) {
                return oldItem.getStartTs() == newItem.getStartTs() && oldItem.getEndTs() == newItem.getEndTs();
            }
        });
    }

    @NonNull
    @Override
    public PresenceAdapter.PresenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PresenceViewHolder(View.inflate(parent.getContext(), android.R.layout.simple_list_item_1, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PresenceAdapter.PresenceViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Presence presence = getItem(position);
        Time time = new Time(Time.getCurrentTimezone());
        time.set(presence.getStartTs());
        holder.tvText.setText(new StringBuilder(context.getString(R.string.date) + " " + time.monthDay + "/" + time.month + "/" + time.year +
                context.getString(R.string.duration) + " " + (presence.getEndTs() - presence.getStartTs()) / 1000 + " " + context.getString(R.string.seconds)));
    }

    class PresenceViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1)
        TextView tvText;

        PresenceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
