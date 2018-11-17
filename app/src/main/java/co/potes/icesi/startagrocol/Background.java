package co.potes.icesi.startagrocol;

import android.content.Intent;
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
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import co.potes.icesi.startagrocol.fragments.Fragment_Home;
import co.potes.icesi.startagrocol.fragments.Fragment_Mis_Proyectos;
import co.potes.icesi.startagrocol.fragments.Fragment_Proyectos;
import co.potes.icesi.startagrocol.fragments.Fragment_Publicar;

public class Background extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navegacionMenuLateral;
    private Toolbar tb;

    FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private Button cerrarSesion;


    private Fragment_Home fragment_home;
    private Fragment_Mis_Proyectos fragment_mis_proyectos;
    private Fragment_Proyectos fragment_proyectos;
    private Fragment_Publicar fragment_publicar;


    private Fragment fragmentActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        configureNavigationDrawer();
        setToolbar();
        auth = FirebaseAuth.getInstance();

        fragment_home = new Fragment_Home();
        fragment_mis_proyectos =new Fragment_Mis_Proyectos();
        fragment_proyectos = new Fragment_Proyectos();
        fragment_publicar = new Fragment_Publicar();

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

                else if (itemId == R.id.proyectos) {
                    fragmentActual = fragment_proyectos;
                    tb.setTitle("No configurado aun");

                }

                else if (itemId == R.id.publicar) {
                    fragmentActual = fragment_publicar;
                    tb.setTitle("Publicar");
                }

                else if(itemId == R.id.salir){

                    if(auth.getCurrentUser()!=null){
                        auth.signOut();
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

}
