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
    Start([Start Application]) :::startNode
    
    InitDB[Initialize MongoDB Connection] :::processNode
    CheckDB{Connection Successful?} :::decisionNode
    ShowDBError[Print: Startup Error] :::ioNode
    
    LaunchGUI[Launch Main UI Frame] :::processNode
    WaitUser{Wait for User Action} :::decisionNode
    
    %% Paths
    ExitAction[Close Application] :::processNode
    ActionCreate[Create Account Flow] :::processNode
    ActionTransact[Transaction Flow (Deposit/Withdraw)] :::processNode
    
    %% Create Account Logic
    InputCreate[Print: Enter Customer Details & Initial Deposit] :::ioNode
    CheckDepositAmount{Initial Deposit > 0?} :::decisionNode
    CreateDBAcc[AccountDAO: Insert Account to DB] :::processNode
    RecordInitTxn[TransactionDAO: Record Initial Transaction] :::processNode
    
    %% Transaction Logic
    InputTxn[Print: Enter Account Number & Amount] :::ioNode
    CheckTxnAmount{Amount > 0?} :::decisionNode
    FetchAcc[AccountDAO: Fetch Account via ID] :::processNode
    CheckAccExists{Account Exists?} :::decisionNode
    CheckBal{Balance >= Amount?} :::decisionNode
    UpdateBal[Memory: Update Balance] :::processNode
    SaveTxnDB[TransactionDAO: Atomic Push to MongoDB] :::processNode
    
    %% Results
    ShowError[Print: Display Error Message] :::ioNode
    ShowSuccess[Print: Display Success Message] :::ioNode
    
    End([End]) :::endNode

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
```
