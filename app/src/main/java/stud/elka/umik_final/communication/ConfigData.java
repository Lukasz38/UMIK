package stud.elka.umik_final.communication;

/**
 * Klasa umożliwiająca tworzenie wiadomości wysyłanych do urządzenia BLE.
 */

public abstract class ConfigData {

    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_GET = "GET";

    public static final String INFO_CODE = "100";
    public static final String FREQ_CODE = "600";
    public static final String LEAK_RANGE_CODE = "700";
    public static final String RESET_CODE = "900";

    /**
     * Metoda umożliwiająca tworzenie wiadomości wysyłanych do urządzenia BLE.
     * @param method nazwa metody
     * @param code kod wartości
     * @param values wartości do ustawienia
     * @return
     */
    public static String createMessage(String method, String code, String[] values) {
        StringBuilder stringBuilder = new StringBuilder(method + ":" + code);
        if(values != null) {
            for (String s : values) {
                stringBuilder.append(s);
            }
        }
        return stringBuilder.toString();
    }
}
