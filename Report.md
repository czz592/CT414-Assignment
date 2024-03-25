
<div align="center">
<h1> CT421 Assignment 2 </h1>
<br>
<h2>Leo Chui (20343266), Aoife Mulligan (20307646)</h2>
<br>
<br>
<h3><a href="https://www.github.com/czz592/ct414-assignment/">GitHub Repo</a>
</div>

<div class="page-break" style="page-break-before: always;"></div>

# Proof of Running

A batch script has been written to aid in the testing of this assignment, and below is a sample output after running the script. The script is included in the ZIP submission.

![alt text](run_proof.png)

All results are included in [Appendices](#appendices).

# Testing Approach

Testing included running the application with different pool sizes for both map and reduce phases. 

Below figure demonstrates the batch script being executed, with all outputs appended to respective output logs.

![[test_approach.png]]

# Results

The expectations for the results are that higher thread count decreases the duration taken, and vice versa, until thread count reaches 1 per file. We can see that is indeed the case when looking at the results. 

When comparing the last approach's results with previous approaches using results obtained from testing, we can see that all runs of approach 4 yielded better results than the other approaches, with the exception of 1 thread for both pools.

Results differ, even between the same approaches, but generally this is the case.

In terms of best performance, it might be beneficial to have at least half the amount of threads as there are files. The best performance out of the entire testing process is yielded by 15 threads for both phases, however the difference over 1 thread per file is not significant enough, thus can be attributed to simple deviations between runs. Therefore, we believe it is reasonable to say that best performance on average can be yielded by having 1 thread per file.

<div class="page-break" style="page-break-before: always;"></div>

# Appendices

![[1_1]]

![[3_3]]

![[5_5]]

![[5_10]]

![[10_5]]

![[10_10]]

![[15_15]]

![[50_50]]
