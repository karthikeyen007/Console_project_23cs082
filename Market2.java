import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Client {
    String mail;
    String key;
    String type;
    double funds;
    int points;
    List<Order> history;
    
    Client(String m, String k, String t) {
        mail = m;
        key = k;
        type = t;
        funds = 1000;
        points = 0;
        history = new ArrayList<>();
    }
    
    boolean verify(String m, String k) {
        return mail.equals(m) && key.equals(k);
    }
    
    void credit(double a) {
        funds += a;
    }
    
    boolean debit(double a) {
        if (funds >= a) {
            funds -= a;
            return true;
        }
        return false;
    }
    
    void grantRewardToClient(int p) {
        points += p;
        if (points >= 50) {
            funds += 100;
            points -= 50;
        }
    }
}

class Goods {
    int id;
    String name;
    double rate;
    int stock;
    
    Goods(int i, String n, double r, int s) {
        id = i;
        name = n;
        rate = r;
        stock = s;
    }
    
    void updateStock(int s) {
        stock = s;
    }
    
    void addStock(int s) {
        stock += s;
    }
    
    boolean reduceStock(int s) {
        if (stock >= s) {
            stock -= s;
            return true;
        }
        return false;
    }
}

class BasketItem {
    Goods goods;
    int qty;
    
    BasketItem(Goods g, int q) {
        goods = g;
        qty = q;
    }
    
    void setQuantity(int q) {
        qty = q;
    }
    
    void addQuantity(int q) {
        qty += q;
    }
    
    double calculateTotal() {
        return goods.rate * qty;
    }
}

class Basket {
    List<BasketItem> items = new ArrayList<>();
    
    void insertItem(Goods g, int q) {
        for (BasketItem i : items) {
            if (i.goods.id == g.id) {
                i.addQuantity(q);
                return;
            }
        }
        items.add(new BasketItem(g, q));
    }
    
    void changeQuantity(int gid, int q) {
        for (BasketItem i : items) {
            if (i.goods.id == gid) {
                if (q <= 0) {
                    items.remove(i);
                } else {
                    i.setQuantity(q);
                }
                return;
            }
        }
    }
    
    void deleteItem(int gid) {
        items.removeIf(i -> i.goods.id == gid);
    }
    
    double calculateTotalCost() {
        double sum = 0;
        for (BasketItem i : items) {
            sum += i.calculateTotal();
        }
        return sum;
    }
    
    void emptyBasket() {
        items.clear();
    }
}

class Order {
    int receipt;
    String mail;
    double amount;
    String time;
    Basket basket;
    
