package stock.stockvisualization.domain.company;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Slf4j
@Repository

public class JdbcCompanyFinancialInfoRepository implements CompanyFinancialInfoRepository {
    //사용자가 관심 있는 company 저장소
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcCompanyFinancialInfoRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("companyFinancialInfo")
                .usingGeneratedKeyColumns("companyFinancialInfo_id");
    }

    @Override
    public CompanyFinancialInfo save(CompanyFinancialInfo company){
        SqlParameterSource param = new BeanPropertySqlParameterSource(company);
        Number key = jdbcInsert.executeAndReturnKey(param);
        company.setCompanyFinancialInfoId(key.longValue());
        return company;
    }

    @Override
    public CompanyFinancialInfo update(CompanyFinancialInfo company) {
        return null;
    }

    @Override
    public CompanyFinancialInfo findByCompanyId(Long id) {
/*        String sql = ""*//**//*
        template.query(sql)*/
        return null;
    }



    @Override
    public void deleteById(Long id) {

    }


}
