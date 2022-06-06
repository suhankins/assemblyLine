package assemblyline.client.utils;

import java.util.ArrayList;
import java.util.List;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;

import assemblyline.utils.IO;

import org.json.JSONObject;
public class Comms {
    public static SocketChannel socketChannel;
    public static InetSocketAddress serverAddress;

    public static JSONObject sendAndReceive(
            String output) {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(serverAddress);

            String text = output.toString() + "\n";
            byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }

            // Response
            // This is ridicilous
            // I couldn't easily convert bytebuffer to string, so I did this hell
            buffer = ByteBuffer.allocate(2048);
            int bytesRead = socketChannel.read(buffer);

            socketChannel.close();

            List<Byte> noNulls = new ArrayList<>();
            for (int i = 0; i < buffer.capacity(); i++) {
                if (buffer.get(i) == 0) {
                    break;
                }
                noNulls.add(buffer.get(i));
            }
            bytes = new byte[noNulls.size()];
            for (int i = 0; i < noNulls.size(); i++) {
                bytes[i] = noNulls.get(i);
            }
            // React
            String rawResponse = new String(bytes, "UTF-8");
            JSONObject response;
            try {
                response = new JSONObject(rawResponse);
                return response;
            } catch(Exception e) {
                IO.print("%s%n", rawResponse);
            }
            return null;
        } catch (Exception e) {
            IO.print(e.getMessage());
        }
        return null;
    }
}