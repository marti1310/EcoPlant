package com.example.ecoplant.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ecoplant.R;
import com.example.ecoplant.adapter.ImageAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AjoutPlanteFragment extends Fragment {

    private static final String ARG_IMAGE_LIST = "imageList";
    private ArrayList<String> imageList = new ArrayList<>();

    public static AjoutPlanteFragment newInstance(ArrayList<String> imageList) {
        AjoutPlanteFragment fragment = new AjoutPlanteFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_IMAGE_LIST, imageList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageList = getArguments().getStringArrayList(ARG_IMAGE_LIST);
            if (imageList == null) imageList = new ArrayList<>();
        }
        // TEST : ajoute une image locale si la liste est vide (pour vérifier l’affichage)
        if (imageList.isEmpty()) {
            imageList.add("flower2");
        }
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajoutplante, container, false);

        // RecyclerView pour afficher les images
        RecyclerView recyclerView = view.findViewById(R.id.recyclerPhotos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ImageAdapter(imageList));

        // "Ajouter une plante/photo"
        ImageButton addButton = view.findViewById(R.id.ajouter_button);
        addButton.setOnClickListener(v -> {
            // Retourner à CameraFragment et passer la liste courante
            CameraFragment frag = CameraFragment.newInstance(imageList);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, frag)
                    .addToBackStack(null)
                    .commit();
        });

        // "Suivant"
        MaterialButton nextButton = view.findViewById(R.id.next);
        nextButton.setOnClickListener(v -> {
            ParcellesFragment frag = ParcellesFragment.newInstance(imageList);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, frag)
                    .addToBackStack(null)
                    .commit();
        });

        // "Retour"
        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }




}
