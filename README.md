```markdown
# Peerless Transaction Processing System

A robust financial transaction processing system built with Java Spring Boot, enabling scheduled fund transfers with comprehensive tracking and management capabilities.

## Features

- Schedule and manage fund transfers with precise timing
- Cancel pending transactions
- Automated transfer processing via scheduled tasks
- Extensive test coverage
- Transaction consistency and error handling
- RESTful API endpoints

## Prerequisites

- Java 11+
- MySQL

## Quick Start

```bash
git clone <repository-url>
cd peerless-transaction

# Configure environment
cp .env.example .env
# Edit .env with your database credentials

# Build and run
./mvnw clean install
java -jar target/peerless-transaction-0.0.1-SNAPSHOT.jar
```

## Database Schema

```sql
CREATE TABLE fund_transfer (
    transfer_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    sender_account_id BIGINT NOT NULL,
    recipient_account_id BIGINT NOT NULL,
    transfer_amount DECIMAL(15,2) NOT NULL,
    scheduled_date DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_scheduled_date (scheduled_date),
    INDEX idx_status (status),
    CONSTRAINT chk_amount CHECK (transfer_amount > 0),
    CONSTRAINT chk_status CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED', 'CANCELED'))
);
```

## API Reference

### Schedule Transfer

```http
POST /api/v1/transfers
Content-Type: application/json

{
  "senderAccountId": "1234567890",
  "recipientAccountId": "0987654321",
  "transferAmount": "5000.00",
  "transferDate": "2025-01-30T10:00:00"
}

Response: {
  "transferId": 1,
  "status": "PENDING"
}
```

### Cancel Transfer

```http
PUT /api/v1/transfers/1/cancel

Response: {
  "transferId": 1,
  "status": "CANCELED"
}
```

## Architecture

- **Clean Code**: Modular services following SOLID principles
- **Error Handling**: Custom exceptions with meaningful messages
- **Database**: Transactional integrity for all critical operations
- **Testing**: Comprehensive unit test coverage
- **Scheduling**: Automated transfer processing every 10 seconds

## Contributing

1. Fork the repository
2. Create a feature branch
3. Submit a pull request with tests

## License

MIT License

```
