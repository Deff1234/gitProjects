import config.JDBCConfig;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@ManagedBean
@RequestScoped
public class LoginPageBean {

    //по окончанию удалить присваивание логина и паролья
    private String login = "admin";
    private String password = "password";

    public String checkSuccessLogin () throws SQLException, NamingException {
        String flag = "";
        ResultSet rs = null;
        PreparedStatement ps = null;
        InitialContext ctx = new javax.naming.InitialContext();
        DataSource ds = (javax.sql.DataSource)ctx.lookup("jdbc/DefaultDC");
        Connection conn = ds.getConnection();
        try {
            ps = conn.prepareStatement("select t.login, t.password from users t where t.login = ? and t.password = ?");
            ps.setString(1, login);
            ps.setString(2, password);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getString("login").equals(login) && rs.getString("password").equals(password))
                    flag = "success";
            }
            if (!flag.equals("success"))
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Ошибка входа!" ,"Неверный логин или пароль."));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
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
