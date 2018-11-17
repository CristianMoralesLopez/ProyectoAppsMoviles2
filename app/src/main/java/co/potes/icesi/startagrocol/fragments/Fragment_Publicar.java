package co.potes.icesi.startagrocol.fragments;

import android.os.Bundle;
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

import co.potes.icesi.startagrocol.R;

public class Fragment_Publicar extends Fragment {


    private FirebaseDatabase db;

    private EditText et_titulo;
    private EditText et_descripcion;
    private EditText et_caracteristicas;
    private Button btnAgregarFotoPrimaria;
    private Button btnAgregarFotoSecundaria;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publicar, container, false);

        db = FirebaseDatabase.getInstance();

        et_titulo = v.findViewById(R.id.et_titulo);

        btnAgregarFotoPrimaria = v.findViewById(R.id.btn_agregarfotoPrim);

        et_descripcion = v.findViewById(R.id.et_descripcion);





        DatabaseReference reference = db.getReference();


        return v;
    }
}
