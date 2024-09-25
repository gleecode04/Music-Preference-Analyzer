package com.example.spotifywrapped;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.data_classes.Stat;

import java.util.List;

public class UserStatAdapter extends RecyclerView.Adapter<UserStatAdapter.StatViewHolder> {
    Context context;
    private List<Stat> stats; // Replace Stat with your actual data class
    public UserStatAdapter(List<Stat> stats) {
        this.stats = stats;
    }

    public UserStatAdapter(List<Stat> stats, Context context){
        this(stats);
        this.context = context;
    }

    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stat, parent, false);
        return new StatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatViewHolder holder, int position) {
        Stat stat = stats.get(position);
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(stat.getTitle());
    }

    @Override
    public int getItemCount() {
        return stats.size();
    }

    static class StatViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvTitle;

        public StatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
