package GUI;
import DAO.*;
import Entity.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Calendar;
public class LapPhieuDialog extends JDialog{
    private JComboBox<String> cbKhachHang;
    private JTable tblThietBi; 
    private DefaultTableModel modelCart;
    private JTable tblCart;
    private JTextField txtTienCoc;
    private JButton btnThemTB, btnXoaTB, btnHoanTat;
    private JSpinner spnSoLuongThue;
    private JComboBox<String> cbHinhThucThue;
    private boolean isSuccess = false;

    public LapPhieuDialog(Frame parent) {
        super(parent, "Lập Phiếu Thuê Mới", true);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // 1. Phía trên: Chọn khách hàng & Tiền cọc
        JPanel pnlTop = new JPanel(new GridLayout(2, 2, 10, 10));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Thông tin chung"));
        pnlTop.add(new JLabel(" Chọn Khách Hàng:"));
        cbKhachHang = new JComboBox<>(); // Bạn nạp List khách hàng vào đây
        pnlTop.add(cbKhachHang);
        
        pnlTop.add(new JLabel(" Tiền đặt cọc:"));
        txtTienCoc = new JTextField("0");
        pnlTop.add(txtTienCoc);
        add(pnlTop, BorderLayout.NORTH);

        // 2. Phía giữa: Chọn thiết bị (Bên trái: DS Máy trống - Bên phải: Máy đã chọn)
        JPanel pnlCenter = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Bảng thiết bị sẵn có
        tblThietBi = new JTable(new DefaultTableModel(new String[]{"Mã TB", "Tên TB", "Giá"}, 0));
        pnlCenter.add(new JScrollPane(tblThietBi));
        
        // Bảng giỏ hàng
        modelCart = new DefaultTableModel(new String[]{"Mã TB", "Tên TB", "Hình thức", "Giá", "ThoiHan"}, 0);
        tblCart = new JTable(modelCart);
        pnlCenter.add(new JScrollPane(tblCart));
        
        add(pnlCenter, BorderLayout.CENTER);

        // 3. Phía dưới: Nút bấm
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        pnlBottom.add(new JLabel("Thời gian thuê:"));
        spnSoLuongThue = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        pnlBottom.add(spnSoLuongThue);
        cbHinhThucThue = new JComboBox<>(new String[]{"gio", "ngay", "thang"});
        pnlBottom.add(cbHinhThucThue);
        
        btnThemTB = new JButton(">> Thêm vào phiếu");
        btnXoaTB = new JButton("<< Xóa khỏi phiếu");
        btnHoanTat = new JButton("XÁC NHẬN THUÊ");
        btnHoanTat.setBackground(new Color(0, 153, 51));
        btnHoanTat.setForeground(Color.BLACK);
        
        pnlBottom.add(btnThemTB);
        pnlBottom.add(btnXoaTB);
        pnlBottom.add(btnHoanTat);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---
        btnThemTB.addActionListener(e -> {
            int row = tblThietBi.getSelectedRow();
            if (row != -1) {
                String maTB = tblThietBi.getValueAt(row, 0).toString();
                String tenTB = tblThietBi.getValueAt(row, 1).toString();
                String hinhThuc = cbHinhThucThue.getSelectedItem().toString();
                int soLuong = (int) spnSoLuongThue.getValue();

                ThietBiDAO dao = new ThietBiDAO();
                long giaThucTe = dao.getGiaTheoHinhThuc(maTB, hinhThuc);

                    if (giaThucTe > 0) {
                    long thanhTien = giaThucTe * soLuong;
                    modelCart.addRow(new Object[]{maTB, tenTB, hinhThuc, thanhTien, soLuong,});
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy đơn giá cho hình thức này!");
                    }
                } else {
                JOptionPane.showMessageDialog(this, "Hãy chọn một thiết bị bên danh sách máy trống!");
            }
        });
        
        btnXoaTB.addActionListener(e -> {
            int row = tblCart.getSelectedRow();
            if (row != -1) {
                modelCart.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn thiết bị muốn xóa khỏi phiếu!");
            }
        });
        
        btnHoanTat.addActionListener(e -> {
            xuLyLuuPhieu();
        });
        napDuLieuKhachHang(); 
        napDuLieuThietBi();
        setLocationRelativeTo(parent);
    }
    
