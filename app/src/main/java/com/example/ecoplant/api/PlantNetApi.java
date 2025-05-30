package com.example.ecoplant.api;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ecoplant.models.Parcelle;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PlantNetApi {

    public static void identifyPlant(Context context, File imageFile, String apiKey, PlantNetCallback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "images",
                        imageFile.getName(),
                        RequestBody.create(imageFile, MediaType.parse("image/jpeg"))
                )
                .addFormDataPart("organs", "auto")
                .build();

        Request request = new Request.Builder()
                .url("https://my-api.plantnet.org/v2/identify/all?api-key=" + apiKey)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Utilise un Handler principal pour Toast
                android.os.Handler mainHandler = new android.os.Handler(context.getMainLooper());
                mainHandler.post(() -> {
                    Toast.makeText(context, "Erreur PlantNet", Toast.LENGTH_SHORT).show();
                });
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(new IOException("Erreur PlantNet : " + response.code()));
                    return;
                }
                String body = response.body().string();
                callback.onSuccess(body);
            }
        });
    }

    // Interface callback pour retour asynchrone
    public interface PlantNetCallback {
        void onSuccess(String plantNetJson);
        void onFailure(Exception e);
    }
}
