package com.example.ecoplant.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.ecoplant.R;
import com.example.ecoplant.api.PlantNetApi;
import com.example.ecoplant.database.AppDatabase;
import com.example.ecoplant.models.Plante;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {

    private PreviewView viewFinder;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private boolean permissionGranted = false;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                permissionGranted = isGranted;
                if (isGranted) startCamera();
                else Toast.makeText(getContext(), "Permission caméra refusée", Toast.LENGTH_SHORT).show();
            });

    public static CameraFragment newInstance(ArrayList<String> imageList) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("imageList", imageList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        viewFinder = view.findViewById(R.id.viewFinder);
        FloatingActionButton captureButton = view.findViewById(R.id.captureButton);

        captureButton.setOnClickListener(v -> {
            if (permissionGranted) {
                takePhoto();
            } else {
                Toast.makeText(getContext(), "Permission caméra refusée, utilisez une image de test.", Toast.LENGTH_SHORT).show();
            }
        });

        cameraExecutor = Executors.newSingleThreadExecutor();

        // Bouton image de test
        com.google.android.material.button.MaterialButton testImageButton = view.findViewById(R.id.testImageButton);
        testImageButton.setOnClickListener(v -> useTestImage());

        // Permission caméra
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;
            startCamera();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }

        return view;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(viewFinder.getSurfaceProvider());

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (Exception e) {
                Log.e("CameraFragment", "Erreur initialisation caméra", e);
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void takePhoto() {
        if (imageCapture == null) return;
        File photoFile = new File(requireContext().getExternalCacheDir(),
                "photo_" + System.currentTimeMillis() + ".jpg");

        ImageCapture.OutputFileOptions outputOptions =
                new ImageCapture.OutputFileOptions.Builder(photoFile).build();

        imageCapture.takePicture(
                outputOptions,
                cameraExecutor,
                new ImageCapture.OnImageSavedCallback() {
                    @Override
                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                        requireActivity().runOnUiThread(() -> {
                            //Toast.makeText(requireContext(), "Photo prise : " + photoFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                            enregistrerPlanteEtNaviguer(photoFile.getAbsolutePath());
                        });
                    }

                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(requireContext(), "Erreur prise de photo", Toast.LENGTH_SHORT).show()
                        );
                        Log.e("CameraFragment", "Erreur prise de photo", exception);
                    }
                }
        );
    }

    /** Image test (pour simulateur/émulateur) **/
    private void useTestImage() {
        File testFile = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "photo_" + System.currentTimeMillis() + ".jpg"
        );
        if (!testFile.exists()) {
            try (@SuppressLint("ResourceType") java.io.InputStream in = requireContext().getResources().openRawResource(R.drawable.flower2);
                 java.io.OutputStream out = new java.io.FileOutputStream(testFile)) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        enregistrerPlanteEtNaviguer(testFile.getAbsolutePath());
    }

    private void enregistrerPlanteEtNaviguer(String imagePath) {
        Plante plante = new Plante();
        plante.setPhotoPath(imagePath);
        plante.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        plante.setCreationDate(
                new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm", java.util.Locale.getDefault())
                        .format(new java.util.Date())
        );

        // Appel PlantNet pour analyser la photo
        PlantNetApi.identifyPlant(requireContext(), new File(imagePath), "2b10mr6phr8K14uTK2FF9klO", new PlantNetApi.PlantNetCallback() {
            @Override
            public void onSuccess(String plantNetJson) {
                // Extraction des scores depuis la réponse JSON
                try {
                    // TODO : adapte ici en fonction du vrai JSON
                    // Exemple DEMO :
                    double scoreSoil = 0.6;
                    double scoreWater = 0.4;
                    double scoreNitrogen = 0.7;

                    // Mets à jour l’objet plante
                    plante.setScoreSoilStructure(scoreSoil);
                    plante.setScoreWaterRetention(scoreWater);
                    plante.setScoreNitrogenFixation(scoreNitrogen);

                } catch (Exception e) {
                    // Mets des valeurs par défaut si erreur
                    plante.setScoreSoilStructure(0.0);
                    plante.setScoreWaterRetention(0.0);
                    plante.setScoreNitrogenFixation(0.0);
                }

                // Insère la plante dans la base et navigue
                Executors.newSingleThreadExecutor().execute(() -> {
                    AppDatabase db = AppDatabase.getInstance(requireContext());
                    db.planteDao().insert(plante);

                    requireActivity().runOnUiThread(() -> {
                        ArrayList<String> imageList = getArguments() != null ? getArguments().getStringArrayList("imageList") : new ArrayList<>();
                        if (imageList == null) imageList = new ArrayList<>();
                        imageList.add(imagePath);

                        AjoutPlanteFragment frag = AjoutPlanteFragment.newInstance(imageList);
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragmentContainer, frag)
                                .addToBackStack(null)
                                .commit();
                    });
                });
            }

            @Override
            public void onFailure(Exception e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Erreur analyse PlantNet", Toast.LENGTH_SHORT).show()
                );
                // Tu peux insérer quand même la plante avec des scores 0 si tu veux
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) cameraExecutor.shutdown();
    }
}
