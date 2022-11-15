package stock.stockvisualization.domain.company;

import lombok.Data;

@Data
public class Company {
    Long companyId;
    String stock_name;
    String corp_code;
    String description;
    String induty_code; // 산업코드
    String induty_description; // 산업 설명
}
