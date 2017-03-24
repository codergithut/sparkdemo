package com.demo.spark.sparkdemo;

import java.util.Arrays;
import java.util.Date;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

public class App 
{
    public static void main( String[] args )
    {
//        System.out.println( "Hello World!" );
//        String logFile = "/Users/huipeizhu/spark-2.1.0-bin-hadoop2.7/test.txt"; 
//        //SparkConf conf =new SparkConf().setAppName("Spark Application in Java").setMaster("local[3]");
//        SparkConf conf = new SparkConf().setAppName("test1").setMaster("spark://hadoop1:7077");
//        JavaSparkContext sc = new JavaSparkContext(conf);
//        JavaRDD<String> logData = sc.textFile(logFile).cache();
//
//        long numAs = logData.filter(new Function<String, Boolean>() {
//            public Boolean call(String s) { return s.contains("2"); }
//        }).count();
//
//        long numBs = logData.filter(new Function<String, Boolean>() {
//            public Boolean call(String s) { return s.contains("41"); }
//        }).count();
//
//        System.out.println("Lines with a: " + numAs +",lines with b: " + numBs);
//        System.out.println(sc);
        
        
        SparkConf conf = new SparkConf().setAppName("test1").setMaster("spark://hadoop1:7077");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> textFile = sc.textFile("hdfs://hadoop1:8020/spark/zhp");
        JavaPairRDD<String, Integer> counts = textFile
            .flatMap(s -> Arrays.asList(s.split(" ")).iterator())
            .mapToPair(word -> new Tuple2<>(word, 1))
            .reduceByKey((a, b) -> a + b);
        counts.saveAsTextFile("hdfs://hadoop1:8020/spark/"+new Date().getTime());
    }
}
