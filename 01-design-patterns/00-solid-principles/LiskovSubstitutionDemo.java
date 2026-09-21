import java.util.ArrayList;
import java.util.List;

// ============================================================================
// PART 1: THE WRONG WAY (VIOLATING LISKOV SUBSTITUTION PRINCIPLE)
// ============================================================================

// The parent makes a promise that ALL vehicles have engines
class BadVehicle {
    public Integer getNumberOfWheels() {
        return 2;
    }

    // A method assuming all vehicles have an engine status
    public Boolean hasEngine() {
        return true;
    }
}

class BadCar extends BadVehicle {
    @Override
    public Integer getNumberOfWheels() {
        return 4;
    }

    @Override
    public Boolean hasEngine() {
        return true;
    }
}

class BadBicycle extends BadVehicle {
    @Override
    public Integer getNumberOfWheels() {
        return 2;
    }

    // LSP VIOLATION: Bicycle is forced to implement hasEngine().
    // Returning null or throwing an exception breaks client code expecting a real
    // Boolean.
    @Override
    public Boolean hasEngine() {
        return null;
    }
}

// ============================================================================
// PART 2: THE CORRECT WAY (ADHERING TO LISKOV SUBSTITUTION PRINCIPLE)
// ============================================================================

// Base class: contains ONLY properties guaranteed to exist on ALL vehicles
abstract class Vehicle {
    public abstract int getNumberOfWheels();
}

// Intermediate class: only vehicles with actual engines extend this
abstract class EngineVehicle extends Vehicle {
    public abstract boolean hasEngine();
}

// Car has wheels AND an engine
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

// MotorCycle has wheels AND an engine
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

// Bicycle has wheels, but is NEVER forced to implement hasEngine()
class Bicycle extends Vehicle {
    @Override
    public int getNumberOfWheels() {
        return 2;
    }
}

// ============================================================================
// MAIN RUNNER: SEEING THE DIFFERENCE IN ACTION
// ============================================================================
public class LiskovSubstitutionDemo {

    public static void main(String[] args) {

        System.out.println("--- 1. THE BAD DESIGN (LSP VIOLATION) ---");

        // Creating a list of BadVehicle objects
        List<BadVehicle> badVehicleList = new ArrayList<>();
        badVehicleList.add(new BadCar());
        badVehicleList.add(new BadBicycle()); // Sneaking in a bicycle

        for (BadVehicle v : badVehicleList) {
            try {
                // When v is BadBicycle, hasEngine() returns null.
                // Calling .toString() on null immediately causes a NullPointerException!
                System.out.println(v.getClass().getSimpleName() + " engine status: " + v.hasEngine().toString());
            } catch (NullPointerException e) {
                System.out.println("CRASH! Subclass " + v.getClass().getSimpleName()
                        + " broke the client contract because it returned null for an engine!");
            }
        }

        System.out.println("\n--- 2. THE GOOD DESIGN (LSP SATISFIED) ---");

        // 1. General list: holds ALL vehicles safely (Car, MotorCycle, Bicycle)
        List<Vehicle> allVehicles = new ArrayList<>();
        allVehicles.add(new Car());
        allVehicles.add(new MotorCycle());
        allVehicles.add(new Bicycle());

        for (Vehicle v : allVehicles) {
            // Every vehicle is guaranteed to honor getNumberOfWheels()
            System.out.println(v.getClass().getSimpleName() + " has " + v.getNumberOfWheels() + " wheels.");
        }

        // 2. Engine-only list: Bicycle is physically barred by compiler from entering
        // here
        List<EngineVehicle> motorizedVehicles = new ArrayList<>();
        motorizedVehicles.add(new Car());
        motorizedVehicles.add(new MotorCycle());
        // motorizedVehicles.add(new Bicycle()); // <-- COMPILER ERROR! Cannot add
        // Bicycle. Safe design.

        System.out.println("\n--- 3. MOTORIZED VEHICLES ONLY ---");
        for (EngineVehicle ev : motorizedVehicles) {
            System.out.println(ev.getClass().getSimpleName() + " hasEngine: " + ev.hasEngine());
        }
    }
}