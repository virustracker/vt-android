package ch.virustracker.app.model.database.receivedtoken;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ReceivedTokenDao {

    @Query("SELECT * FROM ReceivedToken")
    List<ReceivedToken> selectAll();

    @Query("SELECT * FROM ReceivedToken WHERE tokenValue == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<ReceivedToken> selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM ReceivedToken WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<ReceivedToken> selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(ReceivedToken... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(ReceivedToken entry);
}
