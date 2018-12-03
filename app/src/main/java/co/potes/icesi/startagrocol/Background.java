package co.potes.icesi.startagrocol;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import co.potes.icesi.startagrocol.fragments.Fragment_Perfil;
import co.potes.icesi.startagrocol.fragments.Fragment_Proyecto_Completo;
import co.potes.icesi.startagrocol.fragments.Fragment_Publicar;
import co.potes.icesi.startagrocol.fragments.MensajesFragments;
import co.potes.icesi.startagrocol.fragments.Mis_ProyectosFragment;
import co.potes.icesi.startagrocol.model.Proyecto;

public class Background extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, MensajesFragments {

    private DrawerLayout drawerLayout;
    private NavigationView navegacionMenuLateral;
    private Toolbar tb;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private GoogleApiClient mgGoogleApiClient;


    private Fragment_Home fragment_home;
    private Mis_ProyectosFragment fragment_mis_proyectos;
    private Fragment_Publicar fragment_publicar;
    private Fragment_Perfil fragment_perfil;
    private Fragment_Proyecto_Completo fragment_proyecto_completo;
    private Fragment_Inversiones fragment_inversiones;


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
        fragment_mis_proyectos = new Mis_ProyectosFragment();
        fragment_publicar = new Fragment_Publicar();
        fragment_proyecto_completo = new Fragment_Proyecto_Completo();
        fragment_inversiones = new Fragment_Inversiones();
        fragment_perfil = new Fragment_Perfil();

        if (auth.getCurrentUser() != null) {

            Bundle datos = new Bundle();

            datos.putString("usuario", auth.getCurrentUser().getUid());

            fragment_publicar.setArguments(datos);


        }


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
        drawerLayout = findViewById(R.id.drawer);
        navegacionMenuLateral = findViewById(R.id.navigation_view);
        navegacionMenuLateral.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.inicio) {
                    fragmentActual = fragment_home;
                    tb.setTitle("Proyectos");
                } else if (itemId == R.id.mis_proyectos) {
                    fragmentActual = fragment_mis_proyectos;
                    tb.setTitle("Mis proyectos");
                } else if (itemId == R.id.Perfil) {
                    fragmentActual = fragment_perfil;
                    tb.setTitle("Perfil");
                } else if (itemId == R.id.salir) {

                    if (auth.getCurrentUser() != null) {
                        auth.signOut();

                        Auth.GoogleSignInApi.signOut(mgGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                Intent intent = new Intent(Background.this, Login.class);
                                startActivity(intent);
                                drawerLayout.closeDrawers();
                                finish();
                            }
                        });


                        Intent intent = new Intent(Background.this, Login.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        finish();

                        return true;
                    }

                }

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, fragmentActual);
                transaction.commit();
                drawerLayout.closeDrawers();
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
                drawerLayout.openDrawer(GravityCompat.START);
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
    public void mensajeFragment(String dato, Proyecto proyecto) {


        Bundle datos = new Bundle();

        datos.putSerializable("proyecto", proyecto);

        fragment_proyecto_completo.setArguments(datos);


        fragmentActual = fragment_proyecto_completo;


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragmentActual);
        transaction.commit();
        drawerLayout.closeDrawers();
        Toast.makeText(Background.this, "posicion selecionada fue: 0" + dato, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void añadirproyecto(String dato) {


        fragmentActual = fragment_publicar;


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragmentActual);
        transaction.commit();
        drawerLayout.closeDrawers();

    }

    @Override
    public void inversores(Proyecto proyecto) {
        Bundle datos = new Bundle();

        datos.putSerializable("proyecto", proyecto);

        fragment_inversiones.setArguments(datos);


        fragmentActual = fragment_inversiones;


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragmentActual);
        transaction.commit();
        drawerLayout.closeDrawers();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
