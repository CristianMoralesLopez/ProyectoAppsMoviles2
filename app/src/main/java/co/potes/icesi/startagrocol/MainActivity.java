package co.potes.icesi.startagrocol;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.potes.icesi.startagrocol.model.Usuario;

public class MainActivity extends AppCompatActivity {


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


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


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

                    DatabaseReference reference = db.getReference().child(usuario.getTipo()).child(usuario.getUid());

                    reference.setValue(usuario);


                    //aqui me voy para la otra actividad

                    Intent i = new Intent(MainActivity.this, Login.class);

                    startActivity(i);

                    finish();
                }
            }
        });

    }


}
