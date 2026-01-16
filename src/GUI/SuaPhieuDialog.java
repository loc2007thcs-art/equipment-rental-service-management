package GUI;
import Entity.PhieuThue;
import javax.swing.*;
import java.awt.*;
public class SuaPhieuDialog extends JDialog{
    private JTextField txtMaPhieu, txtTienCoc;
    private JComboBox<String> cbTrangThai;
    private JButton btnCapNhat;
    private boolean isSuccess = false;

    public SuaPhieuDialog(Frame parent, PhieuThue pt) {
        super(parent, "Sửa thông tin phiếu", true);
        setLayout(new GridLayout(4, 2, 10, 10));

        add(new JLabel(" Mã Phiếu:"));
        txtMaPhieu = new JTextField(String.valueOf(pt.getMaPhieu()));
        txtMaPhieu.setEditable(false); // Không cho sửa mã
        add(txtMaPhieu);

        add(new JLabel(" Tiền đặt cọc:"));
        txtTienCoc = new JTextField(String.valueOf(pt.getTienDatCoc()));
        add(txtTienCoc);

        add(new JLabel(" Trạng thái:"));
        cbTrangThai = new JComboBox<>(new String[]{"Chưa thanh toán", "Đã hủy"});
        cbTrangThai.setSelectedItem(pt.getTrangThai());
        add(cbTrangThai);

        btnCapNhat = new JButton("Cập nhật");
        add(btnCapNhat);
        
        btnCapNhat.addActionListener(e -> {
            isSuccess = true;
            dispose();
        });

        pack();
        setLocationRelativeTo(parent);
    }
    
    public boolean isSuccess() { 
        return isSuccess; 
    }
}
