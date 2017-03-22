import org.primefaces.validate.ClientValidator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@FacesValidator("clients.clientIdValidator")
public class ClientIdValidator implements Validator, ClientValidator {

    private final static String SQL_CLIENTID_VALIDATE = "select t.clientId from clients t where t.clientId = ?";

    @Override
    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;
        try {
            InitialContext ctx = new InitialContext();
            DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/DefaultDC");
            conn = ds.getConnection();
            ps = conn.prepareStatement(SQL_CLIENTID_VALIDATE);
            ps.setInt(1, (int)value);
            rs = ps.executeQuery();
            rs.next();
            if (rs.getRow() != 0)
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ошибка!",
                        "Клиент с ID = " + value + " уже существует, выберите другой."));
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> getMetadata() {
        return null;
    }

    @Override
    public String getValidatorId() {
        return "clients.clientIdValidator";
    }
}
