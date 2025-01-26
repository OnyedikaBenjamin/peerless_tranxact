# Peerless Transaction Processing System

This project implements a financial transaction processing system with functionalities for scheduling, canceling, and processing fund transfers. It is built using Java with Spring Boot and follows best practices for clean code and modular design.

## Features
- Schedule fund transfers with specific dates.
- Cancel pending transfers.
- Automatically process scheduled transfers using a scheduled task.
- Comprehensive test cases for all functionalities.

---

## Setup and Run Instructions

### Prerequisites
1. Ensure you have Java 11 or higher installed.
2. Install MySQL and set up a database.
3. Clone the repository and navigate to the project directory.

```bash
$ git clone <repository-url>
$ cd peerless-transaction
```

### Environment Configuration
1. Create an `.env` file in the root directory and provide the following details:

```env
DB_URL=jdbc:mysql://<your-database-host>:3306/<database-name>
DB_USERNAME=<your-database-username>
DB_PASSWORD=<your-database-password>
```

2. Replace `<your-database-host>`, `<database-name>`, `<your-database-username>`, and `<your-database-password>` with your MySQL database credentials.

### Build and Run

1. Build the application:

```bash
$ ./mvnw clean install
```

2. Run the application:

```bash
$ java -jar target/peerless-transaction-0.0.1-SNAPSHOT.jar
```

---

## Database Schema

### FundTransfer Table

| Column             | Type          | Description                           |
|--------------------|---------------|---------------------------------------|
| transfer_id        | BIGINT        | Primary key for the transfer.         |
| sender_account_id  | BIGINT        | Account ID of the sender.             |
| recipient_account_id | BIGINT      | Account ID of the recipient.          |
| transfer_amount    | DECIMAL(15,2) | Amount to be transferred.             |
| scheduled_date     | DATETIME      | Date and time the transfer is scheduled. |
| status             | VARCHAR(20)   | Status of the transfer (e.g., PENDING, COMPLETED, FAILED). |
| updated_at         | DATETIME      | Last updated timestamp.               |

---

## Example API Requests and Responses

### Schedule a Transfer
**Request:**

```http
POST /api/v1/transfers
Content-Type: application/json

{
  "senderAccountId": "1234567890",
  "recipientAccountId": "0987654321",
  "transferAmount": "5000.00",
  "transferDate": "2025-01-30T10:00:00"
}
```

**Response:**

```json
{
  "transferId": 1,
  "status": "PENDING"
}
```

### Cancel a Transfer
**Request:**

```http
PUT /api/v1/transfers/1/cancel
```

**Response:**

```json
{
  "transferId": 1,
  "status": "CANCELED"
}
```

---

## Approach

1. **Clean Code Principles:**
   - Each service and repository is modular and follows the Single Responsibility Principle.

2. **Error Handling:**
   - Custom exceptions ensure meaningful error messages for API users.

3. **Database Transactions:**
   - All critical operations are wrapped in transactions to maintain data consistency.

4. **Testing:**
   - Unit tests cover all service methods to ensure reliability and maintainability.

5. **Scheduled Task:**
   - The `TransferProcessor` class automatically processes scheduled transfers every 10 seconds using Spring's `@Scheduled` annotation.

---

## Contributing
Feel free to fork this repository and submit pull requests for enhancements or bug fixes. Ensure that all new features are accompanied by test cases.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.
