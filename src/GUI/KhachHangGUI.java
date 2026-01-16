package GUI;
import DAO.KhachHangDAO;
import Entity.KhachHang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class KhachHangGUI extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private KhachHangDAO dao = new KhachHangDAO();

    public KhachHangGUI() {
        setLayout(new BorderLayout());

        // 1. Thiết kế Bảng
        String[] headers = {"Mã KH", "Tên Khách Hàng", "Số Điện Thoại", "Địa Chỉ"};
        model = new DefaultTableModel(headers, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 2. Thanh công cụ (Nút bấm)
        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm Khách Hàng");
        pnlControl.add(btnAdd);
        add(pnlControl, BorderLayout.SOUTH);

        // 3. Xử lý sự kiện (Lambda)
        btnAdd.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            ThemKhachHangDialog dialog = new ThemKhachHangDialog(parent);
            dialog.setVisible(true);
            if (dialog.isSuccess()) loadData(); // Load lại nếu thêm thành công
        });

        loadData();
    }

    public void loadData() {
        model.setRowCount(0);
        List<KhachHang> list = dao.getAll();
        System.out.println("Số lượng khách hàng lấy được: " + list.size());
        for (KhachHang kh : list) {
            model.addRow(new Object[]{kh.getMaKH(), kh.getTenKH(), kh.getSDT(), kh.getDiachi()});
        }
        table.revalidate();
        table.repaint();
    }
}
