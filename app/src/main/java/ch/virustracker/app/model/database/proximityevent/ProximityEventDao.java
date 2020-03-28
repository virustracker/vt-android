package ch.virustracker.app.model.database.proximityevent;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import ch.virustracker.app.model.proximityevent.ProximityEvent;

@Dao
public interface ProximityEventDao {

    @Query("SELECT * FROM ProximityEvent")
    List<ProximityEvent> selectAll();

    @Query("SELECT * FROM ProximityEvent WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<ProximityEvent> selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(ProximityEvent... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(ProximityEvent entry);

}
