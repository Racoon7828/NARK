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

처음 만들 때 동작에만 집중했던 코드를 아래 기준으로 개선했습니다.

### Before / After 비교

#### 1. 네트워크 처리 — StrictMode 우회 → 비동기 처리

```java
// Before: 메인 스레드에서 직접 HTTP 요청, ANR 위험
StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
StrictMode.setThreadPolicy(policy);
// ... 메인 스레드에서 URL.openStream() 호출
```

```java
// After: ExecutorService로 백그라운드 처리, 완료 후 Handler로 UI 업데이트
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
// Before: 소스코드에 API 키 노출
URL url = new URL("https://open.assembly.go.kr/...?KEY=8081b2b1c98c4e6d83be3454f1dfd506");
```

```java
// After: local.properties → BuildConfig → 코드에서 참조 (키가 커밋에 포함되지 않음)
URL url = new URL("https://open.assembly.go.kr/...?KEY=" + BuildConfig.ASSEMBLY_API_KEY);
```

```groovy
// build.gradle: local.properties에서 읽어 BuildConfig에 주입
def localProps = new Properties()
localPropsFile.withInputStream { localProps.load(it) }
buildConfigField "String", "ASSEMBLY_API_KEY", localProps['ASSEMBLY_API_KEY'] ?: '""'
```

---

#### 3. 데이터 관리 — static List 남용 → 인스턴스 변수

```java
// Before: 11개의 static List, Activity 재생성 시 데이터 오염 방지를 위해 onCreate마다 수동 clear()
static List<String> listBillName = new ArrayList<>();
static List<Integer> listBillNo  = new ArrayList<>();
// ... 9개 더
// onCreate에서 11번 .clear() 호출
```

```java
// After: 파싱 결과를 로컬 List로 수집 후 어댑터에 전달, static 완전 제거
List<Bill_Data> results = new ArrayList<>();
// ... 파싱 완료 후
mainHandler.post(() -> {
    for (Bill_Data data : results) bill_Adapter.addItem(data);
    bill_Adapter.notifyDataSetChanged();
});
```

---

#### 4. 성능 — 루프 안 정렬 제거

```java
// Before: 아이템 추가할 때마다 전체 정렬 → O(n²log n)
for (int i = 0; i < list.size(); i++) {
    userList.add(...);
    Collections.sort(userList, comparator); // 매 iteration마다 정렬
}
```

```java
// After: 수집 완료 후 한 번만 정렬 → O(n log n)
// ... 파싱 루프 (정렬 없음)
Collections.sort(results, (o1, o2) -> o2.getbNum() - o1.getbNum());
```

---

#### 5. 에러 처리 — 무시 → 사용자 알림

```java
// Before: 에러가 발생해도 빈 화면만 보임
} catch (Exception e) {
    System.out.println("에러");
}
```

```java
// After: 사용자에게 Toast로 알림
} catch (Exception e) {
    mainHandler.post(() ->
        Toast.makeText(this, "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
    );
}
```

---

#### 6. 코드 중복 제거

```java
// Before: 생성자로 이미 값을 넘겼음에도 setter를 13번 중복 호출
Bill_Data data = new Bill_Data(name, num, date, prp, prep, ra, dae, crd, ...);
data.setbName(name);
data.setbNum(num);
// ... 11개 더
```

```java
// After: 생성자 한 줄로 끝
Bill_Data data = new Bill_Data(name, num, date, prp, prep, ra, dae, crd, ...);
```

---

## 브랜치 구조

| 브랜치 | 설명 |
|--------|------|
| `main` | 리팩토링 완료 버전 |
| `original` | 최초 작성 버전 (원본 보존) |
