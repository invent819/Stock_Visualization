package stock.stockvisualization.web.selenium;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import stock.stockvisualization.domain.company.Company;
import stock.stockvisualization.domain.member.Member;
import stock.stockvisualization.domain.member.MemberRepository;
import stock.stockvisualization.web.SessionConst;
import stock.stockvisualization.web.company.CompanyForm;
import stock.stockvisualization.web.company.CompanyService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;


@Controller
@Slf4j
@RequiredArgsConstructor
public class SeleniumController {
    private final MemberRepository memberRepository;
    private final SeleniumService seleniumTest;
    private final CompanyService companyService;
    @ModelAttribute("likeCompanies")
    public Map<String, String>likeCompanies(){
        Map<String, String> likeCompanies = new HashMap<>();
        likeCompanies.put("SAMSUNG", "삼성");
        likeCompanies.put("LG", "엘쥐");
        likeCompanies.put("DUSAN", "두산");

        return likeCompanies;
    }
    @GetMapping
    public String home(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);
        if(session == null){
            return "index";
        }
        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember == null){
            return "index";
        }
        model.addAttribute("member", loginMember);
        model.addAttribute("companies", new CompanyForm());
        return "loginHome";

    }

    @PostMapping
    public String addCompany(){

        return "";
    }



    @PostConstruct
    public void crawlingTest() throws ParserConfigurationException, IOException, SAXException, ParseException {

        Member member1 = new Member();
        member1.setLoginId("test");
        member1.setPassword("test");
        member1.setCompanies(new ArrayList<>());
        memberRepository.save(member1);



        Company find_company = seleniumTest.setCompany("삼성전자", "2022", "11013");

//        Company find_company = seleniumTest.setCompany("삼성전자", "2022", "11013");

//        Optional<Member> member = memberRepository.findByLoginId("test");
//        Member mem = member.get();
//        List<Company> company = mem.getCompany();
//        company.add(find_company);
//        log.info(member1.getCompany().toString());

//        companyService.addIndustryCode();


    }



}
