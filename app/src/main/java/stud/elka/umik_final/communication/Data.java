package stud.elka.umik_final.communication;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasa reprezentująca dane przesyłane pomiędzy urządzeniem a telefonem
 * @author mateuszwojciechowski
 */

public class Data {
    private Date timestamp;
    private float acceleration;

    /**
     * Konstruktor domyślny klasy, ustawia dane na 0
     */
    public Data() {
        timestamp = new Date();
        acceleration = 0;
    }

    /**
     * Konstruktor klasy wykorzystujący otrzymaną wiadomość z urządzenia
     * @param cmd wiadomość otrzymana z urządzenia
     */
    public Data(String cmd) {
        timestamp = new Date();
        acceleration = Float.valueOf(cmd);
    }

    /**
     * Funkcja zwracająca odczyt przyspieszenia
     * @return przyspieszenie
     */
    public float getAcceleration() {
        return acceleration;
    }

    /**
     * Funkcja zwracająca czas zdarzenia
     * @return czas zdarzenia
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Funkcja zwracająca czas zdarzenia w postaci String
     * @return czas zdarzenia
     */
    public String getTimestampString() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS ").format(timestamp);
    }
}
