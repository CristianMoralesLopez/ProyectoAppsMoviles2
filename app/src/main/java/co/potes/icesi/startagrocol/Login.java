package co.potes.icesi.startagrocol;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.potes.icesi.startagrocol.model.Usuario;

public class Login extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {


    public final static int SIGN_IN_CODE = 777;

    private EditText et_email;
    private EditText et_pass;
    private Button btn_login;
    private Button btn_registro;
    private Button btn_recuperar;


    private SignInButton btn_login_google;
    private GoogleApiClient mgGoogleApiClient;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        /*
        se inicializa el cliente para la autentificacion
         */


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        /*
        se inicializa el metodo de autentificacion en este caso google
         */


        mgGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        /*
        condicional si el usuario ya se encuentra logueado se pasa directamente al home
        y se finaliza la actividad
         */

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(auth.getCurrentUser()!=null){
                    Intent i = new Intent(Login.this, Background.class);


                    startActivity(i);

                }

            }
        };


        if (auth.getCurrentUser() != null) {
            Intent i = new Intent(this, Background.class);

            startActivity(i);

            finish();

            return;
        } else {
            Log.e("NULL", "esta entrando en NULL");

        }

        /*
        inicializacion de las variables
         */

        btn_login_google = findViewById(R.id.btn_login_google);
        btn_login_google.setOnClickListener(this);

        et_email = findViewById(R.id.et_email);
        et_pass = findViewById(R.id.et_pass);
        btn_login = findViewById(R.id.btn_login);
        btn_registro = findViewById(R.id.btn_registro);
        btn_recuperar = findViewById(R.id.btn_recuperarContrasenia);
        btn_recuperar.setOnClickListener(this);
        btn_registro.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

        btn_login_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(mgGoogleApiClient);
                startActivityForResult(i, 9001);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9001) {


            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult resultado) {

        if (resultado.isSuccess()) {
            FirebaseAuthAuthWithGoogle(resultado.getSignInAccount());
        } else {
            Toast.makeText(Login.this, "Login con google fallido", Toast.LENGTH_SHORT).show();

        }
    }

    private void FirebaseAuthAuthWithGoogle(GoogleSignInAccount signInAccount) {

        AuthCredential credential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);

        String id = signInAccount.getId();
        String nombre = signInAccount.getDisplayName();
        String correo = signInAccount.getEmail();
        String telefono = "sin telefono";
        String contraseña = "logueado con google";
        String tipo = Usuario.EMPRENDEDOR;

        Usuario usuario = new Usuario();

        usuario.setUid(id);
        usuario.setNombre(nombre);
        usuario.setEmail(correo);
        usuario.setTelefono(telefono);
        usuario.setContrasenia(contraseña);
        usuario.setTipo(tipo);

        DatabaseReference reference = db.getReference().child(usuario.getTipo()).child(id);

        reference.setValue(usuario);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"la autentificacion con google fallo",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.equals(btn_login)) {

            loginUsuario(et_email.getText().toString(), et_pass.getText().toString());

        } else if (v.equals(btn_registro)) {

            Intent i = new Intent(this.getApplicationContext(), MainActivity.class);
            startActivity(i);
        } else if (v.equals(btn_recuperar)) {
            Intent i = new Intent(this.getApplicationContext(), RestaurarContrasenia.class);
            startActivity(i);
        }


    }

    private void loginUsuario(String email, String pass) {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(Login.this, "la contraseña esta correcta", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Background.class);

                    startActivity(i);

                    finish();
                } else {

                    Toast.makeText(Login.this, "la contraseña esta mala pirtobo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(firebaseAuthListener);


        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mgGoogleApiClient);

        if (opr.isDone()) {

            GoogleSignInResult result = opr.get();

            verificarLogueo(result);


        } else {

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    verificarLogueo(googleSignInResult);

                }
            });

        }

    }

    private void verificarLogueo(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            firebasecongoogle(result.getSignInAccount());
        }
    }

    private void firebasecongoogle(GoogleSignInAccount signInAccount) {




    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            auth.removeAuthStateListener(firebaseAuthListener);


        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

       // if (firebaseAuthListener != null) {
        //   auth.removeAuthStateListener(firebaseAuthListener);
    //
        //   }

    }
}
