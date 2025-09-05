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