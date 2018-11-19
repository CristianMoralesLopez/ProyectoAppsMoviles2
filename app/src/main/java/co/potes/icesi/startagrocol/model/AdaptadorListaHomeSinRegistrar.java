package co.potes.icesi.startagrocol.model;
import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import co.potes.icesi.startagrocol.R;

public class AdaptadorListaHomeSinRegistrar extends BaseAdapter {
    private Fragment fragment;
    private ArrayList<Proyecto> proyectos;
    private Activity activity ;


    public AdaptadorListaHomeSinRegistrar(Activity activity) {
        this.activity = activity;
        proyectos = new ArrayList<Proyecto>();
    }

    @Override
    public int getCount() {
        return proyectos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View renglon = inflater.inflate(R.layout.xmlisthome, null, false);
        TextView txtTiulto = renglon.findViewById(R.id.lista);
        TextView txtDescripcion = renglon.findViewById(R.id.usuario);


        ImageView image = renglon.findViewById(R.id.iv_foto);

        txtTiulto.setText( proyectos.get(position).getTitulo());
        txtDescripcion.setText(proyectos.get(position).getDescripcion());




        Picasso.get().load(proyectos.get(position).getUrl()).into(image);



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
