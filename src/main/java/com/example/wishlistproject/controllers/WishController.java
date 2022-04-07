package com.example.wishlistproject.controllers;

import com.example.wishlistproject.models.User;
import com.example.wishlistproject.repositories.WishRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

@Controller
public class WishController {
    private WishRepository wr = new WishRepository();
    private User currentUser;


    @GetMapping("/")
    public String startPage() {
        return "log-in";
    }

    @PostMapping("/verify")
    public String verifyInfo(WebRequest userInfo) {
        String username = userInfo.getParameter("username");
        String password = userInfo.getParameter("password");
        boolean userExistence = wr.doesUserExist(username, password);
        if (userExistence) {
            int id = wr.getUserIDFromDB(username);
            currentUser = new User(id, username, password);
            return "redirect:/homepage";
        } else {
            return "redirect:/wrong-info";
        }
    }

    @GetMapping("/sign-up")
    public String signUp() {
        return "sign-up";
    }

    @PostMapping("/validate")
    public String validate(WebRequest userInfo) {
        String username = userInfo.getParameter("username");
        String password = userInfo.getParameter("password");
        boolean isTaken = wr.isUsernameTaken(username);

        if (isTaken) {
            return "redirect:/username-taken";
        } else {
            wr.createUser(username, password);
            int id = wr.getUserIDFromDB(username);
            currentUser = new User(id, username, password);
            return "redirect:/homepage";
        }
    }

    @PostMapping("/submit-wish")
    public String submitWish(WebRequest wishInfo) {
        String wishName = wishInfo.getParameter("wish");
        String username = currentUser.getUsername();
        wr.insertWish(username, wishName);
        return "redirect:/homepage";
    }

    @GetMapping("/homepage")
    public String homepage(Model m) {
        wr.updateWishesFromUser(currentUser);
        List<String> wishes = currentUser.getWishes();
        m.addAttribute("wishes", wishes);
        return "homepage";
    }

    @GetMapping("/username-taken")
    public String usernameTaken() {
        return "username-taken";
    }

    @GetMapping("/wrong-info")
    public String wrongInfo() {
        return "wrong-info";
    }

    @PostMapping("/log-out")
    public String logOut() {
        currentUser = null;
        return "redirect:/";
    }
}
