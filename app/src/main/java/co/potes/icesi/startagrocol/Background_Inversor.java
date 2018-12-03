package co.potes.icesi.startagrocol;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import co.potes.icesi.startagrocol.fragments.Fragment_Home;
import co.potes.icesi.startagrocol.fragments.Fragment_Inversiones;
import co.potes.icesi.startagrocol.fragments.Fragment_Proyecto_Completo;
import co.potes.icesi.startagrocol.fragments.Fragment_Publicar;
import co.potes.icesi.startagrocol.fragments.Fragment_mensajes;
import co.potes.icesi.startagrocol.fragments.MensajesFragments;
import co.potes.icesi.startagrocol.fragments.Mis_ProyectosFragment;
import co.potes.icesi.startagrocol.fragments_inversor.Fragment_Inversion;
import co.potes.icesi.startagrocol.fragments_inversor.Fragment_Mis_Inversiones;
import co.potes.icesi.startagrocol.model.Proyecto;

public class Background_Inversor extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private DrawerLayout drawerLayout_inversor;
    private NavigationView navegacionMenuLateral;
    private Toolbar tb;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private GoogleApiClient mgGoogleApiClient;


    private Fragment_Home fragment_home;
    private Fragment_Inversion fragment_inversion_proyecto;
    private Fragment_mensajes fragment_mensajes;
    private Fragment_Proyecto_Completo fragment_proyecto_completo;
    private Fragment_Mis_Inversiones fragment_mis_inversiones;


    private Fragment fragmentActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        configureNavigationDrawer();
        setToolbar();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        View navView = navegacionMenuLateral.getHeaderView(0);


        final ImageView foto = navView.findViewById(R.id.imagePerfil);
        final TextView nombre = navView.findViewById(R.id.tv_nombrePerfil);

        DatabaseReference reference = db.getReference().child("usuarios").child(auth.getCurrentUser().getUid());


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombre.setText((String) dataSnapshot.child("nombre").getValue());
                Picasso.get().load((String) dataSnapshot.child("urlImagen").getValue()).into(foto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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


        fragment_home = new Fragment_Home();
        fragment_mis_inversiones = new Fragment_Mis_Inversiones();
        fragment_mensajes = new Fragment_mensajes();
        fragment_proyecto_completo = new Fragment_Proyecto_Completo();
        fragment_inversion_proyecto = new Fragment_Inversion();


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment_home);
        transaction.commit();
        setTitle("Proyectos");


    }


    private void setToolbar() {
        tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.mipmap.imagen_menu);
    }

    private void configureNavigationDrawer() {
        drawerLayout_inversor = findViewById(R.id.drawerInversor);
        navegacionMenuLateral = findViewById(R.id.navigation_view);
        navegacionMenuLateral.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.inicio) {
                    fragmentActual = fragment_home;
                    tb.setTitle("Proyectos");
                } else if (itemId == R.id.mis_inversiones) {
                    fragmentActual = fragment_mis_inversiones;
                    tb.setTitle("Mis inversiones");
                } else if (itemId == R.id.mensajes_inversor) {
                    fragmentActual = fragment_mensajes;
                    tb.setTitle("Mensajes");
                } else if (itemId == R.id.salir) {

                    if (auth.getCurrentUser() != null) {
                        auth.signOut();

                        Auth.GoogleSignInApi.signOut(mgGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Intent intent = new Intent(Background_Inversor.this, Login.class);
                                startActivity(intent);
                                drawerLayout_inversor.closeDrawers();
                                finish();
                            }
                        });


                        Intent intent = new Intent(Background_Inversor.this, Login.class);
                        startActivity(intent);
                        drawerLayout_inversor.closeDrawers();
                        finish();

                        return true;
                    }

                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_inversor, fragmentActual);
                transaction.commit();
                drawerLayout_inversor.closeDrawers();
                return true;

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            // Android home
            case android.R.id.home:
                drawerLayout_inversor.openDrawer(GravityCompat.START);
                return true;
            // manage other entries if you have it ...
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
