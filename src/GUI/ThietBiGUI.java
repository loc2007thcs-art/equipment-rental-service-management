package GUI;
import DAO.ThietBiDAO;
import Entity.ThietBi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
public class ThietBiGUI extends JPanel{
    private JTable table;
    private DefaultTableModel model;
    private ThietBiDAO dao = new ThietBiDAO();

    public ThietBiGUI() {
        setLayout(new BorderLayout());

        String[] headers = {"Mã TB", "Tên Thiết Bị", "Cấu Hình", "Giá Thuê", "Trạng Thái"};
        model = new DefaultTableModel(headers, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.getColumnModel().getColumn(0).setPreferredWidth(50); 
        table.getColumnModel().getColumn(1).setPreferredWidth(300); 
        table.getColumnModel().getColumn(2).setPreferredWidth(500); 
        table.getColumnModel().getColumn(3).setPreferredWidth(100); 
        table.getColumnModel().getColumn(4).setPreferredWidth(70);

        // Thanh công cụ
        JPanel pnlControl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("Thêm Thiết Bị");
        btnAdd.setFont(new Font("Arial", Font.BOLD, 14));
        btnAdd.setBackground(new Color(0, 153, 76));
        btnAdd.setForeground(Color.BLACK);
        pnlControl.add(btnAdd);
        add(pnlControl, BorderLayout.SOUTH);

        // Xử lý sự kiện
        btnAdd.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            ThemThietBiDialog dialog = new ThemThietBiDialog(parent);
            dialog.setVisible(true);
            if (dialog.isSuccess()) {
                loadData();
            }
        });

        loadData();
    }

    public void loadData() {
        model.setRowCount(0);
        List<ThietBi> list = dao.getAll();
        for (ThietBi tb : list) {
            String dvt = (tb.getHinhThucThue() != null) ? tb.getHinhThucThue().toLowerCase() : "";
            model.addRow(new Object[]{
                tb.getMaTB(), tb.getTenTB(), tb.getCauHinh(),
                String.format("%,d/%s", tb.getDonGia(), dvt),tb.getTrangThai()
            });
        }
        table.revalidate();
        table.repaint();
    }
}
