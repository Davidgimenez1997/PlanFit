package com.utad.david.planfit.Data.Sport;

import com.utad.david.planfit.Model.Sport.SportGainVolume;
import com.utad.david.planfit.Model.Sport.SportSlimming;
import com.utad.david.planfit.Model.Sport.SportToning;

import java.util.List;

public interface GetSport {
    void getSlimmingSports(boolean status, List<SportSlimming> data);
    void getToningSports (boolean status, List<SportToning> data);
    void getGainVolumeSports(boolean status, List <SportGainVolume> data);
}
