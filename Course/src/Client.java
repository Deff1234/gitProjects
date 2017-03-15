import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;

@ManagedBean
@RequestScoped
public class Client {
    private int clientId;
    private String FIO;
    private String passData;
    private String homeAddress;
    private String phone;
    private int delivery;
    private String typeOfPayment;

    public ArrayList<Client> getMessages() throws SQLException, NamingException {
        return ClientsBean.getClients();
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public String getPassData() {
        return passData;
    }

    public void setPassData(String passData) {
        this.passData = passData;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public String getTypeOfPayment() {
        return typeOfPayment;
    }

    public void setTypeOfPayment(String typeOfPayment) {
        this.typeOfPayment = typeOfPayment;
    }
}
