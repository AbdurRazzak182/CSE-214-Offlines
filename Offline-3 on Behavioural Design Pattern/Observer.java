// a common alert class for each type of disasters

import java.util.ArrayList;
import java.util.List;

abstract class Alert{
    private String title;
    private String category;
    private String afftectedLocation;
    private String severityLevel;
    private String safetyInstructions;

    public Alert(String title,String category,String aL,String sL,String sI){
        this.title = title;
        this.category = category;
        this.afftectedLocation = aL;
        this.severityLevel = sL;
        this.safetyInstructions = sI;
    }
    public void showAlert(){
        System.out.println("Title : "+this.title);
        System.out.println("Category : "+this.category);
        System.out.println("Affected Locations : "+this.afftectedLocation);
        System.out.println("Severity Level : "+this.severityLevel);
        System.out.println("Safety Instructions : "+this.safetyInstructions);
    }
}

class EarthQuake extends Alert{
    EarthQuake(String title,String category,String aL,String sL,String sI){
        super(title,category,aL,sL,sI);
    }
}

class Flood extends Alert{
    Flood(String title,String category,String aL,String sL,String sI){
        super(title,category,aL,sL,sI);
    }
}
class Fire extends Alert{
    Fire(String title,String category,String aL,String sL,String sI){
        super(title,category,aL,sL,sI);
    }
}


// interface for Citizenns 
interface Subscriber{
    void getAlert(Alert alert);
}

class Citizen implements Subscriber{
    private String citizenName;
    private int age;
    private String address;
    private double income;
    private int noOfFamilyMembers;
    private List<Alert>alerts = new ArrayList<>();

    Citizen(String name,int a,String adrs,double inc,int fm){
        citizenName = name;
        age = a;
        address = adrs;
        income = inc;
        noOfFamilyMembers = fm;
    }
    public void showCitizenDetails(){
        System.out.println("Name : "+citizenName);
        System.out.println("Age : "+age);
        System.out.println("Address : "+address);
        System.out.println("Income : "+income);
        System.out.println("No of family members : "+noOfFamilyMembers);
    }
    public void getAlert(Alert alert){
        alerts.add(alert);
        System.out.println("\n[Notification for " + citizenName + "]");
        alert.showAlert();
    }

}

class BDAlert{
    private List<Citizen>earthquakSubscribers = new ArrayList<>();
    private List<Citizen>floodSubscribers = new ArrayList<>();
    private List<Citizen>fireSubscribers = new ArrayList<>();
    private Alert alert;

    public void addEarthquakeSubscriber(Citizen citizen){
        earthquakSubscribers.add(citizen);
    }
    public void addFloodSubscriber(Citizen citizen){
        floodSubscribers.add(citizen);
    }
    public void addFireSubscriber(Citizen citizen){
        fireSubscribers.add(citizen);
    }
    public void removeEarthquakeSubscriber(Citizen citizen){
        earthquakSubscribers.remove(citizen);
    }
    public void removeFloodSubscriber(Citizen citizen){
        floodSubscribers.remove(citizen);
    }
    public void removeFireSubscriber(Citizen citizen){
        fireSubscribers.remove(citizen);
    }
    public void setAlert(Alert alert){
        this.alert = alert;
        notifyCitizens(alert);
    }
    public void notifyCitizens(Alert alert){
        if(alert instanceof EarthQuake){
            for(Citizen citizen:earthquakSubscribers){
                citizen.getAlert(alert);
            }
        }
        else if(alert instanceof Flood){
            for(Citizen citizen:floodSubscribers){
                citizen.getAlert(alert);
            }
        }
        else if(alert instanceof Fire){
            for(Citizen citizen:fireSubscribers){
                citizen.getAlert(alert);
            }
        }
        else{
            System.out.println("Something went wrong to alert subscribers!");
        }
    }

}

public class Observer{
    public static void main(String[] args) {
        BDAlert alertSystem = new BDAlert();
        Citizen rahim = new Citizen("Rahim", 35, "Dhaka", 50000.0, 4);
        Citizen karim = new Citizen("Karim", 28, "Sylhet", 40000.0, 3);
        Citizen fatema = new Citizen("Fatema", 45, "Chittagong", 60000.0, 5);

        //  Subscribe Citizens to specific disaster alerts
        // Rahim wants Earthquake and Fire alerts
        alertSystem.addEarthquakeSubscriber(rahim);
        alertSystem.addFireSubscriber(rahim);

        // Karim wants Earthquake and Flood alerts
        alertSystem.addEarthquakeSubscriber(karim);
        alertSystem.addFloodSubscriber(karim);

        // Fatema wants Flood and Fire alerts
        alertSystem.addFloodSubscriber(fatema);
        alertSystem.addFireSubscriber(fatema);

        //  Create Disaster Alerts
        EarthQuake eqAlert = new EarthQuake(
            "Major Earthquake Warning", 
            "Earthquake", 
            "Dhaka & Sylhet", 
            "High", 
            "Drop, cover, and hold on. Stay away from windows."
        );

        Flood floodAlert = new Flood(
            "Flash Flood Alert", 
            "Flood", 
            "Sylhet & Chittagong", 
            "Severe", 
            "Move to higher ground immediately. Do not walk through moving water."
        );

        Fire fireAlert = new Fire(
            "Industrial Fire Outbreak", 
            "Fire", 
            "Dhaka", 
            "Critical", 
            "Evacuate the building immediately using stairs. Do not use elevators."
        );

        // Test triggering the alerts
        System.out.println("--- Triggering Earthquake Alert ---");
        // Expecting 2 prints (Rahim and Karim)
        alertSystem.setAlert(eqAlert); 

        System.out.println("\n--- Triggering Flood Alert ---");
        // Expecting 2 prints (Karim and Fatema)
        alertSystem.setAlert(floodAlert);

        System.out.println("\n--- Triggering Fire Alert ---");
        // Expecting 2 prints (Rahim and Fatema)
        alertSystem.setAlert(fireAlert);

        // Test removing a subscriber
        System.out.println("\n--- Removing Karim from Earthquake Alerts and Re-triggering ---");
        alertSystem.removeEarthquakeSubscriber(karim);
        
        // Expecting 1 print (Only Rahim)
        alertSystem.setAlert(eqAlert);
    }
}