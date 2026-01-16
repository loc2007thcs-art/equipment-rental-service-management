package DAO;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.Connection;
public class Database {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            String[] params = docFileText();
            String severName = params[0];
            String dbName = params[1];
            String userName = params[2];
            String passWord = params[3];
            String url = "jdbc:mysql://" + severName + ":3306/" + dbName + "?useUnicode=true&characterEncoding=UTF-8";
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, userName, passWord);
            System.out.println("Ket noi CSDL thanh cong!");
        } 
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ket noi that bai! Vui long kiem tra lai ConnectVariable.txt");
        }
        return conn;
	}
         
        public static String[] docFileText() {
            String[] params = new String[4];
            try {
                FileInputStream fis = new FileInputStream("ConnectVariable.txt");
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                params[0] = br.readLine();
                params[1] = br.readLine();
                params[2] = br.readLine();
                params[3] = br.readLine();
                if (params[3] == null) {
                    params[3] = "";
                }
                br.close();
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
            return params;
        }
        public static void closeConnection(Connection conn) {
            try {
                if(conn != null) {
                    conn.close();
                }
            } 
            catch (Exception e) {
                    e.printStackTrace();
            }
    }      
        public static void main(String[] args) {
        System.out.println("--- Bat dau kiem tra ket noi ---");
        
        Connection c = Database.getConnection();

        if (c != null) {
            System.out.println("TEST: Lay doi tuong Connection THANH CONG!");
            
            Database.closeConnection(c);
        } else {
            System.out.println("TEST: Lay doi tuong Connection THAT BAI (null).");
        }
    }
        
}
