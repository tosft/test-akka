package test_lifecycle;

import akka.actor.UntypedActor;
import scala.Option;

/**
 * Created by root on 10/11/16.
 */
public class RestartActor extends UntypedActor {

    //覆盖此父类此方法 用来初始化资源
    @Override
    public void preStart(){
        System.out.println("preStart hashCode : " + this.hashCode());
    }

    //覆盖此父类此方法 用来释放资源
    @Override
    public void postStop(){
        System.out.println("postStop hashCode : " + this.hashCode());
    }

    @Override
    public void postRestart(Throwable t)throws Exception{
        super.postRestart(t);
        System.out.println("postRestart hashcode : " + this.hashCode());
    }

    @Override
    public void preRestart(Throwable t, Option opt)throws Exception{
        System.out.println("preRestart hashcode : " + this.hashCode());
    }

    @Override
    public void onReceive(Object msg) throws Throwable {
        if (msg == Msg.DONE){
            getContext().stop(getSelf());
        }else if(msg == Msg.RESTART){
            //此处会出发nullpoint异常
            System.out.println(((Object)null).toString());
            double a = 0 / 0;
        }
        unhandled(msg);
    }
}
