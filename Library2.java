import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

class Member {
    String mail;
    String key;
    String type;
    double balance;
    double penalty;
    List<Loan> records;
    List<Item> taken;
    
    Member(String m, String k, String t) {
        mail = m;
        key = k;
        type = t;
        balance = 1500;
        penalty = 0;
        records = new ArrayList<>();
        taken = new ArrayList<>();
    }
    
    boolean verify(String m, String k) {
        return mail.equals(m) && key.equals(k);
    }
    
    boolean eligible() {
        return taken.size() < 3 && balance >= 500;
    }
    
    void pick(Item i) {
        taken.add(i);
    }
    
    void drop(Item i) {
        taken.remove(i);
    }
    
    void charge(double p) {
        penalty += p;
        if (balance >= penalty) {
            balance -= penalty;
            penalty = 0;
        }
    }
}

class Item {
    String code;
    String title;
    String writer;
    double price;
    int total;
    int stock;
    
    Item(String c, String t, String w, double p, int s) {
        code = c;
        title = t;
        writer = w;
        price = p;
        total = s;
        stock = s;
    }
    
    void update(int s) {
        total = s;
        stock = s;
    }
    
    boolean lendBook() {
        if (stock > 0) {
            stock--;
            return true;
        }
        return false;
    }
    
    void returnBook() {
        stock++;
    }
}

class Loan {
    String member;
    String item;
    String start;
    String end;
    double fee;
    String cause;
    
    Loan(String m, String i, String s, String e, double f, String c) {
        member = m;
        item = i;
        start = s;
        end = e;
        fee = f;
        cause = c;
    }
}

public class Library2 {
    static List<Member> members = new ArrayList<>();
    static List<Item> items = new ArrayList<>();
    static List<Loan> loans = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    
    public static void main(String[] args) {
        setupLibrary();
        authenticateUser();
    }
    
    static void setupLibrary() {
        members.add(new Member("karthiv@lib2.com", "karthiv123", "admin"));
        members.add(new Member("karthiv2@lib2.com", "karthiv123", "member"));
        
        items.add(new Item("LIB001", "Python Guide", "Martin", 400, 4));
        items.add(new Item("LIB002", "Web Design", "Jennifer", 350, 6));
        items.add(new Item("LIB003", "Mobile Apps", "Smith", 500, 3));
        items.add(new Item("LIB004", "Cloud Computing", "Brown", 650, 2));
        items.add(new Item("LIB005", "AI Basics", "Wilson", 700, 3));
    }
    
    static void authenticateUser() {
        System.out.println("=== LIBRARY2 ===");
        System.out.print("Mail: ");
        String mail = input.nextLine();
        System.out.print("Key: ");
        String key = input.nextLine();
        
        Member m = findMember(mail, key);
        if (m != null) {
            System.out.println("Welcome " + m.type);
            if (m.type.equals("admin")) {
                displayAdminPanel(m);
            } else {
                displayUserPanel(m);
            }
        } else {
            System.out.println("Error!");
            authenticateUser();
        }
    }
    
    static Member findMember(String mail, String key) {
        for (Member m : members) {
            if (m.verify(mail, key)) {
                return m;
            }
        }
        return null;
    }
    
    static void displayAdminPanel(Member m) {
        while (true) {
            System.out.println("\n=== CONTROL ===");
            System.out.println("1 New");
            System.out.println("2 Change");
            System.out.println("3 Remove");
            System.out.println("4 List");
            System.out.println("5 Find");
            System.out.println("6 Penalty");
            System.out.println("7 Add Member");
            System.out.println("8 Stats");
            System.out.println("9 Exit");
            System.out.print("Option: ");
            
            int opt = input.nextInt();
            input.nextLine();
            
            if (opt == 1) addNewBook();
            else if (opt == 2) modifyBook();
            else if (opt == 3) removeBook();
            else if (opt == 4) displayBookList("title");
            else if (opt == 5) searchBook();
            else if (opt == 6) imposePenalty();
            else if (opt == 7) registerNewMember();
            else if (opt == 8) displayStatistics();
            else if (opt == 9) { authenticateUser(); return; }
            else System.out.println("Invalid!");
        }
    }
    
    static void displayUserPanel(Member m) {
        while (true) {
            System.out.println("\n=== ACCESS ===");
            System.out.println("Balance: " + m.balance + " Penalty: " + m.penalty);
            System.out.println("Items: " + m.taken.size() + "/3");
            System.out.println("1 List");
            System.out.println("2 Take");
            System.out.println("3 Return");
            System.out.println("4 Extend");
            System.out.println("5 Missing");
            System.out.println("6 Card Lost");
            System.out.println("7 Past");
            System.out.println("8 Quit");
            System.out.print("Option: ");
            
            int opt = input.nextInt();
            input.nextLine();
            
            if (opt == 1) displayBookList("title");
            else if (opt == 2) borrowBook(m);
            else if (opt == 3) returnBook(m);
            else if (opt == 4) extendDueDate(m);
            else if (opt == 5) reportLostBook(m);
            else if (opt == 6) reportLostCard(m);
            else if (opt == 7) displayBorrowingHistory(m);
            else if (opt == 8) { authenticateUser(); return; }
            else System.out.println("Invalid!");
        }
    }
    
