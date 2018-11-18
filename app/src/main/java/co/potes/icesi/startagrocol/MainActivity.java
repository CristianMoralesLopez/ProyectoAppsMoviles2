package co.potes.icesi.startagrocol;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import co.potes.icesi.*;

import co.potes.icesi.startagrocol.model.Usuario;
import util.UtilDomi;

public class MainActivity extends AppCompatActivity {



    private static final int REQUEST_GALLERY = 101;





    FirebaseDatabase db;
    FirebaseAuth auth;

    private EditText[] txt;
    private EditText[] labels;
    private RadioGroup[] groups;
    private RadioButton inversor;
    private RadioButton emprendedor;
    private Button btnRegistrarse;
    private ImageView imageView;
    private ImageButton imageButton;
    private CheckBox terminos;
    private GoogleApiClient mgGoogleApiClient;
    private String path;
    private FirebaseStorage firebaseStorage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 11);




        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        txt = new EditText[5];


        txt[0] = findViewById(R.id.txtNombreRegistro);
        txt[1] = findViewById(R.id.txtCorreoRegistro);
        txt[2] = findViewById(R.id.txtTelefonoRegistro);
        txt[3] = findViewById(R.id.txtcontraseña1Registro);
        txt[4] = findViewById(R.id.txtcontraseña2Registro);

        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        inversor = findViewById(R.id.radioButtonInversor);
        emprendedor = findViewById(R.id.radioButtonEmprendedor);

        terminos = findViewById(R.id.checkboxTerminos);

        imageView = findViewById(R.id.imagePerfil);
        imageButton = findViewById(R.id.btnCargarFoto);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,REQUEST_GALLERY);
            }
        });


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String nombre = txt[0].getText().toString();
                String correo = txt[1].getText().toString();
                String telefono = txt[2].getText().toString();
                String contraseña1 = txt[3].getText().toString();
                String contraseña2 = txt[4].getText().toString();
                String tipo = "";

                boolean bandera = true;

                if (nombre.equals("")) {

                    Toast.makeText(MainActivity.this, "Introduzca un nombre de usuario valido", Toast.LENGTH_SHORT).show();
                    bandera = false;

                }

                if (correo.contains("@") == false) {

                    Toast.makeText(MainActivity.this, "Introduzca un correo valido", Toast.LENGTH_SHORT).show();
                    bandera = false;
                }


                if ((!contraseña1.equals(contraseña2))) {


                    Toast.makeText(MainActivity.this, "Las contraseñas son diferentes", Toast.LENGTH_SHORT).show();
                    bandera = false;
                } else if (contraseña1.equals("")) {
                    Toast.makeText(MainActivity.this, "por favor introduce una contraseña", Toast.LENGTH_SHORT).show();
                    bandera = false;
                }

                if (!terminos.isChecked()) {

                    Toast.makeText(MainActivity.this, "Acepte los terminos y condicones para registrarse", Toast.LENGTH_SHORT).show();
                    bandera = false;
                }

                if (!emprendedor.isChecked() && !inversor.isChecked()) {

                    Toast.makeText(MainActivity.this, "Debes de elegir tu tipo de rol", Toast.LENGTH_SHORT).show();
                    bandera = false;
                } else if (emprendedor.isChecked()) {
                    tipo = Usuario.EMPRENDEDOR;
                } else if (inversor.isChecked()) {
                    tipo = Usuario.INVERSOR;
                }


                try {


                    //   int numero = Integer.parseInt(telefono.trim());

                } catch (Exception e) {

                    e.printStackTrace();


                    // Toast.makeText(MainActivity.this, "Introduzca un numero de telefono valido", Toast.LENGTH_SHORT).show();
                    // bandera = false;
                    Log.e("numero fallando", telefono + " " + telefono.length());

                }


                if (bandera) {


                    if (Usuario.INVERSOR.equals(tipo)) {

                        DatabaseReference reference = db.getReference().child("UsuarioInversor");

                        Usuario nuevo = new Usuario();

                        nuevo.setNombre(nombre);
                        nuevo.setEmail(correo);
                        nuevo.setTelefono(telefono);
                        nuevo.setContrasenia(contraseña1);
                        nuevo.setTipo(Usuario.INVERSOR);
                        registrarUsuario(nuevo);

                    } else if (tipo.equals(Usuario.EMPRENDEDOR)) {


                        Usuario nuevo = new Usuario();

                        nuevo.setNombre(nombre);
                        nuevo.setEmail(correo);
                        nuevo.setTelefono(telefono);
                        nuevo.setContrasenia(contraseña1);
                        nuevo.setTipo(Usuario.EMPRENDEDOR);


                        registrarUsuario(nuevo);


                    }


                }


            }
        });


    }


    public void registrarUsuario(final Usuario usuario) {



        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getContrasenia()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "registro Exitoso", Toast.LENGTH_SHORT).show();
                    usuario.setUid(auth.getCurrentUser().getUid());

                    String key = auth.getCurrentUser().getUid();

                    DatabaseReference reference = db.getReference().child(usuario.getTipo()).child(usuario.getUid());

                    reference.setValue(usuario);

                    if(path != null){
                        try {
                            StorageReference ref = firebaseStorage.getReference().child("Proyectos").child(key).child("fotoPerfil");
                            FileInputStream file = new FileInputStream(new File(path));
                            //Sube la foto
                            ref.putStream(file).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity.this,"Registro realizado correctamente",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }catch (FileNotFoundException ex){

                        }
                    }




                    //aqui me voy para la otra actividad

                    Intent i = new Intent(MainActivity.this, Login.class);

                    startActivity(i);

                    finish();
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_GALLERY && resultCode == RESULT_OK){
            path = UtilDomi.getPath(MainActivity.this, data.getData());
            Bitmap m = BitmapFactory.decodeFile(path);
            imageView.setImageDrawable(null);
            imageView.setImageResource(0);
            imageView.setImageBitmap(m);



        }
    }


}
