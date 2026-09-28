import java.util.ArrayList;
import java.util.List;

// ============================================================================
// 1. OBSERVER INTERFACE (The Listener Contract)
// ============================================================================
interface NotificationAlertObserver {
    void update();
}

// ============================================================================
// 2. OBSERVABLE INTERFACE (The Subject Contract)
// ============================================================================
interface StocksObservable {
    void add(NotificationAlertObserver observer);

    void remove(NotificationAlertObserver observer);

    void notifySubscribers();

    void setStockCount(int newStockAdded);

    int getStockCount();
}

// ============================================================================
// 3. CONCRETE OBSERVABLE (Amazon Out-of-Stock Product)
// ============================================================================
class IphoneObservableImpl implements StocksObservable {
    private final List<NotificationAlertObserver> observerList = new ArrayList<>();
    private int stockCount = 0;

    @Override
    public void add(NotificationAlertObserver observer) {
        observerList.add(observer);
    }

    @Override
    public void remove(NotificationAlertObserver observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifySubscribers() {
        for (NotificationAlertObserver observer : observerList) {
            observer.update();
        }
    }

    @Override
    public void setStockCount(int newStockAdded) {
        // Business logic: Only trigger alerts when transitioning from out-of-stock
        if (this.stockCount == 0 && newStockAdded > 0) {
            this.stockCount += newStockAdded;
            notifySubscribers();
        } else {
            this.stockCount += newStockAdded;
        }
    }

    @Override
    public int getStockCount() {
        return this.stockCount;
    }
}

// ============================================================================
// 4. CONCRETE OBSERVERS (Subscribers / Notifiers)
// ============================================================================
class EmailAlertObserverImpl implements NotificationAlertObserver {
    private final String emailId;
    private final StocksObservable observable;

    public EmailAlertObserverImpl(String emailId, StocksObservable observable) {
        this.emailId = emailId;
        this.observable = observable;
    }

    @Override
    public void update() {
        sendMail(emailId, "Product is back in stock! Units available: " + observable.getStockCount());
    }

    private void sendMail(String emailId, String msg) {
        System.out.println("[EMAIL DISPATCHED] To: " + emailId + " | Body: " + msg);
    }
}

class MobileAlertObserverImpl implements NotificationAlertObserver {
    private final String userName;
    private final StocksObservable observable;

    public MobileAlertObserverImpl(String userName, StocksObservable observable) {
        this.userName = userName;
        this.observable = observable;
    }

    @Override
    public void update() {
        sendSms(userName, "Hurry! Product is in stock. Current count: " + observable.getStockCount());
    }

    private void sendSms(String userName, String msg) {
        System.out.println("[SMS DISPATCHED]   To: @" + userName + " | Body: " + msg);
    }
}

// ============================================================================
// 5. MAIN DEMO DRIVER (Matches File Name)
// ============================================================================
public class ObserverPatternDemo {

    public static void main(String[] args) {
        // 1. Create the Observable (The Subject)
        StocksObservable iphoneStock = new IphoneObservableImpl();

        // 2. Create Observers subscribing to this specific observable
        NotificationAlertObserver user1 = new EmailAlertObserverImpl("shiva@example.com", iphoneStock);
        NotificationAlertObserver user2 = new EmailAlertObserverImpl("john.doe@gmail.com", iphoneStock);
        NotificationAlertObserver user3 = new MobileAlertObserverImpl("dev_shiva", iphoneStock);

        // 3. Register Subscribers
        iphoneStock.add(user1);
        iphoneStock.add(user2);
        iphoneStock.add(user3);

        System.out.println("--- Scenario 1: Stock replenished from 0 -> 10 ---");
        iphoneStock.setStockCount(10); // All 3 observers get notified automatically

        System.out.println("\n--- Scenario 2: Unsubscribe John and add 5 more items ---");
        iphoneStock.remove(user2);
        iphoneStock.setStockCount(5); // Stock was not 0, so no bulk spam alerts fire

        System.out.println("\n--- Scenario 3: Sell out, then restock 20 units ---");
        iphoneStock.setStockCount(-15); // Empties inventory back to 0
        iphoneStock.setStockCount(20); // Crosses 0 threshold -> alerts fire for remaining subscribers
    }
}