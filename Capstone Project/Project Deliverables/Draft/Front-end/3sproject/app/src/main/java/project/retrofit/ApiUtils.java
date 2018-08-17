package project.retrofit;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://150.95.111.195:8080/3sProjectFinal/api/";

    public static final String BASE_URL_GOOGLE_MAP = "https://maps.googleapis.com/maps/api/geocode/";

    public static final String BASE_URL_FIREBASE = "https://fcm.googleapis.com/fcm/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static APIService getAPIServiceMap() {

        return RetrofitClient.getClient(BASE_URL_GOOGLE_MAP).create(APIService.class);
    }

    public static APIService getAPIServiceFirebaseMessage() {
        return RetrofitClient.getClient(BASE_URL_FIREBASE).create(APIService.class);
    }
}
