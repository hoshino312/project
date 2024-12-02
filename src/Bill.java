import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Bill {
    private String billId;
    private String employeeId;
    private List<Product> productList;
    private double totalAmount;
    private String date;

    public Bill(String billId, String employeeId, List<Product> productList) {
        this.billId = billId;
        this.employeeId = employeeId;
        this.productList = productList;
        this.totalAmount = calculateTotal();
        this.date = getCurrentDate(); // Group bills by date
    }

    private double calculateTotal() {
        double total = 0;
        for (Product product : productList) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public void generateBill() {
        JSONObject billData = new JSONObject();
        billData.put("billId", billId);
        billData.put("employeeId", employeeId);

        JSONArray productsData = new JSONArray();
        for (Product product : productList) {
            JSONObject productData = new JSONObject();
            productData.put("productId", product.getProductId());
            productData.put("productName", product.getProductName());
            productData.put("quantity", product.getStock());
            productData.put("price", product.getPrice());
            productsData.add(productData);
        }

        billData.put("products", productsData);
        billData.put("totalAmount", totalAmount);

        JSONObject database = DatabaseHelper.loadDatabase("bills.json");
        JSONObject billsByDate = (JSONObject) database.get("bills");
        if (billsByDate == null) {
            billsByDate = new JSONObject();
        }

        JSONArray dailyBills = (JSONArray) billsByDate.get(date);
        if (dailyBills == null) {
            dailyBills = new JSONArray();
        }

        dailyBills.add(billData);
        billsByDate.put(date, dailyBills);
        database.put("bills", billsByDate);

        DatabaseHelper.saveDatabase("bills.json", database);

        System.out.println("Bill generated successfully! Total: $" + totalAmount);
    }
}
