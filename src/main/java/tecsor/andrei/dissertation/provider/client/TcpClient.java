package tecsor.andrei.dissertation.provider.client;

import tecsor.andrei.dissertation.provider.model.FHEOperationType;

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

            //send the operation type
            ByteBuffer buffer = ByteBuffer.allocate(4);
            //Java uses network byte order (big-endian) to represent multibyte values such as integers
            //Rust uses native byte order (little-endian)
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(FHEOperationType.ENCRYPT.value);
            byte[] bytes = buffer.array();
            OutputStream out = socket.getOutputStream();
            out.write(bytes);

            //The actual values
            int[] values = new int[]{1, 2, 3, 4, 5};

            //send the number of values
            buffer = ByteBuffer.allocate(4);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(values.length);
            bytes = buffer.array();
            out.write(bytes);

            //send the values
            buffer = ByteBuffer.allocate(4 * values.length);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            for (int value : values) {
                buffer.putInt(value);
            }
            bytes = buffer.array();
            out.write(bytes);

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
}
