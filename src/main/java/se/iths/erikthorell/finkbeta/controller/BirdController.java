package se.iths.erikthorell.finkbeta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.iths.erikthorell.finkbeta.model.BirdPost;
import se.iths.erikthorell.finkbeta.model.User;
import se.iths.erikthorell.finkbeta.repository.BirdPostRepository;
import se.iths.erikthorell.finkbeta.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/birds")
public class BirdController {

    private final BirdPostRepository birdPostRepository;
    private final UserRepository userRepository;

    public BirdController(BirdPostRepository birdPostRepository, UserRepository userRepository) {
        this.birdPostRepository = birdPostRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listBirds(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        List<BirdPost> birds = birdPostRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(user.getId()))
                .toList();
        model.addAttribute("birds", birds);
        model.addAttribute("user", user); // alltid med, annars kraschar Thymeleaf
        return "birds";
    }

    @GetMapping("/new")
    public String newBirdForm(Model model, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        BirdPost bird = new BirdPost();
        bird.setUser(user);
        model.addAttribute("bird", bird);
        model.addAttribute("user", user); // för tillbaka-länk
        return "newBirdPost";
    }

    @PostMapping
    public String saveBird(@ModelAttribute BirdPost bird, Principal principal) {
        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        bird.setUser(user);
        birdPostRepository.save(bird);
        return "redirect:/birds";
    }
}