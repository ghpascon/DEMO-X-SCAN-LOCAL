package com.smartx.rfidreader.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\"\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001BE\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000b\u00a2\u0006\u0002\u0010\fJ\t\u0010\u0017\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0003H\u00c6\u0003J\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0003J\u0010\u0010\u001a\u001a\u0004\u0018\u00010\tH\u00c6\u0003\u00a2\u0006\u0002\u0010\u000eJ\u000f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u00c6\u0003JN\u0010\u001c\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\t2\u000e\b\u0002\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000bH\u00c6\u0001\u00a2\u0006\u0002\u0010\u001dJ\u0013\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010!\u001a\u00020\"H\u00d6\u0001J\t\u0010#\u001a\u00020\u0003H\u00d6\u0001R\u0015\u0010\b\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010\u000f\u001a\u0004\b\r\u0010\u000eR\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0013R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00030\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016\u00a8\u0006$"}, d2 = {"Lcom/smartx/rfidreader/ui/main/LocationInventoryContext;", "", "locationId", "", "locationName", "expectedTags", "", "Lcom/smartx/rfidreader/core/db/XtrackObjectEntity;", "existingEventId", "", "preFoundEpcs", "", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/Long;Ljava/util/Set;)V", "getExistingEventId", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getExpectedTags", "()Ljava/util/List;", "getLocationId", "()Ljava/lang/String;", "getLocationName", "getPreFoundEpcs", "()Ljava/util/Set;", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Ljava/lang/Long;Ljava/util/Set;)Lcom/smartx/rfidreader/ui/main/LocationInventoryContext;", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class LocationInventoryContext {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String locationId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String locationName = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> expectedTags = null;
    
    /**
     * ID do EventEntity existente (não sincronizado) para este local, se houver
     */
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long existingEventId = null;
    
    /**
     * EPCs já encontrados em uma sessão anterior (retomada)
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> preFoundEpcs = null;
    
    public LocationInventoryContext(@org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    java.lang.String locationName, @org.jetbrains.annotations.NotNull()
    java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> expectedTags, @org.jetbrains.annotations.Nullable()
    java.lang.Long existingEventId, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> preFoundEpcs) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLocationId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getLocationName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> getExpectedTags() {
        return null;
    }
    
    /**
     * ID do EventEntity existente (não sincronizado) para este local, se houver
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getExistingEventId() {
        return null;
    }
    
    /**
     * EPCs já encontrados em uma sessão anterior (retomada)
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> getPreFoundEpcs() {
        return null;
    }
    
    public LocationInventoryContext() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component4() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Set<java.lang.String> component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.rfidreader.ui.main.LocationInventoryContext copy(@org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    java.lang.String locationName, @org.jetbrains.annotations.NotNull()
    java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> expectedTags, @org.jetbrains.annotations.Nullable()
    java.lang.Long existingEventId, @org.jetbrains.annotations.NotNull()
    java.util.Set<java.lang.String> preFoundEpcs) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}