package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import DAO.AccountDAO;
import Entity.Account;
public class DangNhapGUI extends JFrame{
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin, btnDMK;

    public DangNhapGUI() {
        initGUI();
    }

    private void initGUI() {
        setTitle("HỆ THỐNG QUẢN LÝ PHÒNG MÁY - ĐĂNG NHẬP");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0;
        mainPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        
        txtUser = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(txtUser, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        mainPanel.add(new JLabel("Mật khẩu:"), gbc);
        
        txtPass = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(txtPass, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnLogin = new JButton("Đăng nhập");
        btnDMK = new JButton("Đổi mật khẩu");

        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.BLACK);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnDMK);
        
        gbc.gridy = 3; gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        pack(); 
        setLocationRelativeTo(null); 

        txtPass.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    xuLyDangNhap();
                }
            }
        });

        btnLogin.addActionListener(e -> xuLyDangNhap());
        btnDMK.addActionListener(e -> {
            new DoiMatKhauGUI().setVisible(true); 
            this.dispose(); 
        });
    }

    private void xuLyDangNhap() {
        String user = txtUser.getText().trim();
        String pass = String.valueOf(txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        AccountDAO dao = new AccountDAO();
        Account acc = dao.login(user, pass);

        if (acc != null) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            ManHinhChinhGUI main = new ManHinhChinhGUI(acc); 
            main.setVisible(true);
            this.dispose();
 
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không chính xác!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        
        SwingUtilities.invokeLater(() -> new DangNhapGUI().setVisible(true));
    }
}

