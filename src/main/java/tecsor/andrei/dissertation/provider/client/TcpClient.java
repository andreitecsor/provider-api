package tecsor.andrei.dissertation.provider.client;

import tecsor.andrei.dissertation.provider.model.FHEOperationType;
import tecsor.andrei.dissertation.provider.model.UserStatistics;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
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
            DataInputStream in = new DataInputStream(socket.getInputStream());
            byte[] receivedBytes = new byte[1024];
            int count = in.read(receivedBytes); // read bytes from the input stream
            String response = new String(receivedBytes, 0, count); // convert bytes to string
            System.out.println("Response from server: " + response);

            //retrieve the encrypted values one by one
            // TODO: 20.05.2023 retrieve encrypted data and server key    

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendIntThroughSocket(OutputStream out, int value) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        byte[] bytes = buffer.array();
        out.write(bytes);
    }
}
