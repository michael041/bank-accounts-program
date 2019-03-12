package bankaccounts;

public class CDAccount extends Account {

    public CDAccount(){
        super(); //uses no-arg constructor of the super class
    }

    public CDAccount(Depositor dp, int an, String tp, double ba, String s){
        super(dp,an,tp,ba,s); //uses parameterized constructor of the super class
    }


    public CDAccount(Account acct){
        super(acct);
    }

    public int withdraws(double withdrawAmount) {
        if (withdrawAmount <= 0) {
            return 0;
        } else if (withdrawAmount > getBalance()) {
            return 1;
        } else {
            setBalance(getBalance()-withdrawAmount);
        }
        return 2;
    }

    public int deposits(double amountDeposit) {
        if (amountDeposit <= getBalance()) {
            return 0;
        } else {
            setBalance(getBalance()+amountDeposit);
            return 1;
        }
    }

}
