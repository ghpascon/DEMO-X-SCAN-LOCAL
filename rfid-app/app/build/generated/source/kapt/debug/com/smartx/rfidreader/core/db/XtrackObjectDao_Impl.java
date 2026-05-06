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
public final class XtrackObjectDao_Impl implements XtrackObjectDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<XtrackObjectEntity> __insertionAdapterOfXtrackObjectEntity;

  private final EntityDeletionOrUpdateAdapter<XtrackObjectEntity> __deletionAdapterOfXtrackObjectEntity;

  private final EntityDeletionOrUpdateAdapter<XtrackObjectEntity> __updateAdapterOfXtrackObjectEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public XtrackObjectDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfXtrackObjectEntity = new EntityInsertionAdapter<XtrackObjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `xtrack_objects` (`epc`,`objectId`,`idcode`,`description`,`active`,`locationId`,`lastSeen`,`homeLocationId`,`lastModified`,`lastLocation`,`syncedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final XtrackObjectEntity entity) {
        if (entity.getEpc() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getEpc());
        }
        if (entity.getObjectId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getObjectId());
        }
        if (entity.getIdcode() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIdcode());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        if (entity.getActive() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getActive());
        }
        if (entity.getLocationId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocationId());
        }
        if (entity.getLastSeen() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLastSeen());
        }
        if (entity.getHomeLocationId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getHomeLocationId());
        }
        if (entity.getLastModified() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLastModified());
        }
        if (entity.getLastLocation() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLastLocation());
        }
        if (entity.getSyncedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSyncedAt());
        }
      }
    };
    this.__deletionAdapterOfXtrackObjectEntity = new EntityDeletionOrUpdateAdapter<XtrackObjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `xtrack_objects` WHERE `epc` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final XtrackObjectEntity entity) {
        if (entity.getEpc() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getEpc());
        }
      }
    };
    this.__updateAdapterOfXtrackObjectEntity = new EntityDeletionOrUpdateAdapter<XtrackObjectEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `xtrack_objects` SET `epc` = ?,`objectId` = ?,`idcode` = ?,`description` = ?,`active` = ?,`locationId` = ?,`lastSeen` = ?,`homeLocationId` = ?,`lastModified` = ?,`lastLocation` = ?,`syncedAt` = ? WHERE `epc` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final XtrackObjectEntity entity) {
        if (entity.getEpc() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getEpc());
        }
        if (entity.getObjectId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getObjectId());
        }
        if (entity.getIdcode() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getIdcode());
        }
        if (entity.getDescription() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getDescription());
        }
        if (entity.getActive() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getActive());
        }
        if (entity.getLocationId() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLocationId());
        }
        if (entity.getLastSeen() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getLastSeen());
        }
        if (entity.getHomeLocationId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getHomeLocationId());
        }
        if (entity.getLastModified() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getLastModified());
        }
        if (entity.getLastLocation() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getLastLocation());
        }
        if (entity.getSyncedAt() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getSyncedAt());
        }
        if (entity.getEpc() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getEpc());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM xtrack_objects";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<XtrackObjectEntity> objects,
      final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfXtrackObjectEntity.insert(objects);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object insert(final XtrackObjectEntity obj, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfXtrackObjectEntity.insert(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object delete(final XtrackObjectEntity obj, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfXtrackObjectEntity.handle(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object update(final XtrackObjectEntity obj, final Continuation<? super Unit> arg1) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfXtrackObjectEntity.handle(obj);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, arg1);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> arg0) {
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
    }, arg0);
  }

  @Override
  public Flow<List<XtrackObjectEntity>> allFlow() {
    final String _sql = "SELECT * FROM xtrack_objects ORDER BY description ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"xtrack_objects"}, new Callable<List<XtrackObjectEntity>>() {
      @Override
      @NonNull
      public List<XtrackObjectEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpc = CursorUtil.getColumnIndexOrThrow(_cursor, "epc");
          final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
          final int _cursorIndexOfIdcode = CursorUtil.getColumnIndexOrThrow(_cursor, "idcode");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeen");
          final int _cursorIndexOfHomeLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeLocationId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfLastLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "lastLocation");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<XtrackObjectEntity> _result = new ArrayList<XtrackObjectEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final XtrackObjectEntity _item;
            final String _tmpEpc;
            if (_cursor.isNull(_cursorIndexOfEpc)) {
              _tmpEpc = null;
            } else {
              _tmpEpc = _cursor.getString(_cursorIndexOfEpc);
            }
            final String _tmpObjectId;
            if (_cursor.isNull(_cursorIndexOfObjectId)) {
              _tmpObjectId = null;
            } else {
              _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
            }
            final String _tmpIdcode;
            if (_cursor.isNull(_cursorIndexOfIdcode)) {
              _tmpIdcode = null;
            } else {
              _tmpIdcode = _cursor.getString(_cursorIndexOfIdcode);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpActive;
            if (_cursor.isNull(_cursorIndexOfActive)) {
              _tmpActive = null;
            } else {
              _tmpActive = _cursor.getString(_cursorIndexOfActive);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLastSeen;
            if (_cursor.isNull(_cursorIndexOfLastSeen)) {
              _tmpLastSeen = null;
            } else {
              _tmpLastSeen = _cursor.getString(_cursorIndexOfLastSeen);
            }
            final String _tmpHomeLocationId;
            if (_cursor.isNull(_cursorIndexOfHomeLocationId)) {
              _tmpHomeLocationId = null;
            } else {
              _tmpHomeLocationId = _cursor.getString(_cursorIndexOfHomeLocationId);
            }
            final String _tmpLastModified;
            if (_cursor.isNull(_cursorIndexOfLastModified)) {
              _tmpLastModified = null;
            } else {
              _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            }
            final String _tmpLastLocation;
            if (_cursor.isNull(_cursorIndexOfLastLocation)) {
              _tmpLastLocation = null;
            } else {
              _tmpLastLocation = _cursor.getString(_cursorIndexOfLastLocation);
            }
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _item = new XtrackObjectEntity(_tmpEpc,_tmpObjectId,_tmpIdcode,_tmpDescription,_tmpActive,_tmpLocationId,_tmpLastSeen,_tmpHomeLocationId,_tmpLastModified,_tmpLastLocation,_tmpSyncedAt);
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
  public Object findByEpc(final String epc, final Continuation<? super XtrackObjectEntity> arg1) {
    final String _sql = "SELECT * FROM xtrack_objects WHERE epc = ? COLLATE NOCASE LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (epc == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, epc);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<XtrackObjectEntity>() {
      @Override
      @Nullable
      public XtrackObjectEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpc = CursorUtil.getColumnIndexOrThrow(_cursor, "epc");
          final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
          final int _cursorIndexOfIdcode = CursorUtil.getColumnIndexOrThrow(_cursor, "idcode");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeen");
          final int _cursorIndexOfHomeLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeLocationId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfLastLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "lastLocation");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final XtrackObjectEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpEpc;
            if (_cursor.isNull(_cursorIndexOfEpc)) {
              _tmpEpc = null;
            } else {
              _tmpEpc = _cursor.getString(_cursorIndexOfEpc);
            }
            final String _tmpObjectId;
            if (_cursor.isNull(_cursorIndexOfObjectId)) {
              _tmpObjectId = null;
            } else {
              _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
            }
            final String _tmpIdcode;
            if (_cursor.isNull(_cursorIndexOfIdcode)) {
              _tmpIdcode = null;
            } else {
              _tmpIdcode = _cursor.getString(_cursorIndexOfIdcode);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpActive;
            if (_cursor.isNull(_cursorIndexOfActive)) {
              _tmpActive = null;
            } else {
              _tmpActive = _cursor.getString(_cursorIndexOfActive);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLastSeen;
            if (_cursor.isNull(_cursorIndexOfLastSeen)) {
              _tmpLastSeen = null;
            } else {
              _tmpLastSeen = _cursor.getString(_cursorIndexOfLastSeen);
            }
            final String _tmpHomeLocationId;
            if (_cursor.isNull(_cursorIndexOfHomeLocationId)) {
              _tmpHomeLocationId = null;
            } else {
              _tmpHomeLocationId = _cursor.getString(_cursorIndexOfHomeLocationId);
            }
            final String _tmpLastModified;
            if (_cursor.isNull(_cursorIndexOfLastModified)) {
              _tmpLastModified = null;
            } else {
              _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            }
            final String _tmpLastLocation;
            if (_cursor.isNull(_cursorIndexOfLastLocation)) {
              _tmpLastLocation = null;
            } else {
              _tmpLastLocation = _cursor.getString(_cursorIndexOfLastLocation);
            }
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _result = new XtrackObjectEntity(_tmpEpc,_tmpObjectId,_tmpIdcode,_tmpDescription,_tmpActive,_tmpLocationId,_tmpLastSeen,_tmpHomeLocationId,_tmpLastModified,_tmpLastLocation,_tmpSyncedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object findByLocation(final String locationId,
      final Continuation<? super List<XtrackObjectEntity>> arg1) {
    final String _sql = "SELECT * FROM xtrack_objects WHERE locationId = ? ORDER BY description ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (locationId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, locationId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<XtrackObjectEntity>>() {
      @Override
      @NonNull
      public List<XtrackObjectEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpc = CursorUtil.getColumnIndexOrThrow(_cursor, "epc");
          final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
          final int _cursorIndexOfIdcode = CursorUtil.getColumnIndexOrThrow(_cursor, "idcode");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeen");
          final int _cursorIndexOfHomeLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeLocationId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfLastLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "lastLocation");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<XtrackObjectEntity> _result = new ArrayList<XtrackObjectEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final XtrackObjectEntity _item;
            final String _tmpEpc;
            if (_cursor.isNull(_cursorIndexOfEpc)) {
              _tmpEpc = null;
            } else {
              _tmpEpc = _cursor.getString(_cursorIndexOfEpc);
            }
            final String _tmpObjectId;
            if (_cursor.isNull(_cursorIndexOfObjectId)) {
              _tmpObjectId = null;
            } else {
              _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
            }
            final String _tmpIdcode;
            if (_cursor.isNull(_cursorIndexOfIdcode)) {
              _tmpIdcode = null;
            } else {
              _tmpIdcode = _cursor.getString(_cursorIndexOfIdcode);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpActive;
            if (_cursor.isNull(_cursorIndexOfActive)) {
              _tmpActive = null;
            } else {
              _tmpActive = _cursor.getString(_cursorIndexOfActive);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLastSeen;
            if (_cursor.isNull(_cursorIndexOfLastSeen)) {
              _tmpLastSeen = null;
            } else {
              _tmpLastSeen = _cursor.getString(_cursorIndexOfLastSeen);
            }
            final String _tmpHomeLocationId;
            if (_cursor.isNull(_cursorIndexOfHomeLocationId)) {
              _tmpHomeLocationId = null;
            } else {
              _tmpHomeLocationId = _cursor.getString(_cursorIndexOfHomeLocationId);
            }
            final String _tmpLastModified;
            if (_cursor.isNull(_cursorIndexOfLastModified)) {
              _tmpLastModified = null;
            } else {
              _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            }
            final String _tmpLastLocation;
            if (_cursor.isNull(_cursorIndexOfLastLocation)) {
              _tmpLastLocation = null;
            } else {
              _tmpLastLocation = _cursor.getString(_cursorIndexOfLastLocation);
            }
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _item = new XtrackObjectEntity(_tmpEpc,_tmpObjectId,_tmpIdcode,_tmpDescription,_tmpActive,_tmpLocationId,_tmpLastSeen,_tmpHomeLocationId,_tmpLastModified,_tmpLastLocation,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg1);
  }

  @Override
  public Object count(final Continuation<? super Integer> arg0) {
    final String _sql = "SELECT COUNT(*) FROM xtrack_objects";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
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
          _statement.release();
        }
      }
    }, arg0);
  }

  @Override
  public Object queryPaged(final String filterField, final String filterValue, final int limit,
      final int offset, final Continuation<? super List<XtrackObjectEntity>> arg4) {
    final String _sql = "\n"
            + "        SELECT * FROM xtrack_objects \n"
            + "        WHERE \n"
            + "            (? = '' OR ? = '') OR\n"
            + "            (? = 'description' AND description LIKE '%' || ? || '%') OR\n"
            + "            (? = 'epc' AND epc LIKE '%' || ? || '%') OR\n"
            + "            (? = 'locationId' AND locationId LIKE '%' || ? || '%') OR\n"
            + "            (? = 'active' AND active LIKE '%' || ? || '%') OR\n"
            + "            (? = 'lastLocation' AND lastLocation LIKE '%' || ? || '%')\n"
            + "        ORDER BY description ASC \n"
            + "        LIMIT ? OFFSET ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 14);
    int _argIndex = 1;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 2;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 3;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 4;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 5;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 6;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 7;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 8;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 9;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 10;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 11;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 12;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 13;
    _statement.bindLong(_argIndex, limit);
    _argIndex = 14;
    _statement.bindLong(_argIndex, offset);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<XtrackObjectEntity>>() {
      @Override
      @NonNull
      public List<XtrackObjectEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEpc = CursorUtil.getColumnIndexOrThrow(_cursor, "epc");
          final int _cursorIndexOfObjectId = CursorUtil.getColumnIndexOrThrow(_cursor, "objectId");
          final int _cursorIndexOfIdcode = CursorUtil.getColumnIndexOrThrow(_cursor, "idcode");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
          final int _cursorIndexOfLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "locationId");
          final int _cursorIndexOfLastSeen = CursorUtil.getColumnIndexOrThrow(_cursor, "lastSeen");
          final int _cursorIndexOfHomeLocationId = CursorUtil.getColumnIndexOrThrow(_cursor, "homeLocationId");
          final int _cursorIndexOfLastModified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastModified");
          final int _cursorIndexOfLastLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "lastLocation");
          final int _cursorIndexOfSyncedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "syncedAt");
          final List<XtrackObjectEntity> _result = new ArrayList<XtrackObjectEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final XtrackObjectEntity _item;
            final String _tmpEpc;
            if (_cursor.isNull(_cursorIndexOfEpc)) {
              _tmpEpc = null;
            } else {
              _tmpEpc = _cursor.getString(_cursorIndexOfEpc);
            }
            final String _tmpObjectId;
            if (_cursor.isNull(_cursorIndexOfObjectId)) {
              _tmpObjectId = null;
            } else {
              _tmpObjectId = _cursor.getString(_cursorIndexOfObjectId);
            }
            final String _tmpIdcode;
            if (_cursor.isNull(_cursorIndexOfIdcode)) {
              _tmpIdcode = null;
            } else {
              _tmpIdcode = _cursor.getString(_cursorIndexOfIdcode);
            }
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            final String _tmpActive;
            if (_cursor.isNull(_cursorIndexOfActive)) {
              _tmpActive = null;
            } else {
              _tmpActive = _cursor.getString(_cursorIndexOfActive);
            }
            final String _tmpLocationId;
            if (_cursor.isNull(_cursorIndexOfLocationId)) {
              _tmpLocationId = null;
            } else {
              _tmpLocationId = _cursor.getString(_cursorIndexOfLocationId);
            }
            final String _tmpLastSeen;
            if (_cursor.isNull(_cursorIndexOfLastSeen)) {
              _tmpLastSeen = null;
            } else {
              _tmpLastSeen = _cursor.getString(_cursorIndexOfLastSeen);
            }
            final String _tmpHomeLocationId;
            if (_cursor.isNull(_cursorIndexOfHomeLocationId)) {
              _tmpHomeLocationId = null;
            } else {
              _tmpHomeLocationId = _cursor.getString(_cursorIndexOfHomeLocationId);
            }
            final String _tmpLastModified;
            if (_cursor.isNull(_cursorIndexOfLastModified)) {
              _tmpLastModified = null;
            } else {
              _tmpLastModified = _cursor.getString(_cursorIndexOfLastModified);
            }
            final String _tmpLastLocation;
            if (_cursor.isNull(_cursorIndexOfLastLocation)) {
              _tmpLastLocation = null;
            } else {
              _tmpLastLocation = _cursor.getString(_cursorIndexOfLastLocation);
            }
            final String _tmpSyncedAt;
            if (_cursor.isNull(_cursorIndexOfSyncedAt)) {
              _tmpSyncedAt = null;
            } else {
              _tmpSyncedAt = _cursor.getString(_cursorIndexOfSyncedAt);
            }
            _item = new XtrackObjectEntity(_tmpEpc,_tmpObjectId,_tmpIdcode,_tmpDescription,_tmpActive,_tmpLocationId,_tmpLastSeen,_tmpHomeLocationId,_tmpLastModified,_tmpLastLocation,_tmpSyncedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, arg4);
  }

  @Override
  public Object countFiltered(final String filterField, final String filterValue,
      final Continuation<? super Integer> arg2) {
    final String _sql = "\n"
            + "        SELECT COUNT(*) FROM xtrack_objects \n"
            + "        WHERE \n"
            + "            (? = '' OR ? = '') OR\n"
            + "            (? = 'description' AND description LIKE '%' || ? || '%') OR\n"
            + "            (? = 'epc' AND epc LIKE '%' || ? || '%') OR\n"
            + "            (? = 'locationId' AND locationId LIKE '%' || ? || '%') OR\n"
            + "            (? = 'active' AND active LIKE '%' || ? || '%') OR\n"
            + "            (? = 'lastLocation' AND lastLocation LIKE '%' || ? || '%')\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 12);
    int _argIndex = 1;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 2;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 3;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 4;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 5;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 6;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 7;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 8;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 9;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 10;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    _argIndex = 11;
    if (filterField == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterField);
    }
    _argIndex = 12;
    if (filterValue == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, filterValue);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
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
          _statement.release();
        }
      }
    }, arg2);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
