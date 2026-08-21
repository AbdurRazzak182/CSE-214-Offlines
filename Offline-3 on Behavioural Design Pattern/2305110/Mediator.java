// mediator interface 
interface CentralResultProcessing{
    void request(Office office);
    void confirm(Office office);
    void issue(Office office);
}

// concrete mediator 
class CentralResultProcessingCoordinator implements CentralResultProcessing{
    private boolean studentRequest = false;
    private boolean departmentalConfirmation = false;
    private boolean examControllerIssue = false;
    private boolean dswIssue = false;

    public void request(Office office){
        if(studentRequest){
            office.notify("You alerady requested before and it is processing.");
        }
        else{
            office.notify("Your request is accepted.");
            studentRequest = true;
        }
    }
    public void confirm(Office office){
        if(studentRequest && !departmentalConfirmation){
            office.notify("Department confirmed as all academic requirements have been completed.");
            departmentalConfirmation = true;
        }
        else if(departmentalConfirmation){
            office.notify("Process is running as Department has confirmed request.");
        }
        else{
            office.notify("Something happened wrong!");
        }
    }
    public void issue(Office office){
        if(office instanceof ExamController){
            if(departmentalConfirmation && !examControllerIssue){
                office.notify("Orders are  issued.");
                examControllerIssue = true;
            } 
            else if(departmentalConfirmation && dswIssue){
                office.notify("Certificates and Transcripts are issued as completeing the all required steps. Student is said to collect his papers.");
            }
            else if(!departmentalConfirmation){
                office.notify("Department has not confirmed yet. Wait!");
            }
            else{
                office.notify("DSW has not been issued Testimonial yet. Wait!");
            }
        }
        else if(office instanceof DSW){
            if(departmentalConfirmation && examControllerIssue){
                office.notify("DSW issued Testimonial.");
                dswIssue = true;
            }
            else if(!departmentalConfirmation){
                office.notify("Department has not confirmed yet. Wait!");
            }
            else{
                office.notify("Exam Controller has not issued yet. Wait!");
            }
        }
        else{
            office.notify("Something happened wrong");
        }
    }
}

abstract class Office{
    protected CentralResultProcessing mediator;
    protected String officeTitle;
    Office(CentralResultProcessing crp,String officeTitle){
        this.mediator = crp;
        this.officeTitle = officeTitle;
    }
    abstract void response();
    abstract void notify(String message);
}

class Department extends Office{
    Department(CentralResultProcessing crp){
        super(crp,"Department Office");
    }
    public void response(){
        System.out.println("Department is checking student's academic requirments...");
        mediator.confirm(this); // 2. Department is confiming to the mediator
    }
    public void notify(String message){
        System.out.println("Coordinator command : "+message);
        System.out.println();
    }
}
class ExamController extends Office{
    private boolean dsw = false;
    ExamController(CentralResultProcessing crp){
        super(crp,"Exam Controller Office");
    }
    public void response(){
        if(!dsw){
            System.out.println("Exam Controller is issuing orders.");
            mediator.issue(this); //3.Exam controller is issuing for orders
            dsw = true;
        }
        else{
            System.out.println("Exam Controller is issuing certificates and transcripts.");
            mediator.issue(this); //5.Exam controller is issuing for orders
            dsw = false;
        }
    }
    public void notify(String message){
        System.out.println("Coordinator command : "+message);
        System.out.println();
    }
}
class DSW extends Office{
    DSW(CentralResultProcessing crp){
        super(crp,"DSW Office");
    }
    public void response(){
        System.out.println("DSW is issuing Testimonial.");
        mediator.issue(this); // 4. DSW 
    }
    public void notify(String message){
        System.out.println("Coordinator command : "+message);
        System.out.println();
    }
}
class Student extends Office{
    public String name;
    public int id;
    public String session;
    Student(CentralResultProcessing crp,String name,int id,String session){
        super(crp,"Student");
        this.name = name;
        this.id = id;
        this.session = session;
    }
    public void response(){
        System.out.println(name + "( "+id+" ) of session "+session + " is applying for academic credentials.");
        mediator.request(this);      // 1.student is making a request to the mediator            
    }
    public void notify(String message){
        System.out.println("CentralCoordinator command : "+message);
        System.out.println();
    }
}

public class Mediator {
    public static void main(String[] args) {
        CentralResultProcessing controller = new CentralResultProcessingCoordinator();
        Office student = new Student(controller,"Abdur Razzak",2305110,"2023-2024");
        Office cse = new Department(controller);
        Office meAnex = new ExamController(controller);
        Office dsw = new DSW(controller);

        // Actual flow
        // student.response();
        // cse.response();
        // meAnex.response();
        // dsw.response();
        // meAnex.response();

        // flaw testing 
        student.response();
        dsw.response();   // an attempt to publish result before deparmental confirmation
        cse.response();
        meAnex.response();   
        meAnex.response();  // early attemp to issue the certificate or transcript
        meAnex.response();  // final-result office order 
        dsw.response();    // issuance of the testimonial 
        meAnex.response();   // final issuance for certificate and transcript
    }
}