    Order(int r, String m, double a, Basket b) {
        receipt = r;
        mail = m;
        amount = a;
        basket = b;
        time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

public class Market2 {
    static List<Client> clients = new ArrayList<>();
    static List<Goods> inventory = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();
    static int receiptnum = 1;
    static int goodsid = 1;
    static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        initializemarket();
        logintomarket();
    }
    
    static void initializemarket() {
        clients.add(new Client("karthiv@shop2.com", "karthiv123", "admin"));
        clients.add(new Client("karthiv2@shop2.com", "karthiv123", "client"));
        
        inventory.add(new Goods(goodsid++, "Orange", 40, 120));
        inventory.add(new Goods(goodsid++, "Grapes", 60, 80));
        inventory.add(new Goods(goodsid++, "Juice", 30, 150));
        inventory.add(new Goods(goodsid++, "Biscuit", 20, 200));
        inventory.add(new Goods(goodsid++, "Chocolate", 50, 100));
    }
    
    static void logintomarket() {
        System.out.println("=== MARKET2 ===");
        System.out.print("Mail: ");
        String mail = input.nextLine();
        System.out.print("Key: ");
        String key = input.nextLine();
        
        Client c = findclient(mail, key);
        if (c != null) {
            System.out.println("Welcome " + c.type);
            if (c.type.equals("admin")) {
                managemarket(c);
            } else {
                shopatmarket(c);
            }
        } else {
            System.out.println("Error!");
            logintomarket();
        }
    }
    
    static Client findclient(String mail, String key) {
        for (Client c : clients) {
            if (c.verify(mail, key)) {
                return c;
            }
        }
        return null;
    }
    
    static void managemarket(Client c) {
        while (true) {
            System.out.println("\n=== MANAGE ===");
            System.out.println("1 Insert");
            System.out.println("2 Update");
            System.out.println("3 Remove");
            System.out.println("4 Display");
            System.out.println("5 Find");
            System.out.println("6 Funds");
            System.out.println("7 Client");
            System.out.println("8 Analytics");
            System.out.println("9 Leave");
            System.out.print("Option: ");
            
            int opt = input.nextInt();
            input.nextLine();
            
            if (opt == 1) insertproduct();
            else if (opt == 2) updateproduct();
            else if (opt == 3) removeproduct();
            else if (opt == 4) displayproducts("name");
            else if (opt == 5) findproduct();
            else if (opt == 6) managefunds();
            else if (opt == 7) enrollclient();
            else if (opt == 8) viewanalytics();
            else if (opt == 9) { logintomarket(); return; }
            else System.out.println("Invalid!");
        }
    }
    
    static void shopatmarket(Client c) {
        Basket basket = new Basket();
        while (true) {
            System.out.println("\n=== SHOP ===");
            System.out.println("Funds: " + c.funds + " Points: " + c.points);
            System.out.println("1 Display");
            System.out.println("2 Add");
            System.out.println("3 Basket");
            System.out.println("4 Edit");
            System.out.println("5 Pay");
            System.out.println("6 Past");
            System.out.println("7 Leave");
            System.out.print("Option: ");
            
            int opt = input.nextInt();
            input.nextLine();
            
            if (opt == 1) displayproducts("name");
            else if (opt == 2) addtobasket(c, basket);
            else if (opt == 3) showbasket(basket);
            else if (opt == 4) editbasket(basket);
            else if (opt == 5) makepayment(c, basket);
            else if (opt == 6) vieworderhistory(c);
            else if (opt == 7) { logintomarket(); return; }
            else System.out.println("Invalid!");
        }
    }
    
    static void insertproduct() {
        System.out.print("Name: ");
        String name = input.nextLine();
        System.out.print("Rate: ");
        double rate = input.nextDouble();
        System.out.print("Stock: ");
        int stock = input.nextInt();
        input.nextLine();
        
        inventory.add(new Goods(goodsid++, name, rate, stock));
        System.out.println("Inserted!");
    }
    
    static void updateproduct() {
        System.out.print("ID: ");
        int id = input.nextInt();
        input.nextLine();
        
        for (Goods g : inventory) {
            if (g.id == id) {
                System.out.print("Name (" + g.name + "): ");
                String name = input.nextLine();
                if (!name.isEmpty()) g.name = name;
                
                System.out.print("Rate (" + g.rate + "): ");
                String rate = input.nextLine();
                if (!rate.isEmpty()) g.rate = Double.parseDouble(rate);
                
                System.out.print("Stock (" + g.stock + "): ");
                String stock = input.nextLine();
                if (!stock.isEmpty()) g.updateStock(Integer.parseInt(stock));
                
                System.out.println("Updated!");
                return;
            }
        }
        System.out.println("Missing!");
    }
    
    static void removeproduct() {
        System.out.print("ID: ");
        int id = input.nextInt();
        input.nextLine();
        
        inventory.removeIf(g -> g.id == id);
        System.out.println("Removed!");
    }
    
    static void displayproducts(String sort) {
        System.out.println("\n=== INVENTORY ===");
        List<Goods> list = new ArrayList<>(inventory);
        if (sort.equals("name")) {
            list.sort((a, b) -> a.name.compareTo(b.name));
        } else if (sort.equals("rate")) {
            list.sort((a, b) -> Double.compare(a.rate, b.rate));
        }
        
        System.out.println("ID\tName\tRate\tStock");
        for (Goods g : list) {
            System.out.println(g.id + "\t" + g.name + "\t" + g.rate + "\t" + g.stock);
        }
    }
    
    static void findproduct() {
        System.out.print("Name: ");
        String name = input.nextLine();
        
        System.out.println("\n=== FOUND ===");
        for (Goods g : inventory) {
            if (g.name.toLowerCase().contains(name.toLowerCase())) {
                System.out.println(g.id + "\t" + g.name + "\t" + g.rate + "\t" + g.stock);
            }
        }
    }
    
    static void managefunds() {
        System.out.print("Mail: ");
        String mail = input.nextLine();
        System.out.print("Amount: ");
        double amt = input.nextDouble();
        input.nextLine();
        
        for (Client c : clients) {
            if (c.mail.equals(mail) && c.type.equals("client")) {
                c.credit(amt);
                System.out.println("Credited!");
                return;
            }
        }
        System.out.println("Missing!");
    }
    
    static void enrollclient() {
        System.out.print("Type (admin/client): ");
        String type = input.nextLine();
        System.out.print("Mail: ");
        String mail = input.nextLine();
        System.out.print("Key: ");
        String key = input.nextLine();
        
        clients.add(new Client(mail, key, type));
        System.out.println("Enrolled!");
    }
    
    static void viewanalytics() {
        System.out.println("\n=== ANALYTICS ===");
        System.out.println("1 Low");
        System.out.println("2 Unsold");
        System.out.println("3 Top");
        System.out.println("4 Admins");
        System.out.print("Option: ");
        
        int opt = input.nextInt();
        input.nextLine();
        
        if (opt == 1) checklowstock();
        else if (opt == 2) checkunsoldproducts();
        else if (opt == 3) checktopclients();
        else if (opt == 4) listadmins();
    }
    
    static void checklowstock() {
        System.out.println("\n=== LOW STOCK ===");
        for (Goods g : inventory) {
            if (g.stock < 50) {
                System.out.println(g.id + "\t" + g.name + "\t" + g.stock);
            }
        }
    }
    
    static void checkunsoldproducts() {
        System.out.println("\n=== UNSOLD ===");
        Set<Integer> sold = new HashSet<>();
        for (Order o : orders) {
            for (BasketItem i : o.basket.items) {
                sold.add(i.goods.id);
            }
        }
        
        for (Goods g : inventory) {
            if (!sold.contains(g.id)) {
                System.out.println(g.id + "\t" + g.name);
            }
        }
    }
    
    static void checktopclients() {
        System.out.println("\n=== TOP CLIENTS ===");
        Map<String, Double> spend = new HashMap<>();
        
        for (Order o : orders) {
            spend.put(o.mail, spend.getOrDefault(o.mail, 0.0) + o.amount);
        }
        
        spend.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
    }
    
    static void listadmins() {
        System.out.println("\n=== ADMINS ===");
        for (Client c : clients) {
            if (c.type.equals("admin")) {
                System.out.println(c.mail);
            }
        }
    }
    
    static void addtobasket(Client c, Basket b) {
        displayproducts("name");
        System.out.print("ID: ");
        int id = input.nextInt();
        System.out.print("Qty: ");
        int qty = input.nextInt();
        input.nextLine();
        
        for (Goods g : inventory) {
            if (g.id == id && g.stock >= qty) {
                b.insertItem(g, qty);
                System.out.println("Added!");
                return;
            }
        }
        System.out.println("No stock!");
    }
    
    static void showbasket(Basket b) {
        System.out.println("\n=== BASKET ===");
        System.out.println("ID\tName\tQty\tRate\tTotal");
        for (BasketItem i : b.items) {
            System.out.println(i.goods.id + "\t" + i.goods.name + "\t" + i.qty + "\t" + i.goods.rate + "\t" + i.calculateTotal());
        }
        System.out.println("Total: " + b.calculateTotalCost());
    }
    
    static void editbasket(Basket b) {
        showbasket(b);
        System.out.print("ID: ");
        int id = input.nextInt();
        System.out.print("Qty (0 remove): ");
        int qty = input.nextInt();
        input.nextLine();
        
        b.changeQuantity(id, qty);
        System.out.println("Updated!");
    }
    
    static void makepayment(Client c, Basket b) {
        double total = b.calculateTotalCost();
        if (total == 0) {
            System.out.println("Empty!");
            return;
        }
        
        if (c.funds >= total) {
            c.debit(total);
            
            if (total >= 5000) {
                c.credit(100);
                System.out.println("Rs 100 discount!");
            }
            
            int pts = (int) (total / 100);
            c.grantRewardToClient(pts);
            
            Order o = new Order(receiptnum++, c.mail, total, b);
            orders.add(o);
            c.history.add(o);
            
            for (BasketItem i : b.items) {
                for (Goods g : inventory) {
                    if (g.id == i.goods.id) {
                        g.reduceStock(i.qty);
                        break;
                    }
                }
            }
            
            System.out.println("Paid!");
            System.out.println("Receipt: " + o.receipt);
            System.out.println("Total: " + total);
            System.out.println("Funds: " + c.funds);
            System.out.println("Points: " + c.points);
            
            b.emptyBasket();
        } else {
            System.out.println("No funds!");
        }
    }
    
    static void vieworderhistory(Client c) {
        System.out.println("\n=== PAST ===");
        for (Order o : c.history) {
            System.out.println("Receipt: " + o.receipt + " Time: " + o.time + " Total: " + o.amount);
        }
    }
}
