import com.example.auth_client.auth.CookiesInterceptor
import com.example.auth_client.auth.TokenAuthenticator
import com.example.auth_client.data.NetworkService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkServiceImpl {
    private const val BASE_URL = "http://192.168.0.4:3000"

    private val okHttpClient: OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(TokenAuthenticator())
            .addInterceptor(CookiesInterceptor())
            .addNetworkInterceptor(CookiesInterceptor())
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    private val retrofit: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val SERVICE: NetworkService = retrofit.create(
        NetworkService::class.java
    )
}