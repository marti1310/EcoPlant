package com.example.ecoplant.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ecoplant.R;
import com.example.ecoplant.database.AppDatabase;
import com.example.ecoplant.models.Parcelle;
import java.util.List;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.HistoriqueViewHolder> {

    public interface OnDeleteListener {
        void onDelete(Parcelle parcelle);
    }

    private final List<Parcelle> parcelles;
    private final OnDeleteListener deleteListener;
    private final Context context;
    private final OnItemClickListener clickListener;

    public HistoriqueAdapter(Context context, List<Parcelle> parcelles, OnDeleteListener deleteListener, OnItemClickListener clickListener) {
        this.context = context;
        this.parcelles = parcelles;
        this.deleteListener = deleteListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public HistoriqueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(context);
            List<Parcelle> parcelles = db.parcelleDao().getAllParcelles();
            for (Parcelle p : parcelles) {
                List<String> images = p.getImagePaths();
                Log.d("DEBUG_IMAGES", "Parcelle " + p.getName() + ": " + images);
            }
        }).start();
        View view = LayoutInflater.from(context).inflate(R.layout.item_historique, parent, false);
        return new HistoriqueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriqueViewHolder holder, int position) {
        Parcelle p = parcelles.get(position);
        holder.cardTitle.setText(p.getName());
        holder.cardDate.setText(p.getCreationDate());

        String imagePath = null;
        if (p.getImagePaths() != null && !p.getImagePaths().isEmpty()) {
            imagePath = p.getImagePaths().get(0);
        }

        Glide.with(context)
                .load(imagePath != null ? imagePath : R.drawable.flower2)
                .placeholder(R.drawable.flower2)
                .into(holder.cardImage);

        holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(p));
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onItemClick(p); // Passe la parcelle au fragment qui va gérer la suite
            }
        });

    }


    @Override
    public int getItemCount() {
        return parcelles.size();
    }

    public static class HistoriqueViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage;
        TextView cardTitle;
        TextView cardDate;
        ImageButton deleteButton;

        public HistoriqueViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImage = itemView.findViewById(R.id.cardImage);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardDate = itemView.findViewById(R.id.cardDate);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Parcelle parcelle);
    }

}
