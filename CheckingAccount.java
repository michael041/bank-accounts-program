package bankaccounts;

public class CheckingAccount extends Account{

    public CheckingAccount(){
        super(); //uses no-arg constructor of the super class
    }

    public CheckingAccount(Depositor dp, int an, String tp, double ba, String s){
        super(dp,an,tp,ba,s);//uses parameterized constructor of the super class
    }

    public CheckingAccount(Account checking)
    {
        super(checking);
    }

     public int withdraws(double withdrawAmount) {
        if (withdrawAmount <= 0) {
            return 0;
        } else if (withdrawAmount > getBalance()) {
            return 1;
        } else {
            if(getBalance()<2500){
                withdrawAmount+=1.50;
                setBalance(getBalance()-withdrawAmount);
                System.out.println(withdrawAmount);
            } else{
                setBalance(getBalance()-withdrawAmount);
            }
            return 2;
        }
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
