package com.smartx.rfidreader.ui.xtrack;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0012\u0010\u0012\u001a\u00020\u00112\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0014J\b\u0010\u0015\u001a\u00020\u0011H\u0002J\b\u0010\u0016\u001a\u00020\u0011H\u0002J\b\u0010\u0017\u001a\u00020\u0011H\u0002J\b\u0010\u0018\u001a\u00020\u0011H\u0002J\b\u0010\u0019\u001a\u00020\u0011H\u0002J\b\u0010\u001a\u001a\u00020\u0011H\u0002J\u0010\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u001dH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\n\u001a\u00020\u000b8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u000e\u0010\u000f\u001a\u0004\b\f\u0010\r\u00a8\u0006\u001e"}, d2 = {"Lcom/smartx/rfidreader/ui/xtrack/XtrackActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/smartx/rfidreader/databinding/ActivityXtrackBinding;", "eventListAdapter", "Lcom/smartx/rfidreader/ui/xtrack/XtrackEventListAdapter;", "logAdapter", "Lcom/smartx/rfidreader/ui/xtrack/XtrackLogAdapter;", "syncLogAdapter", "viewModel", "Lcom/smartx/rfidreader/ui/xtrack/XtrackViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/xtrack/XtrackViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "observeState", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "setupCounterCards", "setupDownloadButton", "setupEventsList", "setupHeader", "setupLogList", "setupXtrackEventsSection", "showPayloadDialog", "event", "Lcom/smartx/rfidreader/core/db/XtrackEventEntity;", "app_debug"})
public final class XtrackActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.smartx.rfidreader.databinding.ActivityXtrackBinding binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.smartx.rfidreader.ui.xtrack.XtrackLogAdapter logAdapter;
    private com.smartx.rfidreader.ui.xtrack.XtrackLogAdapter syncLogAdapter;
    private com.smartx.rfidreader.ui.xtrack.XtrackEventListAdapter eventListAdapter;
    
    public XtrackActivity() {
        super();
    }
    
    private final com.smartx.rfidreader.ui.xtrack.XtrackViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupHeader() {
    }
    
    private final void setupDownloadButton() {
    }
    
    private final void setupCounterCards() {
    }
    
    private final void setupLogList() {
    }
    
    private final void setupEventsList() {
    }
    
    private final void showPayloadDialog(com.smartx.rfidreader.core.db.XtrackEventEntity event) {
    }
    
    private final void setupXtrackEventsSection() {
    }
    
    private final void observeState() {
    }
}