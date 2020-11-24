package vangthao.app.thoitiet.model.places;

public class CityOnlyTitleAndSolrID_Sysn {
    private String solrID;
    private String title;
    private String email;

    public CityOnlyTitleAndSolrID_Sysn(CityOnlyTitleAndSolrID_Sysn districtOnlyTitleAndSolrID_sys) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CityOnlyTitleAndSolrID_Sysn(){

    }
    public CityOnlyTitleAndSolrID_Sysn(String solrID, String title, String email) {
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
