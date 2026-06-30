# Detailed Algorithmic Flowchart

Here is the detailed logical flowchart representing the execution algorithm of the Online Banking System, styled perfectly to match your requested theme (Green for Start/I-O, Blue for Processes, Orange for Decisions, and Red for End).

```mermaid
flowchart TD
    %% ==========================================
    %% Define Theme Styles
    %% ==========================================
    classDef startNode fill:#4CAF50,stroke:#388E3C,color:#fff,stroke-width:2px;
    classDef endNode fill:#f44336,stroke:#d32f2f,color:#fff,stroke-width:2px;
    classDef processNode fill:#e3f2fd,stroke:#64b5f6,color:#0d47a1,stroke-width:2px;
    classDef ioNode fill:#e8f5e9,stroke:#81c784,color:#1b5e20,stroke-width:2px;
    classDef decisionNode fill:#fff3e0,stroke:#ffb74d,color:#e65100,stroke-width:2px;

    %% ==========================================
    %% Nodes
    %% ==========================================
    Start([Start Application])
    
    InitDB[Initialize MongoDB Connection]
    CheckDB{Connection Successful?}
    ShowDBError[Print: Startup Error]
    
    LaunchGUI[Launch Main UI Frame]
    WaitUser{Wait for User Action}
    
    %% Paths
    ExitAction[Close Application]
    ActionCreate[Create Account Flow]
    ActionTransact[Transaction Flow Deposit/Withdraw]
    
    %% Create Account Logic
    InputCreate[Print: Enter Customer Details & Initial Deposit]
    CheckDepositAmount{Initial Deposit > 0?}
    CreateDBAcc[AccountDAO: Insert Account to DB]
    RecordInitTxn[TransactionDAO: Record Initial Transaction]
    
    %% Transaction Logic
    InputTxn[Print: Enter Account Number & Amount]
    CheckTxnAmount{Amount > 0?}
    FetchAcc[AccountDAO: Fetch Account via ID]
    CheckAccExists{Account Exists?}
    CheckBal{Balance >= Amount?}
    UpdateBal[Memory: Update Balance]
    SaveTxnDB[TransactionDAO: Atomic Push to MongoDB]
    
    %% Results
    ShowError[Print: Display Error Message]
    ShowSuccess[Print: Display Success Message]
    
    End([End])

    %% ==========================================
    %% Connections (The Logic Flow)
    %% ==========================================
    Start --> InitDB
    InitDB --> CheckDB
    
    %% DB Fail
    CheckDB -- No --> ShowDBError
    ShowDBError --> End
    
    %% DB Success -> Main Loop
    CheckDB -- Yes --> LaunchGUI
    LaunchGUI --> WaitUser
    
    %% User Options
    WaitUser -- Selects 'Create Account' --> ActionCreate
    WaitUser -- Selects 'Deposit / Withdraw' --> ActionTransact
    WaitUser -- Selects 'Exit' --> ExitAction
    
    ExitAction --> End
    
    %% Create Account Path
    ActionCreate --> InputCreate
    InputCreate --> CheckDepositAmount
    CheckDepositAmount -- No --> ShowError
    CheckDepositAmount -- Yes --> CreateDBAcc
    CreateDBAcc --> RecordInitTxn
    RecordInitTxn --> ShowSuccess
    
    %% Transaction Path
    ActionTransact --> InputTxn
    InputTxn --> CheckTxnAmount
    CheckTxnAmount -- No --> ShowError
    CheckTxnAmount -- Yes --> FetchAcc
    FetchAcc --> CheckAccExists
    
    CheckAccExists -- No --> ShowError
    CheckAccExists -- Yes --> CheckBal
    
    CheckBal -- No --> ShowError
    CheckBal -- Yes --> UpdateBal
    UpdateBal --> SaveTxnDB
    SaveTxnDB --> ShowSuccess
    
    %% Loop back to waiting state
    ShowError --> WaitUser
    ShowSuccess --> WaitUser

    %% ==========================================
    %% Apply Styles
    %% ==========================================
    class Start startNode;
    class End endNode;
    class InitDB,LaunchGUI,ExitAction,ActionCreate,ActionTransact,CreateDBAcc,RecordInitTxn,FetchAcc,UpdateBal,SaveTxnDB processNode;
    class ShowDBError,InputCreate,InputTxn,ShowError,ShowSuccess ioNode;
    class CheckDB,WaitUser,CheckDepositAmount,CheckTxnAmount,CheckAccExists,CheckBal decisionNode;
```
