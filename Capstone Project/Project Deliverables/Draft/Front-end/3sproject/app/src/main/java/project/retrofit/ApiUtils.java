package project.retrofit;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://192.168.107.75:8080/SpringFinal/api/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
