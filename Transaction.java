package bankaccounts;

public class Transaction {
    String type;
    double amount;

    //no-arg constructor
    public Transaction() {
        type = "";
        amount = 0;
    }

    //parameterized constructor
    public Transaction(String t, double a) {
        type = t;
        amount = a;
    }

    //copy constructor
    public Transaction(Transaction transactionCopy) {
        type = transactionCopy.type;
        amount = transactionCopy.amount;
    }

    public void setTransaction(String tp) {
        type = tp;
    }

    public void setAmount(double ammt) {
        amount = ammt;
    }

    public String getTransaction() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String toString() {
        String str = String.format("Transaction type: %s%nAmount: %.2f%n", type, amount);
        return str;
    }
}
