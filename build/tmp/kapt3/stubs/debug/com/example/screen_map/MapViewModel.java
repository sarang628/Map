package com.example.screen_map;

import java.lang.System;

@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u000e\u001a\u00020\u0016J\u0006\u0010\u0017\u001a\u00020\u0016J \u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001a2\b\b\u0002\u0010\u001c\u001a\u00020\u001dJ\u000e\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010\u001f\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001aJ\u000e\u0010 \u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u001aJ\u000e\u0010!\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001aR\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\t0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00140\u000f8F\u00a2\u0006\u0006\u001a\u0004\b\u0015\u0010\u0012\u00a8\u0006\""}, d2 = {"Lcom/example/screen_map/MapViewModel;", "Landroidx/lifecycle/ViewModel;", "mapRepository", "Lcom/example/torang_core/repository/MapRepository;", "nationRepository", "Lcom/example/torang_core/repository/NationRepository;", "(Lcom/example/torang_core/repository/MapRepository;Lcom/example/torang_core/repository/NationRepository;)V", "_cameraUpdate", "Landroidx/lifecycle/MutableLiveData;", "Lcom/google/android/gms/maps/CameraUpdate;", "cameraUpdate", "Landroidx/lifecycle/LiveData;", "getCameraUpdate", "()Landroidx/lifecycle/LiveData;", "clickMap", "Lkotlinx/coroutines/flow/Flow;", "", "getClickMap", "()Lkotlinx/coroutines/flow/Flow;", "selectdNationItem", "Lcom/example/torang_core/data/NationItem;", "getSelectdNationItem", "", "loadRestaurant", "setLocation", "latitude", "", "longitude", "zoom", "", "setNorthEastLatitude", "setNorthEastLongitude", "setSouthWestLatitude", "setSouthWestLongitude", "screen_map_debug"})
public final class MapViewModel extends androidx.lifecycle.ViewModel {
    private final com.example.torang_core.repository.MapRepository mapRepository = null;
    private final com.example.torang_core.repository.NationRepository nationRepository = null;
    private final androidx.lifecycle.MutableLiveData<com.google.android.gms.maps.CameraUpdate> _cameraUpdate = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.google.android.gms.maps.CameraUpdate> cameraUpdate = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<java.lang.Boolean> clickMap = null;
    
    @javax.inject.Inject()
    public MapViewModel(@org.jetbrains.annotations.NotNull()
    com.example.torang_core.repository.MapRepository mapRepository, @org.jetbrains.annotations.NotNull()
    com.example.torang_core.repository.NationRepository nationRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.google.android.gms.maps.CameraUpdate> getCameraUpdate() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.example.torang_core.data.NationItem> getSelectdNationItem() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.lang.Boolean> getClickMap() {
        return null;
    }
    
    public final void loadRestaurant() {
    }
    
    public final void setLocation(double latitude, double longitude, float zoom) {
    }
    
    public final void clickMap() {
    }
    
    public final void setNorthEastLatitude(double latitude) {
    }
    
    public final void setNorthEastLongitude(double longitude) {
    }
    
    public final void setSouthWestLatitude(double latitude) {
    }
    
    public final void setSouthWestLongitude(double longitude) {
    }
}