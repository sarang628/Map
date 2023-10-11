# Retrofit 모듈 만들기

https://square.github.io/retrofit/

http 원격 API를 호출하기위해서는

## 1. Retrofit 인스턴스를 생성해야 함.

홈페이지의 예제
```
Retrofit retrofit = new Retrofit.Builder()
    .baseUrl("https://api.github.com/")
    .build();

GitHubService service = retrofit.create(GitHubService.class);
```

hilt를 사용해 Retrofit 객체를 주입하여 사용 할 수 있게 구현
```
/**
 * Hilt를 사용하여 Retrofit을 주입 할 수 있도록 제공
 */
@Singleton
class RetrofitModule @Inject constructor() {
    fun getRetrofit(httpClient: OkHttpClient, API_URL: String): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
```
모듈을 사용하려면 OkHttpClient를 생성 할 줄 알아야해서 이부분도 조금 더 편하게 사용 할 수 있도록 변화가 필요.

## 2. HTTP 요청 서비스 정의
```
interface FeedServices {
    @FormUrlEncoded
    @POST("getFeeds1")
    suspend fun getFeeds(@FieldMap params: Map<String, String>): List<RemoteFeed>

    @POST("deleteReview")
    suspend fun deleteReview(@Body review: ReviewDeleteRequestVO): Review

    @POST("addLike")
    suspend fun addLike(@Body like: Like): Like

    @POST("deleteLike")
    suspend fun deleteLike(@Body like: Like): Like

    @POST("deleteFavorite")
    suspend fun deleteFavorite(@Body favorite: Favorite): Favorite

    @POST("addFavorite")
    suspend fun addFavorite(@Body favorite: Favorite): Favorite
}
```

3. OkHttpClient 설정 및 제공하기
