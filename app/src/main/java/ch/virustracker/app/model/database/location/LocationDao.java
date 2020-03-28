package ch.virustracker.app.model.database.location;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface LocationDao {
    
    @Query("SELECT * FROM Location")
    Cursor selectAll();

    @Query("SELECT * FROM Location WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    Cursor selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(Location... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(Location entry);

}
