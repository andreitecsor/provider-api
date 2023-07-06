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
        System.out.println("\nSending the following user statistics: " + userStatistics + " to the tfhe-generator");
        UserStatisticsDTO userStatisticsDTO = getUserStatisticsDTO(userStatistics);

        System.out.println("Sending the encrypted user statistics to the requester");
        Call<ResultDTO> processCall = apiCaller.process(userStatisticsDTO);
        Response<ResultDTO> processResponse = processCall.execute();
        ResultDTO resultDTO = processResponse.body();

        System.out.println("Sending the encrypted risk score to the tfhe-generator for decryption");
        assert resultDTO != null;
        Risk riskScore = getRiskScore(resultDTO);
        riskScore.setPid(userStatistics.getPid());
        riskScore.setFid("p1");

        System.out.println("\nRisk score is " + riskScore + " for user with pid= " + userStatistics.getPid());
        System.out.println("Sending the risk score to the requester");
        Call<Void> provideCall = apiCaller.provide(riskScore);
        Response<Void> provideResponse = provideCall.execute();

        System.out.println("Provided with status code: " + provideResponse.code());
        System.out.println("DONE!");
    }
}