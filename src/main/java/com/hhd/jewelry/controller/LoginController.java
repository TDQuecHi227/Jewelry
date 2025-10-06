package com.hhd.jewelry.controller;

import com.hhd.jewelry.DTO.RegisterDto;
import com.hhd.jewelry.entity.Cart;
import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.repository.CartRepository;
import com.hhd.jewelry.repository.UserRepository;
import com.hhd.jewelry.service.OtpService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final CartRepository cartRepository;

    @Autowired
    public LoginController(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           OtpService otpService,
                           CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.cartRepository = cartRepository;
    }

    // ===== LOGIN =====
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/homepage/login";
    }

    // ===== REGISTER (VIEW) =====
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new RegisterDto());
        }
        return "client/homepage/register";
    }

    // ===== GỬI OTP QUA EMAIL =====
    @PostMapping("/register/request-otp")
    @ResponseBody
    public String requestOtp(@RequestParam("email") String email) {
        String v = (email == null) ? "" : email.trim().toLowerCase();

        if (!StringUtils.hasText(v) || !v.endsWith("@gmail.com")) {
            return "INVALID_EMAIL";
        }
        if (userRepository.existsByEmail(v)) {
            return "EXISTS";
        }

        try {
            otpService.sendOtp(v);
            log.info("Sent OTP to {}", v);
            return "OK";
        } catch (Exception e) {
            log.error("Send OTP failed for {}: {}", v, e.getMessage());
            return "FAILED";
        }
    }

    // ===== SUBMIT REGISTER (TẠO USER + CART) =====
    @Transactional
    @PostMapping("/register")
    public String postRegisterPage(
            @Valid @ModelAttribute("form") RegisterDto form,
            BindingResult br,
            Model model,
            RedirectAttributes ra) {

        log.info("POST /register -> form: {}", form);

        if (br.hasErrors()) return "client/homepage/register";

        if (!form.getPassword().equals(form.getConfirmPassword())) {
            br.rejectValue("confirmPassword", "Mismatch", "Mật khẩu xác nhận không khớp.");
            return "client/homepage/register";
        }

        if (userRepository.existsByEmail(form.getEmail())) {
            br.rejectValue("email", "Duplicated", "Email đã được sử dụng.");
            return "client/homepage/register";
        }

        if (StringUtils.hasText(form.getPhone()) && userRepository.existsByPhone(form.getPhone())) {
            br.rejectValue("phone", "Duplicated", "Số điện thoại đã được sử dụng.");
            return "client/homepage/register";
        }

        try {
            // ✅ 1. Tạo và lưu User
            User u = new User();
            u.setFullName(form.getFullName());
            u.setEmail(form.getEmail());
            u.setPhone(form.getPhone());
            u.setGender(form.getGender());
            u.setDateOfBirth(form.getDateOfBirth());
            u.setAddress(form.getAddress());
            u.setPasswordHash(passwordEncoder.encode(form.getPassword()));
            u.setRole(User.Role.USER);
            u.setCreatedAt(LocalDateTime.now());

            userRepository.save(u);

            // ✅ 2. Tạo Cart gắn với User vừa tạo
            Cart cart = new Cart();
            cart.setUser(u);
            cartRepository.save(cart);

            // ✅ 3. Cập nhật lại vào User (2 chiều)
            u.setCart(cart);
            userRepository.save(u);

            log.info("✅ User {} created with cart_id={}", u.getEmail(), cart.getCartId());

        } catch (Exception e) {
            log.error("❌ Save user failed", e);
            model.addAttribute("error", "Không thể lưu người dùng: " + e.getMessage());
            return "client/homepage/register";
        }

        ra.addFlashAttribute("registered", "Đăng ký thành công. Hãy đăng nhập.");
        return "redirect:/login";
    }

    // ===== FORGOT PASSWORD =====
    @GetMapping("/forgot-password")
    public String getForgotPage(Model model) {
        return "client/homepage/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String postForgotPage(@RequestParam("email") String email,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirm,
                                 Model model,
                                 RedirectAttributes ra) {
        email = email == null ? "" : email.trim();

        if (!StringUtils.hasText(email)) {
            model.addAttribute("error", "Vui lòng nhập email.");
            return "client/homepage/forgot-password";
        }

        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống.");
            model.addAttribute("email", email);
            return "client/homepage/forgot-password";
        }

        if (!StringUtils.hasText(password) || password.length() < 8) {
            model.addAttribute("error", "Mật khẩu phải có ít nhất 8 ký tự.");
            model.addAttribute("email", email);
            return "client/homepage/forgot-password";
        }

        if (!password.equals(confirm)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp.");
            model.addAttribute("email", email);
            return "client/homepage/forgot-password";
        }

        User u = userOpt.get();
        u.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(u);

        ra.addFlashAttribute("registered", "Đổi mật khẩu thành công. Hãy đăng nhập bằng mật khẩu mới.");
        return "redirect:/login";
    }
}
