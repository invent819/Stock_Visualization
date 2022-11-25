package stock.stockvisualization.web.company;

import com.beust.jcommander.IParameterizedParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import stock.stockvisualization.domain.company.Company;
import stock.stockvisualization.domain.company.CompanyRepository;
import stock.stockvisualization.web.selenium.SeleniumService;
import stock.stockvisualization.domain.company.CompanyFinancialInfo;
import stock.stockvisualization.domain.member.Member;
import stock.stockvisualization.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {
    private final CompanyRepository companyRepository;
    private final SeleniumService seleniumService;

    @ModelAttribute("likeCompanies")
    public Map<String, String> likeCompanies() {
        Map<String, String> likeCompanies = new HashMap<>();
        likeCompanies.put("SAMSUNG", "삼성");
        likeCompanies.put("LG", "엘쥐");
        likeCompanies.put("DUSAN", "두산");

        return likeCompanies;
    }

    @GetMapping
    @ResponseBody
    public List<Company> Companies() {
        List<Company> companyList = companyRepository.findAll(new Company());
        return companyList;
    }
    @GetMapping("/companyFinancialInfo")
    @ResponseBody
    public List<CompanyFinancialInfo> companyFinancialInfo(@RequestParam Company companyCond){

        return null;
    }

    @GetMapping("/add")
    public String getCompany(
            HttpServletRequest request,
            @ModelAttribute CompanyFinancialInfo company, Model model) {

        //세션이 없으면 index
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "redirect:/";
        }
        //세션에 회원 데이터가 없으면 index
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember == null) {
            return "redirect:/";
        }

        model.addAttribute("member", loginMember);
        return "companies/searchCompany";
    }

    @PostMapping("/add")
    public String addCompany(@ModelAttribute Member addMember,
                             Model model,
                             HttpServletRequest request) throws ParserConfigurationException, IOException, ParseException, SAXException {
//        seleniumService.setCompany(find_corp_name, bsns_year, reprt_code);
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        System.out.println("addMember = " + addMember);
        //HashSet<Company> companies = member.getCompanies();
        System.out.println("member = " + member);
        return "";
    }

}


