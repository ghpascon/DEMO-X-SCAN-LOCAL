package com.smartx.rfidreader.core.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0012\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u000e\u0010\u0006\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\bJ\u001e\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0012\u001a\u00020\u000fH\u00a7@\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0014\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u001c\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0017\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u001c\u0010\u0019\u001a\u00020\u000f2\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u001bJ4\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\u001d\u001a\u00020\u00072\u0006\u0010\u001e\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u0016\u0010 \u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0011\u00a8\u0006!"}, d2 = {"Lcom/smartx/rfidreader/core/db/XtrackObjectDao;", "", "allFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/smartx/rfidreader/core/db/XtrackObjectEntity;", "count", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "countFiltered", "filterField", "", "filterValue", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "", "obj", "(Lcom/smartx/rfidreader/core/db/XtrackObjectEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAll", "findByEpc", "epc", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findByLocation", "locationId", "insert", "insertAll", "objects", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "queryPaged", "limit", "offset", "(Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface XtrackObjectDao {
    
    @androidx.room.Query(value = "SELECT * FROM xtrack_objects ORDER BY description ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>> allFlow();
    
    /**
     * Lookup O(1) por EPC — case-insensitive pois leitores e servidor podem ter casing diferente.
     */
    @androidx.room.Query(value = "SELECT * FROM xtrack_objects WHERE epc = :epc COLLATE NOCASE LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findByEpc(@org.jetbrains.annotations.NotNull()
    java.lang.String epc, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.db.XtrackObjectEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM xtrack_objects WHERE locationId = :locationId ORDER BY description ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findByLocation(@org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM xtrack_objects")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object count(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    /**
     * Query paginada com filtro dinâmico por campo.
     * filterField: nome da coluna SQLite ("description", "epc", "locationId", "active", "lastLocation").
     * Quando filterField ou filterValue são vazios, retorna todos os registros sem filtro.
     */
    @androidx.room.Query(value = "\n        SELECT * FROM xtrack_objects \n        WHERE \n            (:filterField = \'\' OR :filterValue = \'\') OR\n            (:filterField = \'description\' AND description LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'epc\' AND epc LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'locationId\' AND locationId LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'active\' AND active LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'lastLocation\' AND lastLocation LIKE \'%\' || :filterValue || \'%\')\n        ORDER BY description ASC \n        LIMIT :limit OFFSET :offset\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object queryPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String filterField, @org.jetbrains.annotations.NotNull()
    java.lang.String filterValue, int limit, int offset, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>> $completion);
    
    @androidx.room.Query(value = "\n        SELECT COUNT(*) FROM xtrack_objects \n        WHERE \n            (:filterField = \'\' OR :filterValue = \'\') OR\n            (:filterField = \'description\' AND description LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'epc\' AND epc LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'locationId\' AND locationId LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'active\' AND active LIKE \'%\' || :filterValue || \'%\') OR\n            (:filterField = \'lastLocation\' AND lastLocation LIKE \'%\' || :filterValue || \'%\')\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object countFiltered(@org.jetbrains.annotations.NotNull()
    java.lang.String filterField, @org.jetbrains.annotations.NotNull()
    java.lang.String filterValue, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> objects, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackObjectEntity obj, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackObjectEntity obj, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackObjectEntity obj, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM xtrack_objects")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}