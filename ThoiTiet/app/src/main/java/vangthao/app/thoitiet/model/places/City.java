package vangthao.app.thoitiet.model.places;

public class City {
    private int id;
    private String solrId;
    private String title;
    private String email;

    public City(String solrId, String title) {
        this.solrId = solrId;
        this.title = title;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public City(int id, String solrId, String title, String email) {
        this.id = id;
        this.solrId = solrId;
        this.title = title;
        this.email = email;
    }

    public City(City districtOnlyTitleAndSolrID_sys) {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public City(){

    }

    public String getSolrId() {
        return solrId;
    }

    public void setSolrId(String solrId) {
        this.solrId = solrId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
