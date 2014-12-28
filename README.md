BAClassify: Brodmann Area Classification
==========

<p> This project was created to solve the issue of Brodmann Area (BA) classification of anatomical labels from SPM aal in MNI space. It contains methods for adding to a database of BAs and associated cluster information (described in BAFeedback), and for BA predicition based on cluster information (see BAClassification). </p>

<h1> Background: </h1>
<p> When creating whole brain tables from outputs created using the SPM toolkit PickAtlas, neuroscience researchers are not provided with corresponding BA labels for anatomical labels, and thus are forced to locate and classify BA labels by viewing brain images.  This task is obviously tedious and time-consuming, detracting from the more important task of data analysis.  The methods contained in this project attempt to solve this issue and speed up the process. </p>

<h1> Installation and Dependencies: </h1>
<table border="1" style="width:100%">
  <tr>
    <td> New Users </td>
    <td> refer to https://help.github.com/articles/set-up-git/ for Github set up instructions </td>
  </tr>
  <tr>
    <td> Experienced Users </td>
    <td> HTTP: https://github.com/noahrubin/BAClassify.git <br> 
         SSH: git@github.com:noahrubin/BAClassify.git      </td>
  </tr>
  <tr>
  <td> Dependencies </td>
  <td> Apache POI API (see Apache_Lib) </td>
  </tr>
</table>

<h1> Usage </h1>
  <h3> BA Classification </h3> 
    <p> Not ready for stand alone use yet, but can use as method call: <br> 
      BAClassification.classify( database file path, ArrayList of clusters, number of comparisons to perform) </p>
  <h3> Feedback Control </h3> 
    <p> Ready for stand alone use. Extracts information from whole brain table and writes to database </p> <br> 
    <ol>
      <li> Open terminal and type "java -version"  Apache POI requires JDK 1.6 or later, update if needed </li>
      <li> Navigate to the src directory of BAClassify and type "javac BAFeedback.java" </li>
      <li> If no errors occur, the file has compiled correctly. If you encounter errors, follow error messages and trace the isssue </li>
      <li> Type "java BAFeedback databaseFilePath tableFilePath tableSheetNumber"  Please note: sheet number indexes from 0 </li>
    </ol>

<h1> Feedback and Questions/Concerns </h1>
Your feedback is welcome! For all questions, concerns, and comments please email me at nar62@cornell.edu
