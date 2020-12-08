package vangthao.app.thoitiet.viewmodel;

public class APICityUtils {
    public static final String BASE_URL_CITY = "https://thongtindoanhnghiep.co/";

    public static CityService getDataCity() {
        return RetrofitClient.getClient(BASE_URL_CITY).create(CityService.class);
    }
}
