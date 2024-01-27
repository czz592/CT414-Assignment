public class QuestionImpl implements Question {

    // attributes
    private int questionNumber;
    private String questionDetail = null;
    private String[] answerOptions = null;

    // Constructor
    public QuestionImpl() {
        questionNumber = 1;
        questionDetail = "What is the capital of Ireland?";
        answerOptions = new String[] { "Dublin", "Cork", "Galway", "Belfast" };
    }

    @Override
    public int getQuestionNumber() {
        return questionNumber;
    }

    @Override
    public String getQuestionDetail() {
        return questionDetail;
    }

    @Override
    public String[] getAnswerOptions() {
        return answerOptions;
    }

}
