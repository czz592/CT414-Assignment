
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;

public class ExamEngine implements ExamServer {

    private List<Assessment> assessments;
    private Map<Integer, String> map = new HashMap<>();

    public ExamEngine() {
        super();
        assessments = new ArrayList<Assessment>();
        // create hardcoded assessment for testing
        AssessmentImpl as1 = new AssessmentImpl("test", 123);
        assessments.add(as1);
        // hardcoded studentid and password for testing
        // store studentid and matching password in map
        map.put(123, "password");
        map.put(456, "password");
        map.put(789, "password");
    }

    // Return an access token that allows access to the server for some time period
    public int login(int studentid, String password)
            throws UnauthorizedAccess, RemoteException {

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

        List<String> assessmentSummary = new ArrayList<String>();

        // check token is valid
        // if not valid throw UnauthorizedAccess
        if (token != 1) {
            throw new UnauthorizedAccess("Invalid token");
        } else if (map.containsKey(studentid)) {
            throw new UnauthorizedAccess("Invalid studentid");
        } else {
            // for each assessment
            // add assessment.getInformation()
            // to a list of strings
            for (Assessment assessment : assessments) {
                assessmentSummary.add("Summary of available assessments:\n");
                assessmentSummary.add(assessment.getInformation());
            }
        }

        // return the list
        return assessmentSummary;
    }

    // Return an Assessment object associated with a particular course code
    public Assessment getAssessment(int token, int studentid, String courseCode)
            throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        // check token is valid
        // if not valid throw UnauthorizedAccess
        if (token != 1) {
            throw new UnauthorizedAccess("Invalid token");
        }

        /*
         * assessment ID is an attribute set by the server
         * to identify related student when an Assessment object
         * is downloaded by the client
         * getAssessment() is passed studentid, so when the method is called
         * a new Assessment should be created to return to client
         * with the studentid set by the server
         */

        // test if any assessment with courseCode exists in assessments
        // if not throw NoMatchingAssessment (moot because as1 is created with test)
        // else return assessment
        for (Assessment assessment : assessments) {
            if (assessment.getInformation().contains(courseCode)) {
                return assessment;
            }
        }
        throw new NoMatchingAssessment("No matching assessment found in list of assessments.\n");
    }

    // Submit a completed assessment
    public void submitAssessment(int token, int studentid, Assessment completed)
            throws UnauthorizedAccess, NoMatchingAssessment, RemoteException {
        // check token is valid
        // if not valid throw UnauthorizedAccess
        if (token != 1) {
            throw new UnauthorizedAccess("Invalid token");
        }

        // iterate through assessments to find assessment with matching studentid
        // if no matching id throw NoMatchingAssessment
        // else set assessment to completed
        int i = 0;
        for (Assessment assessment : assessments) {
            if (assessment.getAssociatedID() == completed.getAssociatedID()) {
                assessment = completed;
                // write assessment to .txt file
                // write to file using assessment.getInformation()
                System.out.println("ExamEngine - Writing assessment to file...");
                try {
                    // write assessment information
                    FileWriter fw = new FileWriter("assessment_" + i + ".txt");
                    fw.write(assessment.getInformation());
                    int j = 0;
                    for (Question q : assessment.getQuestions()) {
                        // write question information
                        fw.append(q.getQuestionDetail());
                        fw.append("Answer Options:");
                        for (String s : q.getAnswerOptions()) {
                            // write answer options
                            fw.append(s + "\n");
                        }
                        // write answer to question
                        fw.append("Answer: " + (assessment.getSelectedAnswer(j) + 1) + "\n");
                        j++;
                    }
                    fw.close();
                } catch (Exception e) {
                    System.out.println("Error writing to file - " + e.toString());
                }
                i++;
            } else {
                throw new NoMatchingAssessment("No matching assessment found in list of assessments.\n");
            }
        }
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
