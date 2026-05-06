package com.smartx.rfidreader.core.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile EventDao _eventDao;

  private volatile XtrackObjectDao _xtrackObjectDao;

  private volatile XtrackLocationDao _xtrackLocationDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `rfid_events` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `deviceId` TEXT NOT NULL, `eventType` TEXT NOT NULL, `tagsJson` TEXT NOT NULL, `savedAt` TEXT NOT NULL, `gpsLat` REAL NOT NULL, `gpsLng` REAL NOT NULL, `hasGps` INTEGER NOT NULL, `txPower` INTEGER NOT NULL, `session` INTEGER NOT NULL, `rssiFilter` INTEGER NOT NULL, `prefixesJson` TEXT NOT NULL, `isSynced` INTEGER NOT NULL, `syncedAt` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `xtrack_objects` (`epc` TEXT NOT NULL, `objectId` TEXT NOT NULL, `idcode` TEXT NOT NULL, `description` TEXT NOT NULL, `active` TEXT NOT NULL, `locationId` TEXT NOT NULL, `lastSeen` TEXT NOT NULL, `homeLocationId` TEXT NOT NULL, `lastModified` TEXT NOT NULL, `lastLocation` TEXT NOT NULL, `syncedAt` TEXT NOT NULL, PRIMARY KEY(`epc`))");
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_xtrack_objects_epc` ON `xtrack_objects` (`epc`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_xtrack_objects_objectId` ON `xtrack_objects` (`objectId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_xtrack_objects_locationId` ON `xtrack_objects` (`locationId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_xtrack_objects_homeLocationId` ON `xtrack_objects` (`homeLocationId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `xtrack_locations` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_xtrack_locations_name` ON `xtrack_locations` (`name`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e3c8d641850d1ebb0588f271f45c5280')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `rfid_events`");
        db.execSQL("DROP TABLE IF EXISTS `xtrack_objects`");
        db.execSQL("DROP TABLE IF EXISTS `xtrack_locations`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsRfidEvents = new HashMap<String, TableInfo.Column>(14);
        _columnsRfidEvents.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("deviceId", new TableInfo.Column("deviceId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("eventType", new TableInfo.Column("eventType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("tagsJson", new TableInfo.Column("tagsJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("savedAt", new TableInfo.Column("savedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("gpsLat", new TableInfo.Column("gpsLat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("gpsLng", new TableInfo.Column("gpsLng", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("hasGps", new TableInfo.Column("hasGps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("txPower", new TableInfo.Column("txPower", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("session", new TableInfo.Column("session", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("rssiFilter", new TableInfo.Column("rssiFilter", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("prefixesJson", new TableInfo.Column("prefixesJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("isSynced", new TableInfo.Column("isSynced", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRfidEvents.put("syncedAt", new TableInfo.Column("syncedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRfidEvents = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRfidEvents = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRfidEvents = new TableInfo("rfid_events", _columnsRfidEvents, _foreignKeysRfidEvents, _indicesRfidEvents);
        final TableInfo _existingRfidEvents = TableInfo.read(db, "rfid_events");
        if (!_infoRfidEvents.equals(_existingRfidEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "rfid_events(com.smartx.rfidreader.core.db.EventEntity).\n"
                  + " Expected:\n" + _infoRfidEvents + "\n"
                  + " Found:\n" + _existingRfidEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsXtrackObjects = new HashMap<String, TableInfo.Column>(11);
        _columnsXtrackObjects.put("epc", new TableInfo.Column("epc", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("objectId", new TableInfo.Column("objectId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("idcode", new TableInfo.Column("idcode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("active", new TableInfo.Column("active", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("locationId", new TableInfo.Column("locationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("lastSeen", new TableInfo.Column("lastSeen", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("homeLocationId", new TableInfo.Column("homeLocationId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("lastModified", new TableInfo.Column("lastModified", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("lastLocation", new TableInfo.Column("lastLocation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackObjects.put("syncedAt", new TableInfo.Column("syncedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysXtrackObjects = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesXtrackObjects = new HashSet<TableInfo.Index>(4);
        _indicesXtrackObjects.add(new TableInfo.Index("index_xtrack_objects_epc", true, Arrays.asList("epc"), Arrays.asList("ASC")));
        _indicesXtrackObjects.add(new TableInfo.Index("index_xtrack_objects_objectId", false, Arrays.asList("objectId"), Arrays.asList("ASC")));
        _indicesXtrackObjects.add(new TableInfo.Index("index_xtrack_objects_locationId", false, Arrays.asList("locationId"), Arrays.asList("ASC")));
        _indicesXtrackObjects.add(new TableInfo.Index("index_xtrack_objects_homeLocationId", false, Arrays.asList("homeLocationId"), Arrays.asList("ASC")));
        final TableInfo _infoXtrackObjects = new TableInfo("xtrack_objects", _columnsXtrackObjects, _foreignKeysXtrackObjects, _indicesXtrackObjects);
        final TableInfo _existingXtrackObjects = TableInfo.read(db, "xtrack_objects");
        if (!_infoXtrackObjects.equals(_existingXtrackObjects)) {
          return new RoomOpenHelper.ValidationResult(false, "xtrack_objects(com.smartx.rfidreader.core.db.XtrackObjectEntity).\n"
                  + " Expected:\n" + _infoXtrackObjects + "\n"
                  + " Found:\n" + _existingXtrackObjects);
        }
        final HashMap<String, TableInfo.Column> _columnsXtrackLocations = new HashMap<String, TableInfo.Column>(2);
        _columnsXtrackLocations.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsXtrackLocations.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysXtrackLocations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesXtrackLocations = new HashSet<TableInfo.Index>(1);
        _indicesXtrackLocations.add(new TableInfo.Index("index_xtrack_locations_name", false, Arrays.asList("name"), Arrays.asList("ASC")));
        final TableInfo _infoXtrackLocations = new TableInfo("xtrack_locations", _columnsXtrackLocations, _foreignKeysXtrackLocations, _indicesXtrackLocations);
        final TableInfo _existingXtrackLocations = TableInfo.read(db, "xtrack_locations");
        if (!_infoXtrackLocations.equals(_existingXtrackLocations)) {
          return new RoomOpenHelper.ValidationResult(false, "xtrack_locations(com.smartx.rfidreader.core.db.XtrackLocationEntity).\n"
                  + " Expected:\n" + _infoXtrackLocations + "\n"
                  + " Found:\n" + _existingXtrackLocations);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e3c8d641850d1ebb0588f271f45c5280", "513015ffa2c1b38e4018b82f72423d34");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "rfid_events","xtrack_objects","xtrack_locations");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `rfid_events`");
      _db.execSQL("DELETE FROM `xtrack_objects`");
      _db.execSQL("DELETE FROM `xtrack_locations`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(EventDao.class, EventDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(XtrackObjectDao.class, XtrackObjectDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(XtrackLocationDao.class, XtrackLocationDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public EventDao eventDao() {
    if (_eventDao != null) {
      return _eventDao;
    } else {
      synchronized(this) {
        if(_eventDao == null) {
          _eventDao = new EventDao_Impl(this);
        }
        return _eventDao;
      }
    }
  }

  @Override
  public XtrackObjectDao xtrackObjectDao() {
    if (_xtrackObjectDao != null) {
      return _xtrackObjectDao;
    } else {
      synchronized(this) {
        if(_xtrackObjectDao == null) {
          _xtrackObjectDao = new XtrackObjectDao_Impl(this);
        }
        return _xtrackObjectDao;
      }
    }
  }

  @Override
  public XtrackLocationDao xtrackLocationDao() {
    if (_xtrackLocationDao != null) {
      return _xtrackLocationDao;
    } else {
      synchronized(this) {
        if(_xtrackLocationDao == null) {
          _xtrackLocationDao = new XtrackLocationDao_Impl(this);
        }
        return _xtrackLocationDao;
      }
    }
  }
}
