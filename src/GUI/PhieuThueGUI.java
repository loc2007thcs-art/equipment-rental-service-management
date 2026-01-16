package GUI;
import DAO.PhieuThueDAO;
import Entity.PhieuThue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class PhieuThueGUI extends JPanel{
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtSearchName;
    private JButton btnSearch;
    private PhieuThueDAO dao = new PhieuThueDAO();

    public PhieuThueGUI() {
        setLayout(new BorderLayout());
        
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.add(new JLabel("Tìm theo tên KH: "));
        txtSearchName = new JTextField(20);
        btnSearch = new JButton("Tìm kiếm");
        pnlSearch.add(txtSearchName);
        pnlSearch.add(btnSearch);

        add(pnlSearch, BorderLayout.NORTH);

        String[] headers = {"Mã Phiếu", "Khách Hàng", "Ngày Thuê", "Tổng Tiền", "Trạng Thái"};
        model = new DefaultTableModel(headers, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Thanh công cụ: Thêm và Sửa
        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Lập Phiếu Mới");
        JButton btnEdit = new JButton("Sửa Phiếu");
        JButton btnPay = new JButton("THANH TOÁN");
        
        pnlControl.add(btnAdd);
        pnlControl.add(btnEdit);
        pnlControl.add(btnPay);
        add(pnlControl, BorderLayout.SOUTH);

        // 1. Sự kiện Lập Phiếu Mới
        btnAdd.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            LapPhieuDialog Ldialog = new LapPhieuDialog(parent);
            Ldialog.setVisible(true);
            if (Ldialog.isSuccess()) { 
                loadData();
            }
        });

        // 2. Sự kiện Sửa Phiếu
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu từ bảng để sửa!");
                return;
            }
            String ma = table.getValueAt(selectedRow, 0).toString();
            String tienCocStr = table.getValueAt(selectedRow, 1).toString();
            String trangThai = table.getValueAt(selectedRow, 2).toString();
            PhieuThue pt = new PhieuThue();
            pt.setMaPhieu(Integer.parseInt(ma));
            pt.setTienDatCoc(Long.parseLong(tienCocStr));
            pt.setTrangThai(trangThai);
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

            // Khởi tạo Dialog và truyền maPhieu vào để tải dữ liệu cũ lên
            SuaPhieuDialog Sdialog = new SuaPhieuDialog(parent, pt);
            Sdialog.setVisible(true);
            loadData();
        });
        
        btnSearch.addActionListener(e -> {
                String keyword = txtSearchName.getText().trim();
                List<PhieuThue> ketQua = dao.search(keyword); 
                loadData(ketQua);
            });
        
        btnPay.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu cần thanh toán!");
                return;
            }

            String trangThai = table.getValueAt(row, 4).toString();
            if (trangThai.equals("Đã thanh toán")) {
                JOptionPane.showMessageDialog(this, "Phiếu này đã thanh toán rồi!");
                return;
            }
            int maPhieu = Integer.parseInt(table.getValueAt(row, 0).toString());
            PhieuThue pt = dao.getById(maPhieu);
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            ThanhToanDialog dialog = new ThanhToanDialog(parent, pt);
            dialog.setVisible(true);
            loadData(); 
        });
        loadData();
    }

    public void loadData(java.util.List<PhieuThue> ketQuaTimKiem) {
        model.setRowCount(0);

        java.util.List<PhieuThue> list = (ketQuaTimKiem != null) ? ketQuaTimKiem : dao.getAll();

        for (PhieuThue pt : list) {
            model.addRow(new Object[]{
                pt.getMaPhieu(),
                pt.getMaKH(), 
                pt.getNgayThue(),
                String.format("%,d VNĐ", pt.getTongTien()),
                pt.getTrangThai()
            });
        }
        table.revalidate();
        table.repaint();
    }
    public void loadData() {
        loadData(null);
        table.revalidate();
        table.repaint();
    }
}
