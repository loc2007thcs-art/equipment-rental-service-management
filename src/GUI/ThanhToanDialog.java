package GUI;
import DAO.*;
import Entity.*;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;

public class ThanhToanDialog extends JDialog {
    private JLabel lblTienHang, lblTienPhat, lblTienCoc, lblTongThanhToan;
    private JButton btnXacNhan;
    private PhieuThue phieu;
    private DefaultTableModel modelTB;
    private long finalTotal = 0, fine = 0, basePrice = 0;

    public ThanhToanDialog(Frame parent, PhieuThue pt) {
        super(parent, "Thanh Toán Phiếu: " + pt.getMaPhieu(), true);
        this.phieu = pt;
        setSize(500, 650);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);

        // Tính toán logic
        tinhToanTien();

        // --- 1. Tiêu đề ---
        JLabel lblTitle = new JLabel("", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- 2. Phần thân (Center): Gồm Thông tin KH và Bảng thiết bị ---
        JPanel pnlCenter = new JPanel(new BorderLayout(5, 5));
        pnlCenter.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        // Thông tin khách hàng & Phiếu
        JPanel pnlInfo = new JPanel(new GridLayout(2, 2, 5, 5));
        pnlInfo.add(new JLabel("Mã Phiếu: " + pt.getMaPhieu()));
        pnlInfo.add(new JLabel("Khách hàng: " + pt.getMaKH()));
        pnlInfo.add(new JLabel("Ngày thuê: " + pt.getNgayThue()));
        pnlInfo.add(new JLabel("Hạn trả: " + pt.getNgayTraDuKien()));
        pnlCenter.add(pnlInfo, BorderLayout.NORTH);

        // Bảng danh sách thiết bị thuê
        String[] headers = {"Tên thiết bị", "Hình thức", "Đơn giá", "Thời hạn"};
        modelTB = new DefaultTableModel(headers, 0);
        JTable table = new JTable(modelTB);
        pnlCenter.add(new JScrollPane(table), BorderLayout.CENTER);
        loadTableData();
        add(pnlCenter, BorderLayout.CENTER);

        // --- 3. Phần chân (South): Tổng kết tiền & Nút xác nhận ---
        JPanel pnlBottom = new JPanel(new BorderLayout());
        
        // Khu vực tính tiền
        JPanel pnlMoney = new JPanel(new GridLayout(4, 2, 10, 5));
        pnlMoney.setBorder(BorderFactory.createTitledBorder(""));

        pnlMoney.add(new JLabel("Tiền hàng:"));
        pnlMoney.add(new JLabel(String.format("%,d VNĐ", basePrice), JLabel.RIGHT));

        pnlMoney.add(new JLabel("Tiền phạt quá hạn:"));
        JLabel lblFine = new JLabel(String.format("%,d VNĐ", fine), JLabel.RIGHT);
        lblFine.setForeground(Color.RED);
        pnlMoney.add(lblFine);

        pnlMoney.add(new JLabel("Tiền đã đặt cọc:"));
        pnlMoney.add(new JLabel(String.format("- %,d VNĐ", phieu.getTienDatCoc()), JLabel.RIGHT));

        pnlMoney.add(new JLabel("TỔNG THANH TOÁN:"));
        JLabel lblFinal = new JLabel(String.format("%,d VNĐ", finalTotal), JLabel.RIGHT);
        lblFinal.setFont(new Font("Arial", Font.BOLD, 16));
        lblFinal.setForeground(new Color(0, 102, 0));
        pnlMoney.add(lblFinal);

        // Nút bấm
        JButton btnXacNhan = new JButton("XÁC NHẬN THANH TOÁN");
        btnXacNhan.setPreferredSize(new Dimension(0, 50));
        btnXacNhan.setBackground(new Color(0, 123, 255));
        btnXacNhan.setForeground(Color.BLACK);
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 14));
        btnXacNhan.addActionListener(e -> xuLyThanhToan());

        pnlBottom.add(pnlMoney, BorderLayout.CENTER);
        pnlBottom.add(btnXacNhan, BorderLayout.SOUTH);
        add(pnlBottom, BorderLayout.SOUTH);
    }
    
    private void loadTableData() {
        PhieuThueDAO dao = new PhieuThueDAO();
        List<ChiTietThue> chiTiets = dao.getChiTietByMaPhieu(phieu.getMaPhieu());
        for (ChiTietThue ct : chiTiets) {
            modelTB.addRow(new Object[]{
                ct.getMaTB(), 
                ct.getHinhThucThue(),
                String.format("%,d", ct.getGiaThue()),
                ct.getThoiHan()
            });
        }
    }
    private void tinhToanTien() {
        PhieuThueDAO dao = new PhieuThueDAO();
        List<ChiTietThue> chiTiets = dao.getChiTietByMaPhieu(phieu.getMaPhieu());

        // 1. Tính tiền hàng gốc
        for (ChiTietThue ct : chiTiets) {
            basePrice += (ct.getGiaThue() * ct.getThoiHan());
        }

        // 2. Tính tiền phạt dựa trên bảng QuyTacPhat
        Timestamp bayGio = new Timestamp(System.currentTimeMillis());
        if (bayGio.after(phieu.getNgayTraDuKien())) {
            long diffMillis = bayGio.getTime() - phieu.getNgayTraDuKien().getTime();

            for (ChiTietThue ct : chiTiets) {
                String hinhThuc = ct.getHinhThucThue(); 
                long donGiaPhat = dao.getDonGiaPhat(hinhThuc); // Lấy từ DB
                long soDonViTre = 0;

                if (hinhThuc.equalsIgnoreCase("gio")) {
                    soDonViTre = (long) Math.ceil((double) diffMillis / (1000 * 60 * 60));
                } 
                else if (hinhThuc.equalsIgnoreCase("ngay")) {
                    soDonViTre = (long) Math.ceil((double) diffMillis / (1000 * 60 * 60 * 24));
                } 
                else if (hinhThuc.equalsIgnoreCase("thang")) {
                    soDonViTre = (long) Math.ceil((double) diffMillis / (1000L * 60 * 60 * 24 * 30));
                }

                fine += (soDonViTre * donGiaPhat);
            }
        }

    // 3. Tổng thanh toán cuối cùng
    finalTotal = basePrice + fine - phieu.getTienDatCoc();
    if (finalTotal < 0) finalTotal = 0;
    }

    private void xuLyThanhToan() {
        hienThiQRBanking();
    }

    private void hienThiQRBanking() {
        JDialog qrDialog = new JDialog(this, "Quét mã QR để thanh toán", true);
        qrDialog.setSize(450, 550);
        qrDialog.setLayout(new BorderLayout(10, 10));
        qrDialog.setLocationRelativeTo(this);

        try {
            // Đường dẫn ảnh (Bạn thay đổi đường dẫn cho đúng với máy bạn)
            ImageIcon icon = new ImageIcon("src/resources/qr.jpg"); 

            // Chỉnh kích thước ảnh cho vừa khung (ví dụ 350x350)
            Image img = icon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            JLabel lblQR = new JLabel(new ImageIcon(img), JLabel.CENTER);
            qrDialog.add(lblQR, BorderLayout.CENTER);
        } catch (Exception e) {
            qrDialog.add(new JLabel("Không tìm thấy file ảnh QR!", JLabel.CENTER));
        }

        JButton btnDone = new JButton("XÁC NHẬN ĐÃ NHẬN TIỀN");
        btnDone.setFont(new Font("Arial", Font.BOLD, 13));
        btnDone.addActionListener(e -> {
            PhieuThueDAO dao = new PhieuThueDAO();
            long tongTien = basePrice + fine;
            if (dao.updateTrangThai(phieu.getMaPhieu(), "Đã thanh toán", tongTien)) {
                JOptionPane.showMessageDialog(qrDialog, "Thanh toán hoàn tất!");
                qrDialog.dispose();
                this.dispose();
            }
        });
        qrDialog.add(btnDone, BorderLayout.SOUTH);
        qrDialog.setVisible(true);
    }
}
