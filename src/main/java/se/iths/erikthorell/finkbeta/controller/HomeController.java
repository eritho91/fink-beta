package se.iths.erikthorell.finkbeta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import se.iths.erikthorell.finkbeta.model.User;
import se.iths.erikthorell.finkbeta.repository.UserRepository;

import java.security.Principal;

@Controller
public class HomeController {

    private final UserRepository userRepository;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        if (principal == null) {
            return "index"; // visa login istället, ingen redirect
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow();

        model.addAttribute("user", user);
        return "home";
    }
}