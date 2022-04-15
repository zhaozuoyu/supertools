import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaozuoyu
 * @date 2021/12/3
 */
public class FlinkTest {

    private static final Logger logger = LoggerFactory.getLogger(FlinkTest.class);

    @Test
    public void wordCountTest() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 创建数据源
        DataStream<Tuple2<String, Integer>> dataStream = env
            .fromElements("To be, or not to be,--that is the question:--", "Whether 'tis nobler in the mind to suffer",
                "The slings and arrows of outrageous fortune", "Or to take arms against a sea of troubles,")
            .flatMap(new LineSplitter());
        dataStream.print();
        try {
            env.execute("window word count!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
            // normalize and split the line
            String[] tokens = value.toLowerCase().split("\\W+");

            // emit the pairs
            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<String, Integer>(token, 1));
                }
            }
        }
    }

}
