package co.potes.icesi.startagrocol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BannerEmprendedor extends AppCompatActivity {

    private Button btnRegistar;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_emprendedor);

        btnIniciarSesion = findViewById(R.id.btnInicarSesionE);
        btnRegistar = findViewById(R.id.btnRegistrarseE);

        btnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BannerEmprendedor.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i = new Intent(BannerEmprendedor.this,Login.class);
            startActivity(i);
            finish();

        }
    });



    }


}
