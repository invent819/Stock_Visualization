package stock.stockvisualization.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import stock.stockvisualization.domain.company.Company;

import javax.sql.DataSource;
import java.util.*;

@Slf4j
@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member){
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setMemberId(key.longValue());
        return member;
    }

    @Override
    public Member update(MemberUpdateDto member) {
        return null;
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        String sql = "select * from member where login_id = :loginId";
        try {
            Map<String, Object> param = Map.of("loginId", loginId);
            Member member = template.queryForObject(sql, param, memberRowMapper());

            return Optional.of(member);
        }
        catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<HashSet<Company>> findAllCompany() {
        return Optional.empty();
    }

    private RowMapper<Member> memberRowMapper(){
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

}
