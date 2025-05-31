package com.example.ecoplant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ecoplant.R;

public class DescriptionParcelleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_descriptionparcelle, container, false);

        ImageView parcelleImage = view.findViewById(R.id.parcelleImage);
        EditText sol = view.findViewById(R.id.sol);
        EditText retentionEau = view.findViewById(R.id.retentionEau);
        EditText apportAzote = view.findViewById(R.id.apportAzote);

        String imagePath = null;
        double scoreSoil = 0, scoreWater = 0, scoreNitrogen = 0;
        if (getArguments() != null) {
            imagePath = getArguments().getString("imagePath", null);
            scoreSoil = getArguments().getDouble("scoreSoil", 0);
            scoreWater = getArguments().getDouble("scoreWater", 0);
            scoreNitrogen = getArguments().getDouble("scoreNitrogen", 0);
        }

        // Affichage image principale (file path)
        if (imagePath != null) {
            parcelleImage.setImageURI(android.net.Uri.parse(imagePath));
        }

        // Remplit les champs avec les scores reçus (ex : Amélioration sol, ...)
        sol.setText("Amélioration de la structure du sol : " + scoreSoil);
        retentionEau.setText("Capacité à retenir l'eau dans le sol : " + scoreWater);
        apportAzote.setText("Apport en azote : " + scoreNitrogen);

        return view;
    }

    // Tu peux garder la méthode newInstance si tu veux créer via code :
    public static DescriptionParcelleFragment newInstance(String imagePath, double scoreSoil, double scoreWater, double scoreNitrogen) {
        DescriptionParcelleFragment frag = new DescriptionParcelleFragment();
        Bundle args = new Bundle();
        args.putString("imagePath", imagePath);
        args.putDouble("scoreSoil", scoreSoil);
        args.putDouble("scoreWater", scoreWater);
        args.putDouble("scoreNitrogen", scoreNitrogen);
        frag.setArguments(args);
        return frag;
    }
}
