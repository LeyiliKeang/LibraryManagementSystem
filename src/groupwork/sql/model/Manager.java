package groupwork.sql.model;

public class Manager {
    private String managerIDNumber;
    private String managerName;
    private String managerPassword;
    private String managerPhone;
    static String managerPower;
    static String selectID;
    static String idNumber;

    public Manager() {
    }

    public String getManagerIDNumber() {
        return managerIDNumber;
    }

    public String getManagerName() {
        return managerName;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerIDNumber(String managerIDNumber) {
        this.managerIDNumber = managerIDNumber;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public static String getSelectID() {
        return selectID;
    }

    public static void setSelectID(String selectID) {
        Manager.selectID = selectID;
    }

    public static String getManagerPower() {
        return managerPower;
    }

    public static void setManagerPower(String managerPower) {
        Manager.managerPower = managerPower;
    }

    public static String getIdNumber() {
        return idNumber;
    }

    public static void setIdNumber(String idNumber) {
        Manager.idNumber = idNumber;
    }
}
