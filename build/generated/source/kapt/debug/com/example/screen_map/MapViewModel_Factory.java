// Generated by Dagger (https://dagger.dev).
package com.example.screen_map;

import com.example.torang_core.repository.MapRepository;
import com.example.torang_core.repository.NationRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MapViewModel_Factory implements Factory<MapViewModel> {
  private final Provider<MapRepository> mapRepositoryProvider;

  private final Provider<NationRepository> nationRepositoryProvider;

  public MapViewModel_Factory(Provider<MapRepository> mapRepositoryProvider,
      Provider<NationRepository> nationRepositoryProvider) {
    this.mapRepositoryProvider = mapRepositoryProvider;
    this.nationRepositoryProvider = nationRepositoryProvider;
  }

  @Override
  public MapViewModel get() {
    return newInstance(mapRepositoryProvider.get(), nationRepositoryProvider.get());
  }

  public static MapViewModel_Factory create(Provider<MapRepository> mapRepositoryProvider,
      Provider<NationRepository> nationRepositoryProvider) {
    return new MapViewModel_Factory(mapRepositoryProvider, nationRepositoryProvider);
  }

  public static MapViewModel newInstance(MapRepository mapRepository,
      NationRepository nationRepository) {
    return new MapViewModel(mapRepository, nationRepository);
  }
}