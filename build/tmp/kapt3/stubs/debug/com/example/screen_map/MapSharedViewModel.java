package com.example.screen_map;

import java.lang.System;

/**
 * 최초 진입 시 내위치와 가장 가까운 맛집이 선택된다.
 *
 * 검색 결과 첫번째 식당이 선택된다.
 */
@dagger.hilt.android.lifecycle.HiltViewModel()
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\t\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001b\u001a\u00020\nJ\u0006\u00106\u001a\u00020\nJ\u0006\u00107\u001a\u00020\nJ\u0006\u00108\u001a\u00020\nJ\u0006\u00109\u001a\u00020\nJ\u000e\u0010:\u001a\u00020\n2\u0006\u0010;\u001a\u00020\u0007J\u000e\u0010<\u001a\u00020\n2\u0006\u0010=\u001a\u00020\fJ\u009e\u0001\u0010>\u001a\u00020\n2\n\b\u0002\u0010?\u001a\u0004\u0018\u00010@2\u001c\b\u0002\u0010A\u001a\u0016\u0012\u0004\u0012\u00020C\u0018\u00010Bj\n\u0012\u0004\u0012\u00020C\u0018\u0001`D2\n\b\u0002\u0010E\u001a\u0004\u0018\u00010F2\u001c\b\u0002\u0010G\u001a\u0016\u0012\u0004\u0012\u00020H\u0018\u00010Bj\n\u0012\u0004\u0012\u00020H\u0018\u0001`D2\b\b\u0002\u0010I\u001a\u00020J2\b\b\u0002\u0010K\u001a\u00020J2\b\b\u0002\u0010L\u001a\u00020J2\b\b\u0002\u0010M\u001a\u00020J2\b\b\u0002\u0010N\u001a\u00020J2\b\b\u0002\u0010O\u001a\u00020J2\u0006\u0010P\u001a\u00020QJ\u0018\u0010R\u001a\u00020\n2\u0006\u0010I\u001a\u00020J2\u0006\u0010K\u001a\u00020JH\u0002J\u000e\u0010S\u001a\u00020\n2\u0006\u0010T\u001a\u00020\u001eJ\u000e\u0010U\u001a\u00020\n2\u0006\u0010V\u001a\u00020\u0014J\u0010\u0010W\u001a\u00020\n2\u0006\u0010#\u001a\u00020\u0007H\u0002J\u000e\u0010X\u001a\u00020\n2\u0006\u0010Y\u001a\u00020\u0010R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00130\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\f0\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00140\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00070\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u001d\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001aR\u0017\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0017\u0010!\u001a\b\u0012\u0004\u0012\u00020\f0\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u001aR\u0017\u0010#\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010 R\u001a\u0010$\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b$\u0010%\"\u0004\b&\u0010\'R\u0017\u0010(\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010\u001aR\u0017\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00100\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010\u001aR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u0017\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00070\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u0010\u001aR\u001d\u00100\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00130\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010\u001aR\u0017\u00102\u001a\b\u0012\u0004\u0012\u00020\f0\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b3\u0010\u001aR\u0017\u00104\u001a\b\u0012\u0004\u0012\u00020\u00140\u0018\u00a2\u0006\b\n\u0000\u001a\u0004\b5\u0010\u001a\u00a8\u0006Z"}, d2 = {"Lcom/example/screen_map/MapSharedViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/torang_core/repository/MapSharedRepository;", "(Lcom/example/torang_core/repository/MapSharedRepository;)V", "_checkPermission", "Landroidx/lifecycle/MutableLiveData;", "", "_clickMyLocation", "Lcom/example/torang_core/util/Event;", "", "_errorMsg", "", "_mapMode", "Lcom/example/torang_core/data/model/MapMode;", "_myLocation", "Landroid/location/Location;", "_requestLocation", "_restaurants", "", "Lcom/example/torang_core/data/model/RestaurantData;", "_searchKeyword", "_selectedRestaurant", "checkPermission", "Landroidx/lifecycle/LiveData;", "getCheckPermission", "()Landroidx/lifecycle/LiveData;", "clickMyLocation", "getClickMyLocation", "currentRestaurantPosition", "", "getCurrentRestaurantPosition", "()Landroidx/lifecycle/MutableLiveData;", "errorMsg", "getErrorMsg", "isExpended", "isMoved", "()Z", "setMoved", "(Z)V", "mapMode", "getMapMode", "myLocation", "getMyLocation", "getRepository", "()Lcom/example/torang_core/repository/MapSharedRepository;", "requestLocation", "getRequestLocation", "restaurants", "getRestaurants", "searchKeyword", "getSearchKeyword", "selectedRestaurant", "getSelectedRestaurant", "clickTravelMode", "confirmCheckPermission", "confirmRequestLocation", "mapClick", "mapExpand", "b", "search", "keyword", "searchFilterRestaurant", "distances", "Lcom/example/torang_core/data/model/Distances;", "restaurantType", "Ljava/util/ArrayList;", "Lcom/example/torang_core/data/model/RestaurantType;", "Lkotlin/collections/ArrayList;", "prices", "Lcom/example/torang_core/data/model/Prices;", "ratings", "Lcom/example/torang_core/data/model/Ratings;", "latitude", "", "longitude", "northEastLatitude", "northEastLongitude", "southWestLatitude", "southWestLongitude", "searchType", "Lcom/example/torang_core/data/model/Filter$SearchType;", "searchRestaurant", "selectPosition", "position", "selectRestaurant", "restaurantData", "setIsExpended", "setLocation", "result", "screen_map_debug"})
public final class MapSharedViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.torang_core.repository.MapSharedRepository repository = null;
    private final androidx.lifecycle.MutableLiveData<com.example.torang_core.data.model.MapMode> _mapMode = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.torang_core.data.model.MapMode> mapMode = null;
    private boolean isMoved = false;
    
    /**
     * 권한 체크 플레그
     */
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> _checkPermission = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Boolean> checkPermission = null;
    
    /**
     * 최초 진입 플레그
     */
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> _requestLocation = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.Boolean> requestLocation = null;
    
    /**
     * 현재 맛집 리스트
     */
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.example.torang_core.data.model.RestaurantData>> _restaurants = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.example.torang_core.data.model.RestaurantData>> restaurants = null;
    
    /**
     * 필터와 하단 정보 보여지는 여부
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> isExpended = null;
    
    /**
     * 검색어
     */
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _searchKeyword = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> searchKeyword = null;
    
    /**
     * 현재 선택된 맛집 포지션
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Integer> currentRestaurantPosition = null;
    
    /**
     * 위치 클릭 리스너
     */
    private final androidx.lifecycle.MutableLiveData<com.example.torang_core.util.Event<kotlin.Unit>> _clickMyLocation = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.torang_core.util.Event<kotlin.Unit>> clickMyLocation = null;
    
    /**
     * 내 위치
     */
    private final androidx.lifecycle.MutableLiveData<android.location.Location> _myLocation = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<android.location.Location> myLocation = null;
    
    /**
     * 업로드 할 식당 정보
     */
    private final androidx.lifecycle.MutableLiveData<com.example.torang_core.data.model.RestaurantData> _selectedRestaurant = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<com.example.torang_core.data.model.RestaurantData> selectedRestaurant = null;
    
    /**
     * 업로드 할 식당 정보
     */
    private final androidx.lifecycle.MutableLiveData<java.lang.String> _errorMsg = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.lang.String> errorMsg = null;
    
    @javax.inject.Inject()
    public MapSharedViewModel(@org.jetbrains.annotations.NotNull()
    com.example.torang_core.repository.MapSharedRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.torang_core.repository.MapSharedRepository getRepository() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.torang_core.data.model.MapMode> getMapMode() {
        return null;
    }
    
    public final boolean isMoved() {
        return false;
    }
    
    public final void setMoved(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Boolean> getCheckPermission() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Boolean> getRequestLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.example.torang_core.data.model.RestaurantData>> getRestaurants() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> isExpended() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getSearchKeyword() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Integer> getCurrentRestaurantPosition() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.torang_core.util.Event<kotlin.Unit>> getClickMyLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<android.location.Location> getMyLocation() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.example.torang_core.data.model.RestaurantData> getSelectedRestaurant() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.String> getErrorMsg() {
        return null;
    }
    
    /**
     * 맛집 검색
     *
     * 맛집 검색 결과 시 첫번째 결과를 활성화 합니다.
     */
    public final void search(@org.jetbrains.annotations.NotNull()
    java.lang.String keyword) {
    }
    
    /**
     * 현재 맵 필터 하단정보가 보여지는지 상태
     */
    private final void setIsExpended(boolean isExpended) {
    }
    
    /**
     * 맵 클릭
     */
    public final void mapClick() {
    }
    
    /**
     * 맵 필터 하단 정보 보여지는 여부
     */
    public final void mapExpand(boolean b) {
    }
    
    public final void selectPosition(int position) {
    }
    
    /**
     * 위치 검색
     */
    private final void searchRestaurant(double latitude, double longitude) {
    }
    
    /**
     * 내 위치 설정
     * 내 위치 설정시 최초 설정이라면 내 주변에 식당을 검색 한다.
     */
    public final void setLocation(@org.jetbrains.annotations.NotNull()
    android.location.Location result) {
    }
    
    /**
     * 내위치 클릭
     */
    public final void clickMyLocation() {
    }
    
    /**
     * 내 위치 확인
     */
    public final void confirmRequestLocation() {
    }
    
    /**
     * 위치권한 확인
     */
    public final void confirmCheckPermission() {
    }
    
    public final void selectRestaurant(@org.jetbrains.annotations.NotNull()
    com.example.torang_core.data.model.RestaurantData restaurantData) {
    }
    
    /**
     * 필터 검색
     */
    public final void searchFilterRestaurant(@org.jetbrains.annotations.Nullable()
    com.example.torang_core.data.model.Distances distances, @org.jetbrains.annotations.Nullable()
    java.util.ArrayList<com.example.torang_core.data.model.RestaurantType> restaurantType, @org.jetbrains.annotations.Nullable()
    com.example.torang_core.data.model.Prices prices, @org.jetbrains.annotations.Nullable()
    java.util.ArrayList<com.example.torang_core.data.model.Ratings> ratings, double latitude, double longitude, double northEastLatitude, double northEastLongitude, double southWestLatitude, double southWestLongitude, @org.jetbrains.annotations.NotNull()
    com.example.torang_core.data.model.Filter.SearchType searchType) {
    }
    
    public final void clickTravelMode() {
    }
}