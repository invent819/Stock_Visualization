package stock.stockvisualization.domain.member;

import lombok.Data;
import stock.stockvisualization.domain.company.Company;
import stock.stockvisualization.domain.company.CompanyFinancialInfo;

import java.util.LinkedHashSet;

@Data
public class Member {
    private Long memberId;

    private String userName;

    private String loginId;
    private String userPassword;


}
