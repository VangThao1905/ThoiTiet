package vangthao.app.thoitiet.model.places;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class District {

    @SerializedName("LtsItem")
    @Expose
    private List<LtsItem> ltsItem = null;
    @SerializedName("TotalDoanhNghiep")
    @Expose
    private Integer totalDoanhNghiep;

    public List<LtsItem> getLtsItem() {
        return ltsItem;
    }

    public void setLtsItem(List<LtsItem> ltsItem) {
        this.ltsItem = ltsItem;
    }

    public Integer getTotalDoanhNghiep() {
        return totalDoanhNghiep;
    }

    public void setTotalDoanhNghiep(Integer totalDoanhNghiep) {
        this.totalDoanhNghiep = totalDoanhNghiep;
    }

}
