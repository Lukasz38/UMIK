package stud.elka.umik_final.communication;

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Klasa reprezentująca dane o wycieku przesyłane pomiędzy urządzeniem a telefonem.
 * @author mateuszwojciechowski
 */

public class Data implements Serializable{

    public static final int SMALL_LEAK_CODE = 200;
    public static final int LARGE_LEAK_CODE = 300;
    public static final int BIG_LEAK_CODE = 400;

    private static final String TAG = "Data";
    private static final String DATE_FORMAT = "yyyy.MM.dd HH:mm:ss.SSS";

    private long id;
    private long sensorID;
    private int code;
    private Date timestamp;

    /**
     * Konstruktor klasy wykorzystujący otrzymaną wiadomość z urządzenia
     * @param cmd wiadomość otrzymana z urządzenia
     */
    public Data(long sensorID, String cmd) {
        timestamp = new Date();
        code = Integer.valueOf(cmd);
        this.sensorID = sensorID;
    }

    public Data(long id, long sensorID, int code, String timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        try {
            this.timestamp = dateFormat.parse(timestamp);
        } catch (ParseException pe) {
            Log.e(TAG, "Parsing date failed! Date as string: " + timestamp + ". Exception", pe);
        }
        this.id = id;
        this.sensorID = sensorID;
        this.code = code;
    }

    public long getID() {
        return id;
    }

    public long getSensorID() {
        return sensorID;
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
        return new SimpleDateFormat(DATE_FORMAT).format(timestamp);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(getTimestampString() + " ");
        stringBuilder.append(sensorID + ". ");
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
