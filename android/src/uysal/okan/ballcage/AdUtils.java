package uysal.okan.ballcage;

import com.google.android.gms.ads.AdRequest;


public class AdUtils {

    public static AdRequest buildRequest() {
        return new AdRequest.Builder()
                .build();
    }

    private AdUtils() {}
}
