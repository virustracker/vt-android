package ch.virustracker.app.controller.startracing;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ch.ubique.android.starsdk.AppConfigManager;
import ch.ubique.android.starsdk.TracingService;
import ch.ubique.android.starsdk.backend.BackendRepository;
import ch.ubique.android.starsdk.backend.ResponseException;
import ch.ubique.android.starsdk.backend.models.Action;
import ch.ubique.android.starsdk.backend.models.Application;
import ch.ubique.android.starsdk.backend.models.ExposedList;
import ch.ubique.android.starsdk.backend.models.Exposee;
import ch.ubique.android.starsdk.database.Database;

public class VtSyncWorker extends Worker {

    private static final String TAG = "ch.ubique.android.starsdk.SyncWorker";

    private static final String PREFS_LAST_KNOWN = "lastKnownPrefs";
    private static final String PREF_LAST_KNOWN_ID = "knownId";

    public static void startSyncWorker(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(ch.ubique.android.starsdk.SyncWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, periodicWorkRequest);
    }

    public static void stopSyncWorker(Context context) {
        WorkManager workManager = WorkManager.getInstance(context);
        workManager.cancelAllWorkByTag(TAG);
    }

    public VtSyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        TracingService.scheduleNextRun(getApplicationContext());

        try {
            doSync(getApplicationContext());
        } catch (IOException | ResponseException e) {
            return Result.retry();
        }

        return Result.success();
    }

    public static void doSync(Context context) throws IOException, ResponseException {
        AppConfigManager appConfigManager = AppConfigManager.getInstance(context);
        appConfigManager.loadSynchronous();
        Application appConfig = appConfigManager.getAppConfig();

        Database database = new Database(context);
        BackendRepository backendRepository =
                new BackendRepository(context, appConfig.getBackendBaseUrl(), appConfig.getListBaseUrl());

        SharedPreferences prefs = context.getSharedPreferences(PREFS_LAST_KNOWN, Context.MODE_PRIVATE);
        int lastKnownId = prefs.getInt(PREF_LAST_KNOWN_ID, -1);
        int newMaxId = lastKnownId;

        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        cal.add(Calendar.DATE, -14);
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMdd");

        for (int i = 0; i <= 14; i++) {
            //TODO use ETAG for already loaded stuff
            String day = sdf.format(cal.getTime());

            ExposedList exposedList = backendRepository.getExposees(day);
            for (Exposee exposee : exposedList.getExposed()) {
                if (exposee.getId() > lastKnownId) {
                    if (exposee.getAction() == Action.ADD) {
                        database.addKnownCase(
                                context,
                                Base64.decode(exposee.getKey(), Base64.NO_WRAP),
                                day
                        );
                    } else {
                        //TODO remove known case
                    }
                    newMaxId = Math.max(newMaxId, exposee.getId());
                }
            }

            cal.add(Calendar.DATE, 1);
            lastKnownId = newMaxId;
            prefs.edit().putInt(PREF_LAST_KNOWN_ID, lastKnownId).apply();
        }
    }

}
