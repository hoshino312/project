import java.text.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import java.text.SimpleDateFormat;
import java.util.*;

public class Manager extends Employee {
    public Manager(String id, String username, String password, String name, String phone) {
        super(id, username, password, "manager", name, phone);
    }

    public void monitorInventory() {
        Inventory inventory = new Inventory();
        System.out.println("\n--- Inventory Details ---");
        for (Product product : inventory.getInventoryList()) {
            System.out.println(product.getProductInfo());
        }
    }

    public List<Product> filterLowStock() {
        Inventory inventory = new Inventory();
        return inventory.getLowStockProducts();
    }

    public void replenishItem(String productId, int quantity) {
        Inventory inventory = new Inventory();
        inventory.updateInventory(productId, - quantity);
        System.out.println("Product replenished successfully!");
    }

    public void accessSalesReports(String productId, String period, String startDate) {
        JSONObject database = DatabaseHelper.loadDatabase("bills.json");
        JSONObject billsByDate = (JSONObject) database.get("bills");
        if (billsByDate == null) {
            System.out.println("No sales data available.");
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        double totalSales = 0;
        boolean productSpecific = !productId.isEmpty();

        try {
            Date start = formatter.parse(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);

            for (int i = 0; i < (period.equals("7-day") ? 7 : 1); i++) {
                String currentDate = formatter.format(cal.getTime());
                JSONArray dailyBills = (JSONArray) billsByDate.get(currentDate);
                if (dailyBills != null) {
                    for (Object obj : dailyBills) {
                        JSONObject bill = (JSONObject) obj;
                        JSONArray products = (JSONArray) bill.get("products");
                        for (Object productObj : products) {
                            JSONObject product = (JSONObject) productObj;
                            if (!productSpecific || product.get("productId").equals(productId)) {
                                totalSales += (double) product.get("price") * ((Long) product.get("quantity")).intValue();
                            }
                        }
                    }
                }
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }

            System.out.println("Sales Report:");
            System.out.println(productSpecific ? "Product ID: " + productId : "Overall Sales");
            System.out.println("Period: " + period + " from " + startDate);
            System.out.println("Total Sales: $" + totalSales);

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }

    public void reviewPerformance(String employeeId, String period, String startDate) {
        JSONObject database = DatabaseHelper.loadDatabase("bills.json");
        JSONObject billsByDate = (JSONObject) database.get("bills");
        if (billsByDate == null) {
            System.out.println("No performance data available.");
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        int invoiceCount = 0;

        try {
            Date start = formatter.parse(startDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(start);

            for (int i = 0; i < (period.equals("7-day") ? 7 : 1); i++) {
                String currentDate = formatter.format(cal.getTime());
                JSONArray dailyBills = (JSONArray) billsByDate.get(currentDate);
                if (dailyBills != null) {
                    for (Object obj : dailyBills) {
                        JSONObject bill = (JSONObject) obj;
                        if (bill.get("employeeId").equals(employeeId)) {
                            invoiceCount++;
                        }
                    }
                }
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }

            System.out.println("Performance Report:");
            System.out.println("Employee ID: " + employeeId);
            System.out.println("Period: " + period + " from " + startDate);
            System.out.println("Invoices Created: " + invoiceCount);

        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        }
    }
}
