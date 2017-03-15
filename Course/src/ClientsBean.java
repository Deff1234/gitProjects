import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

@ManagedBean
@RequestScoped
public class ClientsBean {
    //@Resource(name = "jdbc/DefaultDC")


    public static ArrayList<Client> getClients() throws SQLException, NamingException {
        InitialContext ctx = new javax.naming.InitialContext();
        DataSource ds = (javax.sql.DataSource)ctx.lookup("jdbc/DefaultDC");
        Connection conn = ds.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("select * from clients");
            ArrayList<Client> al = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            boolean found  = false;
            while (rs.next()) {
                Client c = new Client();
                c.setClientId(rs.getInt("clientId"));
                c.setFIO(rs.getString("FIO"));
                c.setPassData(rs.getString("passportData"));
                c.setHomeAddress(rs.getString("homeAddress"));
                c.setPhone(rs.getString("phone"));
                c.setDelivery(rs.getInt("delivery"));
                c.setTypeOfPayment(rs.getString("typeOfPayment"));
                al.add(c);
                found = true;
            }
            rs.close();
            if (found)
                return al;
            else
                return null;
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            return (null);
        } finally {
            conn.close();
        }
    }
}
