package stock.stockvisualization.domain.company;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcCompanyRepository implements CompanyRepository {
    //사용자가 관심 있는 company 저장소
    private static Map<Long, Company> store = new ConcurrentHashMap<>();
    private final JdbcTemplate jdbcTemplate;
    private static Long sequence = 0L;

    @Override
    public Company save(Company company){
        String sql = "insert into company(id, stock_name, corp_code, reprt_code, bsns_year, account_nm, fs_div, sj_nm, thstrm_amount, thstrm_add_amount, frmtrm_amount, frmtrm_add_amount, induty_code)" +
                " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, company.getId(), company.getStock_name(), company.getCorpcode(), company.getReprt_code(),
                company.getBsns_year(), company.getAccount_nm(), company.getFs_div(), company.getSj_nm(),
                company.getThstrm_amount(), company.getThstrm_add_amount(), company.getFrmtrm_amount(), company.getFrmtrm_add_amount(), company.getInduty_code());
/*        company.setId(++sequence);
        store.put(company.getId(), company);*/
        return company;
    }

    @Override
    public Company update(Company company) {
        return null;
    }

    @Override
    public Company findById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }


}
