
package serverclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author Ebru Vural - Bayram Ünal
 */
public class ServerInterface extends JPanel {

    ArrayList<JLabel> labels = new ArrayList<JLabel>();
    ArrayList<ServerLocation> locations = new ArrayList<ServerLocation>();
    JFrame frame;
    public JPanel panel;
    private static int threadCount = 0;
    private boolean flag = false;

    JLabel mainName, mainPercentage;

    public ServerInterface() {
        createGraphicalInterface();
        mainName = new JLabel();
        mainPercentage = new JLabel();
        mainName.setBounds(197 * 3 + 65, 65, 75, 75);
        mainPercentage.setBounds(197 * 3 + 85, -25, 75, 75);

    }

    public void createGraphicalInterface() {

        frame = new JFrame("Multithread tracker for server");
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.white);
        frame.add(panel);
        frame.setSize(800, 600);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        initServerLocations();

    }

    public void initServerLocations() {

        int startX = 197, startY = 125;

        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (j == 3) { // ilk satıra ve orta sütuna yerleştirme yapmaması için bu satır ve sütun değerleri
                    locations.add(new ServerLocation(startX * j + 65, (startY) * i, i, j));
                    locations.get(locations.size() - 1).setForbidden(true);
                } else {
                    locations.add(new ServerLocation(startX * j + 65, (startY) * i, i, j));
                }
            }
        }

        locations.get(0).isUsed = true; // soldaki ilk slotun kullanılmaması için
        locations.get(6).isUsed = true; // birinci satırdaki en sağdaki slotun kullanılmaması için

        // eğer buralara yerleştirilen thread'ler yüzde sıfır değerine ulaşırsa buralar da kullanıma açılacaktır.
        // sadece ilk gösterimde buraların görüntü için doldurulması düşünüldü
        int rowCount = 0;
        int number = locations.size();
        while (number > 0) {
            number -= 6;
            rowCount++;
        }

    }

    public void drawMainServer() {
        ImageIcon icon = null;
        double count = (main.mainThreadRequestCount * 100) / ((double) main.mainThreadCapacity);
        icon = new ImageIcon("img\\" + ((int) count) + ".png");
        JLabel thumb = new JLabel();
        Image mIcon = null;
        try {

            mIcon = icon.getImage().getScaledInstance(75, 75, Image.SCALE_DEFAULT);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        icon = new ImageIcon(mIcon);
        thumb.setIcon(icon);

        thumb.setBounds(197 * 3 + 65, 20, 75, 75);

        mainName.setText(" Ana Sunucu");
        mainPercentage.setText("%" + count);
        addComponentToPanel(mainPercentage, mainName, thumb);

        if (!flag) {
            addComponentToPanel(mainPercentage, mainName);
            flag = true;
        }
    }

    public void test(SubThread thread, double count) {
        drawMainServer();

        ServerLocation location = null;
        int x = -1, y = -1;

        if (thread.getLocation() == null) {

            location = getEmptySlotCoordinates(thread);
            x = location.x;
            y = location.y;
            thread.initIcon(false, x, y);

        } else {
            x = thread.getLocation().x;
            y = thread.getLocation().y;
            thread.initIcon(false, x, y);

            if (thread.checkUpdate()) {
                thread.initIcon(true, x, y);
            }

        }

        thread.getLblText().setBounds(x, y + 45, 100, 75);

        thread.getLblPercentage().setText("%" + count);
        thread.getLblPercentage().setBounds(x + 20, y - 45, 100, 75);

        addComponentToPanel(thread.getThumb(), thread.getLblPercentage(), thread.getLblText());

        threadCount++;

    }

    public ServerLocation getEmptySlotCoordinates(SubThread thread) {
        //System.out.println("gelennnn : " + thread.getThreadName());
        if (thread.getParent() != null) {
            if (locations.get(0).isUsed == false) { // sol birinci slot boşsa önce oraya yerleştirme yapılsın
                return locations.get(0);
            } else if (locations.get(6).isUsed == false) { // sağ birinci slot boşsa ikinci olarak oraya yerleştirme yapsın
                return locations.get(6);
            } else { // eğer birincil slotlar doluysa üretilen yeni threadın konumu parentına göre belirlensin
                ServerLocation temp = thread.getParent().getLocation();

                int startColumn = temp.column;
                int startRow = temp.row;

                int endRow = 7;
                int endColumn = -1;

                if (temp.column < 2) {
                    endColumn = 3;
                } else if (temp.column > 3) {
                    endColumn = 7;
                } else if (temp.column == 2) {
                    endColumn = 3;
                    startColumn = 0;
                    startRow++;
                }

                if (endColumn == 3) { // soldan sağa yerleştirme

                    for (int j = 0; j < 7; j++) {
                        for (int k = (j * 7); k < (j * 7) + 3; k++) { // o satırdaki son kutuya kadar bakmalı
                            if (!locations.get(k).isUsed() && !locations.get(k).isForbidden()) {
                                locations.get(k).setIsUsed(true);
                                thread.setLocation(locations.get(k));
                                return locations.get(k);
                            }

                        }
                        // buraya kadar gelip yasak olmayan bir boşluk bulamadıysa bir satır aşağı iner
                    }

                } else { // sağdan sola yerleştirme
                    for (int j = 0; j < 7; j++) {
                        for (int k = (j * 7) + 6; k >= (j * 7) + 3; k--) { // o satırdaki son kutuya kadar bakmalı
                            if (!locations.get(k).isUsed() && !locations.get(k).isForbidden()) {
                                locations.get(k).setIsUsed(true);
                                thread.setLocation(locations.get(k));
                                return locations.get(k);
                            }

                        }
                        // buraya kadar gelip yasak olmayan bir boşluk bulamadıysa bir satır aşağı iner
                    }
                }

            }
        }
        return null;
    }

    public void addComponentToPanel(Component... components) {
        for (int i = 0; i < components.length; i++) {
            panel.add(components[i]);
        }
        refreshPanel();
    }

    public void refreshPanel() {
        panel.invalidate();
        panel.repaint();
    }

    public void verticalLines() {
        JLabel l;

        for (int i = 0; i < 7; i++) {
            l = new JLabel();
            l.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            l.setBounds(197 * i, 0, 1, frame.getHeight());
            addComponentToPanel(l);
            refreshPanel();
        }
    }

    public void horizontalLines() {
        JLabel l;

        for (int i = 0; i < 7; i++) {
            l = new JLabel();
            l.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
            l.setBounds(0, 106 * i, frame.getWidth(), 1);
            addComponentToPanel(l);
            refreshPanel();
        }
    }
}


