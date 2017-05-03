package org.VV.SparkStreamer.KafkaConsumer

import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

object StreamConsumer {
  def main(args: Array[String]): Unit = {
    if (args.length < 4) {
      System.err.println("Usage: KafkaConsumer <zkQuorum> <group> <topics> <numThreads>")
      System.exit(1)
    }

    println("================System Paramertes===============")
    args.foreach { x => println(x) }
    println("######### WE made till here ########")

    val sparkConf = new SparkConf().setAppName("KafkaConsumer")
      .setMaster("local[2]").set("spark.executor.memory", "1g")

    val Array(zkQuorum, group, topics, numThreads) = args
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    ssc.checkpoint("checkpoint")

    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
    lines.print()

    ssc.start()
    ssc.awaitTermination()

  }
}
