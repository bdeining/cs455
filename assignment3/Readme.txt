Question 9 :

    On a per state basis, analyze what percentage of the population lives in a rural environment vs
    an urban one.  This demographic shows which states have a predominantly rural population, and may
    show correlations between other demographics on the state as a whole (religious, political, housing values).
    This data was plotted in plot.ly and can be found here : https://plot.ly/~bdeininger/6/rural-population/.
    From analysis, the state with the highest urban population was Pennsylvania.

To Build:
    `ant` (jar located in dist/assignment4.jar)

$HADOOP_HOME/bin/hadoop jar assignment4.jar <job> <input_dir> <output_dir>
$HADOOP_HOME/bin/hdfs dfs -get <path_to_part> <output_dir>

The package structure for this assignment was broken into 5 separate packages : Mappers, Reducers, Jobs,
Combiners, and Records.  There is an additional util package that has helper methods for the Mappers to
reduce code duplication.  For each question in the assignment, a separate job was made (including a
mapper, reducer, record and a combiner where appropriate.  Each record's to string method generates output
in tsv format.  The motivation behind this was to provide an analyst with the ability to load this data
into an excel spreadsheet for visual and graphical analysis.

    Q1:  Tenure* files
    Q2:  Marriage* files
    Q3:  Hispanic* files
    Q4:  HousingType* files
    Q5:  HousingMedian* files
    Q6:  RentMedian* files
    Q7:  AverageRoom* files
    Q8:  ElderlyPopulation* files
    Q9:  RuralPopulation* files
