# Expense_Tracker-JDBC--MotionCut
A expense tracker application to manage and analyse all your expenses.

## Overview
Expense Tracker is a Java-based application that allows users to manage their financial transactions. It is designed to help users manage their financial transactions by allowing them to record expenses and deposits, categorize these transactions, and perform various financial analysis operations and gain insights into one's financial situation. It offers a straightforward user interface for ease of use as the application has a graphical user interface (GUI) for user interaction.

## Features
- **Database Connection:** The application connects to a MySQL database to store transaction data. It uses the JDBC (Java Database Connectivity) API for database operations.There are instructions provided in the code comments for setting up the required MySQL database and schema for storing transaction data.

- **GUI Elements:** The GUI includes buttons for adding transactions, viewing spent money, viewing deposited money, viewing all expenses, and performing overall financial analysis. There are also text areas and input fields for entering and displaying transaction details.

- **Transaction Handling:** Users can add transactions by specifying the transaction method (DEPOSIT or WITHDRAW), selecting a category, entering the expense amount, and providing a transaction message.

- **View Transactions:** Users can view spending transactions (WITHDRAW), deposited transactions (DEPOSIT), or all expenses. The transaction details are retrieved from the database and displayed in a scrollable text area.

- **Overall Analysis:** Users can get an overall financial analysis that includes the total amount deposited, total amount spent, and the remaining balance. The analysis provides insights into the user's financial situation.

- **Data Validation:** The code includes data validation to ensure that only valid numeric input is accepted for the transaction amount.

- **Exception Handling:** The code includes exception handling to manage potential errors during database operations.

 ## Usage
- Launch the application, and you'll be presented with several options.
- To add a new transaction, click on the "Add Transaction" button, select the transaction method, category, enter the expense amount, and provide a reason for the transaction. Then click "Add Transaction."
- To view spending transactions, click on "View Spent."
- To view deposit transactions, click on "View Deposited."
- To view all expenses, click on "View All Expenses."
- For an overall financial analysis, click on "Overall Analysis."

## Technology Stack
- Java
- MySQL database
  
## Database Setup
The application uses a MySQL database to store transaction data. Ensure that you have MySQL installed and a database will be created with given schema in the source code and there are instructions provided in the code comments for setting up the required MySQL database and schema for storing transaction data. Make sure that the database connection is made properly and protect your database url and passwords.
