# Summarised

## Steps

1. **Threading Strategy (Map Phase)**: Approach 3 of the program is modified to use a thread pool of variable size for parallel execution of map functions, instead of a thread per file.

2. **Threading Strategy (Reduce Phase)**: Further modification of Approach 3 to utilize a variable-sized thread pool for parallel execution of reduce functions, instead of a thread per word.

3. **Testing and Optimization**: Conduct tests with different thread pool sizes for both map and reduce operations. Compare performance results with different approaches to determine optimal thread pool sizes.

## Notes

- Submission details include packaging all source code and related documentation into a single ZIP file for submission via Canvas. Groups of up to two students are allowed for collaboration.

# Original

## Steps

1. The program should be modified so that it can be passed a variable length list of text files to be processed via the command line. Use at least 10 large text files for testing, each file at least 100KB in size. The same set of large text files should be used for tests or running the program, so that the results can be compared later, as the program is modified. 

2. Modify the program so that only individual words, separated by white space, are included in the input for the map phase. Words that include punctuation characters or other non-text symbols or numbers should be filtered and modified to only include the word itself. For example, something like the word "finished," should be modified and included in the input data structure as the word "finished".

3. Implement some accurate mechanism that outputs how long it takes, measured in milliseconds, to run the various parts of the program. This provides a measure of the baseline performance of each phase of the program with the set of large text files you are using for testing. Run a number of tests at this point to measure how long the different Approaches and phases of the program take to process the set of large text files. These results will be used later as a baseline to see if modifications to the threading strategy can improve the overall performance of the program.

4. Modify Approach 3 of the program to implement and test a different threading strategy for the map phase of the program. The sample program provided uses a thread per file for the map phase. Modify the program to instead use a thread pool of a variable size, to run the map functions in parallel, where the size or number of threads in the pool is specified as a runtime parameter.

5. Further modify Approach 3 of the program to implement and test a different threading strategy for the reduce phase of the program. The sample program provided uses a thread per individual word for the reduce phase. Modify the program to instead use a thread pool of a variable size, to run the reduce functions in parallel, where the size or number of threads in the pool is specified as a runtime parameter.

6. Run a number of tests that use different values for the size of the thread pools used for both the map and reduce operations, the size of map thread pool and the reduce thread pool should be individually configurable and you should try different values for each as part of your testing. Compare the results from using thread pools with the Approach 1, Approach 2 and the original Approach 3 results. What values work well and give the best performance for the set of large files that you used for testing purposes?

## Notes

- You can download large text files from the internet. There are also some free online PDF to text file converters available that will convert PDF files into text files, these can be useful if you have some large PDF files and want to convert them to text format for testing purposes.
- 

## Submission Details

submit copies of the:

- source code
- report document - describing how you tested the application, include screen shots showing evidence of the application running, also include test results and explanations of the results. 

Combine all the source code files and related documentation (PDF) into a single ZIP file for submission.
