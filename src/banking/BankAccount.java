package banking;

// Abstraction: this abstract class defines common account data and behaviour.
public abstract class BankAccount implements Loanable {

    // Encapsulation: fields are private and accessed through methods.
    private String accountNumber;
    private String accountHolder;
    private double balance;

    public BankAccount(String accountNumber, String accountHolder, double openingBalance) {
        if (openingBalance < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }

        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = openingBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public double getBalance() {
        return balance;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }


    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public abstract double calculateInterest();

    public abstract String getAccountType();

    public abstract double getMaximumLoan();

    public void displayAccountInfo() {
        System.out.println("                                   ");
        System.out.println("Account Type   : " + getAccountType());
        System.out.println("Account Number : " + accountNumber);
        System.out.println("Account Holder : " + accountHolder);
        System.out.printf("Balance        : M%.2f%n", balance);
        System.out.println("                                ");
    }
}
