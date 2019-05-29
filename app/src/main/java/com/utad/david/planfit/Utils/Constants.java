package com.utad.david.planfit.Utils;

import com.utad.david.planfit.R;

public class Constants {

    public interface ModeRootFragment{
        int MODE_SPORT = 0;
        int MODE_NUTRITION = 1;
        int MODE_FAVORITE = 2;
        int MODE_PLAN = 3;
    }

    public interface ModeWebView{
        String EXTRA_MODE = "MODE";
        int MODE_LINKEDIN = 1;
        int MODE_RECIPE = 0;
    }

    public interface ConfigureYouTube{
        String EXTRA_URL = "URL";
        String API_KEY = String.valueOf(R.string.API_KEY);
    }

    public interface ConfigurateWebView{
        String EXTRA_TITLE = "EXTRA_TITLE";
        String EXTRA_URL = "EXTRA_URL";
    }

    public interface NutritionDetails{
        String EXTRA_SLIMMING = "SLIMMING";
        String EXTRA_GAINVOLUME = "GAINVOLUME";
        String EXTRA_TONING = "TONING";
        String EXTRA_OPTION = "OPTION";
    }

    public interface DeportesDetails{
        String EXTRA_SLIMMING = "SLIMMING";
        String EXTRA_GAINVOLUME = "GAINVOLUME";
        String EXTRA_TONING = "TONING";
        String EXTRA_OPTION = "OPTION";
        String EXTRA_URL = "URL";
    }

    public interface RequestPermisos{
        int REQUEST_GALLERY = 1;
        int REQUEST_IMAGE_PERMISSIONS = 1;
    }

    public interface NutricionFavoriteDetails {
        String EXTRA_NUTRICION = "NUTRITION";
    }

    public interface DeportesFavoriteDetails {
        String EXTRA_SPORT = "SPORT";
        String EXTRA_URL = "URL";
    }

    public interface DeportesPlanDetails{
        String EXTRA_SPORT = "SPORT";
    }

    public interface NutricionPlanDetails{
        String EXTRA_NUTRITION = "NUTRITION";
    }

    public interface TagDialogFragment{
        String TAG = "DIALOG";
    }

    public interface TiposPlanNutricion{
        String DESAYUNO = "DESAYUNO";
        String COMIDA = "COMIDA";
        String MERIENDA = "MERIENDA";
        String CENA = "CENA";
        int MODE_DESAYUNO = 1;
        int MODE_COMIDA = 2;
        int MODE_MERIENDA = 3;
        int MODE_CENA = 4;
    }

    public interface ModePlan{
        String YES = "yes";
        String NO = "no";
    }
}
