package co.potes.icesi.startagrocol.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.AdaptadorInversores;
import co.potes.icesi.startagrocol.model.AdaptadorListaHome;
import co.potes.icesi.startagrocol.model.Inversion;
import co.potes.icesi.startagrocol.model.Proyecto;


public class Fragment_Inversiones extends Fragment {



    private ListView lista;
    private AdaptadorInversores adaptadorListas;
    private ArrayList<Inversion> proyectos;
    private FirebaseDatabase db;
    private MensajesFragments mensajesFragments;
    private FirebaseAuth auth;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_inversiones, container, false);

        auth = FirebaseAuth.getInstance();


        final FirebaseAuth auth = FirebaseAuth.getInstance();


        Proyecto proyecto = (Proyecto) getArguments().getSerializable("proyecto");







        adaptadorListas = new AdaptadorInversores(this);
        lista = v.findViewById(R.id.lista_inversores_inversiones);
        lista.setAdapter(adaptadorListas);
        proyectos = new ArrayList<Inversion>();




        db = FirebaseDatabase.getInstance();

        DatabaseReference reference = db.getReference().child("usuarios").child(auth.getCurrentUser().getUid()).child("proyectos")
                .child(proyecto.getId()).child("inversiones");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                   String inversor = (String) postSnapshot.child("inversor").getValue();

                   String valor = (String) postSnapshot.child("valor").getValue();


                   Inversion inversion = new Inversion();

                   inversion.setInversor(inversor);




                   inversion.setValor(valor);
                    adaptadorListas.agregarLista(inversion);
                    proyectos.add(inversion);






                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        TextView valorRecolectado = v.findViewById(R.id.valorInvertido_inversiones);

        int valor1 = Integer.parseInt(proyecto.getValorRecolectado());

        NumberFormat format =  NumberFormat.getCurrencyInstance(new Locale("es","CO"));


        valorRecolectado.setText(""+ format.format(valor1));







        return v;
    }


    public void onAttach(Activity activity){

        super.onAttach(activity);

        mensajesFragments = (MensajesFragments) activity;
    }







    public Fragment_Inversiones() {
        // Required empty public constructor
    }









}
