import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.Objects;

public class ExpenseTracker extends JFrame implements ActionListener {

//     Database connection
    String url = Database.url;
    String userName = Database.userName;
    String password = Database.password;

    Connection connection;

    Statement statement;

    JButton addExpense,allExpenses,spentMoney,depositedMoney,addTransaction,analysis;
    JScrollPane scrollPane_ADD;
    JTextArea transaction_area,displayTransactions;

    JComboBox<String> selection,category;

    JTextField inputField,transaction_reason;

    JLabel selection_label,category_label,successMessage;
    ExpenseTracker() throws Exception{

        Class.forName("com.mysql.cj.jdbc.Driver");

        connection = DriverManager.getConnection(url,userName,password);

        statement = connection.createStatement();

        String createTable = "create table if not exists expenseTracker(\n" +
                "\t\tid INT PRIMARY KEY AUTO_INCREMENT,\n" +
                "method VARCHAR(20),"+
                "        category VARCHAR(25),\n" +
                "\t\texpense DOUBLE,\n" +
                "        message VARCHAR(100)\n" +
                ")";

        int result = statement.executeUpdate(createTable);

        setSize(1200,800);
        setTitle("Expense Tracker");

        addExpense = new JButton("Add Transaction");
        addExpense.setBounds(50,50,150,100);
        addExpense.setBackground(new Color(65, 159, 159));
        addExpense.setFocusable(false);
        addExpense.addActionListener(this);

        spentMoney = new JButton("View Spent");
        spentMoney.setBounds(50,190,150,100);
        spentMoney.setBackground(new Color(239, 107, 107));
        spentMoney.setFocusable(false);
        spentMoney.addActionListener(this);

        depositedMoney = new JButton("View Deposited");
        depositedMoney.setBounds(50,330,150,100);
        depositedMoney.setBackground(new Color(72, 224, 93));
        depositedMoney.setFocusable(false);
        depositedMoney.addActionListener(this);


        allExpenses = new JButton("View All Expenses");
        allExpenses.setBounds(50,470,150,100);
        allExpenses.setBackground(new Color(98, 155, 238));
        allExpenses.setFocusable(false);
        allExpenses.addActionListener(this);


        analysis = new JButton("Overall Analysis");
        analysis.setBounds(50,610,150,100);
        analysis.setBackground(new Color(236, 255, 126));
        analysis.setFocusable(false);
        analysis.addActionListener(this);

        displayTransactions = new JTextArea();
        displayTransactions.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));

        scrollPane_ADD = new JScrollPane(displayTransactions);
        scrollPane_ADD.setBounds(350,75,650,600);
        scrollPane_ADD.setVisible(false);


        transaction_area = new JTextArea();
        transaction_area.setLayout(new GridLayout(5,2));
        transaction_area.setBounds(350,75,650,600);
        transaction_area.setVisible(false);


        selection_label = new JLabel("Please select transaction method");
        selection_label.setHorizontalAlignment(SwingConstants.CENTER);
        transaction_area.add(selection_label);

        String[] method = {"Select","DEPOSIT","WITHDRAW"};
        selection = new JComboBox<>(method);
        selection.setAlignmentY(SwingConstants.CENTER);
        selection.setFont(new Font(Font.SERIF,Font.BOLD,17));
        selection.addActionListener(this);

        transaction_area.add(selection);


        category_label = new JLabel("Select a Category of expense");
        category_label.setHorizontalAlignment(SwingConstants.CENTER);
        transaction_area.add(category_label);

        String[] transactionCategory = {"Select","Grocery","Salary","Travel","Clothing","Rent","HealthCare"
                ,"Education","Bonus","Dividend","Royalty","Rental","Others"};

        category = new JComboBox<>(transactionCategory);
        category.addActionListener(this);
        category.setFont(new Font(Font.SERIF,Font.BOLD,17));
        transaction_area.add(category);


        JLabel input_label = new JLabel("Enter transaction Amount :");
        input_label.setHorizontalAlignment(SwingConstants.CENTER);
        transaction_area.add(input_label);

        inputField = new JTextField("0");
        inputField.setHorizontalAlignment(SwingConstants.CENTER);
        inputField.setFont(new Font(Font.SERIF,Font.BOLD,20));
        Border border = BorderFactory.createLineBorder(new Color(38, 47, 37, 255), 1);
        inputField.setBorder(border);
        inputField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char character = e.getKeyChar();
                if ((character < '0' || character > '9') && character != '.'){
                    e.consume();
                }
            }
        });

        transaction_area.add(inputField);

        JLabel reason_label = new JLabel("Enter reason for current Transaction -->");
        reason_label.setHorizontalAlignment(SwingConstants.CENTER);
        transaction_area.add(reason_label);

        transaction_reason = new JTextField();
        transaction_reason.setHorizontalAlignment(SwingConstants.CENTER);
        transaction_reason.setFont(new Font(Font.SERIF,Font.BOLD,21));
        transaction_area.add(transaction_reason);

        successMessage = new JLabel("");
        successMessage.setHorizontalAlignment(SwingConstants.CENTER);
        successMessage.setForeground(new Color(14, 234, 23));
        successMessage.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
        transaction_area.add(successMessage);


        addTransaction = new JButton("ADD TRANSACTION");
        addTransaction.setFocusable(false);
        addTransaction.addActionListener(this);
        transaction_area.add(addTransaction);


        add(addExpense);
        add(spentMoney);
        add(depositedMoney);
        add(allExpenses);
        add(analysis);

        add(transaction_area);
        add(scrollPane_ADD);


        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void visibility(){
        scrollPane_ADD.setVisible(false);
        transaction_area.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == selection){
            successMessage.setText("");
            selection_label.setText((String) selection.getSelectedItem());
        }

        if(e.getSource() == category){
            successMessage.setText("");
            category_label.setText((String) category.getSelectedItem());
        }

        // Adding transaction
        if (e.getSource() == addTransaction){
            try{
                successMessage.setText("");
                double inputValue = Double.parseDouble(inputField.getText());
                // nothing to do in case of no selection
                if( (!Objects.equals(selection.getSelectedItem(), "Select")) && (!Objects.equals(category.getSelectedItem(), "Select")) && (inputValue > 0)  ){
                    String methodOfTransction = (String) selection.getSelectedItem();
                    String reasonOfTransaction = transaction_reason.getText();
                    String categoryOfExpense = (String) category.getSelectedItem();

                    String query = "INSERT INTO expenseTracker(method,category,expense,message) " +
                            "VALUES(\'" +  methodOfTransction + "\',\'" + categoryOfExpense + "\',\'" + inputValue + "\',\'"
                            + reasonOfTransaction + "\')";

                    inputField.setText("0");
                    transaction_reason.setText("");

                    try {
                        statement.executeUpdate(query);
                        System.out.println("Successfully added to db");
                        successMessage.setText("Transaction successfully added");
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    }
                }


            }catch (Exception exception){
                System.out.println(exception);
            }
        }

        if(e.getSource() == addExpense){
            visibility();
            transaction_area.setVisible(true);
        }

        if(e.getSource() == spentMoney){
            String query = "SELECT * FROM expensetracker WHERE method='WITHDRAW'";
            try {
                String output = "";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    String ans = "\n " +  resultSet.getString(2) + "  -->  " +
                            resultSet.getString(3) + " -->" +
                            resultSet.getDouble(4) + "  ---->  " +
                            resultSet.getString(5) + "\n";
                    output = ans + output;
                }

                visibility();
                scrollPane_ADD.setVisible(true);
                output = "TRANSACTIONS ARRANGED BASED ON RECENT TRANSACTIONS\n" + output ;
                displayTransactions.setText(output);

            } catch (SQLException ex) {
                System.out.println("ERROR in selecting all Spending's");
            }
        }

        if(e.getSource() == depositedMoney){
            String query = "SELECT * FROM expensetracker WHERE method='DEPOSIT'";
            try {
                String output = "";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    String ans = "\n " +  resultSet.getString(2) + "  -->  " +
                            resultSet.getString(3) + " -->" +
                            resultSet.getDouble(4) + "  ---->  " +
                            resultSet.getString(5) + "\n";
                        output = ans + output;
                }

                visibility();
                scrollPane_ADD.setVisible(true);
                output = "TRANSACTIONS ARRANGED BASED ON RECENT TRANSACTIONS\n" + output ;
                displayTransactions.setText(output);

            } catch (SQLException ex) {
                System.out.println("ERROR in selecting all Deposits");
            }
        }

        if(e.getSource() == allExpenses){
            String query = "SELECT * FROM expensetracker";
            try {
                String output = "";
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    String ans = "\n " +  resultSet.getString(2) + "  -->  " +
                            resultSet.getString(3) + " -->" +
                            resultSet.getDouble(4) + "  ---->  " +
                            resultSet.getString(5) + "\n";
                    output = ans + output;
                }

                visibility();
                scrollPane_ADD.setVisible(true);
                output = "TRANSACTIONS ARRANGED BASED ON RECENT TRANSACTIONS\n" + output ;
                displayTransactions.setText(output);

            } catch (SQLException ex) {
                System.out.println("ERROR in selecting all transactions");
            }
        }

        if (e.getSource() == analysis){
            String query = "SELECT * FROM expensetracker";
            try {
                String output = "";
                double spent=0,deposited=0,total=0;
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()){
                    if(resultSet.getString(2).equals("DEPOSIT")){
                        deposited += resultSet.getDouble(4);
                    }else{
                        spent += resultSet.getDouble(4);
                    }
                }

                total = deposited - spent;
                String message = "";

                if(total > 0){
                    message = "You only have \u20B9" + total + " remaining in your Savings.";
                }else{
                    message = "You have a debt of \u20B9" + total ;
                }

                output = "\n Money Deposited\t=   \u20B9" + deposited +
                            "\n\n Money Spent\t\t=   \u20B9" + spent +
                            "\n\n Total Money Remaining\t=   \u20B9" + total +
                            "\n\n\n\n " + message;


                visibility();
                scrollPane_ADD.setVisible(true);
                displayTransactions.setText(output);

            } catch (SQLException ex) {
                System.out.println("ERROR in selecting all transactions");
            }
        }

    }

    public static void main(String[] args) {
        try {
            new ExpenseTracker();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}