package ch.virustracker.app.model.database.owntoken;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface OwnTokenDao {

    @Query("SELECT * FROM OwnToken")
    Cursor selectAll();

    @Query("SELECT * FROM OwnToken WHERE token == :token AND timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    Cursor selectByTimeSpanAndToken(String token, Long from, Long to);

    @Query("SELECT * FROM OwnToken WHERE timestampMs >= :from AND timestampMs < :to ORDER BY timestampMs ASC")
    Cursor selectByTimeSpan(Long from, Long to);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAll(OwnToken... entry);

    @SuppressWarnings("unused")
    @Delete
    void delete(OwnToken entry);
}
