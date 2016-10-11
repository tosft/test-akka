package test_inbox;

import akka.actor.*;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * Created by root on 10/11/16.
 */
public class Main {

    public static void main(String[] args) throws TimeoutException {
        ActorSystem system = ActorSystem.create("inboxdemo", ConfigFactory.load("sample.conf"));
        ActorRef worker = system.actorOf(Props.create(MyWorker.class),"worker");


        final Inbox inbox = Inbox.create(system);
        inbox.watch(worker);
        inbox.send(worker,Msg.WORKING);
        inbox.send(worker,Msg.DONE);
        inbox.send(worker,Msg.CLOSE);

        while(true){
            Object msg = inbox.receive(Duration.create(1, TimeUnit.MINUTES));

            if (msg == Msg.CLOSE){
                System.out.println("worker is close...");
            }else if(msg instanceof Terminated){
                System.out.println("worker is dead...");
                system.shutdown();
                break;
            }else {
                System.out.println(msg);
            }

        }

    }



}
