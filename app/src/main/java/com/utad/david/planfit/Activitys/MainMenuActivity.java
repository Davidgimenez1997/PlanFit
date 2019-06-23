package com.utad.david.planfit.Activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.utad.david.planfit.Data.Firebase.FirebaseAdmin;
import com.utad.david.planfit.Data.SessionUser;
import com.utad.david.planfit.DialogFragment.EditPersonalDataUser;
import com.utad.david.planfit.DialogFragment.InfoAboutApp;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.FragmentCreatePlan;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.Create.Nutrition.NutritionCreatePlanFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.Create.Sport.SportCreatePlanFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Favorite.NutritionFavoriteFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Favorite.SportFavoriteFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.RootFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition.NutritionGainVolumeFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition.NutritionSlimmingFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Nutrition.NutritionToningFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.FragmentShowPlan;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.Show.Nutrition.ShowNutritionPlanFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Plan.Show.Sport.ShowSportPlanFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.SportGainVolumeFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.SportSlimmingFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Sport.SportToningFragment;
import com.utad.david.planfit.Fragments.FragmentsMainMenuActivity.Users.UsersFragment;
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
import com.crashlytics.android.Crashlytics;
import com.utad.david.planfit.Utils.Constants;
import com.utad.david.planfit.Utils.Utils;
import com.utad.david.planfit.Utils.UtilsNetwork;
import io.fabric.sdk.android.Fabric;


