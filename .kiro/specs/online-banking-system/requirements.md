# Requirements Document

## Introduction

The Online Banking System is a Java desktop application that enables bank staff to manage customer accounts and transactions. The system must support account creation, deposits, withdrawals, balance enquiries, and transaction history viewing. The application will use Java Swing for the GUI and MongoDB for persistence, demonstrating core object-oriented programming principles including encapsulation, inheritance, and polymorphism.

This requirements document is organized into two phases: MVP (Minimum Viable Product) features that must be completed and functional for the 4-hour hackathon deadline, and Phase 2 stretch features to be implemented only if MVP is complete and demo-ready with time remaining.

## Glossary

- **System**: The Online Banking System application
- **Account**: A bank account entity with a unique account number, balance, and customer association
- **Customer**: A person who owns one or more accounts in the system
- **Transaction**: A record of a financial operation (deposit or withdrawal) on an account
- **Initial_Deposit**: The amount of money deposited when creating a new account
- **Minimum_Balance**: The lowest balance an account can have without triggering an overdraft
- **Account_Number**: A unique identifier assigned to each account
- **Transaction_History**: A chronological list of all transactions for a specific account
- **Account_DAO**: Data Access Object interface for account persistence operations
- **Transaction_DAO**: Data Access Object interface for transaction persistence operations
- **Bank_Service**: Service layer interface that implements business logic
- **GUI**: Graphical User Interface built with Java Swing
- **Savings_Account**: A type of account that can earn interest
- **Current_Account**: A type of account for regular transactions
- **Transfer**: Moving funds from one account to another account

## Requirements

---

### MVP Requirements (Phase 1 - Must Complete)

### Requirement 1: Create New Customer Account

**User Story:** As a bank staff member, I want to create a new customer account with initial deposit, so that customers can start banking with us.

#### Acceptance Criteria

1. WHEN the staff member provides customer details (name, phone, email, address) and an initial deposit amount greater than zero, THE System SHALL create a new Customer record in the database
2. WHEN a new Customer record is created, THE System SHALL generate a unique Account_Number
3. WHEN an Account_Number is generated, THE System SHALL create an Account record with the Initial_Deposit as the opening balance
4. WHEN an Account is created with an Initial_Deposit, THE System SHALL record a deposit Transaction with the Initial_Deposit amount
5. IF the Initial_Deposit is less than or equal to zero, THEN THE System SHALL reject the account creation and display an error message "Initial deposit must be greater than zero"
6. WHEN account creation succeeds, THE System SHALL display the generated Account_Number to the staff member
7. WHEN the complete account creation succeeds, THE System SHALL store customer details with all fields (name, phone, email, address) in the customers table
8. THE System SHALL store account details with fields (account_number, customer_id, account_type, balance, opening_date, status) in the accounts table

### Requirement 2: Deposit Money to Existing Account

**User Story:** As a bank staff member, I want to deposit money into an existing account, so that customers can add funds to their accounts.

#### Acceptance Criteria

1. WHEN the staff member provides a valid Account_Number and a deposit amount greater than zero, THE System SHALL increase the account balance by the deposit amount
2. WHEN the account balance is increased, THE System SHALL record a deposit Transaction with the amount and updated balance
3. WHEN a deposit Transaction is recorded, THE System SHALL store the transaction type as "DEPOSIT", the amount, the balance after transaction, and the transaction timestamp
4. IF the Account_Number does not exist in the database, THEN THE System SHALL display an error message "Account not found"
5. WHEN the staff member actively provides input with an invalid deposit amount less than or equal to zero, THE System SHALL reject the deposit and display an error message "Deposit amount must be greater than zero"
6. WHEN a deposit succeeds, THE System SHALL display a confirmation message with the Account_Number and new balance
7. THE System SHALL update the account balance in the accounts table within the same database transaction as recording the Transaction record

### Requirement 3: Withdraw Money from Existing Account

**User Story:** As a bank staff member, I want to withdraw money from an existing account, so that customers can access their funds.

#### Acceptance Criteria

1. WHEN the staff member provides a valid Account_Number and a withdrawal amount greater than zero, THE System SHALL check if the account has sufficient balance
2. WHEN the account has sufficient balance, THE System SHALL decrease the account balance by the withdrawal amount
3. WHEN the account balance is decreased, THE System SHALL record a withdrawal Transaction with the amount and updated balance
4. WHEN a withdrawal Transaction is recorded, THE System SHALL store the transaction type as "WITHDRAWAL", the amount, the balance after transaction, and the transaction timestamp
5. IF the withdrawal would cause the balance to fall below the Minimum_Balance, THEN THE System SHALL reject the withdrawal, display an error message "Insufficient balance for withdrawal", and record a transaction for the rejected withdrawal attempt
6. IF the Account_Number does not exist in the database, THEN THE System SHALL display an error message "Account not found"
7. WHEN the staff member actively provides input with an invalid withdrawal amount less than or equal to zero, THE System SHALL reject the withdrawal and display an error message "Withdrawal amount must be greater than zero"
8. WHEN a withdrawal succeeds, THE System SHALL display a confirmation message with the Account_Number and new balance
9. THE System SHALL update the account balance in the accounts table within the same database transaction as recording the Transaction record

