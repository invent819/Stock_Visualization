package stock.stockvisualization.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemberRepository {
    private static Map<Long, Member> store = new ConcurrentHashMap<>();
    private static Long sequence = 0L;
    public Member save(Member member){
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id){
        return store.get(id);
    }

    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public Optional<Member> findByLoginId(String loginId){
       return findAll().stream().filter(m -> m.getLoginId().equals(loginId)).findFirst();
    }

    public void clearStore(){
        store.clear();
    }



}
