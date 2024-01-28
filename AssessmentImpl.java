import java.util.Date;
import java.util.List;

public class AssessmentImpl implements Assessment {

    // attributes
    private String courseCode = null;
    private int assessmentID;
    private Date closingDate = null;
    private List<Question> questions = null;
    private List<Integer> selectedAnswers = null;
    private int associatedID;

    // Constructor
    public AssessmentImpl(String courseCode, int associatedID) {
        super();
        this.courseCode = courseCode;
        assessmentID = 1;
        questions.add(new QuestionImpl());
        closingDate = new Date();
        this.associatedID = associatedID;
    }

    // Return information about the assessment
    @Override
    public String getInformation() {
        String str = "Course code: " + courseCode + "\n";
        str += "Assessment ID: " + assessmentID + "\n";
        str += "Number of questions: " + questions.size() + "\n";
        str += "Closing date: " + closingDate + "\n";
        return str;
    }

    // Return the final date / time for submission of completed assessment
    @Override
    public Date getClosingDate() {
        return closingDate;
    }

    // Return a list of all questions and anser options
    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    // Return one question only with answer options
    @Override
    public Question getQuestion(int questionNumber) throws InvalidQuestionNumber {
        try {
            return questions.get(questionNumber);
        } catch (Exception e) {
            throw new InvalidQuestionNumber();
        }
    }

    // Answer a particular question
    @Override
    public void selectAnswer(int questionNumber, int optionNumber) throws InvalidQuestionNumber, InvalidOptionNumber {
        // check questionNumber is valid
        // if not valid throw InvalidQuestionNumber
        if (questionNumber < 0 || questionNumber > questions.size()) {
            throw new InvalidQuestionNumber();
        }
        // get Question
        Question question = questions.get(questionNumber);
        // check optionNumber is valid
        // if not valid throw InvalidOptionNumber
        if (optionNumber < 0 || optionNumber > question.getAnswerOptions().length) {
            throw new InvalidOptionNumber();
        }
        // set selectedAnswer
        selectedAnswers.set(questionNumber, optionNumber);
    }

    // Return selected answer or zero if none selected yet
    @Override
    public int getSelectedAnswer() {
        // if nothing is selected return 0
        if (selectedAnswers.isEmpty()) {
            return 0;
        }
        // else return selected answer of question
        // assuming only one question for testing
        return selectedAnswers.get(0);
    }

    // Return studentid associated with this assessment object
    // This will be preset on the server before object is downloaded
    @Override
    public int getAssociatedID() {
        return associatedID;
    }

}
