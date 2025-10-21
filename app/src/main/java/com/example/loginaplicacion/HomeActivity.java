package com.example.loginaplicacion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeActivity extends AppCompatActivity{
    private EditText nameField, telefonoField;
    private Button registrarBtn;
    private FirebaseAuth auth;
    private DatabaseReference usuarioRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        usuarioRef = FirebaseDatabase.getInstance().getReference();

        nameField = findViewById(R.id.nameField);
        telefonoField = findViewById(R.id.phoneField);
        registrarBtn = findViewById(R.id.registrarBtn);

        registrarBtn.setOnClickListener(v -> registrarUsuario());
    }

    private void registrarUsuario() {
        String nombre = nameField.getText().toString().trim();
        String telefono = telefonoField.getText().toString().trim();
        String correo = auth.getCurrentUser().getEmail();
        String id = auth.getCurrentUser().getUid();
        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un nombre y un teléfono", Toast.LENGTH_SHORT).show();
            return;
        }
        Usuario nuevoUsuario = new Usuario(id, nombre, telefono, correo);
        usuarioRef.child("usuarios").child(id).setValue(nuevoUsuario)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error en el registro:" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
