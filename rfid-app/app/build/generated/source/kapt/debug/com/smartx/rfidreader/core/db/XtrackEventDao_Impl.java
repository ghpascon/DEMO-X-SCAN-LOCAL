package com.smartx.rfidreader.core.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class XtrackEventDao_Impl implements XtrackEventDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<XtrackEventEntity> __insertionAdapterOfXtrackEventEntity;

  private final EntityDeletionOrUpdateAdapter<XtrackEventEntity> __deletionAdapterOfXtrackEventEntity;

  private final EntityDeletionOrUpdateAdapter<XtrackEventEntity> __updateAdapterOfXtrackEventEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public XtrackEventDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfXtrackEventEntity = new EntityInsertionAdapter<XtrackEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `xtrack_events` (`id`,`deviceId`,`eventType`,`locationId`,`locationName`,`tagsJson`,`savedAt`,`isSynced`,`syncedAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final XtrackEventEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDeviceId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDeviceId());
        }
        if (entity.getEventType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEventType());
        }
        if (entity.getLocationId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLocationId());
        }
        if (entity.getLocationName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLocationName());
        }
        if (entity.getTagsJson() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTagsJson());
        }
        if (entity.getSavedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getSavedAt());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.getSyncedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSyncedAt());
        }
      }
    };
    this.__deletionAdapterOfXtrackEventEntity = new EntityDeletionOrUpdateAdapter<XtrackEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `xtrack_events` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final XtrackEventEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfXtrackEventEntity = new EntityDeletionOrUpdateAdapter<XtrackEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `xtrack_events` SET `id` = ?,`deviceId` = ?,`eventType` = ?,`locationId` = ?,`locationName` = ?,`tagsJson` = ?,`savedAt` = ?,`isSynced` = ?,`syncedAt` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final XtrackEventEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDeviceId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDeviceId());
        }
        if (entity.getEventType() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getEventType());
        }
        if (entity.getLocationId() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLocationId());
        }
        if (entity.getLocationName() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getLocationName());
        }
        if (entity.getTagsJson() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getTagsJson());
        }
        if (entity.getSavedAt() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getSavedAt());
        }
        final int _tmp = entity.isSynced() ? 1 : 0;
        statement.bindLong(8, _tmp);
        if (entity.getSyncedAt() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getSyncedAt());
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM xtrack_events";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final XtrackEventEntity event,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfXtrackEventEntity.insertAndReturnId(event);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final XtrackEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfXtrackEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final XtrackEventEntity event,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfXtrackEventEntity.handle(event);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<XtrackEventEntity>> allFlow() {
    final String _sql = "SELECT * FROM xtrack_events ORDER BY savedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"xtrack_events"}, new Callable<List<XtrackEventEntity>>() {
      @Override
      @NonNull
      public List<XtrackEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfSavedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "savedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<XtrackEventEntity> _result = new ArrayList<XtrackEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final XtrackEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLocationName;
            if (_cursor.isNull(_cursorIndexOfLocationName)) {
              _tmpLocationName = null;
            } else {
              _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            }
            final String _tmpTagsJson;
            if (_cursor.isNull(_cursorIndexOfTagsJson)) {
              _tmpTagsJson = null;
            } else {
              _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            }
            final String _tmpSavedAt;
            if (_cursor.isNull(_cursorIndexOfSavedAt)) {
              _tmpSavedAt = null;
            } else {
              _tmpSavedAt = _cursor.getString(_cursorIndexOfSavedAt);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _item = new XtrackEventEntity(_tmpId,_tmpDeviceId,_tmpEventType,_tmpLocationId,_tmpLocationName,_tmpTagsJson,_tmpSavedAt,_tmpIsSynced,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object pending(final Continuation<? super List<XtrackEventEntity>> $completion) {
    final String _sql = "SELECT * FROM xtrack_events WHERE isSynced = 0 ORDER BY savedAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<XtrackEventEntity>>() {
      @Override
      @NonNull
      public List<XtrackEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfSavedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "savedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<XtrackEventEntity> _result = new ArrayList<XtrackEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final XtrackEventEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLocationName;
            if (_cursor.isNull(_cursorIndexOfLocationName)) {
              _tmpLocationName = null;
            } else {
              _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            }
            final String _tmpTagsJson;
            if (_cursor.isNull(_cursorIndexOfTagsJson)) {
              _tmpTagsJson = null;
            } else {
              _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            }
            final String _tmpSavedAt;
            if (_cursor.isNull(_cursorIndexOfSavedAt)) {
              _tmpSavedAt = null;
            } else {
              _tmpSavedAt = _cursor.getString(_cursorIndexOfSavedAt);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _item = new XtrackEventEntity(_tmpId,_tmpDeviceId,_tmpEventType,_tmpLocationId,_tmpLocationName,_tmpTagsJson,_tmpSavedAt,_tmpIsSynced,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<Integer> pendingCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM xtrack_events WHERE isSynced = 0";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"xtrack_events"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Integer> totalCountFlow() {
    final String _sql = "SELECT COUNT(*) FROM xtrack_events";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"xtrack_events"}, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findPendingLocationInventory(final String locationId,
      final Continuation<? super XtrackEventEntity> $completion) {
    final String _sql = "\n"
            + "        SELECT * FROM xtrack_events\n"
            + "        WHERE eventType = 'location_inventory'\n"
            + "          AND isSynced = 0\n"
            + "          AND locationId = ?\n"
            + "        ORDER BY savedAt DESC\n"
            + "        LIMIT 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (locationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, locationId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<XtrackEventEntity>() {
      @Override
      @Nullable
      public XtrackEventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfSavedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "savedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final XtrackEventEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLocationName;
            if (_cursor.isNull(_cursorIndexOfLocationName)) {
              _tmpLocationName = null;
            } else {
              _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            }
            final String _tmpTagsJson;
            if (_cursor.isNull(_cursorIndexOfTagsJson)) {
              _tmpTagsJson = null;
            } else {
              _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            }
            final String _tmpSavedAt;
            if (_cursor.isNull(_cursorIndexOfSavedAt)) {
              _tmpSavedAt = null;
            } else {
              _tmpSavedAt = _cursor.getString(_cursorIndexOfSavedAt);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _result = new XtrackEventEntity(_tmpId,_tmpDeviceId,_tmpEventType,_tmpLocationId,_tmpLocationName,_tmpTagsJson,_tmpSavedAt,_tmpIsSynced,_tmpSyncedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findById(final long id, final Continuation<? super XtrackEventEntity> $completion) {
    final String _sql = "SELECT * FROM xtrack_events WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<XtrackEventEntity>() {
      @Override
      @Nullable
      public XtrackEventEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDeviceId = CursorUtil.getColumnIndexOrThrow(_cursor, "deviceId");
          final int _cursorIndexOfEventType = CursorUtil.getColumnIndexOrThrow(_cursor, "eventType");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLocationName = CursorUtil.getColumnIndexOrThrow(_cursor, "locationName");
          final int _cursorIndexOfTagsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "tagsJson");
          final int _cursorIndexOfSavedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "savedAt");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final XtrackEventEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDeviceId;
            if (_cursor.isNull(_cursorIndexOfDeviceId)) {
              _tmpDeviceId = null;
            } else {
              _tmpDeviceId = _cursor.getString(_cursorIndexOfDeviceId);
            }
            final String _tmpEventType;
            if (_cursor.isNull(_cursorIndexOfEventType)) {
              _tmpEventType = null;
            } else {
              _tmpEventType = _cursor.getString(_cursorIndexOfEventType);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLocationName;
            if (_cursor.isNull(_cursorIndexOfLocationName)) {
              _tmpLocationName = null;
            } else {
              _tmpLocationName = _cursor.getString(_cursorIndexOfLocationName);
            }
            final String _tmpTagsJson;
            if (_cursor.isNull(_cursorIndexOfTagsJson)) {
              _tmpTagsJson = null;
            } else {
              _tmpTagsJson = _cursor.getString(_cursorIndexOfTagsJson);
            }
            final String _tmpSavedAt;
            if (_cursor.isNull(_cursorIndexOfSavedAt)) {
              _tmpSavedAt = null;
            } else {
              _tmpSavedAt = _cursor.getString(_cursorIndexOfSavedAt);
            }
            final boolean _tmpIsSynced;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp != 0;
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _result = new XtrackEventEntity(_tmpId,_tmpDeviceId,_tmpEventType,_tmpLocationId,_tmpLocationName,_tmpTagsJson,_tmpSavedAt,_tmpIsSynced,_tmpSyncedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
