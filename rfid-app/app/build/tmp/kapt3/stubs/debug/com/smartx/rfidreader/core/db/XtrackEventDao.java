package com.smartx.rfidreader.core.db;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\bg\u0018\u00002\u00020\u0001J\u0014\u0010\u0002\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u00040\u0003H\'J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u000e\u0010\n\u001a\u00020\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\u0004\u0018\u00010\u00052\u0006\u0010\r\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0011\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u000e2\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u000e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u0003H\'J\u000e\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00170\u0003H\'J\u0016\u0010\u0019\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\t\u00a8\u0006\u001a"}, d2 = {"Lcom/smartx/rfidreader/core/db/XtrackEventDao;", "", "allFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/smartx/rfidreader/core/db/XtrackEventEntity;", "delete", "", "event", "(Lcom/smartx/rfidreader/core/db/XtrackEventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteAll", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findPendingLocationInventory", "locationId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insert", "pending", "pendingCountFlow", "", "totalCountFlow", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface XtrackEventDao {
    
    @androidx.room.Query(value = "SELECT * FROM xtrack_events ORDER BY savedAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackEventEntity>> allFlow();
    
    @androidx.room.Query(value = "SELECT * FROM xtrack_events WHERE isSynced = 0 ORDER BY savedAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object pending(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.smartx.rfidreader.core.db.XtrackEventEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM xtrack_events WHERE isSynced = 0")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> pendingCountFlow();
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM xtrack_events")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.lang.Integer> totalCountFlow();
    
    /**
     * Busca o último inventário de local não sincronizado para um dado locationId.
     */
    @androidx.room.Query(value = "\n        SELECT * FROM xtrack_events\n        WHERE eventType = \'location_inventory\'\n          AND isSynced = 0\n          AND locationId = :locationId\n        ORDER BY savedAt DESC\n        LIMIT 1\n    ")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findPendingLocationInventory(@org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.db.XtrackEventEntity> $completion);
    
    @androidx.room.Insert()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM xtrack_events WHERE id = :id LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.db.XtrackEventEntity> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM xtrack_events")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}