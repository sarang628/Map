```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

```
dependencies {
    implementation("com.github.sarang628:Map:Tag")
}
```
[recent version](https://jitpack.io/#sarang628/Map)


```
MapScreen()
```



val list = JsonToObjectGenerator<Restaurant>().getListByFile(
        context,
        "restaurants.json",
        Restaurant::class.java
    )


# 지도 화면 이동 시키는 방법
- [MapViewModel](library/src/main/java/com/example/screen_map/viewmodels/MapViewModel.kt)에 setCameraPosition() 함수 호출
- FindRepositoryImpl에 CameraPosition Flow 데이터를 collect 하고 있음. 해당 값 변경(다른 모듈에서 카메라 위치를 이동시키고 싶을 때)