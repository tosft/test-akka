package test_inbox;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by root on 10/11/16.
 */
public class MyWorker extends UntypedActor {

    private final LoggingAdapter log = Logging.getLogger(getContext().system(),this);

    @Override
    public void onReceive(Object msg) throws Throwable {
        if(msg == Msg.WORKING){
            log.info("working .....");
        }else if(msg == Msg.DONE){
            log.info("stop work..");
        }else if(msg == Msg.CLOSE){

            getSender().tell(Msg.CLOSE,getSelf());
            getContext().stop(getSelf());
        }else{
            unhandled(msg);
        }

    }
}
