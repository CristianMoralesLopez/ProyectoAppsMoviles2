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
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import co.potes.icesi.startagrocol.fragments.Fragment_Home;
import co.potes.icesi.startagrocol.fragments.Fragment_Mis_Proyectos;
import co.potes.icesi.startagrocol.fragments.Fragment_Publicar;

public class Background extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navegacionMenuLateral;
    private Toolbar tb;

    private FirebaseAuth auth;
    private GoogleApiClient mgGoogleApiClient;



    private Fragment_Home fragment_home;
    private Fragment_Mis_Proyectos fragment_mis_proyectos;
    private Fragment_Publicar fragment_publicar;


    private Fragment fragmentActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        configureNavigationDrawer();
        setToolbar();
        auth = FirebaseAuth.getInstance();


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
        fragment_mis_proyectos =new Fragment_Mis_Proyectos();
        fragment_publicar = new Fragment_Publicar();


        if (auth.getCurrentUser()!=null){

            Bundle datos = new Bundle();

            datos.putString("usuario",auth.getCurrentUser().getUid());

            fragment_publicar.setArguments(datos);




        }



        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment_home);
        transaction.commit();
        setTitle("Proyectos");


    }


    private void setToolbar(){
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
                }
                else if (itemId == R.id.mis_proyectos) {
                    fragmentActual = fragment_mis_proyectos;
                    tb.setTitle("Mis proyectos");
                }

                else if (itemId == R.id.publicar) {
                    fragmentActual = fragment_publicar;
                    tb.setTitle("Publicar");
                }

                else if(itemId == R.id.salir){

                    if(auth.getCurrentUser()!=null){
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
        switch(itemId) {
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
