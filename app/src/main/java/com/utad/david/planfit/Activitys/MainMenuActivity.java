package com.utad.david.planfit.Activitys;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.EditPersonalDataUser;
import com.utad.david.planfit.DialogFragment.InfoAboutApp;
import com.utad.david.planfit.DialogFragment.Sport.SportDetailsDialogFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.CreatePlan.FragmentCreatePlan;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Favorite.NutritionFavorite;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Favorite.SportFavorite;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.FirstFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition.NutritionGainVolumeFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition.NutritionSlimmingFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition.NutritionToningFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.ShowPlan.FragmentShowPlan;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.SportGainVolumeFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.SportSlimmingFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.SportToningFragment;
import com.utad.david.planfit.Model.Sport.DefaultSport;
import com.utad.david.planfit.Model.User;
import com.utad.david.planfit.R;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FirebaseAdmin.FirebaseAdminInsertAndDownloandListener,
        FirstFragment.OnFragmentInteractionListener,
        EditPersonalDataUser.OnFragmentInteractionListener{

    private ImageView imagemenu;
    private TextView nickname;
    private TextView email;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private View navigationHeaderView;

    @Override
    protected void onStart() {
        super.onStart();
        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);
        SessionUser.getInstance().firebaseAdmin.dowloandDataUserFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        findById();
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        setUi();

        LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main_menu_navheader, navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        findByIdNavigetionView();

        setTitle(R.string.first_nav_name);
        navigateFragmentSport();
        onClickNavigetionHeaderView();
    }

    public void setUi(){
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void onClickNavigetionHeaderView(){
        navigationHeaderView = navigationView.getHeaderView(0);
        navigationHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUser();
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public void downloadUserDataInFirebase(boolean end) {
        if(end==true){
            Log.d("DatosUsuarioFirebase"," "+SessionUser.getInstance().firebaseAdmin.userDataFirebase.toString());

            putInfoUserInHeaderMenu(SessionUser.getInstance().firebaseAdmin.userDataFirebase);

            //Si la foto es null cogemos una por defecto
            checkPhotoUserNull(SessionUser.getInstance().firebaseAdmin.userDataFirebase);
        }else{
            if(SessionUser.getInstance().firebaseAdmin.userDataFirebase.getImgUser()!=null){
                Log.d("DatosUsuarioFirebase"," "+SessionUser.getInstance().firebaseAdmin.userDataFirebase.toString());

                putInfoUserInHeaderMenu(SessionUser.getInstance().firebaseAdmin.userDataFirebase);

                //Si la foto es null cogemos una por defecto
                checkPhotoUserNull(SessionUser.getInstance().firebaseAdmin.userDataFirebase);
            }
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
        if(user.getImgUser()!=null){
            putPhotoUser(user.getImgUser());
        }else{
            imagemenu.setImageResource(R.drawable.icon_user);
        }
    }

    private void putPhotoUser(String imgUser) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.icon_user);
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(imgUser).into(imagemenu);
    }

    /*
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
*/

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

    //Inflamos en el menu el layout del main_menu

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
            logout();
        }else if (id == R.id.action_edit_user){
            editUser();
        }else if(id == R.id.action_about_app){
            aboutApp();
        }

        return true;
    }

    private void aboutApp() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        InfoAboutApp infoAboutApp = new InfoAboutApp();
        infoAboutApp.show(fragmentTransaction,"dialog");
    }

    private void editUser() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("dialog");
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        EditPersonalDataUser editPersonalDataUser = new EditPersonalDataUser();
        editPersonalDataUser.show(fragmentTransaction,"dialog");
    }

    private void logout() {
        SessionUser.getInstance().firebaseAdmin.mAuth.getInstance().signOut();
        setEmptyItems();
        Intent intent =new Intent(MainMenuActivity.this,FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
            case R.id.nav_favorite:
                setTitle(R.string.itemfavoritos);
                seleted = 3;
                fragment = FirstFragment.newInstance(seleted);
                break;
        }

        //Remplazamos el fragment
        if (fragment != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //Una vez cambiado el fragment cerramos el menu
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void navigateFragmentSport(){
        int seleted = 0;
        Fragment fragment = FirstFragment.newInstance(seleted);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void updateData(User user) {

        putInfoUserInHeaderMenu(user);

        checkPhotoUserNull(user);

    }


    @Override
    public void clickOnAdelgazarSport() {
        SportSlimmingFragment sportSlimmingFragment = new SportSlimmingFragment();
        sportSlimmingFragment.newInstanceSlimming();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportSlimmingFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnTonificarSport() {
        SportToningFragment sportToningFragment = new SportToningFragment();
        sportToningFragment.newInstanceSlimming();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportToningFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnGanarVolumenSport() {
        SportGainVolumeFragment sportGainVolumeFragment = new SportGainVolumeFragment();
        sportGainVolumeFragment.newInstanceSlimming();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportGainVolumeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnAdelgazarNutrition() {
        NutritionSlimmingFragment nutritionSlimmingFragment = new NutritionSlimmingFragment();
        nutritionSlimmingFragment.newInstanceSlimming();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionSlimmingFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnTonificarNutrition() {
        NutritionToningFragment nutritionToningFragment = new NutritionToningFragment();
        nutritionToningFragment.newInstanceSlimming();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionToningFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnGanarVolumenNutrition() {
        NutritionGainVolumeFragment nutritionGainVolumeFragment = new NutritionGainVolumeFragment();
        nutritionGainVolumeFragment.newInstanceSlimming();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionGainVolumeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnCreatePlan() {
        FragmentCreatePlan fragmentCreatePlan = new FragmentCreatePlan();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragmentCreatePlan);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnShowPlan() {
        FragmentShowPlan fragmentShowPlan = new FragmentShowPlan();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragmentShowPlan);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private List<DefaultSport> allSportFavorite;

    @Override
    public void clickSportFavorite() {
        SportFavorite sportFavorite = new SportFavorite();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportFavorite);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickNutritionFavorite() {
        NutritionFavorite nutritionFavorite = new NutritionFavorite();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionFavorite);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void insertUserDataInFirebase(boolean end) {
        //Metodo implementado pero no se usa
    }


    @Override
    public void downloadInfotDeveloper(boolean end) {
        //Metodo implementado pero no se usa
    }
}