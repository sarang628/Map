package com.example.screen_map;

import java.lang.System;

/**
 * @See MapViewModel
 * [FragmentMapsBinding]
 */
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u00a2\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\u001d\u001a\u00020\u001e2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001eH\u0002J\u0012\u0010 \u001a\u00020!2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001eH\u0002J\b\u0010\"\u001a\u00020\u0004H\u0002J\u0018\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020&2\u0006\u0010\'\u001a\u00020\u001aH\u0002J\u001a\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010(\u001a\u00020)H\u0002J\"\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020+H\u0002J$\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u0002002\b\u00101\u001a\u0004\u0018\u0001022\b\u00103\u001a\u0004\u0018\u000104H\u0016J\u000e\u00105\u001a\u00020$2\u0006\u00106\u001a\u00020&J-\u00107\u001a\u00020$2\u0006\u00108\u001a\u0002092\u000e\u0010:\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001e0;2\u0006\u0010<\u001a\u00020=H\u0016\u00a2\u0006\u0002\u0010>J\u0010\u0010?\u001a\u00020$2\u0006\u00106\u001a\u00020&H\u0002J\b\u0010@\u001a\u00020$H\u0002J$\u0010A\u001a\u0004\u0018\u00010B2\b\u0010C\u001a\u0004\u0018\u00010\u001e2\u0006\u0010D\u001a\u0002092\u0006\u0010E\u001a\u000209H\u0002J\b\u0010F\u001a\u00020$H\u0002J\u0010\u0010G\u001a\u00020$2\u0006\u00106\u001a\u00020&H\u0003R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0007\u001a\u00020\b8\u0006@\u0006X\u0087.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001b\u0010\r\u001a\u00020\u000e8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010R\u001b\u0010\u0013\u001a\u00020\u00148BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0017\u0010\u0012\u001a\u0004\b\u0015\u0010\u0016R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\u001cX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006H"}, d2 = {"Lcom/example/screen_map/MapsFragment;", "Landroidx/fragment/app/Fragment;", "()V", "isMoving", "", "lastCircle", "Lcom/google/android/gms/maps/model/Circle;", "locationManager", "Lcom/example/torang_core/util/ITorangLocationManager;", "getLocationManager", "()Lcom/example/torang_core/util/ITorangLocationManager;", "setLocationManager", "(Lcom/example/torang_core/util/ITorangLocationManager;)V", "mViewModel", "Lcom/example/screen_map/MapViewModel;", "getMViewModel", "()Lcom/example/screen_map/MapViewModel;", "mViewModel$delegate", "Lkotlin/Lazy;", "mapSharedViewModel", "Lcom/example/screen_map/MapSharedViewModel;", "getMapSharedViewModel", "()Lcom/example/screen_map/MapSharedViewModel;", "mapSharedViewModel$delegate", "markers", "Ljava/util/ArrayList;", "Lcom/google/android/gms/maps/model/Marker;", "onMarkerClickListener", "Lcom/google/android/gms/maps/GoogleMap$OnMarkerClickListener;", "getRestaurantIcon", "", "restaurantType", "getRestaurantIcon1", "Lcom/google/android/gms/maps/model/BitmapDescriptor;", "hasLocationPermission", "moveCamera", "", "map", "Lcom/google/android/gms/maps/GoogleMap;", "marker", "cameraUpdate", "Lcom/google/android/gms/maps/CameraUpdate;", "latitute", "", "longituge", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onMapReady", "googleMap", "onRequestPermissionsResult", "requestCode", "", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "renewLocation", "requestPermission", "resizeBitmap", "Landroid/graphics/Bitmap;", "drawableName", "width", "height", "showLocationDialog", "subScribeUI", "screen_map_debug"})
@dagger.hilt.android.AndroidEntryPoint()
public final class MapsFragment extends androidx.fragment.app.Fragment {
    @javax.inject.Inject()
    public com.example.torang_core.util.ITorangLocationManager locationManager;
    
    /**
     * 뷰모델
     */
    private final kotlin.Lazy mViewModel$delegate = null;
    
    /**
     * 공유 뷰모델
     */
    private final kotlin.Lazy mapSharedViewModel$delegate = null;
    
    /**
     * 마커 리스트
     */
    private final java.util.ArrayList<com.google.android.gms.maps.model.Marker> markers = null;
    
    /**
     * 반경 변경 시 마지막 그려진 원을 지우기 위한 변수
     */
    private com.google.android.gms.maps.model.Circle lastCircle;
    private boolean isMoving = false;
    
    /**
     * 마커 클릭 리스너
     */
    private final com.google.android.gms.maps.GoogleMap.OnMarkerClickListener onMarkerClickListener = null;
    
    public MapsFragment() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.torang_core.util.ITorangLocationManager getLocationManager() {
        return null;
    }
    
    public final void setLocationManager(@org.jetbrains.annotations.NotNull()
    com.example.torang_core.util.ITorangLocationManager p0) {
    }
    
    /**
     * 뷰모델
     */
    private final com.example.screen_map.MapViewModel getMViewModel() {
        return null;
    }
    
    /**
     * 공유 뷰모델
     */
    private final com.example.screen_map.MapSharedViewModel getMapSharedViewModel() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"MissingPermission"})
    private final void subScribeUI(com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    public final void onMapReady(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    private final void renewLocation(com.google.android.gms.maps.GoogleMap googleMap) {
    }
    
    private final void requestPermission() {
    }
    
    private final void moveCamera(com.google.android.gms.maps.GoogleMap map, double latitute, double longituge) {
    }
    
    private final void moveCamera(com.google.android.gms.maps.GoogleMap map, com.google.android.gms.maps.CameraUpdate cameraUpdate) {
    }
    
    private final void moveCamera(com.google.android.gms.maps.GoogleMap map, com.google.android.gms.maps.model.Marker marker) {
    }
    
    private final boolean hasLocationPermission() {
        return false;
    }
    
    private final void showLocationDialog() {
    }
    
    @java.lang.Override()
    public void onRequestPermissionsResult(int requestCode, @org.jetbrains.annotations.NotNull()
    java.lang.String[] permissions, @org.jetbrains.annotations.NotNull()
    int[] grantResults) {
    }
    
    private final java.lang.String getRestaurantIcon(java.lang.String restaurantType) {
        return null;
    }
    
    private final android.graphics.Bitmap resizeBitmap(java.lang.String drawableName, int width, int height) {
        return null;
    }
    
    private final com.google.android.gms.maps.model.BitmapDescriptor getRestaurantIcon1(java.lang.String restaurantType) {
        return null;
    }
}