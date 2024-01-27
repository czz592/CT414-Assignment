import java.util.Date;
import java.util.List;

public class AssessmentImpl implements Assessment {

    // Constructor
    public AssessmentImpl() {
        super();
    }

    // Return information about the assessment
    @Override
    public String getInformation() {
        String info = "This is a test assessment";
        info += "\nThis assessment is for testing purposes only";
        return info;
    }

    // Return the final date / time for submission of completed assessment
    @Override
    public Date getClosingDate() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getClosingDate'");
    }

    // Return a list of all questions and anser options
    @Override
    public List<Question> getQuestions() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQuestions'");
    }

    // Return one question only with answer options
    @Override
    public Question getQuestion(int questionNumber) throws InvalidQuestionNumber {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQuestion'");
    }

    // Answer a particular question
    @Override
    public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectAnswer'");
    }

    // Return selected answer or zero if none selected yet
    @Override
    public int getSelectedAnswer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSelectedAnswer'");
    }

    // Return studentid associated with this assessment object
    // This will be preset on the server before object is downloaded
    @Override
    public int getAssociatedID() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAssociatedID'");
    }

}
