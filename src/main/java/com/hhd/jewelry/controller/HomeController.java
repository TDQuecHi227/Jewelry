package com.hhd.jewelry.controller;

import com.hhd.jewelry.DTO.ProfileDto;
import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Map<String, Object>> products = List.of(
                Map.of("name", "Nhẫn Kim Cương", "price", 15000000, "imageUrl", "/images/category/Nhan.jpg"),
                Map.of("name", "Vòng Tay Vàng", "price", 12000000, "imageUrl", "/images/category/TrangSucBac.jpg"),
                Map.of("name", "Dây Chuyền Bạc", "price", 2500000, "imageUrl", "/images/category/DayChuyen.jpg"),
                Map.of("name", "Bông Tai Ngọc Trai", "price", 3500000, "imageUrl", "/images/category/BongTai.jpg")
        );

        // Tạo list mới có thêm salePrice và discount
        Random random = new Random();
        List<Map<String, Object>> productsWithSale = new ArrayList<>();
        for (Map<String, Object> p : products) {
            int oldPrice = (int) p.get("price");
            int discountPercent = 5 + random.nextInt(6); // 5 → 10
            int newPrice = oldPrice - (oldPrice * discountPercent / 100);

            Map<String, Object> newP = new HashMap<>(p);
            newP.put("oldPrice", oldPrice);
            newP.put("salePrice", newPrice);
            newP.put("discountPercent", discountPercent);
            productsWithSale.add(newP);
        }
        // Danh sách ngày flash sale
        LocalDate start = LocalDate.now();
        List<Map<String, Object>> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = start.plusDays(i);
            LocalDateTime time = date.atTime(12, 0);

            Map<String, Object> item = new HashMap<>();
            item.put("label", time.format(DateTimeFormatter.ofPattern("dd/MM")));
            item.put("datetime", time.toString());
            item.put("today", i == 0);
            days.add(item);
        }
        List<Map<String,Object>> flashDays = days.subList(0, Math.min(5, days.size()));
        model.addAttribute("flashDays", flashDays);
        model.addAttribute("products", productsWithSale);
        model.addAttribute("days", days);

        return "client/homepage/home"; // chỉ 1 file
    }
    @GetMapping("/account")
    public String getAccountPage(Authentication auth, Model model) {
        User u = userRepository.findByEmail(auth.getName()).orElseThrow();

        if (!model.containsAttribute("form")) {
            ProfileDto form = new ProfileDto();
            form.setFullname(u.getFullname());
            form.setEmail(u.getEmail());
            form.setPhone(u.getPhone());
            form.setGender(u.getGender());
            form.setDateOfBirth(u.getDateOfBirth());
            form.setAddress(u.getAddress());
            model.addAttribute("form", form);
        }
        return "client/homepage/account";
    }

    @PostMapping("/account")
    public String postAccountPage(Authentication auth,
                                @Valid @ModelAttribute("form") ProfileDto form,
                                BindingResult br,
                                RedirectAttributes ra,
                                Model model) {
        User u = userRepository.findByEmail(auth.getName()).orElseThrow();

        // trùng email/phone (trừ bản thân)
        if (userRepository.existsByEmailAndIdNot(form.getEmail(), u.getId())) {
            br.rejectValue("email", "Duplicated", "Email đã được dùng bởi tài khoản khác.");
        }
        if (StringUtils.hasText(form.getPhone())
                && userRepository.existsByPhoneAndIdNot(form.getPhone(), u.getId())) {
            br.rejectValue("phone", "Duplicated", "Số điện thoại đã được dùng.");
        }
        if (br.hasErrors()) return "client/homepage/account";

        // map & lưu
        u.setFullname(form.getFullname());
        u.setEmail(form.getEmail());
        u.setPhone(form.getPhone());
        u.setGender(form.getGender());
        u.setDateOfBirth(form.getDateOfBirth());
        u.setAddress(form.getAddress());
        userRepository.save(u);

        ra.addFlashAttribute("saved", "Cập nhật thông tin thành công.");
        return "redirect:/account";
    }
    }


