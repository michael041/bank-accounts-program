package bankaccounts;

import java.util.ArrayList;

public class Bank {
    private ArrayList<Account> accounts;
    private static double totalamountinsavingsaccts = 0.0;
    private static double totalamountincheckingsaccts = 0.0;
    private static double totalamountincdaccts = 0.0;
    private static double totalamountinallaccts = 0.0;

    //no-arg constructor
    public Bank() {
        accounts = new ArrayList<>();
    }

    public void setAcct(Account acct) {
        accounts.add(acct);
    }

    public Account getAcct(int index) {
        if("CD".equals(accounts.get(index).getType())){
            return new CDAccount(accounts.get(index));
    }
       else if("Checking".equals(accounts.get(index).getType())){
            return new CheckingAccount(accounts.get(index));
        }
        else{
            return new SavingsAccount(accounts.get(index));
        }
    }

    public int findAcct(int acctnumber) {
        for (int count = 0; count < accounts.size(); count++) {
            if (accounts.get(count).equals(acctnumber)) {
                return count;
            }
        }
        return -1;
    }

    public void setRevisedAcct(int index, Account acctCopy){
        accounts.set(index,acctCopy);
    }

    public int findssn(int ssn) {
        for (int count = 0; count < accounts.size(); count++) {
            if (accounts.get(count).getDepositor().equals(ssn)) {
                return count;
            }
        }
        return -1;
    }

    public void deleteAccount ( int index){
        accounts.remove(index);
    }

    public static void addall ( double a){
        totalamountinallaccts += a;
    }

    public static void addsavings ( double s){
        totalamountinsavingsaccts += s;
    }

    public static void addcheckings ( double c){
        totalamountincheckingsaccts += c;
    }

    public static void addcd ( double cd){
        totalamountincdaccts += cd;
    }

    public static void minusall ( double all){
        totalamountinallaccts -= all;
    }

    public static void minussavings ( double ss){
        totalamountinsavingsaccts -= ss;
    }

    public static void minuscheckings ( double cc){
        totalamountincheckingsaccts -= cc;
    }

    public static void minuscd ( double cdm){
        totalamountincdaccts -= cdm;
    }

    public static double getall () {
        return totalamountinallaccts;
    }

    public static double getsavings () {
        return totalamountinsavingsaccts;
    }

    public static double getcheckings () {
        return totalamountincheckingsaccts;
    }

    public static double getcd () {
        return totalamountincdaccts;
    }

    public int getSize(){
        return accounts.size();
    }
}
