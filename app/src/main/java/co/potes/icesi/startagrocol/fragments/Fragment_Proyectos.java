package co.potes.icesi.startagrocol.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import co.potes.icesi.startagrocol.Login;
import co.potes.icesi.startagrocol.R;

public class Fragment_Proyectos extends Fragment implements View.OnClickListener {

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button cerrarSesion;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_proyectos, container, false);

        cerrarSesion = v.findViewById(R.id.btn_cerrarsesion);
        cerrarSesion.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

        return v;
    }

    @Override
    public void onClick(View v) {

        if (v.equals(cerrarSesion)) {

            if(auth.getCurrentUser()!=null){
                auth.signOut();
            }
            Intent intent = new Intent(this.getContext(), Login.class);
            startActivity(intent);
        }

    }
}
