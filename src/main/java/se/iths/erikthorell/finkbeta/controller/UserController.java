package se.iths.erikthorell.finkbeta.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.iths.erikthorell.finkbeta.model.User;
import se.iths.erikthorell.finkbeta.repository.UserRepository;

import java.io.IOException;
import java.nio.file.*;
import java.security.Principal;
import java.util.UUID;

@Controller
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @PostMapping("/profile/upload")
    public String uploadProfileImage(@RequestParam("image") MultipartFile image, Principal principal) throws IOException {
        if (principal == null) {
            return "redirect:/";
        }

        if (image.isEmpty()) {
            return "redirect:/home";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        String uploadDir = "uploads";
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = image.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFileName = UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(newFileName);

        Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        user.setProfileImageName(newFileName);
        userRepository.save(user);

        return "redirect:/home";
    }
}