### Requirement 4: Balance Enquiry by Account Number

**User Story:** As a bank staff member, I want to check the balance of an account by its account number, so that I can provide balance information to customers.

#### Acceptance Criteria

1. WHEN the staff member provides a valid Account_Number, THE System SHALL retrieve and display the current account balance
2. WHEN displaying the balance successfully, THE System SHALL also display the customer name and account type without displaying error messages
3. IF the Account_Number does not exist in the database, THEN THE System SHALL display an error message "Account not found"
4. THE System SHALL format all balance amounts with two decimal places
5. THE System SHALL retrieve balance information from the accounts table joined with the customers table

### Requirement 5: View Transaction History

**User Story:** As a bank staff member, I want to view all transactions for an account in chronological order, so that I can review account activity with customers.

#### Acceptance Criteria

1. WHEN the staff member provides a valid Account_Number, THE System SHALL validate account existence first
2. WHEN the account exists, THE System SHALL query and retrieve all Transaction records for that account
3. WHEN displaying Transaction_History, THE System SHALL order transactions by transaction timestamp with most recent first
4. WHEN displaying each Transaction, THE System SHALL show the transaction type, amount, balance after transaction, and timestamp
5. IF the Account_Number does not exist in the database, THEN THE System SHALL display the error message "Account not found" only
6. IF the Account_Number exists but has no transactions, THEN THE System SHALL display a message "No transactions found for this account"
7. THE System SHALL retrieve transaction records from the transactions table filtered by Account_Number

### Requirement 6: Object-Oriented Design Implementation

**User Story:** As a development team, we want to implement the system using proper OOP principles, so that the code is maintainable, modular, and demonstrates our understanding of OOP concepts.

#### Acceptance Criteria

1. THE System SHALL implement at least three distinct classes with clear single responsibilities
2. THE System SHALL use private fields for all model attributes with public getter and setter methods (encapsulation)
3. THE System SHALL implement an abstract Account base class with concrete Savings_Account and Current_Account subclasses (inheritance)
4. THE System SHALL define Account_DAO and Transaction_DAO as interfaces with concrete implementation classes (interface-based design)
5. THE System SHALL define Bank_Service as an interface with a concrete BankServiceImpl class (interface-based design)
6. THE System SHALL use polymorphism when working with Account subclasses through the abstract Account type
7. THE System SHALL organize code into packages: model, exception, dao, service, ui, and main
8. THE System SHALL implement custom checked exceptions: InsufficientBalanceException, InvalidAmountException, AccountNotFoundException

### Requirement 7: GUI Implementation with Java Swing

**User Story:** As a bank staff member, I want a graphical interface with separate screens for each operation, so that I can easily navigate and perform banking tasks.

#### Acceptance Criteria

1. THE System SHALL implement the GUI using Java Swing components
2. THE System SHALL provide separate panels or frames for each operation: Create Account, Deposit, Withdraw, Balance Enquiry, and Transaction History
3. THE System SHALL implement navigation between different screens using CardLayout or menu system
4. WHEN a user performs an operation, THE System SHALL display success or error messages in the GUI
5. THE System SHALL validate user input in the GUI before calling service layer methods
6. THE System SHALL display data in readable formats with proper labels and layouts
7. THE System SHALL implement a main frame (MainFrame) as the entry point to the GUI

### Requirement 8: Database Persistence with MongoDB Java Driver

**User Story:** As a development team, we want to persist all data to MongoDB using MongoDB Java Driver, so that data is preserved between application sessions.

#### Acceptance Criteria

1. THE System SHALL connect to MongoDB database using MongoDB Java Driver with a MongoDBConnection utility class
2. THE System SHALL implement all database operations through DAO interfaces (Account_DAO and Transaction_DAO)
3. WHEN performing account balance updates, THE System SHALL attempt to use MongoDB transactions to ensure atomicity and handle errors gracefully if no database transaction is active
4. THE System SHALL implement the following database schema: customers collection with fields (customer_id, name, phone, email, address)
5. THE System SHALL implement accounts collection with fields (account_number, customer_id, account_type, balance, opening_date, status)
6. THE System SHALL implement transactions collection with fields (transaction_id, account_number, type, amount, balance_after, txn_time)
7. THE System SHALL use parameterized queries to prevent NoSQL injection
8. WHEN an actual database error occurs, THE System SHALL handle MongoException and display appropriate error messages to the user

### Requirement 9: Exception Handling

**User Story:** As a development team, we want to handle business rule violations with custom exceptions, so that errors are clearly communicated and properly handled.

#### Acceptance Criteria

