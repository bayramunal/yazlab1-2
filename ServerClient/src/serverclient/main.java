


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ebru Vural - Bayram Ünal
 */
public class main {

    public static int subThreadCount = 2;
    public static final int mainThreadCapacity = 10000;
    public static final int subThreadCapacity = 5000;

    public static final int mainThreadRequestTime = 750/4;
    public static final int mainThreadReceiveTime = 10000/4;
    public static final int mainThreadResponseCount = 100;

    public static final int subThreadRequestTime = 500/4;
    public static final int subThreadReceiveTime = 1000/4;
    public static final int subThreadResponseCount = 50;

    public static final int receiveCount = 50;

    public static SubThread mainThread, firstSubThread, secondSubThread;

    public static int mainThreadRequestCount = 0;
    public static ServerInterface serverInterface;

    static long time1, time2;

    public static void main(String[] args) {

        serverInterface = new ServerInterface();
        ServerTracker serverTracker = new ServerTracker();

        firstSubThread = new SubThread("Alt Sunucu - 1", subThreadCapacity, subThreadRequestTime, subThreadReceiveTime, subThreadResponseCount, receiveCount);
        firstSubThread.setLocation(serverInterface.locations.get(0));

        secondSubThread = new SubThread("Alt Sunucu - 2", subThreadCapacity, subThreadRequestTime, subThreadReceiveTime, subThreadResponseCount, receiveCount);
        secondSubThread.setLocation(serverInterface.locations.get(6));

        ThreadTracker mTracker = new ThreadTracker(firstSubThread, secondSubThread);
        ThreadReceiver receiver = new ThreadReceiver();

        Thread[] threads = {serverTracker, mTracker, receiver};

        ExecutorService pool = Executors.newFixedThreadPool(3);

        for (int i = 0; i < threads.length; i++) {
            pool.execute(threads[i]);
        }

        time1 = System.currentTimeMillis();
        time2 = System.currentTimeMillis();

        while (true) {

            if ((System.currentTimeMillis() - time1) > mainThreadRequestTime) {
                int random = generateRandomNumber(mainThreadResponseCount);

                if ((random + mainThreadRequestCount) <= mainThreadCapacity) {
                    mainThreadRequestCount += random;
                } else if ((random + mainThreadRequestCount) > mainThreadCapacity) {
                    mainThreadRequestCount = mainThreadCapacity;
                }
                time1 = System.currentTimeMillis();
            }

            if ((System.currentTimeMillis() - time2) > mainThreadReceiveTime) {
                int random = generateRandomNumber(receiveCount);

                if ((mainThreadRequestCount - random) >= 0) {
                    mainThreadRequestCount -= random;
                } else {
                    random = mainThreadRequestCount;
                    mainThreadRequestCount = 0;
                }
                time2 = System.currentTimeMillis();
            }
        }
    }

    private static int generateRandomNumber(int number) {
        Random r = new Random();
        return r.nextInt(number) + 1;
    }
    static long refreshTime = System.currentTimeMillis();

    public static void refreshGui() {
        if ((System.currentTimeMillis() - refreshTime) > 1000) {
            serverInterface.panel.removeAll();
            refreshTime = System.currentTimeMillis();
        }

        for (int j = 0; j < ThreadTracker.threads.size(); j++) {
            serverInterface.test(ThreadTracker.threads.get(j), ThreadTracker.threads.get(j).getPercentage());
        }
    }
    
    
    //http://bilgisayarkavramlari.sadievrenseker.com/2009/10/09/eslemeli-metotlar-synchronized-methods/
    public static synchronized void decreaseMainRequest(int random) {
        System.out.println("main : " + mainThreadRequestCount);
        if (mainThreadRequestCount > 0) {

            if ((main.mainThreadRequestCount - random) < 0) {
                random = main.mainThreadRequestCount;
                mainThreadRequestCount = 0;
            } else {
                mainThreadRequestCount -= random;
            }
        }
    }

}
