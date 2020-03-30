package ch.virustracker.app.controller.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.ParcelUuid;
import android.util.Base64;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.model.database.VtDatabase;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;
import ch.virustracker.app.view.MainActivity;

import static android.content.Context.BLUETOOTH_SERVICE;

public class AltBeaconTrackerController extends AdvertiseCallback implements ITrackerController {

    protected static final String TAG = "Beacon";
    private static final long CONN_TIME_THRESH = 1000 * 60;

    private BluetoothLeAdvertiser advertiser;
    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothManager mBluetoothManager;
    private HashMap<String, Long> lastConnection = new HashMap<>();
    private HashMap<String, Integer> lastRssi = new HashMap<>();


    @Override
    public void startTracker(MainActivity mainActivity) {
        startAdvertising(mainActivity);
        startServer();
        startScanning();
    }

    private void startScanning() {
        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).setReportDelay(1).build();
        List<ScanFilter> filters = new LinkedList<>();
        ScanFilter.Builder sfb = new ScanFilter.Builder();
        sfb.setServiceUuid(new ParcelUuid(AltBeaconProfile.ALTBEACON_SERVICE_UUID));
        filters.add(sfb.build());
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
        bluetoothAdapter.getBluetoothLeScanner().startScan(filters, scanSettings, scanCallback);
    }

    private void startAdvertising(MainActivity mainActivity) {
        advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
        ParcelUuid pUuid = new ParcelUuid( AltBeaconProfile.ALTBEACON_SERVICE_UUID);
        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName( true )
                .addServiceUuid( pUuid )
                .build();
        mBluetoothManager = (BluetoothManager) mainActivity.getSystemService(BLUETOOTH_SERVICE);
        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
                .setConnectable(true)
                .setTimeout(0)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
                .build();

        advertiser.startAdvertising(settings, data, advertiseCallback);
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    @Override
    public void stopTracker() {
        advertiser.stopAdvertising(advertiseCallback);;
        stopServer();
        stopScanning();
    }

    private void stopScanning() {
        BluetoothAdapter bluetoothAdapter = mBluetoothManager.getAdapter();
        bluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
    }

    /**
     * Initialize the GATT server instance with the services/characteristics
     * from the Time Profile.
     */
    private void startServer() {
        mBluetoothGattServer = mBluetoothManager.openGattServer(VtApp.getContext(), mGattServerCallback);
        if (mBluetoothGattServer == null) {
            Log.w(TAG, "Unable to create GATT server");
            return;
        }
        mBluetoothGattServer.addService(AltBeaconProfile.createAltBeaconService());
    }

    /**
     * Shut down the GATT server.
     */
    private void stopServer() {
        if (mBluetoothGattServer == null) return;
        mBluetoothGattServer.close();
    }

    /**
     * Callback to receive information about the advertisement process.
     */
    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) { Log.i(TAG, "LE Advertise Started."); }
        @Override
        public void onStartFailure(int errorCode) {
            Log.w(TAG, "LE Advertise Failed: "+errorCode);
        }
    };

    /**
     * Callback to handle incoming requests to the GATT server.
     * All read/write requests for characteristics and descriptors are handled here.
     */
    private BluetoothGattServerCallback mGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(TAG, "BluetoothDevice CONNECTED: " + device);
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "BluetoothDevice DISCONNECTED: " + device);
            }
        }
        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            //Log.i(TAG, "Read UUID");
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, 0, Base64.decode(VtApp.getModel().getNewAdvertiseTokenEvent().getTokenValue(), Base64.NO_WRAP));
        }

    };

    private BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            lastConnection.put(gatt.getDevice().getAddress(), System.currentTimeMillis());
            BluetoothGattService service = gatt.getService(AltBeaconProfile.ALTBEACON_SERVICE_UUID);
            if (service != null) {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(AltBeaconProfile.ALTBEACON_ID_CHAR_UUID);
                if (characteristic != null) {
                    gatt.readCharacteristic(characteristic);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            //Log.v(TAG, "Char: " + characteristic.getStringValue(0));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Integer rssi = lastRssi.get(gatt.getDevice().getAddress());
                    if (rssi != null) {
                        float distance = 25;
                        if (rssi > -90) distance = 10;
                        if (rssi > -70) distance = 5;
                        ReceiveEvent receiveEvent = new ReceiveEvent(System.currentTimeMillis(), Base64.encodeToString(characteristic.getValue(), Base64.NO_WRAP), distance);
                        VtDatabase.getInstance().receivedTokenDao().insertAll(receiveEvent);
                    }
                }
            }).start();
            gatt.disconnect();
        }


    };

    private ScanCallback scanCallback = new ScanCallback() {

        @Override
        public void onScanFailed(int errorCode) { Log.v(TAG, "Scan failed " + errorCode); }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            for (ScanResult result : results) onScanResult(0, result);
        }

        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //Log.v(TAG, "Scan Response: " + result.getScanRecord().getServiceUuids().size());
            if (!lastConnection.containsKey(result.getDevice().getAddress()) || System.currentTimeMillis() - lastConnection.get(result.getDevice().getAddress()) > CONN_TIME_THRESH) {
                lastRssi.put(result.getDevice().getAddress(), result.getRssi());
                result.getDevice().connectGatt(VtApp.getContext(), false, gattCallback);
            }
        }
    };

}
