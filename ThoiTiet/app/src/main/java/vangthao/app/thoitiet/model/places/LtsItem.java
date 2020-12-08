package vangthao.app.thoitiet.model.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LtsItem {

    @SerializedName("Type")
    @Expose
    private Integer type;
    @SerializedName("SolrID")
    @Expose
    private String solrID;
    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("Title")
    @Expose
    private String title;

    public LtsItem() {
    }
    @SerializedName("STT")
    @Expose
    private Integer sTT;
    @SerializedName("Created")
    @Expose
    private Object created;
    @SerializedName("Updated")
    @Expose
    private Object updated;
    @SerializedName("TotalDoanhNghiep")
    @Expose
    private Integer totalDoanhNghiep;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSolrID() {
        return solrID;
    }

    public void setSolrID(String solrID) {
        this.solrID = solrID;
    }

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSTT() {
        return sTT;
    }

    public void setSTT(Integer sTT) {
        this.sTT = sTT;
    }

    public Object getCreated() {
        return created;
    }

    public void setCreated(Object created) {
        this.created = created;
    }

    public Object getUpdated() {
        return updated;
    }

    public void setUpdated(Object updated) {
        this.updated = updated;
    }

    public Integer getTotalDoanhNghiep() {
        return totalDoanhNghiep;
    }

    public void setTotalDoanhNghiep(Integer totalDoanhNghiep) {
        this.totalDoanhNghiep = totalDoanhNghiep;
    }

}
