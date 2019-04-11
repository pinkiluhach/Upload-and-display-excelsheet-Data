package com.springboot.display.excelsheet.controller;


import com.springboot.display.excelsheet.model.User;
import com.springboot.display.excelsheet.service.SpringReadFileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class SpringFileController {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    @Autowired
    private SpringReadFileServiceImpl springReadFileService;

    @RequestMapping(value = "/")
    public String Home(Model model) {
        model.addAttribute("user", new User());
        List<User> users = springReadFileService.findAll();
        model.addAttribute("users", users);
        LOGGER.info("send users list to users.html");
        return "view/users";
    }

    @PostMapping(value = "/fileupload")
    public String uploadFile(@ModelAttribute User user, RedirectAttributes redirectAttributes) throws Exception {
        LOGGER.info("Perform Upload Functionality");
        boolean isFlag = springReadFileService.saveDataFromUpload(user.getFile());
        if (isFlag) {
            redirectAttributes.addFlashAttribute("File Upload Successfully");
            LOGGER.info("File Upload Successfully");
        }
        return "redirect:/";
    }
}
