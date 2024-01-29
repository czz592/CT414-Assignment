import java.rmi.*;
import java.util.List;

public class ClientCLI {
    // Command Line Interface for the client
    // that calls the server methods
    // on an AssessmentImpl object
    public static void main(String[] args) {
        // Name lookup on the server
        // to get the ExamEngine object
        // and call its methods
        try {
            ExamEngine engine = (ExamEngine) Naming.lookup("//localhost/ExamServer");
            // call server remote methods
            // and pass CLI args to server

            // prompt for studentid and password
            // and pass to login method
            System.out.println("Enter studentid: ");
            int id = Integer.parseInt(args[0]);
            System.out.println("Enter password: ");
            String password = args[1];
            int token = engine.login(id, password);
            
            // token is passed to other methods when invoked
            // print token for testing
            System.out.println("Token: " + token);
            System.out.println("\n----------------------\n");

            // call getAvailableSummary method
            System.out.println("Printing available assessments: ");
            List<String> available = engine.getAvailableSummary(token, id);
            for (String s : available) {
                System.out.println(s);
            }
            System.out.println("\n----------------------\n");

            // prompt for assessment code
            // and pass to getAssessment method
            // TODO: Implement
            System.out.println("Enter assessment code: ");
            String courseCode = args[2];
            Assessment assessment = engine.getAssessment(token, id, courseCode);
            System.out.println("\n----------------------\n");

            // prompt for assessment answers
            // and pass to submitAssessment method
            // TODO: Implement
            questions = assessment.getQuestions();
            for (Question q : questions) {
                System.out.println(q.getQuestionNumber());
                System.out.println(q.getQuestionDetail());
                System.out.println(q.getAnswerOptions());
                System.out.println("Enter answer number: ");
                int answer = Integer.parseInt(args[3]);
                assessment.selectAnswer(q, answer);
                System.out.println("\n----------------------\n");
            }
            
            // call submitAssessment method
            // download .txt file of completed assessment?
            // TODO: Implement and check if file needs to be "downloaded"
            engine.submitAssessment(token, id, assessment);
            System.out.println("\n----------------------\n");
            // send assessment.txt file of the completed assessment to the server
            


        } catch (Exception e) {
            System.out.println("Error in main - " + e.toString());
        }
    }
}
