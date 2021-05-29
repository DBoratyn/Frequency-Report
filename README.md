# Frequency Reports
### Java Academy Technical Task

Using Spring version: 2.5.0

This is a springframework MVC project that will count the frequency of letters from the given keyword within a sentence.
It has an endpoint which will be used from the index.html which will have a form to create a GET request to the endpoint and open the result in the browser.  
On startup, it will read from seperate text files for the keyword and sentence then create a report in output.txt.
<br />  
It will display in the following format:  
{(<em>letter(s) from the keyword</em>), <em> number of letters from the word</em>} = <em> frequency </em> (<em> number of letters found in the word /  number of matching letters found in the sentence </em>) 
