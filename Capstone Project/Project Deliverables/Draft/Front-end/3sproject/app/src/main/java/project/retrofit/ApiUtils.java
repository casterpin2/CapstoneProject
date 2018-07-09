package project.retrofit;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://192.168.107.101:8080/3sProjectFinal/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
