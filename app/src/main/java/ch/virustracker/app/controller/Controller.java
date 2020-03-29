package ch.virustracker.app.controller;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;
import java.util.List;

import ch.virustracker.app.controller.restapi.RestApiController;
import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;
import ch.virustracker.app.model.database.servertoken.ReportToken;
import ch.virustracker.app.model.proximityevent.IProximityEventProvider;

public class Controller {

    private static final long SEARCH_BACKTIME_MS = 1000 * 60 * 60 * 24 * 20; // search the last 20 days for infected tokens
    private final RestApiController restApiController;
    private IProximityEventProvider proximityEventProvider;

    public Controller() {
        this.restApiController = new RestApiController();
    }

    public void fetchNewInfections() {
        restApiController.fetchInfectedTokens(null);
    }

    public void onNewInfectedTokens(List<ReportToken> reportTokenList) {
        List<ReceiveEvent> seenTokens = VtDatabase.getInstance().receivedTokenDao().selectByTimeSpan(System.currentTimeMillis() - SEARCH_BACKTIME_MS, System.currentTimeMillis());
        //proximityEventProvider.getProximityEvents(seenTokens, infectedTokenList);
    }

    public void startTracking() {
        byte[] tokenValue;
        try {
            tokenValue = Hex.decodeHex(VtApp.getModel().getNewAdvertiseTokenEvent().getTokenValue());
            Beacon beacon = new Beacon.Builder()
                    .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                    .setId2("1")
                    .setId3("2")
                    .setManufacturer(0x0118)
                    .setTxPower(-59)
                    .setDataFields(Arrays.asList(new Long[] {0l}))
                    .build();
            BeaconParser beaconParser = new BeaconParser()
                    .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
            BeaconTransmitter beaconTransmitter = new BeaconTransmitter(VtApp.getContext(), beaconParser);
            beaconTransmitter.startAdvertising(beacon);
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

    public void stopTracking() {

    }
}
