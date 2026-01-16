package GUI;
import Entity.Account;
import javax.swing.*;
import java.awt.*;
public class ManHinhChinhGUI extends JFrame {
    private Account currentAcc;
    private JPanel pnlContent; // Vùng chứa các màn hình chức năng
    private CardLayout cardLayout;

    // Khai báo các Panel chức năng từ file bạn đã gửi
    private ThietBiGUI pnlThietBi;
    private KhachHangGUI pnlKhachHang;
    private PhieuThueGUI pnlPhieuThue;

    // Màu sắc Theme
    private Color colorSidebar = new Color(44, 62, 80);
    private Color colorHeader = new Color(52, 152, 219);

    public ManHinhChinhGUI(Account acc) {
        this.currentAcc = acc;
        initGUI();
    }

    private void initGUI() {
        setTitle("HỆ THỐNG QUẢN LÝ THUÊ THIẾT BỊ");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. THANH SIDEBAR (Bên trái)
        add(createSidebar(), BorderLayout.WEST);

        // 2. THANH HEADER (Bên trên)
        add(createHeader(), BorderLayout.NORTH);

        // 3. VÙNG NỘI DUNG CHÍNH (Sử dụng CardLayout)
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);

        // KHỞI TẠO CÁC PANEL CHỨC NĂNG (Tận dụng file bạn đã gửi)
        pnlThietBi = new ThietBiGUI();
        pnlKhachHang = new KhachHangGUI();
        pnlPhieuThue = new PhieuThueGUI();

        // Đưa các Panel vào CardLayout với tên định danh (ID)
        pnlContent.add(new JPanel(), "Home"); // Màn hình trống mặc định
        pnlContent.add(pnlThietBi, "QUAN_LY_THIET_BI");
        pnlContent.add(pnlKhachHang, "QUAN_LY_KHACH_HANG");
        pnlContent.add(pnlPhieuThue, "QUAN_LY_PHIEU_THUE");

        add(pnlContent, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private JPanel createSidebar() {
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setBackground(colorSidebar);
        pnlSidebar.setPreferredSize(new Dimension(260, 0));
        pnlSidebar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));

        JLabel lblLogo = new JLabel("HỆ THỐNG");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        pnlSidebar.add(lblLogo);

        // Tạo các nút Menu và gán ID CardLayout tương ứng
        pnlSidebar.add(createMenuButton(" QUẢN LÝ THIẾT BỊ", "QUAN_LY_THIET_BI"));
        pnlSidebar.add(createMenuButton(" QUẢN LÝ KHÁCH HÀNG", "QUAN_LY_KHACH_HANG"));
        pnlSidebar.add(createMenuButton(" PHIẾU THUÊ", "QUAN_LY_PHIEU_THUE"));
        
        // Nút Thoát
        JButton btnExit = new JButton("THOÁT");
        btnExit.setPreferredSize(new Dimension(230, 45));
        btnExit.addActionListener(e -> System.exit(0));
        pnlSidebar.add(btnExit);

        return pnlSidebar;
    }

    private JPanel createHeader() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(colorHeader);
        pnl.setPreferredSize(new Dimension(0, 60));

        JLabel lblTitle = new JLabel("  PHẦN MỀM QUẢN LÝ CỬA HÀNG THIẾT BỊ");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));

        String userName = (currentAcc != null) ? currentAcc.getFullName() : "Admin";
        JLabel lblUser = new JLabel("Xin chào: " + userName + "  ");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        pnl.add(lblTitle, BorderLayout.WEST);
        pnl.add(lblUser, BorderLayout.EAST);
        return pnl;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(240, 55));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Sự kiện khi bấm nút: Chuyển Card và Load lại dữ liệu
        btn.addActionListener(e -> {
            cardLayout.show(pnlContent, cardName);
            
            // Tự động làm mới dữ liệu khi mở panel
            switch(cardName) {
                case "QUAN_LY_THIET_BI": pnlThietBi.loadData(); 
                break;
                case "QUAN_LY_KHACH_HANG": pnlKhachHang.loadData(); 
                break;
                // case "QUAN_LY_PHIEU_THUE": pnlPhieuThue.loadData(); break; // Nếu file PhieuThueGUI có loadData()
            }
        });
        return btn;
    }
}
