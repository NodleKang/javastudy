package test.mqx;

import java.io.*;
import java.net.*;
import java.nio.* ;

public class MQWriter {
    private Socket server;
    private final BufferedInputStream bis;
    private final BufferedOutputStream bos;
    private final ByteBuffer lenBuffer;
    private int count = 0;

    public MQWriter(String serverAddr, int serverPort) throws IOException {
        server = new Socket(serverAddr, serverPort);
        bis = new BufferedInputStream(server.getInputStream());
        bos = new BufferedOutputStream(server.getOutputStream());
        lenBuffer = ByteBuffer.allocate(4);
    }

    public void write(byte[] message) throws IOException {
        write(null, message);
    }

    public void write(byte[] id, byte[] message) throws IOException {
        if (message == null || message.length == 0) {
            throw new IllegalArgumentException("No message supplied");
        }

        if (id == null) {
            bos.write('M');
        } else if (id.length != 16) {
            throw new IllegalArgumentException("id must be null or 16 bytes");
        } else {
            bos.write('I');
            bos.write(id);
        }

        byte[] lenBytes = lenBuffer.putInt(0, message.length).array();
        bos.write(lenBytes);
        bos.write(message);
        bos.flush();

        if (bis.read() != 'Y') {
            throw new IOException("Message not acked");
        }
        count++;
    }

    public void close() throws IOException {
        bos.write('E');
        bos.flush();
        bis.close();
        bos.close();
        server.close();
        server = null;
    }

    @Override
    public String toString() {
        return ((server != null) ? ("Connected to " + server.getRemoteSocketAddress().toString()) : "Not connected") + ", sent count:" + count;
    }
}
