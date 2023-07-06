package tecsor.andrei.dissertation.provider.tcp.client;

import tecsor.andrei.dissertation.provider.dto.ResultDTO;
import tecsor.andrei.dissertation.provider.dto.UserStatisticsDTO;
import tecsor.andrei.dissertation.provider.model.FHEOperationType;
import tecsor.andrei.dissertation.provider.model.Risk;
import tecsor.andrei.dissertation.provider.model.UserStatistics;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Base64;

public class TcpClient {
    private TcpClient() {
    }

    public static Risk getRiskScore(ResultDTO resultDTO) throws IOException {
        String hostname = "localhost"; // server hostname
        int port = 8071; // server port number

        // create a socket connection to the server
        try (Socket socket = new Socket(hostname, port)) {

            OutputStream out = socket.getOutputStream();

            //send the operation type
            sendIntThroughSocket(out, FHEOperationType.DECRYPT.value);

            //send the int values from resultDTO
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(resultDTO.getSize());
            out.write(buffer.array());

            out.write(Base64.getDecoder().decode(resultDTO.getResult()));

            //receive the risk score
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String received = input.readLine();
            long riskScore = Long.parseLong(received);
            Risk risk = new Risk();
            risk.setScore((int) riskScore);
            return risk;

        }
    }

    public static UserStatisticsDTO getUserStatisticsDTO(UserStatistics userStatistics) throws IOException {
        String hostname = "localhost"; // server hostname
        int port = 8071; // server port number

        // create a socket connection to the server
        try (Socket socket = new Socket(hostname, port)) {

            OutputStream out = socket.getOutputStream();

            //send the operation type
            sendIntThroughSocket(out, FHEOperationType.ENCRYPT.value);

            //send the int values from userStatistics
            sendIntThroughSocket(out, userStatistics.getGamblingPercent());
            sendIntThroughSocket(out, userStatistics.getOverspendingScore());
            sendIntThroughSocket(out, userStatistics.getImpulsiveBuyingScore());
            sendIntThroughSocket(out, userStatistics.getMeanDepositSum());
            sendIntThroughSocket(out, userStatistics.getMeanReportedIncome());
            sendIntThroughSocket(out, userStatistics.getNoMonthsDeposited());

            // receive a response from the server
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] receivedBytes = new byte[1024];
            int count = dataInputStream.read(receivedBytes); // read bytes from the input stream
            String response = new String(receivedBytes, 0, count); // convert bytes to string

            System.out.println("Response from server: " + response);

            byte[] sizeBytes = new byte[8];
            dataInputStream.readFully(sizeBytes);
            long size = ByteBuffer.wrap(sizeBytes).order(ByteOrder.BIG_ENDIAN).getLong();

            UserStatisticsDTO dto = buildUserStatisticsDTO(dataInputStream, (int) size);

            dataInputStream.close();

            return dto;

        }
    }

    private static UserStatisticsDTO buildUserStatisticsDTO(DataInputStream dataInputStream, int size) throws IOException {
        UserStatisticsDTO dto = new UserStatisticsDTO();

        dto.setSize(size);

        //Get the values from the server
        byte[] encryptedGamblingPercent = new byte[size];
        dataInputStream.readFully(encryptedGamblingPercent);
        dto.setGamblingPercent(Base64.getEncoder().encodeToString(encryptedGamblingPercent));

        // encrypted_overspending_score
        byte[] encryptedOverspendingScore = new byte[size];
        dataInputStream.readFully(encryptedOverspendingScore);
        dto.setOverspendingScore(Base64.getEncoder().encodeToString(encryptedOverspendingScore));

        // encrypted_impulsive_buying_score
        byte[] encryptedImpulsiveBuyingScore = new byte[size];
        dataInputStream.readFully(encryptedImpulsiveBuyingScore);
        dto.setImpulsiveBuyingScore(Base64.getEncoder().encodeToString(encryptedImpulsiveBuyingScore));

        // encrypted_mean_deposit_sum
        byte[] encryptedMeanDepositSum = new byte[size];
        dataInputStream.readFully(encryptedMeanDepositSum);
        dto.setMeanDepositSum(Base64.getEncoder().encodeToString(encryptedMeanDepositSum));

        // encrypted_mean_reported_income
        byte[] encryptedMeanReportedIncome = new byte[size];
        dataInputStream.readFully(encryptedMeanReportedIncome);
        dto.setMeanReportedIncome(Base64.getEncoder().encodeToString(encryptedMeanReportedIncome));

        // encrypted_no_months_deposited
        byte[] encryptedNoMonthsDeposited = new byte[size];
        dataInputStream.readFully(encryptedNoMonthsDeposited);
        dto.setNoMonthsDeposited(Base64.getEncoder().encodeToString(encryptedNoMonthsDeposited));

        return dto;
    }

    public static void sendIntThroughSocket(OutputStream out, int value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        byte[] bytes = buffer.array();
        out.write(bytes);
    }

}
