package tecsor.andrei.dissertation.provider.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import tecsor.andrei.dissertation.provider.dto.ResultDTO;
import tecsor.andrei.dissertation.provider.dto.UserStatisticsDTO;
import tecsor.andrei.dissertation.provider.model.Risk;
import tecsor.andrei.dissertation.provider.model.UserStatistics;

import java.io.IOException;

import static tecsor.andrei.dissertation.provider.tcp.client.TcpClient.getRiskScore;
import static tecsor.andrei.dissertation.provider.tcp.client.TcpClient.getUserStatisticsDTO;

@Component
public class ProviderService {
    private final ApiCaller apiCaller;

    public ProviderService(ApiCaller apiCaller) {
        this.apiCaller = apiCaller;
    }


    @Async
    public void afterIsClientAvailable(UserStatistics userStatistics) throws IOException {
        UserStatisticsDTO userStatisticsDTO = getUserStatisticsDTO(userStatistics);

        Call<ResultDTO> callProcess = apiCaller.process(userStatisticsDTO);
        Response<ResultDTO> response = callProcess.execute();
        ResultDTO resultDTO = response.body();

        Risk riskScore = getRiskScore(resultDTO);
        System.out.println(riskScore);

        //todo: check if the data is safe
        //todo: provide the final result
    }
}