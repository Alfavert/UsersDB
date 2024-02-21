package UsersDB.TaskUserDB.controllers;

import UsersDB.TaskUserDB.models.UsersDB;
import UsersDB.TaskUserDB.repo.UsersDBRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller

public class UsersController {

    @Autowired
    private UsersDBRepo usersRepository;

    @GetMapping("/")
    public String UsersMain(Model model) {
        Iterable<UsersDB> users = usersRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/add-user")
    public String UsersAdd(Model model) {
        return "user-add";
    }

    @PostMapping("/add-user")
    public String userPostAdd(@RequestParam String FullName, @RequestParam String email, @RequestParam String password, Model model) {
        UsersDB user = new UsersDB(FullName, email, password);
        usersRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String UsersDetails(@PathVariable(value = "id") long id, Model model) {
        if (!usersRepository.existsById(id)) {
            return "redirect:/";
        } else {
            Optional<UsersDB> user = usersRepository.findById(id);
            ArrayList<UsersDB> res = new ArrayList<>();
            user.ifPresent(res::add);
            model.addAttribute("user", res);
            return "user-details";
        }
    }

    @GetMapping("/{id}/edit")
    public String UsersEdit(@PathVariable(value = "id") long id, Model model) {
        if (!usersRepository.existsById(id)) {
            return "redirect:/";
        } else {
            Optional<UsersDB> user = usersRepository.findById(id);
            ArrayList<UsersDB> res = new ArrayList<>();
            user.ifPresent(res::add);
            model.addAttribute("user", res);
            return "user-edit";
        }
    }

    @PostMapping("/{id}/edit")
    public String userUpdate(@PathVariable(value = "id") long id,@RequestParam String FullName, @RequestParam String email, @RequestParam String password, Model model) {
        UsersDB user = usersRepository.findById(id).orElseThrow();
        user.setEmail(email);
        user.setFullName(FullName);
        user.setPassword(password);
        usersRepository.save(user);
        return "redirect:/";
    }
    @PostMapping("/{id}/remove")
    public String userDelete(@PathVariable(value = "id") long id, Model model) {
        UsersDB user = usersRepository.findById(id).orElseThrow();
        usersRepository.delete(user);
        return "redirect:/";
    }
}