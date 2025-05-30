package com.example.ecoplant.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecoplant.R;
import com.example.ecoplant.models.Parcelle;
import java.util.List;

public class ParcelleAdapter extends RecyclerView.Adapter<ParcelleAdapter.ParcelleViewHolder> {

    public interface OnParcelleClickListener {
        void onParcelleSelected(Parcelle parcelle);
    }

    private final List<Parcelle> parcelles;
    private Parcelle selectedParcelle;
    private final OnParcelleClickListener listener;

    public ParcelleAdapter(List<Parcelle> parcelles, OnParcelleClickListener listener) {
        this.parcelles = parcelles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ParcelleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parcelle, parent, false);
        return new ParcelleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelleViewHolder holder, int position) {
        Parcelle parcelle = parcelles.get(position);
        holder.title.setText(parcelle.getName());
        holder.image.setImageResource(parcelle.getId());
        holder.checkBox.setChecked(parcelle == selectedParcelle);

        holder.card.setOnClickListener(v -> {
            selectedParcelle = parcelle;
            notifyDataSetChanged();
            if (listener != null) listener.onParcelleSelected(parcelle);
        });
        holder.checkBox.setOnClickListener(v -> {
            selectedParcelle = parcelle;
            notifyDataSetChanged();
            if (listener != null) listener.onParcelleSelected(parcelle);
        });
    }

    @Override
    public int getItemCount() {
        return parcelles.size();
    }

    public void setSelectedParcelle(Parcelle parcelle) {
        this.selectedParcelle = parcelle;
        notifyDataSetChanged();
    }

    static class ParcelleViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        CheckBox checkBox;
        ImageView image;
        TextView title;

        ParcelleViewHolder(View view) {
            super(view);
            card = view.findViewById(R.id.cardView);
            checkBox = view.findViewById(R.id.cardCheckbox);
            image = view.findViewById(R.id.cardImage);
            title = view.findViewById(R.id.cardTitle);
        }
    }
}
