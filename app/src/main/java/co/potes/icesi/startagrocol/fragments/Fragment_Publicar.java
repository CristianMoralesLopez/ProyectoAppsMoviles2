package co.potes.icesi.startagrocol.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.Proyecto;
import co.potes.icesi.startagrocol.model.Usuario;

public class Fragment_Publicar extends Fragment {


    private FirebaseDatabase db;

    private EditText et_titulo;
    private EditText et_descripcion;
    private EditText et_caracteristicas;
    private Button btnAgregarFotoPrimaria;
    private Button btnAgregarFotoSecundaria;
    private FirebaseStorage firebaseStorage;
    private Button btnAgregarProyecto;
    private String id;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publicar, container, false);


        id = getArguments().getString("usuario");

        db = FirebaseDatabase.getInstance();

        et_titulo = v.findViewById(R.id.et_titulo);

        btnAgregarFotoPrimaria = v.findViewById(R.id.btn_agregarfotoPrim);

        et_descripcion = v.findViewById(R.id.et_descripcion);


        btnAgregarProyecto = v.findViewById(R.id.btn_guardar);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        btnAgregarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = firebaseDatabase.getReference().child(Usuario.EMPRENDEDOR).child(id).child("proyectos").push();

                Proyecto nuevo = new Proyecto();

                nuevo.setId(reference.getKey());
                nuevo.setTitulo(et_titulo.getText().toString());
                nuevo.setDescripcion(et_descripcion.getText().toString());
                nuevo.setUrl("");
                reference.setValue(nuevo);
            }
        });


        return v;
    }
}
