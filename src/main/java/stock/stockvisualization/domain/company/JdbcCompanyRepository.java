package stock.stockvisualization.domain.company;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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

    @Override
    public Company update(Company company) {
        return null;
    }

    @Override
    public Company findById(Long id) {
        return null;
    }

    @Override
    public List<Company> findAll(Company companyCond) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
