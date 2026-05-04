# Software-Assign2

## Features Implemented

### 1. Expense Entry Screen
- Input field for expense amount
- Dropdown (Spinner) for category selection:
  - Food
  - Transport
  - Entertainment
  - Other
- Save button to store expense

---

### 2. Input Validation
- Amount field must not be empty
- Amount must be greater than 0
- Prevents invalid data insertion into database

---

### 3. Database Integration
- Inserts expense data into SQLite database
- Retrieves category and amount from user input

---

### 4. Notification Hook Integration
After saving an expense, the module triggers a budget check:
- If spending ≥ 80% → Warning notification
- If spending ≥ 100% → Exhausted notification

(Handled via `NotificationService`)

---

## 🗂️ Files Included
