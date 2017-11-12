package test.aximedia.app.aximediatest.data;

public class Picture {
    private long id;
    private String path;

    public Picture(String path) {
        this.path = path;
    }

    public Picture() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
