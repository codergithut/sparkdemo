
package com.demo.spark.sparkdemo;

import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

public class SparkStreamingPollDataFromFlume {
    public static void main(String[] args) throws Exception {
        //SparkConf conf = new SparkConf().setMaster("spark://192.168.4.163:7077").setAppName("NetworkWordCount");
        SparkConf conf = new SparkConf().setMaster("spark://hadoop1:7077").setAppName("NetworkWordCount_hadoop");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(5));
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("hadoop1", 9999);
        JavaDStream<String> words = lines.flatMap(
                                                  new FlatMapFunction<String, String>() {
                                                    @Override public Iterator<String> call(String x) {
                                                      return Arrays.asList(x.split(" ")).iterator();
                                                    }
                                                  });
     // Count each word in each batch
        JavaPairDStream<String, Integer> pairs = words.mapToPair(
          new PairFunction<String, String, Integer>() {
            @Override public Tuple2<String, Integer> call(String s) {
              return new Tuple2<>(s, 1);
            }
          });
        JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(
          new Function2<Integer, Integer, Integer>() {
            @Override public Integer call(Integer i1, Integer i2) {
              return i1 + i2;
            }
          });

        // Print the first ten elements of each RDD generated in this DStream to the console
        wordCounts.print();
        jssc.start();              // Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
       
    }
}
