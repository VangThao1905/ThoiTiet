package vangthao.app.thoitiet.viewmodel;

public class APICityUtils {
    public static final String BaseUrlCity = "https://thongtindoanhnghiep.co/";

    public static CityService getDataDistrict() {
        return RetrofitClient.getClient(BaseUrlCity).create(CityService.class);
    }
}
