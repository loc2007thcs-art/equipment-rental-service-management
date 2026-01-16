package GUI;

import DAO.AccountDAO;
import javax.swing.*;
import java.awt.*;

public class DoiMatKhauGUI extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtOldPass, txtNewPass, txtConfirmPass;
    private JButton btnChange, btnBack;

    public DoiMatKhauGUI() {
        initGUI();
    }

    private void initGUI() {
        setTitle("ĐỔI MẬT KHẨU");
        setSize(400, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Giao diện nhập liệu
        gbc.gridx = 0; gbc.gridy = 0; mainPanel.add(new JLabel("Username:"), gbc);
        txtUser = new JTextField(15);
        gbc.gridx = 1; mainPanel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(new JLabel("Mật khẩu cũ:"), gbc);
        txtOldPass = new JPasswordField(15);
        gbc.gridx = 1; mainPanel.add(txtOldPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(new JLabel("Mật khẩu mới:"), gbc);
        txtNewPass = new JPasswordField(15);
        gbc.gridx = 1; mainPanel.add(txtNewPass, gbc);

        gbc.gridx = 0; gbc.gridy = 3; mainPanel.add(new JLabel("Xác nhận MK mới:"), gbc);
        txtConfirmPass = new JPasswordField(15);
        gbc.gridx = 1; mainPanel.add(txtConfirmPass, gbc);

        // Nút bấm
        btnChange = new JButton("Đổi Mật Khẩu");
        btnBack = new JButton("Quay lại");
        
        JPanel pnlBtn = new JPanel();
        pnlBtn.add(btnChange);
        pnlBtn.add(btnBack);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        mainPanel.add(pnlBtn, gbc);

        add(mainPanel);

        // Sự kiện
        btnBack.addActionListener(e -> {
            new DangNhapGUI().setVisible(true);
            this.dispose();
        });

        btnChange.addActionListener(e -> xuLyDoiMatKhau());
    }

    private void xuLyDoiMatKhau() {
        String user = txtUser.getText().trim();
        String oldPass = String.valueOf(txtOldPass.getPassword());
        String newPass = String.valueOf(txtNewPass.getPassword());
        String confirm = String.valueOf(txtConfirmPass.getPassword());

        if (user.isEmpty() || oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đủ thông tin!");
            return;
        }

        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không khớp!");
            return;
        }

        AccountDAO dao = new AccountDAO();
        // 1. Kiểm tra tài khoản và mật khẩu cũ có đúng không
        if (dao.login(user, oldPass) != null) {
            // 2. Nếu đúng thì tiến hành cập nhật mật khẩu mới
            if (dao.updatePassword(user, newPass)) {
                JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
                new DangNhapGUI().setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi cập nhật mật khẩu!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Username hoặc mật khẩu cũ sai!");
        }
    }
}