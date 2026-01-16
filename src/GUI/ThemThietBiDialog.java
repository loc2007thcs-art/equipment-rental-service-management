package GUI;
import DAO.ThietBiDAO;
import Entity.ThietBi;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
public class ThemThietBiDialog extends JDialog{
    private JTextField txtMa, txtTen, txtCauHinh;
    private JTextField txtGiaGio, txtGiaNgay, txtGiaThang;
    private JButton btnLuu, btnHuy;
    private boolean isSuccess = false;
    
    public ThemThietBiDialog(Frame parent) {
        super(parent, "Thêm Thiết Bị Mới", true);
        initGUI();      
        setupEvents();  // Hàm xử lý nút bấm
    }
    private void initGUI() {
        setSize(450, 350);
        setLayout(new BorderLayout());
        JPanel pnlInfo = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        txtMa = new JTextField();
        txtTen = new JTextField();
        txtCauHinh = new JTextField();

        pnlInfo.add(new JLabel("Mã thiết bị:"));
        pnlInfo.add(txtMa);
        pnlInfo.add(new JLabel("Tên thiết bị:"));
        pnlInfo.add(txtTen);
        pnlInfo.add(new JLabel("Cấu hình:"));
        pnlInfo.add(txtCauHinh);

        JPanel pnlGia = new JPanel(new GridLayout(3, 2, 5, 10));
        pnlGia.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Bảng giá thuê (VNĐ)", 
                TitledBorder.LEFT, TitledBorder.TOP));

        txtGiaGio = new JTextField("0");
        txtGiaNgay = new JTextField("0");
        txtGiaThang = new JTextField("0");

        pnlGia.add(new JLabel("Giá theo Giờ:"));
        pnlGia.add(txtGiaGio);
        pnlGia.add(new JLabel("Giá theo Ngày:"));
        pnlGia.add(txtGiaNgay);
        pnlGia.add(new JLabel("Giá theo Tháng:"));
        pnlGia.add(txtGiaThang);

        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.add(pnlInfo);
        pnlCenter.add(pnlGia);
        add(pnlCenter, BorderLayout.CENTER);

        // --- PHẦN 3: NÚT BẤM ---
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLuu = new JButton("Lưu dữ liệu");
        btnHuy = new JButton("Hủy bỏ");
        
        // Trang trí nhẹ cho nút Lưu
        btnLuu.setBackground(new Color(0, 123, 255));
        btnLuu.setForeground(Color.BLACK);
        
        pnlButtons.add(btnLuu);
        pnlButtons.add(btnHuy);
        add(pnlButtons, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null); // Hiển thị giữa màn hình
    }

    private void setupEvents() {
        // Nút Hủy
        btnHuy.addActionListener(e -> dispose());

        // Nút Lưu
        btnLuu.addActionListener(e -> {
            try {
                // Kiểm tra nhập liệu cơ bản
                if (txtMa.getText().trim().isEmpty() || txtTen.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã và Tên thiết bị!");
                    return;
                }

                // 1. Lấy dữ liệu và tạo Entity (DonGia đại diện cho giá Giờ)
                int giaGio = Integer.parseInt(txtGiaGio.getText().trim());
                int giaNgay = Integer.parseInt(txtGiaNgay.getText().trim());
                int giaThang = Integer.parseInt(txtGiaThang.getText().trim());

                // Thứ tự tham số chuẩn: Ma, Ten, CauHinh, TrangThai, DonGia
                ThietBi tb = new ThietBi(
                    txtMa.getText().trim(),
                    txtTen.getText().trim(),
                    txtCauHinh.getText().trim(),
                    "Trống",
                    giaGio
                );

                // 2. Gọi DAO để lưu (Hàm này nhận 3 tham số như đã thống nhất)
                if (new ThietBiDAO().insertTB(tb, giaNgay, giaThang)) {
                    isSuccess = true;
                    JOptionPane.showMessageDialog(this, "Thêm thiết bị thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Lưu thất bại! Kiểm tra lại database.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng chỉ nhập số vào các ô giá tiền!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + ex.getMessage());
            }
        });
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
