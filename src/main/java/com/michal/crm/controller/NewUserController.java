package com.michal.crm.controller;

import com.michal.crm.dto.ProfileDto;
import com.michal.crm.model.types.ResultTypes;
import com.michal.crm.service.HelperService;
import com.michal.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/registration")
public class NewUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private HelperService helperService;

    @RequestMapping(value = "/createUser")
    public String showRegistrationForm(Model model, @ModelAttribute("flashAtt") Object flashAtt) {
        model = helperService.addTwoResultAttsForToast(model, flashAtt, "showErrorMatchToast", "showNotAgreeToast", ResultTypes.PASS_NOT_MATCH, ResultTypes.NO_AGREE_TERMS);
        model.addAttribute("profileDto", new ProfileDto());

        return "createUser";
    }

    @RequestMapping(value = "/createNewUser")
    public RedirectView createUser(@ModelAttribute(value = "profileDto") ProfileDto profileDto, RedirectAttributes attributes) {
        ResultTypes resultTypes = userService.registerNewUser(profileDto);

        if (resultTypes == ResultTypes.OK)
            return new RedirectView("/login");

        attributes.addFlashAttribute("flashAtt", resultTypes.getNumber());
        return new RedirectView("/registration/createUser");
    }
}
