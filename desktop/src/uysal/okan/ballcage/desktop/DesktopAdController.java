package uysal.okan.ballcage.desktop;

import com.badlogic.gdx.utils.Logger;

import org.lwjgl.Sys;

import uysal.okan.ballcage.AdController;


public class DesktopAdController implements AdController {

    // == constants ==
    private static final Logger log = new Logger(DesktopAdController.class.getSimpleName());

    // == public methods ==
    @Override
    public void showBanner() {
        log.debug("show banner");
        System.out.println("banner");
    }

    @Override
    public void showInterstitial() {
        log.debug("show interstitial");
    }

    @Override
    public boolean isNetworkConnected() {
        return false;
    }

    @Override
    public void hideandShowBanner() {

    }

    @Override
    public void hideBanner() {

    }

    @Override
    public void ShowBanner() {

    }

    @Override
    public void connect() {

    }

    @Override
    public void unlockAchievement(int n) {

    }
}
