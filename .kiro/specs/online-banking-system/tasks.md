# Implementation Plan: Online Banking System

## Overview

This implementation plan breaks down the Online Banking System into discrete, dependency-ordered tasks optimized for a 4-hour hackathon timeline. The plan follows a bottom-up approach: Foundation → Data Access → Business Logic → UI, ensuring each layer is complete and tested before moving to the next. Tasks are sized for approximately 3-3.5 hours of active coding time, with built-in verification steps and opportunities for parallel work across teammates.

**Implementation Strategy:**
- **Hour 1**: Foundation layer (database, models, exceptions, utilities)
- **Hour 2**: Data Access layer (DAOs with JDBC)
- **Hour 3**: Business Logic & Initial UI (service layer, main frame, account creation)
- **Hour 4**: Complete UI & Integration testing (remaining panels, end-to-end testing)

**Parallelization Opportunities:**
- Tasks within the same wave can be executed simultaneously by different team members
- Foundation layer tasks are highly parallelizable
- UI panel tasks can be split once the service layer is complete

## Tasks

### Phase 1: Foundation (Hour 1 - Database, Models, Exceptions)

- [ ] 1. Set up database schema and verify connectivity
  - Create MySQL database `banking_db`
  - Execute schema script to create `customers`, `accounts`, and `transactions` tables
  - Create `DBConnection` utility class with connection methods
  - Test database connection successfully
  - _Requirements: 8.1, 8.4, 8.5, 8.6_
  - _Estimated Time: 20 minutes_
  - _Dependencies: None_
  - _Parallelizable: No (foundational setup)_
  - _Verification: Run connection test; verify tables exist with `SHOW TABLES;`_

- [ ] 2. Implement model layer entities
  - [ ] 2.1 Create Customer entity class
    - Implement `Customer.java` with fields: customerId, name, phone, email, address
    - Add default constructor and parameterized constructor
    - Implement all getters and setters (encapsulation)
    - _Requirements: 6.2, 10.1_
    - _Estimated Time: 10 minutes_
    - _Dependencies: None_
    - _Parallelizable: Yes (can work simultaneously with 2.2 and 2.3)_
    - _Verification: Compile successfully; instantiate Customer object and test getters/setters_

  - [ ] 2.2 Create abstract Account base class with polymorphism
    - Implement `Account.java` as abstract class with fields: accountNumber, customerId, accountType, balance, openingDate, status
    - Implement getters and setters for all fields (encapsulation)
    - Add `deposit(double amount)` method with validation
    - Add `withdraw(double amount)` method with balance checks
    - Declare abstract method `getMinimumBalance()` for polymorphism
    - Add comprehensive Javadoc comments
    - _Requirements: 6.2, 6.3, 6.6, 10.2_
    - _Estimated Time: 20 minutes_
    - _Dependencies: None_
    - _Parallelizable: Yes (can work simultaneously with 2.1 and 2.3)_
    - _Verification: Compile successfully; verify abstract class cannot be instantiated_

  - [ ] 2.3 Create Transaction entity class
    - Implement `Transaction.java` with fields: transactionId, accountNumber, type, amount, balanceAfter, txnTime
    - Add default constructor and parameterized constructor
    - Implement all getters and setters (encapsulation)
    - _Requirements: 6.2, 10.1_
    - _Estimated Time: 10 minutes_
    - _Dependencies: None_
    - _Parallelizable: Yes (can work simultaneously with 2.1 and 2.2)_
    - _Verification: Compile successfully; instantiate Transaction object and test getters/setters_

- [ ] 3. Implement Account subclasses demonstrating inheritance
  - [ ] 3.1 Create SavingsAccount subclass
    - Implement `SavingsAccount.java` extending `Account`
    - Add field `interestRate` with getter and setter
    - Override `getMinimumBalance()` to return 500.0
    - Implement constructor calling super constructor
    - _Requirements: 6.3, 6.6, 10.1_
    - _Estimated Time: 10 minutes_
    - _Dependencies: 2.2_
    - _Parallelizable: Yes (can work simultaneously with 3.2)_
    - _Verification: Compile successfully; verify inheritance with `extends` keyword; test minimum balance returns 500.0_

  - [ ] 3.2 Create CurrentAccount subclass
    - Implement `CurrentAccount.java` extending `Account`
    - Add field `overdraftLimit` with getter and setter
    - Override `getMinimumBalance()` to return 1000.0
    - Implement constructor calling super constructor
    - _Requirements: 6.3, 6.6, 10.1_
    - _Estimated Time: 10 minutes_
    - _Dependencies: 2.2_
    - _Parallelizable: Yes (can work simultaneously with 3.1)_
    - _Verification: Compile successfully; verify inheritance with `extends` keyword; test minimum balance returns 1000.0_

