package DAO;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import Entity.PhieuThue;
import Entity.ChiTietThue;

public class PhieuThueDAO {
    
    public List<PhieuThue> getAll() {
    List<PhieuThue> list = new ArrayList<>();
    // Join với bảng KhachHang để lấy TenKH thay vì chỉ lấy MaKH
    String sql = "SELECT p.*, k.TenKH FROM PhieuThue p JOIN KhachHang k ON p.MaKH = k.MaKH ORDER BY p.MaPhieu ASC";
    
    try (Connection con = Database.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            PhieuThue pt = new PhieuThue();
            pt.setMaPhieu(rs.getInt("MaPhieu"));
            pt.setMaKH(rs.getString("TenKH")); 
            pt.setNgayThue(rs.getTimestamp("NgayThue"));
            pt.setNgayTraDuKien(rs.getTimestamp("NgayTraDuKien"));
            pt.setTongTien(rs.getLong("TongTien"));
            pt.setTrangThai(rs.getString("TrangThai"));
            list.add(pt);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return list;
}

    public boolean insertPhieu(PhieuThue pt, List<ChiTietThue> dsChiTiet) {
        String sqlPhieu = "INSERT INTO PhieuThue (MaKH, NgayLap, NgayThue, NgayTraDuKien, TienDatCoc, TongTien, TrangThai) VALUES (?, NOW(), NOW(), ?, ?, ?, ?)";
        String sqlCT = "INSERT INTO ChiTietThue (MaPhieu, MaTB, HinhThucThue, GiaThue, ThoiHan) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateTB = "UPDATE ThietBi SET TrangThai = 'Đang thuê' WHERE MaTB = ?";

        Connection con = null;
        try {
            con = Database.getConnection();
            con.setAutoCommit(false);

            int generatedId = -1;

            try (PreparedStatement psP = con.prepareStatement(sqlPhieu, Statement.RETURN_GENERATED_KEYS)) {
                psP.setString(1, pt.getMaKH());
                psP.setTimestamp(2, pt.getNgayTraDuKien());
                psP.setLong(3, pt.getTienDatCoc());
                psP.setLong(4, pt.getTongTien());
                psP.setString(5, pt.getTrangThai());
                psP.executeUpdate();

                ResultSet rs = psP.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

            if (generatedId != -1) {
                try (PreparedStatement psCT = con.prepareStatement(sqlCT);
                     PreparedStatement psTB = con.prepareStatement(sqlUpdateTB)) {
                    
                    for (ChiTietThue ct : dsChiTiet) {
                        psCT.setInt(1, generatedId);
                        psCT.setString(2, ct.getMaTB());
                        psCT.setString(3, ct.getHinhThucThue());
                        psCT.setLong(4, ct.getGiaThue());
                        psCT.setInt(5, ct.getThoiHan());
                        psCT.executeUpdate();

                        psTB.setString(1, ct.getMaTB());
                        psTB.executeUpdate();
                    }
                }
                con.commit();
                return true;
            }
        } catch (Exception e) {
            try {
                if(con != null) con.rollback(); 
            } catch(SQLException ex) {
            }
            e.printStackTrace();
        }
        return false;
    }
    
    public List<ChiTietThue> getChiTietByMaPhieu(int maPhieu) {
        List<ChiTietThue> list = new ArrayList<>();
        String sql = "SELECT ct.*, tb.TenTB FROM ChiTietThue ct " +
                 "JOIN ThietBi tb ON ct.MaTB = tb.MaTB " +
                 "WHERE ct.MaPhieu = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietThue ct = new ChiTietThue();
                ct.setTenTB(rs.getString("TenTB"));
                ct.setHinhThucThue(rs.getString("HinhThucThue"));
                ct.setGiaThue(rs.getLong("GiaThue"));
                ct.setThoiHan(rs.getInt("ThoiHan")); 
                list.add(ct);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    
    public boolean hoanTatThanhToan(int maPhieu, long tongTien, long tienPhat, List<String> dsMaTB) {
        Connection con = null;
        try {
            con = Database.getConnection();
            con.setAutoCommit(false); // Dùng Transaction để đảm bảo an toàn dữ liệu

            // Cập nhật trạng thái phiếu và tiền nong
            String sqlPhieu = "UPDATE PhieuThue SET NgayTra = NOW(), TongTien = ?, TongTienPhat = ?, TrangThai = 'Đã thanh toán' WHERE MaPhieu = ?";
            PreparedStatement psP = con.prepareStatement(sqlPhieu);
            psP.setLong(1, tongTien);
            psP.setLong(2, tienPhat);
            psP.setInt(3, maPhieu);
            psP.executeUpdate();

            // Cập nhật trạng thái các thiết bị thành 'Trống' để người khác thuê
            String sqlTB = "UPDATE ThietBi SET TrangThai = 'Trống' WHERE MaTB = ?";
            PreparedStatement psTB = con.prepareStatement(sqlTB);
            for (String maTB : dsMaTB) {
                psTB.setString(1, maTB);
                psTB.executeUpdate();
            }

            con.commit();
            return true;
        } catch (Exception e) {
            try { if(con != null) con.rollback(); } catch(Exception ex) {}
            e.printStackTrace();
            return false;
        }
    }
    
    public ResultSet getHoaDonChiTiet(int maPhieu) {
        String sql = "SELECT p.MaPhieu, k.TenKH, t.TenTB, p.NgayThue, p.NgayThucTeTra, " +
                     "ct.HinhThucThue, ct.GiaThue, ct.ThoiHan, p.TienDatCoc, p.TongTien " +
                     "FROM PhieuThue p " +
                     "JOIN KhachHang k ON p.MaKH = k.MaKH " +
                     "JOIN ChiTietThue ct ON p.MaPhieu = ct.MaPhieu " +
                     "JOIN ThietBi t ON ct.MaTB = t.MaTB " +
                     "WHERE p.MaPhieu = ?";
        try {
                Connection con = Database.getConnection(); 
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, maPhieu);
                return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<PhieuThue> search(String keyword){
        List<PhieuThue> list =new ArrayList();
        String sql = "SELECT p.*, k.TenKH FROM PhieuThue p " +
                 "JOIN KhachHang k ON p.MaKH = k.MaKH " +
                 "WHERE k.TenKH LIKE ? " +
                 "ORDER BY p.MaPhieu ASC";
        try(Connection con = Database.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            String seachPattern = keyword+"%";
            ps.setString(1, seachPattern);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                PhieuThue pt = new PhieuThue();
                pt.setMaPhieu(rs.getInt("MaPhieu"));
                pt.setMaKH(rs.getString("TenKH")); 
                pt.setNgayThue(rs.getTimestamp("NgayThue"));
                pt.setNgayTraDuKien(rs.getTimestamp("NgayTraDuKien"));
                pt.setTongTien(rs.getLong("TongTien"));
                pt.setTrangThai(rs.getString("TrangThai"));
                list.add(pt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }    
    
    public PhieuThue getById(int maPhieu) {
        String sql = "SELECT p.*, k.TenKH FROM PhieuThue p " +
                 "JOIN KhachHang k ON p.MaKH = k.MaKH " +
                 "WHERE p.MaPhieu = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, maPhieu);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PhieuThue pt = new PhieuThue();
                pt.setMaPhieu(rs.getInt("MaPhieu"));
                pt.setMaKH(rs.getString("TenKH"));
                pt.setNgayThue(rs.getTimestamp("NgayThue"));
                pt.setNgayTraDuKien(rs.getTimestamp("NgayTraDuKien"));
                pt.setTienDatCoc(rs.getLong("TienDatCoc"));
                pt.setTrangThai(rs.getString("TrangThai"));
                return pt;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
    
    public long getDonGiaPhat(String hinhThuc) {
        String sql = "SELECT TyLePhat FROM QuyTacPhat WHERE HinhThucThue = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hinhThuc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getLong("TyLePhat");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean updateTrangThai(int maPhieu, String trangThai, long tongTien ) {
        String sql = "UPDATE PhieuThue SET TrangThai = ?, TongTien = ? WHERE MaPhieu = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, trangThai);
            ps.setLong(2, tongTien);
            ps.setInt(3, maPhieu);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

