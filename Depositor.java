package bankaccounts;

public class Depositor {
    private int snumber;
    private Name name;

    //no-arg constructor
    public Depositor() {
        name = new Name();
        snumber = 0;
    }

    //parameterized constructor
    public Depositor(Name nm, int s) {
        snumber = s;
        name = nm;
    }

    //copy constructor
    public Depositor(Depositor myDepositor) {
        snumber = myDepositor.snumber;
        name = new Name(myDepositor.name);
    }

    public void setName(Name n) {
        name = n;
    }

    public Name getName() {
        Name namecopy=new Name(name);
        return namecopy;
    }

    public void setSSNum(int so) {
        snumber = so;
    }

    public int getsoNum() {
        return snumber;
    }

    public String toString(){
        String str=String.format("Owner of account: %s%nSocial security number: %d",name,snumber);
        return str;
    }

    public boolean equals(int securityNumber){
        if(snumber==securityNumber){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean equals(Name myname){
        if(name.equals(myname)){
            return true;
        }
        else{
            return false;
        }
    }
}
