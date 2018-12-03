package co.potes.icesi.startagrocol.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import co.potes.icesi.startagrocol.Background;
import co.potes.icesi.startagrocol.Background_Inversor;
import co.potes.icesi.startagrocol.HomeSinRegistro;
import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.Proyecto;
import co.potes.icesi.startagrocol.model.Usuario;


public class Fragment_Proyecto_Completo extends Fragment {


    private TextView titulo;
    private TextView descripcion;
    private TextView caracteristicas;
    private TextView monto;
    private TextView metodoInversion;
    private TextView fecha;
    private ImageView imagenPrincipal;
    private ImageView imagenSecundaria;
    private Button btnInvertir;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private EditText valorInversion;




    public Fragment_Proyecto_Completo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        final Proyecto proyecto = (Proyecto) getArguments().getSerializable("proyecto");


        View v = inflater.inflate(R.layout.fragment_fragment__proyecto__completo, container, false);

        titulo = v.findViewById(R.id.et_titulo_completo);

        descripcion = v.findViewById(R.id.et_descripcion_completo);

        descripcion.setMovementMethod(new ScrollingMovementMethod());

        caracteristicas = v.findViewById(R.id.et_caracteristicas_completo);
        caracteristicas.setMovementMethod(new ScrollingMovementMethod());

        fecha = v.findViewById(R.id.et_fecha_completo);

        monto = v.findViewById(R.id.et_monto_completo);

        metodoInversion = v.findViewById(R.id.et_Metodo_Inversion_Completo);

        imagenPrincipal = v.findViewById(R.id.imagen_principal_completo);

        imagenSecundaria = v.findViewById(R.id.imagen_secundaria_completo);

        btnInvertir = v.findViewById(R.id.invertir_proyecto_completo);

        valorInversion = v.findViewById(R.id.valor_version_proyecto_completo);

        btnInvertir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (valorInversion.getText().equals("")){
                    Toast.makeText(getActivity(),"ingrese el valor de la inversion",Toast.LENGTH_SHORT);
                }else {



                    DatabaseReference reference = db.getReference().child("usuarios").child(proyecto.getIdPropietario()).child("proyectos").
                            child(proyecto.getId()).child("inversiones").push();

                    DatabaseReference reference1 = db.getReference().child("usuarios").child(proyecto.getIdPropietario()).child("proyectos").
                            child(proyecto.getId()).child("inversiones").child(reference.getKey()).child("id");
                    reference1.setValue(reference.getKey());

                    DatabaseReference reference2 = db.getReference().child("usuarios").child(proyecto.getIdPropietario()).child("proyectos").
                            child(proyecto.getId()).child("inversiones").child(reference.getKey()).child("inversor");
                    reference2.setValue(auth.getCurrentUser().getDisplayName());

                    DatabaseReference reference3 = db.getReference().child("usuarios").child(proyecto.getIdPropietario()).child("proyectos").
                            child(proyecto.getId()).child("inversiones").child(reference.getKey()).child("valor");

                    reference3.setValue(valorInversion.getText().toString());

                    final DatabaseReference reference4 = db.getReference().child("usuarios").child(proyecto.getIdPropietario()).child("proyectos").
                            child(proyecto.getId()).child("valorRecolectado");

                    DatabaseReference reference5 = db.getReference().child("Proyectos").child(proyecto.getId()).child("inversiones").child(reference.getKey())
                            .child("id");

                    reference5.setValue(reference.getKey());

                    DatabaseReference reference6 = db.getReference().child("Proyectos").child(proyecto.getId()).child("inversiones").child(reference.getKey())
                            .child("inversor");
                    reference6.setValue(auth.getCurrentUser().getDisplayName());

                    DatabaseReference reference7 = db.getReference().child("Proyectos").child(proyecto.getId()).child("inversiones").child(reference.getKey())
                            .child("valor");
                    reference7.setValue(valorInversion.getText().toString());

                   final DatabaseReference reference9 = db.getReference().child("Proyectos").child(proyecto.getId()).child("valorRecolectado");


                 DatabaseReference   reference10 = db.getReference().child("usuarios").child(proyecto.getIdPropietario()).child("proyectos").
                         child(proyecto.getId()).child("valorRecolectado");

                 reference10.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                         int val1 = Integer.parseInt((String) dataSnapshot.getValue());

                         int val2 = Integer.parseInt(valorInversion.getText().toString());

                         int val  = val1 + val2;


                         reference4.setValue(""+val);
                         reference9.setValue(""+val);

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });






                }



            }
        });


        auth = FirebaseAuth.getInstance();

                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {

                    DatabaseReference reference = db.getReference().child("usuarios").child(user.getUid());


                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            String valor3 = (String) dataSnapshot.child("tipo").getValue();

                            if(valor3.equals(Usuario.EMPRENDEDOR)){
                                btnInvertir.setVisibility(View.GONE);
                                metodoInversion.setVisibility(View.GONE);
                                valorInversion.setVisibility(View.GONE);
                            }
                            else{
                                btnInvertir.setVisibility(View.VISIBLE);
                                metodoInversion.setVisibility(View.VISIBLE);
                                valorInversion.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



        };
        actualizar(proyecto);

        return v;


    }



    public void actualizar(Proyecto proyecto){





        titulo.setText(proyecto.getTitulo());
        fecha.setText("Fecha de cierre del proyecto "+proyecto.getFechaCierreProyecto());
        metodoInversion.setText("Metodo de inversion: "+proyecto.getMetodoInversion());
        caracteristicas.setText(proyecto.getResumen());
        descripcion.setText(proyecto.getDescripcion());

        int valor = Integer.parseInt(proyecto.getValorProyecto());

        NumberFormat format =  NumberFormat.getCurrencyInstance(new Locale("es","CO"));


        monto.setText("valor del proyecto: "+ format.format(valor));

        Picasso.get().load(proyecto.getImagenPrimaria()).into(imagenPrincipal);
        Picasso.get().load(proyecto.getImagenSecundaria()).into(imagenSecundaria);

    }


}
