package stock.stockvisualization.web.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stock.stockvisualization.domain.login.LoginService;
import stock.stockvisualization.domain.member.Member;
import stock.stockvisualization.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm form){
        return "login/loginForm";
    }
    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm form,
                        BindingResult bindingResult, HttpServletRequest request ){
        if(bindingResult.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(),form.getPassword());
        if (loginMember == null){
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //세션이 있으면 있는 세션 반환, 없으면 신규 세선 생성
        HttpSession session = request.getSession();

        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";

    }
    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        //세션을 삭제한다.
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }


}
