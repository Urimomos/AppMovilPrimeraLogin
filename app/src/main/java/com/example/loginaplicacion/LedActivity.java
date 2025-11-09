package com.example.loginaplicacion;

import android.widget.ImageView;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class LedActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button btnOn, btnOff;
    private ImageView ivLedIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar EdgeToEdge (parece que lo estás usando)
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_led);

        // Aplicar paddings para las barras del sistema (parte de EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- INICIA CÓDIGO FIREBASE ---

        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);
        ivLedIcon = findViewById(R.id.ivLedIcon);

        // 1. Obtenemos la referencia al nodo "led"
        mDatabase = FirebaseDatabase.getInstance().getReference("led");

        // 2. Este es el listener SEGURO que previene el cierre
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Comprueba si el dato existe
                if (snapshot.exists() && snapshot.getValue() != null) {

                    // Convierte el valor a String de forma SEGURA
                    String status = String.valueOf(snapshot.getValue());

                    if ("ON".equals(status)) {
                        // Si es "ON", deshabilita "Encender" y habilita "Apagar"
                        btnOn.setEnabled(false);
                        btnOff.setEnabled(true);
                        ivLedIcon.setImageResource(R.drawable.luz_indicadora_on);
                    } else {
                        // Cualquier otro valor ("OFF", null, 1, 0, etc.) se trata como apagado
                        btnOn.setEnabled(true);
                        btnOff.setEnabled(false);
                        ivLedIcon.setImageResource(R.drawable.luz_indicadora);
                    }
                } else {
                    // Si el nodo "led" no existe, lo tratamos como "apagado"
                    btnOn.setEnabled(true);
                    btnOff.setEnabled(false);
                    ivLedIcon.setImageResource(R.drawable.luz_indicadora);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Opcional: registrar el error si falla la lectura
                // Log.w("LedActivity", "Failed to read value.", error.toException());
            }
        });

        // 3. Listeners para escribir en Firebase
        btnOn.setOnClickListener(v -> mDatabase.setValue("ON"));
        btnOff.setOnClickListener(v -> mDatabase.setValue("OFF"));
    }
}