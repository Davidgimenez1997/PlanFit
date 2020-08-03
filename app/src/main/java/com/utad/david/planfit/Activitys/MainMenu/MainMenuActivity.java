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
        this.findByIdNavigationView();
        this.setTitle(R.string.titulo_deportes);
        this.navigateFragmentSport();
        this.onClickNavigationHeaderView();
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

    public void onClickNavigationHeaderView(){
        this.navigationHeaderView = this.navigationView.getHeaderView(0);
        this.navigationHeaderView.setOnClickListener(v -> {
            this.presenter.clickHeaderMenu(this);
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

    public void findByIdNavigationView() {
        this.imagemenu = this.navigationView.findViewById(R.id.imagemenuUser);
        this.nickname = this.navigationView.findViewById(R.id.nickNameMenuUser);
        this.email = this.navigationView.findViewById(R.id.emailUserMenu);
    }

    /******************************** PONE LA INFORMACION DEL USUARIO EN LA CABEZERA DEL MENU *************************************+/
     *
     */

    public void setUserInfoInHeaderMenu(User user) {
        this.nickname.setText(user.getNickName());
        this.email.setText(user.getEmail());
        this.presenter.checkImagenUser(user.getImgUser());
    }

    /******************************** BOTON ATRAS *************************************+/
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
        if (this.presenter.checkInternetDevice(this)) {
            switch (item.getItemId()){
                case R.id.action_logout:
                    this.clickLogout();
                    break;
                case R.id.action_edit_user:
                    this.openEditUserDialog();
                    break;
                case R.id.action_about_app:
                    this.openInfoAboutAppDialog();
                    break;
                case R.id.polity:
                    this.openPolityAppDialog();
                    break;
            }
        }
        return true;
    }

    /******************************** METODOS DEL MENU LATERAL *************************************+/
     *
     */

    private void openPolityAppDialog() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(WebViewActivity.EXTRA_TITLE, Constants.ConfigWebView.TITLE_PRIVACITY);
        intent.putExtra(WebViewActivity.EXTRA_URL, Constants.ConfigWebView.URL_PRIVACITY);
        intent.putExtra(WebViewActivity.EXTRA_MODE, Constants.ModeWebView.MODE_PRIVACITY);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay);
        startActivity(intent);
    }

    private void openInfoAboutAppDialog() {
        this.fragmentTransaction = this.fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (fragment != null) {
            this.fragmentTransaction.remove(fragment);
        }
        InfoAboutAppDialogFragment infoAboutAppDialogFragment = new InfoAboutAppDialogFragment();
        infoAboutAppDialogFragment.show(this.fragmentTransaction,Constants.TagDialogFragment.TAG);
    }

    private void openEditUserDialog() {
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Constants.TagDialogFragment.TAG);
        if (fragment != null) {
            this.fragmentTransaction.remove(fragment);
        }
        EditUserProfilerDialogFragment editUserProfilerDialogFragment = new EditUserProfilerDialogFragment();
        editUserProfilerDialogFragment.show(this.fragmentTransaction,Constants.TagDialogFragment.TAG);
    }

    private void clickLogout() {
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
        this.presenter.onClickDrawerMenu(itemId);
    }

    /******************************** CARGA LA PRIMERA PANTALLA *************************************+/
     *
     */

    public void navigateFragmentSport(){
        this.checkOptionMenuHeader(R.id.nav_deportes);
        RootFragment rootFragment = this.presenter.getRootFragmentByType(Constants.ModeRootFragment.MODE_SPORT);
        rootFragment.setToolbarRunnable(() -> setTitle(R.string.titulo_deportes));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, rootFragment);
        this.fragmentTransaction.commit();
    }

    /******************************** CALLBACK EDITAR PERFIL *************************************+/
     *
     */

    @Override
    public void updateData(User user) {
        this.setUserInfoInHeaderMenu(user);
    }

    /******************************** CALLBACK DEPORTE *************************************+/
     *
     */

    @Override
    public void clickOnAdelgazarSport() {
        this.presenter.clickSportByType(Constants.SportNutritionOption.SLIMMING, R.string.deportes_adelgazar);
    }

    @Override
    public void clickOnTonificarSport() {
        this.presenter.clickSportByType(Constants.SportNutritionOption.TONING, R.string.deporte_tonificar);
    }

    @Override
    public void clickOnGanarVolumenSport() {
        this.presenter.clickSportByType(Constants.SportNutritionOption.GAIN_VOLUMEN, R.string.deporte_ganar_volumen);
    }

    /******************************** CALLBACK NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void clickOnAdelgazarNutrition() {
        this.presenter.clickNutritionByType(Constants.SportNutritionOption.SLIMMING, R.string.nutricion_adelgazar);
    }

    @Override
    public void clickOnTonificarNutrition() {
        this.presenter.clickNutritionByType(Constants.SportNutritionOption.TONING, R.string.nutricion_tonificar);
    }

    @Override
    public void clickOnGanarVolumenNutrition() {
        this.presenter.clickNutritionByType(Constants.SportNutritionOption.GAIN_VOLUMEN, R.string.nutricion_ganar_volumen);
    }

    /******************************** CALLBACK FAVORITOS *************************************+/
     *
     */

    @Override
    public void clickSportFavorite() {
        this.presenter.clickSportFavorite(R.string.deportes_favoritos);
    }

    @Override
    public void clickNutritionFavorite() {
        this.presenter.clickNutritionFavorite(R.string.nutricion_favorita);
    }

    /******************************** CALLBACK PLAN *************************************+/
     *
     */

    @Override
    public void clickOnCreatePlan() {
        this.presenter.clickCreatePlan(R.string.crear_plan_title);
    }


    @Override
    public void clickOnShowPlan() {
        this.presenter.clickShowPlan(R.string.ver_tu_plan_title);
    }

    /******************************** CALLBACK CREAR PLAN DEPORTE Y NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void onClickSportPlan() {
        this.presenter.clickSportPlan(R.string.plan_deporte);
    }

    @Override
    public void onClickNutritionPlan() {
        this.presenter.clickNutritionPlan(R.string.plan_nutricion);
    }

    @Override
    public void onClickSaveAndExit() {
        this.presenter.clickSaveExit(R.string.plan);
    }

    /******************************** CALLBACK VER PLAN DEPORTE Y NUTRICIÓN *************************************+/
     *
     */

    @Override
    public void onClickButtonShowPlanSport() {
        this.presenter.clickShowSportPlan(R.string.plan_deporte);
    }

    @Override
    public void onClickButtonShowPlanNutrition() {
        this.presenter.clickShowNutritionPlan(R.string.plan_nutricion);
    }

    @Override
    public void onClickButtonShowPlanClose() {
        this.presenter.clickSaveExit(R.string.plan);
    }

    /******************************** CALLBACK DEL PRESENTER *************************************+/
     *
     */

    @Override
    public void deviceOfflineMessage() {
        Toast.makeText(this,getString(R.string.info_network_device),Toast.LENGTH_LONG).show();
    }

    @Override
    public void clickHeaderMenu() {
        this.openEditUserDialog();
        assert this.drawer != null;
        this.drawer.closeDrawer(GravityCompat.START);
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
        this.setUserInfoInHeaderMenu(UserRepository.getInstance().getUser());
    }

    @Override
    public void setImagenUser(String imgUser) {
        Utils.loadImage(imgUser, this.imagemenu, Utils.PLACEHOLDER_USER);
    }

    @Override
    public void setDefaultImagen() {
        this.imagemenu.setImageResource(R.drawable.icon_gallery);
    }

    @Override
    public void clickDrawerMenu(int selected, RootFragment rootFragment, int itemId) {
        if (rootFragment != null) {
            this.checkOptionMenuHeader(itemId);
            this.setTitleFragmentOptionSelected(selected, rootFragment);
            this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
            this.fragmentTransaction.replace(R.id.content_frame, rootFragment);
            this.fragmentTransaction.addToBackStack(null);
            this.fragmentTransaction.commit();
            this.closeDrawerMenu();
        }
    }

    private RootFragment setTitleFragmentOptionSelected(int selected, RootFragment fragmentOptionSelected) {
        fragmentOptionSelected.setToolbarRunnable(() -> {
            switch (selected){
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
        return fragmentOptionSelected;
    }

    @Override
    public void clickChatDrawerMenu(int itemId) {
        this.checkOptionMenuHeader(itemId);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(Constants.ConfigChat.EXTRA_NAME, UserRepository.getInstance().getUser().getNickName());
        startActivityForResult(intent,22);
        this.closeDrawerMenu();
    }

    private void checkOptionMenuHeader(int itemId) {
        this.navigationView.getMenu().findItem(itemId).setChecked(true);
    }

    private void closeDrawerMenu() {
        this.drawer = findViewById(R.id.drawer_layout);
        this.drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void clickSportByType(int type, int title) {
        SportFragment sportFragment = new SportFragment();
        sportFragment.newInstance(type);
        sportFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickNutritionByType(int type, int tilte) {
        NutritionFragment nutritionFragment = new NutritionFragment();
        nutritionFragment.newInstance(type);
        nutritionFragment.setToolbarRunnable(() -> setTitle(tilte));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickSportFavorite(int title) {
        SportFavoriteFragment sportFavoriteFragment = new SportFavoriteFragment();
        sportFavoriteFragment.newInstance();
        sportFavoriteFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportFavoriteFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickNutritionFavorite(int title) {
        NutritionFavoriteFragment nutritionFavoriteFragment = new NutritionFavoriteFragment();
        nutritionFavoriteFragment.newInstance();
        nutritionFavoriteFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionFavoriteFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickCreatePlan(int title) {
        FragmentCreatePlan fragmentCreatePlan = new FragmentCreatePlan();
        fragmentCreatePlan.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, fragmentCreatePlan);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickShowPlan(int title) {
        FragmentShowPlan fragmentShowPlan = new FragmentShowPlan();
        fragmentShowPlan.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, fragmentShowPlan);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickSportPlan(int title) {
        SportCreatePlanFragment sportCreatePlanFragment = new SportCreatePlanFragment();
        sportCreatePlanFragment.newInstanceSlimming();
        sportCreatePlanFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, sportCreatePlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickNutritionPlan(int title) {
        NutritionCreatePlanFragment nutritionCreatePlanFragment = new NutritionCreatePlanFragment();
        nutritionCreatePlanFragment.newInstanceSlimming();
        nutritionCreatePlanFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, nutritionCreatePlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickSaveExit(int title) {
        RootFragment rootFragment = this.presenter.getRootFragmentByType(Constants.ModeRootFragment.MODE_PLAN);
        rootFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, rootFragment);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickShowSportPlan(int title) {
        ShowSportPlanFragment showSportPlanFragment = new ShowSportPlanFragment();
        showSportPlanFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, showSportPlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }

    @Override
    public void clickShowNutritionPlan(int title) {
        ShowNutritionPlanFragment showNutritionPlanFragment = new ShowNutritionPlanFragment();
        showNutritionPlanFragment.setToolbarRunnable(() -> setTitle(title));
        this.fragmentTransaction = getSupportFragmentManager().beginTransaction();
        this.fragmentTransaction.replace(R.id.content_frame, showNutritionPlanFragment);
        this.fragmentTransaction.addToBackStack(null);
        this.fragmentTransaction.commit();
    }
}