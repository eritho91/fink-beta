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

        if (principal == null) {
            return "redirect:/";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        List<BirdPost> birds = birdPostRepository.findAll().stream()
                .filter(b -> b.getUser() != null && b.getUser().getId().equals(user.getId()))
                .toList();

        model.addAttribute("birds", birds);
        model.addAttribute("user", user);

        return "birds";
    }

    @GetMapping("/new")
    public String newBirdForm(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        BirdPost bird = new BirdPost();
        bird.setUser(user);

        model.addAttribute("bird", bird);
        model.addAttribute("user", user);

        return "newBirdPost";
    }

    @PostMapping
    public String saveBird(@ModelAttribute BirdPost bird, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        bird.setUser(user);
        birdPostRepository.save(bird);

        return "redirect:/birds";
    }

    @PostMapping("/delete/{id}")
    public String deleteBird(@PathVariable Long id, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();

        BirdPost bird = birdPostRepository.findById(id).orElseThrow();

        if (bird.getUser() != null && bird.getUser().getId().equals(user.getId())) {
            birdPostRepository.delete(bird);
        }

        return "redirect:/birds";
    }

    @GetMapping("/edit/{id}")
    public String editBirdForm(@PathVariable Long id, Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        BirdPost bird = birdPostRepository.findById(id).orElseThrow();

        if (bird.getUser() == null || !bird.getUser().getId().equals(user.getId())) {
            return "redirect:/birds";
        }

        model.addAttribute("bird", bird);
        model.addAttribute("user", user);

        return "editBirdPost";
    }

    @PostMapping("/edit/{id}")
    public String updateBird(@PathVariable Long id, @ModelAttribute BirdPost updatedBird, Principal principal) {

        if (principal == null) {
            return "redirect:/";
        }

        User user = userRepository.findByUsername(principal.getName()).orElseThrow();
        BirdPost existingBird = birdPostRepository.findById(id).orElseThrow();

        if (existingBird.getUser() == null || !existingBird.getUser().getId().equals(user.getId())) {
            return "redirect:/birds";
        }

        existingBird.setSpecies(updatedBird.getSpecies());
        existingBird.setLocation(updatedBird.getLocation());
        existingBird.setCreatedAt(updatedBird.getCreatedAt());

        birdPostRepository.save(existingBird);

        return "redirect:/birds";
    }


}