package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.model.Cart;
import vn.edu.huit.diengiadung.model.CartItem;

// Lưu ý: Nếu nhóm bạn dùng Service thay vì Repository, hãy đổi tương ứng nhé
// import vn.edu.huit.diengiadung.repository.SanPhamRepository;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {

    // TODO: Mở comment 2 dòng dưới đây nếu nhóm bạn đã tạo SanPhamRepository để gọi Database
     @Autowired
     private SanPhamRepository sanPhamRepository;

    // Hàm tiện ích: Lấy giỏ hàng từ Session của người dùng hiện tại
    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // 1. Hiển thị trang giỏ hàng
    @GetMapping
    public String hienThiGioHang(HttpSession session, Model model) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        return "gio-hang"; // Trả về file giao diện: templates/gio-hang.html (hoặc tên file nhóm quy định)
    }

    // 2. Xử lý khi khách bấm "Thêm vào giỏ"
    @PostMapping("/them")
    public String themVaoGio(@RequestParam("idSanPham") Long idSanPham,
                             @RequestParam(value = "soLuong", defaultValue = "1") int soLuong,
                             HttpSession session) {

        // TODO: Mở comment dòng này để lấy thông tin sản phẩm thật từ Database
        // SanPham sp = sanPhamRepository.findById(idSanPham).orElse(null);

        // Đoạn code giả lập (Hãy XÓA đoạn này khi bạn đã kết nối được với Database ở dòng trên)
        SanPham sp = new SanPham();
        sp.setId(idSanPham);
        sp.setTenSanPham("Sản phẩm test ID " + idSanPham);
        sp.setGiaBan(100000L);
        sp.setHinhAnh("/img/hero-san-pham.jpg");
        // -----------------------------------------------------------------------------------

        if (sp != null) {
            Cart cart = getCart(session);
            CartItem item = new CartItem(sp, soLuong);
            cart.themVaoGio(item);
            session.setAttribute("cart", cart); // Lưu cập nhật vào session
        }

        return "redirect:/gio-hang"; // Thêm xong thì chuyển hướng về trang giỏ hàng
    }

    // 3. Xóa sản phẩm khỏi giỏ
    @GetMapping("/xoa/{idSanPham}")
    public String xoaKhoiGio(@PathVariable("idSanPham") Long idSanPham, HttpSession session) {
        Cart cart = getCart(session);
        cart.xoaKhoiGio(idSanPham);
        session.setAttribute("cart", cart);
        return "redirect:/gio-hang";
    }
}