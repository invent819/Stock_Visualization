package stock.stockvisualization.domain.member;

import lombok.Data;
import stock.stockvisualization.domain.company.Company;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class Member {
    private Long id;

    private String name;

    private String loginId;
    private String password;

    private List<Company> companies;

}
