import java.util.*;

// ============================================================
//  No shared Behaviour. Each device is its own island.
//  Upgraded functionalities are booleans and fields crammed into each class.
//  Everything "works" but every new feature touches everything.
// ============================================================

interface SmartDevice{
    void activate();
    void deactivate();
    double getPowerUsage();
    String getStatus();
    List<SmartDevice> getComponents();
    Class<?> getBaseType();
}

class SmartLight implements SmartDevice{
    boolean on = false;
   
    

    public void activate(){
        this.on = true;

    }
    public void deactivate(){
        this.on = false;
    }
    public double getPowerUsage(){
        double p = on ? 10.0 : 0.0;
        return p;

    }
    public String getStatus(){
        String s = "Light: " + (on ? "ON" : "OFF");
        return s;

    }
   
    public List<SmartDevice> getComponents(){
        return Collections.emptyList();
    }
    public Class<?>getBaseType(){
        return SmartLight.class;
    }
}



class SmartThermostat implements SmartDevice{
    boolean on = false;
  
    
    public void activate() {
        this.on = true;
    }

    public void deactivate() {
        this.on = false;

    }

    public double getPowerUsage() {
        double p = on ? 150.0 : 0.0;
        return p;

    }

    public String getStatus() {
        String s = "Thermostat: " + (on ? "ON" : "OFF");
        return s;

    }
    
    public List<SmartDevice> getComponents(){
        return Collections.emptyList();
    }
    public Class<?>getBaseType(){
        return SmartThermostat.class;
    }
}

class SmartSpeaker implements SmartDevice{
    boolean on = false;


    public void activate() {
        this.on = true;
    }

    public void deactivate() {
        this.on = false;

    }

    public double getPowerUsage() {
        double p = on ? 5.0 : 0.0;
        return p;

    }

    public String getStatus() {
        String s = "Speaker: " + (on ? "Playing" : "Idle");
        return s;

    }
    
    public List<SmartDevice> getComponents(){
        return Collections.emptyList();
    }
    public Class<?>getBaseType(){
        return SmartSpeaker.class;
    }
}

// Rooms are basically a list of devices. But here, rooms can't just hold a list of "devices" — there's no shared type.
// So it holds three separate lists. Adding a fourth device type means
// editing Room, every helper method, and every demo.



class Room implements SmartDevice{
    String name;

    List<SmartDevice>components = new ArrayList<>();

    Room(String name) {
         this.name = name; 
    }
    void addComponent(SmartDevice smartComponent){
        components.add(smartComponent);
    }

    public void activate() {
        for(SmartDevice sc:components){
                sc.activate();
        }

    }

    public void deactivate() {
        for(SmartDevice sc:components){
            sc.deactivate();
        }
    }

    public double getPowerUsage() {
        double total = 0;
        for(SmartDevice sc:components){
                 total += sc.getPowerUsage();
        }
        return total;
    }
    
    public String getStatus() {
        
        StringBuilder sb = new StringBuilder("[" + name + "]");
        for(SmartDevice sc:components){
            sb.append(sc.getStatus());
        }
        return sb.toString();   
    }

   
    public void addDevice(SmartDevice sc){
        addComponent(sc);
    }
    public List<SmartDevice> getComponents(){
        return components;
    }
    public Class<?>getBaseType(){
        return Room.class;
    }
    
}

// Home is basically Room's logic copy-pasted with "rooms" instead of "devices"
class Home implements SmartDevice{
    String name;
    List<SmartDevice> components = new ArrayList<>();

    Home(String name) {
         this.name = name; 
    }
    
    void addComponent(SmartDevice smartComponent){
        components.add(smartComponent);
    }
    
    public void activate() {
        for(SmartDevice sc:components){
            sc.activate();
        }
    }

    public void deactivate() {
        for(SmartDevice sc:components){
            sc.deactivate();
        }  
    }

    public double getPowerUsage() {
        double total = 0;
        for(SmartDevice sc:components){
            total += sc.getPowerUsage();
        }
        return total;
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder("=== " + name + " ===");
        for(SmartDevice sc:components){
            sb.append(sc.getStatus());
        }
        return sb.toString();
    }
    
