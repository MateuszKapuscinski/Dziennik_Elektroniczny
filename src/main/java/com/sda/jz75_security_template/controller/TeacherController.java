package com.sda.jz75_security_template.controller;

import com.sda.jz75_security_template.exception.InvalidRegisterData;
import com.sda.jz75_security_template.model.account.CreateStudentAccountRequest;
import com.sda.jz75_security_template.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/teacher")
@PreAuthorize(value = "hasRole('SUPERVISOR')")
@RequiredArgsConstructor
public class TeacherController {
    private final AccountService accountService;

    @GetMapping("/register/student")
    public String getRegisterPage(){
        return "register-student";
    }

    @PostMapping("/register/student")
    public String submitRegisterPage(Model model, CreateStudentAccountRequest request){
        try{
            boolean success = accountService.registerStudent(request);
            if(success) {
                return "redirect:/login";
            }
        }catch (InvalidRegisterData ird){
            model.addAttribute("error_msg", ird.getMessage());
            model.addAttribute("prev_user", request.getUsername());
        }
        return "register-student";
    }

}
