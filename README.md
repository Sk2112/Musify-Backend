# 🎶 Musify Backend

Musify Backend is a Spring Boot application that powers the Musify music streaming platform.  
It handles user requests, manages songs and playlists, and connects to a database for persistent storage.  

---

## 🚀 Features
- RESTful APIs built with **Spring Boot**
- Database integration with **MySQL**
- Supports **Audio Uplaod and Audio Streming** for music data
- Configurable environment using `application.properties`
- Follows industry-standard backend practices
- Integrated Gemini Api to generate Short Description and Facts About the song via title 
---

## ⚙️ Setup Instructions
### 1. Clone the Repository
```bash
git clone https://github.com/Sk2112/Musify-Backend.git
cd Musify-Backend
```

### 2.Database Configuration in Appication Properties File

application.properties file is not included in GitHub (for security reasons).
Instead, you will find an application-example.properties file inside src/main/resources/.
You have to change the file name to application.properties 

### 3.Fill in your database details

``` bash
spring.datasource.url=jdbc:postgresql://<NEON_HOST>:5432/<DB_NAME>
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

gemini.api.key=<GEMINI API KEY>
``` 
---

## ▶️ Run the Application

### 1.Run with Maven:
```bash
 mvn spring-boot:run
```
---
## 📡 API Endpoints
| Method | Endpoint          | Description       |
| ------ | ----------------- | ----------------- |
| GET    | `/api/songs`      | Fetch all songs   |
| POST   | `/api/songs`      | Add a new song    |

---
## 🤝 Contribution

- Fork the repo
- Create a new branch (feature/new-feature)
- Commit changes
- Push and create a PR
---
## Front End
Link to Frontend 
- ``` bash
  http://localhhost:3000
  ```
---
## 📜 License
This project is licensed under the MIT License.

## 👨‍💻 Author
Sumit Kumar
Backend Developer | Java | Spring Boot | React JS | Docker 