    public void addRoom(SmartDevice sc){
        addComponent(sc);
    }
    public List<SmartDevice> getComponents(){
        return components;
    }
    public Class<?>getBaseType(){
        return Home.class;
    }
    
}
abstract class ComponentDecorator implements SmartDevice{
    protected SmartDevice wrappedComponent;
    ComponentDecorator(SmartDevice wrappedComponent){
        this.wrappedComponent = wrappedComponent;
    }
    public void activate(){
        wrappedComponent.activate();
    }
    
    public void deactivate(){
        wrappedComponent.deactivate();
    }

    public double getPowerUsage(){
        return wrappedComponent.getPowerUsage();
    }
    public String getStatus(){
        return wrappedComponent.getStatus();
    }
    public List<SmartDevice> getComponents(){
        return wrappedComponent.getComponents();
    }
    public Class<?>getBaseType(){
        return wrappedComponent.getBaseType();
    }

}


class AccessRestricted extends ComponentDecorator{
    private int pin;
    boolean locked = false;
    AccessRestricted(SmartDevice s,int pin){
        super(s);
        this.pin = pin;
        this.locked = true;
    }
    
    public boolean unlock(int pin){
        if(this.pin == pin){
            this.locked = false; 
            return true;      
        }
        return false;
    }
    public void activate(){
        if(!locked){
            wrappedComponent.activate();
        }
    }
    public void deactivate(){
        if(!locked){
            wrappedComponent.deactivate();
        }
    }
    public String getStatus(){
        String s = wrappedComponent.getStatus();
        if(locked){
            s += " [LOCKED] ";
        }
        return s;
        
    }
    
    public double getPowerUsage(){
        return wrappedComponent.getPowerUsage();
    }

}
class TimerControlled extends ComponentDecorator {
    private final int shutoffDuration;  
    private boolean isActive = false;    
    private Timer timer;

    TimerControlled(SmartDevice s, int shutoffTimer) {
        super(s);
        this.shutoffDuration = shutoffTimer;
    }

    
    public void activate() {
        wrappedComponent.activate();
        isActive = true;
        restartTimer();
    }

    public void deactivate() {
        isActive = false;
        wrappedComponent.deactivate();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean getActiveStatus(){
        return isActive;
    }
    private void restartTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                deactivate();
            }
        }, shutoffDuration * 1000L);
    }

    public String getStatus() {
        String s = wrappedComponent.getStatus();
        if (!isActive) {
            s += " [Deactivated]";
        } else {
            s += " [auto-off in " + shutoffDuration + "s]";
        }
        return s;
    }


    public double getPowerUsage() {
        return wrappedComponent.getPowerUsage();
    }
    public void simulateTimerExpiry(){
        if(isActive){
            deactivate();
            System.out.println("Auto off in + " + shutoffDuration);
        }
        else{
            System.out.println("Deactivated");
        }
    }
}

class PowerThrottled extends ComponentDecorator{
    private int powerCap;
    PowerThrottled(SmartDevice smartComponent,int tp){
        super(smartComponent);
        this.powerCap = tp;
    }
    public void activate(){
        wrappedComponent.activate();
    }
    public double getPowerUsage(){
        return wrappedComponent.getPowerUsage() > powerCap ? powerCap : wrappedComponent.getPowerUsage();
    }
   
    public String getStatus(){
        return wrappedComponent.getStatus() + " [throttled to " + powerCap + "W]";
    }
}

class EcoMode extends ComponentDecorator{
    private double powerBudget;
    EcoMode(SmartDevice smartComponent,double pb){
        super(smartComponent);
        this.powerBudget = pb;
    }
    public void activate() {
        super.activate(); 

        if (wrappedComponent.getPowerUsage() > powerBudget) {
            List<SmartDevice> children = getComponents();
            for (int i = children.size() - 1; i >= 0 && wrappedComponent.getPowerUsage() > powerBudget; i--) {
                children.get(i).deactivate();
            }
        }
    }
    public void deactivate(){
        wrappedComponent.deactivate();
    }
    public double getPowerUsage(){
        double total_power = wrappedComponent.getPowerUsage();
        return powerBudget > total_power ? total_power : powerBudget;
    }
   
    public String getStatus() {
        return "[ECO: " + powerBudget + "W budget]\n" + super.getStatus();
    }
}

class GuestMode extends ComponentDecorator {
    private Set<Class<?>> allowed;

