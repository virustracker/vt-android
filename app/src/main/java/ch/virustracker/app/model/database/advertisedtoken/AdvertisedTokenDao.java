package ch.virustracker.app.model.database.advertisedtoken;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface AdvertisedTokenDao {

    @Query("SELECT * FROM AdvertisedToken")
    List<AdvertisedToken> selectAll();

    @Query("SELECT * FROM AdvertisedToken WHERE tokenValue == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<AdvertisedToken> selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM AdvertisedToken WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<AdvertisedToken> selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(AdvertisedToken... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(AdvertisedToken entry);
}