- [ ] 4. Implement custom exception classes
  - [ ] 4.1 Create business exception classes
    - Implement `InsufficientBalanceException.java` extending Exception
    - Implement `InvalidAmountException.java` extending Exception
    - Implement `AccountNotFoundException.java` extending Exception
    - Each exception should have constructor accepting String message
    - Add appropriate Javadoc comments explaining when each exception is thrown
    - _Requirements: 6.8, 9.1, 9.2, 9.3, 9.4, 9.5, 9.6_
    - _Estimated Time: 10 minutes_
    - _Dependencies: None_
    - _Parallelizable: Yes (can work simultaneously with any task)_
    - _Verification: Compile successfully; verify each exception extends Exception_

- [ ] 5. Checkpoint - Verify foundation layer compilation
  - Compile all model and exception classes
  - Verify no compilation errors
  - Test database connection utility
  - Ensure all tests pass, ask the user if questions arise
  - _Estimated Time: 5 minutes_
  - _Dependencies: 1, 2.1, 2.2, 2.3, 3.1, 3.2, 4.1_

### Phase 2: Data Access Layer (Hour 2 - DAOs with JDBC)

- [ ] 6. Implement DAO interfaces (interface-based design)
  - [ ] 6.1 Create AccountDAO interface
    - Define interface `AccountDAO` with methods: `createAccount(Customer, Account)`, `getAccountByNumber(String)`, `updateBalance(String, double)`, `getCustomerName(String)`
    - Add Javadoc comments for each method specifying parameters, return types, and exceptions
    - _Requirements: 6.4, 6.5, 8.2_
    - _Estimated Time: 10 minutes_
    - _Dependencies: 2.1, 2.2_
    - _Parallelizable: Yes (can work simultaneously with 6.2)_
    - _Verification: Compile successfully; verify interface keyword used_

  - [ ] 6.2 Create TransactionDAO interface
    - Define interface `TransactionDAO` with methods: `recordTransaction(Transaction)`, `getTransactionHistory(String)`
    - Add Javadoc comments for each method
    - _Requirements: 6.4, 6.5, 8.2_
    - _Estimated Time: 10 minutes_
    - _Dependencies: 2.3_
    - _Parallelizable: Yes (can work simultaneously with 6.1)_
    - _Verification: Compile successfully; verify interface keyword used_

- [ ] 7. Implement DAO implementations with JDBC
  - [ ] 7.1 Implement AccountDAOImpl with JDBC operations
    - Create `AccountDAOImpl` implementing `AccountDAO` interface
    - Implement `createAccount()` with transaction management (INSERT into customers and accounts)
    - Generate unique account number with format "ACC" + timestamp + random
    - Implement `getAccountByNumber()` with PreparedStatement (throw AccountNotFoundException if not found)
    - Implement `updateBalance()` with PreparedStatement
    - Implement `getCustomerName()` with JOIN query
    - Use PreparedStatement for all queries (prevent SQL injection)
    - Handle SQLException appropriately
    - Add comprehensive error handling and logging
    - _Requirements: 1.2, 1.7, 4.5, 6.4, 8.2, 8.3, 8.7, 8.8_
    - _Estimated Time: 30 minutes_
    - _Dependencies: 1, 6.1_
    - _Parallelizable: Yes (can work simultaneously with 7.2)_
    - _Verification: Write standalone test to create account and retrieve it; verify data in database_

  - [ ] 7.2 Implement TransactionDAOImpl with JDBC operations
    - Create `TransactionDAOImpl` implementing `TransactionDAO` interface
    - Implement `recordTransaction()` with INSERT statement using PreparedStatement
    - Implement `getTransactionHistory()` with ORDER BY txn_time DESC
    - Use PreparedStatement for all queries
    - Handle SQLException appropriately
    - _Requirements: 1.4, 2.3, 3.3, 5.2, 5.3, 8.2, 8.7, 8.8_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 1, 6.2_
    - _Parallelizable: Yes (can work simultaneously with 7.1)_
    - _Verification: Write standalone test to record transaction and retrieve history; verify data in database_

  - [ ]* 7.3 Write unit tests for DAO layer
    - Create test class for AccountDAOImpl
    - Test account creation, retrieval, and balance updates
    - Test AccountNotFoundException for non-existent accounts
    - Create test class for TransactionDAOImpl
    - Test transaction recording and history retrieval
    - Use test database or rollback transactions after tests
    - _Requirements: 8.2, 8.8_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 7.1, 7.2_
    - _Verification: All DAO unit tests pass_

