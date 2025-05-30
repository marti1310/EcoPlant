package com.example.ecoplant.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoplant.R;
import com.example.ecoplant.adapter.ImageAdapter;
import com.example.ecoplant.models.Parcelle;
import com.example.ecoplant.repository.ParcelleRepository;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CreationParcelleFragment extends Fragment {

    private ArrayList<String> imageList = new ArrayList<>();
    private EditText titleEdit, localisationEdit, descriptionEdit;
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public static CreationParcelleFragment newInstance(ArrayList<String> imageList) {
        CreationParcelleFragment fragment = new CreationParcelleFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("imageList", imageList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creationparcelle, container, false);

        // Récupère la liste des photos depuis les arguments
        if (getArguments() != null) {
            imageList = getArguments().getStringArrayList("imageList");
            if (imageList == null) imageList = new ArrayList<>();
        }

        // Affiche les photos
        RecyclerView recyclerView = view.findViewById(R.id.recyclerPhotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ImageAdapter(imageList));

        titleEdit = view.findViewById(R.id.titleParcelle);
        localisationEdit = view.findViewById(R.id.localisationParcelle);
        descriptionEdit = view.findViewById(R.id.parcelleDescription);

        // Back button
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        // Save button
        MaterialButton saveButton = view.findViewById(R.id.save);
        saveButton.setOnClickListener(v -> saveParcelle());

        return view;
    }
    private void saveParcelle() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String name = titleEdit.getText().toString().trim();
        String location = localisationEdit.getText().toString().trim();
        String description = descriptionEdit.getText().toString().trim();
        String creationDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            creationDate = LocalDateTime.now().toString();
        }

        if (name.isEmpty() || location.isEmpty()) {
            Toast.makeText(getContext(), "Titre et localisation obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        Parcelle parcelle = new Parcelle(userId, name, location, creationDate, imageList, description);

        new Thread(() -> {
            ParcelleRepository repo = new ParcelleRepository(requireContext());
            repo.insert(parcelle);

            if (isInternetAvailable()) {
                FirebaseFirestore.getInstance()
                        .collection("parcelles")
                        .add(parcelle)
                        .addOnSuccessListener(doc -> {
                            requireActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Parcelle enregistrée (cloud)", Toast.LENGTH_SHORT).show();
                                // Redirige vers le ProfilFragment
                                requireActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragmentContainer, new com.example.ecoplant.fragments.ProfilFragment())
                                        .commit();
                            });
                        })
                        .addOnFailureListener(e -> {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Erreur Firestore", Toast.LENGTH_SHORT).show());
                        });
            } else {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Parcelle enregistrée (local uniquement)", Toast.LENGTH_SHORT).show();
                    // Redirige vers le ProfilFragment
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new com.example.ecoplant.fragments.ProfilFragment())
                            .commit();
                });
            }
        }).start();
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