    static void addNewBook() {
        System.out.print("Code: ");
        String code = input.nextLine();
        System.out.print("Title: ");
        String title = input.nextLine();
        System.out.print("Writer: ");
        String writer = input.nextLine();
        System.out.print("Price: ");
        double price = input.nextDouble();
        System.out.print("Stock: ");
        int stock = input.nextInt();
        input.nextLine();
        
        items.add(new Item(code, title, writer, price, stock));
        System.out.println("Added!");
    }
    
    static void modifyBook() {
        System.out.print("Code: ");
        String code = input.nextLine();
        
        for (Item i : items) {
            if (i.code.equals(code)) {
                System.out.print("Title (" + i.title + "): ");
                String title = input.nextLine();
                if (!title.isEmpty()) i.title = title;
                
                System.out.print("Writer (" + i.writer + "): ");
                String writer = input.nextLine();
                if (!writer.isEmpty()) i.writer = writer;
                
                System.out.print("Price (" + i.price + "): ");
                String price = input.nextLine();
                if (!price.isEmpty()) i.price = Double.parseDouble(price);
                
                System.out.print("Stock (" + i.total + "): ");
                String stock = input.nextLine();
                if (!stock.isEmpty()) i.update(Integer.parseInt(stock));
                
                System.out.println("Updated!");
                return;
            }
        }
        System.out.println("Missing!");
    }
    
    static void removeBook() {
        System.out.print("Code: ");
        String code = input.nextLine();
        
        items.removeIf(i -> i.code.equals(code));
        System.out.println("Removed!");
    }
    
    static void displayBookList(String sort) {
        System.out.println("\n=== CATALOG ===");
        List<Item> list = new ArrayList<>(items);
        if (sort.equals("title")) {
            list.sort((a, b) -> a.title.compareTo(b.title));
        } else if (sort.equals("stock")) {
            list.sort((a, b) -> Integer.compare(b.stock, a.stock));
        }
        
        System.out.println("Code\tTitle\tWriter\tPrice\tTotal\tStock");
        for (Item i : list) {
            System.out.println(i.code + "\t" + i.title + "\t" + i.writer + "\t" + i.price + "\t" + i.total + "\t" + i.stock);
        }
    }
    
    static void searchBook() {
        System.out.print("Find (Title/Code): ");
        String term = input.nextLine();
        
        System.out.println("\n=== RESULTS ===");
        for (Item i : items) {
            if (i.title.toLowerCase().contains(term.toLowerCase()) || i.code.toLowerCase().contains(term.toLowerCase())) {
                System.out.println(i.code + "\t" + i.title + "\t" + i.writer + "\t" + i.stock);
            }
        }
    }
    
    static void imposePenalty() {
        System.out.print("Mail: ");
        String mail = input.nextLine();
        System.out.print("Penalty: ");
        double penalty = input.nextDouble();
        input.nextLine();
        
        for (Member m : members) {
            if (m.mail.equals(mail) && m.type.equals("member")) {
                m.charge(penalty);
                System.out.println("Charged!");
                return;
            }
        }
        System.out.println("Missing!");
    }
    
    static void registerNewMember() {
        System.out.print("Type (admin/member): ");
        String type = input.nextLine();
        System.out.print("Mail: ");
        String mail = input.nextLine();
        System.out.print("Key: ");
        String key = input.nextLine();
        
        members.add(new Member(mail, key, type));
        System.out.println("Enrolled!");
    }
    
    static void displayStatistics() {
        System.out.println("\n=== STATISTICS ===");
        System.out.println("1 Low");
        System.out.println("2 Unused");
        System.out.println("3 Popular");
        System.out.println("4 Late");
        System.out.println("5 Track");
        System.out.print("Option: ");
        
        int opt = input.nextInt();
        input.nextLine();
        
        if (opt == 1) checkLowStock();
        else if (opt == 2) checkUnusedBooks();
        else if (opt == 3) checkPopularBooks();
        else if (opt == 4) checkOverdueBooks();
        else if (opt == 5) trackBook();
    }
    
    static void checkLowStock() {
        System.out.println("\n=== LOW STOCK ===");
        for (Item i : items) {
            if (i.stock < 2) {
                System.out.println(i.code + "\t" + i.title + "\t" + i.stock);
            }
        }
    }
    
    static void checkUnusedBooks() {
        System.out.println("\n=== UNUSED ===");
        Set<String> used = new HashSet<>();
        for (Loan l : loans) {
            used.add(l.item);
        }
        
        for (Item i : items) {
            if (!used.contains(i.code)) {
                System.out.println(i.code + "\t" + i.title);
            }
        }
    }
    
    static void checkPopularBooks() {
        System.out.println("\n=== POPULAR ===");
        Map<String, Integer> count = new HashMap<>();
        
        for (Loan l : loans) {
            count.put(l.item, count.getOrDefault(l.item, 0) + 1);
        }
        
        count.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .forEach(e -> {
                for (Item i : items) {
                    if (i.code.equals(e.getKey())) {
                        System.out.println(i.code + "\t" + i.title + "\t" + e.getValue());
                        break;
                    }
                }
            });
    }
    