- [ ] 8. Checkpoint - Verify data access layer
  - Run DAO tests (manual or unit tests)
  - Verify database operations succeed
  - Check data integrity in database tables
  - Ensure all tests pass, ask the user if questions arise
  - _Estimated Time: 5 minutes_
  - _Dependencies: 7.1, 7.2_

### Phase 3: Business Logic & Initial UI (Hour 3 - Service Layer & Account Creation)

- [ ] 9. Implement service layer interfaces and implementation
  - [ ] 9.1 Create BankService interface
    - Define interface `BankService` with methods: `createAccount()`, `deposit()`, `withdraw()`, `getBalance()`, `getTransactionHistory()`
    - Specify all parameters, return types, and exceptions in method signatures
    - Add comprehensive Javadoc comments
    - _Requirements: 6.4, 6.5_
    - _Estimated Time: 10 minutes_
    - _Dependencies: 2.1, 2.2, 2.3_
    - _Parallelizable: No (needed by 9.2)_
    - _Verification: Compile successfully; verify interface definition_

  - [ ] 9.2 Implement BankServiceImpl with business logic
    - Create `BankServiceImpl` implementing `BankService` interface
    - Implement constructor with dependency injection (AccountDAO, TransactionDAO)
    - Implement `createAccount()`: validate initialDeposit > 0, create Customer and Account, record initial transaction
    - Implement `deposit()`: validate amount > 0, get account, update balance, record transaction
    - Implement `withdraw()`: validate amount > 0, check sufficient balance against minimum, update balance, record transaction
    - Implement `getBalance()`: retrieve account and customer name, return Map with details
    - Implement `getTransactionHistory()`: validate account exists, retrieve transactions
    - Use database transactions for balance updates (BEGIN, COMMIT, ROLLBACK on error)
    - Throw appropriate custom exceptions (InvalidAmountException, InsufficientBalanceException, AccountNotFoundException)
    - Add comprehensive error handling and exception propagation
    - _Requirements: 1.1-1.6, 2.1-2.7, 3.1-3.9, 4.1-4.5, 5.1-5.7, 6.6, 8.3, 9.1-9.7_
    - _Estimated Time: 40 minutes_
    - _Dependencies: 4.1, 6.1, 6.2, 7.1, 7.2, 9.1_
    - _Parallelizable: No (critical integration point)_
    - _Verification: Write standalone main method to test each service operation; verify correct exceptions thrown_

  - [ ]* 9.3 Write unit tests for service layer
    - Create test class for BankServiceImpl
    - Test account creation with valid and invalid initial deposits
    - Test deposit with valid and invalid amounts
    - Test withdrawal with sufficient and insufficient balance
    - Test balance enquiry for existing and non-existent accounts
    - Test transaction history retrieval
    - Mock DAO dependencies or use test database
    - Verify custom exceptions are thrown appropriately
    - _Requirements: 1.5, 2.5, 3.5, 3.7, 9.1-9.6_
    - _Estimated Time: 25 minutes_
    - _Dependencies: 9.2_
    - _Verification: All service layer unit tests pass_

- [ ] 10. Implement main application frame
  - [ ] 10.1 Create MainFrame with navigation using CardLayout
    - Create `MainFrame.java` extending JFrame
    - Implement CardLayout for panel switching
    - Create navigation menu or buttons for: Create Account, Deposit, Withdraw, Balance Enquiry, Transaction History
    - Initialize BankService with DAO instances
    - Set up frame properties (size, close operation, visibility)
    - Implement panel switching methods
    - _Requirements: 7.1, 7.2, 7.3, 7.7_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 9.2_
    - _Parallelizable: No (needed by all UI panels)_
    - _Verification: Launch application; verify navigation menu displays; test panel switching_