    public GuestMode(SmartDevice wrapped, Set<Class<?>> allowed) {
        super(wrapped);
        this.allowed = allowed;
    }

    
    public void activate() {
        for(SmartDevice sd:wrappedComponent.getComponents()){
            if(allowed.contains(sd.getBaseType())){
                sd.activate();
            }
        }
    }
    public double getPowerUsage(){
        double total = 0.0;
        for(SmartDevice device:wrappedComponent.getComponents()){
            if(allowed.contains(device.getBaseType())){
                total += device.getPowerUsage();
            }
        }
        return total;
    }
    public String getStatus() {
        StringBuilder sb = new StringBuilder("[GUEST MODE]\n");
        boolean hasRestricted = false;
        for (SmartDevice device : wrappedComponent.getComponents()) {
            if (!allowed.contains(device.getBaseType())) {
                hasRestricted = true;
                break;
            }
        }
        if (hasRestricted) {
            sb.append("[guest-restricted]\n");
        }
        sb.append(super.getStatus());
        return sb.toString();
    }
    
    
}

// ============================================================
//  MAIN — Some demos, but built on the mess above
// ============================================================

public class SmartHomeSpaghettiDemo {

    public static void main(String[] args) {
        demoA();
        demoB();
        demoC();
        demoD();
        demoE();
        demoF();
    }

    static void header(String title) {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("  " + title);
        System.out.println("=".repeat(55));
    }

    // DEMO A: Home overview
    static void demoA() {
        header("DEMO A: Home Overview");

        Room living = new Room("Living Room");
        living.addComponent(new SmartLight());
        living.addComponent(new SmartSpeaker());

        Room bedroom = new Room("Bedroom");
        bedroom.addComponent(new SmartLight());
        bedroom.addComponent(new SmartThermostat());

        Home home = new Home("My Home");
        home.addComponent(living);
        home.addComponent(bedroom);

        System.out.println("Before activation:");
        System.out.println(home.getStatus());
        System.out.println("Power: " + home.getPowerUsage() + "W");

        home.activate();
        System.out.println("\nAfter activation:");
        System.out.println(home.getStatus());
        System.out.println("Power: " + home.getPowerUsage() + "W");
    }

    // DEMO B: Stacking device-level "upgrades"
    static void demoB() {
        header("DEMO B: AccessRestricted + TimerControlled");

        SmartLight light= new SmartLight();
        
        AccessRestricted restrictedLight = new AccessRestricted(light, 1234);

        
        TimerControlled tcLight = new TimerControlled(restrictedLight, 60);


        System.out.println("Step 1 — Activate while locked:");
        tcLight.activate();
        System.out.println("  Status: " + tcLight.getStatus());
        System.out.println("  Power:  " + tcLight.getPowerUsage() + "W");

        System.out.println("\nStep 2 — Wrong PIN:");
        // Unlock logic is here in main, not encapsulated anywhere


        if (restrictedLight.unlock(0000)) { 
             System.out.println("    >> Unlock SUCCESS"); 
        }
        else { System.out.println("    >> Unlock FAILED"); }
        System.out.println("  Status: " + tcLight.getStatus());

        System.out.println("\nStep 3 — Correct PIN, activate:");
        if (restrictedLight.unlock(1234)) {  System.out.println("    >> Unlock SUCCESS"); }
        else { System.out.println("    >> Unlock FAILED"); }
        light.activate();
        System.out.println("  Status: " + restrictedLight.getStatus());
        System.out.println("  Power:  " + restrictedLight.getPowerUsage() + "W");

        System.out.println("\nStep 4 — Timer expires:");
        // Simulating timer — manually calling deactivate because there's
        // no timer object, no dedicated class, just a flag
        if (!tcLight.getActiveStatus()) {
            System.out.println("    >> Timer expired — auto-deactivating.");
        }
        System.out.println("  Status: " + light.getStatus());
        System.out.println("  Power:  " + light.getPowerUsage() + "W");
    }

    // DEMO C: EcoMode
    static void demoC() {
        header("DEMO C: EcoMode (budget = 100W)");

        Room office = new Room("Office");
        office.addComponent(new SmartLight());
        office.addComponent(new SmartLight());
        office.addComponent(new SmartThermostat());
        // office.ecoMode = true;
        // office.ecoBudget = 100;
        EcoMode ecoOffice = new EcoMode(office,100);

        System.out.println("Activating with EcoMode:");
        office.activate();
        System.out.println("\n" + ecoOffice.getStatus());
        System.out.println("Power: " + ecoOffice.getPowerUsage() + "W");
    }

