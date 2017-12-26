package stud.elka.umik_final.communication;

import android.content.Intent;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasa reprezentująca dane przesyłane pomiędzy urządzeniem a telefonem
 * @author mateuszwojciechowski
 */

public class Data implements Serializable{

    public static final int SMALL_LEAK_CODE = 200;
    public static final int LARGE_LEAK_CODE = 300;
    public static final int BIG_LEAK_CODE = 400;

    private Date timestamp;
    private int code;

    /**
     * Konstruktor domyślny klasy, ustawia dane na 0
     */
    public Data() {
        timestamp = new Date();
        code = 0;
    }

    /**
     * Konstruktor klasy wykorzystujący otrzymaną wiadomość z urządzenia
     * @param cmd wiadomość otrzymana z urządzenia
     */
    public Data(String cmd) {
        timestamp = new Date();
        code = Integer.valueOf(cmd);
    }

    /**
     * Funkcja zwracająca odczyt kodu zdarzenia
     * @return kod zdarzenia
     */
    public float getCode() {
        return code;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(getTimestampString() + " ");
        if(code == SMALL_LEAK_CODE) {
            stringBuilder.append("SMALL LEAK");
        } else if (code == LARGE_LEAK_CODE) {
            stringBuilder.append("LARGE LEAK");
        } else if (code == BIG_LEAK_CODE) {
            stringBuilder.append("BIG LEAK");
        } else {
            stringBuilder.append("Unknown event");
        }
        return stringBuilder.toString();
    }
}