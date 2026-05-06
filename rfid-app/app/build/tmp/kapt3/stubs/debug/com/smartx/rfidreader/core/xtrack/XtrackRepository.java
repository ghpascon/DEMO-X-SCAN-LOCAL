package com.smartx.rfidreader.core.xtrack;

/**
 * Responsável por toda a integração com o servidor Xtrack:
 * - Requisições HTTP (GET/POST com XML)
 * - Persistência local de objetos e localizações
 * - Lookup rápido por EPC (usado em background durante leitura)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\b\u0015\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J$\u0010\u0014\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\u0012\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u0017H\u0002J\u0010\u0010\u0018\u001a\u00020\b2\u0006\u0010\u0019\u001a\u00020\bH\u0002J:\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\b2\u0014\b\u0002\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020 0\u001fH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\"J:\u0010#\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001b2\u0006\u0010\u001d\u001a\u00020\b2\u0014\b\u0002\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020 0\u001fH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b$\u0010\"J\u0018\u0010%\u001a\u0004\u0018\u00010\u000e2\u0006\u0010&\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\'J\u0018\u0010(\u001a\u0004\u0018\u00010\u00122\u0006\u0010)\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\'J\u000e\u0010*\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010+J\u000e\u0010,\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010+J\"\u0010-\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0.0\r2\u0006\u0010/\u001a\u00020\bH\u0002J\u0016\u00100\u001a\b\u0012\u0004\u0012\u00020\u000e0\r2\u0006\u0010/\u001a\u00020\bH\u0002J(\u00101\u001a\u001a\u0012\u0004\u0012\u00020\b\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u00170\u00172\u0006\u0010/\u001a\u00020\bH\u0002J,\u00102\u001a\b\u0012\u0004\u0012\u00020\b0\u001b2\u0006\u0010\u001d\u001a\u00020\b2\u0006\u00103\u001a\u00020\bH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u00105J\u0018\u00106\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\b2\u0006\u00103\u001a\u00020\bH\u0002JB\u00107\u001a\b\u0012\u0004\u0012\u00020\b0\u001b2\u0006\u0010\u001d\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\b2\u0014\b\u0002\u0010\u0016\u001a\u000e\u0012\u0004\u0012\u00020\b\u0012\u0004\u0012\u00020\b0\u0017H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b8\u00109JB\u0010:\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\r\u0012\u0004\u0012\u00020\u001c0.2\u0006\u0010;\u001a\u00020\b2\u0006\u0010<\u001a\u00020\b2\u0006\u0010=\u001a\u00020\u001c2\b\b\u0002\u0010>\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010?J:\u0010@\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r\u0012\u0004\u0012\u00020\u001c0.2\u0006\u0010A\u001a\u00020\b2\u0006\u0010=\u001a\u00020\u001c2\b\b\u0002\u0010>\u001a\u00020\u001cH\u0086@\u00a2\u0006\u0002\u0010BR\u000e\u0010\u0007\u001a\u00020\bX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00120\r0\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0010\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006C"}, d2 = {"Lcom/smartx/rfidreader/core/xtrack/XtrackRepository;", "", "objectDao", "Lcom/smartx/rfidreader/core/db/XtrackObjectDao;", "locationDao", "Lcom/smartx/rfidreader/core/db/XtrackLocationDao;", "(Lcom/smartx/rfidreader/core/db/XtrackObjectDao;Lcom/smartx/rfidreader/core/db/XtrackLocationDao;)V", "TAG", "", "httpClient", "Lokhttp3/OkHttpClient;", "locationsFlow", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/smartx/rfidreader/core/db/XtrackLocationEntity;", "getLocationsFlow", "()Lkotlinx/coroutines/flow/Flow;", "objectsFlow", "Lcom/smartx/rfidreader/core/db/XtrackObjectEntity;", "getObjectsFlow", "buildXmlCommand", "command", "fields", "", "escapeXml", "s", "fetchAndSaveLocations", "Lkotlin/Result;", "", "baseUrl", "onLog", "Lkotlin/Function1;", "", "fetchAndSaveLocations-0E7RQCE", "(Ljava/lang/String;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchAndSaveObjects", "fetchAndSaveObjects-0E7RQCE", "getLocationById", "id", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getObjectByEpc", "epc", "locationCount", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "objectCount", "parseIdentifications", "Lkotlin/Pair;", "xmlStr", "parseLocations", "parseObjectsMap", "postRawXml", "xmlBody", "postRawXml-0E7RQCE", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postXml", "postXmlCommand", "postXmlCommand-BWLJW6A", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "queryObjectsPaged", "filterField", "filterValue", "page", "pageSize", "(Ljava/lang/String;Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "searchLocationsPaged", "search", "(Ljava/lang/String;IILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class XtrackRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.db.XtrackObjectDao objectDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.smartx.rfidreader.core.db.XtrackLocationDao locationDao = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "XtrackRepository";
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient httpClient = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackObjectEntity>> objectsFlow = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.util.List<com.smartx.rfidreader.core.db.XtrackLocationEntity>> locationsFlow = null;
    
    public XtrackRepository(@org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackObjectDao objectDao, @org.jetbrains.annotations.NotNull()
    com.smartx.rfidreader.core.db.XtrackLocationDao locationDao) {
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