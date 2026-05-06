package com.smartx.rfidreader.ui.xtrack;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0019\b\u0086\b\u0018\u00002\u00020\u0001BQ\u0012\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0006\u0012\b\b\u0002\u0010\b\u001a\u00020\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\f\u0012\b\b\u0002\u0010\r\u001a\u00020\f\u00a2\u0006\u0002\u0010\u000eJ\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001c\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\nH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\fH\u00c6\u0003J\t\u0010\u001f\u001a\u00020\fH\u00c6\u0003JU\u0010 \u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\b\b\u0002\u0010\b\u001a\u00020\u00062\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\fH\u00c6\u0001J\u0013\u0010!\u001a\u00020\n2\b\u0010\"\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010#\u001a\u00020\u0006H\u00d6\u0001J\t\u0010$\u001a\u00020\fH\u00d6\u0001R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\r\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0012R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\b\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0016R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0016\u00a8\u0006%"}, d2 = {"Lcom/smartx/rfidreader/ui/xtrack/ObjectsState;", "", "items", "", "Lcom/smartx/rfidreader/core/db/XtrackObjectEntity;", "page", "", "totalPages", "total", "isLoading", "", "filterField", "", "filterValue", "(Ljava/util/List;IIIZLjava/lang/String;Ljava/lang/String;)V", "getFilterField", "()Ljava/lang/String;", "getFilterValue", "()Z", "getItems", "()Ljava/util/List;", "getPage", "()I", "getTotal", "getTotalPages", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "equals", "other", "hashCode", "toString", "app_debug"})
public final class ObjectsState {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> items = null;
    private final int page = 0;
    private final int totalPages = 0;
    private final int total = 0;
    private final boolean isLoading = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String filterField = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String filterValue = null;
    
    public ObjectsState(@org.jetbrains.annotations.NotNull()
    java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> items, int page, int totalPages, int total, boolean isLoading, @org.jetbrains.annotations.NotNull()
    java.lang.String filterField, @org.jetbrains.annotations.NotNull()
    java.lang.String filterValue) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> getItems() {
        return null;
    }
    
    public final int getPage() {
        return 0;
    }
    
    public final int getTotalPages() {
        return 0;
    }
    
    public final int getTotal() {
        return 0;
    }
    
    public final boolean isLoading() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFilterField() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getFilterValue() {
        return null;
    }
    
    public ObjectsState() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final int component3() {
        return 0;
    }
    
    public final int component4() {
        return 0;
    }
    
    public final boolean component5() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.rfidreader.ui.xtrack.ObjectsState copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity> items, int page, int totalPages, int total, boolean isLoading, @org.jetbrains.annotations.NotNull()
    java.lang.String filterField, @org.jetbrains.annotations.NotNull()
    java.lang.String filterValue) {
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