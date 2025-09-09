package com.hhd.jewelry.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class RegisterDto {

    @NotBlank(message = "Vui lòng nhập họ và tên")
    private String fullname;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Vui lòng nhập email")
    private String email;

    // Cho phép rỗng hoặc số VN phổ biến (tùy bạn)
    @Pattern(regexp = "^$|^(0|\\+?84)\\d{8,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Vui lòng chọn giới tính")
    private String gender; // MALE/FEMALE/OTHER

    @NotNull(message = "Vui lòng chọn ngày sinh")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateOfBirth;

    @Size(max = 255, message = "Địa chỉ quá dài")
    private String address;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu")
    private String confirmPassword;

    public @NotBlank(message = "Vui lòng nhập họ và tên") String getFullname() {
        return fullname;
    }

    public void setFullname(@NotBlank(message = "Vui lòng nhập họ và tên") String fullname) {
        this.fullname = fullname;
    }

    public @Email(message = "Email không hợp lệ") @NotBlank(message = "Vui lòng nhập email") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Email không hợp lệ") @NotBlank(message = "Vui lòng nhập email") String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^$|^(0|\\+?84)\\d{8,10}$", message = "Số điện thoại không hợp lệ") String getPhone() {
        return phone;
    }

    public void setPhone(@Pattern(regexp = "^$|^(0|\\+?84)\\d{8,10}$", message = "Số điện thoại không hợp lệ") String phone) {
        this.phone = phone;
    }

    public @NotBlank(message = "Vui lòng chọn giới tính") String getGender() {
        return gender;
    }

    public void setGender(@NotBlank(message = "Vui lòng chọn giới tính") String gender) {
        this.gender = gender;
    }

    public @NotNull(message = "Vui lòng chọn ngày sinh") LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull(message = "Vui lòng chọn ngày sinh") LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public @Size(max = 255, message = "Địa chỉ quá dài") String getAddress() {
        return address;
    }

    public void setAddress(@Size(max = 255, message = "Địa chỉ quá dài") String address) {
        this.address = address;
    }

    public @NotBlank(message = "Vui lòng nhập mật khẩu") @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Vui lòng nhập mật khẩu") @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Vui lòng xác nhận mật khẩu") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank(message = "Vui lòng xác nhận mật khẩu") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}