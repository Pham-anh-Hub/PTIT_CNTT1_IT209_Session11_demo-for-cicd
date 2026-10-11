# Demo CI/CD - Product Management Service

Dự án Spring Boot cung cấp RESTful API CRUD cho thực thể **Product** (Sản phẩm), kết nối cơ sở dữ liệu **MySQL** và hỗ trợ chạy in-memory test **H2** phục vụ quy trình CI/CD.

---

## 1. Cấu hình Cơ sở dữ liệu

Ứng dụng hỗ trợ cấu hình linh hoạt qua biến môi trường (Environment Variables) hoặc giá trị mặc định trong `application.properties`:

| Biến môi trường | Giá trị mặc định | Mô tả |
| :--- | :--- | :--- |
| `DB_URL` | `jdbc:mysql://localhost:3306/product_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true` | Chuỗi kết nối MySQL JDBC |
| `DB_USERNAME` | `root` | Tên đăng nhập MySQL |
| `DB_PASSWORD` | `root` | Mật khẩu MySQL |
| `SERVER_PORT` | `8080` | Cổng HTTP Server |

---

## 2. Danh sách API Endpoints (`/api/products`)

| Method | Endpoint | Mô tả | Body / Params | Mã phản hồi |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/products` | Lấy danh sách sản phẩm | `keyword` *(tùy chọn)* | `200 OK` |
| `GET` | `/api/products/{id}` | Lấy chi tiết sản phẩm theo ID | - | `200 OK` / `404 Not Found` |
| `POST` | `/api/products` | Thêm mới sản phẩm | `ProductRequest` (JSON) | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/products/{id}` | Cập nhật thông tin sản phẩm | `ProductRequest` (JSON) | `200 OK` / `404 Not Found` |
| `DELETE`| `/api/products/{id}` | Xóa sản phẩm theo ID | - | `200 OK` / `404 Not Found` |

### Ví dụ Payload Request (`POST` / `PUT`):
```json
{
  "name": "Laptop Dell XPS 15",
  "description": "Intel Core i7, 16GB RAM, 512GB SSD",
  "price": 28500000.00,
  "quantity": 15,
  "active": true
}
```

### Ví dụ Response chuẩn (`ApiResponse<T>`):
```json
{
  "success": true,
  "message": "Tạo mới sản phẩm thành công",
  "data": {
    "id": 1,
    "name": "Laptop Dell XPS 15",
    "description": "Intel Core i7, 16GB RAM, 512GB SSD",
    "price": 28500000.00,
    "quantity": 15,
    "active": true,
    "createdAt": "2026-10-11T19:15:00",
    "updatedAt": "2026-10-11T19:15:00"
  },
  "timestamp": "2026-10-11T19:15:00.123"
}
```

---

## 3. Kiểm thử (Testing)

Dự án được cấu hình sẵn H2 Database in-memory trong `src/test/resources/application.properties` để chạy độc lập các bài test mà không cần khởi động MySQL:

```bash
# Chạy toàn bộ Unit & Integration tests
./gradlew test

# Hoặc trên Windows
.\gradlew.bat test
```
Có thể kiểm thử nhanh các API trực tiếp bằng file `products-api-tests.http`.
