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
import stock.stockvisualization.domain.company.Company;
import stock.stockvisualization.domain.company.CompanyFinancialInfo;
import stock.stockvisualization.domain.company.CompanyFinancialInfoRepository;
import stock.stockvisualization.domain.company.CompanyRepository;
import stock.stockvisualization.web.company.CompanyService;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Data
@Slf4j
public class SeleniumService {
    // stock_name, corpcode, bsns_year, account_nm, fs_div, sj_nm, thstrm_amount, frmtrm_amount, induty_code
    private  WebDriver driver;
    private final String API_KEY = "12ed28c5a1ccad901a74e01b506d6690588f73f5";
    private final String COMPANY_INFO_URL = "https://opendart.fss.or.kr/api/company.xml";
    private final String FINANCIAL_URL = "https://opendart.fss.or.kr/api/fnlttSinglAcnt.xml";
    @Autowired
    private final CompanyFinancialInfoRepository companyFinancialInfoRepository;
    @Autowired
    private final CompanyRepository companyRepository;

    private final CompanyService companyService;

    public SeleniumService(CompanyFinancialInfoRepository companyFinancialInfoRepository, CompanyRepository companyRepository, CompanyService companyService){
        this.companyFinancialInfoRepository = companyFinancialInfoRepository;
        this.companyRepository = companyRepository;
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

        company.setStock_name(stock_name.text());
        company.setInduty_code(induty_code.substring(0, 3));
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

        try {
            File file = new File("src/main/resources/corpCode/CORPCODE.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            document.getDocumentElement().normalize();
            NodeList nList = document.getElementsByTagName("list");

            for (int temp = 91100; temp > 0; temp--, cnt++) { //546  nList.getLength()
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
        int size = document.select("list").size();
        String induty_code = null;
        for(int i =0; i < size; i++){
            CompanyFinancialInfo companyFinancialInfo = new CompanyFinancialInfo(stock_name, corp_code);
            try {
                String corpcode = document.select("list").get(i).select("corp_code").text();
                String account_nm = document.select("list").get(i).select("account_nm").text();
                String fs_div = document.select("list").get(i).select("fs_div").text();
                String sj_nm = document.select("list").get(i).select("sj_nm").text();
                String thstrm_amount = document.select("list").get(i).select("thstrm_amount").text().replace(",", "");
                String thstrm_add_amount = document.select("list").get(i).select("thstrm_add_amount").text().replace(",", "");
                String frmtrm_amount = document.select("list").get(i).select("frmtrm_amount").text().replace(",", "");
                String frmtrm_add_amount = document.select("list").get(i).select("frmtrm_add_amount").text().replace(",", "");


                companyFinancialInfo.setCorp_code(corpcode);
                companyFinancialInfo.setReprt_code(reprt_code);
                companyFinancialInfo.setBsns_year(Integer.parseInt(bsns_year));
                companyFinancialInfo.setSj_nm(sj_nm);
                companyFinancialInfo.setAccount_nm(account_nm);
                companyFinancialInfo.setFs_div(fs_div);
                if (thstrm_amount.equals(""))
                    companyFinancialInfo.setThstrm_amount(null);
                else companyFinancialInfo.setThstrm_amount(Long.parseLong(thstrm_amount));
                if (thstrm_add_amount.equals(""))
                    companyFinancialInfo.setThstrm_add_amount(null);
                else companyFinancialInfo.setThstrm_add_amount(Long.parseLong(thstrm_add_amount));
                if(frmtrm_amount.equals("")||frmtrm_amount.equals("-"))
                    companyFinancialInfo.setFrmtrm_amount(null);
                else companyFinancialInfo.setFrmtrm_amount(Long.parseLong(frmtrm_amount));
                if (frmtrm_add_amount.equals("")||frmtrm_add_amount.equals("-")) {
                    companyFinancialInfo.setFrmtrm_add_amount(null);
                }
                else
                    companyFinancialInfo.setFrmtrm_add_amount(Long.parseLong(frmtrm_add_amount));

                induty_code = findCompanyInfo(companyFinancialInfo, corp_code);
                companyFinancialInfoRepository.save(companyFinancialInfo);
            }
            catch(IndexOutOfBoundsException e)
            {
                log.info(e.getMessage());
            }

        }
        Company company = new Company();
        company.setCorp_code(corp_code);
        company.setStock_name(stock_name);
        company.setInduty_code(induty_code);
        Map<String, String> industry_code = companyService.getIndustry_code();
        String description = industry_code.get(induty_code);
        company.setDescription(description);
        companyRepository.save(company);

    }
}
