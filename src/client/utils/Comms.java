package assemblyline.client.utils;

import java.util.ArrayList;
import java.util.List;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;

import assemblyline.utils.ErrorMessages;
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
            // This is ridiculous
            // Because Java is so old, I had to create a list of buffers
            // and then convert it to one buffer
            // why are you doing this to me, java?
            List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();
            buffer = ByteBuffer.allocate(2048);
            while(socketChannel.read(buffer) > -1) {
                if (buffer.remaining() == 0) {
                    buffers.add(buffer);
                    buffer = ByteBuffer.allocate(2048);
                }
            }
            if (buffer.position() > 0) {
                buffers.add(buffer);
            }

            socketChannel.close();

            List<Byte> listOfBytes = new ArrayList<>();
            for (int i = 0; i < buffers.size(); i++) {
                for (int j = 0; j < buffers.get(i).capacity(); j++) {
                    Byte current = buffers.get(i).get(j);
                    if (current == 0) {
                        break;
                    }
                    listOfBytes.add(current);
                }
            }
            bytes = new byte[listOfBytes.size()];
            for (int i = 0; i < listOfBytes.size(); i++) {
                bytes[i] = listOfBytes.get(i);
            }
            // React
            String rawResponse = new String(bytes, StandardCharsets.UTF_8);
            JSONObject response;
            try {
                response = new JSONObject(rawResponse);
                return response;
            } catch(Exception e) {
                IO.print("%s%n", rawResponse);
            }
            return null;
        } catch (Exception e) {
            IO.print(ErrorMessages.TEMPLATE, e.getMessage());
        }
        return null;
    }
}