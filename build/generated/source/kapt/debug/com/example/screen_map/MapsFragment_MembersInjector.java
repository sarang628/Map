// Generated by Dagger (https://dagger.dev).
package com.example.screen_map;

import com.example.torang_core.util.ITorangLocationManager;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MapsFragment_MembersInjector implements MembersInjector<MapsFragment> {
  private final Provider<ITorangLocationManager> locationManagerProvider;

  public MapsFragment_MembersInjector(Provider<ITorangLocationManager> locationManagerProvider) {
    this.locationManagerProvider = locationManagerProvider;
  }

  public static MembersInjector<MapsFragment> create(
      Provider<ITorangLocationManager> locationManagerProvider) {
    return new MapsFragment_MembersInjector(locationManagerProvider);
  }

  @Override
  public void injectMembers(MapsFragment instance) {
    injectLocationManager(instance, locationManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.screen_map.MapsFragment.locationManager")
  public static void injectLocationManager(MapsFragment instance,
      ITorangLocationManager locationManager) {
    instance.locationManager = locationManager;
  }
}