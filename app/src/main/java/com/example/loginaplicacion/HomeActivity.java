package com.example.loginaplicacion;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.loginaplicacion.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity{
    private EditText nameField, telefonoField;
    private Button registrarBtn, logoutBtn, ledBtn;
    private TextView userInfo;
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
        userInfo = findViewById(R.id.userInfo);
        logoutBtn = findViewById(R.id.logoutBtn);
        ledBtn = findViewById(R.id.ledBtn);

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            loadAndDisplayUserData(user.getUid());
        }
        
        registrarBtn.setOnClickListener(v -> registrarUsuario());

        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            finish();
        });

        ledBtn.setOnClickListener(v -> abrirLedBotones(LedActivity.class));


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
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    String infoCompleta = "Bienvenido, " + nombre + "\n" +
                            "Email: " + correo + "\n" +
                            "Teléfono: " + telefono;
                    userInfo.setText(infoCompleta);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error en el registro:" + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void abrirLedBotones(Class<?> activity) {
        Intent intent = new Intent(HomeActivity.this, activity);
        startActivity(intent);
    }

    private void loadAndDisplayUserData(String userId) {
        // Apuntamos directamente a la referencia de ESE usuario
        DatabaseReference currentUserRef = usuarioRef.child("usuarios").child(userId);

        // addValueEventListener se mantiene escuchando cambios en tiempo real
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // El usuario ya tiene datos guardados, los cargamos
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        String info = "Bienvenido, " + usuario.getNombre() + "\n" +
                                "Email: " + usuario.getCorreo() + "\n" +
                                "Teléfono: " + usuario.getTelefono();
                        userInfo.setText(info);
                    }
                } else {
                    // El usuario está autenticado pero no ha completado el registro
                    FirebaseUser user = auth.getCurrentUser();
                    if (user != null) {
                        userInfo.setText("Bienvenido, " + user.getEmail() + "\n(Completa tu registro)");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
