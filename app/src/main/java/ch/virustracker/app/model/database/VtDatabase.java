package ch.virustracker.app.model.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ch.virustracker.app.controller.VtApp;
import ch.virustracker.app.model.database.infectedtoken.InfectedToken;
import ch.virustracker.app.model.database.infectedtoken.InfectedTokenDao;
import ch.virustracker.app.model.database.location.Location;
import ch.virustracker.app.model.database.location.LocationDao;
import ch.virustracker.app.model.database.advertisedtoken.AdvertisedToken;
import ch.virustracker.app.model.database.advertisedtoken.AdvertisedTokenDao;
import ch.virustracker.app.model.database.receivedtoken.ReceivedToken;
import ch.virustracker.app.model.database.receivedtoken.ReceivedTokenDao;

@Database(entities = {AdvertisedToken.class, ReceivedToken.class, InfectedToken.class, Location.class}, version = 2, exportSchema = false)
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

    public abstract AdvertisedTokenDao advertisedTokenDao();
    public abstract ReceivedTokenDao receivedTokenDao();
    public abstract InfectedTokenDao infectedTokenDao();
    public abstract LocationDao locationDao();
}
