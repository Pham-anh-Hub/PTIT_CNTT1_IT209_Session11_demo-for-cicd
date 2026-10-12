# Bài tập Session 12 - Bài 4 (Ex4)
## Tự động triển khai ứng dụng Spring Boot (Container) lên VPS từ xa qua SSH

### 1. Mục tiêu bài tập
- Tự động hóa quá trình đóng gói và đẩy Docker Image lên GHCR (`ghcr.io`).
- Sử dụng Action `appleboy/ssh-action@v1.0.3` kết nối SSH từ xa vào máy chủ VPS để kéo Image mới và khởi chạy lại Container.

---

### 2. Sản phẩm nộp bài
1. Tệp kịch bản: [deploy-vps.yml](file:///d:/DevOps_Fundamental/demo-for-cicd/.github/workflows/deploy-vps.yml) (được đặt tại `.github/workflows/deploy-vps.yml` và sao lưu tại `homework/session_12/ex4/deploy-vps.yml`).
2. Ảnh chụp màn hình:
   - Log thực thi chi tiết của bước **Thực thi câu lệnh triển khai trên VPS qua SSH** (Job `deploy-vps`) trên tab Actions đạt trạng thái tích xanh ✅.
   - (Tùy chọn bổ sung): Ảnh chụp trình duyệt truy cập `http://<VPS_IP>:8080/api/products` trả về kết quả JSON HTTP 200 OK.
   - Đặt tên ảnh chụp: `homework/session_12/ex4/vps-deploy-log.png`.

---

### 3. Hướng dẫn Thiết lập Repository Secrets trên GitHub

Để máy ảo GitHub Runner kết nối được vào VPS của bạn, bạn cần lưu trữ thông tin đăng nhập vào **GitHub Repository Secrets**:

1. Mở GitHub Repository trên trình duyệt.
2. Bấm vào tab **Settings** -> Ở cột bên trái, bấm vào **Secrets and variables** -> Chọn **Actions**.
3. Bấm nút **New repository secret** để thêm lần lượt 3 biến:

| Tên Secret | Ý nghĩa | Giá trị mẫu |
| :--- | :--- | :--- |
| **`SSH_HOST`** | Địa chỉ IP công khai của máy chủ VPS | `103.179.189.50` |
| **`SSH_USER`** | Tên người dùng SSH trên VPS | `root` hoặc `ubuntu` |
| **`SSH_KEY`** | Toàn bộ nội dung Khóa riêng tư (Private Key) dùng để kết nối VPS | Bắt đầu bằng `-----BEGIN OPENSSH PRIVATE KEY-----` và kết thúc bằng `-----END OPENSSH PRIVATE KEY-----` |

> **Mẹo tạo khóa SSH trên VPS (nếu chưa có):**
> 1. Trên VPS, gõ: `ssh-keygen -t ed25519 -C "github-actions"` (bấm Enter qua các câu hỏi).
> 2. Thêm khóa công khai vào danh sách cho phép: `cat ~/.ssh/id_ed25519.pub >> ~/.ssh/authorized_keys && chmod 600 ~/.ssh/authorized_keys`.
> 3. Đọc nội dung khóa riêng tư để copy dán vào Secret `SSH_KEY`: `cat ~/.ssh/id_ed25519`.

---

### 4. Hướng dẫn Đẩy Code & Kiểm tra Nghiệm thu

#### Bước 1: Commit và đẩy mã nguồn lên nhánh `main`
```bash
git add .
git commit -m "feat(cd): add remote vps deployment workflow via ssh"
git push origin main
```

#### Bước 2: Theo dõi quá trình triển khai trên GitHub Actions
1. Mở tab **Actions** trên GitHub repo.
2. Bấm vào workflow **Deploy Spring Boot Container to Remote VPS**.
3. Quan sát:
   - Job `build-image` đóng gói và đẩy image `:latest` lên GHCR ✅.
   - Job `deploy-vps` tự động kích hoạt ngay sau đó (`needs: build-image`), kết nối SSH vào VPS và chạy các lệnh:
     - `docker login ghcr.io`
     - `docker pull ...`
     - `docker stop app && docker rm app`
     - `docker run -d --name app -p 8080:8080 ...`
4. Bấm vào Job `deploy-vps`, mở rộng bước **Thực thi câu lệnh triển khai trên VPS qua SSH** để xem toàn bộ log output.
5. **Chụp ảnh màn hình log này** và lưu vào:
   `homework/session_12/ex4/vps-deploy-log.png`.

#### Bước 3: Kiểm tra trực tiếp trên trình duyệt
Mở trình duyệt web và truy cập địa chỉ:
```
http://<Địa_chỉ_IP_VPS>:8080/api/products
```
Xác nhận ứng dụng Spring Boot phản hồi danh sách JSON dữ liệu thành công (HTTP 200 OK)!
