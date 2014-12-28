BAClassify
==========

Brodmann Area Classification

Purpose: <br>
This project was created to solve the issue of Brodmann Area (BA) classification of anatomical labels from SPM aal in MNI space.  It contains methods for adding to a database of BAs and associated cluster information (described in BAFeedback), and for BA predicition based on cluster information (see BAClassification). <br>

Background: <br>
When creating whole brain tables from outputs created using the SPM toolkit PickAtlas, neuroscience researchers are not provided with corresponding BA labels for anatomical labels, and thus are forced to locate and classify BA labels by viewing brain images.  This task is obviously tedious and time-consuming, detracting from the more important task of data analysis.  The methods contained in this project attempt to solve this issue and speed up the process. <br>

Feedback Usage: <br>
The BAFeedback class is provided for updating the database (provided as 
