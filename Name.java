package bankaccounts;

public class Name {
    private String first;
    private String last;

    //no-arg constructor
    public Name() {
        first = "";
        last = "";
    }

    //parameterized constructor
    public Name(String f, String l) {
        first = f;
        last = l;
    }

    //copy constructor
    public Name(Name namecopy) {
        last = namecopy.last;
        first = namecopy.first;
    }

    public void setFirst(String f) {
        first = f;
    }

    public void setLast(String l) {
        last = l;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String toString(){
        String str= String.format("%s %s",first,last);
        return str;
    }

    public boolean equals(Name myName){
        if(first.equals(myName.first)&&last.equals(myName.last)){
            return true;
        }
        else{
            return false;
        }
    }
}