- [ ] 11. Implement account creation UI panel
  - [ ] 11.1 Create CreateAccountPanel with form and validation
    - Create `CreateAccountPanel.java` extending JPanel
    - Add form fields: name (JTextField), phone (JTextField), email (JTextField), address (JTextArea), accountType (JComboBox with SAVINGS/CURRENT), initialDeposit (JTextField)
    - Add "Create Account" button with action listener
    - Implement input validation (non-empty fields, positive initial deposit)
    - Call `bankService.createAccount()` on button click
    - Display success message with generated account number using JOptionPane
    - Catch and display custom exceptions (InvalidAmountException) with appropriate error messages
    - Handle NumberFormatException for invalid number inputs
    - Format layout using GridBagLayout or GroupLayout
    - _Requirements: 1.1-1.8, 7.2, 7.4, 7.5, 7.6, 9.7_
    - _Estimated Time: 30 minutes_
    - _Dependencies: 9.2, 10.1_
    - _Parallelizable: No (first UI to implement for end-to-end testing)_
    - _Verification: Create account through UI; verify account number returned; check database for customer, account, and transaction records_

- [ ] 12. Checkpoint - Test account creation end-to-end
  - Launch application and navigate to Create Account panel
  - Create test account with valid data
  - Verify account number is displayed
  - Verify database contains customer, account, and transaction records
  - Test error handling with invalid initial deposit (<=0)
  - Ensure all tests pass, ask the user if questions arise
  - _Estimated Time: 10 minutes_
  - _Dependencies: 11.1_

### Phase 4: Complete UI & Integration Testing (Hour 4 - All Panels & Testing)

- [ ] 13. Implement deposit and withdrawal UI panels
  - [ ] 13.1 Create DepositPanel with form
    - Create `DepositPanel.java` extending JPanel
    - Add form fields: accountNumber (JTextField), amount (JTextField)
    - Add "Deposit" button with action listener
    - Implement input validation (non-empty fields, positive amount)
    - Call `bankService.deposit()` on button click
    - Display success message with new balance using JOptionPane
    - Catch and display exceptions (AccountNotFoundException, InvalidAmountException, SQLException)
    - Handle NumberFormatException for invalid inputs
    - _Requirements: 2.1-2.7, 7.2, 7.4, 7.5, 9.7_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 9.2, 10.1_
    - _Parallelizable: Yes (can work simultaneously with 13.2)_
    - _Verification: Deposit money to test account; verify success message with new balance; check database for updated balance and transaction record_

  - [ ] 13.2 Create WithdrawPanel with form
    - Create `WithdrawPanel.java` extending JPanel
    - Add form fields: accountNumber (JTextField), amount (JTextField)
    - Add "Withdraw" button with action listener
    - Implement input validation (non-empty fields, positive amount)
    - Call `bankService.withdraw()` on button click
    - Display success message with new balance using JOptionPane
    - Catch and display exceptions (AccountNotFoundException, InvalidAmountException, InsufficientBalanceException, SQLException)
    - Handle NumberFormatException for invalid inputs
    - _Requirements: 3.1-3.9, 7.2, 7.4, 7.5, 9.7_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 9.2, 10.1_
    - _Parallelizable: Yes (can work simultaneously with 13.1)_
    - _Verification: Withdraw money from test account; verify success message; test insufficient balance error; check database_

