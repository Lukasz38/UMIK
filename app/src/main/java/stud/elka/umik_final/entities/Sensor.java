package stud.elka.umik_final.entities;

/**
 * Created by Łukasz on 14.12.2017.
 */

public class Sensor {

    private long id;
    private String macAddress;
    private String name;
    private String location;

    public Sensor(long id, String macAddress, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.macAddress = macAddress;
    }

    public Sensor(String macAddress, String name, String location) {
        this.name = name;
        this.location = location;
        this.macAddress = macAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

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
