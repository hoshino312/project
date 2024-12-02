import org.json.simple.JSONObject;

public abstract class Employee extends Account {
    protected String name;
    protected String phone;

    public Employee(String id, String username, String password, String role, String name, String phone) {
        super(id, username, password, role);
        this.name = name;
        this.phone = phone;
    }

    public void editPersonalInformation(String newName, String newPhone) {
        this.name = newName;
        this.phone = newPhone;

        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        JSONObject user = (JSONObject) users.get(username);
        if (user != null) {
            user.put("name", newName);
            user.put("phone", newPhone);
            DatabaseHelper.saveDatabase("users.json", users);
        }
    }
}

