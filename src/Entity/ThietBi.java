package Entity;

public class ThietBi {
   private String MaTB;
   private String TenTB;
   private String CauHinh;
   private String TrangThai;
   private int DonGia;
   private String hinhThucThue;

    public String getMaTB() {
        return MaTB;
    }
    public void setMaTB(String MaTB) {
        this.MaTB = MaTB;
    }
    public String getTenTB() {
        return TenTB;
    }
    public void setTenTB(String TenTB) {
        this.TenTB = TenTB;
    }
    public String getCauHinh() {
        return CauHinh;
    }
    public void setCauHinh(String CauHinh) {
        this.CauHinh = CauHinh;
    }
    public String getTrangThai() {
        return TrangThai;
    }
    public void setTrangThai(String TrangThai) {
        this.TrangThai = TrangThai;
    }
    public int getDonGia() {
        return DonGia;
    }
    public void setDonGia(int DonGia) {
        this.DonGia = DonGia;
    }
    public String getHinhThucThue() { 
        return hinhThucThue; 
    }
    public void setHinhThucThue(String hinhThucThue) { 
        this.hinhThucThue = hinhThucThue; 
    }
    
   public ThietBi(){
       
   }
   public ThietBi(String MaTB, String TenTB, String CauHinh, String TrangThai, int DonGia){
       this.MaTB = MaTB;
       this.TenTB = TenTB;
       this.CauHinh = CauHinh;
       this.TrangThai = TrangThai;
       this.DonGia = DonGia;
   }
}
