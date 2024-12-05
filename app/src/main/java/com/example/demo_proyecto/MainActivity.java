package com.example.demo_proyecto;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class MainActivity extends AppCompatActivity {
    private MemoramaView memoramaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memoramaView = findViewById(R.id.memoramaView);
        String playerName = getIntent().getStringExtra("PLAYER_NAME");
        if (playerName != null) {
            Toast.makeText(this, "¡Bienvenido, " + playerName + "!", Toast.LENGTH_SHORT).show();
        }
    }
}



