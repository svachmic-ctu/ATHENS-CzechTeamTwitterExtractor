# CzechUberTeamTwitterExtractorAthens
Data analysis on top of real Twitter data that can be found [here](https://partage.mines-telecom.fr/public.php?service=files&t=c72a61684bbbdf76dd0b4e98bb9c42d6).

### Problem description

Tweets' texts are extracted from the dataset along with their tweeters and hastags. Graph from hashtag connections should then be constructed and analyzed.

The goal is to find around twenty dense subgraphs for each of the four subfolders (NewYork, ParisJanuary, ParisFebruary, Oscars).
Then analyze those subgraphs, see what can be observed and report the findings.

#### 1. Data Cleaning and Processing

Read data into memory, filter out unnecessary information, save unique hastags and build the graph.

#### 2. Finding Dense Subgraphs

Implement greedy algorithm for finding dense subgraphs.

#### 3. Data Analysis

Analyze the data by various techniques to obtain dense subgraphs.

### Problem solution

All Paris Files log (generated 4 subgraphs):

<pre><code>
Files processed
37753 unique tweets
Graph constructed

Will remove: [republique, marcherepublicaine, liberté, jesuischarlie, charliehebdo]
[jesuischarlie, paris, liberté, republique, charliehebdo, république, noussommescharlie, france, marcherepublicaine, charlie]

Will remove: [fontainebleau, chateau, europe, france, paris]
[love, paris, eiffeltower, notredame, france, travel, europe, fontainebleau, architecture, chateau]

Will remove: [laics, african, american, palestine, israel]
[ue, pfw, palestine, african, israel, hautecouture, juifs, american, laics, musulmans]

Will remove: [hautecouture, couture, pfw, vscocam, fashion]
[pfw, love, instafood, foodporn, yummy, vscocam, hautecouture, couture, food, fashion]
</code></pre>

### How to use the program

TBD
