# Library Management System

The Library Management System is a comprehensive application that allows users to manage a library's book inventory, handle user checkouts, and generate reports. This README provides all necessary information to install, set up, and use the application.

## Rules and Permissions

### Admin Role
- Only users with the **Admin** role can manage (Create, Read, Update, Delete) books in the library. This includes:
    - Adding new books to the inventory.
    - Updating book details.
    - Deleting books from the inventory.
- Admins can also perform all other functions available to regular users, such as checking out books and generating reports.

### Regular Users
- Regular users can perform the following actions:
    - View the list of books available in the library.
    - Checkout books from the library.
    - Return books they have checked out.
    - Generate reports on book inventory and checkouts.

### Book Inventory Rules
- When updating the number of copies of a book, the new number of copies cannot be less than the number of copies currently checked out and awaiting return. This ensures that the inventory accurately reflects the books available and prevents over-checkout.

### Checkout Rules
- Users can check out a book more than once.
- Users can only check out books if the total number of checked out books does not exceed the available copies in the inventory.
- The checkout process and the return process are atomic to ensure that a book is not checked out if it is not available.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [Endpoints](#endpoints)
- [Testing](#testing)

## Features

- User Authentication (Login and Logout)
- Book Management (CRUD Operations)
- User Checkouts and Returns
- Reporting (Books, Pending Returns, Returned Books)

## Technologies Used

- **Java 22**
- **Spring Boot 3.2.5**
- **Spring Security**
- **Spring Data JPA**
- **Hibernate**
- **H2 Database (for testing)**
- **PostgreSQL (for production)**
- **ModelMapper 2.4.4**
- **JUnit 5**
- **Mockito**
- **Jackson**
- **JSON Web Tokens (JWT)**
- **JavaMailSender**
- **Lombok**
- **JavaFaker**
- **Maven 3.9.6 or later**


## Installation

1.  **Clone the repository:**

    ```
    git https://github.com/ibukunoreofe/library-management-java-spring-boot.git
    cd library-management-java-spring-boot
    ```

2.  **Build the project:**

    ```
    mvn clean install
    ```

3.  **Set up the database:**

    Ensure you have a running PostgreSQL database. Create a database named `library_management`.

    Update the `application.properties` file with your database configuration:

    ```
    spring.datasource.url=jdbc:postgresql://localhost:5432/library_management
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    
    # JPA and Hibernate configuration
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    
    # Admin user configuration
    admin.email=admin@example.com
    admin.password=password
    
    # Mail server configuration
    spring.mail.host=smtp.mailtrap.io
    spring.mail.port=587
    spring.mail.username=your_mailtrap_username
    spring.mail.password=your_mailtrap_password
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    ```

4.  **Run the application:**

    ```
    mvn spring-boot:run
    ```

5.  **Access the application:**

    The application will be available at `http://localhost:8080`.

6. Optionally set JVM

   SET JVM HOME to the path installed
   ```shell
   $ export JAVA_HOME="~\.jdks\openjdk-22.0.1"
   ```

7. Generate JWT Token
   https://dev.to/tkirwa/generate-a-random-jwt-secret-key-39j4
   
   ```shell
   $ node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"
   ```

## Testing

To run the tests for this application, use the following Maven command:

```
mvn test
```

The following are the main test classes and their purposes:

*   **CreateAndEnsureAdminLoginTest:**

    Ensures that an admin user is created and can log in successfully. Stores the admin token for use in other tests.

*   **BookControllerTest:**

    Tests all CRUD operations for the `/api/books` endpoints, including pagination and search functionality.

*   **CheckoutControllerTest:**

    Tests the checkout and return functionality for the `/api/checkouts` endpoints.

*   **ReportControllerTest:**

    Tests the report generation endpoints for checked-out and returned books in the `/api/reports/books` endpoints.

*   **PasswordResetControllerTest:**

    Tests the password reset functionality for the `/api/auth/password/reset-request` and `/api/auth/password/reset` endpoints.

*   **LogoutControllerTest:**

    Tests the logout functionality for the `/api/auth/logout` endpoint.


To run a specific test class, use the following Maven command:

```
mvn -Dtest=ClassNameTest test
```

For example, to run the `BookControllerTest`:

```
mvn -Dtest=BookControllerTest test
```

Ensure that the required environment variables or system properties are set for the tests to run successfully. The tests may require access to a running database or other services, depending on the application configuration.


## Usage

The application provides a set of RESTful endpoints for managing the library system. Below is a list of the available endpoints:

### Authentication

### Authentication and Authorization

*   **Login:**

    ```
    POST /api/auth/login
    ```

    Parameters:

    ```
    {
      "email": "admin@example.com",
      "password": "password"
    }
    ```

    Response:

    ```
    {
      "token": "your_jwt_token"
    }
    ```

*   **Register:**

    ```
    POST /api/auth/register
    ```

    Parameters:

    ```
    {
      "name": "User Name",
      "email": "user@example.com",
      "password": "password"
    }
    ```

    Response:

    ```
    {
      "message": "User registered successfully"
    }
    ```

*   **Request Password Reset:**

    ```
    POST /api/auth/password/reset-request
    ```

    Parameters:

    ```
    {
      "email": "user@example.com"
    }
    ```

    Response:

    ```
    {
      "message": "Password reset code sent to your email."
    }
    ```

*   **Reset Password:**

    ```
    POST /api/auth/password/reset
    ```

    Parameters:

    ```
    {
      "email": "user@example.com",
      "password": "newpassword",
      "password_confirmation": "newpassword",
      "token": "reset_token"
    }
    ```

    Response:

    ```
    {
      "message": "Your password has been reset."
    }
    ```

*   **Logout:**

    ```
    POST /api/auth/logout
    ```

    Parameters:

    Response:

    ```
    {
      "message": "User 'username' has been logged out successfully."
    }
    ```

### Books

*   **Get All Books:**

    ```
    GET /api/books
    ```

    Parameters:

   *   `paginate` (boolean, optional): If true, returns paginated results.
   *   `page` (integer, optional): Page number for pagination.
   *   `size` (integer, optional): Page size for pagination.
   *   `search` (string, optional): Search query for book titles or authors.

    Response:

    ```
    [
      {
        "id": 1,
        "title": "Book Title",
        "author": "Author Name",
        "isbn": "1234567890",
        "publishedAt": "2023-01-01",
        "copies": 10
      },
      ...
    ]
    ```

*   **Get Book by ID:**

    ```
    GET /api/books/{id}
    ```

    Response:

    ```
    {
      "id": 1,
      "title": "Book Title",
      "author": "Author Name",
      "isbn": "1234567890",
      "publishedAt": "2023-01-01",
      "copies": 10
    }
    ```

*   **Create Book:**

    ```
    POST /api/books
    ```

    Request Body:

    ```
    {
      "title": "New Book",
      "author": "Author Name",
      "isbn": "1234567890",
      "publishedAt": "2023-01-01",
      "copies": 10
    }
    ```

    Response:

    ```
    {
      "id": 1,
      "title": "New Book",
      "author": "Author Name",
      "isbn": "1234567890",
      "publishedAt": "2023-01-01",
      "copies": 10
    }
    ```

*   **Update Book:**

    ```
    PUT /api/books/{id}
    ```

    Request Body:

    ```
    {
      "title": "Updated Book",
      "author": "Author Name",
      "isbn": "1234567890",
      "publishedAt": "2023-01-01",
      "copies": 10
    }
    ```

    Response:

    ```
    {
      "id": 1,
      "title": "Updated Book",
      "author": "Author Name",
      "isbn": "1234567890",
      "publishedAt": "2023-01-01",
      "copies": 10
    }
    ```

*   **Delete Book:**

    ```
    DELETE /api/books/{id}
    ```

    Response:

    ```
    {
      "message": "Book deleted successfully."
    }
    ```


### Checkouts

*   **Checkout Book:**

    ```
    POST /api/checkouts
    ```

    Request Body:

    ```
    {
      "bookId": 1
    }
    ```

    Response:

    ```
    {
      "checkoutId": 1,
      "userId": 1,
      "bookId": 1,
      "checkoutPersonName": "Admin User",
      "checkoutPersonEmailAddress": "admin@example.com",
      "bookTitle": "Book Title",
      "bookAuthor": "Author Name",
      "bookIsbn": "1234567890",
      "checkoutDateTimeUtc": "2023-01-01T00:00:00Z",
      "returnDateTimeUtc": null,
      "createdAt": "2023-01-01T00:00:00Z",
      "updatedAt": "2023-01-01T00:00:00Z"
    }
    ```

*   **Return Book:**

    ```
    PATCH /api/checkouts/{checkoutId}
    ```

    Response:

    ```
    {
      "checkoutId": 1,
      "userId": 1,
      "bookId": 1,
      "checkoutPersonName": "Admin User",
      "checkoutPersonEmailAddress": "admin@example.com",
      "bookTitle": "Book Title",
      "bookAuthor": "Author Name",
      "bookIsbn": "1234567890",
      "checkoutDateTimeUtc": "2023-01-01T00:00:00Z",
      "returnDateTimeUtc": "2023-01-02T00:00:00Z",
      "createdAt": "2023-01-01T00:00:00Z",
      "updatedAt": "2023-01-02T00:00:00Z"
    }
    ```

### Reports

*   **Get All Books Report:**

    ```
    GET /api/reports/books
    ```

    Parameters:

    *   `paginate` (boolean, optional) - If true, returns paginated results.
    *   `page` (integer, optional) - Page number for pagination.
    *   `size` (integer, optional) - Number of items per page for pagination.
    *   `search` (string, optional) - Search query for filtering results.

    Response:

    ```
    {
      "books": [
        {
          "id": 1,
          "title": "Book Title",
          "author": "Author Name",
          "isbn": "1234567890",
          "publishedAt": "2023-01-01",
          "copies": 10,
          "checkedOutCount": 2,
          "quantityLeft": 8,
          "timesCheckedOutAndReturned": 1
        }
      ],
      "totalPages": 1,
      "totalItems": 1
    }
    ```

*   **Get Books Pending Return:**

    ```
    GET /api/reports/books/pending-return
    ```

    Parameters:

    *   `paginate` (boolean, optional) - If true, returns paginated results.
    *   `page` (integer, optional) - Page number for pagination.
    *   `size` (integer, optional) - Number of items per page for pagination.
    *   `search` (string, optional) - Search query for filtering results.

    Response:

    ```
    {
      "books": [
        {
          "id": 1,
          "title": "Book Title",
          "author": "Author Name",
          "isbn": "1234567890",
          "publishedAt": "2023-01-01",
          "copies": 10,
          "checkedOutCount": 2,
          "quantityLeft": 8,
          "timesCheckedOutAndReturned": 1
        }
      ],
      "totalPages": 1,
      "totalItems": 1
    }
    ```

*   **Get Returned Books:**

    ```
    GET /api/reports/books/returned
    ```

    Parameters:

    *   `paginate` (boolean, optional) - If true, returns paginated results.
    *   `page` (integer, optional) - Page number for pagination.
    *   `size` (integer, optional) - Number of items per page for pagination.
    *   `search` (string, optional) - Search query for filtering results.

    Response:

    ```
    {
      "books": [
        {
          "id": 1,
          "title": "Book Title",
          "author": "Author Name",
          "isbn": "1234567890",
          "publishedAt": "2023-01-01",
          "copies": 10,
          "checkedOutCount": 2,
          "quantityLeft": 8,
          "timesCheckedOutAndReturned": 1
        }
      ],
      "totalPages": 1,
      "totalItems": 1
    }
    ```

## License

This project is licensed under the MIT License. You are free to use, modify, and distribute this software, provided that you include the original license and give credit to the original author.

## Author

This project was created and is maintained by [Ibukun Bello](https://github.com/ibukunoreofe).