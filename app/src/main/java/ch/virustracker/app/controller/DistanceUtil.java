package ch.virustracker.app.controller;

public class DistanceUtil {

    public static float getDistanceFromRssi(float rssi) {
        float distance = 25;
        if (rssi > -90) distance = 10;
        if (rssi > -70) distance = 5;
        return distance;
    }
}
