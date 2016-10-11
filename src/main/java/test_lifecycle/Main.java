package test_lifecycle;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * Created by root on 10/11/16.
 */
public class Main {

    public static void consutomStrategy(ActorSystem system){
        ActorRef ref = system.actorOf(Props.create(Supervisor.class),"supervisor");
        ref.tell(Props.create(RestartActor.class),ActorRef.noSender());
        ActorSelection sel = system.actorSelection("akka://lifecycle/user/Supervisor/restartActor");
        for(int i=0;i<100;i++){
            sel.tell(Msg.RESTART,ActorRef.noSender());
        }
    }

    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("lifecycle", ConfigFactory.load("lifecycle.conf"));
        consutomStrategy(system);
    }

}
