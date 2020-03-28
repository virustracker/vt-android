package ch.virustracker.app.model.database.seentoken;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SeenTokenDao {

    @Query("SELECT * FROM SeenToken")
    Cursor selectAll();

    @Query("SELECT * FROM SeenToken WHERE token == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    Cursor selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM SeenToken WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    Cursor selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(SeenToken... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(SeenToken entry);
}
