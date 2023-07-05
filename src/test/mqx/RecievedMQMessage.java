package test.mqx;

import java.math.BigInteger;

public class RecievedMQMessage {

    private final byte[] id; // 메시지 ID
    private final byte[] contents; // 메시지 내용
    private final boolean possiblyReplayed; // 메시지가 재전송된 것인지 여부

    public RecievedMQMessage(byte[] id, byte[] contents, boolean possiblyReplayed) {
        this.id = id;
        this.contents = contents;
        this.possiblyReplayed = possiblyReplayed;
    }

    // 메시지 ID를 16진수 문자열로 변환하여 반환
    public String getIdAsString() {
        return new BigInteger(1, id).toString(16);
    }

    // 메시지 내용을 문자열로 변환하여 반환
    public String getContentsAsString() {
        return new String(contents);
    }

    public byte[] getId() {
        return id;
    }

    public byte[] getContents() {
        return contents;
    }

    public boolean isPossiblyReplayed() {
        return possiblyReplayed;
    }

}
