package test.mqx;

public class MQ {

    static final int DEFAULT_PORT_USED_BY_SOURCES = 6211 ;
    static final int DEFAULT_PORT_USED_BY_SINK = 6212 ;
    static final int DEFAULT_MAX_MEMORY_QUEUE_SIZE = 64 * 1000000 ;	// 64MB
    static final int DEFAULT_DISK_FILE_SIZE_DIVISOR = 4 ;

    static final String DEFAULT_DIRECTORY_NAME = "messageStore" ;

    static final int APPROX_PER_MESSAGE_MEMORY_OVERHEAD = 200 ; 	// 약간의 자바 객체 오버헤드..

    static final int MINIMUM_RECORDS_PER_FILE = 100 ;	// 마지막 메시지가 가져가진 않았을 때 현재 파일을 닫거나 삭제하지 않는다. 작은 숫자는 시작 시 재생 시간을 줄이지만 오버헤드를 증가시킨다. 시스템 속성 -DminimumRecordsPerFile로 재정의할 수 있다.

    public static boolean DEBUG = true ; 							// 추가 시스템.err 로깅
    /** 메시지 소스는 이 포트에 연결합니다. 시스템 속성 -DmessageQueueSourcePort로 재정의할 수 있습니다. **/
    public int portUsedBySources ;

    /** 메시지 싱크는 이 포트에 연결합니다. 시스템 속성 -DmessageQueueSinkPort로 재정의할 수 있습니다. **/
    public int portUsedBySink ;

    /** 메모리 내 메시지 큐의 대략적인 최대 크기(바이트). 시스템 속성 -DmaxMemoryQueueSize로 재정의할 수 있습니다. **/
    public int maxMemoryQueueSize ;

    /** 메모리 큐가 고갈되더라도 이 수의 레코드가 파일에 기록되기 전까지 영속 파일을 닫지 않습니다 **/
    public int minimumRecordsPerFile ;

    /** maxMemoryQueueSize를 diskFileSizeDivisor로 나눈 대략적인 최대 디스크 파일 크기; 예를 들어, 4는 디스크 파일의 크기를 1/4로 만듭니다.
     시스템 속성 -DdiskFileSizeDivisor로 재정의할 수 있습니다.
     참고: 메모리 내 메시지 큐 크기가 (파일의 메시지 크기로 인한) 파일 메시지 크기 아래로 감소되면 파일이 디스크에 지속되고 할당된 메모리에 맞지 않으므로 잘못된 동작이 발생할 수 있습니다.
     **/
}
