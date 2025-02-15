package com.example;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class DocumentSimilarityMapper extends Mapper<LongWritable, Text, Text, Text> {
    
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // Get the input file name
        String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
        
        // Get the document content
        String content = value.toString().toLowerCase();
        
        // Create a set of words for the document
        Set<String> words = new HashSet<>();
        StringTokenizer tokenizer = new StringTokenizer(content);
        while (tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            // Keep all words as is, just convert to lowercase
            word = word.toLowerCase();
            if (!word.isEmpty()) {
                words.add(word);
            }
        }
        
        // Emit document ID and its word set
        context.write(new Text("DOC"), 
                     new Text(fileName + "|" + String.join(" ", words)));
    }
}