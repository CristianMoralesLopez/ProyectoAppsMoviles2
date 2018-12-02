package co.potes.icesi.startagrocol.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.AdaptadorListaHome;
import co.potes.icesi.startagrocol.model.AdaptadorMisproyectos;
import co.potes.icesi.startagrocol.model.Proyecto;


public class Mis_ProyectosFragment extends Fragment {





    private ListView lista;
    private AdaptadorMisproyectos adaptadorMisproyectos;
    private ArrayList<Proyecto> proyectos;
    private FirebaseDatabase db;
    private MensajesFragments mensajesFragments;
    private FirebaseAuth auth;
    private FloatingActionButton añadir;




    public Mis_ProyectosFragment() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_mis__proyectos, container, false);


        auth = FirebaseAuth.getInstance();


        final FirebaseAuth auth = FirebaseAuth.getInstance();

        adaptadorMisproyectos = new AdaptadorMisproyectos(this);
        lista = v.findViewById(R.id.lista_Mis_Proyectos);
        lista.setAdapter(adaptadorMisproyectos);
        proyectos = new ArrayList<Proyecto>();




        db = FirebaseDatabase.getInstance();

        DatabaseReference reference = db.getReference().child("usuarios").child(auth.getCurrentUser().getUid()).child("proyectos");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    String titulo =(String) postSnapshot.child("titulo").getValue();
                    String url = (String) postSnapshot.child("imagenPrimaria").getValue();
                    String descripcion = (String) postSnapshot.child("descripcion").getValue();
                    String fecha = (String) postSnapshot.child("fechaCierreProyecto").getValue();
                    String id = (String) postSnapshot.child("id").getValue();
                    String idPropietario = (String) postSnapshot.child("idPropietario").getValue();
                    String imagenSecundaria = (String) postSnapshot.child("imagenSecundaria").getValue();
                    String metodoInversion = (String) postSnapshot.child("metodoInversion").getValue();
                    String resumen  = (String) postSnapshot.child("resumen").getValue();
                    String valorProyecto = (String ) postSnapshot.child("valorProyecto").getValue();
                    String publicado = (String) postSnapshot.child("publicado").getValue();
                    String valorRecolectado = (String) postSnapshot.child("valorRecolectado").getValue();

                    Proyecto proyecto = new Proyecto();

                    proyecto.setDescripcion(descripcion);
                    proyecto.setTitulo(titulo);
                    proyecto.setImagenPrimaria(url);
                    proyecto.setImagenSecundaria(imagenSecundaria);
                    proyecto.setFechaCierreProyecto(fecha);
                    proyecto.setId(id);
                    proyecto.setIdPropietario(idPropietario);
                    proyecto.setMetodoInversion(metodoInversion);
                    proyecto.setResumen(resumen);
                    proyecto.setValorProyecto(valorProyecto);
                    proyecto.setPublicado(publicado);
                    proyecto.setValorRecolectado(valorRecolectado);


                    adaptadorMisproyectos.agregarLista(proyecto);
                    proyectos.add(proyecto);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });









        añadir = v.findViewById(R.id.añadirProyecto);


        añadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mensajesFragments.añadirproyecto("");

            }
        });



        return v;
    }


    public void onAttach(Activity activity){

        super.onAttach(activity);

        mensajesFragments = (MensajesFragments) activity;
    }


    public void fragmentInvertir (Proyecto proyecto){

        mensajesFragments.inversores(proyecto);
    }


}
