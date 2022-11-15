package stock.stockvisualization.web.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import stock.stockvisualization.web.SessionConst;
import stock.stockvisualization.domain.member.Member;
import stock.stockvisualization.domain.member.JdbcMemberRepository;
import stock.stockvisualization.domain.member.MemberSaveForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final JdbcMemberRepository memberRepository;
    @GetMapping("/add")
    public String addFrom(@ModelAttribute("member") MemberSaveForm member){
        return "members/addMemberForm";
    }
    @PostMapping("/add")
    public String save(@Validated @ModelAttribute("member") MemberSaveForm member, BindingResult bindingResult,
                       HttpServletRequest request){
        if(bindingResult.hasErrors()){
            return "members/addMemberForm";
        }
        Member savedMember = new Member();
        savedMember.setLoginId(member.getLoginId());
        savedMember.setUserName(member.getName());
        savedMember.setUserPassword(member.getPassword());
        memberRepository.save(savedMember);
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, savedMember);
        return "redirect:/";
    }


}
