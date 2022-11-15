package stock.stockvisualization.web.company;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.sql.Types.NUMERIC;
import static org.apache.tomcat.util.bcel.classfile.ElementValue.STRING;

/*
https://kssc.kostat.go.kr:8443/ksscNew_web/kssc/common/ClassificationContent.do?gubun=1&strCategoryNameCode=001&categoryMenu=007&addGubun=no
*/
@Service
@Slf4j
@Data
public class CompanyService {
    private Map<String, String> industry_code = new HashMap<>();
    private static String filePath = "src/main/resources/industryCode";
    public static String fileNm = "industryCode.xlsx";
    public void addIndustryCode() throws IOException {
            String code=null;
            String name=null;
            int flag=0;

            FileInputStream file = new FileInputStream(new File(filePath,fileNm ));

            // 엑셀 파일로 Workbook instance를 생성한다.
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            // workbook의 첫번째 sheet를 가저온다.
            XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // 각각의 행에 존재하는 모든 열(cell)을 순회한다.
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                        if(flag==0) {
                            code = cell.getStringCellValue();
                            flag=1;
                        }
                        else{
                            name = cell.getStringCellValue();
                            flag=0;
                        }
                        System.out.print(cell.getStringCellValue() + "\t");
            }
            industry_code.put(code, name);

/*            System.out.println("industry_code = " + industry_code);
            System.out.println("industry_code = " + industry_code.size());*/
        }
        file.close();


    }
}
