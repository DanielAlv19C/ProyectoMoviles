package com.example.demo_proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroNombreM extends AppCompatActivity {

    private EditText editTextName;
    private Button buttonStart;
    private Button buttonViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_nombre);

        editTextName = findViewById(R.id.editTextName);
        buttonStart = findViewById(R.id.buttonStart);
        buttonViewData = findViewById(R.id.buttonViewData);

        buttonStart.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                // Enviar el nombre a la siguiente actividad
                Intent intent = new Intent(RegistroNombreM.this, MainActivity.class);
                intent.putExtra("PLAYER_NAME", name);
                startActivity(intent);
                finish(); // Finaliza esta actividad para que no vuelva con "atrás"
            } else {
                editTextName.setError("Por favor, ingresa un nombre");
            }
        });

        // Abrir la nueva pantalla para mostrar los datos almacenados
        buttonViewData.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroNombreM.this, ViewDataActivity.class);
            startActivity(intent);
        });
    }
}