    static void checkOverdueBooks() {
        System.out.print("Date (DD/MM/YYYY): ");
        String date = input.nextLine();
        
        System.out.println("\n=== LATE ===");
        for (Loan l : loans) {
            if (l.end.compareTo(date) < 0) {
                System.out.println(l.member + "\t" + l.item + "\t" + l.end);
            }
        }
    }
    
    static void trackBook() {
        System.out.print("Code: ");
        String code = input.nextLine();
        
        for (Loan l : loans) {
            if (l.item.equals(code)) {
                System.out.println("Member: " + l.member);
                System.out.println("Taken: " + l.start);
                System.out.println("Due: " + l.end);
                return;
            }
        }
        System.out.println("Available");
    }
    
    static void borrowBook(Member m) {
        if (!m.eligible()) {
            System.out.println("Not eligible!");
            return;
        }
        
        displayBookList("title");
        System.out.print("Code: ");
        String code = input.nextLine();
        
        for (Item i : items) {
            if (i.code.equals(code) && i.lendBook()) {
                boolean exists = false;
                for (Item ti : m.taken) {
                    if (ti.code.equals(code)) {
                        exists = true;
                        break;
                    }
                }
                
                if (!exists) {
                    m.pick(i);
                    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String due = LocalDate.now().plusDays(15).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    loans.add(new Loan(m.mail, code, today, due, 0, "Borrow"));
                    System.out.println("Taken! Due: " + due);
                } else {
                    i.returnBook();
                    System.out.println("Already taken!");
                }
                return;
            }
        }
        System.out.println("Not available!");
    }
    
    static void returnBook(Member m) {
        System.out.println("Taken items:");
        for (int i = 0; i < m.taken.size(); i++) {
            Item item = m.taken.get(i);
            System.out.println((i+1) + ". " + item.code + "\t" + item.title);
        }
        
        System.out.print("Item number: ");
        int num = input.nextInt();
        input.nextLine();
        
        if (num > 0 && num <= m.taken.size()) {
            Item item = m.taken.get(num - 1);
            item.returnBook();
            m.drop(item);
            
            System.out.print("Return date (DD/MM/YYYY): ");
            String rdate = input.nextLine();
            
            double fee = calculateFine(item, rdate);
            if (fee > 0) {
                m.charge(fee);
                System.out.println("Fee: " + fee);
            }
            
            System.out.println("Returned!");
        }
    }
    
    static void extendDueDate(Member m) {
        System.out.println("Taken items:");
        for (int i = 0; i < m.taken.size(); i++) {
            Item item = m.taken.get(i);
            System.out.println((i+1) + ". " + item.code + "\t" + item.title);
        }
        
        System.out.print("Item number: ");
        int num = input.nextInt();
        input.nextLine();
        
        if (num > 0 && num <= m.taken.size()) {
            System.out.print("Days: ");
            int days = input.nextInt();
            input.nextLine();
            
            if (days <= 15) {
                System.out.println("Extended!");
            } else {
                System.out.println("Max 15 days!");
            }
        }
    }
    
    static void reportLostBook(Member m) {
        System.out.println("Taken items:");
        for (int i = 0; i < m.taken.size(); i++) {
            Item item = m.taken.get(i);
            System.out.println((i+1) + ". " + item.code + "\t" + item.title);
        }
        
        System.out.print("Item number: ");
        int num = input.nextInt();
        input.nextLine();
        
        if (num > 0 && num <= m.taken.size()) {
            Item item = m.taken.get(num - 1);
            double fee = item.price * 0.5;
            m.charge(fee);
            m.drop(item);
            System.out.println("Fee: " + fee);
        }
    }
    
    static void reportLostCard(Member m) {
        m.charge(10);
        System.out.println("Card fee: 10");
    }
    
    static void displayBorrowingHistory(Member m) {
        System.out.println("\n=== PAST ===");
        System.out.println("Penalties:");
        for (Loan l : m.records) {
            if (l.fee > 0) {
                System.out.println(l.item + "\t" + l.cause + "\t" + l.fee);
            }
        }
        
        System.out.println("\nLoans:");
        for (Loan l : m.records) {
            System.out.println(l.item + "\t" + l.start + "\t" + l.end);
        }
    }
    
    static double calculateFine(Item i, String rdate) {
        try {
            LocalDate ret = LocalDate.parse(rdate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            LocalDate due = LocalDate.now().plusDays(15);
            
            if (ret.isAfter(due)) {
                long days = ChronoUnit.DAYS.between(due, ret);
                double fee = days * 2;
                
                if (days > 10) {
                    fee = fee * Math.pow(1.5, (days - 10) / 10);
                }
                
                double max = i.price * 0.8;
                return Math.min(fee, max);
            }
        } catch (Exception e) {
            System.out.println("Bad date!");
        }
        return 0;
    }
}
