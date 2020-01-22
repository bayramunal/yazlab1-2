

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import static java.lang.Thread.sleep;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ebru Vural - Bayram Ünal
 */
public class ThreadReceiver extends Thread {

    public static ArrayList<SubThread> threads = new ArrayList<SubThread>();

    public ThreadReceiver() {
        threads = ThreadTracker.threads;
    }

    @Override
    public synchronized void run() {

        while (true) {

            threads = ThreadTracker.threads;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss SSS");
            LocalDateTime now = LocalDateTime.now();

            for (int i = 0; i < threads.size(); i++) {
                System.out.println("rec : " + this.threads.get(i).getRequestCount() + " " + this.threads.get(i).getThreadName() + " : " + dtf.format(now));
                int random = generateRandomNumber(threads.get(i).getResponseCount());

                if (threads.get(i).getRequestCount() > 0
                        && (threads.get(i).getRequestCount() - random) < 0) {
                    random = threads.get(i).getRequestCount();
                    threads.get(i).setRequestCount(0);

                } else if ((threads.get(i).getRequestCount() - random) > 0) {
                    threads.get(i).setRequestCount(threads.get(i).getRequestCount() - random);
                }
                System.out.println(threads.get(i).getThreadName() + " " + random + " istek karşıladı");
            }
            try {
                this.sleep(main.subThreadReceiveTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int generateRandomNumber(int number) {
        Random r = new Random();
        return r.nextInt(number) + 1;
    }

}

