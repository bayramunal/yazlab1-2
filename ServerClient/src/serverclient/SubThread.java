

package serverclient;

import java.awt.Component;
import java.awt.Image;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import static serverclient.main.receiveCount;

/**
 *
 * @author Ebru Vural - Bayram Ünal
 */
public class SubThread extends Thread {

    private ServerLocation location;
    private int capacity, requestTime, receiveTime, requestCount;
    private int responseCount, receiveCount;
    private double percentage;
    private String threadName;
    private boolean isWorked = false;
    private SubThread parent;
    private JProgressBar progressBar;
    private int[] zzz = {1, 7, 13, 19, 26, 32, 35, 38, 44, 51, 57, 63, 69, 76, 82, 88, 94};

    private ImageIcon icon = null;
    private JLabel thumb;

    private JLabel lblText, lblPercentage;

    public SubThread() {
        lblText = new JLabel();
        lblText.setText(this.threadName);
        lblPercentage = new JLabel();
        thumb = new JLabel();
    }

    public SubThread(String name, int capacity, int requestTime, int receiveTime, int responseCount, int receiveCount) {
        this.threadName = name;
        this.capacity = capacity;
        this.requestTime = requestTime;
        this.receiveTime = receiveTime;
        this.responseCount = responseCount;
        this.receiveCount = receiveCount;

        lblText = new JLabel();
        lblPercentage = new JLabel();
        lblText.setText(this.threadName);
    }

    private void getRequestFromMain(int random) {
        if (this.requestCount < this.capacity && (this.requestCount + random) > this.capacity) {
            this.setRequestCount(this.requestCount + (this.capacity - this.requestCount));

        } else if ((this.requestCount + random) <= this.capacity) {
            this.setRequestCount(this.requestCount + random);
        }
    }

    @Override
    public synchronized void run() {

        int random = generateRandomNumber(receiveCount);
        main.decreaseMainRequest(random);
        getRequestFromMain(random);
        System.out.println(this.threadName + " " + random + " istek aldı");

    }

    public boolean checkUpdate() {

        for (int i = 0; i < zzz.length; i++) {
            if (((int) percentage) == zzz[i]) {
                return true;
            }
        }
        return false;
    }

    public void setComponentBounds(Component component, int x, int y, int width, int height) {
        component.setBounds(x, y, width, height);
    }

    public void initIcon(boolean flag, int x, int y) {
        if (this.thumb == null || flag == true) {
            thumb = new JLabel();
            ImageIcon icon = null;

            icon = new ImageIcon("img\\" + ((int) percentage) + ".png");

            Image mIcon = null;
            try {

                mIcon = icon.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            icon = new ImageIcon(mIcon);
            thumb.setIcon(icon);
            thumb.setBounds(x, y, 75, 75);

        }
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public JLabel getThumb() {
        return thumb;
    }

    public void setThumb(JLabel thumb) {
        this.thumb = thumb;
    }

    public JLabel getLblText() {
        return lblText;
    }

    public void setLblText(JLabel lblText) {
        this.lblText = lblText;
    }

    public JLabel getLblPercentage() {
        return lblPercentage;
    }

    public void setLblPercentage(JLabel lblPercentage) {
        this.lblPercentage = lblPercentage;
    }

    public void setTextLblText(String text) {
        this.lblText.setText(text);
    }

    public void setTextLblPercentage(String text) {
        this.lblPercentage.setText(text);
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void initJProgressBar() {
        if (this.progressBar == null) {
            progressBar = new JProgressBar();
        }
    }

    public ServerLocation getLocation() {
        return location;
    }

    public void setLocation(ServerLocation location) {
        this.location = location;
    }

    public SubThread getParent() {
        return parent;
    }

    public void setParent(SubThread parent) {
        this.parent = parent;
    }

    public boolean isIsWorked() {
        return isWorked;
    }

    public void setIsWorked(boolean isWorked) {
        this.isWorked = isWorked;
    }

    public int getResponseCount() {
        return responseCount;
    }

    public void setResponseCount(int responseCount) {
        this.responseCount = responseCount;
    }

    public int getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(int receiveCount) {
        this.receiveCount = receiveCount;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    private int generateRandomNumber(int number) {
        Random r = new Random();
        return r.nextInt(number) + 1;
    }

    public int getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(int receiveTime) {
        this.receiveTime = receiveTime;
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;

        double percent = (requestCount * 100) / ((double) this.capacity);

        this.setPercentage(percent);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(int requestTime) {
        this.requestTime = requestTime;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;

    }

}


