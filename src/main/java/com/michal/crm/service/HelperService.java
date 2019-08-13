package com.michal.crm.service;

import com.michal.crm.model.types.ResultTypes;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class HelperService {

    public Model addTwoResultAttsForToast(Model model, Object flashAtt, String att1, String att2, ResultTypes resultTypes1, ResultTypes resultTypes2) {
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

    public String getRelPath(String path) {
        String startWord = "users_files";
        Pattern word = Pattern.compile(startWord);
        Matcher match = word.matcher(path);

        while (match.find()) {
            return path.substring(match.start() - 1);
        }

        return path;
    }
}
