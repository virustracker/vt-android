package ch.virustracker.app.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.dpppt.android.sdk.DP3T;
import org.dpppt.android.sdk.internal.AppConfigManager;

import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import ch.virustracker.app.R;
import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.controller.androidpermissions.PermissionController;
import ch.virustracker.app.model.database.proximityevent.Distance;
import ch.virustracker.app.model.proximityevent.ProximityEvent;
import ch.virustracker.app.model.proximityevent.ProximityEventSummary;

import static ch.virustracker.app.controller.VtApp.getContext;

public class MainActivity extends PermissionController {

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 12246;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 1;
    private static final int REQUEST_CODE_SAVE_DB = 2;
    private static final int REQUEST_CODE_REPORT_EXPOSED = 3;

    private static final DateFormat DATE_FORMAT_SYNC = SimpleDateFormat.getDateTimeInstance();

    private static final String REGEX_VALIDITY_AUTH_CODE = "\\w+";
    private static final int EXPOSED_MIN_DATE_DIFF = -21;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_main);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        initUi();
    }

    private void initUi() {
        Button enableTracingButton = ((Button) findViewById(R.id.enableTracingButton));
        enableTracingButton.setOnClickListener(v -> {
            if (AppConfigManager.getInstance(VtApp.getContext()).isReceivingEnabled()) {
                VtApp.getController().stopTracking();
            } else {
                VtApp.getController().startTracking(this);
            }
            updateEnableButtonState();
        });
    }

    private void updateUi() {
        updateEnableButtonState();
        updateProximityEventList();
    }

    private void updateProximityEventList() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.proximityEventList);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        new Thread(() -> {
            List<ProximityEvent> proximityEvents = VtApp.getModel().getEncountersForTimeSpan(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 5, System.currentTimeMillis());
            runOnUiThread(() -> ((TextView)findViewById(R.id.stats)).setText(VtApp.getContext().getString(R.string.tracing_stats, proximityEvents.size())));
            List<ProximityEventSummary> proximityEventsByDay = ProximityEventSummary.getProximityEventsByDay(proximityEvents);
            ProximityEventListAdapter mAdapter = new ProximityEventListAdapter(proximityEventsByDay);
            runOnUiThread(() -> recyclerView.setAdapter(mAdapter));
        }).start();
    }

    private void updateEnableButtonState() {
        Button enableTracingButton = ((Button) findViewById(R.id.enableTracingButton));
        if (AppConfigManager.getInstance(VtApp.getContext()).isReceivingEnabled()) {
            enableTracingButton.setBackgroundColor(getColor(R.color.buttonActive));
            enableTracingButton.setText(R.string.monitoring_enabled);
        } else {
            enableTracingButton.setBackgroundColor(getColor(R.color.buttonInactive));
            enableTracingButton.setText(R.string.monitoring_disabled);
        }
    }

    private BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            }
        }
    };

    private BroadcastReceiver sdkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        //VtApp.getController().fetchNewInfections();
        getContext().registerReceiver(bluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        getContext().registerReceiver(sdkReceiver, DP3T.getUpdateIntentFilter());
        updateUi();
    }

    @Override
    public void onPause() {
        super.onPause();
        getContext().unregisterReceiver(bluetoothReceiver);
        getContext().unregisterReceiver(sdkReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SAVE_DB && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                OutputStream targetOut = getContext().getContentResolver().openOutputStream(uri);
                DP3T.exportDb(getContext(), targetOut, () ->
                        new Handler(getContext().getMainLooper()).post(() -> {}));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return;
        } else if (requestCode == REQUEST_CODE_REPORT_EXPOSED) {
            if (resultCode == Activity.RESULT_OK) {

            }
        }
    }

}
