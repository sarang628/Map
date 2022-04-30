package com.example.screen_map;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 2, d1 = {"\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a \u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u001a\u0012\u0010\u0007\u001a\u00020\b*\u00020\t2\u0006\u0010\n\u001a\u00020\u000b\u001a\"\u0010\f\u001a\u00020\r*\u00020\u000e2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012\u00a8\u0006\u0013"}, d2 = {"drawCircle", "Lcom/google/android/gms/maps/model/Circle;", "Lcom/google/android/gms/maps/GoogleMap;", "location", "Landroid/location/Location;", "distances", "Lcom/example/torang_core/data/model/Distances;", "hasPermission", "", "Landroid/content/Context;", "permission", "", "requestPermissionWithRationale", "", "Landroid/app/Activity;", "requestCode", "", "snackbar", "Lcom/google/android/material/snackbar/Snackbar;", "screen_map_debug"})
public final class UtilsKt {
    
    /**
     * Helper functions to simplify permission checks/requests.
     */
    public static final boolean hasPermission(@org.jetbrains.annotations.NotNull()
    android.content.Context $this$hasPermission, @org.jetbrains.annotations.NotNull()
    java.lang.String permission) {
        return false;
    }
    
    /**
     * Requests permission and if the user denied a previous request, but didn't check
     * "Don't ask again", we provide additional rationale.
     *
     * Note: The [Snackbar] should have an action to request the permission.
     */
    public static final void requestPermissionWithRationale(@org.jetbrains.annotations.NotNull()
    android.app.Activity $this$requestPermissionWithRationale, @org.jetbrains.annotations.NotNull()
    java.lang.String permission, int requestCode, @org.jetbrains.annotations.NotNull()
    com.google.android.material.snackbar.Snackbar snackbar) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public static final com.google.android.gms.maps.model.Circle drawCircle(@org.jetbrains.annotations.NotNull()
    com.google.android.gms.maps.GoogleMap $this$drawCircle, @org.jetbrains.annotations.Nullable()
    android.location.Location location, @org.jetbrains.annotations.Nullable()
    com.example.torang_core.data.model.Distances distances) {
        return null;
    }
}