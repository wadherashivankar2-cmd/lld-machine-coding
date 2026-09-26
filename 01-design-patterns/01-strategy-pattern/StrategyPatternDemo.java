// ============================================================================
// 1. STRATEGY INTERFACE (The Contract)
// ============================================================================
interface DriveStrategy {
    void drive();
}

// ============================================================================
// 2. CONCRETE STRATEGIES (Where the code actually lives)
// ============================================================================
class NormalDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving normally: Standard fuel efficiency and smooth speed.");
    }
}

class SpecialDriveStrategy implements DriveStrategy {
    @Override
    public void drive() {
        System.out.println("Driving in Sport mode: Aggressive power and high torque.");
    }
}

// ============================================================================
// 3. CONTEXT BASE CLASS (Vehicle HAS-A DriveStrategy)
// ============================================================================
abstract class Vehicle {
    private DriveStrategy driveStrategy;

    // Injected via constructor
    public Vehicle(DriveStrategy driveStrategy) {
        this.driveStrategy = driveStrategy;
    }

    // Delegates the execution to the plugged-in strategy
    public void drive() {
        this.driveStrategy.drive();
    }
}

// ============================================================================
// 4. CONCRETE VEHICLES (Plugging in the strategies)
// ============================================================================
class PassengerCar extends Vehicle {
    public PassengerCar() {
        super(new NormalDriveStrategy());
    }
}

class SportsCar extends Vehicle {
    public SportsCar() {
        super(new SpecialDriveStrategy());
    }
}

class OffRoadCar extends Vehicle {
    public OffRoadCar() {
        // Reuses SpecialDriveStrategy without duplicating any code
        super(new SpecialDriveStrategy());
    }
}

// ============================================================================
// 5. MAIN DEMO CLASS (Matches file name)
// ============================================================================
public class StrategyPatternDemo {
    public static void main(String[] args) {
        Vehicle cityCar = new PassengerCar();
        Vehicle ferrari = new SportsCar();
        Vehicle jeep = new OffRoadCar();

        System.out.print("PassengerCar: ");
        cityCar.drive();

        System.out.print("SportsCar:    ");
        ferrari.drive();

        System.out.print("OffRoadCar:   ");
        jeep.drive();
    }
}