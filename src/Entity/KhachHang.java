package Entity;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String SDT;
    private String Diachi;

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getDiachi() {
        return Diachi;
    }

    public void setDiachi(String Diachi) {
        this.Diachi = Diachi;
    }
    
    public KhachHang(){
        
    }
    public KhachHang(String maKH, String tenKH, String SDT, String Diachi){
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.SDT = SDT;
        this.Diachi = Diachi;
    }
}


