package com.example.urban_issue_reporter_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.urban_issue_reporter_mobile.model.Reclamation;

import java.util.List;

public class ReclamationAdapter extends RecyclerView.Adapter<ReclamationAdapter.ViewHolder> {

    private List<Reclamation> reclamations;

    public void setReclamations(List<Reclamation> list) {
        this.reclamations = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reclamation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reclamation r = reclamations.get(position);
        holder.title.setText(r.getTitle());
        holder.date.setText(r.getDate());
    }

    @Override
    public int getItemCount() {
        return reclamations != null ? reclamations.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, date;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.titleText);
            date = view.findViewById(R.id.dateText);
        }
    }
}
