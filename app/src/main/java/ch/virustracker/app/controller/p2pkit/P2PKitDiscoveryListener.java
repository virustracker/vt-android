package ch.virustracker.app.controller.p2pkit;

import android.util.Log;

import ch.uepaa.p2pkit.discovery.DiscoveryListener;
import ch.uepaa.p2pkit.discovery.Peer;

public class P2PKitDiscoveryListener implements DiscoveryListener {

        @Override
        public void onStateChanged(final int state) {
                Log.d("P2PKitDiscoveryListener", "State changed: " + state);
        }

        @Override
        public void onPeerDiscovered(final Peer peer) {
                Log.d("P2PKitDiscoveryListener", "Peer discovered: " + peer.getPeerId() + " with info: " + new String(peer.getDiscoveryInfo()));
        }

        @Override
        public void onPeerLost(final Peer peer) {
                Log.d("P2PKitDiscoveryListener", "Peer lost: " + peer.getPeerId());
        }

        @Override
        public void onPeerUpdatedDiscoveryInfo(Peer peer) {
                Log.d("P2PKitDiscoveryListener", "Peer updated: " + peer.getPeerId() + " with new info: " + new String(peer.getDiscoveryInfo()));
        }

        @Override
        public void onProximityStrengthChanged(Peer peer) {
                Log.d("P2PKitDiscoveryListener", "Peer " + peer.getPeerId() + " changed proximity strength: " + peer.getProximityStrength());
        }
}
