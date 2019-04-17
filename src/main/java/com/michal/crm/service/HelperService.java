package com.michal.crm.service;

import com.michal.crm.model.types.ResultTypes;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class HelperService {

//    public Model addPassResultAtts(Model model, Object flashAtt){
//
//        if (flashAtt.equals(ResultTypes.PASS_NOT_MATCH.getNumber())) {
//            model.addAttribute("showErrorWPassToast", false);
//            model.addAttribute("showErrorMatchToast", true);
//        } else if (flashAtt.equals(ResultTypes.WRONG_PASS.getNumber())) {
//            model.addAttribute("showErrorWPassToast", true);
//            model.addAttribute("showErrorMatchToast", false);
//        } else {
//            model.addAttribute("showErrorWPassToast", false);
//            model.addAttribute("showErrorMatchToast", false);
//        }
//
//        return model;
//    }

    public Model addTwoResultAttsForToast(Model model, Object flashAtt, String att1, String att2, ResultTypes resultTypes1, ResultTypes resultTypes2){
        if (flashAtt.equals(resultTypes1.getNumber())) {
            model.addAttribute(att1, true);
            model.addAttribute(att2, false);
        } else if (flashAtt.equals(resultTypes2.getNumber())) {
            model.addAttribute(att1, false);
            model.addAttribute(att2, true);
        } else {
            model.addAttribute(att1, false);
            model.addAttribute(att2, false);
        }
        return model;
    }
}
