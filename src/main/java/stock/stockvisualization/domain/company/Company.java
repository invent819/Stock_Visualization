package stock.stockvisualization.domain.company;

import lombok.Data;

@Data
public class Company {
    Long companyId;
    String stockCode;
    String stockName;
    String corpCode;
    String description;
    int indutyId; // 산업코드
    String indutyDescription; // 산업 설명
    Long marketCap; //시가총액
    int searchCnt;
}