    // DEMO D: Order matters
    static void demoD() {
        header("DEMO D: Order Matters");

        // Setup 1: Throttled thermostat
        Room room1 = new Room("Lab-1");
        room1.addComponent(new SmartLight());
        room1.addComponent(new SmartLight());
        SmartThermostat t1 = new SmartThermostat();

        room1.addComponent(t1);
    
        PowerThrottled roomPt1 = new PowerThrottled(t1, 80);
        EcoMode ecoRoom1 = new EcoMode(roomPt1,100);

        System.out.println("Setup 1: Throttled thermostat (80W) + EcoMode(100W)");
        ecoRoom1.activate();
        System.out.println(ecoRoom1.getStatus());
        System.out.println("Power: " + ecoRoom1.getPowerUsage() + "W");

        // Setup 2: Raw thermostat
        Room room2 = new Room("Lab-2");
        room2.addComponent(new SmartLight());
        room2.addComponent(new SmartLight());
        room2.addComponent(new SmartThermostat());
    
        EcoMode ecoRoom2 = new EcoMode(room2,100);

        System.out.println("\nSetup 2: Raw thermostat (150W) + EcoMode(100W)");
        ecoRoom2.activate();
        System.out.println(room2.getStatus());
        System.out.println("Power: " + ecoRoom2.getPowerUsage() + "W");
    }

    // DEMO E: GuestMode
    static void demoE() {
 
        Room guest = new Room("Guest Room");
        guest.addComponent(new SmartSpeaker());
 
        SmartThermostat thermostat = new SmartThermostat();
        AccessRestricted lockedThermo = new AccessRestricted(thermostat, 9999);
        guest.addComponent(lockedThermo);
 
        SmartLight light = new SmartLight();
        TimerControlled timedLight = new TimerControlled(light, 120);
        guest.addComponent(timedLight);
 
        Set<Class<?>> allowed = new HashSet<>();
        allowed.add(SmartLight.class);
        allowed.add(SmartSpeaker.class);
        GuestMode guestRoom = new GuestMode(guest, allowed);
 
        System.out.println("Activating GuestMode room:");
        guestRoom.activate();
        System.out.println("\n" + guestRoom.getStatus());
        System.out.println("Guest-visible power: " + guestRoom.getPowerUsage() + "W");
    }

    // DEMO F: "Enhance an entire room"
    static void demoF() {
        header("DEMO F: prepareForNight wraps a Room");
 
        Room kids = new Room("Kids Room");
        kids.addComponent(new SmartLight());
        kids.addComponent(new SmartSpeaker());
        kids.addComponent(new SmartThermostat());
 
        // Keep a reference to the AccessRestricted layer so we can unlock it,
        // and wrap that in TimerControlled — same composition demoB used on
        // a single light, just applied to a Room this time.
        SmartDevice nightKids = prepareForNight(kids);
 
        System.out.println("Step 1 — Activate while locked (nothing happens):");
        nightKids.activate();
        System.out.println("  Status:\n" + nightKids.getStatus());
        System.out.println("  Power: " + nightKids.getPowerUsage() + "W");
 
        System.out.println("\nStep 2 — Unlock and activate:");
        if (nightKids instanceof AccessRestricted) {
            AccessRestricted restrictedKids = (AccessRestricted) nightKids;
            if (restrictedKids.unlock(0)) {
                System.out.println("    >> Unlock SUCCESS");
            } else {
                System.out.println("    >> Unlock FAILED");
            }
        }
        nightKids.activate();
        System.out.println("  Status:\n" + nightKids.getStatus());
        System.out.println("  Power: " + nightKids.getPowerUsage() + "W");
 
        System.out.println("\nStep 3 — Timer expires (entire room shuts off):");
        nightKids.deactivate(); // simulate the scheduled timer callback firing
        System.out.println("  Status:\n" + nightKids.getStatus());
        System.out.println("  Power: " + nightKids.getPowerUsage() + "W");
 
        System.out.println("\nStep 4 — Add to Home:");
        Home home = new Home("Night Home");
        home.addComponent(nightKids);
        System.out.println("  Home power: " + home.getPowerUsage() + "W");
 
        
    }

    static SmartDevice prepareForNight(SmartDevice entity) {
        return new TimerControlled(new AccessRestricted(entity, 0), 5);
    }
}
