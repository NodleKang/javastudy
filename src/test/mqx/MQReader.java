package test.mqx;

import java.io.*;
import java.net.*;
import java.nio.* ;

public class MQReader {

    private final Socket server;
    private final BufferedInputStream bis;
    private final BufferedOutputStream bos;
    private final ByteBuffer lenBuffer;
    private int count = 0;

    public MQReader(String serverAddr, int serverSinkPort) throws IOException {
        server = new Socket(serverAddr, serverSinkPort);
        bis = new BufferedInputStream(server.getInputStream());
        bos = new BufferedOutputStream(server.getOutputStream());
        lenBuffer = ByteBuffer.allocate(4);
    }

    public RecievedMQMessage read() throws IOException {
        byte b = (byte) bis.read();
        if (b < 0) throw new IOException("Unexpected eof received from message queue");

        boolean possiblyReplayed;
        if (b == 'M') possiblyReplayed = false;
        else if (b == 'R') possiblyReplayed = true;
        else throw new IOException("Unexpected message type from message queue: " + b);

        byte[] id = readBytes(16);
        int len = readLen();
        byte[] contents = readBytes(len);

        bos.write('Y');
        bos.flush();
        count++;
        return new RecievedMQMessage(id, contents, possiblyReplayed);
    }

    public void close() throws IOException {
        bis.close();
        bos.close();
        server.close();
    }

    public String toString() {
        String serverAddress;
        if (server != null) {
            serverAddress = "Connected to message queue " + server.getRemoteSocketAddress().toString();
        } else {
            serverAddress = "Not connected";
        }
        return serverAddress;
    }

    private int readLen() throws IOException {
        for (int i=0;i<4;i++) {
            int j = bis.read();
            if (j < 0) throw new IOException("Eof reading message length");
            lenBuffer.put(i, (byte) j);
        }
        return lenBuffer.getInt(0);
    }

    private byte[] readBytes(int sz) throws IOException {
        final byte buf[] = new byte[sz];
        int start = 0;
        int len = sz;
        while (len > 0) {
            int i = bis.read(buf, start, len);
            if (i < 0) throw new IOException("Eof reading message");
            start += i;
            len -= i;
        }
        return buf;
    }

}
