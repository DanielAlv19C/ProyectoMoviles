package com.example.demo_proyecto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainMemorama extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Referencias a los widgets
        ImageView widgetSolo = findViewById(R.id.widget_solo);
        ImageView widgetDos = findViewById(R.id.widget_dos);

        // Acción para "Modo un jugador"
        widgetSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainMemorama.this, RegistroNombreM.class);
                startActivity(intent);
            }
        });

        // Acción para "Modo dos jugadores"
        widgetDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMemorama.this, RegistroMultijugador.class);
                startActivity(intent);
            }
        });
    }
}