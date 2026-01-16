package GUI;
import DAO.KhachHangDAO;
import Entity.KhachHang;
import javax.swing.*;
import java.awt.*;
public class ThemKhachHangDialog extends JDialog{
    private JTextField txtMa, txtTen, txtSdt, txtDiaChi;
    private JButton btnLuu, btnHuy;
    private boolean isSuccess = false; // Biến kiểm tra xem đã thêm thành công chưa

    public ThemKhachHangDialog(Frame parent) {
        super(parent, "Thêm Khách Hàng Mới", true); // true = Modal (khóa màn hình chính)
        initGUI();
    }

    private void initGUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label và TextField
        gbc.gridx = 0; 
        gbc.gridy = 0; 
        add(new JLabel("Mã KH:"), gbc);
        txtMa = new JTextField(15); 
        txtMa.setText(new KhachHangDAO().getNextMaKH());                            
        txtMa.setEditable(false);                           
        txtMa.setBackground(Color.LIGHT_GRAY);
        gbc.gridx = 1; 
        add(txtMa, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 1; 
        add(new JLabel("Tên KH:"), gbc);
        txtTen = new JTextField(15); 
        gbc.gridx = 1; 
        add(txtTen, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("SĐT:"), gbc);
        txtSdt = new JTextField(15); 
        gbc.gridx = 1; 
        add(txtSdt, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 3; 
        add(new JLabel("Địa chỉ:"), gbc);
        txtDiaChi = new JTextField(15); 
        gbc.gridx = 1;
        add(txtDiaChi, gbc);

        // Nút bấm
        JPanel pnlBtns = new JPanel();
        btnLuu = new JButton("Lưu");
        btnHuy = new JButton("Hủy");
        pnlBtns.add(btnLuu); pnlBtns.add(btnHuy);

        gbc.gridx = 0; 
        gbc.gridy = 4; 
        gbc.gridwidth = 2;
        add(pnlBtns, gbc);

        // Xử lý sự kiện
        btnLuu.addActionListener(e -> {
            KhachHang kh = new KhachHang(txtMa.getText(), txtTen.getText(), txtSdt.getText(), txtDiaChi.getText());
            if (new KhachHangDAO().insert(kh)) { // Giả sử DAO có hàm insert()
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                isSuccess = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm!");
            }
        });

        btnHuy.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(getOwner());
    }

    public boolean isSuccess() { 
        return isSuccess; 
    }
}

