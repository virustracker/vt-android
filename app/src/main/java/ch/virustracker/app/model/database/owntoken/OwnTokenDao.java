package ch.virustracker.app.model.database.owntoken;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface OwnTokenDao {

    @Query("SELECT * FROM OwnToken")
    List<OwnToken> selectAll();

    @Query("SELECT * FROM OwnToken WHERE tokenValue == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<OwnToken> selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM OwnToken WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<OwnToken> selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(OwnToken... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(OwnToken entry);
}
