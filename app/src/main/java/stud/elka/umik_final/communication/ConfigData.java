package stud.elka.umik_final.communication;

/**
 * Created by Łukasz on 25.12.2017.
 */

public class ConfigData {

    private static final String METHOD_PUT = "PUT";
    private static final String FREQ_CODE = "600";

    private int value;

    public ConfigData(int value) {
        this.value = value;
    }

    public String createMessage() {
        return METHOD_PUT + ":" + FREQ_CODE + ":" + value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
