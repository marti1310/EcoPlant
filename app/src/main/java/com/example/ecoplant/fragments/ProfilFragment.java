package com.example.ecoplant.fragments;

import static androidx.camera.core.impl.utils.ContextUtil.getApplicationContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ecoplant.R;
import com.example.ecoplant.activities.MainActivity;
import com.example.ecoplant.database.AppDatabase;
import com.example.ecoplant.fragments.HistoriqueFragment;
import com.example.ecoplant.fragments.GestionCompteFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ecoplant.models.Utilisateur;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        MaterialButton historiqueButton = view.findViewById(R.id.historiqueButton);
        MaterialButton gestionCompteButton = view.findViewById(R.id.gestioncompteButton);
        MaterialButton signoutButton = view.findViewById(R.id.signoutButton);
        TextView pseudoTextView = view.findViewById(R.id.pseudo);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            Utilisateur utilisateur = db.utilisateurDao().findByUid(uid);


            requireActivity().runOnUiThread(() -> {
                if (utilisateur != null && utilisateur.getUsername() != null) {
                    pseudoTextView.setText(utilisateur.getUsername());
                } else {
                    pseudoTextView.setText("Pseudo inconnu");
                }
            });
        }).start();



        historiqueButton.setOnClickListener(v -> {
            // Remplace le fragment courant par HistoriqueFragment
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new HistoriqueFragment())
                    .addToBackStack(null)
                    .commit();
        });

        gestionCompteButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new GestionCompteFragment())
                    .addToBackStack(null)
                    .commit();
        });

        signoutButton.setOnClickListener(v -> {
            // Déconnexion (retour à MainActivity)
            Intent intent = new Intent(requireContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}
