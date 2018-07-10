package project.googleMapAPI;

public class Location {
    private BoundEntity location;

    public Location(BoundEntity location) {
        this.location = location;
    }

    public BoundEntity getLocation() {
        return location;
    }

    public void setLocation(BoundEntity location) {
        this.location = location;
    }
}
