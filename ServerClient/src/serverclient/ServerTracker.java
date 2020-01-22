

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ebru Vural - Bayram Ünal
 */
public class ServerTracker extends Thread {

    ExecutorService pool = Executors.newFixedThreadPool(30);
    ArrayList<SubThread> threads;

    public ServerTracker() {
        this.threads = ThreadTracker.threads;
    }

    @Override
    public void run() {

        while (true) {

            //if ((System.currentTimeMillis() - time) > main.subThreadRequestTime) {
            this.threads = ThreadTracker.threads;
            for (int i = 0; i < threads.size(); i++) {
                pool.execute(threads.get(i));
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
                LocalDateTime now = LocalDateTime.now();
                System.out.println("req : " + threads.get(i).getRequestCount() + " " + threads.get(i).getThreadName() + " - " + dtf.format(now));
                //System.out.println("test: " + ThreadTracker.threads.get(i).getThreadName() + " calisti");

            }

            main.refreshGui();
            try {
                this.sleep(main.subThreadRequestTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(ServerTracker.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}


