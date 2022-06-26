# YU-MAKET-2.3.1

완성된 부분 

- 이미지 내장 저장소 로드 기능 완료
- 고객센터로의 intent 및 카테고리 출력, 고객센터의 1대 1 문의 버튼 생성
- 알림 설정 기능 -> 알림 설정을 하면 현재 시간 및 설정 내용을 메시지로 출력
- 고객센터에 Mock 데이터를 주입
- MyInfoFragment 부분 버튼에서 -> TextView로 변경
- MyInfoFragment에서 CSCenter를 거쳐서 -> CSListFragment로 넘어감
- CSCenter 고객센터 및 불량식품 신고 ACTION_CALL
- 코드리뷰에서 나온 Entity와 모델을 통해서 모델에 표현
- CSCenter에서 문의 전화를 바로 전화로 연결하기
- 이메일 작성 fragment 생성
- gmail 및 데이터 연동

+0.0.1    
런타임 권한 확인 및 요청(01.25)

+0.1.0    
환경설정 Activity와 Fragment 생성 
버전코드 생성 (01.26)

+0.0.1
이용약관 Activity 생성 및 이용약관 생성
메소드 명 변경 (01.27)

2.2.0
고객센터 자주 문의하는 글 디테일 화면 생성 및 데이터 전달
(01.28)

+0.0.1
디테일 xml 글자 사이즈 변경 및 디테일 화면에서 뒤로가기 버튼 클릭 시 CSCenter화면으로 (01.29)

+0.0.1
자주 문의글에서 1대 1 문의 버튼 클릭시 Email 화면으로 supportFragment.replace 적용
*불필요한 데이터 정리 (01.30)

+0.0.1
뒤로가기 버튼 생성 및 코드 변경
(02.03)

2.3.0
보일러플레이트 코드 제거 및 개인정보처리 Activity/Fragment 생성
현재 문제점으로는 각 fragment마다 뒤로가기 버튼을 사용하는데 이때 MainActivity로 가게된다면 fragment.replace를 하게된다면
MainActivity에 3개의 fragment가 존재하고 있어 하나를 불러오면 나머지 2개가 보이지않는 현상이 있음 (02.04)

2.3.1
잘못사용하고 있던 코드들의 변경 및 수정전 데이터 보관을 위한 업데이트 (02.05)
"# -" 
"# -" 
