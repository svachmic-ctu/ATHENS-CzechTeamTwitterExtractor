# ATHENS: CzechTeamTwitterExtractor

This is a final project for class **Mining Massive Datasets** that was part of 🇪🇺 [ATHENS exchange program](http://www.athensprogramme.com/) (Fall 2015).

## 🎓 Problem description

Analyze Twitter data based on hashtag usage. Look for interesting information and patterns in hashtag co-occurrences. Build a graph representing hashtag clusters and analyze it. See what can be observed and report the findings.

### 1. Data Cleaning and Processing 🗃

The input data is a JSON Twitter dump using the [streaming API](https://dev.twitter.com/streaming/overview). This data is usually quite noisy for several reasons. One of the reasons is the presence of spam (users or robots repeatedly using trending hashtags to attract attention). One way to remediate this issue to some extent is to remove the copies of the same tweet. For our purposes, we say that two tweets are identical if they contain exactly the same set of hashtags. As we are analyzing mainly hashtags, we will also remove the tweet's text.

Example:

```
Tweet 1: "Terrorists at #CharlieHebdo #terrorists"
Tweet 2: "Attack at #ChalieHebdo #terrorists"
Tweet 3: "Terrorist attack at #CharlieHebdo #terrorists #Paris"
```

After the filtering we have:

```
Tweet 1: "#CharlieHebdo #terrorists"
Tweet 3: "#ChalieHebdo #terrorists #Paris"
```

Once we cleaned the data, we build the graph of co-occurrences. Each hashtag is represented as a node and co-occurrence is represented as a weighted undirected edge between two nodes. Weight represents the number of co-occurrences.

### 2. Finding Dense Subgraphs 🔎

In order to find dense subgraphs, one has to first define, what is a density:

```
density = (2・∑ edge_weights) / (|V|・|V - 1|)
```

This could possibly produce very large graphs that might not me absolutely relevant. Therefore, the algorithm searches for subgraphs that have 2-10 nodes.

The most important information that is extracted after each subgraph is generated is:

1. density
2. number of distinct users who contributed to the creation of the subgraph
3. number of distinct tweets from which the subgraph emerged

To calculate the number of distinct users who contributed to the creation of a subgraph, the program keeps track of all the users that used a particular hashtag somewhere in their tweets. That information is stored in RAM for each individual hashtag in the dataset. Then it just needs to retrieve the sets for all the hashtags contained in the subgraph and make an intersection.

The number of distinct tweets, that contributed to the creation of a subgraph, is calculated by retrieving all the tweets that contain any of the hashtags appearing in the subgraph and summing up the number of the duplicates of all those tweets.

The big question was which vertices to remove after a dense graph has been generated.

#### 1. The naive approach

After a dense graph has been generated, the algorithm simply removed all of the nodes that occurred in the subgraph. This approach was very fast (removed the most significant nodes after each iteration), but also produced very bad results after first two iterations.

#### 2. Weighted connectivity approach

Much more interesting results are generated when looking at the betweenness of all other nodes from the initial graph and the dense subgraph. It is possible to identify which nodes are connected strongly but to fewer nodes by sorting them by: 

```
edge_weight_sum / edge_count
```

for every node. 

The implementation of this solution always cuts the array in half and takes the half away. 

Other approach would be looking at the slope of these numbers. If it is linear, take all of them out, otherwise find the increasing slope and take everything out that was in the saddle.

### 3. Data Analysis 💡

Analyze the data by various techniques to obtain dense subgraphs and fine interesting trends.

The results that were turned in can be found in the release - solutions folder (file **analysis.md**).

## ⚙️ How to run the program

The program receives Twitter dump (formatted in JSON) on the input. The release comes with sample data, compressed in order to keep the size bearable. It is compressed using 7z archiver set on the highest level, so expect the dataset to be rather large when extracted (details in the tree structure below).

The released code is also available as a JAR file, to make it super easy to run. Simply download all the resources in the release section of the repository and store them in the same folder. Make sure to fully unzip each dataset you want to use - the code handles raw text files, not archives.

```
|-- CzechTeamTwitterExtractor
|     |-- CzechTeamTwitterExtractor-1.0.0.jar
|     |-- datasets
|           |-- NewYorkOneWeek
|                 |-- 4 files (23-26/02/2015 geotagged in New York - 897MB total)
|           |-- Oscars
|                 |-- 4 files (23-26/02/2015 with hashtag #Oscars2015 - 2.04GB total)
|           |-- ParisSearchFeb
|                 |-- 28 files (January 2015 geotagged in Paris - 2.59GB total)
|           |-- ParisSearchJan
|                 |-- 31 files (February 2015 geotagged in Paris - 3.09GB total)
```

The program accepts three command types for the input:

#### 1. Single file 📄

Accepts and processes a single file.

```
java -jar CzechTeamTwitterExtractor-1.0.0.jar "file" "NewYorkOneWeek/NewYork-2015-2-23.txt"
```

#### 2. Single folder 📗

Accepts and processes a single folder.

```
java -jar CzechTeamTwitterExtractor-1.0.0.jar "dir" "NewYorkOneWeek"
```

#### 3. Multiple folders 📚

Accepts and processes multiple fodlers (as many as needed).

```
java -jar CzechTeamTwitterExtractor-1.0.0.jar "dirs" "NewYorkOneWeek" "Oscars"
```

### Sample output

```
[newyorkcity, usa, newyork, jobs, brooklyn, manhattan, nyc, tweetmyjobs, ny, job]
Density: 281.1111111111111
Users: 14473
Tweets: 47528
```

---

If you want to run the code yourself or make changes and run it as a JAR file, simply call:

```
mvn clean compile assembly:single
```

## 🔖 License

CzechTeamTwitterExtractor is released under the MIT license. [See LICENSE](https://github.com/svachmic/ATHENS-CzechTeamTwitterExtractor/blob/master/LICENSE) for details.






