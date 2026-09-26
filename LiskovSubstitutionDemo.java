import java.util.*;


abstract class vehicle{
    
    public  abstract void wheels();

} 
    
    abstract class engveh extends vehicle{
     public abstract void engine();
    }


    class car extends engveh{
        @Override
        public void wheels()
        {
            System.out.println("It has 4 wheels");
        }

        public void engine()
        {
            System.out.println("It has engine");
        }
    }

    class bike extends engveh{
        @Override
        public void wheels()
        {
            System.out.println("It has 2 wheels");
        }

        public void engine()
        {
            System.out.println("It has engine");
        }

    }

    class bicycle extends vehicle{ 
        @Override
        public void wheels()
        {
            System.out.println("It has 2 wheels");
        }
    }
    
    public class LiskovSubstitutionDemo {
    
    
    public static void main(String[] args)
    {
        List<engveh> motorlist = new ArrayList<>();
        motorlist.add(new car());
        motorlist.add(new bike());

        List<vehicle> Allvehicle = new ArrayList<>();
        Allvehicle.add(new bicycle());

        for(vehicle v: Allvehicle){
            v.wheels();
        }
        
        for(engveh v: motorlist)
            {
                v.wheels();
                v.engine();
        }

    }
}
    

