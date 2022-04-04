package core;

// Model Class
public class PathCell extends Cell{

    private String direction;

    public PathCell() {
        super.setCellValue("X");
        this.direction = "";
    }

    protected String getDirection() {
        return direction;
    }

    protected void setDirection(String direction) {
        this.direction = direction;
    }
}
