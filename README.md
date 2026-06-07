# Kotobase - Nền tảng học tiếng Nhật JLPT

## Giới thiệu

Kotobase là nền tảng hỗ trợ học tiếng Nhật dành cho người học JLPT từ N5 đến N1. Hệ thống cung cấp các tính năng học từ vựng, Kanji, ngữ pháp, luyện đề thi, theo dõi tiến độ học tập và nâng cấp tài khoản Premium.

Dự án được phát triển theo mô hình Client - Server với Frontend ReactJS và Backend Spring Boot.


## Tính năng chính

### Hệ thống học tập

* Học từ vựng theo cấp độ JLPT
* Học Kanji theo cấp độ JLPT
* Học ngữ pháp và ví dụ minh họa
* Lưu từ vựng, Kanji yêu thích để ôn tập
* Progressive Learning Roadmap (học theo lộ trình vượt ải)
* Spaced Repetition hỗ trợ ghi nhớ hiệu quả

### Luyện tập

* Sinh câu hỏi trắc nghiệm tự động
* Bài tập từ vựng
* Bài tập Kanji
* Bài tập ngữ pháp
* Chấm điểm tự động phía Server

### Thi thử JLPT

* Thi thử đầy đủ các phần của đề JLPT
* Tự động lưu đáp án trong quá trình làm bài
* Hệ thống đếm giờ theo từng phần thi
* Tự động nộp bài khi hết thời gian
* Chấm điểm bất đồng bộ (Asynchronous Scoring)
* Realtime thông báo kết quả bằng WebSocket

### Tài khoản Premium

* Nâng cấp tài khoản Premium
* Mở khóa toàn bộ đề thi thử
* Truy cập nội dung học nâng cao
* Quản lý gói dịch vụ và thời hạn sử dụng

### Thống kê

* Theo dõi tiến độ học tập
* Thống kê số lượng từ vựng/Kanji đã học
* Theo dõi lịch sử làm bài
* Quản lý streak học tập

### Quản trị hệ thống

* Quản lý người dùng
* Quản lý đề thi
* Quản lý câu hỏi và đáp án
* Quản lý nội dung học tập
* Quản lý gói Premium


## Công nghệ sử dụng

### Backend

* Java 17
* Spring Boot
* Spring Security
* Spring Data JPA
* JWT Authentication
* OAuth2 Google Login
* WebSocket (STOMP)
* MySQL
* Maven

### Frontend

* ReactJS
* JavaScript
* React Router
* Axios
* TailwindCSS

### Công cụ

* Git & GitHub
* Postman
* IntelliJ IDEA
* VS Code
* MySQL Workbench


## Kiến trúc hệ thống

Frontend (ReactJS)

↓

REST API / WebSocket

↓

Backend (Spring Boot)

↓

MySQL Database


## Các chức năng nổi bật đã triển khai

### Hệ thống thi thử realtime

* Tự động lưu đáp án theo thời gian thực
* Bộ đếm giờ riêng cho từng phần thi
* Tự động chuyển phần thi khi hết giờ
* Chấm điểm nền bằng hàng đợi xử lý
* WebSocket thông báo kết quả ngay sau khi chấm xong

### Tối ưu hiệu năng

* Loại bỏ N+1 Query
* Tối ưu truy vấn JPA
* Xử lý bất đồng bộ bằng ExecutorService
* Giảm tải Database khi chấm điểm số lượng lớn

### Bảo mật

* JWT Authentication
* OAuth2 Login với Google
* Spring Security
* Role-based Authorization

## Thành viên thực hiện
* Đỗ Đình Phúc (Backend Developer)
## Hướng phát triển
* Thanh toán trực tuyến cho Premium
* Mobile App Android
* Hệ thống gợi ý nội dung học cá nhân hóa
