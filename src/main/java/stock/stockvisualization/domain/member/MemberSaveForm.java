package stock.stockvisualization.domain.member;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberSaveForm {
    @NotEmpty(message = "이름은 필수입니다.")
    private String name;
    @NotEmpty(message = "아이디는 필수입니다.")
    private String loginId;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;

}
