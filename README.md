# NARK - 국회 정보 조회 앱

국회 공공 API를 활용한 Android 앱입니다.  
법안 현황, 의원 정보, 예산 정책, 채용 공고를 검색하고 조회할 수 있습니다.

> 이 프로젝트는 처음 Android를 공부하며 만든 앱을 리팩토링한 결과물입니다.  
> 원본 코드는 [`original`](https://github.com/Racoon7828/NARK/tree/original) 브랜치에 보존되어 있습니다.

---

## 기능

| 메뉴 | 설명 |
|------|------|
| 법안 조회 | 국회에 제출된 법안 목록 검색 및 상세 조회 |
| 의원 정보 | 현역 국회의원 검색, 사진/소속 정당/약력/연락처 조회 |
| 예산 정책 | 예산 관련 정책 자료 목록 조회 |
| 채용 공고 | 국회 및 산하기관 채용 공고 조회 |

---

## 사용 기술

- **언어**: Java
- **UI**: RecyclerView, Toolbar, SearchView
- **이미지 로딩**: Glide
- **네트워크**: XmlPullParser (국회 열린국회정보 API, 공공데이터포털 API)
- **비동기 처리**: ExecutorService + Handler

---

## 시작하기

### 1. API 키 발급

| API | 발급처 |
|-----|--------|
| 국회 열린국회정보 API | https://open.assembly.go.kr |
| 공공데이터포털 국회의원 사진 API | https://www.data.go.kr |

### 2. local.properties 설정

프로젝트 루트의 `local.properties`에 발급받은 키를 입력합니다.  
이 파일은 `.gitignore`에 포함되어 있어 커밋되지 않습니다.

```properties
sdk.dir=YOUR_SDK_PATH

ASSEMBLY_API_KEY="YOUR_KEY"
MP_API_KEY="YOUR_KEY"
EMPLOY_API_KEY="YOUR_KEY"
GOVDATA_API_KEY="YOUR_KEY"
```

### 3. 빌드 및 실행

Android Studio에서 프로젝트를 열고 Run 합니다.  
`minSdk 24` / `targetSdk 32`

---

## 리팩토링 기록

2021년 처음 만들 때 동작에만 집중했던 코드를 아래 기준으로 개선했습니다.

### 주요 변경사항

#### 1. 네트워크 처리 — StrictMode 우회 → 비동기 처리

```java
// 이전: 네트워크 요청을 메인 스레드에서 직접 처리해서 앱이 멈추는 문제가 있었음
StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
StrictMode.setThreadPolicy(policy);
// ... 메인 스레드에서 URL.openStream() 호출
```

```java
// 이후: 백그라운드에서 데이터를 받아오고, 완료되면 메인 스레드에서 화면 갱신
executor.execute(() -> {
    List<Bill_Data> results = fetchFromApi(pageIndex, searchText);
    mainHandler.post(() -> {
        bill_Adapter.addAll(results);
        bill_Adapter.notifyDataSetChanged();
    });
});
```

---

#### 2. API 키 관리 — 하드코딩 → BuildConfig 분리

```java
// 이전: API 키가 코드에 그대로 박혀 있으므로 GitHub에 올리면 바로 노출됨
URL url = new URL("https://open.assembly.go.kr/...?KEY=8081b2b1c98c4e6d83be3454f1dfd506");
```

```java
// 이후: local.properties에 키 저장 => BuildConfig 참조 (키가 커밋에 포함되지 않도록 수정)
URL url = new URL("https://open.assembly.go.kr/...?KEY=" + BuildConfig.ASSEMBLY_API_KEY);
```

```groovy
// build.gradle에서 local.properties를 읽어 BuildConfig에 주입
def localProps = new Properties()
localPropsFile.withInputStream { localProps.load(it) }
buildConfigField "String", "ASSEMBLY_API_KEY", localProps['ASSEMBLY_API_KEY'] ?: '""'
```

---

#### 3. 데이터 관리 — static List 남용 → 인스턴스 변수

```java
// 이전: static List를 11개나 선언했고, 화면을 다시 열 때 데이터가 중복되는 걸
//       막으려고 onCreate마다 .clear()를 11번 직접 호출해야 했음
static List<String> listBillName = new ArrayList<>();
static List<Integer> listBillNo  = new ArrayList<>();
// ...
```

```java
// 이후: 파싱할 때마다 로컬 List를 새로 만들어서 쓰고 어댑터에 넘겨줌, static 완전 제거
List<Bill_Data> results = new ArrayList<>();
// ... 파싱 완료 후
mainHandler.post(() -> {
    for (Bill_Data data : results) bill_Adapter.addItem(data);
    bill_Adapter.notifyDataSetChanged();
});
```

---

#### 4. 성능 — 루프 안에서 매번 정렬 → 다 모은 뒤 한 번만 정렬

```java
// 이전: 아이템을 하나 추가할 때마다 전체를 다시 정렬해서 불필요한 연산이 반복됐음
for (int i = 0; i < list.size(); i++) {
    userList.add(...);
    Collections.sort(userList, comparator);
}
```

```java
// 이후: 다 불러온 다음에 한 번만 정렬
Collections.sort(results, (o1, o2) -> o2.getbNum() - o1.getbNum());
```

---

#### 5. 에러 처리 — 무시 → 사용자에게 알림

```java
// 이전: 에러가 나도 그냥 넘어가서 왜 화면이 비어있는지 알 수 없었음
 catch (Exception e) {
    System.out.println("에러");
}
```

```java
// 이후: 실패하면 Toast로 사용자에게 알려줌
 catch (Exception e) {
    mainHandler.post(() ->
        Toast.makeText(this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
    );
}
```

---

#### 6. 코드 중복 제거

```java
// 이전: 생성자에 이미 값을 다 넘겼는데 아래에서 setter로 똑같은 값을 또 넣고 있었음
Bill_Data data = new Bill_Data(name, num, date, prp, prep, ra, dae, crd, ...);
data.setbName(name);
data.setbNum(num);
// ... 11개 더
```

```java
// 이후: 생성자 한 줄
Bill_Data data = new Bill_Data(name, num, date, prp, prep, ra, dae, crd, ...);
```

---

## 브랜치 구조

| 브랜치 | 설명 |
|--------|------|
| `main` | 리팩토링한 버전 |
| `original` | 최초 작성 버전 (원본 보존) |
