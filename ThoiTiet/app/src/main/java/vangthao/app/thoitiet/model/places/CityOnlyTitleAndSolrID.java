package vangthao.app.thoitiet.model.places;

public class CityOnlyTitleAndSolrID {
    private String solrId;
    private String title;

    public String getSolrId() {
        return solrId;
    }

    public void setSolrID(String solrId) {
        this.solrId = solrId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CityOnlyTitleAndSolrID(String solrId, String title) {
        this.solrId = solrId;
        this.title = title;
    }
}
