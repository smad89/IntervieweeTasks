# Data Integration, Task 1

## Scenario:

A customer has an online, cloud document editing program. Their software records all of the changes that have happened to online documents. They would like us to analyse the changes metadata to identify user behaviours, and have given us a small sample file containing around 10,000 lines of JSON objects representing metadata.

Your task is to write a Java 1.8 program, built with Maven, to read and parse the JSON file, produce a filtered CSV file for our behaviour analytics team  to analyze, as well as aggregate and print out statistics at the end.

Feel free to use any open source, public libraries to help you process the metadata. We suggest Jackson for JSON, Commons CSV for CSV, and Joda or Java DateTime for working with timestamps.

Your program should be runnable from a command prompt, should take two arguments, one for a JSON file to read, and one for a CSV file to write, and after completing, should print out the statistics to the console.

Your program should not be tied to any specific operating system. Try to keep the solution purely Java. You can deviate from this, but be prepared to justify.

Feel free to branch off of this GitHub repo to do your work. Email us when you feel that you are done, and we'll review.

**The JSON Metadata** section will outline what you should expect from the metadata file we’ve provided you with.

**The CSV File** section will cover the format we expect outputted.

**The Metrics Output** section will detail what we expected printed out to the console after the program has completed.

**Additional Assumptions You Can Make** section lists some overall things to keep in mind as you work on this task.

## The JSON Metadata:

The metadata file has been included with this task. Each line is a single JSON object with the following key/values:

|Key|Data Type|Description|Examples|
|---|---|---|---|
|eventId|Long|Unique ID for this metadata object.|43985498|
|user|String|The user performing the activity. Either “*@customer.com”, “SYSTEM”, or “ADMIN”.|jsmith@customer.com, SYSTEM, ADMIN
|ipAddr|String|The IP address of the user.|10.10.12.25|
|file|String|Fully qualified, unix style path name for the file being acted on.|/data/onlineDocs/2015/Q4/expenses-jan.doc|
|activity|String|The activity that the user is performing on the file. All values are below this chart.|viewedDoc|
|timestamp|String|Date in the format “MM/DD/YY HH:MM:SS” followed by either “am” or “pm”.|11/04/2015 01:15:37PM|
|timeOffset|String|Current timezone offset in the format “-##:##’. Field may not be present. If missing, assume UTC timezone.|-05:00, -08:00, +08:30|

Activity can be one of:
- createdDoc
- deletedDoc
- viewedDoc
- addedText
- changedText
- deletedText
- hashed
- replicated
- archived
- restored

Sample:
```
{
    "eventId": 43985498,
    "user": "jsmith@customer.com",
    "ipAddr": "10.10.12.35",
    "file": "/data/onlineDocs/2015/Q4/expenses-Jan.doc",
    "activity": "viewedDoc",
    "timestamp": "11/04/2015 1:15:37pm",
    "timeOffset": "-05:00"
}
```
**Note:** The sample is written on multiple lines for readability, but will be on a single line with the given metadata file.

## The CSV File ##

The output CSV file should be comma delimited, fields should be quoted if necessary, and formatted to this schema;

`TIMESTAMP,ACTION,USER,FOLDER,FILENAME,IP`

|Column|Description|Examples|
|---|---|---|---|
|Timestamp|ISO 8601 compliant String, with milliseconds and timezone.|2015-11-04T13:15:37.000-05:00, 2015-11-05T14:45:34.000Z|
|Action|Our interpretation of the activities, one of “ADD”, “REMOVE” or “ACCESSED”. Mapping below.|ACCESSED|
|User|The user performing the action, without “@customer.com”|jsmith|
|Folder|The path to the file.|/data/onlineDocs/2015/Q4/|
|File Name|The file in above folder.|expenses-Jan.doc|
|IP|The IP address related to the activity.|10.10.12.35|

Action Mappings:

|CSV Action|Metadata Activity|
|---|---|
|ADD|createdDoc, addedText, changedText|
|REMOVE|deletedDoc, deletedText, archived|
|ACCESSED|viewDoc|

Any valid metadata objects that fit at least one of the following criteria, should be dropped, and not included in the CSV file results.
- Any metadata objects with activity that is not mapped above.
- Duplicate metadata objects, indicated by the event ID.
  - You only have to check the eventId. You do not have to compare the entire event.

## The Metrics Output ##

You should output a single JSON object (preferably ‘pretty’ formatted) that has the following statistics and structure.

- How many lines were read
- How many were dropped 
- Counts for why a line was dropped
- How many unique users 
- How many unique files
- The time range of the data set
- Counts of each action encountered

Sample output:
```
{
    "linesRead":10000,
    "droppedEventsCounts": 150
    "droppedEvents": {
        "No action mapping": 134,
        "Duplicates": 16
    },
    "uniqueUsers": 23,
    "uniqueFiles": 256,
    "startDate": "2015-11-04T13:15:37.000-05:00",
    "endDate": "2016-01-15T06:54:23.000-05:00",
    "actions": {
        "ADD": 5550,
        "REMOVE": 2025,
        "ACCESSED": 2275
    }
}
```
**Notes:**

Other than “lines read”, and “event dropped”, dropped messages should not affect the other statistics.

The sample above does not contain the values we expect for the given data set, There may not be 23 users, or 256 files.

## Additional Assumptions You Can Make ##

- Each line will always be valid JSON
- Key:values in the metadata objects will always be present unless **The JSON Metadata** indicates otherwise.
- Value type (Int, String) will always match the type listed in **The JSON Metadata** section.
- If you encounter a metadata object you can't parse for reason not covered in **The CSV File**, do not include it in the CSV output.
  - Print out an error, with a simple explanation
