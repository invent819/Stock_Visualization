package stock.stockvisualization.domain.company;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class JdbcCompanyRepository implements CompanyRepository{
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcCompanyRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("company")
                .usingGeneratedKeyColumns("company_id");
    }

    @Override
    public Company save(Company company) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(company);
        Number key = jdbcInsert.executeAndReturnKey(param);
        company.setCompanyId(key.longValue());
        return company;
    }
//
//            companyUpdateDto.setStock_code(stock_code);
//        companyUpdateDto.setInduty_code(induty_code.substring(0,2));
//        companyUpdateDto.setInduty_description(description);
//        companyUpdateDto.setMarketCap(marketCap);
    @Override
    public void update(Long companyId, CompanyUpdateDto updateParam) {
        String sql = "update company " +
                "set stock_code=:stockCode, induty_code=:indutyCode, induty_description=:indutyDescription, market_cap=:marketCap " +
                "where company_id=" + String.valueOf(companyId);
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("stockCode", updateParam.getStockCode())
                .addValue("indutyCode", updateParam.getIndutyCode())
                .addValue("indutyDescription", updateParam.getIndutyDescription())
                .addValue("marketCap", updateParam.getMarketCap());
        template.update(sql, param);
    }

    @Override
    public Company findById(Long id) {
        return null;
    }

    @Override
    public List<Company> findAll(Company companyCond) {
        String sql = "select * from company";
        SqlParameterSource param = new BeanPropertySqlParameterSource(companyCond);
        List<Company> companyList = template.query(sql, param, companyRowMapper());
        return companyList;
    }

    @Override
    public void deleteById(Long id) {

    }
    private RowMapper<Company> companyRowMapper(){
        return BeanPropertyRowMapper.newInstance(Company.class);
    }
}
