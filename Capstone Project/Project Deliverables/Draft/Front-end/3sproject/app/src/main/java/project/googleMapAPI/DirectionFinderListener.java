package project.googleMapAPI;

import java.util.List;


public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
    void onDirectionFinderFailed();
}
