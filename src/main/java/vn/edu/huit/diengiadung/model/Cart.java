package vn.edu.huit.diengiadung.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    // Thêm sản phẩm vào giỏ
    public void themVaoGio(CartItem itemMoi) {
        for (CartItem i : items) {
            if (i.getIdSanPham().equals(itemMoi.getIdSanPham())) {
                i.setSoLuong(i.getSoLuong() + itemMoi.getSoLuong());
                return;
            }
        }
        items.add(itemMoi);
    }

    // Cập nhật số lượng
    public void capNhatSoLuong(Long idSanPham, int soLuongMoi) {
        for (CartItem i : items) {
            if (i.getIdSanPham().equals(idSanPham)) {
                if (soLuongMoi <= 0) {
                    xoaKhoiGio(idSanPham);
                } else {
                    i.setSoLuong(soLuongMoi);
                }
                break;
            }
        }
    }

    // Xóa sản phẩm khỏi giỏ
    public void xoaKhoiGio(Long idSanPham) {
        items.removeIf(item -> item.getIdSanPham().equals(idSanPham));
    }

    // Tính tổng tiền giỏ hàng
    public Long getTongTien() {
        long tong = 0;
        for (CartItem item : items) {
            tong += item.getThanhTien();
        }
        return tong;
    }

    // Lấy tổng số lượng sản phẩm (để hiện lên icon giỏ hàng góc phải màn hình)
    public int getTongSoLuong() {
        int tong = 0;
        for (CartItem item : items) {
            tong += item.getSoLuong();
        }
        return tong;
    }

    public List<CartItem> getItems() {
        return items;
    }
}