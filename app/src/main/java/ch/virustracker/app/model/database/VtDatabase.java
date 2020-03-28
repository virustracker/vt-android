package ch.virustracker.app.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import ch.virustracker.app.model.database.location.Location;
import ch.virustracker.app.model.database.location.LocationDao;
import ch.virustracker.app.model.database.owntoken.OwnToken;
import ch.virustracker.app.model.database.owntoken.OwnTokenDao;
import ch.virustracker.app.model.database.seentoken.SeenToken;
import ch.virustracker.app.model.database.seentoken.SeenTokenDao;

@Database(entities = {OwnToken.class, SeenToken.class, Location.class}, version = 1, exportSchema = false)
public abstract class VtDatabase extends RoomDatabase {

    /** The only instance */
    private static VtDatabase sInstance;

    /**
     * Gets the singleton instance of VtDatabase.
     *
     * @param context The context.
     * @return The singleton instance of VtDatabase.
     */
    public static synchronized VtDatabase getInstance(Context context) {
        if (sInstance == null || !sInstance.isOpen()) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), VtDatabase.class, "vt-log-data").fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }

    public static synchronized void closeDb() {
        if (sInstance != null && !sInstance.isOpen()) {
            sInstance.close();
        }
    }

    public abstract OwnTokenDao ownTokenDao();
    public abstract SeenTokenDao seenTokenDao();
    public abstract LocationDao locationDao();
}
