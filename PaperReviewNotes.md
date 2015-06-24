Paper Review Notes
==================

###Paper 1###
- Title: *Bugs as Deviant Behavior: A General Approach to Inferring Errors in System Code*
- Authors: Dawson Engler et. al.
- Description: This paper is I believe Dawson Engler's and companies first paper in there exploration into the concept of MUST and MAY beliefs. In the paper he proposes a technique for inferring specification and generating error messages to the user. This is the underlying concept that lead to the Coverity tool. He walks through what a MUST belief which is when there is no doubt that a programmer has the belief. He gives the example of the null pointer dereference for this. He then walks through what a MAY belief is which is suggested belief from inferring certain patterns in the code but in turn could just be a coincidence. This is exactly what the software testing project covered.
- Things to include in literature review:
  - What may beliefs are and why they are different from must beliefs.
  - How statistical analysis is used to reduce the number of false positives.
  - How MUST beliefs and MAY beliefs can be used in the static analysis of Clafer.

###Paper 2###
- Title: *Checking System Rules Using System-Specific, Programmer-Written Compiler Extension*
- Author: Dawson Engler et. al.
- Description: This paper precedes the work that Engler did in Paper 1 with MAY and MUST beliefs. In this paper Engler and company propose a method and results for how adding checkers into the compiler to look for various specifications can help find bugs. These specifications, unlike Paper 1 are not inferred but rather given as patterns to the compiler checkers.
- Things to include in literature review:
  - Why this approach is different from infering the specs given in Paper 1
  - How good this approach is at finding bugs and why it does well
  - How this technique can be used in the static analysis of Clafer.

###Paper 3###
- Title: *Finding Bugs in Easy*
- Author: David Hovemeyer et. al.
- Description: This paper introduces FindBugs which looks for bug patterns which are code idioms that are likely to result in an error. It then goes into detail about some of the bug patterns it has and how they are implemented. They also show some results for running their tool on real systems and show how some patterns produce false warnings.
- Things to include in literature review:
  - How FingBugs detectors work
  - Some of the results they have for their patterns
  - The comparisons between the different ways to find bugs

###Paper 4###
- Title: *Using Static Analysis to Find Bugs*
- Author: Nathaniel Ayewah et. al.
- Description: FindBugs was started from an observation that developed into an experiment which found how many errors are made in production software that could be found with trivial analysis. To find certain issues such as dereferencing null pointers they use more sophisticated methods such as intraprocedural dataflow analysis. The driving goal of FindBugs is to find bugs in code that are relatively simple to find and leaves the harder bugs to find to more sophisticated tools. FindBugs does not use pattern matching languages but rather detectors written in Java. Also, FindBugs groups the bugs into different categories and assigns a priority to them.
- Things to include in literature review:
  - Good background information of static analysis.
  - The approach that FindBugs takes to find bugs and some of the results
  - How the FingBugs approach differs from papers 1 and 2.
  - Some of the techniques used to suppress warnings.
  - How the FindBugs approach can work in the static analysis Clafer

###Paper 5###
- Title: **
- Author:
- Description:
- Things to include in literature review:

###Misc. Paper Ideas###
What if we did some sort of comparison of FindBugs and Engler's work? Do the projects overlap at all?
