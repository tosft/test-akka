package test_lifecycle;

import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.actor.UntypedActor;
import akka.japi.Function;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by root on 10/11/16.
 */
public class Supervisor extends UntypedActor {

    //定义一个oneforone的监督策略
    //在actor遇到err时在一分钟进行3次重试如果超过这个频率直接杀死actor
    private static SupervisorStrategy strategy = new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES),
            new Function<Throwable, SupervisorStrategy.Directive>() {
                @Override
                public SupervisorStrategy.Directive apply(Throwable t) throws Exception {
                    if (t instanceof ArithmeticException){
                        //遇到此异常 继续执行actor不做任何处理
                        System.out.println("ArithmeticException resume");
                        return SupervisorStrategy.resume();
                    } else if(t instanceof NullPointerException){
                        //遇到此异常 重启actor
                        System.out.println("NullPointerException restart");
                        return SupervisorStrategy.restart();
                    }else if(t instanceof IllegalArgumentException){
                        //遇到此异常 停止actor
                        System.out.println("IllegalArgumentException stop");
                        return SupervisorStrategy.stop();
                    }else{
                        //没有涉及段异常抛给上层actor处理
                        return SupervisorStrategy.escalate();
                    }

                }
            });


    //覆盖父类的方法 设置使用自定义监督策略
    @Override
    public SupervisorStrategy supervisorStrategy(){
        return strategy;
    }

    @Override
    public void onReceive(Object o) throws Throwable {
        if (o instanceof Props){
            //新建一个名为restartActor的子actor由当前supervior进行监督
            //当supervior actor接受到props对象时 由这个props配置生成一个restartactor
            getContext().actorOf((Props)o,"restartActor");
        }else{
            unhandled(o);
        }
    }
}
