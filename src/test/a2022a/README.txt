<< Service Proxy 개발 >>

[기능 요약]
- 'Client’는 콘솔 입/출력 또는 HTTP 요청/응답 방식으로 'Service Proxy’를 통해 Service를 호출한다.
- Service Proxy는 Routing Rule에 따라 Service 또는 타 Service Proxy를 호출한다. ('Service 호출’, 'Proxy 호출’)
- 'Service Proxy’는 Service 또는 타 Service Proxy를 호출한 이력을 관리하여 Service간 호출 관계정보를 제공한다. ('Service 추적’)
- Service에서 비 정상적인 응답 상황이 감지되면 'Service 호출’을 일정 기간동안 차단한다. ('회로 차단')

=============================================================
[Q 1]
=============================================================
- Proxy 호출
  : 콘솔을 통해 Proxy Name을 입력 받음
  : Proxy Name에 해당하는 'Routing Rule 파일’을 읽고, 파일에서 Service 파일의 경로를 식별
  : 식별된 Service 파일의 내용을 출

- 준비된 파일들
Proxy-1.txt 파일 내용
Service-A.txt

Proxy-2.txt 파일 내용
Service-B.txt

Proxy-3.txt 파일 내용
Service-C.txt

Service-A.txt 파일 내용
Service Init.

Service-B.txt 파일 내용
Auth Check.

Service-C.txt 파일 내용
Notice Announced.

