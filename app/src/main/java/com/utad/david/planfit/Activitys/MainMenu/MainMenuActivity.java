package com.utad.david.planfit.Activitys.MainMenu;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.utad.david.planfit.Activitys.AuthenticationActivity;
import com.utad.david.planfit.Activitys.Chat.ChatActivity;
import com.utad.david.planfit.Activitys.WebView.WebViewActivity;
import com.utad.david.planfit.Base.BaseActivity;
import com.utad.david.planfit.Data.User.User.UserRepository;
import com.utad.david.planfit.DialogFragment.User.EditUserProfiler.EditUserProfilerDialogFragment;
import com.utad.david.planfit.DialogFragment.User.InfoAboutApp.InfoAboutAppDialogFragment;
import com.utad.david.planfit.Fragments.MainMenu.Nutrition.NutritionFragment;
import com.utad.david.planfit.Fragments.MainMenu.Plan.FragmentCreatePlan;
import com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Nutrition.NutritionCreatePlanFragment;
import com.utad.david.planfit.Fragments.MainMenu.Plan.Create.Sport.SportCreatePlanFragment;
import com.utad.david.planfit.Fragments.MainMenu.Favorite.Nutrition.NutritionFavoriteFragment;
import com.utad.david.planfit.Fragments.MainMenu.Favorite.Sport.SportFavoriteFragment;
import com.utad.david.planfit.Fragments.MainMenu.RootFragment;
import com.utad.david.planfit.Fragments.MainMenu.Plan.FragmentShowPlan;
import com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Nutrition.ShowNutritionPlanFragment;
import com.utad.david.planfit.Fragments.MainMenu.Plan.Show.Sport.ShowSportPlanFragment;
import com.utad.david.planfit.Fragments.MainMenu.Sport.SportFragment;
import com.utad.david.planfit.Model.User.User;
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
import com.utad.david.planfit.Utils.SharedPreferencesManager;
import com.utad.david.planfit.Utils.Utils;
import io.fabric.sdk.android.Fabric;

