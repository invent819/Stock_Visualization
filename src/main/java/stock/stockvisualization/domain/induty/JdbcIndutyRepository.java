package stock.stockvisualization.domain.induty;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import stock.stockvisualization.domain.member.Member;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcIndutyRepository {
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcIndutyRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("induty");

    }

    public Optional<Induty> findByIndutyCode(String indutyCode) {
        String sql = "select * from induty where induty_code = :indutyCode";
        try {
            Map<String, Object> param = Map.of("indutyCode", indutyCode);
            Induty induty = template.queryForObject(sql, param, indutyRowMapper());

            return Optional.of(induty);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Induty> indutyRowMapper(){
        return BeanPropertyRowMapper.newInstance(Induty.class);
    }

}
