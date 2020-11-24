package vangthao.app.thoitiet.model.places;

public class DistrictOnlyTitleAndSolrID_Sys {
    private String solrID;
    private String title;
    private String email;

    public DistrictOnlyTitleAndSolrID_Sys(DistrictOnlyTitleAndSolrID_Sys districtOnlyTitleAndSolrID_sys) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DistrictOnlyTitleAndSolrID_Sys(){

    }
    public DistrictOnlyTitleAndSolrID_Sys(String solrID, String title, String email) {
        this.solrID = solrID;
        this.title = title;
        this.email = email;
    }

    public String getSolrID() {
        return solrID;
    }

    public void setSolrID(String solrID) {
        this.solrID = solrID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
