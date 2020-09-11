package com.utad.david.planfit.Utils;

import com.utad.david.planfit.R;

public class Constants {

    public interface ModeRootFragment {
        int MODE_SPORT = 0;
        int MODE_NUTRITION = 1;
        int MODE_FAVORITE = 2;
        int MODE_PLAN = 3;
    }

    public interface ModeWebView {
        String EXTRA_MODE = "MODE";
        int MODE_LINKEDIN = 1;
        int MODE_RECIPE = 0;
        int MODE_PRIVACITY = 2;
        int MODE_CHAT = 3;
    }

    public interface ModePlan{
        String YES = "yes";
        String NO = "no";
    }

    public interface TagDialogFragment{
        String TAG = "DIALOG";
    }

    public interface TypesPlanNutrition {
        String DESAYUNO = "Desayuno";
        String COMIDA = "Comida";
        String MERIENDA = "Merienda";
        String CENA = "Cena";
        int MODE_DESAYUNO = 1;
        int MODE_COMIDA = 2;
        int MODE_MERIENDA = 3;
        int MODE_CENA = 4;
    }

    public interface ConfigYoutube {
        String EXTRA_URL = "URL";
        String API_KEY = String.valueOf(R.string.API_KEY);
    }

    public interface ConfigChat {
        String EXTRA_NAME = "NAME";
        String EXTRA_USER = "USER";
    }

    public interface ConfigWebView {
        String EXTRA_TITLE = "EXTRA_TITLE";
        String EXTRA_URL = "EXTRA_URL";
        String TITLE_PRIVACITY = "Politica de Privacidad";
        String URL_PRIVACITY = "https://politicayprivacidadplanfit.000webhostapp.com/planfit.html";
    }

    public interface RequestPermissions {
        int REQUEST_CAMERA = 0;
        int REQUEST_GALLERY = 1;
        int REQUEST_IMAGE_PERMISSIONS = 1;
    }

    public interface NutritionDetails {
        String EXTRA_SLIMMING = "SLIMMING";
        String EXTRA_GAINVOLUME = "GAINVOLUME";
        String EXTRA_TONING = "TONING";
        String EXTRA_OPTION = "OPTION";
    }

    public interface SportDetails {
        String EXTRA_MODE = "MODE";
        String EXTRA_ITEM = "ITEM";
        String EXTRA_URL = "URL";
    }

    public interface SportNutritionOption {
        int SLIMMING = 0;
        int TONING = 1;
        int GAIN_VOLUMEN = 2;
        String EXTRA_MODE = "MODE";
    }

    public interface NutritionFavoriteDetails {
        String EXTRA_NUTRICION = "NUTRITION";
    }

    public interface SportFavoriteDetails {
        String EXTRA_SPORT = "SPORT";
        String EXTRA_URL = "URL";
    }

    public interface SportPlanDetails {
        String EXTRA_SPORT = "SPORT";
    }

    public interface NutritionPlanDetails {
        String EXTRA_NUTRITION = "NUTRITION";
    }
    
    public interface CollectionsNames {
        String USER = "users";
        String USERS = "users/";
        String IMAGES = "images/";
        String DETAILS = "/detalles";
        String ORDER_MESSAGES = "messageTime";

        String SPORTS = "deportes/";
        String SPORTS_FAVORITE = "/deporteFavorito";
        String SPORTS_PLAN = "/planesDeporte";
        String SPORT_SLIMMING = SPORTS + "adelgazar" + DETAILS;
        String SPORT_TONING = SPORTS + "tonificar" + DETAILS;
        String SPORT_GAIN_VOLUME = SPORTS + "ganarVolumen" + DETAILS;

        String NUTRITION = "nutricion/";
        String NUTRITION_FAVORITE = "/nutricionFavorita";
        String NUTRITION_PLAN = "/planesNutricion";
        String NUTRITION_SLIMMING = NUTRITION + "adelgazar" + DETAILS;
        String NUTRITION_TONING = NUTRITION + "tonificar" + DETAILS;
        String NUTRITION_GAIN_VOLUME = NUTRITION + "ganarVolumen" + DETAILS;

        String DEVELOPER = "developer_info";
        String DOCUMENT_DEVELOPER = "david";
    }

    public interface ModelUser {
        String EMAIL = "email";
        String PASSWORD = "password";
        String NAME = "fullName";
        String NICK = "nickName";
        String IMG = "imgUser";
        String UID = "uid";
    }

    public interface ModelSportFavorite {
        String NAME = "name";
        String PHOTO = "photo";
        String VIDEO = "video";
        String DESCRIPTION = "description";
        String TYPE = "type";
        String ADELGAZAR = "adelgazar";
        String TONIFICAR = "tonificar";
        String GANAR_VOLUMEN = "ganarVolumen";
    }

    public interface ModelNutritionFavorite {
        String NAME = "name";
        String PHOTO = "photo";
        String URL = "url";
        String DESCRIPTION = "description";
        String TYPE = "type";
        String ADELGAZAR = "adelgazar";
        String TONIFICAR = "tonificar";
        String GANAR_VOLUMEN = "ganarVolumen";
    }

    public interface ModelSportPlan {
        String NAME = "name";
        String PHOTO = "photo";
        String TIME_START = "timeStart";
        String TIME_END = "timeEnd";
        String IS_OK = "isOk";
        String ID = "id";
    }

    public interface ModelNutritionPlan {
        String NAME = "name";
        String PHOTO = "photo";
        String TYPE = "type";
        String IS_OK = "isOk";
        String ID = "id";
    }

}
