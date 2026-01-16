package Entity;

public class ChiTietThue {
    private int maCT;
    private int maPhieu;
    private String maTB;
    private String hinhThucThue; 
    private long giaThue;        
    private int ThoiHan;

    public int getMaCT() {
        return maCT;
    }

    public void setMaCT(int maCT) {
        this.maCT = maCT;
    }

    public int getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(int maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaTB() {
        return maTB;
    }

    public void setMaTB(String maTB) {
        this.maTB = maTB;
    }

    public String getHinhThucThue() {
        return hinhThucThue;
    }

    public void setHinhThucThue(String hinhThucThue) {
        this.hinhThucThue = hinhThucThue;
    }

    public long getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(long giaThue) {
        this.giaThue = giaThue;
    }

    public int getThoiHan() {
        return ThoiHan;
    }

    public void setThoiHan(int ThoiHan) {
        this.ThoiHan = ThoiHan;
    }
    
    public ChiTietThue() {
    
    }
    public ChiTietThue(int maCT, int maPhieu, String maTB, String hinhThucThue, long giaThue, int ThoiHan) {
        this.maCT = maCT;
        this.maPhieu = maPhieu;
        this.maTB = maTB;
        this.hinhThucThue = hinhThucThue;
        this.giaThue = giaThue;
        this.ThoiHan = ThoiHan;
    }
    public ChiTietThue(int maPhieu, String maTB, String hinhThucThue, long giaThue, int ThoiHan) {
        this.maPhieu = maPhieu;
        this.maTB = maTB;
        this.hinhThucThue = hinhThucThue;
        this.giaThue = giaThue;
        this.ThoiHan = ThoiHan;
    }
    
}
