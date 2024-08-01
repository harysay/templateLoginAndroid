package id.go.kebumenkab.realcount.network

import com.google.gson.GsonBuilder
import id.go.kebumenkab.realcount.utils.Constants
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class ApiConfig {

    companion object{
        private const val REQUEST_TIMEOUT = 180

        //Use 10.0.2.2 to access your actual machine.
        const val DOMAIN = "https://10.28.11.58/projectharysay/public/"
//        const val DOMAIN = "https://192.168.1.189/projectharysay/public/"
        private var retrofitelet: Retrofit? = null

        fun getApiServiceRealcount(): ApiService {
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val gson = GsonBuilder()
                .setLenient()
                .create()

            //ini untuk yang sudah SSL
//            val client = OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .addInterceptor(Interceptor { chain ->
//                    val credentials = Credentials.basic(Constants.USERNAME_HISTORIES, Constants.PASSWORD_HISTORIES)
//                    val request = chain.request().newBuilder()
//                        .header("Authorization", credentials)
//                        .build()
//                    chain.proceed(request)
//                })
//                .build()

            //ini untuk url yang biasa
            val client = OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()

            //ini untuk url yang tidak SSL
            fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
                return try {
                    // Create a trust manager that does not validate certificate chains
                    val trustAllCerts = arrayOf<TrustManager>(
                        object : X509TrustManager {
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }
                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }
                            override fun getAcceptedIssuers(): Array<X509Certificate> {
                                return arrayOf()
                            }
                        }
                    )

                    // Install the all-trusting trust manager
                    val sslContext = SSLContext.getInstance("SSL")
                    sslContext.init(null, trustAllCerts, SecureRandom())

                    // Create an ssl socket factory with our all-trusting manager
                    val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
                    val builder = OkHttpClient.Builder()
                    builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
                    builder
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }

            val retrofit = Retrofit.Builder()
                .baseUrl(DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getUnsafeOkHttpClient().build())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}