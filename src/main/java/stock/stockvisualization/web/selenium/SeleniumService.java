package stock.stockvisualization.web.selenium;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import stock.stockvisualization.domain.company.*;
import stock.stockvisualization.domain.induty.Induty;
import stock.stockvisualization.domain.induty.JdbcIndutyRepository;
import stock.stockvisualization.web.company.CompanyService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Data
@Slf4j
public class SeleniumService {
    private  WebDriver driver;
    private final String API_KEY = "12ed28c5a1ccad901a74e01b506d6690588f73f5";
    private final String COMPANY_INFO_URL = "https://opendart.fss.or.kr/api/company.xml";
    private final String FINANCIAL_URL = "https://opendart.fss.or.kr/api/fnlttSinglAcnt.xml";
    @Autowired
    private final CompanyFinancialInfoRepository companyFinancialInfoRepository;
    @Autowired
    private final CompanyRepository companyRepository;

    @Autowired
    private final JdbcIndutyRepository indutyRepository;

    private final CompanyService companyService;

    public SeleniumService(CompanyFinancialInfoRepository companyFinancialInfoRepository, CompanyRepository companyRepository, JdbcIndutyRepository indutyRepository, CompanyService companyService){
        this.companyFinancialInfoRepository = companyFinancialInfoRepository;
        this.companyRepository = companyRepository;
        this.indutyRepository = indutyRepository;
        this.companyService = companyService;
    }

    public String findCompanyInfo(CompanyFinancialInfo company, String corp_code) throws IOException {
        String url = COMPANY_INFO_URL
                +"?crtfc_key=" + API_KEY
                +"&corp_code=" + corp_code;

        org.jsoup.nodes.Document document = Jsoup.connect(url).get();
        String status = document.select("status").text();
        if(status.equals("013"))
        {
            System.out.println("데이터 없음");
            return null;
        }
        Elements stock_name = document.select("stock_name");
        String induty_code = document.select("induty_code").text();


        return induty_code.substring(0, 3);
    }

    /*  reprt_code
    	1분기보고서 : 11013
        반기보고서 : 11012
        3분기보고서 : 11014
        사업보고서 : 11011
     */

    @Transactional
    public boolean findFinancialInfo(String corp_name, String corp_code, String bsns_year, String reprt_code) throws IOException {
        String url = FINANCIAL_URL
                +"?crtfc_key=" + API_KEY
                +"&corp_code=" + corp_code
                +"&bsns_year=" + bsns_year
                +"&reprt_code=" + reprt_code;
        //System.out.println("findFinancialInfo 실행 중");
        org.jsoup.nodes.Document document = Jsoup.connect(url).get();
        //System.out.println("document = " + document);
        String status = document.select("status").text();
        if(status.equals("013"))
        {
            System.out.println("데이터 없음");
            return false;
        }
        insertCompanyFinancialInfo(corp_name, corp_code, bsns_year, reprt_code, document);
        return true;
    }



