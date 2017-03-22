import org.primefaces.context.RequestContext;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class ClientsBean implements Serializable {

    private int currentClientId;
    private Client currentClient;
    private boolean editClient;
    private String messageHeader;

    private static final String SQL_UPDATE_CLIENT = "UPDATE clients SET FIO = ?, passportData = ?, homeAddress = ?, phone = ?, delivery = ?, typeOfPayment = ? WHERE clientId = ?";
    private static final String SQL_INDERT_CLIENT = "INSERT INTO clients (FIO, passportData, homeAddress, phone, delivery, typeOfPayment,clientId) VALUES (?,?,?,?,?,?,?);";
    private static final String SQL_DELETE_CLIENT = "DELETE FROM clients WHERE clientId = ?";

    @PostConstruct
    public void init() {
        currentClient = new Client();
    }

    public static List<Client> getClients() throws SQLException, NamingException {
        InitialContext ctx = new javax.naming.InitialContext();
        DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/DefaultDC");
        Connection conn = ds.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("select * from clients");
            List<Client> al = new ArrayList<>();
            rs = ps.executeQuery();
            boolean found = false;
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
            e.printStackTrace();
            return (null);
        } finally {
            if (rs != null)
                rs.close();
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        }
    }

    public void actionConfirm() throws SQLException, NamingException {
        if (editClient)
            editClientConfirm();
        else
            addClientConfirm();
    }


    public void clientConfirm(String query) throws NamingException, SQLException {
        InitialContext ctx = new javax.naming.InitialContext();
        DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/DefaultDC");
        Connection conn = ds.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, currentClient.getFIO());
            ps.setString(2, currentClient.getPassData());
            ps.setString(3, currentClient.getHomeAddress());
            ps.setString(4, currentClient.getPhone());
            ps.setInt(5, currentClient.getDelivery());
            ps.setString(6, currentClient.getTypeOfPayment());
            ps.setInt(7, currentClient.getClientId());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        }
    }

    public void editClient() throws SQLException, NamingException {
        editClient = true;
        messageHeader = "Редактирование";
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params =
                fc.getExternalContext().getRequestParameterMap();
        currentClientId = Integer.valueOf(params.get("clientId"));

        for (Client c : getClients()) {
            if (c.getClientId() == currentClientId) {
                currentClient = c;
                break;
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('clientEditDialogVar').show();");
    }

    public void editClientConfirm() throws NamingException, SQLException {
        clientConfirm(SQL_UPDATE_CLIENT);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('clientEditDialogVar').hide();");
    }

    public void addClient() {
        editClient = false;
        setMessageHeader("Добавление");
        currentClient = new Client();
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('clientAddDialogVar').show();");
    }

    public void addClientConfirm() throws NamingException, SQLException {
        clientConfirm(SQL_INDERT_CLIENT);
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('clientAddDialogVar').hide();");
    }

    public void deleteClient() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, String> params =
                fc.getExternalContext().getRequestParameterMap();
        currentClientId = Integer.valueOf(params.get("clientId"));
    }

    public void deleteClientConfirm() throws SQLException, NamingException{
        deleteClient();
        InitialContext ctx = new javax.naming.InitialContext();
        DataSource ds = (javax.sql.DataSource) ctx.lookup("jdbc/DefaultDC");
        Connection conn = ds.getConnection();
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(SQL_DELETE_CLIENT);
            ps.setInt(1, currentClientId);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null)
                ps.close();
            if (conn != null)
                conn.close();
        }
    }

    public int getCurrentClientId() {
        return currentClientId;
    }

    public void setCurrentClientId(int currentClientId) {
        this.currentClientId = currentClientId;
    }

    public Client getCurrentClient() {
        return currentClient;
    }

    public void setCurrentClient(Client currentClient) {
        this.currentClient = currentClient;
    }

    public boolean isEditClient() {
        return editClient;
    }

    public void setEditClient(boolean editClient) {
        this.editClient = editClient;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

}
