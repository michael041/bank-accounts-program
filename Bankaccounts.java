package bankaccounts;
import java.util.Scanner;
import java.io.File;
import java.io.PrintStream;

public class Bankaccounts {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("bankdata.txt"));
        Scanner sc2 = new Scanner(new File("bankdatamenu.txt"));
        PrintStream ps = new PrintStream("output.txt");
        ps.println();

        Bank base = new Bank();
        boolean notDone = true; //loop control
        readAccts(base, sc); //reads in the accounts
        printAccts(base, ps); //prints database
        do {
            menu();
            char choice = sc2.next().charAt(0);
            switch (choice) {
                case 'q':
                case 'Q':
                    printAccts(base, ps);
                    printHistory(base, ps);
                    notDone = false;
                    break;
                case 'b':
                case 'B':
                    balance(base, sc2, ps);
                    System.out.println();
                    break;
                case 'd':
                case 'D':
                    deposit(base, sc2, ps);
                    System.out.println();
                    break;
                case 'w':
                case 'W':
                    withdrawal(base, sc2, ps);
                    System.out.println();
                    break;
                case 'n':
                case 'N':
                    newAcct(base, sc2, ps);
                    System.out.println();
                    break;
                case 'i':
                case 'I':
                    accountInfo(base, ps, sc2);
                    break;
                case 'h':
                case 'H':
                    infoHistory(base, sc2, ps);
                    break;
                case 'c':
                case 'C':
                    closeAcct(base, sc2, ps);
                    break;
                case 'r':
                case 'R':
                    openAcct(base, sc2, ps);
                    break;
                case 'x':
                case 'X':
                    deleteAcct(base, sc2, ps);
                    System.out.println();
                    break;
                default:
                    ps.println("Error: " + choice + " is an invalid"
                            + " selection. Try again.");
                    ps.println();
                    break;
            }
        } while (notDone);
    }

    /* Method menu()
     *  Prints the menu of transaction choices
     */
    public static void menu() {
        System.out.println();
        System.out.println("Select one of the following transactions:");
        System.out.println("\t****************************");
        System.out.println("\t    List of Choices         ");
        System.out.println("\t****************************");
        System.out.println("\t     W -- Withdrawal");
        System.out.println("\t     D -- Deposit");
        System.out.println("\t     N -- New Account");
        System.out.println("\t     B -- Balance Inquiry");
        System.out.println("\t     C -- Close Account");
        System.out.println("\t     R -- Re-open Account");
        System.out.println("\t     H -- Account Info With Transaction History");
        System.out.println("\t     I -- Account Info");
        System.out.println("\t     X -- Delete Account");
        System.out.println("\t     Q -- Quit");
        System.out.println();
        System.out.println("Enter your selection: ");
        System.out.println();
    }

    /* Method readAccts()
     *  Reads the initial database of accounts and balances
     */
    public static void readAccts(Bank base, Scanner sc) throws Exception {
        String line;
        while (sc.hasNext()) {
            line = sc.nextLine();
            String[] tokens = line.split(" ");
            Name n = new Name(tokens[0], tokens[1]);
            Depositor d = new Depositor(n, Integer.parseInt(tokens[2]));

            if ("Savings".equals(tokens[4])){
                Account savings =new SavingsAccount(d, Integer.parseInt(tokens[3]), tokens[4], Double.parseDouble(tokens[5]),
                        tokens[6]);
                base.setAcct(savings);
                Bank.addsavings(savings.getBalance());
                Bank.addall(savings.getBalance());

            }
            if ("Checking".equals(tokens[4])){
                Account checking =new CheckingAccount(d, Integer.parseInt(tokens[3]), tokens[4], Double.parseDouble(tokens[5]),
                        tokens[6]);
                System.out.println(checking instanceof CheckingAccount);
                base.setAcct(checking);
                Bank.addcheckings(checking.getBalance());
                Bank.addall(checking.getBalance());

            }
            if ("CD".equals(tokens[4])){
                Account cd =new CDAccount(d, Integer.parseInt(tokens[3]), tokens[4], Double.parseDouble(tokens[5]),
                        tokens[6]);
                base.setAcct(cd);
                Bank.addcd(cd.getBalance());
                Bank.addall(cd.getBalance());
            }

        }
    }

    /* Method withdrawal:
     *  Prompts the use to enter an accont number.
     *  Calls method findAcct of the Bank class to see if the account exists
     *  If the account exists, prompts for the amount to withdrawal
     *  If the amount is valid, it makes the withdrawal and prints the new
     *  balance otherwise, an error message is printed. If the account is closed
     *  the user is notified and the funds remain untouched.
     */
    public static void withdrawal(Bank base, Scanner sc, PrintStream ps)
            throws Exception {
        System.out.println("Enter the account number:");
        int account = sc.nextInt();
        int index = base.findAcct(account);
        System.out.println(index);

        if (index == -1) { //invalid account
            ps.println("Transaction requested: Withdrawal");
            ps.println("Error: Account " + account + " does not exist");
            ps.println();
            ps.println();
        } else{
            Account tempAcct=base.getAcct(index);
            if ("Closed".equals(tempAcct.getStatus())) {
                ps.println("Transaction requested: withdrawal");
                ps.println("Error: Account " + account + " is closed. Reopen the account to perform transactions.");
                ps.println();
                ps.println();
                Transaction trans = new Transaction("Withdrawal", 0);
                tempAcct.addTran(trans);
            } else {
                double old = tempAcct.getBalance();
                System.out.println("Enter withdrawal amount:");
                double minus = sc.nextDouble();
                int decide = tempAcct.withdraws(minus);
                if (decide == 0) {
                    ps.println("Transaction requested: Withdrawal");
                    ps.println("Account: " + account);
                    ps.printf("Error: %.2f is an invalid amount%n", minus);
                    ps.println();
                    ps.println();
                    Transaction trans = new Transaction("Withdrawal", minus);
                    tempAcct.addTran(trans);
                    base.setRevisedAcct(index,tempAcct);
                } else if (decide == 1) {
                    ps.println("Transaction requested: Withdrawal");
                    ps.println("Account: " + tempAcct.getAcnum());
                    ps.printf("Error: Insufficient funds to withdraw $%.2f from%n", minus);
                    ps.printf("Current balance: $%.2f%n", tempAcct.getBalance());
                    ps.println();
                    ps.println();
                    Transaction trans = new Transaction("Withdrawal", minus);
                    tempAcct.addTran(trans);
                    base.setRevisedAcct(index,tempAcct);
                } else if (decide == 2) { //sufficient funds
                        ps.println("Transaction requested: Withdrawal");
                        ps.println("Account: " + tempAcct.getAcnum());
                        ps.printf("Amount to withdraw: $%.2f%n", minus);
                        if(tempAcct.getBalance()<2500 && "Checking".equals(tempAcct.getType())) {
                            ps.printf("Withdrawal fee: $1.50%n");
                        }
                        ps.printf("Old balance: $%.2f%n", old);
                        ps.printf("New balance: $%.2f%n", tempAcct.getBalance());
                        ps.println();
                        ps.println();
                        Transaction trans = new Transaction("Withdrawal", minus);
                        tempAcct.addTran(trans);
                        base.setRevisedAcct(index,tempAcct);
                        if ("CD".equals(tempAcct.getType())) {
                            Bank.minuscd(minus);
                            Bank.minusall(minus);
                        }
                        if ("Checking".equals(tempAcct.getType())) {
                            if(tempAcct.getBalance()<2500) {
                                Bank.minuscheckings((minus + 1.50));
                                Bank.minusall((minus + 1.50));
                            }
                            else{
                                Bank.minuscheckings(minus);
                                Bank.minusall(minus);
                            }
                        }
                        if ("Savings".equals(tempAcct.getType())) {
                            Bank.minussavings(minus);
                            Bank.minusall(minus);
                        }

                    }
                }
            }
        }

    /* Method deposit:
     *  Prompts the user to enter an account number.
     *  Calls findAcct method of the Bank class to see if the account exists.
     *  If the account exists, the user is asked for the amount to deposit
     *  If the amount is valid, it makes the deposit and prints the new
     *  balance Otherwise, an error message is printed. if the account is closed
     *  the user is notified and the funds remain untouched.
     */
    public static void deposit(Bank base, Scanner sc, PrintStream ps)
            throws Exception {
        System.out.println("Enter the account number:");
        int account = sc.nextInt();
        int index = base.findAcct(account);
        if (index == -1) { //invalid account
            ps.println("Transaction requested: Deposit");
            ps.println("Error: Account " + account + " does not exist");
            ps.println();
            ps.println();
        } else {
            Account tempAcct=base.getAcct(index);
            if ("Closed".equals(tempAcct.getStatus())) {
                //valid account, closed
                ps.println("Transaction requested: Deposit");
                ps.println("Error: Account " + account + " is closed. Reopen the account to perform transactions.");
                ps.println();
                ps.println();
                Transaction trans = new Transaction("Deposit", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            } else { //valid account, open
                double old = tempAcct.getBalance();
                System.out.println("Enter deposit amount:");
                double plus = sc.nextDouble(); //read in deposit amount
                int decide = tempAcct.deposits(plus);
                if (decide == 0) { //invalid deposit amount
                    ps.println("Transaction requested: Deposit");
                    ps.println("Account: " + account);
                    ps.printf("Error: %.2f is an invalid amount%n", plus);
                    ps.println();
                    ps.println();
                    Transaction trans = new Transaction("Deposit", plus);
                    tempAcct.addTran(trans);
                    base.setRevisedAcct(index,tempAcct);
                } else if (decide == 1) { //valid deposit amount
                    ps.println("Transaction requested: Deposit");
                    ps.println("Account: " + tempAcct.getAcnum());
                    ps.printf("Amount to deposit: $%.2f%n", plus);
                    ps.printf("Old balance: $%.2f%n", old);
                    ps.printf("New balance: $%.2f%n%n%n", tempAcct.getBalance());
                    Transaction trans = new Transaction("Deposit", plus);
                    tempAcct.addTran(trans);
                    Bank.addall(plus);
                    if ("CD".equals(tempAcct.getType())) {
                        Bank.addcd(plus);
                    }
                    if ("Checking".equals(tempAcct.getType())) {
                        Bank.addcheckings(plus);
                    }
                    if ("Savings".equals(tempAcct.getType())) {
                        Bank.addsavings(plus);
                    }
                    base.setRevisedAcct(index,tempAcct);
                }
            }
        }
    }

    /* Method balance:
     *  Prompts the user to enter an account number
     *  Calls the findAcct method of the Bank to see if the account exists
     *  If the account exists, the balance is printed.
     *  Otherwise, an error message is printed. if the account is closed then
     *  the user is notified.
     */
    public static void balance(Bank base, Scanner sc, PrintStream ps)
            throws Exception {
        //prompt user for account number
        System.out.println("Enter the account number:");
        int account = sc.nextInt(); //read in account number
        //calls to findAcct to see if the account exists
        int index = base.findAcct(account);

        if (index == -1) { //invalid account
            ps.println("Transaction requested: Balance inquiry");
            ps.println("Error: Account " + account + " does not exist");
            ps.println();
            ps.println();
        } else {
            Account tempAcct = base.getAcct(index);
            if ("Closed".equals(tempAcct.getStatus())) {
                //valid account,closed
                ps.println("Transaction requested: Account balance");
                ps.println("Error: Account " + account + " is closed. Reopen the account to perform transactions.");
                ps.println();
                ps.println();
                Transaction trans = new Transaction("Account balance", 0);
                tempAcct.addTran(trans);
            } else { //valid account
                ps.println("Transaction requested: Balance inquiry");
                ps.println("Account Number: " + account);
                ps.println("Current balance: $" + tempAcct.getBalance());
                ps.printf("%n%n");
                Transaction trans = new Transaction("Balance", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            }
        }
    }

    /* Method accountInfo:
     *  Prompts the user for a social security number and then calls the
     *  findssn method of the Bank class to see if the social security number
     *  is associated with an account. if it is, then it gives the user
     *  the information of the account. if it isn't, it gives an error message.
     *  if the account is closed the user is notified.
     */
    public static void accountInfo(Bank base, PrintStream ps, Scanner sc)
            throws Exception {
        System.out.println("Enter your social security number");
        int sonum = sc.nextInt();
        int index = base.findssn(sonum);

        if (index == -1) { //invalid social security number
            ps.println("Transaction requested: Account information");
            ps.println("Error: Social security number " + sonum + " doesn't have a bank account");
            ps.println();
            ps.println();
        } else {
            Account tempAcct = base.getAcct(index);
            if ("Closed".equals(tempAcct.getStatus())) { //valid social security number,closed
                ps.println("Transaction requested: Account info");
                ps.println("Error: Account " + base.getAcct(index).getAcnum() + " is closed. Reopen the account " +
                        "to perform transactions.");
                ps.println();
                ps.println();
                Transaction trans = new Transaction("Account info", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            } else { //valid social security number, open
                ps.println("Transaction requested: Account information");
                ps.println("Account information");
                ps.println("Account number: " + tempAcct.getAcnum());
                ps.println(tempAcct.getDepositor().toString());
                ps.println("Account type: " + tempAcct.getType());
                ps.printf("Balance: $%.2f%n%n%n", tempAcct.getBalance());
                Transaction trans = new Transaction("Account info", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            }
        }
    }

    /* Method deleteAcct:
     *  Prompts the user to enter an account number.
     *  Calls the findAcct method of the Bank class to see if the account
     *  exists. if the entered account number exists, it deletes the account. 
     *  If the entered account number exists and has a non zero balance, 
     *  the user is told to withdraw the remaining balance before deleting the
     *  account. otherwise, if a non existent account number is entered, 
     *  an error message is printed. If the account is closed the user is notified.
     */
    public static void deleteAcct(Bank base, Scanner sc, PrintStream ps) throws Exception {
        //prompts user for account number
        System.out.println("Enter the number of the account "
                + "that you want to delete:");
        int account = sc.nextInt(); //read in account number

        if ((base.findAcct(account) == -1)) {//invalid account
            ps.println("Transaction requested: Account deletion");
            ps.println("Error: Account " + account + " does not exist");
            ps.println();
            ps.println();
        }
        if (base.findAcct(account) != -1) { //valid account
            int index = base.findAcct(account);
            Account tempAcct=base.getAcct(index);
            if (tempAcct.getStatus().equals("Closed")) { //valid account,closed
                ps.println("Transaction requested: Account deletion");
                ps.println("Error: Account " + account + " Reopen the account to perform transactions.");
                ps.println();
                ps.println();
                Transaction trans = new Transaction("Delete account", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            }
            else if (tempAcct.getBalance() == 0 && tempAcct.getAcnum() != 0) {
                Bank.minusall(tempAcct.getBalance());
                if ("CD".equals(tempAcct.getType())) {
                    Bank.minuscd(tempAcct.getBalance());
                }
                if ("Checking".equals(tempAcct.getType())) {
                    Bank.minuscheckings(tempAcct.getBalance());
                }
                if ("Savings".equals(tempAcct.getType())) {
                    Bank.minussavings(tempAcct.getBalance());
                }
                base.deleteAccount(index);
                ps.println("Transaction requested: Account deletion");
                ps.println("Deleted account: " + account);
                ps.println();
                ps.println();
            } else { //valid account, non-zero balance
                ps.println("Transaction requested: Account deletion");
                ps.printf("Account %d has a non zero balance of $%.2f.%nThe remaining balance must be withdrawn" +
                        " for the account to be deleted.%n%n%n", account, tempAcct.getBalance());
                Transaction trans = new Transaction("Delete account", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            }
        }
    }

    /* Method newAcct:
     *  Prompts for a new account number within a specified valid range
     *  Calls findAcct method of the Bank class to see if the
     *  account exists and receives the index of the account.
     *  if the entered account number is a valid non existent
     *  number, it creates a new account with the number and assigns the
     *  account a balance of zero dollars and zero cents. Otherwise,
     *  an error message is printed.
     */
    public static void newAcct(Bank base, Scanner sc, PrintStream ps) throws Exception {
        //prompt for new account number
        System.out.println("Enter a new, six digit account number within"
                + " the range of 100000 and 999999:");
        int newAcnt = sc.nextInt(); //read in account number
        int spot = base.findAcct(newAcnt);

        if (newAcnt < 100000 || newAcnt > 999999) { //valid account number
            ps.println("Transaction requested: Account creation");
            ps.println("Error: " + newAcnt + " is an invalid account number");
            ps.println();
            ps.println();
        } else {
            if (spot != -1) { //account number already exists
                ps.println("Transaction requested: Account creation");
                ps.println("Error: Account " + newAcnt + " already exists");
                ps.println();
                ps.println();
            } else { //valid, non existent account number
                ps.println(spot);
                System.out.println("Enter your first name");
                String first = sc.next();
                System.out.println("Enter your last name");
                String last = sc.next();
                Name nm = new Name(first, last);

                System.out.println("Enter your Social security number");
                int social = sc.nextInt();
                Depositor dp = new Depositor(nm, social);

                System.out.println("enter your account type");
                String type = sc.next();
                System.out.println("Enter your beginning balance");
                double balances = sc.nextDouble();

                if("Savings".equals(type)){
                    SavingsAccount savings = new SavingsAccount(dp, newAcnt, type, balances, "Open");
                    if (savings.getDepositor().getName().equals(nm)){
                        base.setAcct(savings); //sets the new account to an array slot
                        ps.println("Transaction requested: Account creation");
                        ps.println("New account: " + newAcnt);
                        ps.println("Account owner: " + savings.getDepositor().getName().toString());
                        ps.println("account type: " + type);
                        ps.printf("Balance: $%.2f%n%n%n", balances);
                        Bank.addsavings(balances);
                        Bank.addall(balances);
                    }

                }
                if("Checking".equals(type)){
                    SavingsAccount checking= new SavingsAccount(dp, newAcnt, type, balances, "Open");
                    if (checking.getDepositor().getName().equals(nm)){
                        base.setAcct(checking); //sets the new account to an array slot
                        ps.println("Transaction requested: Account creation");
                        ps.println("New account: " + newAcnt);
                        ps.println("Account owner: " + checking.getDepositor().getName().toString());
                        ps.println("account type: " + type);
                        ps.printf("Balance: $%.2f%n%n%n", balances);
                        Bank.addcheckings(balances);
                        Bank.addall(balances);
                    }

                }
                if("CD".equals(type)){
                    SavingsAccount cd= new SavingsAccount(dp, newAcnt, type, balances, "Open");
                    if (cd.getDepositor().getName().equals(nm)){
                        base.setAcct(cd); //sets the new account to an array slot
                        ps.println("Transaction requested: Account creation");
                        ps.println("New account: " + newAcnt);
                        ps.println("Account owner: " + cd.getDepositor().getName().toString());
                        ps.println("account type: " + type);
                        ps.printf("Balance: $%.2f%n%n%n", balances);
                        Bank.addcd(balances);
                        Bank.addall(balances);
                    }
                }
            }
        }
    }

    /* Method closeAcct:
     *  Prompts the user for an account number and then calls to a method to
     *  to see if the account exists. if it does exists, it then calls to
     *  method closeIt of the Account class which simply changes 
     *  the accounts status to closed.
     */
    public static void closeAcct(Bank base, Scanner sc, PrintStream ps)
            throws Exception {
        System.out.println("Enter the number of the account that you want to close");
        int account = sc.nextInt();
        int index = base.findAcct(account);
        if (index == -1) { //account number not found
            ps.println("Transaction requested: Account close");
            ps.println("Error: Account " + account + " doesn't exist");
            ps.printf("%n%n");
        } else {
            Account tempAcct = base.getAcct(index);
            tempAcct.setStatus("Closed");
            ps.println("Transaction requested: Account close");
            ps.println("Account closed: " + account);
            ps.println();
            ps.println();
            Transaction trans = new Transaction("Close account", 0);
            tempAcct.addTran(trans);
            base.setRevisedAcct(index, tempAcct);
        }
    }

    /* Method openAcct:
     *  Prompts the user for an account number and then calls to a method to
     *  to see if the account exists. if it does exists, it then calls to
     *  method openIt of the Account class which simply changes the accounts status to closed.
     */
    public static void openAcct(Bank base, Scanner sc, PrintStream ps)
            throws Exception {
        System.out.println("Enter the number of the account that "
                + "you want to re-open");
        int account = sc.nextInt();
        int index = base.findAcct(account);
        if (index == -1) { //account number not found
            ps.println("Transaction requested: Reopen account");
            ps.println("Error: Account " + account + " doesn't exist");
            ps.printf("%n%n");
        } else {
            Account tempAcct = base.getAcct(index);
            tempAcct.setStatus("Open");
            ps.println("Transaction requested: Reopen account");
            ps.println("Re-opened account: " + account);
            ps.println();
            ps.println();
            Transaction trans = new Transaction("Reopen account", 0);
            base.getAcct(index).addTran(trans);
            tempAcct.addTran(trans);
            base.setRevisedAcct(index, tempAcct);
        }
    }

    /* Method infoHistory:
     *  Prompts the user for a social security number and then calls the
     *  findssn method of the Bank class to see if the social security number
     *  is associated with an account. if it is, then it gives the user
     *  the information of the account along with the account's transaction
     *  history. if it isn't, it gives an error message. if the account is closed
     *  the user is notified.
     */
    public static void infoHistory(Bank base, Scanner sc, PrintStream ps)
            throws Exception {
        System.out.println("Enter your social security number");
        int sonum = sc.nextInt();
        int index = base.findssn(sonum);
        if (index == -1) { //invalid social security number
            ps.println("Transaction requested: Account information with transaction history");
            ps.println("Error: Social security number " + sonum + " doesn't have a bank account");
            ps.println();
            ps.println();
        } else {
            Account tempAcct=base.getAcct(index);
            if ("Closed".equals(tempAcct.getStatus())) { //valid number, but closed account
                ps.println("Transaction requested: Account info with history");
                ps.println("Error: Account " + tempAcct.getAcnum() + " is closed. Reopen the account to perform " +
                        "transactions.");
                ps.println();
                ps.println();
                Transaction trans = new Transaction("Account info with transaction history", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            } else { //valid social security number,open account
                ps.println("Transaction requested: Account information with" + " transaction history");
                ps.println("Account information");
                ps.println("Account number: " + tempAcct.getAcnum());
                ps.println(tempAcct.getDepositor().toString());
                ps.println("Account type: " + tempAcct.getType());
                ps.printf("Balance: $%.2f%n", tempAcct.getBalance());
                ps.println("Transaction history:");
                for (int x = 0; x < tempAcct.getSize(); x++) { ps.println("Transaction type: " + tempAcct.getTran(x)
                        .getTransaction());
                    ps.println("Amount: " + tempAcct.getTran(x).getAmount());
                }
                ps.println();
                ps.println();
                Transaction trans = new Transaction("Account info"
                        + " with transaction history", 0);
                tempAcct.addTran(trans);
                base.setRevisedAcct(index,tempAcct);
            }
        }
    }

    /* Method printAccts:
     *  Prints the database of accounts and balances
     */
    public static void printAccts(Bank base, PrintStream ps) throws Exception {
        Account account;
        ps.println("Database of bank accounts");
        ps.println();
        ps.println("Account holder   Social security number   Account number   Account type   balance   Status");
        for (int count = 0; count < base.getSize(); count++) {
            account=(base.getAcct(count));
            ps.println(account.toString());
        }
        ps.println();
        ps.printf("Total balance of checking accounts: $%.2f%n", Bank.getcheckings());
        ps.printf("Total balance of savings accounts: $%.2f%n", Bank.getsavings());
        ps.printf("Total balance of CD accounts: $%.2f%n", Bank.getcd());
        ps.printf("Total balance of all accounts: $%.2f%n%n%n", Bank.getall());
    }

    /* Method printHistory:
     *  Prints the transaction history of each account in the database
     */
    public static void printHistory(Bank base, PrintStream ps) throws Exception {
        ps.println("Transaction History");
        ps.println();
        for (int count = 0; count < base.getSize(); count++) {
            ps.println("Transaction history for account: " + base.getAcct(count).getAcnum());
            for (int x = 0; x < base.getAcct(count).getSize(); x++) {
                ps.println(base.getAcct(count).getTran(x).toString());
            }
            ps.println();
        }
    }
}