public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FirebaseAdmin.FirebaseAdminInsertAndDownloandListener,
        RootFragment.Callback,
        EditPersonalDataUser.Callback,
        FragmentCreatePlan.Callback,
        FragmentShowPlan.Callback{


    /******************************** VARIABLES *************************************+/
     *
     */

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


    /******************************** PROGRESS DIALOG Y METODOS *************************************+/
     *
     */

    private ProgressDialog progressDialog;

    public void showLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            return;
        }
        progressDialog = new ProgressDialog(this, R.style.TransparentProgressDialog);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);
        ImageView ivLoading = ButterKnife.findById(progressDialog, R.id.image_cards_animation);
        ivLoading.startAnimation(rotate);
        progressDialog.show();
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideLoading();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideLoading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Fabric.with(this, new Crashlytics());

        SessionUser.getInstance().firebaseAdmin.setFirebaseAdminInsertAndDownloandListener(this);
        SessionUser.getInstance().firebaseAdmin.dowloandDataUserFirebase();

        findById();

        showLoading();
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        setUi();

        LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main_menu_navheader, navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        findByIdNavigetionView();

        setTitle(R.string.titulo_deportes);
        navigateFragmentSport();
        onClickNavigetionHeaderView();
    }


    /******************************** CREA EL ICONO DEL MENU LATERAL *************************************+/
     *
     */


    public void setUi(){
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    /******************************** ONCLICK DE LA CABEZERA DEL MENU *************************************+/
     *
     */

    public void onClickNavigetionHeaderView(){
        navigationHeaderView = navigationView.getHeaderView(0);
        navigationHeaderView.setOnClickListener(v -> {
            if(UtilsNetwork.checkConnectionInternetDevice(this)){
                editUser();
                assert drawer != null;
                drawer.closeDrawer(GravityCompat.START);
            }else{
                Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
            }
        });
    }

    /******************************** CONFIGURACIONES DE LA VISTA *************************************+/
     *
     */

    public void findById() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    public void findByIdNavigetionView() {
        imagemenu = navigationView.findViewById(R.id.imagemenuUser);
        nickname = navigationView.findViewById(R.id.nickNameMenuUser);
        email = navigationView.findViewById(R.id.emailUserMenu);
    }

    /******************************** PONE LA INFORMACION DEL USUARIO EN LA CABEZERA DEL MENU *************************************+/
     *
     */

    public void putInfoUserInHeaderMenu(User user) {
        nickname.setText(user.getNickName());
        email.setText(user.getEmail());
    }

    /******************************** COMPRUEBA LA FOTO DEL USUARIO, SINO PONE UNA POR DEFECTO *************************************+/
     *
     */

    public void checkPhotoUserNull(User user) {
        if(user.getImgUser()!=null){
            putPhotoUser(user.getImgUser());
        }else{
            imagemenu.setImageResource(Utils.PLACEHOLDER_USER);
        }
    }

    /******************************** USA LA LIBRERIA GLIDE PARA PONER UNA FOTO *************************************+/
     *
     */

    private void putPhotoUser(String imgUser) {
        Utils.loadImage(imgUser,imagemenu,Utils.PLACEHOLDER_USER);
    }

    /******************************** BOTON ATRAS DEL TELEFONO *************************************+/
     *
     */

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /******************************** CREA EL MENU DE LA IZQUIERDA *************************************+/
     *
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /******************************** ONCLICK DEL MENU DE LA IZQUIERDA *************************************+/
     *
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_logout:
                if(UtilsNetwork.checkConnectionInternetDevice(this)){
                    logout();
                }else{
                    Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.action_edit_user:
                if(UtilsNetwork.checkConnectionInternetDevice(this)){
                    editUser();
                }else{
                    Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.action_about_app:
                if(UtilsNetwork.checkConnectionInternetDevice(this)){
                    aboutApp();
                }else{
                    Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.polity:
                if(UtilsNetwork.checkConnectionInternetDevice(this)){
                    openPolity();
                }else{
                    Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
                }
                break;
        }

        return true;
    }

    /******************************** METODOS DEL MENU LATERAL *************************************+/
     *
     */

    private void openPolity() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_TITLE, Constants.ConfigurateWebView.TITLE_PRIVACITY);
        intent.putExtra(WebViewActivity.EXTRA_URL, Constants.ConfigurateWebView.URL_PRIVACITY);
        intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_PRIVACITY);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
        startActivity(intent);
    }

    private void aboutApp() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        InfoAboutApp infoAboutApp = new InfoAboutApp();
        infoAboutApp.show(fragmentTransaction,Constants.TagDialogFragment.TAG);
    }

    private void editUser() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        EditPersonalDataUser editPersonalDataUser = new EditPersonalDataUser();
        editPersonalDataUser.show(fragmentTransaction,Constants.TagDialogFragment.TAG);
    }

    private void logout() {
        SessionUser.getInstance().firebaseAdmin.mAuth.getInstance().signOut();
        SessionUser.getInstance().removeUser();
        Intent intent =new Intent(MainMenuActivity.this, FirstActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /******************************** ONCLICK DEL MENU DE LA DERECHA *************************************+/
     *
     */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    /******************************** METODO QUE GESTIONA EL ONCLICK *************************************+/
     *
     */

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        int seleted = 0;

        switch (itemId) {

            case R.id.nav_deportes:
                navigationView.getMenu().findItem(R.id.nav_deportes).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_SPORT;
                fragment = RootFragment.newInstance(seleted);
                break;

            case R.id.nav_nutricion:
                navigationView.getMenu().findItem(R.id.nav_nutricion).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_NUTRITION;
                fragment = RootFragment.newInstance(seleted);
                break;

            case R.id.nav_favorite:
                navigationView.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_FAVORITE;
                fragment = RootFragment.newInstance(seleted);
                break;

            case R.id.nav_crear_tu_plan:
                navigationView.getMenu().findItem(R.id.nav_crear_tu_plan).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_PLAN;
                fragment = RootFragment.newInstance(seleted);
                break;

            case R.id.nav_user:
                navigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                /*
                seleted = Constants.ModeRootFragment.MODE_USER;
                fragment = RootFragment.newInstance(seleted);
                */
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(Constants.ConfigureChat.EXTRA_NAME, SessionUser.getInstance().firebaseAdmin.userDataFirebase.getNickName());
                intent.putExtra(Constants.ConfigureChat.EXTRA_UID, SessionUser.getInstance().firebaseAdmin.userDataFirebase.getUid());
                startActivity(intent);
                break;
        }

        if (fragment != null) {
            int finalSeleted = seleted;
            ((RootFragment) fragment).setToolbarRunnable(() -> {
                switch (finalSeleted){
                    case Constants.ModeRootFragment.MODE_SPORT:
                        setTitle(R.string.titulo_deportes);
                        break;
                    case Constants.ModeRootFragment.MODE_NUTRITION:
                        setTitle(R.string.titulo_nutricion);
                        break;
                    case Constants.ModeRootFragment.MODE_FAVORITE:
                        setTitle(R.string.titulo_favoritos);
                        break;
                    case Constants.ModeRootFragment.MODE_PLAN:
                        setTitle(R.string.titulo_plan);
                        break;
                    case Constants.ModeRootFragment.MODE_USER:
                        //setTitle(R.string.usuarios);
                        break;
                }
            });
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /******************************** CALLBACK DE FIREBASE *************************************+/
     *
     */

    @Override
    public void downloadUserDataInFirebase(boolean end) {
        if(end==true){
            hideLoading();
            putInfoUserInHeaderMenu(SessionUser.getInstance().firebaseAdmin.userDataFirebase);
            checkPhotoUserNull(SessionUser.getInstance().firebaseAdmin.userDataFirebase);
        }else{
            if(SessionUser.getInstance().firebaseAdmin.userDataFirebase.getImgUser()!=null){
                hideLoading();
                putInfoUserInHeaderMenu(SessionUser.getInstance().firebaseAdmin.userDataFirebase);
                checkPhotoUserNull(SessionUser.getInstance().firebaseAdmin.userDataFirebase);
            }
        }
    }

    @Override
    public void insertUserDataInFirebase(boolean end) {}

    @Override
    public void downloadInfotDeveloper(boolean end) {}

    /******************************** CARGA LA PRIMERA PANTALLA *************************************+/
     *
     */

    public void navigateFragmentSport(){
        navigationView.getMenu().findItem(R.id.nav_deportes).setChecked(true);
        Fragment fragment = RootFragment.newInstance(Constants.ModeRootFragment.MODE_SPORT);
        ((RootFragment) fragment).setToolbarRunnable(() -> setTitle(R.string.titulo_deportes));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    /******************************** CALLBACK EDITAR PERFIL *************************************+/
     *
     */

    @Override
    public void updateData(User user) {
        putInfoUserInHeaderMenu(user);
        checkPhotoUserNull(user);
    }

    /******************************** CALLBACK DEPORTE *************************************+/
     *
     */

    @Override
    public void clickOnAdelgazarSport() {
        SportSlimmingFragment sportSlimmingFragment = new SportSlimmingFragment();
        sportSlimmingFragment.newInstanceSlimming();
        sportSlimmingFragment.setToolbarRunnable(() -> setTitle(R.string.deportes_adelgazar));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportSlimmingFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnTonificarSport() {
        SportToningFragment sportToningFragment = new SportToningFragment();
        sportToningFragment.newInstanceSlimming();
        sportToningFragment.setToolbarRunnable(() -> setTitle(R.string.deporte_tonificar));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportToningFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnGanarVolumenSport() {
        SportGainVolumeFragment sportGainVolumeFragment = new SportGainVolumeFragment();
        sportGainVolumeFragment.newInstanceSlimming();
        sportGainVolumeFragment.setToolbarRunnable(() -> setTitle(R.string.deporte_ganar_volumen));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportGainVolumeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /******************************** CALLBACK NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void clickOnAdelgazarNutrition() {
        NutritionSlimmingFragment nutritionSlimmingFragment = new NutritionSlimmingFragment();
        nutritionSlimmingFragment.newInstanceSlimming();
        nutritionSlimmingFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_adelgazar));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionSlimmingFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnTonificarNutrition() {
        NutritionToningFragment nutritionToningFragment = new NutritionToningFragment();
        nutritionToningFragment.newInstanceSlimming();
        nutritionToningFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_tonificar));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionToningFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickOnGanarVolumenNutrition() {
        NutritionGainVolumeFragment nutritionGainVolumeFragment = new NutritionGainVolumeFragment();
        nutritionGainVolumeFragment.newInstanceSlimming();
        nutritionGainVolumeFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_ganar_volumen));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionGainVolumeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /******************************** CALLBACK FAVORITOS *************************************+/
     *
     */

    @Override
    public void clickSportFavorite() {
        SportFavoriteFragment sportFavoriteFragment = new SportFavoriteFragment();
        sportFavoriteFragment.newInstanceSlimming();
        sportFavoriteFragment.setToolbarRunnable(() -> setTitle(R.string.deportes_favoritos));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportFavoriteFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void clickNutritionFavorite() {
        NutritionFavoriteFragment nutritionFavoriteFragment = new NutritionFavoriteFragment();
        nutritionFavoriteFragment.newInstanceSlimming();
        nutritionFavoriteFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_favorita));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionFavoriteFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /******************************** CALLBACK PLAN *************************************+/
     *
     */

    @Override
    public void clickOnCreatePlan() {
        FragmentCreatePlan fragmentCreatePlan = new FragmentCreatePlan();
        fragmentCreatePlan.setToolbarRunnable(() -> setTitle(R.string.crear_plan_title));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragmentCreatePlan);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void clickOnShowPlan() {
        FragmentShowPlan fragmentShowPlan = new FragmentShowPlan();
        fragmentShowPlan.setToolbarRunnable(() -> setTitle(R.string.ver_tu_plan_title));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragmentShowPlan);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /******************************** CALLBACK CREAR PLAN DEPORTE Y NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void onClickSportPlan() {
        SportCreatePlanFragment sportCreatePlanFragment = new SportCreatePlanFragment();
        sportCreatePlanFragment.newInstanceSlimming();
        sportCreatePlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_deporte));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, sportCreatePlanFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClickNutritionPlan() {
        NutritionCreatePlanFragment nutritionCreatePlanFragment = new NutritionCreatePlanFragment();
        nutritionCreatePlanFragment.newInstanceSlimming();
        nutritionCreatePlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_nutricion));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, nutritionCreatePlanFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClickSaveAndExit() {
        Fragment fragment = RootFragment.newInstance(Constants.ModeRootFragment.MODE_PLAN);
        ((RootFragment) fragment).setToolbarRunnable(() -> setTitle(R.string.plan));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    /******************************** CALLBACK VER PLAN DEPORTE Y NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void onClickButtonShowPlanSport() {
        ShowSportPlanFragment showSportPlanFragment = new ShowSportPlanFragment();
        showSportPlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_deporte));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, showSportPlanFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClickButtonShowPlanNutrition() {
        ShowNutritionPlanFragment showNutritionPlanFragment = new ShowNutritionPlanFragment();
        showNutritionPlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_nutricion));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, showNutritionPlanFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onClickButtonShowPlanClose() {
        Fragment fragment = RootFragment.newInstance(Constants.ModeRootFragment.MODE_PLAN);
        ((RootFragment) fragment).setToolbarRunnable(() -> setTitle(R.string.plan));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    /******************************** CALLBACK USUARIOS *************************************+/
     *
     */

    @Override
    public void navigateToUsers() {
        UsersFragment usersFragment = new UsersFragment();
        usersFragment.setToolbarRunnable(() -> setTitle(R.string.usuarios));
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, usersFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}