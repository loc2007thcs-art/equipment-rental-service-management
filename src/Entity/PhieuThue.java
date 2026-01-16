package Entity;
import java.sql.Timestamp;
public class PhieuThue {
    private int maPhieu;
    private String maKH;    
    private Timestamp ngayLap;   
    private Timestamp ngayThue;
    private Timestamp NgayTraDuKien;
    private Timestamp ngayTra; 
    private long tienDatCoc;
    private long tongTienPhat;
    private long tongTien;
    private String trangThai;

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public Timestamp getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Timestamp ngayLap) {
        this.ngayLap = ngayLap;
    }

    public Timestamp getNgayThue() {
        return ngayThue;
    }

    public void setNgayThue(Timestamp ngayThue) {
        this.ngayThue = ngayThue;
    }

    public Timestamp getNgayTra() {
        return ngayTra;
    }

    public void setNgayTra(Timestamp ngayTra) {
        this.ngayTra = ngayTra;
    }

    public long getTienDatCoc() {
        return tienDatCoc;
    }

    public void setTienDatCoc(long tienDatCoc) {
        this.tienDatCoc = tienDatCoc;
    }

    public long getTongTienPhat() {
        return tongTienPhat;
    }

    public void setTongTienPhat(long tongTienPhat) {
        this.tongTienPhat = tongTienPhat;
    }

    public long getTongTien() {
        return tongTien;
    }

    public void setTongTien(long tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    public Timestamp getNgayTraDuKien() {
        return NgayTraDuKien;
    }

    public void setNgayTraDuKien(Timestamp NgayTraDuKien) {
        this.NgayTraDuKien = NgayTraDuKien;
    }
    public PhieuThue(){
        
    }
    public PhieuThue(String maKH, long tienDatCoc) {
        this.maKH = maKH;
        this.tienDatCoc = tienDatCoc;
        this.trangThai = "Đang thuê";
    }
}
