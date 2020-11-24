package vangthao.app.thoitiet.model.places;

public class DistrictOnlyTitleAndSolrID {
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

    public DistrictOnlyTitleAndSolrID(String solrID, String title) {
        this.solrID = solrID;
        this.title = title;
    }
}
