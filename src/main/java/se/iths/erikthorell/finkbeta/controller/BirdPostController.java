package se.iths.erikthorell.finkbeta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.iths.erikthorell.finkbeta.model.BirdPost;
import se.iths.erikthorell.finkbeta.model.User;
import se.iths.erikthorell.finkbeta.repository.BirdPostRepository;
import se.iths.erikthorell.finkbeta.repository.UserRepository;

@Controller
@RequestMapping("/birds")
public class BirdPostController {

    private final BirdPostRepository birdPostRepository;
    private final UserRepository userRepository;

    public BirdPostController(BirdPostRepository birdPostRepository, UserRepository userRepository) {
        this.birdPostRepository = birdPostRepository;
        this.userRepository = userRepository;
    }

    // Lista alla fåglar för en användare
    @GetMapping
    public String listBirds(@RequestParam Long userId, Model model) {
        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("birds", birdPostRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(userId)).toList());
        model.addAttribute("user", user);
        return "birds";
    }

    // Formulär för ny fågelpost
    @GetMapping("/new")
    public String newBird(@RequestParam Long userId, Model model) {
        BirdPost bird = new BirdPost();
        bird.setUser(userRepository.findById(userId).orElseThrow());
        model.addAttribute("bird", bird);
        return "newBirdPost";
    }

    // Spara ny fågelpost
    @PostMapping
    public String saveBird(@ModelAttribute BirdPost bird) {
        birdPostRepository.save(bird);
        return "redirect:/birds?userId=" + bird.getUser().getId();
    }
}