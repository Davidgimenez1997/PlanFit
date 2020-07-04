package com.utad.david.planfit.Data.Sport;

import com.utad.david.planfit.Model.Sport.DefaultSport;
import java.util.List;

public interface GetSport {
    void getSportList(boolean status, List<DefaultSport> list, int mode);
}
