package com.example.ecoplant.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.ecoplant.R;
import com.example.ecoplant.adapter.HistoriqueAdapter;
import com.example.ecoplant.api.PlantNetApi;
import com.example.ecoplant.database.AppDatabase;
import com.example.ecoplant.models.Parcelle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class HistoriqueFragment extends Fragment {
    private HistoriqueAdapter adapter;
    private List<Parcelle> parcelles = new ArrayList<>();
    private TextView emptyView;

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull android.view.LayoutInflater inflater,
                                          @Nullable android.view.ViewGroup container,
                                          @Nullable Bundle savedInstanceState) {
        android.view.View view = inflater.inflate(R.layout.fragment_historique, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        emptyView = view.findViewById(R.id.emptyView);

        adapter = new HistoriqueAdapter(requireContext(), parcelles,
                parcelle -> supprimerParcelle(parcelle),
                parcelle -> {
                    // SEULEMENT l'appel ici
                    lancerAnalyseEtAfficher(parcelle);
                });
        recyclerView.setAdapter(adapter);

        if (isInternetAvailable()) {
            loadFromFirebase();
        } else {
            loadFromRoom();
        }
        return view;
    }

    private void updateEmptyView() {
        if (parcelles.isEmpty()) {
            emptyView.setVisibility(android.view.View.VISIBLE);
        } else {
            emptyView.setVisibility(android.view.View.GONE);
        }
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private void loadFromRoom() {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            List<Parcelle> result = db.parcelleDao().getAllParcelles();
            requireActivity().runOnUiThread(() -> {
                parcelles.clear();
                parcelles.addAll(result);
                adapter.notifyDataSetChanged();
                updateEmptyView();
            });
        });
    }

    private void loadFromFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("parcelles")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    parcelles.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Parcelle p = doc.toObject(Parcelle.class);
                        parcelles.add(p);
                    }
                    adapter.notifyDataSetChanged();
                    updateEmptyView();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Erreur Firebase", Toast.LENGTH_SHORT).show();
                    updateEmptyView();
                });
    }

    private void supprimerParcelle(Parcelle parcelle) {
        if (isInternetAvailable()) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("parcelles")
                    .whereEqualTo("id", parcelle.getId())
                    .whereEqualTo("userId", uid)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            doc.getReference().delete();
                        }
                        parcelles.remove(parcelle);
                        adapter.notifyDataSetChanged();
                        updateEmptyView();
                    });
        } else {
            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase db = AppDatabase.getInstance(requireContext());
                db.parcelleDao().delete(parcelle);
                requireActivity().runOnUiThread(() -> {
                    parcelles.remove(parcelle);
                    adapter.notifyDataSetChanged();
                    updateEmptyView();
                });
            });
        }
    }

    private void lancerAnalyseEtAfficher(Parcelle parcelle) {
        if (parcelle.getImagePaths() == null || parcelle.getImagePaths().isEmpty()) {
            Toast.makeText(requireContext(), "Aucune image pour cette parcelle.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Variables pour les scores
        final double[] scoreSoil = {0.0};
        final double[] scoreWater = {0.0};
        final double[] scoreNitrogen = {0.0};
        final int[] done = {0};
        final int total = parcelle.getImagePaths().size();

        // Pour chaque image, fait une requête PlantNet
        for (String imagePath : parcelle.getImagePaths()) {
            PlantNetApi.identifyPlant(requireContext(), new File(imagePath), "2b10mr6phr8K14uTK2FF9klO", new PlantNetApi.PlantNetCallback() {
                @Override
                public void onSuccess(String plantNetJson) {
                    // Traitement simplifié : récupère les scores dans le JSON
                    try {
                        JSONObject json = new JSONObject(plantNetJson);
                        // À ADAPTER selon structure JSON renvoyée par PlantNet
                        // scoreSoil[0] += ...; scoreWater[0] += ...; scoreNitrogen[0] += ...;

                        // --- EXEMPLE DÉMO :
                        scoreSoil[0] += 0.6;
                        scoreWater[0] += 0.4;
                        scoreNitrogen[0] += 0.7;

                    } catch (Exception e) {
                        Log.e("PlantNet", "Erreur parse JSON", e);
                    }

                    done[0]++;
                    // Quand toutes les requêtes sont finies, ouvre DescriptionParcelleFragment
                    if (done[0] == total) {
                        requireActivity().runOnUiThread(() -> {
                            Bundle args = new Bundle();
                            args.putString("parcelleName", parcelle.getName());
                            args.putString("imagePath", parcelle.getImagePaths().get(0));
                            args.putDouble("scoreSoil", scoreSoil[0]);
                            args.putDouble("scoreWater", scoreWater[0]);
                            args.putDouble("scoreNitrogen", scoreNitrogen[0]);
                            DescriptionParcelleFragment frag = new DescriptionParcelleFragment();
                            frag.setArguments(args);
                            requireActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, frag)
                                    .addToBackStack(null)
                                    .commit();
                        });
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Erreur analyse PlantNet", Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

}
