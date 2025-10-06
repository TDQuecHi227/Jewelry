package com.hhd.jewelry.controller;

import com.hhd.jewelry.DTO.RegisterDto;
import com.hhd.jewelry.entity.User;
import com.hhd.jewelry.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public LoginController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "client/homepage/login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new RegisterDto());
        }
        return "client/homepage/register";
    }

    @PostMapping("/register")
    public String postRegisterPage(@Valid @ModelAttribute("form") RegisterDto form, BindingResult br, Model model, RedirectAttributes ra) {
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
            log.info("Saved user id={}", u.getId());
        } catch (Exception e) {
            log.error("Save user failed", e);
            model.addAttribute("error", "Không thể lưu người dùng: " + e.getMessage());
            return "client/homepage/register";
        }

        ra.addFlashAttribute("registered", "Đăng ký thành công. Hãy đăng nhập.");
        return "redirect:/login";
    }

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
