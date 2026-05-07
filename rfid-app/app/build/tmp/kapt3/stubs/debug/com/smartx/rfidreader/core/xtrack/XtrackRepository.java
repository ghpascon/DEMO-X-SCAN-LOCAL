package com.smartx.rfidreader.core.xtrack;

/**
 * Responsável por toda a integração com o servidor Xtrack:
 * - Requisições HTTP (GET/POST com XML)
 * - Persistência local de objetos e localizações
 * - Lookup rápido por EPC (usado em background durante leitura)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u00b2\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010$\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0018\u0010\u001e\u001a\u00020\n2\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010 \u001a\u00020\nH\u0002J$\u0010!\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0012\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0$H\u0002J\u000e\u0010%\u001a\u00020&H\u0086@\u00a2\u0006\u0002\u0010\'J\u0016\u0010(\u001a\u00020&2\u0006\u0010)\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010*J\u0010\u0010+\u001a\u00020\n2\u0006\u0010,\u001a\u00020\nH\u0002J:\u0010-\u001a\b\u0012\u0004\u0012\u00020\u00190.2\u0006\u0010/\u001a\u00020\n2\u0014\b\u0002\u00100\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020&01H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b2\u00103J:\u00104\u001a\b\u0012\u0004\u0012\u00020\u00190.2\u0006\u0010/\u001a\u00020\n2\u0014\b\u0002\u00100\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020&01H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b5\u00103J\u0018\u00106\u001a\u0004\u0018\u00010\u001c2\u0006\u00107\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u00108J\u001a\u00109\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0$H\u0086@\u00a2\u0006\u0002\u0010\'J\u0014\u0010:\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011H\u0086@\u00a2\u0006\u0002\u0010\'J\u0018\u0010;\u001a\u0004\u0018\u00010\u00122\u0006\u0010<\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u00108J\u0018\u0010=\u001a\u0004\u0018\u00010\u00162\u0006\u0010>\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u00108J\u001c\u0010?\u001a\b\u0012\u0004\u0012\u00020\u00160\u00112\u0006\u00107\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u00108J\u000e\u0010@\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\'J\u000e\u0010A\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\'J\"\u0010B\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0C0\u00112\u0006\u0010D\u001a\u00020\nH\u0002J\u0016\u0010E\u001a\b\u0012\u0004\u0012\u00020\u00120\u00112\u0006\u0010D\u001a\u00020\nH\u0002J(\u0010F\u001a\u001a\u0012\u0004\u0012\u00020\n\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0$0$2\u0006\u0010D\u001a\u00020\nH\u0002J,\u0010G\u001a\b\u0012\u0004\u0012\u00020\n0.2\u0006\u0010/\u001a\u00020\n2\u0006\u0010H\u001a\u00020\nH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bI\u0010JJ\u0018\u0010K\u001a\u00020\n2\u0006\u0010/\u001a\u00020\n2\u0006\u0010H\u001a\u00020\nH\u0002JB\u0010L\u001a\b\u0012\u0004\u0012\u00020\n0.2\u0006\u0010/\u001a\u00020\n2\u0006\u0010\"\u001a\u00020\n2\u0014\b\u0002\u0010#\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0$H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bM\u0010NJB\u0010O\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u0011\u0012\u0004\u0012\u00020\u00190C2\u0006\u0010P\u001a\u00020\n2\u0006\u0010Q\u001a\u00020\n2\u0006\u0010R\u001a\u00020\u00192\b\b\u0002\u0010S\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010TJF\u0010U\u001a\u00020V2\u0006\u0010W\u001a\u00020\n2\u0006\u00107\u001a\u00020\n2\u0006\u0010 \u001a\u00020\n2\u001e\u0010X\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0Y0\u0011H\u0086@\u00a2\u0006\u0002\u0010ZJV\u0010[\u001a\u00020V2\u0006\u0010W\u001a\u00020\n2\u0006\u00107\u001a\u00020\n2\u0006\u0010 \u001a\u00020\n2\u0006\u0010\\\u001a\u00020\u00192\f\u0010]\u001a\b\u0012\u0004\u0012\u00020\n0\u00112\f\u0010^\u001a\b\u0012\u0004\u0012\u00020\n0\u00112\n\b\u0002\u0010_\u001a\u0004\u0018\u00010VH\u0086@\u00a2\u0006\u0002\u0010`J:\u0010a\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u0011\u0012\u0004\u0012\u00020\u00190C2\u0006\u0010b\u001a\u00020\n2\u0006\u0010R\u001a\u00020\u00192\b\b\u0002\u0010S\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010cJX\u0010d\u001a\u0010\u0012\u0004\u0012\u00020e\u0012\u0006\u0012\u0004\u0018\u00010\n0C2\u0006\u0010)\u001a\u00020\u001c2\u0006\u0010f\u001a\u00020\n2\u0006\u0010g\u001a\u00020h2\"\u00100\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0j\u0012\u0006\u0012\u0004\u0018\u00010\u00010iH\u0082@\u00a2\u0006\u0002\u0010kJ`\u0010l\u001a\u0010\u0012\u0004\u0012\u00020e\u0012\u0006\u0012\u0004\u0018\u00010\n0C2\u0006\u0010)\u001a\u00020\u001c2\u0006\u0010f\u001a\u00020\n2\u0006\u0010m\u001a\u00020\n2\u0006\u0010g\u001a\u00020h2\"\u00100\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0j\u0012\u0006\u0012\u0004\u0018\u00010\u00010iH\u0082@\u00a2\u0006\u0002\u0010nJ\u00e5\u0001\u0010o\u001a\u000e\u0012\u0004\u0012\u00020\u0019\u0012\u0004\u0012\u00020\u00190C2\u0006\u0010f\u001a\u00020\n2\u0006\u0010m\u001a\u00020\n2\b\b\u0002\u0010p\u001a\u00020\u00192$\b\u0002\u00100\u001a\u001e\b\u0001\u0012\u0004\u0012\u00020\n\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0j\u0012\u0006\u0012\u0004\u0018\u00010\u00010i2\u0088\u0001\u0010q\u001a\u0083\u0001\b\u0001\u0012\u0013\u0012\u00110\u0019\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(u\u0012\u0013\u0012\u00110\u0019\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(\\\u0012\u0013\u0012\u00110\u001c\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b()\u0012\u0013\u0012\u00110e\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(v\u0012\u0015\u0012\u0013\u0018\u00010\n\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\n\u0012\b\u0012\u0004\u0012\u00020&0j\u0012\u0006\u0012\u0004\u0018\u00010\u00010rH\u0086@\u00a2\u0006\u0002\u0010xR\u000e\u0010\t\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\u00110\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00160\u00110\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0014R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u00110\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0014\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006y"}, d2 = {"Lcom/smartx/rfidreader/core/xtrack/XtrackRepository;", "", "objectDao", "Lcom/smartx/rfidreader/core/db/XtrackObjectDao;", "locationDao", "Lcom/smartx/rfidreader/core/db/XtrackLocationDao;", "xtrackEventDao", "Lcom/smartx/rfidreader/core/db/XtrackEventDao;", "(Lcom/smartx/rfidreader/core/db/XtrackObjectDao;Lcom/smartx/rfidreader/core/db/XtrackLocationDao;Lcom/smartx/rfidreader/core/db/XtrackEventDao;)V", "TAG", "", "httpClient", "Lokhttp3/OkHttpClient;", "isoFormat", "Ljava/text/SimpleDateFormat;", "locationsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/smartx/rfidreader/core/db/XtrackLocationEntity;", "getLocationsFlow", "()Lkotlinx/coroutines/flow/Flow;", "objectsFlow", "Lcom/smartx/rfidreader/core/db/XtrackObjectEntity;", "getObjectsFlow", "pendingXtrackCountFlow", "", "getPendingXtrackCountFlow", "xtrackEventsFlow", "Lcom/smartx/rfidreader/core/db/XtrackEventEntity;", "getXtrackEventsFlow", "buildMoveLocationXml", "idcode", "locationName", "buildXmlCommand", "command", "fields", "", "deleteAllXtrackEvents", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteXtrackEvent", "event", "(Lcom/smartx/rfidreader/core/db/XtrackEventEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "escapeXml", "s", "fetchAndSaveLocations", "Lkotlin/Result;", "baseUrl", "onLog", "Lkotlin/Function1;", "fetchAndSaveLocations-0E7RQCE", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchAndSaveObjects", "fetchAndSaveObjects-0E7RQCE", "findExistingXtrackLocationInventory", "locationId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllLocationNames", "getAllLocations", "getLocationById", "id", "getObjectByEpc", "epc", "getObjectsByLocation", "locationCount", "objectCount", "parseIdentifications", "Lkotlin/Pair;", "xmlStr", "parseLocations", "parseObjectsMap", "postRawXml", "xmlBody", "postRawXml-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postXml", "postXmlCommand", "postXmlCommand-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "queryObjectsPaged", "filterField", "filterValue", "page", "pageSize", "(Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveChangeLocation", "", "deviceId", "tags", "Lkotlin/Triple;", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "saveXtrackLocationInventory", "total", "foundEpcs", "missingEpcs", "existingEventId", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/util/List;Ljava/util/List;Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchLocationsPaged", "search", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncChangeLocationEvent", "", "xtrackUrl", "semaphore", "Lkotlinx/coroutines/sync/Semaphore;", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "(Lcom/smartx/rfidreader/core/db/XtrackEventEntity;Ljava/lang/String;Lkotlinx/coroutines/sync/Semaphore;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncLocationInventoryEvent", "webhookUrl", "(Lcom/smartx/rfidreader/core/db/XtrackEventEntity;Ljava/lang/String;Ljava/lang/String;Lkotlinx/coroutines/sync/Semaphore;Lkotlin/jvm/functions/Function2;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "syncXtrackEventsWithProgress", "maxConcurrentCalls", "onProgress", "Lkotlin/Function6;", "Lkotlin/ParameterName;", "name", "current", "success", "error", "(Ljava/lang/String;Ljava/lang/String;ILkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function6;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class XtrackRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.db.XtrackObjectDao objectDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.db.XtrackLocationDao locationDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.db.XtrackEventDao xtrackEventDao = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "XtrackRepository";
    @org.jetbrains.annotations.NotNull()
    private final java.text.SimpleDateFormat isoFormat = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient httpClient = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>> objectsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackLocationEntity>> locationsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackEventEntity>> xtrackEventsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Integer> pendingXtrackCountFlow = null;
    
    public XtrackRepository(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackObjectDao objectDao, @org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackLocationDao locationDao, @org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackEventDao xtrackEventDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>> getObjectsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackLocationEntity>> getLocationsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackEventEntity>> getXtrackEventsFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Integer> getPendingXtrackCountFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getObjectByEpc(@org.jetbrains.annotations.NotNull()
    java.lang.String epc, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.db.XtrackObjectEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getLocationById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.db.XtrackLocationEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllLocationNames(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.Map<java.lang.String, java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllLocations(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.smartx.rfidreader.core.db.XtrackLocationEntity>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getObjectsByLocation(@org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object objectCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object locationCount(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    /**
     * Salva uma movimentação de local (change_location).
     * tags: lista de Triple(epc, idcode, description)
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveChangeLocation(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    java.lang.String locationName, @org.jetbrains.annotations.NotNull()
    java.util.List<kotlin.Triple<java.lang.String, java.lang.String, java.lang.String>> tags, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * Salva (ou atualiza) um inventário de local na tabela de eventos Xtrack.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object saveXtrackLocationInventory(@org.jetbrains.annotations.NotNull()
    java.lang.String deviceId, @org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    java.lang.String locationName, int total, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> foundEpcs, @org.jetbrains.annotations.NotNull()
    java.util.List<java.lang.String> missingEpcs, @org.jetbrains.annotations.Nullable()
    java.lang.Long existingEventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object findExistingXtrackLocationInventory(@org.jetbrains.annotations.NotNull()
    java.lang.String locationId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.db.XtrackEventEntity> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteXtrackEvent(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackEventEntity event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAllXtrackEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Sincroniza todos os eventos Xtrack pendentes.
     * - change_location  → chama MoveLocation no servidor Xtrack (por tag)
     * - location_inventory → POST JSON ao webhookUrl
     * Remove o evento do banco apenas em caso de sucesso.
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object syncXtrackEventsWithProgress(@org.jetbrains.annotations.NotNull()
    java.lang.String xtrackUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String webhookUrl, int maxConcurrentCalls, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.String, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onLog, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function6<? super java.lang.Integer, ? super java.lang.Integer, ? super com.smartx.rfidreader.core.db.XtrackEventEntity, ? super java.lang.Boolean, ? super java.lang.String, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onProgress, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Integer, java.lang.Integer>> $completion) {
        return null;
    }
    
    private final java.lang.Object syncChangeLocationEvent(com.smartx.rfidreader.core.db.XtrackEventEntity event, java.lang.String xtrackUrl, kotlinx.coroutines.sync.Semaphore semaphore, kotlin.jvm.functions.Function2<? super java.lang.String, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onLog, kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Boolean, java.lang.String>> $completion) {
        return null;
    }
    
    private final java.lang.Object syncLocationInventoryEvent(com.smartx.rfidreader.core.db.XtrackEventEntity event, java.lang.String xtrackUrl, java.lang.String webhookUrl, kotlinx.coroutines.sync.Semaphore semaphore, kotlin.jvm.functions.Function2<? super java.lang.String, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> onLog, kotlin.coroutines.Continuation<? super kotlin.Pair<java.lang.Boolean, java.lang.String>> $completion) {
        return null;
    }
    
    private final java.lang.String buildMoveLocationXml(java.lang.String idcode, java.lang.String locationName) {
        return null;
    }
    
    /**
     * Retorna uma página de objetos filtrada por campo/valor.
     * filterField: coluna SQLite ("description", "epc", "locationId", "active", "lastLocation").
     * filterValue: valor a buscar (LIKE %value%).
     * @return Par (lista de itens, total de itens filtrados).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object queryObjectsPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String filterField, @org.jetbrains.annotations.NotNull()
    java.lang.String filterValue, int page, int pageSize, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<? extends java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>, java.lang.Integer>> $completion) {
        return null;
    }
    
    /**
     * Retorna uma página de localizações filtrada por nome.
     * search: texto parcial no campo name (LIKE %search%).
     * @return Par (lista de itens, total filtrado).
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object searchLocationsPaged(@org.jetbrains.annotations.NotNull()
    java.lang.String search, int page, int pageSize, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Pair<? extends java.util.List<com.smartx.rfidreader.core.db.XtrackLocationEntity>, java.lang.Integer>> $completion) {
        return null;
    }
    
    private final java.lang.String postXml(java.lang.String baseUrl, java.lang.String xmlBody) {
        return null;
    }
    
    private final java.lang.String buildXmlCommand(java.lang.String command, java.util.Map<java.lang.String, java.lang.String> fields) {
        return null;
    }
    
    private final java.lang.String escapeXml(java.lang.String s) {
        return null;
    }
    
    /**
     * Parseia a resposta do GetObject e retorna um mapa indexado pelo ID do objeto.
     * Cada valor é um mapa de campos (IDCODE, DESCRIPTION, LOCATION_ID, etc.).
     */
    private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parseObjectsMap(java.lang.String xmlStr) {
        return null;
    }
    
    /**
     * Parseia a resposta do GetIdentification.
     * Retorna lista de pares (epc, objectId):
     * - epc      = IDCODE do GetIdentification (EPC real da tag, normalizado para uppercase)
     * - objectId = OBJECT_ID do GetIdentification (referencia o ID do GetObject)
     *
     * Registros sem EPC ou sem OBJECT_ID são ignorados.
     * Múltiplos registros com o mesmo OBJECT_ID são todos mantidos.
     */
    private final java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> parseIdentifications(java.lang.String xmlStr) {
        return null;
    }
    
    private final java.util.List<com.smartx.rfidreader.core.db.XtrackLocationEntity> parseLocations(java.lang.String xmlStr) {
        return null;
    }
}