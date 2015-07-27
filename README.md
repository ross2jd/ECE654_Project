# ECE654_Project

This project is for the University of Waterloo ECE654 course project. The goal of this project was to apply well known static analysis techniques to a modeling language called Clafer to aid users in writing correct models.

# Download and Installation

## Requirements:
- JDK 8+
- maven 3+
- [Clafer Compiler](https://github.com/gsdlab/clafer) v0.3.10.
  - Required for compiling Clafer files (`.cfr`) into the Clafer Choco Javascript format (`.js`), so that they can be run using the tool.

## Download & building from sources
The project can be built by doing the following:

1. Clone the repository: `git clone https://github.com/ross2jd/ECE654_Project.git` 
2. Traverse to the directory: `cd ECE654_Project/Implementation` 
3. Build the project using Maven: `$ mvn clean install` 

# Usage
The .jar file that is outputed is located in the ECE654_Project/target/ directory. To run the program on a clafer file do the following:
```
$ clafer -m choco <clafer file name>.cfr
$ java -jar <jar file name>.jar <clafer file name>.js
```
