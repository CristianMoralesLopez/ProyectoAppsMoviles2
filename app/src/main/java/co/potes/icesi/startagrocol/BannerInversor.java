package co.potes.icesi.startagrocol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BannerInversor extends AppCompatActivity {

    private Button btnRegistar;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_inversor);

        btnIniciarSesion = findViewById(R.id.btnInicarSesionI);
        btnRegistar = findViewById(R.id.btnRegistrarseI);

        btnRegistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BannerInversor.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(BannerInversor.this,Login.class);
                startActivity(i);
                finish();

            }
        });
    }
}
