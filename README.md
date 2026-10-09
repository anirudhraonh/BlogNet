# BlogNet - Full Stack Blog Application

A comprehensive blog platform built with Spring Boot, featuring user authentication, post management, and commenting system.

## Overview

BlogNet is a full-stack blogging application designed to showcase modern web development practices. It provides a complete blogging ecosystem where users can create accounts, write posts, and engage with other users through comments.

## Features

### User Management
- **User Registration**: Create new user accounts with validation
- **User Login**: Secure authentication using Spring Security
- **Role-Based Access Control**: Support for different user roles (ROLE_USER, etc.)
- **User Profiles**: Track user information and created posts

### Post Management
- **Create Posts**: Authenticated users can create new blog posts
- **View Posts**: Browse all posts or view individual posts
- **Delete Posts**: Authors can delete their own posts
- **Pagination**: Paginated post listing with customizable page size and sorting
- **Post Metadata**: Automatic timestamp tracking for post creation

### Comments System
- **Add Comments**: Users can comment on posts
- **View Comments**: Fetch all comments for a specific post
- **Comment Threading**: Comments linked to parent posts

### Security Features
- **Spring Security Integration**: Secure authentication and authorization
- **Password Encryption**: Secure password handling
- **CSRF Protection**: Built-in CSRF token support via Thymeleaf
- **Input Validation**: Data validation using Jakarta Validation (formerly javax.validation)

## Technology Stack

### Backend
- **Java 17**: Modern Java runtime
- **Spring Boot 4.0.2**: Application framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database abstraction layer
- **Hibernate**: ORM framework

### Database
- **MySQL**: Relational database for persistent storage
- **JPA/Hibernate**: Object-relational mapping

### Frontend
- **Thymeleaf**: Server-side template engine
- **HTML/CSS**: User interface

### Tools & Libraries
- **Maven**: Build and dependency management
- **Lombok**: Reduce boilerplate code with annotations
- **Jakarta Validation**: Input validation framework

## Project Structure

```
BlogNet/
├── src/
│   ├── main/
│   │   ├── java/com/example/BlogNet/
│   │   │   ├── controller/
│   │   │   │   ├── UserController.java      # Authentication endpoints
│   │   │   │   ├── PostController.java      # Post CRUD operations
│   │   │   │   └── CommentController.java   # Comment management
│   │   │   ├── service/
│   │   │   │   ├── UserService.java         # User business logic
│   │   │   │   ├── PostService.java         # Post business logic
│   │   │   │   ├── CommentService.java      # Comment business logic
│   │   │   │   └── MyUserDetailsService.java # Custom user details
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java      # User data access
│   │   │   │   ├── PostRepository.java      # Post data access
│   │   │   │   └── CommentRepository.java   # Comment data access
│   │   │   ├── entity/
│   │   │   │   ├── User.java                # User model
│   │   │   │   ├── Post.java                # Post model
│   │   │   │   └── Comment.java             # Comment model
│   │   │   ├── dto/
│   │   │   │   ├── UserRequestDTO.java      # User registration/login DTOs
│   │   │   │   ├── UserResponseDTO.java
│   │   │   │   ├── PostRequestDTO.java      # Post request/response DTOs
│   │   │   │   ├── PostResponseDTO.java
│   │   │   │   └── LoginRequestDTO.java
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java      # Centralized error handling
│   │   │   │   ├── ResourceNotFoundExcecption.java
│   │   │   │   ├── UnauthorizedException.java
│   │   │   │   └── DuplicateUserException.java
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java      # Spring Security configuration
│   │   │   └── BlogNetApplication.java      # Application entry point
│   │   └── resources/
│   │       ├── templates/
│   │       │   └── Homepage.html
│   │       └── application.properties
│   └── test/
│       └── java/com/example/BlogNet/
│           └── BlogNetApplicationTests.java
├── pom.xml                                   # Maven configuration
└── README.md                                 # This file
```

## API Endpoints

### Authentication
- **POST** `/api/auth/register` - Register a new user
- **POST** `/api/auth/login` - Login user

### Posts
- **GET** `/api/posts` - Get all posts
- **GET** `/api/posts/{id}` - Get specific post
- **POST** `/api/posts` - Create new post (authenticated)
- **DELETE** `/api/posts/{id}` - Delete post (authenticated)
- **GET** `/api/posts/paginated` - Get paginated posts with sorting

Query Parameters for `/api/posts/paginated`:
- `page` (default: 0) - Page number
- `size` (default: 10) - Number of items per page
- `sortBy` (default: createdAt) - Field to sort by

### Comments
- **POST** `/api/posts/{post_id}/comments` - Add comment to post
- **GET** `/api/posts/{post_id}/comments` - Get comments for post

## Database Schema

### Users Table
- `id` (Long, PK, Auto-increment)
- `username` (String, Unique, Not Null)
- `password` (String, Not Null)
- `email` (String, Unique, Not Null)
- `role` (String, Default: ROLE_USER)
- `created_at` (DateTime)

### Posts Table
- `id` (Long, PK, Auto-increment)
- `title` (String)
- `body` (String)
- `created_at` (DateTime)
- `user_id` (Long, FK)

### Comments Table
- `id` (Long, PK, Auto-increment)
- `content` (String)
- `created_at` (DateTime)
- `post_id` (Long, FK)
- `user_id` (Long, FK)

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6.0 or higher
- MySQL 8.0 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/anirudhraonh/BlogNet.git
   cd BlogNet
   ```

2. **Configure Database**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/blognet
   spring.datasource.username=your_mysql_username
   spring.datasource.password=your_mysql_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=false
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`

## Usage Examples

### Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "email": "john@example.com",
    "password": "securepassword123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john_doe",
    "password": "securepassword123"
  }'
```

### Create a Post
```bash
curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{
    "title": "My First Blog Post",
    "body": "This is the content of my first blog post."
  }'
```

### Get All Posts
```bash
curl http://localhost:8080/api/posts
```

### Get Paginated Posts
```bash
curl "http://localhost:8080/api/posts/paginated?page=0&size=5&sortBy=createdAt"
```

## Error Handling

The application includes comprehensive error handling with custom exceptions:

- **ResourceNotFoundException**: When a requested resource (post, user, comment) is not found
- **UnauthorizedException**: When unauthorized access is attempted
- **DuplicateUserException**: When registering with duplicate username or email

All errors are handled globally and return appropriate HTTP status codes.

## Contributing

Contributions are welcome! Please follow these guidelines:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Future Enhancements

- [ ] Email verification for user registration
- [ ] User profile pages
- [ ] Post editing capability
- [ ] Like/upvote system for posts and comments
- [ ] Search functionality
- [ ] User follow system
- [ ] Real-time notifications
- [ ] API documentation with Swagger/OpenAPI

## License

This project is open source and available for educational and commercial use.

## Author

Developed by Anirudh Rao - Full Stack Practice Project

## Support

For issues, questions, or suggestions, please open an issue on the GitHub repository.

---

**Last Updated**: October 2026
