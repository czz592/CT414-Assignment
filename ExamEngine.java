
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamEngine implements ExamServer {


    public ExamEngine() {
        super();
    }

    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password)
            throws UnauthorizedAccess, RemoteException {
        // hardcoded studentid and password for testing
        // store studentid and matching password in map
        Map<Integer, String> map = new HashMap<>();
        map.put(123, "password");
        map.put(456, "password");
        map.put(789, "password");
        // if studentid and password match
        // return token
        // else return UnauthorizedAccess
        int token;
        if (map.containsKey(studentid) && map.containsValue(password)) {
            token = 1; // 1 for testing purposes
        } else {
            throw new UnauthorizedAccess("Invalid studentid or password");
        }
        return token;
    }

    // Return a summary list of Assessments currently available for this studentid
    public List<String> getAvailableSummary(int token, int studentid)
            throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        // create hardcoded assessment for testing
        List<AssessmentImpl> assessments = new ArrayList<AssessmentImpl>();
        AssessmentImpl as1 = new AssessmentImpl();
        assessments.add(as1);

        // for each assessment
        // add assessment.getInformation()
        // to a list
        List<String> assessmentSummary = new ArrayList<String>();
        for (AssessmentImpl assessment : assessments) {
            assessmentSummary.add("Summary of available assessments:\n");
            assessmentSummary.add(assessment.getInformation());
        }

        // return the list
        return assessmentSummary;
    }

    // TODO
    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode)
            throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // does courseCode have a list?
        // if studentId in courseCode return coursecode.Assessment
        // else return NoMatchingAssessment ?

        return null;
    }

    // TODO
    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed)
            throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {

        // If assessment.getClosingDate() > currdate
        // return "You can no longer submit this assessment!"
        // else ??
    }

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            String name = "ExamServer";
            ExamServer engine = new ExamEngine();
            ExamServer stub = (ExamServer) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("ExamEngine bound");
        } catch (Exception e) {
            System.err.println("ExamEngine exception:");
            e.printStackTrace();
        }
    }
}
