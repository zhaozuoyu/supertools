package cn.zzy.flink.event;

import java.util.concurrent.ExecutorService;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.zzy.flink.job.AgentServiceJobHandler;
import cn.zzy.flink.util.CommonUtils;

/**
 * @author zhaozuoyu
 * @date 2021/12/15
 */
@Component
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger logger = LoggerFactory.getLogger(ContextRefreshedEventListener.class);

    private volatile boolean start = false;

    @Resource
    private ExecutorService flinkExecutorService;
    @Autowired
    private AgentServiceJobHandler agentServiceJobHandler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!this.start) {
            synchronized (this) {
                if (!this.start) {
                    if (CommonUtils.getApplicationContext() == null) {
                        CommonUtils.setApplicationContext(event.getApplicationContext());
                    }
                    this.initFlinkJob();
                    this.start = true;
                }
            }
        }
    }

    private void initFlinkJob() {
        logger.info("开始初始化Flink任务");
        // this.flinkExecutorService.submit(this.agentServiceJobHandler);
    }
}
