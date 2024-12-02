import java.util.List;
import java.util.Scanner;

public class Cashier extends Employee {
    public Cashier(String id, String username, String password, String name, String phone) {
        super(id, username, password, "cashier", name, phone);
    }

    public void scanProduct(List<Product> cart, String productId, int quantity, Inventory inventory) {
        Product product = inventory.getProduct(productId);
        if (product != null && product.getStock() >= quantity) {
            cart.add(new Product(productId, product.getProductName(), product.getPrice(), quantity));
            System.out.println("Scanned: " + product.getProductInfo() + " | Quantity: " + quantity);
        } else {
            System.out.println("Product not found or insufficient stock.");
        }
    }

    public boolean processPayment(String paymentMethod, double totalAmount) {
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Amount Due: $" + totalAmount);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Is payment successful? (yes/no): ");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }
}