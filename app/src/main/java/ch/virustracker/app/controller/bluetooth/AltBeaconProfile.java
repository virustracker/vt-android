package ch.virustracker.app.controller.bluetooth;

/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.UUID;

/**
 * Implementation of the Bluetooth GATT Time Profile.
 * https://www.bluetooth.com/specifications/adopted-specifications
 */
public class AltBeaconProfile {

    private static final String TAG = AltBeaconProfile.class.getSimpleName();

    public static final String ALT_BEACON_SERVICE = "8C422626-0C6E-4B86-8EC7-9147B233D97E";
    public static final String ALT_BEACON_CHARACTERISTIC = "A05F9DF4-9D54-4600-9224-983B75B9D154";


    public static UUID ALTBEACON_SERVICE_UUID = UUID.fromString(ALT_BEACON_SERVICE);
    public static UUID ALTBEACON_ID_CHAR_UUID = UUID.fromString(ALT_BEACON_CHARACTERISTIC);

    /**
     * Return a configured {@link BluetoothGattService} instance for the
     * Current Time Service.
     */
    public static BluetoothGattService createAltBeaconService() {
        BluetoothGattService service = new BluetoothGattService(ALTBEACON_SERVICE_UUID,
                BluetoothGattService.SERVICE_TYPE_PRIMARY);
        BluetoothGattCharacteristic altBeacon = new BluetoothGattCharacteristic(ALTBEACON_ID_CHAR_UUID, BluetoothGattCharacteristic.PROPERTY_READ, BluetoothGattCharacteristic.PERMISSION_READ);
        service.addCharacteristic(altBeacon);
        return service;
    }


}