    private void napDuLieuKhachHang() {
        KhachHangDAO khDAO = new KhachHangDAO();
        List<KhachHang> dsKH = khDAO.getAll();
        for (KhachHang kh : dsKH) {
            // Lưu Mã KH vào ComboBox (định dạng: "MaKH - TenKH")
            cbKhachHang.addItem(kh.getMaKH() + " - " + kh.getTenKH());
        }
    }
    private void napDuLieuThietBi() {
        ThietBiDAO tbDAO = new ThietBiDAO();
        // Giả sử DAO của bạn có hàm lấy danh sách thiết bị rảnh (chưa ai thuê)
        List<ThietBi> dsTB = tbDAO.getThietBiRanh(); 

        DefaultTableModel model = (DefaultTableModel) tblThietBi.getModel();
        model.setRowCount(0); // Xóa trắng bảng trước khi nạp

        for (ThietBi tb : dsTB) {
            model.addRow(new Object[]{
                tb.getMaTB(), 
                tb.getTenTB(), 
                tb.getDonGia() 
            });
        }
    }
    private void xuLyLuuPhieu() {
        // 1. Kiểm tra đầu vào
        if (cbKhachHang.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
            return;
        }
        if (modelCart.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Chưa chọn thiết bị nào để thuê!");
            return;
        }

        try {
            // 2. Thu thập thông tin Phiếu Thuê
            String khSelected = cbKhachHang.getSelectedItem().toString();
            String maKH = khSelected.split(" - ")[0]; // Tách lấy Mã KH
            long tienCoc = Long.parseLong(txtTienCoc.getText().trim());
            
            PhieuThue pt = new PhieuThue();
            pt.setMaKH(maKH);
            pt.setTienDatCoc(tienCoc);
            pt.setTrangThai("Chưa thanh toán");
            
            // --- TÍNH NGÀY THUÊ VÀ NGÀY TRẢ DỰ KIẾN ---
            long currentMillis = System.currentTimeMillis();
            Timestamp ngayThue = new Timestamp(currentMillis);
            pt.setNgayThue(ngayThue);
            long maxExpiryMillis = currentMillis;

            // --- 3. Duyệt giỏ hàng để tính toán ---
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                String hinhThuc = modelCart.getValueAt(i, 2).toString();
                int thoiHan = Integer.parseInt(modelCart.getValueAt(i, 4).toString());

                // TẠO MỚI Calendar bên trong vòng lặp for
                // Điều này đảm bảo mỗi dòng TB đều tính từ gốc ngayThue, KHÔNG cộng dồn
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(currentMillis); 

                if (hinhThuc.equalsIgnoreCase("ngay")) {
                    c.add(Calendar.DAY_OF_YEAR, thoiHan);
                } else if (hinhThuc.equalsIgnoreCase("gio")) {
                    c.add(Calendar.HOUR_OF_DAY, thoiHan);
                } else if(hinhThuc.equalsIgnoreCase("thang")){
                    c.add(Calendar.MONTH, thoiHan);
                }

                // So sánh tìm mốc xa nhất
                if (c.getTimeInMillis() > maxExpiryMillis) {
                    maxExpiryMillis = c.getTimeInMillis();
                }
            }

            // --- 4. Gán kết quả cuối cùng ---
            pt.setNgayTraDuKien(new Timestamp(maxExpiryMillis));

            // 3. Thu thập danh sách Chi Tiết Thuê từ bảng Giỏ hàng (modelCart)
            List<ChiTietThue> dsChiTiet = new ArrayList<>();
            for (int i = 0; i < modelCart.getRowCount(); i++) {
                ChiTietThue ct = new ChiTietThue();
                ct.setMaTB(modelCart.getValueAt(i, 0).toString());
                ct.setHinhThucThue(modelCart.getValueAt(i, 2).toString());

                String giaStr = modelCart.getValueAt(i, 3).toString().replace(",", "");
                ct.setGiaThue(Long.parseLong(giaStr));
                int thoiHan = Integer.parseInt(modelCart.getValueAt(i, 4).toString());
                ct.setThoiHan(thoiHan);
                dsChiTiet.add(ct);
            }
            
            // 4. Gọi DAO thực hiện Transaction
            PhieuThueDAO ptDAO = new PhieuThueDAO();
            if (ptDAO.insertPhieu(pt, dsChiTiet)) {
                JOptionPane.showMessageDialog(this, "Lập phiếu thuê thành công!");
                this.isSuccess = true;
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu vào cơ sở dữ liệu!");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tiền cọc hoặc số lượng phải là số!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi: " + ex.getMessage());
        }
    }

    public boolean isSuccess() { 
        return isSuccess; 
    }
}
