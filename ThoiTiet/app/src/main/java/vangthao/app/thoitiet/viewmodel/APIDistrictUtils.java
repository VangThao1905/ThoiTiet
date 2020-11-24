package vangthao.app.thoitiet.viewmodel;

public class APIDistrictUtils {
    public static final String BaseUrlDistrict = "https://thongtindoanhnghiep.co/";
    public static DistrictService getDataDistrict(){
        return RetrofitClient.getClient(BaseUrlDistrict).create(DistrictService.class);
    }
}
