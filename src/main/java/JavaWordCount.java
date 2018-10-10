import org.apache.hadoop.conf.Configuration;
import scala.Tuple2;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public final class JavaWordCount {
    private static final Pattern SPACE = Pattern.compile(" ");

    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .config("spark.master", "local")
                .getOrCreate();

        Configuration hadoopConf = spark.sparkContext().hadoopConfiguration();
        hadoopConf.set("fs.swift.impl","org.apache.hadoop.fs.swift.snative.SwiftNativeFileSystem");
        hadoopConf.set("fs.swift.service.auth.endpoint.prefix","/AUTH_");
        hadoopConf.set("fs.swift.service.OVH.http.port","443");
        hadoopConf.set("fs.swift.service.OVH.auth.url","https://auth.cloud.ovh.net/v2.0/tokens");
        hadoopConf.set("fs.swift.service.OVH.tenant",System.getenv("OS_TENANT_NAME"));
        hadoopConf.set("fs.swift.service.OVH.region",System.getenv("OS_REGION_NAME"));
        hadoopConf.set("fs.swift.service.OVH.useApikey","false");
        hadoopConf.set("fs.swift.service.OVH.username",System.getenv("OS_USERNAME"));
        hadoopConf.set("fs.swift.service.OVH.password",System.getenv("OS_PASSWORD"));


        JavaRDD<String> lines = spark.read().textFile("swift://novel.OVH/novel.txt").javaRDD();

        JavaRDD<String> words = lines.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

        JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(s, 1));

        JavaPairRDD<String, Integer> counts = ones.reduceByKey((i1, i2) -> i1 + i2);

        List<Tuple2<String, Integer>> output = counts.collect();
        for (Tuple2<?,?> tuple : output) {
            System.out.println(tuple._1() + ": " + tuple._2());
        }
        spark.stop();
    }
}