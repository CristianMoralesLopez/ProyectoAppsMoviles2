package co.potes.icesi.startagrocol;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.potes.icesi.startagrocol.model.AdaptadorListaHome;
import co.potes.icesi.startagrocol.model.AdaptadorListaHomeSinRegistrar;
import co.potes.icesi.startagrocol.model.Proyecto;

public class HomeSinRegistro extends AppCompatActivity {

    private Button btnEmprendedor;
    private Button btnInversor;
    private ListView lista;
    private AdaptadorListaHomeSinRegistrar adaptadorListas;
    private ArrayList<Proyecto> proyectos;
    private FirebaseDatabase db;
    private EditText txtbusqueda;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener fireAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_sin_registro);

        btnEmprendedor = findViewById(R.id.btnRegistrarseEmprendedor);
        btnInversor = findViewById(R.id.btnRegistrarseInversor);
        lista = findViewById(R.id.listaSinRegistrar);
        adaptadorListas = new AdaptadorListaHomeSinRegistrar(this);
        lista.setAdapter(adaptadorListas);
        db = FirebaseDatabase.getInstance();
        txtbusqueda = findViewById(R.id.txtBusquedaSinRegistro);


        firebaseAuth = FirebaseAuth.getInstance();

        fireAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Intent i = new Intent(HomeSinRegistro.this, Background.class);

                    i.putExtra("usuario", user.getUid());

                    startActivity(i);


                    finish();


                }


            }
        };

        db = FirebaseDatabase.getInstance();

        DatabaseReference reference = db.getReference().child("Proyectos");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String titulo = (String) postSnapshot.child("titulo").getValue();
                    String url = (String) postSnapshot.child("imagenPrimaria").getValue();
                    String descripcion = (String) postSnapshot.child("descripcion").getValue();


                    Proyecto proyecto = new Proyecto();

                    proyecto.setDescripcion(descripcion);
                    proyecto.setTitulo(titulo);
                    proyecto.setImagenPrimaria(url);


                    adaptadorListas.agregarLista(proyecto);


                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnInversor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeSinRegistro.this, BannerInversor.class);
                startActivity(i);
                finish();


            }
        });

        btnEmprendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeSinRegistro.this, BannerEmprendedor.class);
                startActivity(i);
                finish();


            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        firebaseAuth.addAuthStateListener(fireAuthStateListener);
    }


    @Override
    protected void onStop() {
        super.onStop();


        if (fireAuthStateListener != null) {
            firebaseAuth.removeAuthStateListener(fireAuthStateListener);
        }

    }
}
