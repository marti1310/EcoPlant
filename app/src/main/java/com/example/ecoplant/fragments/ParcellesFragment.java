package com.example.ecoplant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.ecoplant.R;
import com.example.ecoplant.database.AppDatabase;
import com.example.ecoplant.models.Parcelle;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ParcellesFragment extends Fragment {

    private List<Parcelle> parcelles = new ArrayList<>();
    private Parcelle selectedParcelle = null;
    private ArrayList<String> photoList = new ArrayList<>();

    public static ParcellesFragment newInstance(ArrayList<String> imageList) {
        ParcellesFragment fragment = new ParcellesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("imageList", imageList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooseparcelle, container, false);

        if (getArguments() != null) {
            photoList = getArguments().getStringArrayList("imageList");
            if (photoList == null) photoList = new ArrayList<>();
        }

        // 1. Affichage des parcelles existantes dynamiquement
        LinearLayout parcelleListLayout = view.findViewById(R.id.parcelleListLayout);
        // (Assure-toi d’avoir un LinearLayout d’id parcelleListLayout dans le LinearLayout du ScrollView)

        Executors.newSingleThreadExecutor().execute(() -> {
            List<Parcelle> loaded = AppDatabase.getInstance(requireContext()).parcelleDao().getAllParcelles();
            requireActivity().runOnUiThread(() -> {
                parcelles.clear();
                parcelles.addAll(loaded);
                afficherParcelles(parcelleListLayout, parcelles);
            });
        });

        // 2. Bouton pour prendre une nouvelle photo (ajouter une plante)
        ImageButton addButton = view.findViewById(R.id.ajouter_button);
        addButton.setOnClickListener(v -> {
            CameraFragment frag = CameraFragment.newInstance(photoList);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, frag)
                    .addToBackStack(null)
                    .commit();
        });

        // 3. Bouton Enregistrer : Ajoute les photos à la parcelle sélectionnée ou redirige vers CreationParcelleFragment pour une nouvelle
        MaterialButton saveButton = view.findViewById(R.id.save);
        saveButton.setOnClickListener(v -> {
            if (selectedParcelle != null) {
                ArrayList<String> images = selectedParcelle.getImagePaths();
                if (images == null) images = new ArrayList<>();
                images.addAll(photoList);
                selectedParcelle.setImagePaths(images);

                // Update dans Room
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    db.parcelleDao().update(selectedParcelle);
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Photos ajoutées à la parcelle sélectionnée !", Toast.LENGTH_SHORT).show();
                        requireActivity().onBackPressed(); // Ou naviguer vers le profil
                    });
                });
            } else {
                // Aucune parcelle sélectionnée, va vers création
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, CreationParcelleFragment.newInstance(photoList))
                        .addToBackStack(null)
                        .commit();
            }
        });

        // 4. Bouton retour
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }

    /** Affiche dynamiquement les parcelles dans le layout */
    private void afficherParcelles(LinearLayout parcelleListLayout, List<Parcelle> parcelles) {
        parcelleListLayout.removeAllViews();
        for (Parcelle parcelle : parcelles) {
            View cardView = LayoutInflater.from(getContext()).inflate(R.layout.item_parcelle_card, parcelleListLayout, false);            // Adapte "item_parcelle_card" à la structure de ta carte (voir plus bas)
            CheckBox checkBox = cardView.findViewById(R.id.cardCheckbox);
            ImageView imageView = cardView.findViewById(R.id.cardImage);
            TextView titleView = cardView.findViewById(R.id.cardTitle);

            // Affichage de la première image
            ArrayList<String> images = parcelle.getImagePaths();
            String img = (images != null && !images.isEmpty()) ? images.get(0) : null;
            if (img != null && !img.isEmpty()) {
                imageView.setImageURI(android.net.Uri.fromFile(new java.io.File(img)));
            } else {
                imageView.setImageResource(R.drawable.flower2);
            }

            titleView.setText(parcelle.getName() != null ? parcelle.getName() : "Sans titre");

            checkBox.setChecked(selectedParcelle == parcelle);
            checkBox.setOnClickListener(v -> {
                // Un seul sélectionné à la fois
                selectedParcelle = parcelle;
                notifyAllCheckboxes(parcelleListLayout, parcelle);
            });

            parcelleListLayout.addView(cardView);
        }
    }

    /** Déselectionne toutes les autres checkboxs sauf celle de la parcelle courante */
    private void notifyAllCheckboxes(LinearLayout parcelleListLayout, Parcelle parcelleCourante) {
        for (int i = 0; i < parcelleListLayout.getChildCount(); i++) {
            View child = parcelleListLayout.getChildAt(i);
            CheckBox checkBox = child.findViewById(R.id.cardCheckbox);
            Parcelle cardParcelle = parcelles.get(i);
            checkBox.setChecked(cardParcelle == parcelleCourante);
        }
    }
}
