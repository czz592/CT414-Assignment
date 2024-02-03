
# CT414 Assignment 1

*Leo Chui (), Aoife Mulligan (20307646)*

## Testing the Application

The start_client.bat file contains hardcoded 'answers' to the prompts which appear in the CLI. For example, 123 password test 1, represent the student ID, password, course code, and selected answer. 

These get read in by the ClientCLI object like so:

``` Java
id = Integer.parseInt(args[0]);
password = args[1];
courseCode = args[2];
answer = Integer.parseInt(args[3]);
```

This allows for quick input, fuzz and boundary testing. 

#### ExamEngine Tests

**login()** Method:

The login() method takes an Integer studentId and a String password. Since the student will have already singned up (i.e., it is not a sign up method), we only need to check if the student ID and password are in the 'database' and that they match.

``` Java
if (map.containsKey(studentid) && map.containsValue(password))
// rest of code
throw new UnauthorizedAccess("Invalid studentid or password");
```

Here is an output from a successful login:

![](Pasted%20image%2020240202193739.png)

Here is an output from a login where the password was incorrect:

![](Pasted%20image%2020240202193626.png)

Here is an output where the user had an incorrect studentID:

![](Pasted%20image%2020240202193812.png)

**getAvailableSummary()** Method:

The getAvailableSummary() method takes a token and a studentis as parameters. For this method, we first check if the token is valid. For this basic implementation, we hardcoded the token to be 1. (For a more secure implementation, I would probably try to have a dict of studentID and token, and generate a new token each time the student logs in.) Because the token is simply 1, we check if the token is not 1, and if true we throw an UnauthorisedAccess error.

Similarly, we check if the studentid is in the 'database' and if not, throw an UnauthorisedAccess error.

Success:

![](Pasted%20image%2020240202193931.png)


Fail:

![](Pasted%20image%2020240202193900.png)



**getAssessment()** Method:

Checks if the token is valid again.

Checks if there is an assessment object which matches the courseCode that has been input. Checks all assessments, and if it does not find a match it throws a NoMatchingAssessment error.

Here is an output where the courseCode was in the database:

![](Pasted%20image%2020240202194001.png)


Here is an output from where the courseCode was not in the database:

![](Pasted%20image%2020240202194015.png)


**submitAssessment()** Method:

Takes a token, a studentid, and a completed Assessment object.

Checks if the token is valid again, throws UnauthorisedAccess if not.

Checks if assessment is completed, if not, throws NoMatchingAssessment.

If assessment is completed, tries to write assessment information to a new file.

**main()** Method:

Tries to start server. If fails, catches the exception.

Success:
![](Pasted%20image%2020240202194443.png)

#### AssessmentImpl Tests

**getQuestion()** Method:

Takes a question number as a parameter, and tries to get the questions from that question number. If it can't, throws an InvalidQuestionNumber exception

**selectAnswer()** Method:

Takes a question number and an option number as parameters.

Checks if the question number is valid, if not throw InvalidQuestionNumber.

Checks if the option number is valid, if not throw InvalidOptionNumber.

**getSelectedAnswer()** Method:

Take a question number as a parameter.

Checks if nothing has been selected, and return zero.

Otherwise checks if the question number is valid, if not throws InvalidQuestionNumber.

Otherwise, returns selected answer.

#### ClientCLI Tests

**login()** Method:

Tries to login, if fails throws exception.

**printSummary()** Method:

Tries to print all available assessments, if can't, throws exception.

**getAssessment()** Method:

Tries to get assessments from ExamEngine, if can't, throws exception.

**getQuestion()** Method:

Tries to get questions from AssessmentImpl and select answer. If can't throws exception.

**submit()** Method:
Tries to call the submitAssessment method, if can't, throws exception.

**main()** Method:

Tries to connect to server. If can't, throws exception.