- 콘솔 입출력
C:\>SP_TEST<엔터키>  구현한 프로그램 실행 (Argument 없음)
Proxy-2<엔터키>  콘솔 입력 (Proxy Name은 'Proxy-2’)
Auth Check.  콘솔 출력 ('Service’ 파일 내용)

=============================================================
[Q 2]
=============================================================
- Proxy 호출 변경 ('※ 콘솔 입/출력’ 형식정보 참조)
  : 콘솔을 통해 'Proxy Name’과 'Path’를 입력 받는다.
  : Proxy Name에 해당하는 'Routing Rule 파일’에서 'Path’에 해당되는 파일명을 식별
  : 식별된 파일명이 'Proxy-’로 시작되는 경우 해당 'Routing Rule 파일’을 다시 읽고,
    'Path’에 해당되는 파일명이 'Service-’로 시작되는 'Service 파일’의 경로를 식별
    (한번의 Proxy 호출에 Routing Rule파일을 읽는 횟수는 최대 2회만 발생함)
  : 최종적으로 식별된 'Service 파일’의 내용을 출력

Proxy-1.txt 파일 내용
/front#Service-A.txt
/auth#Proxy-2.txt
/notice#Proxy-3.txt

Proxy-2.txt 파일 내용
/front#Proxy-1.txt
/auth#Service-B.txt
/notice#Proxy-3.txt

Proxy-3.txt 파일 내용
/front#Proxy-1.txt
/auth#Proxy-2.txt
/notice#Service-C.txt

Service-A.txt 파일 내용
Service Init.

Service-B.txt 파일 내용
Auth Check.

Service-C.txt 파일 내용
Notice Announced.

- 콘솔 입출력
C:\>SP_TEST<엔터키>  구현한 프로그램 실행 (Argument 없음)
Proxy-2 /notice<엔터키>  콘솔 입력 (Proxy Name은 'Proxy-2’, Path는 '/notice’)
Notice Announced.  콘솔 출력 ('Service’ 파일 내용)

=============================================================
[Q 3]
=============================================================
※ Proxy 구현
  : 지정된 Routing Rule에 따라 HTTP 요청을 전달하고 수신된 응답을 반환하는 Service Proxy 추가 구현
    ('※ HTTP 기반 Service Proxy’ 참조)
  : 'Routing Rule 파일’ 형식이 변경됨 ('※ Routing Rule 파일’ 참조)

※ 용어 기준
h t t p : / / 1 2 7 . 0 . 0 . 1 : 5 0 0 2 / a u t h / c o m p a n y ? i d = a p p l e & k e y = D F G E
----------------- URL --------------------|---------- Path ---------|---------- Query String ----------

※ HTTP 기반 Service Proxy
  : Service Proxy는 기동 시 첫번째 Argument로 경로가 입력되는 ‘Routing Rule 파일’에 정의된 설정을 읽어서 기동 (‘※ Routing Rule 파일 형식정보’ 참조)
  : Service Proxy는 요청된 URI의 Path에 따라 ‘Routing Rule 파일’에 정의된 Route 정보를 선택하여 해당 Service 또는 타 Service Proxy를 호출
  : Service Proxy는 Service 또는 타 Proxy 호출 시 요청된 HTTP Method, Path, Query를 그대로 전달하고,
    수신된 응답의 Status, Content-Type Header, Body를 변경없이 응답으로 전달
    (HTTP Method는 GET, POST 만 사용됨)
  : 제공프로그램인 Service는 필요시 타 Service를 직접 호출하지 않고, Service Proxy를 통해서만 호출 함

※ Routing Rule 파일 형식정보
  - 파일명 : <Proxy Name>.json (각 소문항 아래)
  - Service Proxy의 HTTP Port (“port”)와 Service Proxy로 요청된 URI의 Path가 “pathPrefix”로 시작될 때
    요청을 전달해야 할 대상의 URL (“url”)이 설정되어 있음
  - Routing Rule 파일은 JSON 포맷으로 예시는 아래와 같음
    {
        "port": 5001, --> Service Proxy의 HTTP Port
        "routes": [
            {
                "pathPrefix": "/front",        --> Service Proxy로 요청된 URI의 Path가 “/front”로 시작될 때
                "url": "http://127.0.0.1:8081" --> http://127.0.0.1:8081로 요청을 전달
            },
            {
                "pathPrefix": "/auth",
                "url": "http://127.0.0.1:5002"
            }
        ]
    }

※ 제공 파일
Proxy-1.json 파일 내용
{
    "port": 5001,
    "routes": [
        {
            "pathPrefix": "/front",
            "url": "http://127.0.0.1:8081"
        },
        {
            "pathPrefix": "/auth",
            "url": "http://127.0.0.1:5002"
        }
    ]
}

Proxy-2.json 파일 내용
{
    "port": 5002,
    "routes": [
        {
            "pathPrefix": "/auth",
            "url": "http://127.0.0.1:8082"
        }
    ]
}

=============================================================
[Q 4]
=============================================================
※ Proxy 구현
  : Service 추적 API 추가 구현
  : 제공프로그램에 Header 관리가 추가됨

※ 제공프로그램 Header 관리 (구현 대상 아님)
- 제공프로그램인 Client는 매 ‘Proxy 호출’ 시 고유의 ‘요청ID’를 생성하여 ‘x-requestId’ HTTP Header값으로 설정하고 Proxy를 호출함
- 제공프로그램인 Service는 ‘Proxy 호출’ 시 수신된 HTTP 요청 Header 중 ‘x-’로 시작하는 Header 값을 그대로 유지하여 Proxy를 호출함

※ Service 추적 API
- Service Proxy에 ‘요청ID’별로 Proxy 및 Service 호출관계인 Service 추적정보를 응답하는 API를 추가
- 제공프로그램 Header 관리를 이용하여 다양한 방식으로 구현 가능
  (예 : 호출 이력 로그 파일, Service Proxy간 호출 이력 수집 API, 호출이력 수집 Server 등)
- 요청 URI : GET <Service Proxy(URL)>/trace/<요청ID>
- 응답 Body : 호출순서에 따라 계층구조로 표현된 JSON 형식 (‘※ Service 추적 API 응답 형식’ 참조)
  • 최초 Client에서 실행한 Service Proxy호출을 포함한 모든 Service Proxy 및 Service 호출에 대한 요청 주소, 응답 Status, 하위호출 정보를 출력
- Service Proxy를 2번 이상 실행 해도 정상적으로 동작하도록 구현

※ Routing Rule 파일 형식정보
Proxy-1.json 파일 내용
{
    "port": 5001,
    "routes": [
        {
            "pathPrefix": "/front",
            "url": "http://127.0.0.1:8081"
        },
        {
            "pathPrefix": "/auth",
            "url": "http://127.0.0.1:5002"
        },
        {
            "pathPrefix": "/notice",
            "url": "http://127.0.0.1:5003"
        }
    ]
}

Proxy-2.json 파일 내용
{
    "port": 5002,
    "routes": [
        {
            "pathPrefix": "/auth",
            "url": "http://127.0.0.1:8082"
        }
    ]
}

Proxy-3.json 파일 내용
{
    "port": 5003,
    "routes": [
        {
            "pathPrefix": "/notice",
            "url": "http://127.0.0.1:8083"
        }
    ]
}

※ Service 추적 API 응답 형식
- 응답 Status : 200
- 응답 Body : JSON 포맷으로
  “target” 속성에 호출된 <Service 또는 Service Proxy URL> + <첫번째 Path>,
  “status” 속성에 호출 결과의 <응답 Status>,
  “services” 속성에 <하위 Service 또는 Service Proxy 호출 정보> 배열
- 응답 Body 예시
{
    "target": "http://127.0.0.1:5001/front", --> 최초 CLient에서 실행한 Proxy 호출 정보
    "status": "200",
    "services": [                            --> 하위 Service 또는 Service Proxy 호출 정보 배열
        {
            "target": "http://127.0.0.1:8081/front",
            "status": "200",
            "services": [ --> Service에서 Proxy 호출을 2회 실행
                {
                    "target": "http://127.0.0.1:5001/auth",
                    "status": "200",
                    "services": [
                        {
                            "target": "http://127.0.0.1:5002/auth",
                            "status": "200",
                            "services": [
                                {
                                    "target": "http://127.0.0.1:8082/auth",
                                    "status": "200"
                                }
                            ]
                        }
                    ]
                },
                {
                    "target": "http://127.0.0.1:5001/notice",
                    "status": "200",
                    "services": [
                        {
                            "target": "http://127.0.0.1:5003/notice",
                            "status": "200",
                            "services": [
                                {
                                    "target": "http://127.0.0.1:8083/notice",
                                    "status": "200"
                                }
                            ]
                        }
                    ]
                }
            ]
        }
    ]
}
