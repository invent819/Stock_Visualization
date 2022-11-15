package stock.stockvisualization.domain.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stock.stockvisualization.domain.member.Member;
import stock.stockvisualization.domain.member.JdbcMemberRepository;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final JdbcMemberRepository memberRepository;

    public Member login(String loginId, String password){
        return memberRepository.findByLoginId(loginId).filter(m->m.getUserPassword().equals(password)).orElse(null);
    }
}
