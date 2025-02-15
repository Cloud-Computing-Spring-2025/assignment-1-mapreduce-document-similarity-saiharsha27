# Document Similarity MapReduce

## Approach and Implementation

### Overview
This project implements a MapReduce-based solution to compute the similarity between text documents using the Jaccard similarity coefficient. The application is structured using the Hadoop framework, leveraging a Mapper to process documents and a Reducer to calculate similarity scores.

### Mapper: `DocumentSimilarityMapper.java`
- Reads the input text file and extracts words.
- Converts all words to lowercase and removes duplicates.
- Emits a key-value pair where the key is a constant ("DOC"), and the value contains the document name and its set of words.

### Reducer: `DocumentSimilarityReducer.java`
- Receives all document word sets under the key "DOC".
- Parses the document names and their corresponding word sets.
- Computes Jaccard similarity between each pair of documents.
- Outputs the similarity percentage for each document pair if the similarity exceeds 20%.

## Running the Project

### Prerequisites
- Docker
- Apache Hadoop (preconfigured within Docker container)
- Maven

### Step-by-Step Instructions

1. Start Hadoop using Docker:
   ```sh
   docker compose up -d
   ```

2. Build the project using Maven:
   ```sh
   mvn install
   ```

3. Copy the generated JAR file to the Hadoop container:
   ```sh
   docker cp target/DocumentSimilarity-0.0.1-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
   ```

4. Copy the input data to the container:
   ```sh
   docker cp input_data resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
   ```

5. Access the Hadoop container:
   ```sh
   docker exec -it resourcemanager /bin/bash
   ```

6. Set up HDFS directories:
   ```sh
   hadoop fs -mkdir -p /input/dataset
   hdfs dfs -put /opt/hadoop-3.2.1/share/hadoop/mapreduce/input_data/* /input/
   ```

7. Run the Hadoop job:
   ```sh
   hadoop jar /opt/hadoop-3.2.1/share/hadoop/mapreduce/DocumentSimilarity-0.0.1-SNAPSHOT.jar com.example.controller.DocumentSimilarityDriver /input /output
   ```

8. View the results:
   ```sh
   hdfs dfs -cat /output/part-r-00000
   ```

9. Retrieve the output from the container:
   ```sh
   hdfs dfs -get /output /opt/hadoop-3.2.1/share/hadoop/mapreduce/
   exit
   docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output/ output/
   ```

## Challenges Faced and Solutions

### 1. Handling File Names in Mapper
**Issue:** Extracting filenames correctly in a distributed environment.  
**Solution:** Used `FileSplit` to retrieve the file name of the input document.

### 2. Memory Optimization in Reducer
**Issue:** Large input files led to memory overhead.  
**Solution:** Used `HashSet` to store unique words and optimized pairwise comparison loops.

### 3. Debugging MapReduce Errors
**Issue:** Debugging was difficult due to limited logging.
**Solution:** Implemented logging statements and tested with smaller datasets before full execution.

---
This README serves as both the project report and user guide for running the Document Similarity MapReduce program.

