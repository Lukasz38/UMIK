package stud.elka.umik_final.entities;

/**
 * Created by Łukasz on 14.12.2017.
 */

public class Sensor {

    private Long id;
    private String name;
    private String location;
    private String macAddress;

    public  Sensor(String name, String location, String macAddress) {
        this.name = name;
        this.location = location;
        this.macAddress = macAddress;
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

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public String toString() {
        return id + ". " + name + " [" + location + "] ";
    }
}
