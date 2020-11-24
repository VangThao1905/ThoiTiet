package vangthao.app.thoitiet.viewmodel;

public class APICityUtils {
    public static final String BaseUrlDistrict = "https://thongtindoanhnghiep.co/";
    public static CityService getDataDistrict(){
        return RetrofitClient.getClient(BaseUrlDistrict).create(CityService.class);
    }
}
