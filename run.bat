@echo off
set map_thread=%1
set reduce_thread=%2
javac MapReduceFiles.java
java MapReduceFiles %map_thread% %reduce_thread% large_txts/*.txt > outputs/%map_thread%_%reduce_thread%.txt
