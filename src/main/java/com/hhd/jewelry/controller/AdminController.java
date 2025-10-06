package com.hhd.jewelry.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminController {
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model){
        // Fake stats
        var stats = Map.of(
                "totalUsers", 120,
                "totalProducts", 58,
                "activeProducts", 40,
                "totalOrders", 230,
                "revenueToday", 12500000,
                "revenueThisMonth", 98000000,
                "orderTrendUp", true,
                "deltaUsersWeek", 12
        );

        // Fake đơn hàng mới
        record RecentOrder(Long id, String code, String customerName, Date createdAt, Double total, String status) {}
        List<RecentOrder> recentOrders = List.of(
                new RecentOrder(1L, "ORD-001", "Nguyễn Văn A", new Date(), 1500000.0, "PAID"),
                new RecentOrder(2L, "ORD-002", "Trần Thị B", new Date() , 2750000.0, "PENDING")
        );

        // Fake sản phẩm sắp hết hàng
        record LowStock(Long id, String name, String thumbnailUrl, Integer qty) {}
        List<LowStock> lowStocks = List.of(
                new LowStock(10L, "Nhẫn vàng PNJ", "/images/products/nhan.jpg", 5),
                new LowStock(11L, "Vòng tay bạc", "/images/products/vong.jpg", 2)
        );

        // Fake chart
        Map<String, Object> ordersByDay = Map.of(
                "labels", List.of("T2","T3","T4","T5","T6","T7","CN"),
                "values", List.of(5, 8, 12, 6, 14, 20, 9)
        );
        Map<String, Object> revenueByCategory = Map.of(
                "labels", List.of("Nhẫn","Dây chuyền","Vòng tay","Bông tai"),
                "values", List.of(12000000, 9000000, 7000000, 3000000)
        );

        // Add vào model
        model.addAttribute("stats", stats);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("lowStocks", lowStocks);
        model.addAttribute("chart", Map.of(
                "ordersByDay", ordersByDay,
                "revenueByCategory", revenueByCategory
        ));
        return "manager/dashboard/dashboard";
    }
    @GetMapping("/admin/orders")
    public String listOrders(Model model) {
        // Fake đơn hàng
        record OrderVM(Long id, String code, String customerName, String customerPhone,
                       Date createdAt, Double total, String status) {}

        List<OrderVM> orders = List.of(
                new OrderVM(1L, "ORD-001", "Nguyễn Văn A", "0901234567",
                        new Date(), 1500000.0, "PENDING"),
                new OrderVM(2L, "ORD-002", "Trần Thị B", "0987654321",
                        new Date(), 2750000.0, "PAID"),
                new OrderVM(3L, "ORD-003", "Lê Văn C", "0911222333",
                        new Date(), 3250000.0, "SHIPPING"),
                new OrderVM(4L, "ORD-004", "Phạm Thị D", "0933444555",
                        new Date(), 5000000.0, "COMPLETED")
        );

        model.addAttribute("orders", orders);
        return "manager/dashboard/order";
    }
    @GetMapping("/admin/products")
    public String listProducts(Model model) {
        // Fake sản phẩm
        record ProductVM(Long id, String code, String name, String categoryName,
                         Double price, Integer stock, String thumbnailUrl) {
        }

        List<ProductVM> products = List.of(
                new ProductVM(1L, "SP001", "Nhẫn vàng PNJ", "Nhẫn", 5200000.0, 5,
                        "/images/products/nhan.jpg"),
                new ProductVM(2L, "SP002", "Dây chuyền bạc Ý", "Dây chuyền", 1800000.0, 12,
                        "/images/products/daychuyen.jpg"),
                new ProductVM(3L, "SP003", "Bông tai ngọc trai", "Bông tai", 1250000.0, 8,
                        "/images/products/bongtai.jpg"),
                new ProductVM(4L, "SP004", "Vòng tay charm", "Vòng tay", 2250000.0, 3,
                        "/images/products/vongtay.jpg")
        );

        model.addAttribute("products", products);
        return "manager/dashboard/product";
    }
    @GetMapping("/admin/categories")
    public String listCategories(Model model) {
        // Fake danh mục
        record CategoryVM(Long id, String name, String description, Integer productCount) {}

        List<CategoryVM> categories = List.of(
                new CategoryVM(1L, "Nhẫn", "Trang sức đeo tay bằng vàng, bạc, đá quý", 12),
                new CategoryVM(2L, "Dây chuyền", "Dây chuyền vàng, bạc, kiểu dáng đa dạng", 8),
                new CategoryVM(3L, "Bông tai", "Hoa tai, khuyên tai từ vàng, bạc, ngọc trai", 15),
                new CategoryVM(4L, "Vòng tay", "Vòng tay charm, lắc tay cao cấp", 6)
        );

        model.addAttribute("categories", categories);
        return "manager/dashboard/category";
    }
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        // Fake user data
        record UserVM(Long id, String fullName, String email, String role, boolean active) {}

        List<UserVM> users = List.of(
                new UserVM(1L, "Nguyễn Văn A", "a@example.com", "ADMIN", true),
                new UserVM(2L, "Trần Thị B", "b@example.com", "MANAGER", true),
                new UserVM(3L, "Lê Văn C", "c@example.com", "CUSTOMER", false),
                new UserVM(4L, "Phạm Thị D", "d@example.com", "STAFF", true)
        );

        model.addAttribute("users", users);
        return "manager/dashboard/user";
    }
    @GetMapping("/admin/vendors")
    public String listVendors(Model model) {
        // Fake vendors
        record VendorVM(Long id, String name, String email, String phone,
                        String address, Integer productCount) {}

        List<VendorVM> vendors = List.of(
                new VendorVM(1L, "PNJ Supplier", "supplier@pnj.vn", "0901234567",
                        "123 Lê Lợi, Q.1, TP.HCM", 20),
                new VendorVM(2L, "SJC Gold", "contact@sjc.com.vn", "0987654321",
                        "45 Nguyễn Huệ, Q.1, TP.HCM", 15),
                new VendorVM(3L, "DOJI Silver", "info@doji.vn", "0911222333",
                        "88 Hai Bà Trưng, Q.3, TP.HCM", 10)
        );

        model.addAttribute("vendors", vendors);
        return "manager/dashboard/vendor";
    }
    @GetMapping("/admin/stock")
    public String listStockMovements(Model model) {
        // Fake stock movements
        record StockMovementVM(Long id, String productName, String type,
                               Integer quantity, Date date, String performedBy) {}

        List<StockMovementVM> movements = List.of(
                new StockMovementVM(1L, "Nhẫn vàng PNJ", "IMPORT", 10, new Date(), "Nguyễn Văn A"),
                new StockMovementVM(2L, "Dây chuyền bạc Ý", "EXPORT", 3, new Date(), "Trần Thị B"),
                new StockMovementVM(3L, "Vòng tay charm", "IMPORT", 5, new Date(), "Phạm Văn C"),
                new StockMovementVM(4L, "Bông tai ngọc trai", "EXPORT", 2, new Date(), "Lê Thị D")
        );

        model.addAttribute("movements", movements);
        return "manager/dashboard/stock";
    }
    @GetMapping("/admin/audits")
    public String listAudits(Model model) {
        // Fake audit logs
        record AuditVM(Long id, String user, String role,
                       String action, String detail, Date time, String ip) {}

        List<AuditVM> audits = List.of(
                new AuditVM(1L, "Nguyễn Văn A", "ADMIN", "LOGIN", "Đăng nhập thành công", new Date(), "192.168.1.10"),
                new AuditVM(2L, "Trần Thị B", "MANAGER", "CREATE", "Tạo đơn hàng ORD-002", new Date(), "192.168.1.11"),
                new AuditVM(3L, "Lê Văn C", "STAFF", "UPDATE", "Cập nhật sản phẩm SP003", new Date(), "192.168.1.12"),
                new AuditVM(4L, "Phạm Thị D", "ADMIN", "DELETE", "Xóa người dùng ID=5", new Date(), "192.168.1.13")
        );

        model.addAttribute("audits", audits);
        return "manager/dashboard/audit";
    }
}
