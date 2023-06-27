package tecsor.andrei.dissertation.provider.client;

import tecsor.andrei.dissertation.provider.model.FHEOperationType;
import tecsor.andrei.dissertation.provider.model.UserStatistics;
import tecsor.andrei.dissertation.provider.model.UserStatisticsDTO;

import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TcpClient {
    //For the moment, this is just for testing (later, i will integrate it with the api)
    public static void main(String[] args) {
        String hostname = "localhost"; // server hostname
        int port = 8071; // server port number

        // create a socket connection to the server
        try (Socket socket = new Socket(hostname, port)) {

            UserStatistics userStatistics = new UserStatistics("3ac8bb5f170b7f34e5e6d74c3f0a33db186b31c7a68e5706a4f54c11693934f6",
                    6, 10, 3, 5600, 5000, 2);

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

            UserStatisticsDTO userStatisticsDTO = buildUserStatisticsDTO(dataInputStream, (int) size);

            dataInputStream.close();

            try {
                //Use FileOutputStream to write to a file
                FileOutputStream fileOut = new FileOutputStream("userstatisticsdto.ser");
                //Use ObjectOutputStream to write objects
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOut);
                objectOutputStream.writeObject(userStatisticsDTO);
                objectOutputStream.close();
                fileOut.close();
                System.out.print("Serialized data is saved in userstatisticsdto.ser");
            } catch (IOException i) {
                i.printStackTrace();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static UserStatisticsDTO buildUserStatisticsDTO(DataInputStream dataInputStream, int size) throws IOException {
        UserStatisticsDTO userStatisticsDTO = new UserStatisticsDTO(size);

        //Get the values from the server
        byte[] encryptedGamblingPercent = new byte[size];
        dataInputStream.readFully(encryptedGamblingPercent);
        userStatisticsDTO.setGamblingPercent(encryptedGamblingPercent);

        // encrypted_overspending_score
        byte[] encryptedOverspendingScore = new byte[size];
        dataInputStream.readFully(encryptedOverspendingScore);
        userStatisticsDTO.setOverspendingScore(encryptedOverspendingScore);

        // encrypted_impulsive_buying_score
        byte[] encryptedImpulsiveBuyingScore = new byte[size];
        dataInputStream.readFully(encryptedImpulsiveBuyingScore);
        userStatisticsDTO.setImpulsiveBuyingScore(encryptedImpulsiveBuyingScore);

        // encrypted_mean_deposit_sum
        byte[] encryptedMeanDepositSum = new byte[size];
        dataInputStream.readFully(encryptedMeanDepositSum);
        userStatisticsDTO.setMeanDepositSum(encryptedMeanDepositSum);

        // encrypted_mean_reported_income
        byte[] encryptedMeanReportedIncome = new byte[size];
        dataInputStream.readFully(encryptedMeanReportedIncome);
        userStatisticsDTO.setMeanReportedIncome(encryptedMeanReportedIncome);

        // encrypted_no_months_deposited
        byte[] encryptedNoMonthsDeposited = new byte[size];
        dataInputStream.readFully(encryptedNoMonthsDeposited);
        userStatisticsDTO.setNoMonthsDeposited(encryptedNoMonthsDeposited);

        return userStatisticsDTO;
    }

    public static void sendIntThroughSocket(OutputStream out, int value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        byte[] bytes = buffer.array();
        out.write(bytes);
    }
}
