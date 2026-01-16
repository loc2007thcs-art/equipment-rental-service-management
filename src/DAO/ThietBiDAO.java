package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Entity.ThietBi;
public class ThietBiDAO {
    public List<ThietBi> getAll(){
        List<ThietBi> list = new ArrayList();
        String sql = "SELECT t.*, g.DonGia, g.HinhThucThue FROM ThietBi t JOIN BangGiaThue g on t.MaTB = g.MaTB";
        try(Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                ThietBi tb = new ThietBi();
                    tb.setMaTB(rs.getString("MaTB"));
                    tb.setTenTB(rs.getString("TenTB"));
                    tb.setCauHinh(rs.getString("CauHinh"));
                    tb.setTrangThai(rs.getString("TrangThai"));
                    tb.setDonGia(rs.getInt("DonGia"));
                    tb.setHinhThucThue(rs.getString("HinhThucThue"));
                list.add(tb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean insertTB(ThietBi tb, int giaNgay, int giaThang) {
        String sqlTB = "INSERT INTO ThietBi(MaTB, TenTB, CauHinh, TrangThai) VALUES(?,?,?,?)";
        String sqlGia = "INSERT INTO BangGiaThue(MaTB, HinhThucThue, DonGia) VALUES(?,?,?)";

        try (Connection con = Database.getConnection()) {
            con.setAutoCommit(false); // Quan trọng: Để lưu cả 2 bảng cùng lúc

            // 1. Lưu thông tin máy vào bảng ThietBi
            PreparedStatement psTB = con.prepareStatement(sqlTB);
            psTB.setString(1, tb.getMaTB());
            psTB.setString(2, tb.getTenTB());
            psTB.setString(3, tb.getCauHinh());
            psTB.setString(4, tb.getTrangThai());
            psTB.executeUpdate();

            // 2. Lưu 3 dòng giá vào bảng BangGiaThue
            PreparedStatement psGia = con.prepareStatement(sqlGia);

            // Dòng 1: Giá giờ (Lấy từ biến DonGia duy nhất trong Entity)
            psGia.setString(1, tb.getMaTB());
            psGia.setString(2, "giờ");
            psGia.setInt(3, tb.getDonGia()); 
            psGia.executeUpdate();

            // Dòng 2: Giá ngày (Lấy từ tham số truyền thêm)
            psGia.setString(1, tb.getMaTB());
            psGia.setString(2, "ngày");
            psGia.setInt(3, giaNgay);
            psGia.executeUpdate();

            // Dòng 3: Giá tháng (Lấy từ tham số truyền thêm)
            psGia.setString(1, tb.getMaTB());
            psGia.setString(2, "tháng");
            psGia.setInt(3, giaThang);
            psGia.executeUpdate();

            con.commit(); 
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<ThietBi> seearch(String keyword){
        List<ThietBi> list = new ArrayList();
        String sql = "SELECT * FROM ThietBi Where TenTB=?";
        try(Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            String searchPattern = "%"+keyword+"%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                list.add(new ThietBi(
                    rs.getString("MaTB"),
                    rs.getString("TenTB"),
                    rs.getString("CauHinh"),
                    rs.getString("TrangThai"),
                    rs.getInt("DonGia")
                    
                    
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<ThietBi> getThietBiRanh() {
        List<ThietBi> list = new ArrayList<>();
        String sql = "SELECT t.*, g.DonGia FROM ThietBi t " +
                     "JOIN BangGiaThue g ON t.MaTB = g.MaTB " +
                     "WHERE t.TrangThai = N'Trống' AND g.HinhThucThue = N'giờ'";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ThietBi tb = new ThietBi();
                tb.setMaTB(rs.getString("MaTB"));
                tb.setTenTB(rs.getString("TenTB"));
                tb.setTrangThai(rs.getString("TrangThai"));
                tb.setDonGia(rs.getInt("DonGia")); 

                list.add(tb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public long getGiaTheoHinhThuc(String maTB, String hinhThuc) {
        String sql = "SELECT DonGia FROM BangGiaThue WHERE MaTB = ? AND HinhThucThue = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maTB);
            ps.setNString(2, hinhThuc);         

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("DonGia");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean updateStatus(String maTB, String newStatus) {
        String sql = "UPDATE ThietBi SET TrangThai = ? WHERE MaTB = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, maTB);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return false;
    }
}
