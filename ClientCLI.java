import java.rmi.*;
import java.util.List;

public class ClientCLI {

    private static int id;
    private static String password;
    private static String courseCode;
    private static int token;
    private static ExamServer engine;
    private static ClientCLI client;
    private static Assessment assessment;
    private static int answer;

    // constructor
    public ClientCLI() {
        super();
    }

    private int login() {
        // prompt for studentid and password
        // and pass to login method
        // get user input in command line
        System.out.printf("ID: %d\n", id);
        System.out.printf("Password: %s\n", password);
        System.out.printf("Course code: %s\n", courseCode);
        try {
            return engine.login(id, password);
        } catch (Exception e) {
            System.out.println("Error in login() - " + e.toString());
        }
        return -1;
    }

    private void printSummary() {
        // call getAvailableSummary method
        System.out.println("Printing available assessments: ");
        try {
            List<String> available = engine.getAvailableSummary(token, id);
            for (String s : available) {
                System.out.println(s);
            }
        } catch (Exception e) {
            System.out.println("Error in printSummary() - " + e.toString());
        }
    }

    private void getAssessment() {
        // prompt for assessment code
        // and pass to getAssessment method
        System.out.println("Enter assessment code: ");
        System.out.println("Course code: " + courseCode);
        try {
            assessment = engine.getAssessment(token, id, courseCode);
        } catch (Exception e) {
            System.out.println("Error in getAssessment() - " + e.toString());
        }
    }

    private void getQuestion() {
        // prompt for assessment answers
        // and pass to submitAssessment method
        System.out.println("Getting questions");
        List<Question> questions = assessment.getQuestions();
        for (Question q : questions) {
            System.out.println("Question number: ");
            System.out.println(q.getQuestionNumber());
            System.out.println("Question detail: ");
            System.out.println(q.getQuestionDetail());
            System.out.println("Answer options: ");
            String options[] = q.getAnswerOptions();
            for (String s : options) {
                System.out.println(s);
            }
            try {
                System.out.println("Select answer: ");
                assessment.selectAnswer(q.getQuestionNumber(), answer);
                System.out.println("Answer selected: " + answer);
            } catch (Exception e) {
                System.out.println("Error in getQuestion() - " + e.toString());
            }
        }
    }

    private void submit() {
        // download .txt file of completed assessment?
        try {
            System.out.println("Submitting assessment: ");
            engine.submitAssessment(token, id, assessment);
        } catch (Exception e) {
            System.out.println("Error in submit() - " + e.toString());
        }
    }

    // Command Line Interface for the client
    // that calls the server methods
    // on an AssessmentImpl object
    public static void main(String[] args) {
        // Name lookup on the server
        // to get the ExamEngine object
        // and call its methods
        client = new ClientCLI();
        try {
            engine = (ExamServer) Naming.lookup("//localhost/ExamServer");

            id = Integer.parseInt(args[0]);
            password = args[1];
            courseCode = args[2];
            answer = Integer.parseInt(args[3]);

            // call login method
            token = client.login();

            // token is passed to other methods when invoked
            // print token for testing
            System.out.println("Token: " + token);
            System.out.println("\n----------------------\n");

            // call getAvailableSummary method
            client.printSummary();
            System.out.println("\n----------------------\n");

            // call getAssessment method
            client.getAssessment();
            System.out.println("\n----------------------\n");

            // call getQuestion method
            client.getQuestion();
            System.out.println("\n----------------------\n");

            // call submitAssessment method
            client.submit();
            System.out.println("\n----------------------\n");
            // send assessment.txt file of the completed assessment to the server

        } catch (Exception e) {
            System.out.println("Error in main - " + e.toString());
        }
    }
}
