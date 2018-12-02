package co.potes.icesi.startagrocol.model;

import android.app.Activity;
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

public class AdaptadorInversores extends BaseAdapter {
    private Fragment fragment;
    private ArrayList<Inversion> proyectos;
    private Activity activity ;
    private FirebaseDatabase db;
    private FirebaseAuth auth;


    public AdaptadorInversores(Fragment fragment) {
        this.fragment = fragment;
        proyectos = new ArrayList<Inversion>();
    }


    public AdaptadorInversores(Activity activity) {
        this.activity = activity;
        proyectos = new ArrayList<Inversion>();
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
        View renglon = inflater.inflate(R.layout.xmlinversiones, null, false);

        TextView inversor = renglon.findViewById(R.id.inversor_xml);
        TextView valor = renglon.findViewById(R.id.valor_xml);

        inversor.setText(proyectos.get(position).getInversor());

        int valor1 = Integer.parseInt(proyectos.get(position).getValor());

        NumberFormat format =  NumberFormat.getCurrencyInstance(new Locale("es","CO"));


        valor.setText("Valor Invertido: "+ format.format(valor1));



        return renglon;
    }


    public void agregarLista (Inversion play){
        proyectos.add(play);
        notifyDataSetChanged();
    }

    public void limpiar(){
        proyectos.clear();
    }
}
