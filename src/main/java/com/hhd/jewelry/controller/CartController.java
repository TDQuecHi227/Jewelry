package com.hhd.jewelry.controller;

import com.hhd.jewelry.entity.Cart;
import com.hhd.jewelry.entity.CartItem;
import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.repository.CartRepository;
import com.hhd.jewelry.repository.UserRepository;
import com.hhd.jewelry.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Stream;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final ProductService productService;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @GetMapping("/cart")
    public String  getCartPage(Model model, Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        Cart cart = cartRepository.findByUser(user);
        List<CartItem> cartItems = cart.getItems();
        int subtotal = 0;
        int discount = 0; // tuỳ bạn tính
        int total = 0;
        model.addAttribute("carts", cartItems);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("discount", discount);
        model.addAttribute("total", total);

        return "client/product/cart";
    }

    @PostMapping(value="/cart/add/{serialnumber}", params="ajax=1")
    @ResponseBody
    public Map<String, Object> addToCartAjax(@PathVariable String serialnumber,
                                             Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return Map.of("ok", false, "reason", "unauthenticated");
        }
        Product p = productService.getProductBySerialNumber(serialnumber);
        if (p == null) return Map.of("ok", false, "reason", "not_found");

        productService.AddProductToCart(auth.getName(), p.getSerialNumber());

        // Tính lại tổng số lượng để trả về
        var user = userRepository.findByEmail(auth.getName()).orElse(null);
        var cart = (user == null) ? null : cartRepository.findByUser(user);
        int count = 0;
        if (cart != null && cart.getItems() != null) {
            count = cart.getItems().stream()
                    .map(ci -> ci.getQuantity() == null ? 0 : ci.getQuantity())
                    .reduce(0, Integer::sum);
        }
        return Map.of("ok", true, "count", count);
    }
    @PostMapping("/cart/update")
    @ResponseBody
    @Transactional
    public Map<String, Object> updateCartAjax(@RequestParam Integer itemId,
                                                  @RequestParam Integer qty,
                                                  @RequestParam(value = "selectedIds", required = false) List<Integer> selectedIds,
                                                  Authentication auth) {
        // 1) Xác thực
        if (auth == null || !auth.isAuthenticated()) {
            return Map.of("ok", false, "reason", "unauthenticated");
        }
        if (qty == null || qty <= 0) qty = 1;

        // 2) Lấy user + cart
        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) return Map.of("ok", false, "reason", "no_user");
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) return Map.of("ok", false, "reason", "no_cart");

        // 3) Tìm cart item cần cập nhật
        CartItem updated = cart.getItems().stream()
                .filter(ci -> Objects.equals(ci.getId(), itemId))
                .findFirst()
                .orElse(null);
        if (updated == null) return Map.of("ok", false, "reason", "no_item");

        // 4) Cập nhật số lượng & lưu
        updated.setQuantity(qty);
        // Do @OneToMany(cascade = ALL) trên Cart → save cart là đủ
        cartRepository.save(cart);

        // 5) Tính lineTotal của dòng vừa cập nhật
        int lineTotal = (updated.getPrice() == null ? 0 : updated.getPrice())
                * (updated.getQuantity() == null ? 0 : updated.getQuantity());

        // 6) Tính subtotal theo "selectedIds"
        Stream<CartItem> stream = cart.getItems().stream();
        // Nếu client gửi selectedIds (kể cả rỗng) → chỉ tính các id đó
        int subtotal = 0;
        if (selectedIds != null) {
            Set<Integer> selectedSet = new HashSet<>(selectedIds);
            stream = stream.filter(ci -> selectedSet.contains(ci.getId()));
            subtotal = stream
                    .mapToInt(ci -> (ci.getPrice() == null ? 0 : ci.getPrice())
                            * (ci.getQuantity() == null ? 0 : ci.getQuantity()))
                    .sum();
        }
        int discount = 0;
        int total = subtotal - discount;

        // 7) Trả JSON cho client
        Map<String, Object> res = new HashMap<>();
        res.put("ok", true);
        res.put("itemId", itemId);
        res.put("qty", updated.getQuantity());
        res.put("lineTotal", lineTotal);
        res.put("subtotal", subtotal);
        res.put("discount", discount);
        res.put("total", total);
        return res;
    }

    @GetMapping("/cart/remove/{id}")
    public String removeItem(@PathVariable("id") Integer id,
                             Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) return "redirect:/login";

        User user = userRepository.findByEmail(auth.getName()).orElse(null);
        if (user == null) return "redirect:/cart";
        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getItems() == null) return "redirect:/cart";
        productService.RemoveProductToCart(cart.getCartId(), id);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String getCheckOut(Model model){

        return "client/product/checkout";
    }
}

