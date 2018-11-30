package co.potes.icesi.startagrocol.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.AdaptadorListaHome;
import co.potes.icesi.startagrocol.model.Proyecto;

public class Fragment_Home extends Fragment  {

    private ImageButton btnBusqueda;
    private EditText txtBusqueda;
    private ListView lista;
    private AdaptadorListaHome adaptadorListas;
    private ArrayList<Proyecto> proyectos;
    private FirebaseDatabase db;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_home, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();



        FirebaseAuth auth = FirebaseAuth.getInstance();





        btnBusqueda = v.findViewById(R.id.btnBusqueda);
        txtBusqueda = v.findViewById(R.id.txtBusqueda);

        adaptadorListas = new AdaptadorListaHome(this);
        lista = v.findViewById(R.id.lista);
        lista.setAdapter(adaptadorListas);
        proyectos = new ArrayList<Proyecto>();




        db = FirebaseDatabase.getInstance();

        DatabaseReference reference = db.getReference().child("Proyectos");

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


                    adaptadorListas.agregarLista(proyecto);





                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnBusqueda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                adaptadorListas.limpiar();

                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });



        return v;
    }




}
