package co.potes.icesi.startagrocol.model;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.fragments.Fragment_Inversiones;
import co.potes.icesi.startagrocol.fragments.Mis_ProyectosFragment;

public class AdaptadorMisproyectos extends BaseAdapter {

    private Mis_ProyectosFragment fragment;
    private ArrayList<Proyecto> proyectos;
    private Activity activity ;
    private FirebaseDatabase db;
    private FirebaseAuth auth;


    public AdaptadorMisproyectos(Mis_ProyectosFragment fragment) {
        this.fragment = fragment;
        proyectos = new ArrayList<Proyecto>();
    }


    public AdaptadorMisproyectos(Activity activity) {
        this.activity = activity;
        proyectos = new ArrayList<Proyecto>();
    }

    @Override
    public int getCount() {
        return proyectos.size();
    }

    @Override
    public Object getItem(int position) {


        return proyectos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(fragment.getContext());
        View renglon = inflater.inflate(R.layout.xmlmisproyectos, null, false);
        TextView txtTiulto = renglon.findViewById(R.id.titulo_misProyectos);
        TextView txtDescripcion = renglon.findViewById(R.id.descripcion_misProyectos);
        TextView txtFecha= renglon.findViewById(R.id.fechaProyecto_misProyectos);
        TextView monto = renglon.findViewById(R.id.valorProyecto_misProyectos);
        final Button btnEditar = renglon.findViewById(R.id.editar_misProyectos);
        Button btnInversiones = renglon.findViewById(R.id.inversiones_misProyectos);
        final Button btnEliminar = renglon.findViewById(R.id.eliminar_misProyectos);
        final Button btnPublicar = renglon.findViewById(R.id.publicar_misProyectos);

        if(proyectos.get(position).getPublicado().equals("si")){

            btnEditar.setEnabled(false);
            btnEliminar.setEnabled(false);
            btnPublicar.setEnabled(false);
        }


        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        ImageView image = renglon.findViewById(R.id.iv_foto_misProyectos);

        txtTiulto.setText( proyectos.get(position).getTitulo());
        txtDescripcion.setText("Descripcion"+"\n"+"\n"+proyectos.get(position).getDescripcion());
        txtFecha.setText("Fecha de Cierre: "+proyectos.get(position).getFechaCierreProyecto());

        int valor = Integer.parseInt(proyectos.get(position).getValorProyecto());

        NumberFormat format =  NumberFormat.getCurrencyInstance(new Locale("es","CO"));


        monto.setText("valor del proyecto: "+ format.format(valor));





        Picasso.get().load(proyectos.get(position).getImagenPrimaria()).into(image);



        btnPublicar.setTag(position);

        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference reference = db.getReference().child("Proyectos").child(proyectos.get((int)v.getTag()).getId());

                reference.setValue(proyectos.get((int)v.getTag()));

                btnPublicar.setEnabled(false);
                btnEliminar.setEnabled(false);
                btnEditar.setEnabled(false);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = db.getReference().child("Proyectos").child(proyectos.get(position).getId());
                DatabaseReference reference1 = db.getReference().child("usuarios").child(auth.getCurrentUser().getUid()).child("proyectos")
                        .child(proyectos.get(position).getId());


                reference.setValue(null);
                reference1.setValue(null);
                notifyDataSetChanged();
            }
        });


        btnEditar.setTag(position);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(fragment.getActivity(),""+ position, Toast.LENGTH_SHORT).show();
            }
        });



        btnInversiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        fragment.fragmentInvertir(proyectos.get(position));

            }
        });



        return renglon;
    }


    public void agregarLista (Proyecto play){
        proyectos.add(play);
        notifyDataSetChanged();
    }

    public void limpiar(){
        proyectos.clear();
    }

}