- [ ] 14. Implement balance enquiry and transaction history UI panels
  - [ ] 14.1 Create BalanceEnquiryPanel with display
    - Create `BalanceEnquiryPanel.java` extending JPanel
    - Add form field: accountNumber (JTextField)
    - Add "Check Balance" button with action listener
    - Add display area (JTextArea or JLabels) for customer name, account type, and balance
    - Call `bankService.getBalance()` on button click
    - Display account details formatted with 2 decimal places
    - Catch and display AccountNotFoundException
    - _Requirements: 4.1-4.5, 7.2, 7.4, 7.5, 7.6_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 9.2, 10.1_
    - _Parallelizable: Yes (can work simultaneously with 14.2)_
    - _Verification: Check balance for test account; verify customer name, account type, and balance display correctly_

  - [ ] 14.2 Create TransactionHistoryPanel with table display
    - Create `TransactionHistoryPanel.java` extending JPanel
    - Add form field: accountNumber (JTextField)
    - Add "View History" button with action listener
    - Create JTable with columns: Transaction ID, Type, Amount, Balance After, Timestamp
    - Call `bankService.getTransactionHistory()` on button click
    - Populate table with transaction data ordered by timestamp descending
    - Display "No transactions found" message for empty history
    - Catch and display AccountNotFoundException
    - Format amounts with 2 decimal places
    - _Requirements: 5.1-5.7, 7.2, 7.4, 7.5, 7.6_
    - _Estimated Time: 25 minutes_
    - _Dependencies: 9.2, 10.1_
    - _Parallelizable: Yes (can work simultaneously with 14.1)_
    - _Verification: View transaction history for test account; verify all transactions display in correct order with proper formatting_

- [ ] 15. Wire all UI panels into MainFrame
  - [ ] 15.1 Integrate all panels into CardLayout
    - Add all panels (CreateAccountPanel, DepositPanel, WithdrawPanel, BalanceEnquiryPanel, TransactionHistoryPanel) to MainFrame's CardLayout
    - Ensure navigation buttons switch between panels correctly
    - Test navigation flow between all screens
    - Verify all panels can access BankService instance
    - _Requirements: 7.3, 7.7_
    - _Estimated Time: 10 minutes_
    - _Dependencies: 11.1, 13.1, 13.2, 14.1, 14.2_
    - _Parallelizable: No (integration task)_
    - _Verification: Navigate between all panels; verify each panel loads correctly_

  - [ ]* 15.2 Write integration tests for UI flow
    - Test complete user workflow: create account → deposit → withdraw → check balance → view history
    - Verify data consistency across operations
    - Test error scenarios: invalid account numbers, insufficient balance, invalid amounts
    - Verify exception handling and error message display
    - _Requirements: 1.1-1.8, 2.1-2.7, 3.1-3.9, 4.1-4.5, 5.1-5.7_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 15.1_
    - _Verification: All integration tests pass; complete workflow executes without errors_

- [ ] 16. Final testing and polish
  - [ ] 16.1 Perform end-to-end MVP testing
    - Test complete workflow: create account, deposit, withdraw, check balance, view history
    - Test all error scenarios: invalid account numbers, insufficient balance, invalid amounts
    - Verify database consistency after each operation
    - Verify balance calculations are accurate with 2 decimal places
    - Test with both SAVINGS and CURRENT account types
    - Verify polymorphism: different minimum balances for account types
    - Document any bugs found and fix critical issues
    - _Requirements: All MVP requirements (1-10)_
    - _Estimated Time: 20 minutes_
    - _Dependencies: 15.1_
    - _Parallelizable: No (comprehensive testing)_
    - _Verification: All MVP features work correctly; no runtime errors with valid inputs_

  - [ ] 16.2 Code quality review and documentation
    - Review code for naming conventions (camelCase, PascalCase)
    - Ensure consistent indentation and formatting
    - Add missing comments and Javadoc where needed
    - Verify all classes compile without errors
    - Verify methods are reasonably sized (<30 lines preferred)
    - Check that all packages are correctly organized
    - _Requirements: 10.1-10.7_
    - _Estimated Time: 15 minutes_
    - _Dependencies: 15.1_
    - _Parallelizable: Yes (can be done by different team members reviewing different packages)_
    - _Verification: Code compiles cleanly; no warnings; all files properly formatted_

- [ ] 17. Prepare demo and presentation materials
  - [ ] 17.1 Create demo script and test data
    - Prepare demo script showing all 5 MVP operations in sequence
    - Create test accounts with varying balances
    - Prepare scenarios demonstrating OOP concepts (encapsulation, inheritance, polymorphism, interfaces)
    - Document code locations for OOP demonstrations (see rubric mapping in design)
    - Practice 2-minute feature walkthrough
    - Prepare to explain: abstract Account class, subclass inheritance, polymorphic getMinimumBalance(), interface-based design
    - _Estimated Time: 10 minutes_
    - _Dependencies: 16.1_
    - _Parallelizable: Yes (can work simultaneously with 17.2)_
    - _Verification: Successfully execute demo script; OOP concepts clearly demonstrated_

  - [ ] 17.2 Verify grading rubric compliance
    - Verify coding practices (3 marks): naming, indentation, comments, modularity, readability
    - Verify OOP concepts (3 marks): encapsulation, inheritance, polymorphism, interfaces demonstrated
    - Verify functional correctness (4 marks): all MVP features work correctly
    - Prepare to answer questions about design decisions
    - _Estimated Time: 5 minutes_
    - _Dependencies: 16.2_
    - _Parallelizable: Yes (can work simultaneously with 17.1)_
    - _Verification: All rubric items addressed; demo-ready_

