package tecsor.andrei.dissertation.provider.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tecsor.andrei.dissertation.provider.service.ApiCaller;

@Configuration
@EnableAsync
public class ProviderConfig {
    @Bean
    public ApiCaller apiCaller() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        // TODO: 29.06.2023 extend timeout

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiCaller.class);
    }
}
