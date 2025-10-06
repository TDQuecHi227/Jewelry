package com.hhd.jewelry.controller;

import com.hhd.jewelry.entity.Category;
import com.hhd.jewelry.entity.Product;
import com.hhd.jewelry.entity.ViewedProduct;
import com.hhd.jewelry.entity.Collection;
import com.hhd.jewelry.service.*;
import lombok.RequiredArgsConstructor;
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

import java.util.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final CollectionService collectionService;
    private final UserService userService;
    private final ViewedProductService viewedProductService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        List<Product> products = productService.getAllProducts();
        List<Category> categories = categoryService.getAllCategories();
        List<Collection> collections = collectionService.getAllCollections();

        if (authentication != null && authentication.isAuthenticated()) {
            User user = userService.getByEmail(authentication.getName()).orElse(null);
            List<ViewedProduct> viewed = viewedProductService.getViewedProductsByUser(user);

            if (!viewed.isEmpty()) {
                List<Product> productsViewed = viewed.stream()
                        .map(ViewedProduct::getProduct)
                        .toList();
                model.addAttribute("productsViewed", productsViewed);
            }
        }

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("collections", collections);

        return "client/homepage/home";
    }
    @GetMapping("/account")
    public String getAccountPage(Authentication auth, Model model) {
        User u = userRepository.findByEmail(auth.getName()).orElseThrow();

        if (!model.containsAttribute("form")) {
            ProfileDto form = new ProfileDto();
            form.setFullname(u.getFullName());
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
        u.setFullName(form.getFullname());
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


