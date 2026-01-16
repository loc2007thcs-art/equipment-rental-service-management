package Entity;
public class Account {
    private String userName;
    private String passWord;
    private String FullName;
     public Account(){
         
     }    
    public Account(String fullName, String userName, String passWord) {
        this.FullName = fullName;
        this.userName = userName;
        this.passWord = passWord;
    }
    public String getUserName() {
        return userName; 
    }
    public void setUserName(String userName) { 
        this.userName = userName; 
    }
    public String getPassword() {
        return passWord; 
    }
    public void setPassword(String passWord) { 
        this.passWord = passWord; 
    }   
    public String getFullName(){
        return FullName;
    }
    public void setFullName(String FullName){
        this.FullName = FullName;
    }
}
