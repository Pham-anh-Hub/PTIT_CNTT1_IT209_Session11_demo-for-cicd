# Sử dụng Eclipse Temurin JRE 17 nền Alpine/Jammy gọn nhẹ và bảo mật cho Spring Boot
FROM eclipse-temurin:17-jre-jammy

# Thiết lập thư mục làm việc bên trong container
WORKDIR /app

# Sao chép tệp JAR đã được đóng gói từ bước Gradle bootJar
COPY build/libs/*.jar app.jar

# Mở cổng 8080 cho ứng dụng Spring Boot
EXPOSE 8080

# Khởi chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]


