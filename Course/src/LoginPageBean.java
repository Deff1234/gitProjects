import config.JDBCConfig;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.sql.ResultSet;
import java.sql.SQLException;

@ManagedBean
@RequestScoped
public class LoginPageBean {

    private String login;
    private String password;

    public String checkSuccessLogin () throws SQLException {
        String flag = "";
        ResultSet rs = null;
        try {
            rs = JDBCConfig.executeQuery("select t.login, t.password from users t where t.login = '" + login + "' and t.password = '" + password + "'");
            while (rs.next()) {
                if (rs.getString("login").equals(login) && rs.getString("password").equals(password))
                    flag = "success";
            }
            if (!flag.equals("success"))
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Неверный логин или пароль!"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null)
                rs.close();
            return flag;
        }
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