    //파라미터 없음
    public void saveCrawlingData() throws IOException {
        companyService.addIndustryCode();

        // xml 파싱 빌드업
        int cnt=0;
        int parsingCnt=0;
        try {
            File file = new File("src/main/resources/corpCode/CORPCODE.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("list");

            for (int temp = 14392; temp < nList.getLength(); temp++, cnt++) {// 14392
                System.out.println("temp = " + temp);

                // 크롤링 속도 조절
                if (cnt>350){
                    log.error("70초 딜레이 중");
                    TimeUnit.SECONDS.sleep(70);
                    cnt=0;

                }

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
//                    log.info("corp_code : " + eElement.getElementsByTagName("corp_code").item(0).getTextContent());
                    String corp_code = eElement.getElementsByTagName("corp_code").item(0).getTextContent();
//                    log.info("corp_name : " + eElement.getElementsByTagName("corp_name").item(0).getTextContent());
                    String corp_name = eElement.getElementsByTagName("corp_name").item(0).getTextContent();
//                    log.info("modify_date : " + eElement.getElementsByTagName("modify_date").item(0).getTextContent());


                    boolean errorCheck = findFinancialInfo(corp_name, corp_code, "2022", "11013");

                }

            }

        }
        catch(IOException e) {
            log.info("errors = {}",e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }





    private void insertCompanyFinancialInfo(String stock_name, String corp_code, String bsns_year, String reprt_code, org.jsoup.nodes.Document document) throws IOException {
        Company company = new Company();
        company.setCorpCode(corp_code);
        company.setStockName(stock_name);
        company.setIndutyId(22);
        companyRepository.save(company);


        int size = document.select("list").size();
        String induty_code = null;
        String stock_code = null;
        for(int i =0; i < size; i++){
            CompanyFinancialInfo companyFinancialInfo = new CompanyFinancialInfo();
            try {
                stock_code = document.select("list").get(i).select("stock_code").text();
                String account_nm = document.select("list").get(i).select("account_nm").text();
                String fs_div = document.select("list").get(i).select("fs_div").text();
                String sj_nm = document.select("list").get(i).select("sj_nm").text();
                String thstrm_amount = document.select("list").get(i).select("thstrm_amount").text().replace(",", "");
                String thstrm_add_amount = document.select("list").get(i).select("thstrm_add_amount").text().replace(",", "");
                String frmtrm_amount = document.select("list").get(i).select("frmtrm_amount").text().replace(",", "");
                String frmtrm_add_amount = document.select("list").get(i).select("frmtrm_add_amount").text().replace(",", "");



                companyFinancialInfo.setReprtCode(reprt_code);
                companyFinancialInfo.setBsnsYear(Integer.parseInt(bsns_year));
                companyFinancialInfo.setSjNm(sj_nm);
                companyFinancialInfo.setAccountNm(account_nm);
                companyFinancialInfo.setFsDiv(fs_div);
                companyFinancialInfo.setCompanyId(company.getCompanyId());
                if (thstrm_amount.equals(""))
                    companyFinancialInfo.setThstrmAmount(null);
                else companyFinancialInfo.setThstrmAmount(Long.parseLong(thstrm_amount));
                if (thstrm_add_amount.equals(""))
                    companyFinancialInfo.setThstrmAddAmount(null);
                else companyFinancialInfo.setThstrmAddAmount(Long.parseLong(thstrm_add_amount));
                if(frmtrm_amount.equals("")||frmtrm_amount.equals("-"))
                    companyFinancialInfo.setFrmtrmAmount(null);
                else companyFinancialInfo.setFrmtrmAmount(Long.parseLong(frmtrm_amount));
                if (frmtrm_add_amount.equals("")||frmtrm_add_amount.equals("-")) {
                    companyFinancialInfo.setFrmtrmAddAmount(null);
                }
                else
                    companyFinancialInfo.setFrmtrmAddAmount(Long.parseLong(frmtrm_add_amount));

                induty_code = findCompanyInfo(companyFinancialInfo, corp_code);
                companyFinancialInfoRepository.save(companyFinancialInfo);
            }
            catch(IndexOutOfBoundsException e)
            {
                log.info(e.getMessage());
            }
        }

        Map<String, String> industry_code = companyService.getIndustry_code();
        String description = industry_code.get(induty_code);
        Long marketCap = companyService.getMarketCap(stock_code);
        CompanyUpdateDto companyUpdateDto = new CompanyUpdateDto();
        companyUpdateDto.setStockCode(stock_code);
        String refactorIndutyCode = refactorIndutyCode(induty_code.substring(0, 2));

        Optional<Induty> induty = indutyRepository.findByIndutyCode(refactorIndutyCode);
        int induty_id;
        if (induty.isEmpty())
            induty_id = 22;
        else
            induty_id =induty.get().getIndutyId();
        // induty table -> induty_id 참조
        companyUpdateDto.setIndutyId(induty_id);
        companyUpdateDto.setIndutyDescription(description);
        companyUpdateDto.setMarketCap(marketCap);
        companyRepository.update(company.getCompanyId(), companyUpdateDto);

    }

    private String refactorIndutyCode(String indutyCode){
        int intIndutyCode = Integer.parseInt(indutyCode);
        if(intIndutyCode<4)
            return "01";
        else if(intIndutyCode<9)
            return "05";
        else if(intIndutyCode<35)
            return "10";
        else if(intIndutyCode==35)
            return "35";
        else if (intIndutyCode<40)
            return "36";
        else if(intIndutyCode<43)
            return "41";
        else if (intIndutyCode<48)
            return "45";
        else if(intIndutyCode<53)
            return "49";
        else if (intIndutyCode<57)
            return "55";
        else if(intIndutyCode<64)
            return "58";
        else if (intIndutyCode<67)
            return "64";
        else if(intIndutyCode==68)
            return "68";
        else if (intIndutyCode<74)
            return "70";
        else if(intIndutyCode<77)
            return "74";
        else if (intIndutyCode==84)
            return "84";
        else if(intIndutyCode==85)
            return "85";
        else if (intIndutyCode<88)
            return "86";
        else if(intIndutyCode<92)
            return "90";
        else if (intIndutyCode<97)
            return "94";
        else if(intIndutyCode==97)
            return "97";
        else if(intIndutyCode==99)
            return "99";
        return null;

    }
}
