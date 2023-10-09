//package ca.babpool.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//
//@Configuration
//@EnableAsync
//public class SpringAsyncConfig {
//
//    @Bean(name = "threadPoolTaskExecutor")
//    public Executor threadPoolTaskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        /**
//         * 기본 스레드 수
//         */
//        taskExecutor.setCorePoolSize(5);
//        /**
//         * 최대 스레드 수
//         */
//        taskExecutor.setMaxPoolSize(100);
//        /**
//         * Queue 사이즈
//         */
//        taskExecutor.setQueueCapacity(100);
//        taskExecutor.setThreadNamePrefix("Executor-");
//        return taskExecutor;
//    }
//}
