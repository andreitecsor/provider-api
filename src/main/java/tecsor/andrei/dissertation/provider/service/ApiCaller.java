package tecsor.andrei.dissertation.provider.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import tecsor.andrei.dissertation.provider.dto.ResultDTO;
import tecsor.andrei.dissertation.provider.dto.UserStatisticsDTO;

public interface ApiCaller {
    @POST("/requester/api/v1/process")
    Call<ResultDTO> process(@Body UserStatisticsDTO userStatisticsDTO);
}
