package bankaccounts;

public class SavingsAccount extends Account {

    public SavingsAccount(){
        super(); //uses no-arg constructor of the super class
    }

    public SavingsAccount(Depositor dp, int an, String tp, double ba, String s){
        super(dp,an,tp,ba,s); //uses parameterized constructor of the super class
    }

    public SavingsAccount(Account savings){
        super(savings);
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
