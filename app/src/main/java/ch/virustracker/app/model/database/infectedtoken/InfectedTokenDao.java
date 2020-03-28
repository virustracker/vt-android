package ch.virustracker.app.model.database.infectedtoken;

import android.database.Cursor;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import ch.virustracker.app.model.database.owntoken.OwnToken;

@Dao
public interface InfectedTokenDao {

    @Query("SELECT * FROM OwnToken")
    List<InfectedToken> selectAll();

    @Query("SELECT * FROM OwnToken WHERE tokenValue == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<InfectedToken> selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM OwnToken WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<InfectedToken> selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(InfectedToken... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(InfectedToken entry);
}
