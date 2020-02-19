package Ass.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("pages/")
public class HomeController {

    @GetMapping("view-home")
    public ModelAndView viewHome(){
        ModelAndView modelAndView = new ModelAndView("/pages/home");
        System.out.println("hrllo");
        return modelAndView;
    }


}
