package Ass.Controller;

import Ass.Model.Users;
import Ass.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("pages/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("login")
    public ModelAndView viewLogin() {
        ModelAndView modelAndView = new ModelAndView("/pages/login");
        modelAndView.addObject("user", new Users());
        return modelAndView;
    }

    @PostMapping("login")
    public ModelAndView login(@ModelAttribute("User") Users users) {
        ModelAndView modelAndView;
        if (userService.isUsers(users.getTaiKhoan(), users.getMatKhau())) {
            modelAndView = new ModelAndView("/pages/user");
            modelAndView.addObject("user", userService.findAll());
        } else {
            modelAndView = new ModelAndView("/pages/login");
            modelAndView.addObject("user", new Users());
        }
        return modelAndView;
    }

    @GetMapping("view-user")
    public ModelAndView viewUser() {
        ModelAndView modelAndView = new ModelAndView("/pages/user");
        modelAndView.addObject("user", userService.findAll());
        return modelAndView;
    }

    @GetMapping("create-user")
    public ModelAndView viewPagesCreateUser(){
        ModelAndView modelAndView = new ModelAndView("/pages/create-user");
        modelAndView.addObject("user", new Users());
        return modelAndView;
    }

    @PostMapping("create-user")
    public ModelAndView createUser (@ModelAttribute("User") Users users){
        userService.save(users);
        ModelAndView modelAndView = new ModelAndView("/pages/user");
        modelAndView.addObject("user", userService.findAll());
        return modelAndView;
    }

    @GetMapping("update-user/{id}")
    public ModelAndView viewUpdate(@PathVariable("id") Long id){
        ModelAndView modelAndView = new ModelAndView("/pages/update-user");
        modelAndView.addObject("user", userService.finById(id));
        return modelAndView;
    }

    @PostMapping("update-user/{id}")
    public ModelAndView viewUpdate(@PathVariable("id") Long id, @ModelAttribute("User") Users users){
        users.setId(id);
        userService.save(users);
        ModelAndView modelAndView = new ModelAndView("/pages/user");
        modelAndView.addObject("user", userService.findAll());
        return modelAndView;
    }

    @GetMapping("remove-user/{id}")
    public ModelAndView removeUser(@PathVariable("id") Long id) {
        userService.remove(id);
        ModelAndView modelAndView = new ModelAndView("/pages/user");
        modelAndView.addObject("user", userService.findAll());
        return modelAndView;
    }
}
