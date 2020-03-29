package ch.virustracker.app.model.database.advertiseevent;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AdvertisedTokenDao {

    @Query("SELECT * FROM AdvertiseEvent")
    List<AdvertiseEvent> selectAll();

    @Query("SELECT * FROM AdvertiseEvent WHERE tokenValue == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<AdvertiseEvent> selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM AdvertiseEvent WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<AdvertiseEvent> selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(AdvertiseEvent... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(AdvertiseEvent entry);
}
