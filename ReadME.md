val list = JsonToObjectGenerator<Restaurant>().getListByFile(
        context,
        "restaurants.json",
        Restaurant::class.java
    )