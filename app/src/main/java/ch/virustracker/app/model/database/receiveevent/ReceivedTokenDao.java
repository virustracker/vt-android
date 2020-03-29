package ch.virustracker.app.model.database.receiveevent;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ReceivedTokenDao {

    @Query("SELECT * FROM ReceiveEvent")
    List<ReceiveEvent> selectAll();

    @Query("SELECT * FROM ReceiveEvent WHERE tokenValue == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<ReceiveEvent> selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM ReceiveEvent WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    List<ReceiveEvent> selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(ReceiveEvent... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(ReceiveEvent entry);
}
