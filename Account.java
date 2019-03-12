package bankaccounts;
import java.util.ArrayList;

public class Account {
    private Depositor dep;
    private int acnum;
    private String type;
    private double balance;
    private String status;
    private ArrayList<Transaction> transactions;

    //no-arg constructor
    public Account() {
        dep = new Depositor();
        acnum= 0;
        type = "";
        balance = 0;
        status = "";
        transactions = new ArrayList<>();
    }

    //parameterized constructor
    public Account(Depositor dp, int an, String tp, double ba, String s) {
        dep = dp;
        acnum = an;
        type = tp;
        balance = ba;
        status = s;
        transactions = new ArrayList<>();
    }

    //copy constructor
    public Account(Account acctcopy){
        status = acctcopy.status;
        dep = new Depositor(acctcopy.dep);
        acnum = acctcopy.acnum;
        type = acctcopy.type;
        balance = acctcopy.balance;
        transactions= new ArrayList<>(acctcopy.transactions);
    }

    public void setAcnum(int a) {
        acnum = a;
    }

    public void setType(String t) {
        type = t;
    }

    public void setBalance(double b) {
        balance = b;
    }

    public void setDepositor(Depositor d) {
        dep = d;
    }

    public int getAcnum() {
        return acnum;
    }

    public String getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public Depositor getDepositor() {
        Depositor depositorcopy=new Depositor(dep);
        return depositorcopy;
    }

    public void setStatus(String s) {
        status = s;
        System.out.println(status);
    }

    public String getStatus() {
        return status;
    }

    public int withdraws(double withdrawAmount) {
        if (withdrawAmount <= 0) {
            return 0;
        } else if (withdrawAmount > balance) {
            return 1;
        } else {
            balance -= withdrawAmount;
        }
        return 2;
    }

    public int deposits(double amountDeposit) {
        if (amountDeposit <= 0) {
            return 0;
        } else {
            balance += amountDeposit;
            return 1;
        }
    }

    public void addTran(Transaction tr) {
        transactions.add(tr);
    }

    public Transaction getTran(int index) {
        return new Transaction(transactions.get(index));
    }

    public int getSize() {
        return transactions.size();
    }

    public boolean equals(int accountNumber){
        if (this.acnum == accountNumber){
            return true;
        }else{
            return false;
        }
    }

    public String toString(){
        String name = String.format("%s %s",dep.getName().getFirst(),dep.getName().getLast());
        String money = String.format("$%.2f", balance);
        String str=String.format("%-17s%21d%17d%15s%11s%9s",name,dep.getsoNum(),acnum,type,money,status);
        return str;
    }
}
