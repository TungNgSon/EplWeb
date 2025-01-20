package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {

        if (error != null) {
            model.addAttribute("error", "Invalid username or password, or account not verified.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // Trả về form đăng ký
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Kiểm tra nếu username đã tồn tại
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username đã tồn tại!");
            return "register";
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Đặt mặc định role là "USER"
        user.setVerified(false);

        // Tạo mã xác nhận
        String verificationCode = UUID.randomUUID().toString();
        user.setVerificationCode(verificationCode);

        // Lưu user vào database
        userRepository.save(user);

        // Gửi email xác nhận
        sendVerificationEmail(user);

        return "redirect:/login"; // Chuyển hướng về trang đăng nhập sau khi đăng ký thành công
    }

    private void sendVerificationEmail(User user) {
        String toAddress = user.getUsername();
        String subject = "Please verify your registration";
        String content = "Dear " + user.getUsername() + ",\n"
                + "Please click the link below to verify your registration:\n"
                + "http://localhost:8080/verify?code=" + user.getVerificationCode() + "\n"
                + "Thank you,\n"
                + "Your Company";
        //String content="xinchao";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("code") String code, Model model) {
        User user = userRepository.findByVerificationCode(code);

        if (user == null) {
            model.addAttribute("message", "Invalid verification code.");
            return "error";
        }

        user.setVerificationCode(null);
        user.setVerified(true);
        userRepository.save(user);
        model.addAttribute("message", "Your account has been verified successfully.");
        return "success";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied"; // Trả về template theo tên "access-denied.html"
    }
}