1. WHEN a withdrawal would overdraw an account, THE System SHALL throw InsufficientBalanceException
2. WHEN a deposit or withdrawal amount is less than or equal to zero, THE System SHALL throw InvalidAmountException
3. WHEN an Account_Number is not found in the database, THE System SHALL throw AccountNotFoundException
4. THE System SHALL define InsufficientBalanceException as a custom checked exception extending Exception
5. THE System SHALL define InvalidAmountException as a custom checked exception extending Exception
6. THE System SHALL define AccountNotFoundException as a custom checked exception extending Exception
7. WHEN an exception is thrown in the service layer, THE System SHALL catch it in the GUI layer and display the exception message to the user
8. WHEN the GUI layer fails to catch an exception, THE System SHALL use a global exception handler as a fallback to handle uncaught exceptions

### Requirement 10: Code Quality Standards

**User Story:** As a development team, we want to maintain high code quality standards, so that the code is readable, maintainable, and meets grading criteria.

#### Acceptance Criteria

1. THE System SHALL use meaningful names for all classes, methods, and variables following Java naming conventions
2. THE System SHALL include comments explaining the purpose of classes and complex methods
3. THE System SHALL use consistent indentation and formatting throughout all source files
4. THE System SHALL implement methods with single responsibilities (methods under 30 lines preferred)
5. THE System SHALL compile without errors using javac
6. THE System SHALL run without runtime errors when proper inputs are provided
7. THE System SHALL handle all checked exceptions appropriately with try-catch blocks

---

### Phase 2 Requirements (Stretch Features - Only if MVP Complete)

### Requirement 11: Transfer Funds Between Accounts

**User Story:** As a bank staff member, I want to transfer funds from one account to another, so that customers can move money between their accounts.

#### Acceptance Criteria

1. WHEN the staff member provides a valid source Account_Number, destination Account_Number, and transfer amount greater than zero, THE System SHALL check if the source account has sufficient balance
2. WHEN the source account has sufficient balance, THE System SHALL decrease the source account balance by the transfer amount
3. WHEN the source account balance is decreased, THE System SHALL increase the destination account balance by the transfer amount
4. WHEN both account balances are updated, THE System SHALL record a withdrawal Transaction for the source account
5. WHEN the withdrawal Transaction is recorded, THE System SHALL record a deposit Transaction for the destination account
6. IF the transfer would cause the source account balance to fall below the Minimum_Balance, THEN THE System SHALL reject the Transfer and display an error message "Insufficient balance for transfer"
7. IF either Account_Number does not exist, THEN THE System SHALL display an error message "Account not found"
8. IF the transfer amount is less than or equal to zero, THEN THE System SHALL reject the Transfer and display an error message "Transfer amount must be greater than zero"
9. THE System SHALL execute all Transfer operations (withdrawal from source, deposit to destination, transaction records) within a single database transaction
10. WHEN a Transfer succeeds, THE System SHALL display a confirmation message with both account numbers and their new balances

### Requirement 12: Interest Calculation for Savings Accounts

**User Story:** As a bank staff member, I want to calculate and apply interest to savings accounts, so that customers earn returns on their savings.

#### Acceptance Criteria

1. WHERE the account is a Savings_Account, THE System SHALL provide an interest calculation feature
2. WHEN interest calculation is triggered for a Savings_Account, THE System SHALL calculate interest amount as balance multiplied by interest rate
3. WHEN the interest rate is greater than 0%, THE System SHALL add the interest amount to the account balance
4. WHEN the account balance is increased by interest, THE System SHALL record a deposit Transaction with type "INTEREST"
5. WHERE the account is a Current_Account, THE System SHALL exclude the account from interest calculation
6. THE Savings_Account class SHALL define an interest rate field with getter and setter methods
7. THE System SHALL demonstrate polymorphism by calling interest calculation method only on Savings_Account instances
8. WHEN interest is applied successfully, THE System SHALL display a confirmation message with the Account_Number, interest amount, and new balance
9. WHEN the interest rate is 0%, THE System SHALL skip interest processing entirely without updating the balance or creating transaction records

### Requirement 13: PIN-based Authentication for Sensitive Operations

**User Story:** As a bank staff member, I want to verify a PIN before performing withdrawals and transfers, so that account operations are secure.

#### Acceptance Criteria

1. WHEN creating a new Account, THE System SHALL prompt for a 4-digit PIN
2. WHEN a 4-digit PIN is provided, THE System SHALL store the PIN in the accounts table
3. WHEN performing a withdrawal operation, THE System SHALL prompt the staff member to enter the account PIN
4. WHEN performing a Transfer operation, THE System SHALL prompt the staff member to enter the source account PIN
5. IF the entered PIN matches the stored PIN, THEN THE System SHALL proceed with the operation
6. IF the entered PIN does not match the stored PIN, THEN THE System SHALL reject the operation and display an error message "Invalid PIN"
7. THE System SHALL store PIN in the accounts table as a new column
8. THE System SHALL mask PIN input in the GUI using JPasswordField
9. THE Account class SHALL include a PIN field with getter and setter methods
