@echo off
set BASE=C:\Users\Nagarjuna\OneDrive\Documents\Desktop\OOPs_JAVA
set CP=%BASE%\lib\bson-5.1.1.jar;%BASE%\lib\mongodb-driver-core-5.1.1.jar;%BASE%\lib\mongodb-driver-sync-5.1.1.jar
set OUT=%BASE%\out

echo === Layer 1: Model + Exception ===
javac -d "%OUT%" "%BASE%\src\com\banking\model\Customer.java" "%BASE%\src\com\banking\model\Transaction.java" "%BASE%\src\com\banking\exception\InsufficientBalanceException.java" "%BASE%\src\com\banking\exception\InvalidAmountException.java" "%BASE%\src\com\banking\exception\AccountNotFoundException.java" "%BASE%\src\com\banking\model\Account.java" "%BASE%\src\com\banking\model\SavingsAccount.java" "%BASE%\src\com\banking\model\CurrentAccount.java"
if %ERRORLEVEL% NEQ 0 (echo LAYER 1 FAILED && exit /b 1)
echo Layer 1 OK

echo === Layer 2: DAO ===
javac -d "%OUT%" -cp "%OUT%;%CP%" "%BASE%\src\com\banking\dao\DBConnection.java" "%BASE%\src\com\banking\dao\AccountDAO.java" "%BASE%\src\com\banking\dao\TransactionDAO.java" "%BASE%\src\com\banking\dao\AccountDAOImpl.java" "%BASE%\src\com\banking\dao\TransactionDAOImpl.java"
if %ERRORLEVEL% NEQ 0 (echo LAYER 2 FAILED && exit /b 1)
echo Layer 2 OK

echo === Layer 3: Service ===
javac -d "%OUT%" -cp "%OUT%;%CP%" "%BASE%\src\com\banking\service\BankService.java" "%BASE%\src\com\banking\service\BankServiceImpl.java"
if %ERRORLEVEL% NEQ 0 (echo LAYER 3 FAILED && exit /b 1)
echo Layer 3 OK

echo === Layer 4: UI + Main ===
javac -d "%OUT%" -cp "%OUT%;%CP%" "%BASE%\src\com\banking\ui\MainFrame.java" "%BASE%\src\com\banking\ui\CreateAccountPanel.java" "%BASE%\src\com\banking\ui\DepositPanel.java" "%BASE%\src\com\banking\ui\WithdrawPanel.java" "%BASE%\src\com\banking\ui\BalanceEnquiryPanel.java" "%BASE%\src\com\banking\ui\TransactionHistoryPanel.java" "%BASE%\src\com\banking\Main.java"
if %ERRORLEVEL% NEQ 0 (echo LAYER 4 FAILED && exit /b 1)
echo Layer 4 OK

echo.
echo === BUILD SUCCESS ===
