# File-Type-Analyzer-PJ-in-Java
Java Project for Practicing using multiple feature (multi-thread, pattern checking pattern algorithms,..)

source file in task/src/analyzer

This a tool for determining file type. It's not like determining file type based on the extension of the file; the filename can be random. 
This program search for an occurrence of the specific pattern inside a single file.
And it it will check hundreds and thousands of files against a huge pattern set automatically to find out the type of the target file(s). 

Using multi-thread and algorithm (KMP and Rabin Karp) to speed up the pattern checking.

Example: 

java Main test_files patterns.db

Output:
doc_0.doc: MS Office Word 2003
doc_1.pptx: MS Office PowerPoint 2007+
doc_2.pdf: PDF document
file.pem: PEM certificate

where test_files is the directory that contain target-files
and pattern.db is the file containing many different patterns in priorities order
