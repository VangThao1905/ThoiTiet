package vangthao.app.thoitiet.model.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityResponse {

    @SerializedName("LtsItem")
    @Expose
    private final List<LtsItem> ltsItem = null;
    @SerializedName("TotalDoanhNghiep")
    @Expose
    private Integer totalEnterprise;

    public List<LtsItem> getLtsItem() {
        return ltsItem;
    }

    public void setTotalDoanhNghiep(Integer totalEnterprise) {
        this.totalEnterprise = totalEnterprise;
    }

}
