import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class LoginPageBean {

    private String login;
    private String password;

    public String checkSuccessLogin () {
        if ("login".equals(login) && "pass".equals(password))
            return "success";
        else
            return "failure";

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
