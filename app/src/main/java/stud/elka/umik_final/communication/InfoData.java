package stud.elka.umik_final.communication;

import java.io.Serializable;

/**
 * Klasa reprezentująca dane o aktualnej konfiguracji urządzenia BLE.
 */

public class InfoData implements Serializable {

    private long sensorId;
    private int freqency;
    private int smallLeakRange;
    private int largeLeakRange;

    public InfoData(long sensorId, int freqency, int smallLeakRange, int largeLeakRange) {
        this.sensorId = sensorId;
        this.freqency = freqency;
        this.smallLeakRange = smallLeakRange;
        this.largeLeakRange = largeLeakRange;
    }

    public long getSensorId() {
        return sensorId;
    }

    public void setSensorId(long sensorId) {
        this.sensorId = sensorId;
    }

    public int getFreqency() {
        return freqency;
    }

    public void setFreqency(int freqency) {
        this.freqency = freqency;
    }

    public int getSmallLeakRange() {
        return smallLeakRange;
    }

    public void setSmallLeakRange(int smallLeakRange) {
        this.smallLeakRange = smallLeakRange;
    }

    public int getLargeLeakRange() {
        return largeLeakRange;
    }

    public void setLargeLeakRange(int largeLeakRange) {
        this.largeLeakRange = largeLeakRange;
    }
}
