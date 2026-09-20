import java.util.List;

// ============================================================================
// 1. SINGLE RESPONSIBILITY PRINCIPLE (SRP)
// A class should have only ONE reason to change.
// ============================================================================
class Item {
    String name;
    int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }
}

// Responsibility 1: Calculation logic ONLY
class Invoice {
    private Item item;
    private int quantity;

    public Invoice(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public int calculateTotal() {
        return item.price * this.quantity;
    }

    public Item getItem() { return item; }
    public int getQuantity() { return quantity; }
}

// Responsibility 2: Printing logic ONLY
class InvoicePrinter {
    public void printInvoice(Invoice invoice) {
        System.out.println("[SRP] Printing Invoice: " + invoice.getQuantity() + "x " 
            + invoice.getItem().name + " | Total: Rs." + invoice.calculateTotal());
    }
}

// ============================================================================
// 2. OPEN / CLOSED PRINCIPLE (OCP)
// Open for extension, closed for modification.
// ============================================================================
// Core persistence interface
interface InvoiceDao {
    void save(Invoice invoice);
}

// Extended for Database persistence without modifying original classes
class DatabaseInvoiceDao implements InvoiceDao {
    @Override
    public void save(Invoice invoice) {
        System.out.println("[OCP] Saved invoice to SQL Database.");
    }
}

// Extended for File persistence without modifying DatabaseInvoiceDao or Invoice
class FileInvoiceDao implements InvoiceDao {
    @Override
    public void save(Invoice invoice) {
        System.out.println("[OCP] Saved invoice to File System.");
    }
}

// ============================================================================
// 3. LISKOV SUBSTITUTION PRINCIPLE (LSP)
// Subclasses should extend parent capabilities, not narrow them down.
// ============================================================================
abstract class Vehicle {
    public abstract int getNumberOfWheels();
}

// Segregated base class specifically for motorized vehicles
abstract class EngineVehicle extends Vehicle {
    public abstract boolean hasEngine();
}

class Car extends EngineVehicle {
    @Override
    public int getNumberOfWheels() {
        return 4;
    }

    @Override
    public boolean hasEngine() {
        return true;
    }
}

class MotorCycle extends EngineVehicle {
    @Override
    public int getNumberOfWheels() {
        return 2;
    }

    @Override
    public boolean hasEngine() {
        return true;
    }
}

// Bicycle extends base Vehicle; it is never forced to implement hasEngine()
class Bicycle extends Vehicle {
    @Override
    public int getNumberOfWheels() {
        return 2;
    }
}

// ============================================================================
// 4. INTERFACE SEGREGATION PRINCIPLE (ISP)
// Segment interfaces into focused contracts so clients don't implement dead methods.
// ============================================================================
interface WaiterInterface {
    void serveCustomers();
    void takeOrder();
}

interface ChefInterface {
    void cookFood();
    void decideMenu();
}

class Waiter implements WaiterInterface {
    @Override
    public void serveCustomers() {
        System.out.println("[ISP] Waiter is serving food to customers.");
    }

    @Override
    public void takeOrder() {
        System.out.println("[ISP] Waiter is taking the customer order.");
    }
}

class Chef implements ChefInterface {
    @Override
    public void cookFood() {
        System.out.println("[ISP] Chef is preparing dishes.");
    }

    @Override
    public void decideMenu() {
        System.out.println("[ISP] Chef is planning the seasonal menu.");
    }
}

// ============================================================================
// 5. DEPENDENCY INVERSION PRINCIPLE (DIP)
// Depend on abstractions (interfaces), not concrete classes.
// ============================================================================
interface Keyboard {
    void type();
}

interface Mouse {
    void click();
}

class WiredKeyboard implements Keyboard {
    @Override
    public void type() {
        System.out.println("[DIP] Typing on Wired Keyboard.");
    }
}

class BluetoothKeyboard implements Keyboard {
    @Override
    public void type() {
        System.out.println("[DIP] Typing on Bluetooth Keyboard.");
    }
}

class WiredMouse implements Mouse {
    @Override
    public void click() {
        System.out.println("[DIP] Clicking Wired Mouse.");
    }
}

class BluetoothMouse implements Mouse {
    @Override
    public void click() {
        System.out.println("[DIP] Clicking Bluetooth Mouse.");
    }
}

// High-level module depends strictly on Keyboard and Mouse interfaces
class MacBook {
    private final Keyboard keyboard;
    private final Mouse mouse;

    // Injected via constructor
    public MacBook(Keyboard keyboard, Mouse mouse) {
        this.keyboard = keyboard;
        this.mouse = mouse;
    }

    public void work() {
        keyboard.type();
        mouse.click();
    }
}

// ============================================================================
// DRIVER DEMO EXECUTION
// ============================================================================
public class SolidPrinciplesDemo {
    public static void main(String[] args) {
        // 1. SRP Demo
        System.out.println("=== 1. Single Responsibility Principle ===");
        Item notebook = new Item("Design Patterns Notebook", 120);
        Invoice invoice = new Invoice(notebook, 2);
        InvoicePrinter printer = new InvoicePrinter();
        printer.printInvoice(invoice);

        // 2. OCP Demo
        System.out.println("\n=== 2. Open/Closed Principle ===");
        InvoiceDao dbDao = new DatabaseInvoiceDao();
        InvoiceDao fileDao = new FileInvoiceDao();
        dbDao.save(invoice);
        fileDao.save(invoice);

        // 3. LSP Demo
        System.out.println("\n=== 3. Liskov Substitution Principle ===");
        List<Vehicle> allVehicles = List.of(new Car(), new MotorCycle(), new Bicycle());
        for (Vehicle v : allVehicles) {
            System.out.println(v.getClass().getSimpleName() + " wheel count: " + v.getNumberOfWheels());
        }

        // 4. ISP Demo
        System.out.println("\n=== 4. Interface Segregation Principle ===");
        WaiterInterface waiter = new Waiter();
        waiter.takeOrder();
        waiter.serveCustomers();

        ChefInterface chef = new Chef();
        chef.cookFood();
        chef.decideMenu();

        // 5. DIP Demo
        System.out.println("\n=== 5. Dependency Inversion Principle ===");
        // Wired Setup
        MacBook wiredDesk = new MacBook(new WiredKeyboard(), new WiredMouse());
        wiredDesk.work();

        // Wireless Setup (Plug-and-play without touching MacBook class)
        MacBook wirelessDesk = new MacBook(new BluetoothKeyboard(), new BluetoothMouse());
        wirelessDesk.work();
    }
}