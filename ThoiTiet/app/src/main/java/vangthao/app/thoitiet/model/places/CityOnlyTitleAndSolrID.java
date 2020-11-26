package vangthao.app.thoitiet.model.places;

public class CityOnlyTitleAndSolrID {
    private String solrID;
    private String title;

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

    public CityOnlyTitleAndSolrID(String solrID, String title) {
        this.solrID = solrID;
        this.title = title;
    }
}
