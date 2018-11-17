package co.potes.icesi.startagrocol;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RestaurarContrasenia extends AppCompatActivity implements View.OnClickListener {

    private EditText correo;
    private Button btn_recuperar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurar_contrasenia);

        correo = findViewById(R.id.et_email);
        btn_recuperar = findViewById(R.id.btn_recuperar);
        btn_recuperar.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        if(v.equals(btn_recuperar)){


            String email = correo.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Por favor ingrese su correo", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RestaurarContrasenia.this, "Ya te enviamos un correo con las instrucciones para recuperar tu contraseña!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RestaurarContrasenia.this, "¡Fallo en recuperar contraseña!", Toast.LENGTH_SHORT).show();
                            }
                            }
                    });

        }
    }
}
