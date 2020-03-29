package ch.virustracker.app.model.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.model.database.advertiseevent.AdvertiseEvent;
import ch.virustracker.app.model.database.advertiseevent.AdvertisedTokenDao;
import ch.virustracker.app.model.database.proximityevent.ProximityEventDao;
import ch.virustracker.app.model.database.receiveevent.ReceiveEvent;
import ch.virustracker.app.model.database.receiveevent.ReceivedTokenDao;
import ch.virustracker.app.model.proximityevent.ProximityEvent;

@Database(entities = {AdvertiseEvent.class, ReceiveEvent.class, ProximityEvent.class}, version = 3, exportSchema = false)
public abstract class VtDatabase extends RoomDatabase {

    /** The only instance */
    private static VtDatabase sInstance;

    /**
     * Gets the singleton instance of VtDatabase.
     *
     * @return The singleton instance of VtDatabase.
     */
    public static synchronized VtDatabase getInstance() {
        if (sInstance == null || !sInstance.isOpen()) {
            sInstance = Room
                    .databaseBuilder(VtApp.getContext(), VtDatabase.class, "vt-log-data").fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }

    public static synchronized void closeDb() {
        if (sInstance != null && !sInstance.isOpen()) {
            sInstance.close();
        }
    }

    public abstract AdvertisedTokenDao advertiseEventDao();
    public abstract ReceivedTokenDao receivedTokenDao();
    public abstract ProximityEventDao proximityEventDao();
}
