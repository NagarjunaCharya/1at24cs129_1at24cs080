# Online Banking System — Code Explanation Guide

This document is designed to be your "cheat sheet" during your hackathon presentation. It explicitly maps your codebase to the OOP grading rubric so you can easily show the judges exactly where and how you implemented each concept.

---

## 1. Encapsulation
**Concept:** Hiding the internal state of objects and requiring all interaction to be performed through an object's methods.

**Where to show it:**
- **File:** `src/com/banking/model/Customer.java` and `src/com/banking/model/Account.java`
- **Explanation:** All data fields (like `balance`, `accountNumber`, `customerId`) are marked as `private`. They cannot be modified directly from outside the class. Instead, they are accessed and modified safely using `public` getter and setter methods.

---

## 2. Abstraction
**Concept:** Hiding complex implementation details and showing only the essential features of the object.

**Where to show it:**
- **File:** `src/com/banking/model/Account.java`
- **Explanation:** The `Account` class is declared as `abstract`, meaning you can never instantiate a raw "Account". It defines the abstract method `public abstract double getMinimumBalance();`. This forces any child class to provide its own specific implementation of the minimum balance rule, hiding the generic concept behind a strict contract.

---

## 3. Inheritance
**Concept:** Mechanism where a new class inherits properties and behaviors from an existing class.

**Where to show it:**
- **File:** `src/com/banking/model/SavingsAccount.java` and `src/com/banking/model/CurrentAccount.java`
- **Explanation:** Both of these classes use the `extends Account` keyword. They inherit all the fields (balance, opening date) and methods (deposit, withdraw) from the parent `Account` class, drastically reducing code duplication.

---

## 4. Polymorphism
**Concept:** The ability of different objects to respond in their own specific way to the same method call.

**Where to show it:**
- **File:** `src/com/banking/service/BankServiceImpl.java` (Line 126-129)
- **Explanation:** In the `withdraw` method, the system fetches a generic `Account` object from the database:
  ```java
  Account account = accountDAO.getAccountByNumber(accountNumber);
  account.withdraw(amount);
  ```
  The `withdraw()` method internally calls `getMinimumBalance()`. Due to polymorphism, Java dynamically figures out at runtime whether this is a `SavingsAccount` (requires 500 minimum) or a `CurrentAccount` (requires 1000 minimum) and applies the correct mathematical rule without needing ugly `if/else` statements!

---

## 5. Exception Handling
**Concept:** Gracefully handling errors to prevent the application from crashing.

**Where to show it:**
- **File:** `src/com/banking/exception/` (The custom exception classes) and `src/com/banking/ui/WithdrawPanel.java` (Line 99-102)
- **Explanation:** The system does not use generic errors. We built custom checked exceptions: `InsufficientBalanceException`, `InvalidAmountException`, and `AccountNotFoundException`. When a user tries to withdraw too much money, the `Account` class `throw`s the exception, and the `WithdrawPanel` uses a `try-catch` block to intercept it and display a friendly pop-up alert to the user instead of crashing the program.

---

## 6. Database Integration (MongoDB)
**Concept:** Storing and retrieving state securely.

**Where to show it:**
- **File:** `src/com/banking/dao/AccountDAOImpl.java` and `src/com/banking/dao/TransactionDAOImpl.java`
- **Explanation:** We use the DAO (Data Access Object) Design Pattern to completely isolate the database code from the UI. The MongoDB Java Sync Driver is used to perform atomic operations (using `$push` and `$set`) ensuring that a user's transaction history and their live balance are updated flawlessly at the exact same time.
