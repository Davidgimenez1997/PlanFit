package com.utad.david.planfit.Activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.EditPersonalDataUser;
import com.utad.david.planfit.DialogFragment.InfoAboutApp;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.FirstFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.GainVolumeFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.SlimmingFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.ToningFragment;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,FirebaseAdmin.FirebaseAdminInsertAndDownloandListener,FirstFragment.OnFragmentInteractionListener{

    private ImageView imagemenu;
    private TextView nickname;
    private TextView email;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
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

        //Nuestro título sera Lessons
        setTitle(R.string.first_nav_name);
        displaySelectedScreen(R.id.nav_deportes);

        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);
        SessionUser.getInstance().firebaseAdmin.dowloandDataUserFirebase();

    }

    @Override
    public void downloadUserDataInFirebase(boolean end) {
        if(end==true){
            Log.d("DatosUsuarioFirebase"," "+SessionUser.getInstance().firebaseAdmin.userDataFirebase.toString());

            putInfoUserInHeaderMenu(SessionUser.getInstance().firebaseAdmin.userDataFirebase);

            //Si la foto es null cogemos una por defecto
            checkPhotoUserNull(SessionUser.getInstance().firebaseAdmin.userDataFirebase);
        }
    }

    public void findById() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    //Se tiene que buscar el id con el navigetionView delante ya que en este elemento se incluye el header del menu
    public void findByIdNavigetionView() {
        imagemenu = navigationView.findViewById(R.id.imagemenuUser);
        nickname = navigationView.findViewById(R.id.nickNameMenuUser);
        email = navigationView.findViewById(R.id.emailUserMenu);
    }

    public void putInfoUserInHeaderMenu(User user) {
        nickname.setText(user.getNickName());
        email.setText(user.getEmail());
    }

    public void checkPhotoUserNull(User user) {
        if(user!=null){
            if(user.getImgUser().equals("")){
                imagemenu.setImageResource(R.drawable.icon_user);
            }else{
                putPhotoUser(user.getImgUser());
            }
        }else{
            imagemenu.setImageResource(R.drawable.icon_user);
        }
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
            SessionUser.getInstance().firebaseAdmin.mAuth.getInstance().signOut();
            setEmptyItems();
            Intent intent =new Intent(MainMenuActivity.this,FirstActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if (id == R.id.action_edit_user){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
            if (fragment != null) {
                transaction.remove(fragment);
            }
            EditPersonalDataUser editPersonalDataUser = new EditPersonalDataUser();
            editPersonalDataUser.show(transaction,"dialog");
        }else if(id == R.id.action_about_app){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
            if (fragment != null) {
                transaction.remove(fragment);
            }
            InfoAboutApp infoAboutApp = new InfoAboutApp();
            infoAboutApp.show(transaction,"dialog");
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

        int seleted;

        switch (itemId) {
            case R.id.nav_deportes:
                setTitle(R.string.first_nav_name);
                seleted = 0;
                fragment = FirstFragment.newInstance(seleted);
                break;
            case R.id.nav_nutricion:
                setTitle(R.string.two_nav_name);
                seleted = 1;
                fragment = FirstFragment.newInstance(seleted);
                break;
            case R.id.nav_crear_tu_plan:
                setTitle(R.string.three_nav_name);
                seleted = 2;
                fragment = FirstFragment.newInstance(seleted);
                break;
            case R.id.nav_comunidad:
                setTitle(R.string.four_nav_name);
                seleted = 3;
                fragment = FirstFragment.newInstance(seleted);
                break;
        }

        //Remplazamos el fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        //Una vez cambiado el fragment cerramos el menu
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void clickOnAdelgazarSport() {
        SlimmingFragment slimmingFragment = new SlimmingFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, slimmingFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void clickOnTonificarSport() {
        ToningFragment toningFragment = new ToningFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, toningFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void clickOnGanarVolumenSport() {
        GainVolumeFragment gainVolumeFragment = new GainVolumeFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, gainVolumeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void insertUserDataInFirebase(boolean end) {
        //Metodo implementado pero no se usa
    }


    @Override
    public void downloadInfoFirstDeveloper(boolean end) {
        //Metodo implementado pero no se usa
    }

    @Override
    public void downloadInfoSecondDeveloper(boolean end) {
        //Metodo implementado pero no se usa
    }

}