public class MainMenuActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainMenuView,
        RootFragment.Sport,
        RootFragment.Nutrition,
        RootFragment.Favorite,
        RootFragment.Plan,
        EditUserProfilerDialogFragment.Callback,
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
    private MainMenuPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.presenter = new MainMenuPresenter(this);
        if (this.presenter.checkInternetDevice(this)) {
            Fabric.with(this, new Crashlytics());
            this.showLoading();
            this.presenter.loadData();
        }

        this.findById();
        this.setSupportActionBar(toolbar);
        this.fragmentManager = getSupportFragmentManager();
        this.setUi();
        LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_main_menu_navheader, navigationView);
        this.navigationView.setNavigationItemSelectedListener(this);
        this.findByIdNavigetionView();
        this.setTitle(R.string.titulo_deportes);
        this.navigateFragmentSport();
        this.onClickNavigetionHeaderView();
        SharedPreferencesManager.clearAllSharedPreferences(getApplicationContext());
    }

    /******************************** CREA EL ICONO DEL MENU LATERAL *************************************+/
     *
     */

    public void setUi(){
        this.toggle = new ActionBarDrawerToggle(
                this, this.drawer, this.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(this.toggle);
        this.toggle.syncState();
    }

    /******************************** ONCLICK DE LA CABEZERA DEL MENU *************************************+/
     *
     */

    public void onClickNavigetionHeaderView(){
        this.navigationHeaderView = this.navigationView.getHeaderView(0);
        this.navigationHeaderView.setOnClickListener(v -> {
            if (this.presenter.checkInternetDevice(this)) {
                this.editUser();
                assert this.drawer != null;
                this.drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    /******************************** CONFIGURACIONES DE LA VISTA *************************************+/
     *
     */

    public void findById() {
        this.toolbar = findViewById(R.id.toolbar);
        this.drawer = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.nav_view);
    }

    public void findByIdNavigetionView() {
        this.imagemenu = this.navigationView.findViewById(R.id.imagemenuUser);
        this.nickname = this.navigationView.findViewById(R.id.nickNameMenuUser);
        this.email = this.navigationView.findViewById(R.id.emailUserMenu);
    }

    /******************************** PONE LA INFORMACION DEL USUARIO EN LA CABEZERA DEL MENU *************************************+/
     *
     */

    public void putInfoUserInHeaderMenu(User user) {
        this.nickname.setText(user.getNickName());
        this.email.setText(user.getEmail());
    }

    /******************************** COMPRUEBA LA FOTO DEL USUARIO, SINO PONE UNA POR DEFECTO *************************************+/
     *
     */

    public void checkPhotoUserNull(User user) {
        if (user.getImgUser() != null) {
            this.putPhotoUser(user.getImgUser());
        } else {
            this.imagemenu.setImageResource(Utils.PLACEHOLDER_USER);
        }
    }

    /******************************** USA LA LIBRERIA GLIDE PARA PONER UNA FOTO *************************************+/
     *
     */

    private void putPhotoUser(String imgUser) {
        Utils.loadImage(imgUser, this.imagemenu, Utils.PLACEHOLDER_USER);
    }

    /******************************** BOTON ATRAS DEL TELEFONO *************************************+/
     *
     */

    @Override
    public void onBackPressed() {
        this.drawer = findViewById(R.id.drawer_layout);
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
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
                if (this.presenter.checkInternetDevice(this)) {
                    this.logout();
                }
                break;
            case R.id.action_edit_user:
                if (this.presenter.checkInternetDevice(this)) {
                    this.editUser();
                }
                break;
            case R.id.action_about_app:
                if (this.presenter.checkInternetDevice(this)) {
                    this.aboutApp();
                }
                break;
            case R.id.polity:
                if (this.presenter.checkInternetDevice(this)) {
                    this.openPolity();
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
        intent.putExtra(WebViewActivity.EXTRA_TITLE, Constants.ConfigWebView.TITLE_PRIVACITY);
        intent.putExtra(WebViewActivity.EXTRA_URL, Constants.ConfigWebView.URL_PRIVACITY);
        intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_PRIVACITY);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
        startActivity(intent);
    }

    private void aboutApp() {
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (fragment != null) {
            this.fragmentTransaction.remove(fragment);
        }
        InfoAboutAppDialogFragment infoAboutAppDialogFragment = new InfoAboutAppDialogFragment();
        infoAboutAppDialogFragment.show(this.fragmentTransaction,Constants.TagDialogFragment.TAG);
    }

    private void editUser() {
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (fragment != null) {
            this.fragmentTransaction.remove(fragment);
        }
        EditUserProfilerDialogFragment editUserProfilerDialogFragment = new EditUserProfilerDialogFragment();
        editUserProfilerDialogFragment.show(this.fragmentTransaction,Constants.TagDialogFragment.TAG);
    }

    private void logout() {
        this.presenter.logout();
    }

    /******************************** ONCLICK DEL MENU DE LA DERECHA *************************************+/
     *
     */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        this.displaySelectedScreen(item.getItemId());
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
                this.navigationView.getMenu().findItem(R.id.nav_deportes).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_SPORT;
                fragment = RootFragment.newInstance(seleted);
                break;
            case R.id.nav_nutricion:
                this.navigationView.getMenu().findItem(R.id.nav_nutricion).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_NUTRITION;
                fragment = RootFragment.newInstance(seleted);
                break;
            case R.id.nav_favorite:
                this.navigationView.getMenu().findItem(R.id.nav_favorite).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_FAVORITE;
                fragment = RootFragment.newInstance(seleted);
                break;
            case R.id.nav_crear_tu_plan:
                this.navigationView.getMenu().findItem(R.id.nav_crear_tu_plan).setChecked(true);
                seleted = Constants.ModeRootFragment.MODE_PLAN;
                fragment = RootFragment.newInstance(seleted);
                break;
            case R.id.nav_user:
                this.navigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(Constants.ConfigChat.EXTRA_NAME, UserRepository.getInstance().getUser().getNickName());
                startActivityForResult(intent,22);
                break;
        }

        if (fragment != null) {
            int finalSeleted = seleted;
            ((RootFragment) fragment).setToolbarRunnable(() -> {
                switch (finalSeleted){
                    case Constants.ModeRootFragment.MODE_SPORT:
                        this.setTitle(R.string.titulo_deportes);
                        break;
                    case Constants.ModeRootFragment.MODE_NUTRITION:
                        this.setTitle(R.string.titulo_nutricion);
                        break;
                    case Constants.ModeRootFragment.MODE_FAVORITE:
                        this.setTitle(R.string.titulo_favoritos);
                        break;
                    case Constants.ModeRootFragment.MODE_PLAN:
                        this.setTitle(R.string.titulo_plan);
                        break;
                }
            });
            this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
            this.fragmentTransaction.replace(R.id.content_frame, fragment);
            this.fragmentTransaction.addToBackStack(null);
            this.fragmentTransaction.commit();
        }

        this.drawer = findViewById(R.id.drawer_layout);
        this.drawer.closeDrawer(GravityCompat.START);
    }


    /******************************** CARGA LA PRIMERA PANTALLA *************************************+/
     *
     */

    public void navigateFragmentSport(){
        this.navigationView.getMenu().findItem(R.id.nav_deportes).setChecked(true);
        Fragment fragment = RootFragment.newInstance(Constants.ModeRootFragment.MODE_SPORT);
        ((RootFragment) fragment).setToolbarRunnable(() -> setTitle(R.string.titulo_deportes));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, fragment);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK EDITAR PERFIL *************************************+/
     *
     */

    @Override
    public void updateData(User user) {
        this.putInfoUserInHeaderMenu(user);
        this.checkPhotoUserNull(user);
    }

    /******************************** CALLBACK DEPORTE *************************************+/
     *
     */

    @Override
    public void clickOnAdelgazarSport() {
        SportFragment sportFragment = new SportFragment();
        sportFragment.newInstance(Constants.SportNutritionOption.SLIMMING);
        sportFragment.setToolbarRunnable(() -> setTitle(R.string.deportes_adelgazar));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickOnTonificarSport() {
        SportFragment sportFragment = new SportFragment();
        sportFragment.newInstance(Constants.SportNutritionOption.TONING);
        sportFragment.setToolbarRunnable(() -> setTitle(R.string.deporte_tonificar));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickOnGanarVolumenSport() {
        SportFragment sportFragment = new SportFragment();
        sportFragment.newInstance(Constants.SportNutritionOption.GAIN_VOLUMEN);
        sportFragment.setToolbarRunnable(() -> setTitle(R.string.deporte_ganar_volumen));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void clickOnAdelgazarNutrition() {
        NutritionFragment nutritionFragment = new NutritionFragment();
        nutritionFragment.newInstance(Constants.SportNutritionOption.SLIMMING);
        nutritionFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_adelgazar));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickOnTonificarNutrition() {
        NutritionFragment nutritionFragment = new NutritionFragment();
        nutritionFragment.newInstance(Constants.SportNutritionOption.TONING);
        nutritionFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_tonificar));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickOnGanarVolumenNutrition() {
        NutritionFragment nutritionFragment = new NutritionFragment();
        nutritionFragment.newInstance(Constants.SportNutritionOption.GAIN_VOLUMEN);
        nutritionFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_ganar_volumen));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK FAVORITOS *************************************+/
     *
     */

    @Override
    public void clickSportFavorite() {
        SportFavoriteFragment sportFavoriteFragment = new SportFavoriteFragment();
        sportFavoriteFragment.newInstance();
        sportFavoriteFragment.setToolbarRunnable(() -> setTitle(R.string.deportes_favoritos));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportFavoriteFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickNutritionFavorite() {
        NutritionFavoriteFragment nutritionFavoriteFragment = new NutritionFavoriteFragment();
        nutritionFavoriteFragment.newInstance();
        nutritionFavoriteFragment.setToolbarRunnable(() -> setTitle(R.string.nutricion_favorita));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionFavoriteFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK PLAN *************************************+/
     *
     */

    @Override
    public void clickOnCreatePlan() {
        FragmentCreatePlan fragmentCreatePlan = new FragmentCreatePlan();
        fragmentCreatePlan.setToolbarRunnable(() -> setTitle(R.string.crear_plan_title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, fragmentCreatePlan);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }


    @Override
    public void clickOnShowPlan() {
        FragmentShowPlan fragmentShowPlan = new FragmentShowPlan();
        fragmentShowPlan.setToolbarRunnable(() -> setTitle(R.string.ver_tu_plan_title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, fragmentShowPlan);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK CREAR PLAN DEPORTE Y NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void onClickSportPlan() {
        SportCreatePlanFragment sportCreatePlanFragment = new SportCreatePlanFragment();
        sportCreatePlanFragment.newInstanceSlimming();
        sportCreatePlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_deporte));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportCreatePlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void onClickNutritionPlan() {
        NutritionCreatePlanFragment nutritionCreatePlanFragment = new NutritionCreatePlanFragment();
        nutritionCreatePlanFragment.newInstanceSlimming();
        nutritionCreatePlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_nutricion));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionCreatePlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void onClickSaveAndExit() {
        Fragment fragment = RootFragment.newInstance(Constants.ModeRootFragment.MODE_PLAN);
        ((RootFragment) fragment).setToolbarRunnable(() -> setTitle(R.string.plan));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, fragment);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK VER PLAN DEPORTE Y NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void onClickButtonShowPlanSport() {
        ShowSportPlanFragment showSportPlanFragment = new ShowSportPlanFragment();
        showSportPlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_deporte));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, showSportPlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void onClickButtonShowPlanNutrition() {
        ShowNutritionPlanFragment showNutritionPlanFragment = new ShowNutritionPlanFragment();
        showNutritionPlanFragment.setToolbarRunnable(() -> setTitle(R.string.plan_nutricion));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, showNutritionPlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void onClickButtonShowPlanClose() {
        Fragment fragment = RootFragment.newInstance(Constants.ModeRootFragment.MODE_PLAN);
        ((RootFragment) fragment).setToolbarRunnable(() -> setTitle(R.string.plan));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, fragment);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void navigateLogout() {
        Intent intent =new Intent(MainMenuActivity.this, AuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void getUserData() {
        this.hideLoading();
        this.putInfoUserInHeaderMenu(UserRepository.getInstance().getUser());
        this.checkPhotoUserNull(UserRepository.getInstance().getUser());
    }
}