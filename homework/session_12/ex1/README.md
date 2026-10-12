# Bài tập Session 12 - Bài 1 (Ex1)
## Quy trình CI đa phiên bản JDK (Matrix Strategy) cho Java Spring Boot Gradle

### 1. Mục tiêu bài tập
- Cấu hình ma trận kiểm thử tự động (Matrix Strategy) chạy song song bài test trên nhiều phiên bản JDK (JDK 17 và JDK 21).
- Tối ưu hóa tốc độ thực thi ma trận với Gradle Dependency Caching (`cache: 'gradle'`).

### 2. Sản phẩm nộp bài
1. Tệp kịch bản: [ci-matrix.yml](file:///d:/DevOps_Fundamental/demo-for-cicd/.github/workflows/ci-matrix.yml) (được đặt tại `.github/workflows/ci-matrix.yml` và sao lưu tại `homework/session_12/ex1/ci-matrix.yml`).
2. Ảnh chụp màn hình tab GitHub Actions hiển thị:
   - Workflow `Java Spring Boot CI Matrix Test` được kích hoạt khi push vào nhánh `main`.
   - Job `test-matrix` tự động chia nhánh thành 2 sub-jobs song song:
     - `Test on Java 17` (hoặc `test-matrix (17)`) - Tích xanh ✅
     - `Test on Java 21` (hoặc `test-matrix (21)`) - Tích xanh ✅
   - Lưu ảnh màn hình vào thư mục này: `homework/session_12/ex1/matrix-test-result.png`.

### 3. Cấu hình kịch bản GitHub Actions
```yaml
name: Java Spring Boot CI Matrix Test

on:
  push:
    branches: [ "main" ]

jobs:
  test-matrix:
    name: Test on Java ${{ matrix.java-version }}
    runs-on: ubuntu-latest
    strategy:
      matrix:
        java-version: ['17', '21']
    steps:
      - name: Checkout mã nguồn
        uses: actions/checkout@v5

      - name: Thiết lập Java JDK ${{ matrix.java-version }}
        uses: actions/setup-java@v5
        with:
          java-version: ${{ matrix.java-version }}
          distribution: 'temurin'
          cache: 'gradle'

      - name: Cấp quyền thực thi cho Gradle Wrapper
        run: chmod +x gradlew

      - name: Thực thi JUnit Test với Gradle
        run: ./gradlew test
```

### 4. Hướng dẫn các lệnh Git đẩy bài
```bash
git add .github/workflows/ci-matrix.yml homework/session_12/ex1/
git commit -m "ci: add matrix test workflow for JDK 17 and 21"
git push origin main
```
