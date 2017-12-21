package stud.elka.umik_final.entities;

/**
 * Created by Łukasz on 14.12.2017.
 */

public class Sensor {

    private Long id;
    private String name;
    private String location;

    public  Sensor(Long id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return id + ". " + name + " [" + location + "] ";
    }
}
