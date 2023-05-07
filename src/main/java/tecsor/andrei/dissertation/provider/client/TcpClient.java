package tecsor.andrei.dissertation.provider.client;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TcpClient {
    //For the moment, this is just for testing (later, i will integrate it with the api)
    public static void main(String[] args) throws IOException {
        String hostname = "localhost"; // server hostname
        int port = 8070; // server port number

        try {
            // create a socket connection to the server
            Socket socket = new Socket(hostname, port);

            // send an int value to the server
            int value = 123;
            ByteBuffer buffer = ByteBuffer.allocate(4);
            //Java uses network byte order (big-endian) to represent multibyte values such as integers
            //Rust uses native byte order (little-endian)
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(value);
            byte[] bytes = buffer.array();
            OutputStream out = socket.getOutputStream();
            out.write(bytes);

            // receive a response from the server
            DataInputStream in = new DataInputStream(socket.getInputStream());
            byte[] receivedBytes = new byte[1024];
            int count = in.read(receivedBytes); // read bytes from the input stream
            String response = new String(receivedBytes, 0, count); // convert bytes to string
            System.out.println("Response from server: " + response);

            // close the socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
