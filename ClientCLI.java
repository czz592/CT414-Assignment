import java.rmi.*;

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
            // TODO - call methods on the server
            // and passing CLI args to the server

        } catch (Exception e) {
            System.out.println("Error in main - " + e.toString());
        }
    }
}
