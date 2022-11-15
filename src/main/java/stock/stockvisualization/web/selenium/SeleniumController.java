package stock.stockvisualization.web.selenium;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;
import stock.stockvisualization.domain.member.Member;
import stock.stockvisualization.domain.member.JdbcMemberRepository;
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
    private final JdbcMemberRepository memberRepository;
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
        member1.setUserPassword("test");
        memberRepository.save(member1);
        //companyService.addIndustryCode();
        seleniumTest.saveCrawlingData();


        //jsoupTest();
        //Company find_company = seleniumTest.setCompany("삼성전자", "2022", "11013");

//        Company find_company = seleniumTest.setCompany("삼성전자", "2022", "11013");

//        Optional<Member> member = memberRepository.findByLoginId("test");
//        Member mem = member.get();
//        List<Company> company = mem.getCompany();
//        company.add(find_company);
//        log.info(member1.getCompany().toString());

//        companyService.addIndustryCode();


    }

    public void jsoupTest() throws IOException, ParseException {//
        String url = "https://opendart.fss.or.kr/api/fnlttSinglAcnt.xml?crtfc_key=12ed28c5a1ccad901a74e01b506d6690588f73f5&corp_code=00266961&bsns_year=2022&reprt_code=11012";
        String url2 = "https://opendart.fss.or.kr/api/company.xml?crtfc_key=12ed28c5a1ccad901a74e01b506d6690588f73f5&corp_code=00126380";
        String stock_code=null;
        Document document = Jsoup.connect(url).get();

        String status = document.select("status").text();
        if(status.equals("013"))
        {
            System.out.println("데이터 없음");

        }
        /**
         *     String name;
         *     Integer corpcode;
         *     int bsns_year;
         *     String account_nm;
         *     String fs_div;
         *     <sj_nm>재무상태표</sj_nm>
         *     int thstrm_amount;
         *     int frmtrm_amount;
         */
        int size = document.select("list").size();
        String frmtrm_add_amount = document.select("list").get(0).select("frmtrm_add_amount").text();



        System.out.println("frmtrm_add_amount = " + frmtrm_add_amount);
        for(int i =size; i < size; i++){
            System.out.println("-------------------------------------------------------------");
            String corpcode = document.select("corp_code").get(i).text();
            String bsns_year = document.select("bsns_year").get(i).text();
            String account_nm = document.select("account_nm").get(i).text();
            String fs_div = document.select("fs_div").get(i).text();
            String sj_nm = document.select("sj_nm").get(i).text();
            String thstrm_amount = document.select("thstrm_amount").get(i).text();
            String frmtrm_amount = document.select("frmtrm_amount").get(i).text();
            System.out.println("corpcode = " + corpcode);
            System.out.println("bsns_year = " + bsns_year);
            System.out.println("frmtrm_amount = " + frmtrm_amount);
            System.out.println("thstrm_amount = " + thstrm_amount);
            System.out.println("-------------------------------------------------------------");
        }



    }


}
