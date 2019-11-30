package com.jsrdxzw.aspect;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;


/**
 * @Author: xuzhiwei
 * @Date: 2019/11/07
 * @Description: 监听服务的运行时间
 */
//@Aspect
//@Component
//public class ServiceLogAspect {
//
//    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
//    private static final int SLOW_TIME = 3000;
//    private static final int MEDIUM_TIME = 2000;
//
//    /**
//     * 切面表达式：
//     * execution 代表所要执行的表达式主体
//     * 第一处 * 代表方法返回类型 *代表所有类型
//     * 第二处 包名代表aop监控的类所在的包
//     * 第三处 .. 代表该包以及其子包下的所有类方法
//     * 第四处 * 代表类名，*代表所有类
//     * 第五处 *(..) *代表类中的方法名，(..)表示方法中的任何参数
//     *
//     * @param joinPoint
//     * @return
//     * @throws Throwable
//     */
//    @Around("execution(* com.jsrdxzw.service.impl..*.*(..))")
//    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        logger.info("====== 开始执行 {}.{} ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
//        long start = System.currentTimeMillis();
//        Object result = joinPoint.proceed();
//        long end = System.currentTimeMillis();
//        long takeTime = end - start;
//        if (takeTime > SLOW_TIME) {
//            logger.error("====== 执行结束 {}.{}, 耗时 {} 毫秒 ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), takeTime);
//        } else if (takeTime > MEDIUM_TIME) {
//            logger.warn("====== 执行结束 {}.{}, 耗时 {} 毫秒 ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), takeTime);
//        } else {
//            logger.info("====== 执行结束 {}.{}, 耗时 {} 毫秒 ======", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), takeTime);
//        }
//        return result;
//    }
//}
