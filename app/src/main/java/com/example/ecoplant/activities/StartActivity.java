package com.example.ecoplant.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.ecoplant.R;
import com.example.ecoplant.fragments.CameraFragment;
import com.example.ecoplant.fragments.ProfilFragment;
import com.example.ecoplant.fragments.CameraFragment;
import com.example.ecoplant.fragments.HistoriqueFragment;
import com.example.ecoplant.fragments.GestionCompteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        BottomNavigationView nav = findViewById(R.id.navigation);

        // Affiche le fragment profil par défaut
        if (savedInstanceState == null) {
            showFragment(new ProfilFragment());
            nav.setSelectedItemId(R.id.action_home);

        }

        nav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.action_home) {
                selectedFragment = new ProfilFragment();
            } else if (id == R.id.action_camera) {
                selectedFragment = new CameraFragment();
            } else if (id == R.id.action_files) {
                selectedFragment = new HistoriqueFragment();
            } else if (id == R.id.action_profile) {
                selectedFragment = new GestionCompteFragment();
            }

            if (selectedFragment != null) {
                showFragment(selectedFragment);
                return true;
            }
            return false;
        });

    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
