package co.potes.icesi.startagrocol.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimatedImageDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import co.potes.icesi.startagrocol.MainActivity;
import co.potes.icesi.startagrocol.R;
import co.potes.icesi.startagrocol.model.Proyecto;
import co.potes.icesi.startagrocol.model.Usuario;
import util.UtilDomi;

import static android.app.Activity.RESULT_OK;

public class Fragment_Publicar extends Fragment {


    public static final int IMAGEN1 = 1000;
    public static final int IMAGEN2 = 1002;


    /*
    RUTAS PARA LAS FOTOS
     */

    private String path1;
    private String path2;


    private Spinner metodoInversion;


    private FirebaseDatabase db;

    private EditText et_titulo;
    private EditText et_descripcion;
    private EditText et_caracteristicas;
    private Button btnAgregarFotoPrimaria;
    private Button btnAgregarFotoSecundaria;
    private FirebaseStorage firebaseStorage;
    private Button btnAgregarProyecto;
    private Button btnFecha;
    private String id;
    private ImageView imagenPrincipal;
    private ImageView imagenSecundaria;
    private TextView txtFecha;
    private EditText et_monto;

    private int ano;
    private int mes;
    private int dia;

    private int ano1;
    private int mes1;
    private int dia1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_publicar, container, false);

        firebaseStorage = FirebaseStorage.getInstance();

        metodoInversion = v.findViewById(R.id.spin);





        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.opcionesMetodoPago,android.R.layout.simple_spinner_item);


        metodoInversion.setAdapter(adapter);




        id = getArguments().getString("usuario");

        db = FirebaseDatabase.getInstance();

        et_titulo = v.findViewById(R.id.et_titulo);

        et_caracteristicas = v.findViewById(R.id.et_caracteristicas);

        et_descripcion = v.findViewById(R.id.et_descripcion);

        btnAgregarFotoPrimaria = v.findViewById(R.id.btn_agregarfotoPrim);
        btnAgregarFotoSecundaria = v.findViewById(R.id.btn_agregarfotoSec);
        btnFecha = v.findViewById(R.id.btnFechaProyecto);

        imagenPrincipal = v.findViewById(R.id.img_fotoP);
        imagenSecundaria = v.findViewById(R.id.img_fotoS);


        et_descripcion = v.findViewById(R.id.et_descripcion);

        txtFecha = v.findViewById(R.id.txtFecha);

        et_monto = v.findViewById(R.id.et_monto);


        btnAgregarProyecto = v.findViewById(R.id.btn_guardar);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


        btnAgregarFotoPrimaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, IMAGEN1);

            }
        });

        btnAgregarFotoSecundaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, IMAGEN2);
            }
        });


        btnAgregarProyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                boolean bandera = false;
                String titulo = et_titulo.getText().toString();
                String descripcion = et_descripcion.getText().toString();
                String monto = et_monto.getText().toString();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");

                boolean fecha = true;

                int valorNecesario = 0;

                boolean convertido = false;

                String metodo = "";

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    metodo = metodoInversion.getTransitionName();
                }


                try {

                    valorNecesario = Integer.parseInt(monto);


                    Date date1 = simpleDateFormat.parse("" + dia + "-" + mes + "-" + ano);

                    Date date2 = simpleDateFormat.parse("" + dia1 + "-" + mes1 + "-" + ano1);

                    int valor = date1.compareTo(date2);

                    if (valor > 0) {
                        fecha = true;
                    } else if (valor < 0) {
                        fecha = false;
                    } else if (valor == 0) {
                        fecha = true;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NumberFormatException ex) {

                    convertido = true;
                }


                if(titulo.equals("")){

                    bandera = true;

                    Toast.makeText(getActivity(),"Introduzca un titulo valido",Toast.LENGTH_SHORT).show();
                }

                if (descripcion.equals("")){

                    bandera= true;
                    Toast.makeText(getActivity(),"Introduzca una descripcion valida",Toast.LENGTH_SHORT).show();
                }
                if (path1.equals("")|| path2.equals("")){

                    bandera= true;
                    Toast.makeText(getActivity(),"Debe de cargar minimo dos imagenes para su proyecto",Toast.LENGTH_SHORT).show();
                }

                if(fecha){

                    bandera = true;

                    Toast.makeText(getActivity(),"Debe ingresar una fecha mayor a la actual",Toast.LENGTH_SHORT).show();
                }


                if (convertido){

                    bandera = true;

                    Toast.makeText(getActivity(),"ingrese un monto valido",Toast.LENGTH_SHORT).show();
                }


                if (!bandera){

                    Toast.makeText(getActivity(),"todo esta correcto",Toast.LENGTH_SHORT).show();
                }










                DatabaseReference reference = firebaseDatabase.getReference().child(Usuario.EMPRENDEDOR).child(id).child("proyectos").push();

                Proyecto nuevo = new Proyecto();

                nuevo.setId(reference.getKey());
                nuevo.setTitulo(et_titulo.getText().toString());
                nuevo.setDescripcion(et_descripcion.getText().toString());
                reference.setValue(nuevo);
            }
        });


        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                dia = c.get(Calendar.DAY_OF_MONTH);

                mes = c.get(Calendar.MONTH);

                ano = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {



                        ano1= year;
                        mes1 = month;
                        dia1 = dayOfMonth;

                        txtFecha.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                    }
                }, ano, mes, dia);


                datePickerDialog.show();
            }
        });


        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGEN1 && resultCode == RESULT_OK) {

            path1 = UtilDomi.getPath(getActivity(), data.getData());
            Bitmap m = BitmapFactory.decodeFile(path1);
            imagenPrincipal.setImageBitmap(m);

        }

        if (requestCode == IMAGEN2 && resultCode == RESULT_OK) {

            path2 = UtilDomi.getPath(getActivity(), data.getData());
            Bitmap m = BitmapFactory.decodeFile(path2);
            imagenSecundaria.setImageBitmap(m);

        }


    }
}
