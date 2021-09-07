package io.zhengweiliu.ppmtool.validator;

import io.zhengweiliu.ppmtool.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals((aClass));
    }


    @Override
    public void validate(Object object, Errors errors) {
        User user = (User) object;
        if(user.getPassword()==null|| user.getPassword().length()<6){
            errors.rejectValue("password","Length","Password mush be at least 6 characters");
        }
        if(user.getPassword()!=null && user.getConfirmPassword()!=null && !user.getPassword().equals((user.getConfirmPassword()))){
            errors.rejectValue("confirmPassword","Match","Password mush match");
        }

    }
}
