


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static serverclient.main.mainThreadRequestCount;

/**
 *
 * @author Ebru Vural - Bayram Ünal
 */
public class ThreadTracker extends Thread {

    public static ArrayList<SubThread> threads = new ArrayList<SubThread>();

    public ThreadTracker(SubThread... thread) {

        setThreads(thread);
    }

    @Override
    public void run() {

        int i = 0;
        boolean firstStep = false;

        while (true) {
            //System.out.println("tread tracker array size : " + threads.size());
            if (!threads.get(i).isIsWorked()) { // thread çalışmadıysa
                if ((double) (threads.get(i).getPercentage()) >= (double) 70) {
                    createNewThread(threads.get(i));
                }

                if (firstStep) {  // başlangıçta bütün thread değerleri sıfır olduğu için hepsini kaldırıyordu
                    // bu nedenle bir kere çalıştırılıp daha sonra kontrol edilmesi sağlandı
                    if ((int) (threads.get(i).getPercentage()) == (int) 0 && (threads.size() > 2)) {
                        threads.get(i).setIsWorked(true);
                        threads.get((i + 1) % threads.size()).setIsWorked(false);
                        removeThread(threads.get(i), i);
                        //System.out.println("\nkaldirildi : " + threads.get(i).getThreadName() + "\n");
                    }
                }

                //System.out.println("main\t:\t" + main.mainThreadRequestCount + "\n" + threads.get(i).getThreadName() + "\t:\t" + threads.get(i).getRequestCount() + " \t-\t" + threads.get(i).getPercentage());
            }

            i++;
            i = i % threads.size();
            //System.out.println("size : " + threads.size());
            firstStep = true;
            try {
                sleep(main.subThreadRequestTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadTracker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void createNewThread(SubThread mThread) {

        int count;
        if (mThread.getRequestCount() % 2 == 1) {
            count = (mThread.getRequestCount() - 1) / 2;
        } else {
            count = mThread.getRequestCount() / 2;
        }

        main.subThreadCount++;

        SubThread newSubThread = new SubThread("Alt Sunucu - " + main.subThreadCount + "", main.subThreadCapacity, main.subThreadRequestTime, main.subThreadReceiveTime,
                main.subThreadResponseCount, main.receiveCount);

        newSubThread.setRequestCount(count);
        mThread.setRequestCount(count);
        newSubThread.setParent(mThread);

        threads.add(newSubThread);
        newSubThread.start();
        //System.out.println("uretilen : " + newSubThread.getThreadName() + " parent : " + mThread.getThreadName());
        ThreadReceiver.threads = this.threads;

    }

    public void removeThread(SubThread mThread, int i) {
        mThread.getLocation().setIsUsed(false);
        System.out.println("kaldirildi : " + mThread.getThreadName());
        mThread.stop();
        threads.remove(i);
        ThreadReceiver.threads = this.threads;
        main.subThreadCount--;

    }

    public ArrayList<SubThread> getThreads() {
        return threads;
    }

    public void setThreads(SubThread... threads) {
        for (int i = 0; i < threads.length; i++) {
            this.threads.add(threads[i]);
        }
    }

}