- [ ] 18. Final checkpoint - Application demo-ready
  - Run complete demo script successfully
  - All 5 MVP operations work without errors
  - Database contains valid test data
  - Code is clean, commented, and properly formatted
  - OOP concepts can be demonstrated on demand
  - Ready for individual presentation and questions
  - _Estimated Time: 5 minutes_
  - _Dependencies: 17.1, 17.2_

## Notes

### Task Characteristics
- **Tasks marked with `*` are optional** and can be skipped for faster MVP completion. Test tasks are optional but recommended for robustness.
- **Core implementation tasks are NOT marked optional** and must be completed for a functional MVP.
- **Each task references specific requirements** from the requirements document for traceability.
- **Checkpoints ensure incremental validation** at major milestones (foundation, data access, initial UI, complete application).

### Parallelization Strategy
- **Foundation layer (Tasks 2.1-4.1)**: Highly parallelizable - model classes and exceptions can be developed simultaneously
- **DAO layer (Tasks 7.1-7.2)**: AccountDAO and TransactionDAO implementations can be developed in parallel
- **UI panels (Tasks 13.1-14.2)**: All panels can be developed simultaneously once service layer is complete
- **Testing and polish (Tasks 16.2-17.2)**: Code review and demo preparation can be split across team members

### Testing Approach
- **Unit tests (optional tasks)** validate individual components in isolation
- **Integration tests (optional tasks)** verify end-to-end workflows
- **Manual testing** is included in checkpoint tasks for rapid validation during hackathon

### Time Management
- **Hour 1 (Foundation)**: Tasks 1-5 (~65 minutes + 5 min checkpoint)
- **Hour 2 (Data Access)**: Tasks 6-8 (~70 minutes + 5 min checkpoint)
- **Hour 3 (Business Logic & Initial UI)**: Tasks 9-12 (~110 minutes + 10 min checkpoint)
- **Hour 4 (Complete UI & Testing)**: Tasks 13-18 (~135 minutes + 5 min final checkpoint)

Total estimated: ~3.5 hours active coding + 25 minutes checkpoints/testing = ~3.75 hours

### Success Criteria
- All 5 MVP operations functional (create account, deposit, withdraw, balance enquiry, transaction history)
- No runtime exceptions with valid inputs
- Database persistence verified
- Custom exceptions properly thrown and displayed
- OOP concepts demonstrable in code
- Clean, commented, well-formatted code

## Task Dependency Graph

```json
{
  "waves": [
    {
      "id": 0,
      "tasks": ["1", "2.1", "2.3", "4.1"]
    },
    {
      "id": 1,
      "tasks": ["2.2"]
    },
    {
      "id": 2,
      "tasks": ["3.1", "3.2"]
    },
    {
      "id": 3,
      "tasks": ["6.1", "6.2"]
    },
    {
      "id": 4,
      "tasks": ["7.1", "7.2"]
    },
    {
      "id": 5,
      "tasks": ["7.3"]
    },
    {
      "id": 6,
      "tasks": ["9.1"]
    },
    {
      "id": 7,
      "tasks": ["9.2"]
    },
    {
      "id": 8,
      "tasks": ["9.3", "10.1"]
    },
    {
      "id": 9,
      "tasks": ["11.1"]
    },
    {
      "id": 10,
      "tasks": ["13.1", "13.2", "14.1", "14.2"]
    },
    {
      "id": 11,
      "tasks": ["15.1"]
    },
    {
      "id": 12,
      "tasks": ["15.2", "16.1", "16.2"]
    },
    {
      "id": 13,
      "tasks": ["17.1", "17.2"]
    }
  ]
}
```
