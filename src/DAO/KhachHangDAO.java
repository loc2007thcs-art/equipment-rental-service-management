package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Entity.KhachHang;
public class KhachHangDAO {
    public boolean isValidPhone(String sdt){
        String regex = "^0\\d{9}$";
        return sdt!=null&&sdt.matches(regex);
    }
    public String getNextMaKH() {
    String sql = "SELECT MaKH FROM KhachHang ORDER BY MaKH DESC LIMIT 1";
    try (Connection con = Database.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            String lastMaKH = rs.getString("MaKH"); 
            String numberPart = lastMaKH.substring(2); 
            int number = Integer.parseInt(numberPart);
            number++;
            
            return String.format("KH%05d", number);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "KH00001";
    }
    public List<KhachHang> getAll() {
        List<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("MaKH"));
                    kh.setTenKH(rs.getString("TenKH"));
                    kh.setSDT(rs.getString("SDT"));
                    kh.setDiachi(rs.getString("DiaChi")
                );
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean insert(KhachHang kh) {
        if(!isValidPhone(kh.getSDT())){
            System.out.println("Lỗi: Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!");
        return false;
        }
        String newMaKH = getNextMaKH();
        String sql = "INSERT INTO KhachHang (MaKH, TenKH, SDT, DiaChi) VALUES (?, ?, ?, ?)";
        try (Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setString(1, newMaKH); 
        ps.setString(2, kh.getTenKH());
        ps.setString(3, kh.getSDT());
        ps.setString(4, kh.getDiachi());
        
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
    }
    public boolean update(KhachHang kh) {
        String sql = "UPDATE KhachHang SET TenKH = ?, SDT = ?, DiaChi = ? WHERE MaKH = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSDT());
            ps.setString(3, kh.getDiachi());
            ps.setString(4, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<KhachHang> search(String keyword){
        List<KhachHang> list =new ArrayList();
        String sql = "SELECT * FROM KhachHang WHERE TenKH=?";
        try(Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            String seachPattern = "%"+keyword+"%";
            ps.setString(1, seachPattern);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new KhachHang(
                    rs.getString("MaKH"),
                    rs.getString("TenKH"),
                    rs.getString("SDT"),
                    rs.getString("DiaChi")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }        

}

