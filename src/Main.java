import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.*;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        DatabaseHelper.initializeAdminAccount();

        while (true) {
            System.out.println("Welcome to the Store Management System");
            System.out.println("1. Login");
            System.out.println("2. Exit");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void handleLogin() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Load the user from the database
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        JSONObject user = (JSONObject) users.get(username);

        if (user != null && user.get("password").equals(password)) {
            String role = (String) user.get("role");
            String id = (String) user.get("id");
            System.out.println("Login successful as " + role);

            switch (role.toLowerCase()) {
                case "admin":
                    Admin admin = new Admin(id, username, password);
                    showAdminMenu(admin);
                    break;
                case "manager":
                    String name = (String) user.get("name");
                    String phone = (String) user.get("phone");
                    Manager manager = new Manager(id, username, password, name, phone);
                    showManagerMenu(manager);
                    break;
                case "cashier":
                    name = (String) user.get("name");
                    phone = (String) user.get("phone");
                    Cashier cashier = new Cashier(id, username, password, name, phone);
                    showCashierMenu(cashier);
                    break;
                default:
                    System.out.println("Unknown role. Returning to login.");
            }
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    private static void showAdminMenu(Admin admin) {    
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Assign Role");
            System.out.println("3. Edit Account");
            System.out.println("4. Delete Account");
            System.out.println("5. Change Your Password");
            System.out.println("6. Logout");
    
            int choice = getUserChoice();
            String id;
                switch (choice) {
                    case 1:
                        System.out.print("Enter Role: ");
                        String role = scanner.nextLine();
                        admin.createAccount(role);
                        break;
                    case 2:
                        System.out.print("Enter User ID to update role: ");
                        String userId = scanner.nextLine();
                        System.out.print("Enter new Role: ");
                        String newRole = scanner.nextLine();
                        admin.assignRole(userId, newRole);
                        break;
                
                    case 3:
                        System.out.print("Enter User ID to edit: ");
                        id = scanner.nextLine();
                    System.out.print("Enter new Password: ");
                    String newPassword = scanner.nextLine();
                    System.out.print("Enter new Role: ");
                    newRole = scanner.nextLine();
                    admin.editAccount(id, newPassword, newRole);
                    break;
    
                case 4:
                    System.out.print("Enter User ID to delete: ");
                    id = scanner.nextLine();
                    admin.deleteAccount(id);
                    break;
    
                case 5: // New case for changing the admin's own password
                    System.out.print("Enter your current password: ");
                    String currentPassword = scanner.nextLine();
                    System.out.print("Enter your new password: ");
                    String newPass = scanner.nextLine();
                    if (admin.login(admin.username, currentPassword)) { // Verify current password
                        admin.changePassword(newPass); // Change to new password
                        System.out.println("Password changed successfully.");
                    } else {
                        System.out.println("Incorrect current password. Password change failed.");
                    }
                    break;
    
                case 6:
                    System.out.println("Logging out...");
                    return;
    
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
        
    private static void showManagerMenu(Manager manager) {
        while (true) {
            System.out.println("\n--- Manager Main Menu ---");
            System.out.println("1. Inventory Management");
            System.out.println("2. Reports and Performance");
            System.out.println("3. Personal Information");
            System.out.println("4. Logout");
    
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    showInventoryMenu(manager);
                    break;
                case 2:
                    showReportsMenu(manager);
                    break;
                case 3:
                    showPersonalInfoMenu(manager);
                    break;
                case 4:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
    private static void showInventoryMenu(Manager manager) {
        Inventory inventory = new Inventory();
        while (true) {
            System.out.println("\n--- Inventory Management Menu ---");
            System.out.println("1. Monitor Inventory");
            System.out.println("2. Filter Low Stock");
            System.out.println("3. Replenish Item");
            System.out.println("4. Add Product to Inventory");
            System.out.println("5. Remove Product from Inventory");
            System.out.println("6. Back to Main Menu");
    
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    manager.monitorInventory();
                    break;
                case 2:
                    List<Product> lowStockProducts = manager.filterLowStock();
                    System.out.println("Low Stock Products:");
                    for (Product product : lowStockProducts) {
                        System.out.println(product.getProductInfo());
                    }
                    break;
                case 3:
                    System.out.print("Enter Product ID: ");
                    String productId = scanner.nextLine();
                    System.out.print("Enter Quantity to Replenish: ");
                    int quantity = Integer.parseInt(scanner.nextLine());
                    manager.replenishItem(productId, quantity);
                    break;
                case 4:
                    manager.addProductToInventory(inventory);
                    break;
                case 5:
                    manager.removeProductFromInventory(inventory);
                    break;
                    case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
    private static void showReportsMenu(Manager manager) {
        while (true) {
            System.out.println("\n--- Reports and Performance Menu ---");
            System.out.println("1. Access Sales Reports");
            System.out.println("2. Review Employee Performance");
            System.out.println("3. Back to Main Menu");
    
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    System.out.print("Enter Product ID (or leave blank for all): ");
                    String reportProductId = scanner.nextLine();
                    System.out.print("Enter Period (1-day/7-day): ");
                    String reportPeriod = scanner.nextLine();
                    System.out.print("Enter Start Date (yyyy-MM-dd): ");
                    String reportStartDate = scanner.nextLine();
                    manager.accessSalesReports(reportProductId, reportPeriod, reportStartDate);
                    break;
                case 2:
                    System.out.print("Enter Employee ID: ");
                    String employeeId = scanner.nextLine();
                    System.out.print("Enter Period (1-day/7-day): ");
                    String performancePeriod = scanner.nextLine();
                    System.out.print("Enter Start Date (yyyy-MM-dd): ");
                    String performanceStartDate = scanner.nextLine();
                    manager.reviewPerformance(employeeId, performancePeriod, performanceStartDate);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showPersonalInfoMenu(Manager manager) {
        while (true) {
            System.out.println("\n--- Personal Information Menu ---");
            System.out.println("1. View Personal Information");
            System.out.println("2. Update Personal Information");
            System.out.println("3. Back to Main Menu");
    
            int choice = getUserChoice();
            switch (choice) {
                case 1:
                    manager.viewPersonalInformation();
                    break;
                case 2:
                    System.out.print("Enter new name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter new phone number: ");
                    String newPhone = scanner.nextLine();
                    manager.editPersonalInformation(newName, newPhone);
                    System.out.println("Personal information updated successfully.");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }                
    
    public static void showCashierMenu(Cashier cashier) {
        Inventory inventory = new Inventory();
        List<Product> cart = new ArrayList<>();
    
        while (true) {
            System.out.println("\n--- Cashier Menu ---");
            System.out.println("1. Scan Products");
            System.err.println("2. Process Payment");
            System.out.println("3. View Personal Information");
            System.out.println("4. Update Personal Information");
            System.out.println("5. Logout");
            int choice = getUserChoice();
    
            switch (choice) {
                case 1:
                    // Continuous scanning of products
                    System.out.println("Start scanning products. Type 'done' when finished.");
                    cashier.scanProducts(cart, inventory);
                    break;
    
                case 2:
            
                    // Calculate total amount
                    if (cart.isEmpty()) {
                        System.out.println("No products in the cart. Please scan products first.");
                        break;
                    }
    
                    double totalAmount = cart.stream()
                                             .mapToDouble(p -> p.getPrice() * p.getStock())
                                             .sum();
                    System.out.println("Total Amount: $" + totalAmount);
    
                    // Process payment
                    System.out.print("Enter Payment Method (cash/credit/debit/digital wallet): ");
                    String paymentMethod = scanner.nextLine();
                    if (cashier.processPayment(paymentMethod, totalAmount)) {
                        // Update inventory and generate bill
                        cart.forEach(product -> inventory.updateInventory(product.getProductId(), product.getStock()));
                        new Bill("BILL-" + System.currentTimeMillis(), cashier.id, cart).generateBill();
                        cart.clear(); // Clear cart after successful checkout
                    } else {
                        System.out.println("Payment failed. Returning to menu and clear cart");
                        cart.clear();
                    }
                    break;
    

                case 3:
                cashier.viewPersonalInformation();
                break;

                case 4:
                // Update personal information
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                System.out.print("Enter new phone number: ");
                String newPhone = scanner.nextLine();

                cashier.editPersonalInformation(newName, newPhone);
                System.out.println("Personal information updated successfully.");
                break;
                case 5:
                    // Logout
                    System.out.println("Logging out...");
                    return;
    
                default:
                    // Invalid choice handling
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

