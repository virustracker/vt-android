package ch.virustracker.app.controller.p2pkit;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import ch.uepaa.p2pkit.AlreadyEnabledException;
import ch.uepaa.p2pkit.P2PKit;
import ch.uepaa.p2pkit.P2PKitStatusListener;
import ch.uepaa.p2pkit.discovery.DiscoveryInfoTooLongException;
import ch.uepaa.p2pkit.discovery.DiscoveryPowerMode;
import ch.virustracker.app.controller.VtApp;

public class P2PKitTrackerController implements ITrackerController {

    private static final String APP_KEY = "SECRET";

    private final P2PKitStatusListener statusListener = new P2PStatusListener();
    private final P2PKitDiscoveryListener discoveryListener = new P2PKitDiscoveryListener();

    @Override
    public void startTracker() {
        try {
            P2PKit.enable(VtApp.getContext(), APP_KEY, statusListener);
        } catch (AlreadyEnabledException e) {
            e.printStackTrace();
        }
        try {
            P2PKit.enableProximityRanging();
            P2PKit.startDiscovery(Hex.decodeHex(VtApp.getModel().getNewAdvertiseTokenEvent().getTokenValue()), DiscoveryPowerMode.HIGH_PERFORMANCE, discoveryListener);
        } catch (DiscoveryInfoTooLongException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopTracker() {
        P2PKit.stopDiscovery();
    }
}
