package stock.stockvisualization.web.login;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty(message = "아이디는 필수입니다.")
    private String loginId;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String password;
}
