package com.utad.david.planfit.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.R;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imagemenu;
    private TextView name;
    private TextView surname;
    private TextView email;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    @Override

    //Definimos el Toolbar, el DrawerLayout y el NavigationView
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findById();
        setSupportActionBar(toolbar);

        //Muestra en la pantalla un boton que hace visible que es un menu lateral
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Inflamos el layout del header del menu para poder modificar el contenido del header
        LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main_menu_navheader, navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        findByIdNavigetionView();


        putInfoUserInHeaderMenu();

        //Si la foto es null cogemos una por defecto
        checkPhotoUserNull();

        //Nuestro título sera Lessons
        setTitle(R.string.first_nav_name);
        displaySelectedScreen(R.id.nav_deportes);

    }

    public void findById() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    //Se tiene que buscar el id con el navigetionView delante ya que en este elemento se incluye el header del menu
    public void findByIdNavigetionView() {
        imagemenu = navigationView.findViewById(R.id.imagemenu);
        name = navigationView.findViewById(R.id.textviewname_menu);
        surname = navigationView.findViewById(R.id.textviewsurname_menu);
        email = navigationView.findViewById(R.id.textview_email);
    }

    public void putInfoUserInHeaderMenu() {
    }

    public void checkPhotoUserNull() {
    }

    //Sirve para poner la foto que hemos recogido en el PersonalData en la cabecera del menu
    public void putPhotoUser(String stringUri) {
        Uri uri = Uri.parse(stringUri);
        final InputStream imageStream;
        try {
            imageStream = getContentResolver().openInputStream(uri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            RoundedBitmapDrawable roundedDrawable1 =
                    RoundedBitmapDrawableFactory.create(getResources(), selectedImage);

            //asignamos el CornerRadius
            roundedDrawable1.setCornerRadius(selectedImage.getHeight());
            imagemenu.setImageDrawable(roundedDrawable1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Cuando le damos hacia atrás con el menu abierto se cierra el menu
    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Inflamos en el menu el layout del main_menu(botón derecha logout)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //Funcionalidad de Logout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {

        }

        return true;
    }

    //Vacía user
    public void setEmptyItems() {
        SessionUser.getInstance().user.setEmail(null);
        SessionUser.getInstance().user.setPassword(null);
        SessionUser.getInstance().user.setImgUser(null);
        SessionUser.getInstance().user.setNickName(null);
        SessionUser.getInstance().user.setFullName(null);
    }

    //Llamamos a un método propio y le pasamos el id del item pinchado
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    //Este método sirve para cargar los diferentes fragments
    private void displaySelectedScreen(int itemId) {
        //Creamos el objeto fragment
        Fragment fragment = null;

        //iniciamos los fragments dependiendo del item selecionado
        switch (itemId) {
            case R.id.nav_deportes:
                setTitle(R.string.first_nav_name);
                //fragment = new LessonsFragment();
                break;
            case R.id.nav_nutricion:
                setTitle(R.string.two_nav_name);
                //fragment = new NotificationFragment();
                break;
            case R.id.nav_crear_tu_plan:
                setTitle(R.string.three_nav_name);
                //fragment = new NotesFragment();
                break;
            case R.id.nav_comunidad:
                setTitle(R.string.four_nav_name);
                //fragment = new TeachersFragment();
                break;
        }

        //Remplazamos el fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        //Una vez cambiado el fragment cerramos el menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}

/*
        Button button = findViewById(R.id.cerrarsesion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionUser.getInstance().firebaseAdmin.mAuth.getInstance().signOut();
                Intent I=new Intent(MainMenuActivity.this,FirstActivity.class);
                startActivity(I);
            }
        });
 */
