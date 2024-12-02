import org.json.simple.JSONObject;

public class Admin extends Account {

    public Admin(String id, String username, String password) {
        super(id, username, password, "admin");
    }

    public void createAccount(String id, String username, String password, String role) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        JSONObject newUser = new JSONObject();
        newUser.put("id", id);
        newUser.put("password", password);
        newUser.put("role", role);

        users.put(username, newUser);
        DatabaseHelper.saveDatabase("users.json", users);
    }

    public void assignRole(String id, String newRole) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        for (Object key : users.keySet()) {
            JSONObject user = (JSONObject) users.get(key);
            if (user.get("id").equals(id)) {
                user.put("role", newRole);
                DatabaseHelper.saveDatabase("users.json", users);
                return;
            }
        }
    }

    public void editAccount(String id, String newUsername, String newPassword, String newRole) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        for (Object key : users.keySet()) {
            JSONObject user = (JSONObject) users.get(key);
            if (user.get("id").equals(id)) {
                user.put("username", newUsername);
                user.put("password", newPassword);
                user.put("role", newRole);
                DatabaseHelper.saveDatabase("users.json", users);
                return;
            }
        }
    }

    public void deleteAccount(String id) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        users.keySet().removeIf(key -> {
            JSONObject user = (JSONObject) users.get(key);
            return user.get("id").equals(id);
        });
        DatabaseHelper.saveDatabase("users.json", users);
    }
}
