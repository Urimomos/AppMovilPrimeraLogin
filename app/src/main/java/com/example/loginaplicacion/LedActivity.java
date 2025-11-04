package com.example.loginaplicacion;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class LedActivity extends AppCompatActivity {
//private DatabaseReference mDatabase;
    private Button btnOn, btnOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_led);

        btnOn = findViewById(R.id.btnOn);
        btnOff = findViewById(R.id.btnOff);

        /*mDatabase = FirebaseDatabase.getInstance().getReference("led");

        // Listener para cambios en la base de datos
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String status = snapshot.getValue(String.class);
                if ("ON".equals(status)) {
                    btnOn.setEnabled(false);
                    btnOff.setEnabled(true);
                } else {
                    btnOn.setEnabled(true);
                    btnOff.setEnabled(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });

        btnOn.setOnClickListener(v -> mDatabase.setValue("ON"));
        btnOff.setOnClickListener(v -> mDatabase.setValue("OFF"));*/
    }

}
