# Online Banking System

A robust, desktop-based banking application built with **Java Swing** and **MongoDB**. This project was developed as part of a time-boxed college hackathon to demonstrate core Object-Oriented Programming (OOP) principles and database integration.

## Features

- **Account Management**: Create new `SAVINGS` or `CURRENT` accounts with distinct business rules.
- **Transactions**: Process deposits and withdrawals with atomic MongoDB updates.
- **Business Logic**: Enforces minimum balance requirements using polymorphism (e.g., Savings accounts require a 500 minimum balance).
- **Ledger**: View a completely ordered, immutable transaction history.
- **Cross-Platform UI**: A clean, responsive desktop interface built with Java Swing CardLayouts.

## Screenshots

### 1. Account Creation
![Create Account](images/1.png)

### 2. Balance Enquiry
![Balance Enquiry](images/2.png)

### 3. Transaction History
![Transaction History](images/3.png)

### 4. Database Backend (MongoDB)
![MongoDB Compass](images/4.png)

## Technology Stack

- **Language**: Java 17
- **GUI Framework**: Java Swing / AWT
- **Database**: MongoDB (Localhost:27017)
- **Database Driver**: MongoDB Java Driver Sync 5.1.1
- **Architecture**: Layered MVC (Model, DAO, Service, UI)

## Object-Oriented Principles Demonstrated

- **Encapsulation**: Private fields with public getters/setters in models (`Customer`, `Account`).
- **Abstraction**: Abstract `Account` class with abstract `getMinimumBalance()` method.
- **Polymorphism**: `SavingsAccount` and `CurrentAccount` overriding `getMinimumBalance()` to enforce different withdrawal rules.
- **Exception Handling**: Custom checked exceptions (`InsufficientBalanceException`, `InvalidAmountException`, `AccountNotFoundException`).

## How to Run

1. **Start MongoDB**: Ensure your local MongoDB server is running on `localhost:27017`.
2. **Compile the Code**: 
   Since all dependencies (`bson`, `mongodb-driver-core`, `mongodb-driver-sync`) are included in the `lib/` folder, you can easily compile using `javac` or the provided batch script if on Windows.
3. **Run**: Execute `com.banking.Main`.

```bash
# Example manual compilation (Windows)
javac -d out -cp "lib/*" src/com/banking/**/*.java
java -cp "out;lib/*" com.banking.Main
```
