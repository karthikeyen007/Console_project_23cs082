import java.util.*;

class Card {
    String id;
    String code;
    double cash;
    List<String> logs;
    String holder;
    String mobile;
    String mail;
    double dayLimit;
    double dayUsed;
    String lastDay;
    boolean locked;
    int badCode;
    
    Card(String i, String c, double ca, String holder, String mobile, String mail) {
        id = i;
        code = c;
        cash = ca;
        logs = new ArrayList<>();
        this.holder = holder;
        this.mobile = mobile;
        this.mail = mail;
        dayLimit = 25000;
        dayUsed = 0;
        lastDay = java.time.LocalDate.now().toString();
        locked = false;
        badCode = 0;
    }
    
    boolean verify(String i, String c) {
        if (locked) return false;
        if (id.equals(i) && code.equals(c)) {
            badCode = 0;
            return true;
        }
        badCode++;
        if (badCode >= 3) {
            locked = true;
        }
        return false;
    }
    
    void credit(double a) {
        cash += a;
        logs.add(java.time.LocalDateTime.now() + " Credit: " + a);
    }
    
    boolean debit(double a) {
        if (locked) return false;
        if (cash >= a && (dayUsed + a) <= dayLimit) {
            cash -= a;
            dayUsed += a;
            logs.add(java.time.LocalDateTime.now() + " Debit: " + a);
            return true;
        }
        return false;
    }
    
    void resetDay() {
        String today = java.time.LocalDate.now().toString();
        if (!today.equals(lastDay)) {
            dayUsed = 0;
            lastDay = today;
        }
    }
    
    void updateCode(String newCode) {
        code = newCode;
        logs.add(java.time.LocalDateTime.now() + " Code Updated");
    }
}

public class atm2 {
    static List<Card> cards = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    static Card active = null;
    
    public static void main(String[] args) {
        setup();
        enter();
    }
    
    static void setup() {
        cards.add(new Card("987654", "9999", 6000, "Alice Brown", "9876543213", "alice@email.com"));
        cards.add(new Card("543210", "8888", 4000, "Charlie Davis", "9876543214", "charlie@email.com"));
        cards.add(new Card("135791", "7777", 9000, "Diana Miller", "9876543215", "diana@email.com"));
    }
    
    static void enter() {
        System.out.println("=== ATM2 ===");
        System.out.print("Card: ");
        String card = input.nextLine();
        System.out.print("Code: ");
        String code = input.nextLine();
        
        for (Card c : cards) {
            c.resetDay();
            if (c.verify(card, code)) {
                if (c.locked) {
                    System.out.println("Card locked!");
                    enter();
                    return;
                }
                active = c;
                panel();
                return;
            }
        }
        System.out.println("Error!");
        enter();
    }
    
    static void panel() {
        while (true) {
            System.out.println("\n=== PANEL ===");
            System.out.println("1 Cash");
            System.out.println("2 Take");
            System.out.println("3 Add");
            System.out.println("4 Logs");
            System.out.println("5 Send");
            System.out.println("6 Code");
            System.out.println("7 Info");
            System.out.println("8 Slip");
            System.out.println("9 Quit");
            System.out.print("Option: ");
            
            int opt = input.nextInt();
            input.nextLine();
            
            if (opt == 1) cash();
            else if (opt == 2) take();
            else if (opt == 3) add();
            else if (opt == 4) logs();
            else if (opt == 5) send();
            else if (opt == 6) code();
            else if (opt == 7) info();
            else if (opt == 8) slip();
            else if (opt == 9) { active = null; enter(); return; }
            else System.out.println("Invalid!");
        }
    }
    
    static void cash() {
        System.out.println("Cash: " + active.cash);
    }
    
    static void take() {
        System.out.println("Day Limit: " + active.dayLimit);
        System.out.println("Day Used: " + active.dayUsed);
        System.out.print("Amount: ");
        double amt = input.nextDouble();
        input.nextLine();
        
        if (amt <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        
        if (active.debit(amt)) {
            System.out.println("Taken: " + amt);
            System.out.println("Cash: " + active.cash);
        } else {
            if (active.cash < amt) {
                System.out.println("No cash!");
            } else {
                System.out.println("Day limit exceeded!");
            }
        }
    }
    
    static void add() {
        System.out.print("Amount: ");
        double amt = input.nextDouble();
        input.nextLine();
        
        if (amt <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        
        active.credit(amt);
        System.out.println("Added: " + amt);
        System.out.println("Cash: " + active.cash);
    }
    
    static void logs() {
        System.out.println("\n=== LOGS ===");
        for (String l : active.logs) {
            System.out.println(l);
        }
    }
    
    static void send() {
        System.out.print("To Card: ");
        String toCard = input.nextLine();
        System.out.print("Amount: ");
        double amt = input.nextDouble();
        input.nextLine();
        
        if (amt <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        
        for (Card c : cards) {
            if (c.id.equals(toCard) && !c.id.equals(active.id)) {
                if (active.debit(amt + 10)) {
                    c.credit(amt);
                    active.logs.add(java.time.LocalDateTime.now() + " Send to " + toCard + ": " + amt + " (Fee: 10)");
                    c.logs.add(java.time.LocalDateTime.now() + " Receive from " + active.id + ": " + amt);
                    System.out.println("Sent: " + amt);
                    System.out.println("Fee: 10");
                    System.out.println("Cash: " + active.cash);
                    return;
                } else {
                    System.out.println("No cash!");
                    return;
                }
            }
        }
        System.out.println("Card not found!");
    }
    
    static void code() {
        System.out.print("Current Code: ");
        String oldCode = input.nextLine();
        System.out.print("New Code: ");
        String newCode = input.nextLine();
        System.out.print("Confirm Code: ");
        String confCode = input.nextLine();
        
        if (!active.code.equals(oldCode)) {
            System.out.println("Wrong current code!");
            return;
        }
        
        if (!newCode.equals(confCode)) {
            System.out.println("Codes don't match!");
            return;
        }
        
        if (newCode.length() != 4) {
            System.out.println("Code must be 4 digits!");
            return;
        }
        
        active.updateCode(newCode);
        System.out.println("Code updated!");
    }
    
    static void info() {
        System.out.println("\n=== INFO ===");
        System.out.println("Holder: " + active.holder);
        System.out.println("Card: " + active.id);
        System.out.println("Mobile: " + active.mobile);
        System.out.println("Mail: " + active.mail);
        System.out.println("Cash: " + active.cash);
        System.out.println("Day Limit: " + active.dayLimit);
        System.out.println("Day Used: " + active.dayUsed);
        System.out.println("Status: " + (active.locked ? "Locked" : "Active"));
    }
    
    static void slip() {
        System.out.println("\n=== SLIP ===");
        System.out.println("Card: " + active.id);
        System.out.println("Holder: " + active.holder);
        System.out.println("Cash: " + active.cash);
        System.out.println("Last 5 Logs:");
        int start = Math.max(0, active.logs.size() - 5);
        for (int i = start; i < active.logs.size(); i++) {
            System.out.println(active.logs.get(i));
        }
    }
}
