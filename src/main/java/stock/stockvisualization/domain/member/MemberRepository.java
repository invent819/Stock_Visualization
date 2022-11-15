package stock.stockvisualization.domain.member;

import stock.stockvisualization.domain.company.Company;

import java.util.HashSet;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Member update(MemberUpdateDto member);
    Optional<Member> findByLoginId(String loginId);
    void deleteById(Long id);
    Optional<HashSet<Company>> findAllCompany();

}
