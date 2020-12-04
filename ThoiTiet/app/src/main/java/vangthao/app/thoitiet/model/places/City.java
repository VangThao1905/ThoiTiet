package vangthao.app.thoitiet.model.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {

    @SerializedName("LtsItem")
    @Expose
    private List<LtsItem> ltsItem = null;
    @SerializedName("TotalDoanhNghiep")
    @Expose
    private Integer totalDoanhNghiep;

    public List<LtsItem> getLtsItem() {
        return ltsItem;
    }

    public void setTotalDoanhNghiep(Integer totalDoanhNghiep) {
        this.totalDoanhNghiep = totalDoanhNghiep;
    }

}
