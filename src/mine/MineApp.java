package mine;

import java.awt.Color;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author TMN
 */
public class MineApp extends javax.swing.JFrame {

    int value = 0;
    boolean mineGenerated = false;
    boolean onGame = true;
    HashMap<Integer, ArrayList<Integer>> adjacentMap;
    HashMap<Integer, JButton> buttonMap;
    ArrayList<Integer> adjacentList;
    ArrayList<Integer> mineList;
    ArrayList<Integer> ChangeList;
    HashMap<Integer, Boolean> buttonStatusList;
    JButton defaultButton;
    Thread timer;
    float[] colorScheme;

    public MineApp() {
        initComponents();
        getAdjacentBtn();
        mapButton();
        defaultButton = new JButton();
        txtTimer.setEditable(false);
        colorScheme = Color.RGBtoHSB(103, 153, 255, null);
    }

    private void reset() {
        for (int i = 1; i <= buttonMap.size(); i++) {
            defaultButton = buttonMap.get(i);
            defaultButton.setEnabled(true);
            defaultButton.setText("");
            buttonStatusList.replace(i, false);
            defaultButton.setBackground(Color.getHSBColor(colorScheme[0], colorScheme[1], colorScheme[2]));
        }
        txtTimer.setText("000");
        mineGenerated = false;
        onGame = true;
        mineList.clear();

    }

    private void manageFlag(int slot) {
        defaultButton = buttonMap.get(slot);
        if (defaultButton.isEnabled()) {
            String text = defaultButton.getText();
            if (text.equals("")) {
                buttonStatusList.replace(slot, true);
                defaultButton.setForeground(Color.white);
                defaultButton.setText("?");
            } else if (text.equals("?")) {
                buttonStatusList.replace(slot, true);
                defaultButton.setForeground(Color.red);
                defaultButton.setText("B");
            } else {
                buttonStatusList.replace(slot, false);
                defaultButton.setText("");
            }
        }
    }

    private void mineHit() {
        for (int i = 0; i < mineList.size(); i++) {
            defaultButton = buttonMap.get(mineList.get(i));
            defaultButton.setBackground(Color.DARK_GRAY);
            timer.stop();
        }
        if (JOptionPane.showConfirmDialog(this, "Game Over!\nYour Time: "
                + "" + Integer.parseInt(txtTimer.getText()) + "s\n\nContinue?",
                "Game Over!", JOptionPane.OK_OPTION) == 0) {
            reset();
        } else {
            this.dispose();
        }
    }

    private void manageSlot(int slot) {
        defaultButton = buttonMap.get(slot);
        int adjacentMines = countAdjacentMines(slot);
        if (adjacentMines == 0) {
            ChangeList = new ArrayList<>();
            manageNonAdjacentButton(slot);
            for (int i = 0; i < ChangeList.size(); i++) {
                adjacentList = adjacentMap.get((int) ChangeList.get(i));
                for (int j = 0; j < adjacentList.size(); j++) {
                    int mineCount = countAdjacentMines(adjacentList.get(j));
                    if (mineCount != 0) {
                        manageAdjacentButton(adjacentList.get(j));
                    }
                }
            }
        } else {
            manageAdjacentButton(slot);
        }
        if (countClearSlot() == 300) {
            timer.stop();
            if (JOptionPane.showConfirmDialog(this, "Victory!\nYour Time: "
                    + "" + Integer.parseInt(txtTimer.getText()) + "s\n\nContinue?",
                    "Victory!", JOptionPane.OK_OPTION) == 0) {
                reset();
            } else {
                this.dispose();
            }
        }
    }

    private void manageAdjacentButton(int slot) {
        if (!buttonStatusList.get(slot)) {
            defaultButton = buttonMap.get(slot);
            int adjacentMines = countAdjacentMines(slot);
            defaultButton.setText("" + adjacentMines);
            switch (adjacentMines) {
                case 1: {
                    defaultButton.setForeground(Color.MAGENTA);
                    break;
                }
                case 2: {
                    defaultButton.setForeground(Color.GREEN);
                    break;
                }
                case 3: {
                    defaultButton.setForeground(Color.RED);
                    break;
                }
                case 4: {
                    defaultButton.setForeground(Color.BLUE);
                    break;
                }
                case 5: {
                    defaultButton.setForeground(Color.ORANGE);
                    break;
                }
                case 6: {
                    defaultButton.setForeground(Color.PINK);
                    break;
                }
                case 7: {
                    defaultButton.setForeground(Color.YELLOW);
                    break;
                }
                case 8: {
                    defaultButton.setForeground(Color.BLACK);
                    break;
                }
            }
            disableButton(slot);
        }
    }

    private void manageNonAdjacentButton(int slot) {
        if (!buttonStatusList.get(slot)) {
            disableButton(slot);
            ChangeList.add(slot);
            int sizeCount = adjacentMap.get(slot).size();
            for (int i = 0; i < sizeCount; i++) {
                adjacentList = adjacentMap.get(slot);
                int newSlot = (int) adjacentList.get(i);
                int count = countAdjacentMines(newSlot);
                if (count == 0) {
                    if (!buttonStatusList.get(newSlot)) {
                        manageNonAdjacentButton(newSlot);
                    }
                }
            }
        }
    }

    private int countClearSlot() {
        int total = 0;
        for (int i = 1; i <= 300; i++) {
            if (buttonStatusList.get(i)) {
                total++;
            }
        }
        return total;
    }

    private void disableButton(int slot) {
        defaultButton = buttonMap.get(slot);
        defaultButton.setEnabled(false);
        buttonStatusList.replace(slot, true);
    }

    private void generateMineSlots(int slot) {
        adjacentList = adjacentMap.get(slot);
        mineList = new ArrayList<>();
        while (mineList.size() < 75) {
            int mineSlot = (int) (Math.random() * 300 + 1);
            if (!checkDuplicateSlot(mineSlot, mineList)
                    && !checkDuplicateSlot(mineSlot, adjacentList) && mineSlot != slot) {
                mineList.add(mineSlot);
            }
        }
        mineGenerated = true;
        timer = new Thread() {
            @Override
            public void run() {
                int timer = 0;
                String out = "";
                while (true) {
                    try {
                        sleep(1000);
                        timer++;
                        if (timer < 10) {
                            out = "00" + timer;
                        } else if (timer < 100) {
                            out = "0" + timer;
                        }
                        txtTimer.setText(out);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        timer.start();
    }

    private int countAdjacentMines(int slot) {
        int count = 0;
        ArrayList<Integer> tmpList = adjacentMap.get(slot);
        for (int i = 0; i < mineList.size(); i++) {
            for (int j = 0; j < tmpList.size(); j++) {
                if (tmpList.get(j).equals(mineList.get(i))) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean checkDuplicateSlot(int slot, ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if (slot == list.get(i)) {
                return true;
            }
        }
        return false;
    }

    private void getAdjacentBtn() {
        adjacentMap = new HashMap<>();
        for (int i = 1; i <= 300; i++) {
            adjacentList = new ArrayList<>();
            if (i == 1) {
                adjacentList.add(2);
                adjacentList.add(21);
                adjacentList.add(22);
            } else if (i == 20) {
                adjacentList.add(19);
                adjacentList.add(39);
                adjacentList.add(40);
            } else if (i == 281) {
                adjacentList.add(261);
                adjacentList.add(262);
                adjacentList.add(282);
            } else if (i == 300) {
                adjacentList.add(279);
                adjacentList.add(280);
                adjacentList.add(299);
            } else if (i < 20) {
                adjacentList.add(i - 1);
                adjacentList.add(i + 1);
                adjacentList.add(i + 19);
                adjacentList.add(i + 20);
                adjacentList.add(i + 21);
            } else if (i > 281 && i < 300) {
                adjacentList.add(i - 21);
                adjacentList.add(i - 20);
                adjacentList.add(i - 19);
                adjacentList.add(i - 1);
                adjacentList.add(i + 1);
            } else if (((i - 1) % 20) == 0) {
                adjacentList.add(i - 20);
                adjacentList.add(i - 19);
                adjacentList.add(i + 1);
                adjacentList.add(i + 20);
                adjacentList.add(i + 21);
            } else if ((i % 20) == 0) {
                adjacentList.add(i - 21);
                adjacentList.add(i - 20);
                adjacentList.add(i - 1);
                adjacentList.add(i + 19);
                adjacentList.add(i + 20);
            } else {
                adjacentList.add(i - 21);
                adjacentList.add(i - 20);
                adjacentList.add(i - 19);
                adjacentList.add(i - 1);
                adjacentList.add(i + 1);
                adjacentList.add(i + 19);
                adjacentList.add(i + 20);
                adjacentList.add(i + 21);
            }
            adjacentMap.put(i, adjacentList);
        }
    }

    private void mapButton() {
        buttonMap = new HashMap<>();
        buttonMap.put(1, btn1);
        buttonMap.put(2, btn2);
        buttonMap.put(3, btn3);
        buttonMap.put(4, btn4);
        buttonMap.put(5, btn5);
        buttonMap.put(6, btn6);
        buttonMap.put(7, btn7);
        buttonMap.put(8, btn8);
        buttonMap.put(9, btn9);
        buttonMap.put(10, btn10);
        buttonMap.put(11, btn11);
        buttonMap.put(12, btn12);
        buttonMap.put(13, btn13);
        buttonMap.put(14, btn14);
        buttonMap.put(15, btn15);
        buttonMap.put(16, btn16);
        buttonMap.put(17, btn17);
        buttonMap.put(18, btn18);
        buttonMap.put(19, btn19);
        buttonMap.put(20, btn20);
        buttonMap.put(21, btn21);
        buttonMap.put(22, btn22);
        buttonMap.put(23, btn23);
        buttonMap.put(24, btn24);
        buttonMap.put(25, btn25);
        buttonMap.put(26, btn26);
        buttonMap.put(27, btn27);
        buttonMap.put(28, btn28);
        buttonMap.put(29, btn29);
        buttonMap.put(30, btn30);
        buttonMap.put(31, btn31);
        buttonMap.put(32, btn32);
        buttonMap.put(33, btn33);
        buttonMap.put(34, btn34);
        buttonMap.put(35, btn35);
        buttonMap.put(36, btn36);
        buttonMap.put(37, btn37);
        buttonMap.put(38, btn38);
        buttonMap.put(39, btn39);
        buttonMap.put(40, btn40);
        buttonMap.put(41, btn41);
        buttonMap.put(42, btn42);
        buttonMap.put(43, btn43);
        buttonMap.put(44, btn44);
        buttonMap.put(45, btn45);
        buttonMap.put(46, btn46);
        buttonMap.put(47, btn47);
        buttonMap.put(48, btn48);
        buttonMap.put(49, btn49);
        buttonMap.put(50, btn50);
        buttonMap.put(51, btn51);
        buttonMap.put(52, btn52);
        buttonMap.put(53, btn53);
        buttonMap.put(54, btn54);
        buttonMap.put(55, btn55);
        buttonMap.put(56, btn56);
        buttonMap.put(57, btn57);
        buttonMap.put(58, btn58);
        buttonMap.put(59, btn59);
        buttonMap.put(60, btn60);
        buttonMap.put(61, btn61);
        buttonMap.put(62, btn62);
        buttonMap.put(63, btn63);
        buttonMap.put(64, btn64);
        buttonMap.put(65, btn65);
        buttonMap.put(66, btn66);
        buttonMap.put(67, btn67);
        buttonMap.put(68, btn68);
        buttonMap.put(69, btn69);
        buttonMap.put(70, btn70);
        buttonMap.put(71, btn71);
        buttonMap.put(72, btn72);
        buttonMap.put(73, btn73);
        buttonMap.put(74, btn74);
        buttonMap.put(75, btn75);
        buttonMap.put(76, btn76);
        buttonMap.put(77, btn77);
        buttonMap.put(78, btn78);
        buttonMap.put(79, btn79);
        buttonMap.put(80, btn80);
        buttonMap.put(81, btn81);
        buttonMap.put(82, btn82);
        buttonMap.put(83, btn83);
        buttonMap.put(84, btn84);
        buttonMap.put(85, btn85);
        buttonMap.put(86, btn86);
        buttonMap.put(87, btn87);
        buttonMap.put(88, btn88);
        buttonMap.put(89, btn89);
        buttonMap.put(90, btn90);
        buttonMap.put(91, btn91);
        buttonMap.put(92, btn92);
        buttonMap.put(93, btn93);
        buttonMap.put(94, btn94);
        buttonMap.put(95, btn95);
        buttonMap.put(96, btn96);
        buttonMap.put(97, btn97);
        buttonMap.put(98, btn98);
        buttonMap.put(99, btn99);
        buttonMap.put(100, btn100);
        buttonMap.put(101, btn101);
        buttonMap.put(102, btn102);
        buttonMap.put(103, btn103);
        buttonMap.put(104, btn104);
        buttonMap.put(105, btn105);
        buttonMap.put(106, btn106);
        buttonMap.put(107, btn107);
        buttonMap.put(108, btn108);
        buttonMap.put(109, btn109);
        buttonMap.put(110, btn110);
        buttonMap.put(111, btn111);
        buttonMap.put(112, btn112);
        buttonMap.put(113, btn113);
        buttonMap.put(114, btn114);
        buttonMap.put(115, btn115);
        buttonMap.put(116, btn116);
        buttonMap.put(117, btn117);
        buttonMap.put(118, btn118);
        buttonMap.put(119, btn119);
        buttonMap.put(120, btn120);
        buttonMap.put(121, btn121);
        buttonMap.put(122, btn122);
        buttonMap.put(123, btn123);
        buttonMap.put(124, btn124);
        buttonMap.put(125, btn125);
        buttonMap.put(126, btn126);
        buttonMap.put(127, btn127);
        buttonMap.put(128, btn128);
        buttonMap.put(129, btn129);
        buttonMap.put(130, btn130);
        buttonMap.put(131, btn131);
        buttonMap.put(132, btn132);
        buttonMap.put(133, btn133);
        buttonMap.put(134, btn134);
        buttonMap.put(135, btn135);
        buttonMap.put(136, btn136);
        buttonMap.put(137, btn137);
        buttonMap.put(138, btn138);
        buttonMap.put(139, btn139);
        buttonMap.put(140, btn140);
        buttonMap.put(141, btn141);
        buttonMap.put(142, btn142);
        buttonMap.put(143, btn143);
        buttonMap.put(144, btn144);
        buttonMap.put(145, btn145);
        buttonMap.put(146, btn146);
        buttonMap.put(147, btn147);
        buttonMap.put(148, btn148);
        buttonMap.put(149, btn149);
        buttonMap.put(150, btn150);
        buttonMap.put(151, btn151);
        buttonMap.put(152, btn152);
        buttonMap.put(153, btn153);
        buttonMap.put(154, btn154);
        buttonMap.put(155, btn155);
        buttonMap.put(156, btn156);
        buttonMap.put(157, btn157);
        buttonMap.put(158, btn158);
        buttonMap.put(159, btn159);
        buttonMap.put(160, btn160);
        buttonMap.put(161, btn161);
        buttonMap.put(162, btn162);
        buttonMap.put(163, btn163);
        buttonMap.put(164, btn164);
        buttonMap.put(165, btn165);
        buttonMap.put(166, btn166);
        buttonMap.put(167, btn167);
        buttonMap.put(168, btn168);
        buttonMap.put(169, btn169);
        buttonMap.put(170, btn170);
        buttonMap.put(171, btn171);
        buttonMap.put(172, btn172);
        buttonMap.put(173, btn173);
        buttonMap.put(174, btn174);
        buttonMap.put(175, btn175);
        buttonMap.put(176, btn176);
        buttonMap.put(177, btn177);
        buttonMap.put(178, btn178);
        buttonMap.put(179, btn179);
        buttonMap.put(180, btn180);
        buttonMap.put(181, btn181);
        buttonMap.put(182, btn182);
        buttonMap.put(183, btn183);
        buttonMap.put(184, btn184);
        buttonMap.put(185, btn185);
        buttonMap.put(186, btn186);
        buttonMap.put(187, btn187);
        buttonMap.put(188, btn188);
        buttonMap.put(189, btn189);
        buttonMap.put(190, btn190);
        buttonMap.put(191, btn191);
        buttonMap.put(192, btn192);
        buttonMap.put(193, btn193);
        buttonMap.put(194, btn194);
        buttonMap.put(195, btn195);
        buttonMap.put(196, btn196);
        buttonMap.put(197, btn197);
        buttonMap.put(198, btn198);
        buttonMap.put(199, btn199);
        buttonMap.put(200, btn200);
        buttonMap.put(201, btn201);
        buttonMap.put(202, btn202);
        buttonMap.put(203, btn203);
        buttonMap.put(204, btn204);
        buttonMap.put(205, btn205);
        buttonMap.put(206, btn206);
        buttonMap.put(207, btn207);
        buttonMap.put(208, btn208);
        buttonMap.put(209, btn209);
        buttonMap.put(210, btn210);
        buttonMap.put(211, btn211);
        buttonMap.put(212, btn212);
        buttonMap.put(213, btn213);
        buttonMap.put(214, btn214);
        buttonMap.put(215, btn215);
        buttonMap.put(216, btn216);
        buttonMap.put(217, btn217);
        buttonMap.put(218, btn218);
        buttonMap.put(219, btn219);
        buttonMap.put(220, btn220);
        buttonMap.put(221, btn221);
        buttonMap.put(222, btn222);
        buttonMap.put(223, btn223);
        buttonMap.put(224, btn224);
        buttonMap.put(225, btn225);
        buttonMap.put(226, btn226);
        buttonMap.put(227, btn227);
        buttonMap.put(228, btn228);
        buttonMap.put(229, btn229);
        buttonMap.put(230, btn230);
        buttonMap.put(231, btn231);
        buttonMap.put(232, btn232);
        buttonMap.put(233, btn233);
        buttonMap.put(234, btn234);
        buttonMap.put(235, btn235);
        buttonMap.put(236, btn236);
        buttonMap.put(237, btn237);
        buttonMap.put(238, btn238);
        buttonMap.put(239, btn239);
        buttonMap.put(240, btn240);
        buttonMap.put(241, btn241);
        buttonMap.put(242, btn242);
        buttonMap.put(243, btn243);
        buttonMap.put(244, btn244);
        buttonMap.put(245, btn245);
        buttonMap.put(246, btn246);
        buttonMap.put(247, btn247);
        buttonMap.put(248, btn248);
        buttonMap.put(249, btn249);
        buttonMap.put(250, btn250);
        buttonMap.put(251, btn251);
        buttonMap.put(252, btn252);
        buttonMap.put(253, btn253);
        buttonMap.put(254, btn254);
        buttonMap.put(255, btn255);
        buttonMap.put(256, btn256);
        buttonMap.put(257, btn257);
        buttonMap.put(258, btn258);
        buttonMap.put(259, btn259);
        buttonMap.put(260, btn260);
        buttonMap.put(261, btn261);
        buttonMap.put(262, btn262);
        buttonMap.put(263, btn263);
        buttonMap.put(264, btn264);
        buttonMap.put(265, btn265);
        buttonMap.put(266, btn266);
        buttonMap.put(267, btn267);
        buttonMap.put(268, btn268);
        buttonMap.put(269, btn269);
        buttonMap.put(270, btn270);
        buttonMap.put(271, btn271);
        buttonMap.put(272, btn272);
        buttonMap.put(273, btn273);
        buttonMap.put(274, btn274);
        buttonMap.put(275, btn275);
        buttonMap.put(276, btn276);
        buttonMap.put(277, btn277);
        buttonMap.put(278, btn278);
        buttonMap.put(279, btn279);
        buttonMap.put(280, btn280);
        buttonMap.put(281, btn281);
        buttonMap.put(282, btn282);
        buttonMap.put(283, btn283);
        buttonMap.put(284, btn284);
        buttonMap.put(285, btn285);
        buttonMap.put(286, btn286);
        buttonMap.put(287, btn287);
        buttonMap.put(288, btn288);
        buttonMap.put(289, btn289);
        buttonMap.put(290, btn290);
        buttonMap.put(291, btn291);
        buttonMap.put(292, btn292);
        buttonMap.put(293, btn293);
        buttonMap.put(294, btn294);
        buttonMap.put(295, btn295);
        buttonMap.put(296, btn296);
        buttonMap.put(297, btn297);
        buttonMap.put(298, btn298);
        buttonMap.put(299, btn299);
        buttonMap.put(300, btn300);
        buttonStatusList = new HashMap<>();
        for (int i = 1; i <= 300; i++) {
            buttonStatusList.put(i, false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtTimer = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btn10 = new javax.swing.JButton();
        btn11 = new javax.swing.JButton();
        btn12 = new javax.swing.JButton();
        btn13 = new javax.swing.JButton();
        btn14 = new javax.swing.JButton();
        btn15 = new javax.swing.JButton();
        btn16 = new javax.swing.JButton();
        btn17 = new javax.swing.JButton();
        btn18 = new javax.swing.JButton();
        btn19 = new javax.swing.JButton();
        btn20 = new javax.swing.JButton();
        btn21 = new javax.swing.JButton();
        btn22 = new javax.swing.JButton();
        btn23 = new javax.swing.JButton();
        btn24 = new javax.swing.JButton();
        btn25 = new javax.swing.JButton();
        btn26 = new javax.swing.JButton();
        btn27 = new javax.swing.JButton();
        btn28 = new javax.swing.JButton();
        btn29 = new javax.swing.JButton();
        btn30 = new javax.swing.JButton();
        btn31 = new javax.swing.JButton();
        btn32 = new javax.swing.JButton();
        btn33 = new javax.swing.JButton();
        btn34 = new javax.swing.JButton();
        btn35 = new javax.swing.JButton();
        btn36 = new javax.swing.JButton();
        btn37 = new javax.swing.JButton();
        btn38 = new javax.swing.JButton();
        btn39 = new javax.swing.JButton();
        btn40 = new javax.swing.JButton();
        btn41 = new javax.swing.JButton();
        btn42 = new javax.swing.JButton();
        btn43 = new javax.swing.JButton();
        btn44 = new javax.swing.JButton();
        btn45 = new javax.swing.JButton();
        btn46 = new javax.swing.JButton();
        btn47 = new javax.swing.JButton();
        btn48 = new javax.swing.JButton();
        btn49 = new javax.swing.JButton();
        btn50 = new javax.swing.JButton();
        btn51 = new javax.swing.JButton();
        btn52 = new javax.swing.JButton();
        btn53 = new javax.swing.JButton();
        btn54 = new javax.swing.JButton();
        btn55 = new javax.swing.JButton();
        btn56 = new javax.swing.JButton();
        btn57 = new javax.swing.JButton();
        btn58 = new javax.swing.JButton();
        btn59 = new javax.swing.JButton();
        btn60 = new javax.swing.JButton();
        btn61 = new javax.swing.JButton();
        btn62 = new javax.swing.JButton();
        btn63 = new javax.swing.JButton();
        btn64 = new javax.swing.JButton();
        btn65 = new javax.swing.JButton();
        btn66 = new javax.swing.JButton();
        btn67 = new javax.swing.JButton();
        btn68 = new javax.swing.JButton();
        btn69 = new javax.swing.JButton();
        btn70 = new javax.swing.JButton();
        btn71 = new javax.swing.JButton();
        btn72 = new javax.swing.JButton();
        btn73 = new javax.swing.JButton();
        btn74 = new javax.swing.JButton();
        btn75 = new javax.swing.JButton();
        btn76 = new javax.swing.JButton();
        btn77 = new javax.swing.JButton();
        btn78 = new javax.swing.JButton();
        btn79 = new javax.swing.JButton();
        btn80 = new javax.swing.JButton();
        btn81 = new javax.swing.JButton();
        btn82 = new javax.swing.JButton();
        btn83 = new javax.swing.JButton();
        btn84 = new javax.swing.JButton();
        btn85 = new javax.swing.JButton();
        btn86 = new javax.swing.JButton();
        btn87 = new javax.swing.JButton();
        btn88 = new javax.swing.JButton();
        btn89 = new javax.swing.JButton();
        btn90 = new javax.swing.JButton();
        btn91 = new javax.swing.JButton();
        btn92 = new javax.swing.JButton();
        btn93 = new javax.swing.JButton();
        btn94 = new javax.swing.JButton();
        btn95 = new javax.swing.JButton();
        btn96 = new javax.swing.JButton();
        btn97 = new javax.swing.JButton();
        btn98 = new javax.swing.JButton();
        btn99 = new javax.swing.JButton();
        btn100 = new javax.swing.JButton();
        btn101 = new javax.swing.JButton();
        btn102 = new javax.swing.JButton();
        btn103 = new javax.swing.JButton();
        btn104 = new javax.swing.JButton();
        btn105 = new javax.swing.JButton();
        btn106 = new javax.swing.JButton();
        btn107 = new javax.swing.JButton();
        btn108 = new javax.swing.JButton();
        btn109 = new javax.swing.JButton();
        btn110 = new javax.swing.JButton();
        btn111 = new javax.swing.JButton();
        btn112 = new javax.swing.JButton();
        btn113 = new javax.swing.JButton();
        btn114 = new javax.swing.JButton();
        btn115 = new javax.swing.JButton();
        btn116 = new javax.swing.JButton();
        btn117 = new javax.swing.JButton();
        btn118 = new javax.swing.JButton();
        btn119 = new javax.swing.JButton();
        btn120 = new javax.swing.JButton();
        btn121 = new javax.swing.JButton();
        btn122 = new javax.swing.JButton();
        btn123 = new javax.swing.JButton();
        btn124 = new javax.swing.JButton();
        btn125 = new javax.swing.JButton();
        btn126 = new javax.swing.JButton();
        btn127 = new javax.swing.JButton();
        btn128 = new javax.swing.JButton();
        btn129 = new javax.swing.JButton();
        btn130 = new javax.swing.JButton();
        btn131 = new javax.swing.JButton();
        btn132 = new javax.swing.JButton();
        btn133 = new javax.swing.JButton();
        btn134 = new javax.swing.JButton();
        btn135 = new javax.swing.JButton();
        btn136 = new javax.swing.JButton();
        btn137 = new javax.swing.JButton();
        btn138 = new javax.swing.JButton();
        btn139 = new javax.swing.JButton();
        btn140 = new javax.swing.JButton();
        btn141 = new javax.swing.JButton();
        btn142 = new javax.swing.JButton();
        btn143 = new javax.swing.JButton();
        btn144 = new javax.swing.JButton();
        btn145 = new javax.swing.JButton();
        btn146 = new javax.swing.JButton();
        btn147 = new javax.swing.JButton();
        btn148 = new javax.swing.JButton();
        btn149 = new javax.swing.JButton();
        btn150 = new javax.swing.JButton();
        btn151 = new javax.swing.JButton();
        btn152 = new javax.swing.JButton();
        btn153 = new javax.swing.JButton();
        btn154 = new javax.swing.JButton();
        btn155 = new javax.swing.JButton();
        btn156 = new javax.swing.JButton();
        btn157 = new javax.swing.JButton();
        btn158 = new javax.swing.JButton();
        btn159 = new javax.swing.JButton();
        btn160 = new javax.swing.JButton();
        btn161 = new javax.swing.JButton();
        btn162 = new javax.swing.JButton();
        btn163 = new javax.swing.JButton();
        btn164 = new javax.swing.JButton();
        btn165 = new javax.swing.JButton();
        btn166 = new javax.swing.JButton();
        btn167 = new javax.swing.JButton();
        btn168 = new javax.swing.JButton();
        btn169 = new javax.swing.JButton();
        btn170 = new javax.swing.JButton();
        btn171 = new javax.swing.JButton();
        btn172 = new javax.swing.JButton();
        btn173 = new javax.swing.JButton();
        btn174 = new javax.swing.JButton();
        btn175 = new javax.swing.JButton();
        btn176 = new javax.swing.JButton();
        btn177 = new javax.swing.JButton();
        btn178 = new javax.swing.JButton();
        btn179 = new javax.swing.JButton();
        btn180 = new javax.swing.JButton();
        btn181 = new javax.swing.JButton();
        btn182 = new javax.swing.JButton();
        btn183 = new javax.swing.JButton();
        btn184 = new javax.swing.JButton();
        btn185 = new javax.swing.JButton();
        btn186 = new javax.swing.JButton();
        btn187 = new javax.swing.JButton();
        btn188 = new javax.swing.JButton();
        btn189 = new javax.swing.JButton();
        btn190 = new javax.swing.JButton();
        btn191 = new javax.swing.JButton();
        btn192 = new javax.swing.JButton();
        btn193 = new javax.swing.JButton();
        btn194 = new javax.swing.JButton();
        btn195 = new javax.swing.JButton();
        btn196 = new javax.swing.JButton();
        btn197 = new javax.swing.JButton();
        btn198 = new javax.swing.JButton();
        btn199 = new javax.swing.JButton();
        btn200 = new javax.swing.JButton();
        btn201 = new javax.swing.JButton();
        btn202 = new javax.swing.JButton();
        btn203 = new javax.swing.JButton();
        btn204 = new javax.swing.JButton();
        btn205 = new javax.swing.JButton();
        btn206 = new javax.swing.JButton();
        btn207 = new javax.swing.JButton();
        btn208 = new javax.swing.JButton();
        btn209 = new javax.swing.JButton();
        btn210 = new javax.swing.JButton();
        btn211 = new javax.swing.JButton();
        btn212 = new javax.swing.JButton();
        btn213 = new javax.swing.JButton();
        btn214 = new javax.swing.JButton();
        btn215 = new javax.swing.JButton();
        btn216 = new javax.swing.JButton();
        btn217 = new javax.swing.JButton();
        btn218 = new javax.swing.JButton();
        btn219 = new javax.swing.JButton();
        btn220 = new javax.swing.JButton();
        btn221 = new javax.swing.JButton();
        btn222 = new javax.swing.JButton();
        btn223 = new javax.swing.JButton();
        btn224 = new javax.swing.JButton();
        btn225 = new javax.swing.JButton();
        btn226 = new javax.swing.JButton();
        btn227 = new javax.swing.JButton();
        btn228 = new javax.swing.JButton();
        btn229 = new javax.swing.JButton();
        btn230 = new javax.swing.JButton();
        btn231 = new javax.swing.JButton();
        btn232 = new javax.swing.JButton();
        btn233 = new javax.swing.JButton();
        btn234 = new javax.swing.JButton();
        btn235 = new javax.swing.JButton();
        btn236 = new javax.swing.JButton();
        btn237 = new javax.swing.JButton();
        btn238 = new javax.swing.JButton();
        btn239 = new javax.swing.JButton();
        btn240 = new javax.swing.JButton();
        btn241 = new javax.swing.JButton();
        btn242 = new javax.swing.JButton();
        btn243 = new javax.swing.JButton();
        btn244 = new javax.swing.JButton();
        btn245 = new javax.swing.JButton();
        btn246 = new javax.swing.JButton();
        btn247 = new javax.swing.JButton();
        btn248 = new javax.swing.JButton();
        btn249 = new javax.swing.JButton();
        btn250 = new javax.swing.JButton();
        btn251 = new javax.swing.JButton();
        btn252 = new javax.swing.JButton();
        btn253 = new javax.swing.JButton();
        btn254 = new javax.swing.JButton();
        btn255 = new javax.swing.JButton();
        btn256 = new javax.swing.JButton();
        btn257 = new javax.swing.JButton();
        btn258 = new javax.swing.JButton();
        btn259 = new javax.swing.JButton();
        btn260 = new javax.swing.JButton();
        btn261 = new javax.swing.JButton();
        btn262 = new javax.swing.JButton();
        btn263 = new javax.swing.JButton();
        btn264 = new javax.swing.JButton();
        btn265 = new javax.swing.JButton();
        btn266 = new javax.swing.JButton();
        btn267 = new javax.swing.JButton();
        btn268 = new javax.swing.JButton();
        btn269 = new javax.swing.JButton();
        btn270 = new javax.swing.JButton();
        btn271 = new javax.swing.JButton();
        btn272 = new javax.swing.JButton();
        btn273 = new javax.swing.JButton();
        btn274 = new javax.swing.JButton();
        btn275 = new javax.swing.JButton();
        btn276 = new javax.swing.JButton();
        btn277 = new javax.swing.JButton();
        btn278 = new javax.swing.JButton();
        btn279 = new javax.swing.JButton();
        btn280 = new javax.swing.JButton();
        btn281 = new javax.swing.JButton();
        btn282 = new javax.swing.JButton();
        btn283 = new javax.swing.JButton();
        btn284 = new javax.swing.JButton();
        btn285 = new javax.swing.JButton();
        btn286 = new javax.swing.JButton();
        btn287 = new javax.swing.JButton();
        btn288 = new javax.swing.JButton();
        btn289 = new javax.swing.JButton();
        btn290 = new javax.swing.JButton();
        btn291 = new javax.swing.JButton();
        btn292 = new javax.swing.JButton();
        btn293 = new javax.swing.JButton();
        btn294 = new javax.swing.JButton();
        btn295 = new javax.swing.JButton();
        btn296 = new javax.swing.JButton();
        btn297 = new javax.swing.JButton();
        btn298 = new javax.swing.JButton();
        btn299 = new javax.swing.JButton();
        btn300 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(730, 50));

        txtTimer.setBackground(new java.awt.Color(102, 102, 102));
        txtTimer.setFont(new java.awt.Font("Trebuchet MS", 1, 30)); // NOI18N
        txtTimer.setForeground(new java.awt.Color(255, 255, 255));
        txtTimer.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTimer.setText("000");
        txtTimer.setPreferredSize(new java.awt.Dimension(70, 40));
        jPanel1.add(txtTimer);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jPanel2.setPreferredSize(new java.awt.Dimension(30, 100));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel3.setPreferredSize(new java.awt.Dimension(730, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 876, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setPreferredSize(new java.awt.Dimension(30, 100));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 220, Short.MAX_VALUE)
        );

        getContentPane().add(jPanel4, java.awt.BorderLayout.LINE_END);

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));
        jPanel5.setLayout(new java.awt.GridLayout(15, 20, -5, -5));

        btn1.setBackground(new java.awt.Color(102, 153, 255));
        btn1.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn1.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn1.setMaximumSize(new java.awt.Dimension(20, 20));
        btn1.setPreferredSize(new java.awt.Dimension(20, 20));
        btn1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn1MouseClicked(evt);
            }
        });
        jPanel5.add(btn1);

        btn2.setBackground(new java.awt.Color(102, 153, 255));
        btn2.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn2.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn2.setMaximumSize(new java.awt.Dimension(20, 20));
        btn2.setPreferredSize(new java.awt.Dimension(20, 20));
        btn2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn2MouseClicked(evt);
            }
        });
        jPanel5.add(btn2);

        btn3.setBackground(new java.awt.Color(102, 153, 255));
        btn3.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn3.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn3.setMaximumSize(new java.awt.Dimension(20, 20));
        btn3.setPreferredSize(new java.awt.Dimension(20, 20));
        btn3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn3MouseClicked(evt);
            }
        });
        jPanel5.add(btn3);

        btn4.setBackground(new java.awt.Color(102, 153, 255));
        btn4.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn4.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn4.setMaximumSize(new java.awt.Dimension(20, 20));
        btn4.setPreferredSize(new java.awt.Dimension(20, 20));
        btn4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn4MouseClicked(evt);
            }
        });
        jPanel5.add(btn4);

        btn5.setBackground(new java.awt.Color(102, 153, 255));
        btn5.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn5.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn5.setMaximumSize(new java.awt.Dimension(20, 20));
        btn5.setPreferredSize(new java.awt.Dimension(20, 20));
        btn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn5MouseClicked(evt);
            }
        });
        jPanel5.add(btn5);

        btn6.setBackground(new java.awt.Color(102, 153, 255));
        btn6.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn6.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn6.setMaximumSize(new java.awt.Dimension(20, 20));
        btn6.setPreferredSize(new java.awt.Dimension(20, 20));
        btn6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn6MouseClicked(evt);
            }
        });
        jPanel5.add(btn6);

        btn7.setBackground(new java.awt.Color(102, 153, 255));
        btn7.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn7.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn7.setMaximumSize(new java.awt.Dimension(20, 20));
        btn7.setPreferredSize(new java.awt.Dimension(20, 20));
        btn7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn7MouseClicked(evt);
            }
        });
        jPanel5.add(btn7);

        btn8.setBackground(new java.awt.Color(102, 153, 255));
        btn8.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn8.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn8.setMaximumSize(new java.awt.Dimension(20, 20));
        btn8.setPreferredSize(new java.awt.Dimension(20, 20));
        btn8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn8MouseClicked(evt);
            }
        });
        jPanel5.add(btn8);

        btn9.setBackground(new java.awt.Color(102, 153, 255));
        btn9.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn9.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn9.setMaximumSize(new java.awt.Dimension(20, 20));
        btn9.setPreferredSize(new java.awt.Dimension(20, 20));
        btn9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn9MouseClicked(evt);
            }
        });
        jPanel5.add(btn9);

        btn10.setBackground(new java.awt.Color(102, 153, 255));
        btn10.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn10.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn10.setMaximumSize(new java.awt.Dimension(20, 20));
        btn10.setPreferredSize(new java.awt.Dimension(20, 20));
        btn10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn10MouseClicked(evt);
            }
        });
        jPanel5.add(btn10);

        btn11.setBackground(new java.awt.Color(102, 153, 255));
        btn11.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn11.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn11.setMaximumSize(new java.awt.Dimension(20, 20));
        btn11.setPreferredSize(new java.awt.Dimension(20, 20));
        btn11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn11MouseClicked(evt);
            }
        });
        jPanel5.add(btn11);

        btn12.setBackground(new java.awt.Color(102, 153, 255));
        btn12.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn12.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn12.setMaximumSize(new java.awt.Dimension(20, 20));
        btn12.setPreferredSize(new java.awt.Dimension(20, 20));
        btn12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn12MouseClicked(evt);
            }
        });
        jPanel5.add(btn12);

        btn13.setBackground(new java.awt.Color(102, 153, 255));
        btn13.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn13.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn13.setMaximumSize(new java.awt.Dimension(20, 20));
        btn13.setPreferredSize(new java.awt.Dimension(20, 20));
        btn13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn13MouseClicked(evt);
            }
        });
        jPanel5.add(btn13);

        btn14.setBackground(new java.awt.Color(102, 153, 255));
        btn14.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn14.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn14.setMaximumSize(new java.awt.Dimension(20, 20));
        btn14.setPreferredSize(new java.awt.Dimension(20, 20));
        btn14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn14MouseClicked(evt);
            }
        });
        jPanel5.add(btn14);

        btn15.setBackground(new java.awt.Color(102, 153, 255));
        btn15.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn15.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn15.setMaximumSize(new java.awt.Dimension(20, 20));
        btn15.setPreferredSize(new java.awt.Dimension(20, 20));
        btn15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn15MouseClicked(evt);
            }
        });
        jPanel5.add(btn15);

        btn16.setBackground(new java.awt.Color(102, 153, 255));
        btn16.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn16.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn16.setMaximumSize(new java.awt.Dimension(20, 20));
        btn16.setPreferredSize(new java.awt.Dimension(20, 20));
        btn16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn16MouseClicked(evt);
            }
        });
        jPanel5.add(btn16);

        btn17.setBackground(new java.awt.Color(102, 153, 255));
        btn17.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn17.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn17.setMaximumSize(new java.awt.Dimension(20, 20));
        btn17.setPreferredSize(new java.awt.Dimension(20, 20));
        btn17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn17MouseClicked(evt);
            }
        });
        jPanel5.add(btn17);

        btn18.setBackground(new java.awt.Color(102, 153, 255));
        btn18.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn18.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn18.setMaximumSize(new java.awt.Dimension(20, 20));
        btn18.setPreferredSize(new java.awt.Dimension(20, 20));
        btn18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn18MouseClicked(evt);
            }
        });
        jPanel5.add(btn18);

        btn19.setBackground(new java.awt.Color(102, 153, 255));
        btn19.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn19.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn19.setMaximumSize(new java.awt.Dimension(20, 20));
        btn19.setPreferredSize(new java.awt.Dimension(20, 20));
        btn19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn19MouseClicked(evt);
            }
        });
        jPanel5.add(btn19);

        btn20.setBackground(new java.awt.Color(102, 153, 255));
        btn20.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn20.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn20.setMaximumSize(new java.awt.Dimension(20, 20));
        btn20.setPreferredSize(new java.awt.Dimension(20, 20));
        btn20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn20MouseClicked(evt);
            }
        });
        jPanel5.add(btn20);

        btn21.setBackground(new java.awt.Color(102, 153, 255));
        btn21.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn21.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn21.setMaximumSize(new java.awt.Dimension(20, 20));
        btn21.setPreferredSize(new java.awt.Dimension(20, 20));
        btn21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn21MouseClicked(evt);
            }
        });
        jPanel5.add(btn21);

        btn22.setBackground(new java.awt.Color(102, 153, 255));
        btn22.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn22.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn22.setMaximumSize(new java.awt.Dimension(20, 20));
        btn22.setPreferredSize(new java.awt.Dimension(20, 20));
        btn22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn22MouseClicked(evt);
            }
        });
        jPanel5.add(btn22);

        btn23.setBackground(new java.awt.Color(102, 153, 255));
        btn23.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn23.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn23.setMaximumSize(new java.awt.Dimension(20, 20));
        btn23.setPreferredSize(new java.awt.Dimension(20, 20));
        btn23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn23MouseClicked(evt);
            }
        });
        jPanel5.add(btn23);

        btn24.setBackground(new java.awt.Color(102, 153, 255));
        btn24.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn24.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn24.setMaximumSize(new java.awt.Dimension(20, 20));
        btn24.setPreferredSize(new java.awt.Dimension(20, 20));
        btn24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn24MouseClicked(evt);
            }
        });
        jPanel5.add(btn24);

        btn25.setBackground(new java.awt.Color(102, 153, 255));
        btn25.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn25.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn25.setMaximumSize(new java.awt.Dimension(20, 20));
        btn25.setPreferredSize(new java.awt.Dimension(20, 20));
        btn25.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn25MouseClicked(evt);
            }
        });
        jPanel5.add(btn25);

        btn26.setBackground(new java.awt.Color(102, 153, 255));
        btn26.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn26.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn26.setMaximumSize(new java.awt.Dimension(20, 20));
        btn26.setPreferredSize(new java.awt.Dimension(20, 20));
        btn26.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn26MouseClicked(evt);
            }
        });
        jPanel5.add(btn26);

        btn27.setBackground(new java.awt.Color(102, 153, 255));
        btn27.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn27.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn27.setMaximumSize(new java.awt.Dimension(20, 20));
        btn27.setPreferredSize(new java.awt.Dimension(20, 20));
        btn27.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn27MouseClicked(evt);
            }
        });
        jPanel5.add(btn27);

        btn28.setBackground(new java.awt.Color(102, 153, 255));
        btn28.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn28.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn28.setMaximumSize(new java.awt.Dimension(20, 20));
        btn28.setPreferredSize(new java.awt.Dimension(20, 20));
        btn28.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn28MouseClicked(evt);
            }
        });
        jPanel5.add(btn28);

        btn29.setBackground(new java.awt.Color(102, 153, 255));
        btn29.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn29.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn29.setMaximumSize(new java.awt.Dimension(20, 20));
        btn29.setPreferredSize(new java.awt.Dimension(20, 20));
        btn29.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn29MouseClicked(evt);
            }
        });
        jPanel5.add(btn29);

        btn30.setBackground(new java.awt.Color(102, 153, 255));
        btn30.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn30.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn30.setMaximumSize(new java.awt.Dimension(20, 20));
        btn30.setPreferredSize(new java.awt.Dimension(20, 20));
        btn30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn30MouseClicked(evt);
            }
        });
        jPanel5.add(btn30);

        btn31.setBackground(new java.awt.Color(102, 153, 255));
        btn31.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn31.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn31.setMaximumSize(new java.awt.Dimension(20, 20));
        btn31.setPreferredSize(new java.awt.Dimension(20, 20));
        btn31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn31MouseClicked(evt);
            }
        });
        jPanel5.add(btn31);

        btn32.setBackground(new java.awt.Color(102, 153, 255));
        btn32.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn32.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn32.setMaximumSize(new java.awt.Dimension(20, 20));
        btn32.setPreferredSize(new java.awt.Dimension(20, 20));
        btn32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn32MouseClicked(evt);
            }
        });
        jPanel5.add(btn32);

        btn33.setBackground(new java.awt.Color(102, 153, 255));
        btn33.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn33.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn33.setMaximumSize(new java.awt.Dimension(20, 20));
        btn33.setPreferredSize(new java.awt.Dimension(20, 20));
        btn33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn33MouseClicked(evt);
            }
        });
        jPanel5.add(btn33);

        btn34.setBackground(new java.awt.Color(102, 153, 255));
        btn34.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn34.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn34.setMaximumSize(new java.awt.Dimension(20, 20));
        btn34.setPreferredSize(new java.awt.Dimension(20, 20));
        btn34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn34MouseClicked(evt);
            }
        });
        jPanel5.add(btn34);

        btn35.setBackground(new java.awt.Color(102, 153, 255));
        btn35.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn35.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn35.setMaximumSize(new java.awt.Dimension(20, 20));
        btn35.setPreferredSize(new java.awt.Dimension(20, 20));
        btn35.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn35MouseClicked(evt);
            }
        });
        jPanel5.add(btn35);

        btn36.setBackground(new java.awt.Color(102, 153, 255));
        btn36.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn36.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn36.setMaximumSize(new java.awt.Dimension(20, 20));
        btn36.setPreferredSize(new java.awt.Dimension(20, 20));
        btn36.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn36MouseClicked(evt);
            }
        });
        jPanel5.add(btn36);

        btn37.setBackground(new java.awt.Color(102, 153, 255));
        btn37.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn37.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn37.setMaximumSize(new java.awt.Dimension(20, 20));
        btn37.setPreferredSize(new java.awt.Dimension(20, 20));
        btn37.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn37MouseClicked(evt);
            }
        });
        jPanel5.add(btn37);

        btn38.setBackground(new java.awt.Color(102, 153, 255));
        btn38.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn38.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn38.setMaximumSize(new java.awt.Dimension(20, 20));
        btn38.setPreferredSize(new java.awt.Dimension(20, 20));
        btn38.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn38MouseClicked(evt);
            }
        });
        jPanel5.add(btn38);

        btn39.setBackground(new java.awt.Color(102, 153, 255));
        btn39.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn39.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn39.setMaximumSize(new java.awt.Dimension(20, 20));
        btn39.setPreferredSize(new java.awt.Dimension(20, 20));
        btn39.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn39MouseClicked(evt);
            }
        });
        jPanel5.add(btn39);

        btn40.setBackground(new java.awt.Color(102, 153, 255));
        btn40.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn40.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn40.setMaximumSize(new java.awt.Dimension(20, 20));
        btn40.setPreferredSize(new java.awt.Dimension(20, 20));
        btn40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn40MouseClicked(evt);
            }
        });
        jPanel5.add(btn40);

        btn41.setBackground(new java.awt.Color(102, 153, 255));
        btn41.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn41.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn41.setMaximumSize(new java.awt.Dimension(20, 20));
        btn41.setPreferredSize(new java.awt.Dimension(20, 20));
        btn41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn41MouseClicked(evt);
            }
        });
        jPanel5.add(btn41);

        btn42.setBackground(new java.awt.Color(102, 153, 255));
        btn42.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn42.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn42.setMaximumSize(new java.awt.Dimension(20, 20));
        btn42.setPreferredSize(new java.awt.Dimension(20, 20));
        btn42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn42MouseClicked(evt);
            }
        });
        jPanel5.add(btn42);

        btn43.setBackground(new java.awt.Color(102, 153, 255));
        btn43.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn43.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn43.setMaximumSize(new java.awt.Dimension(20, 20));
        btn43.setPreferredSize(new java.awt.Dimension(20, 20));
        btn43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn43MouseClicked(evt);
            }
        });
        jPanel5.add(btn43);

        btn44.setBackground(new java.awt.Color(102, 153, 255));
        btn44.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn44.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn44.setMaximumSize(new java.awt.Dimension(20, 20));
        btn44.setPreferredSize(new java.awt.Dimension(20, 20));
        btn44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn44MouseClicked(evt);
            }
        });
        jPanel5.add(btn44);

        btn45.setBackground(new java.awt.Color(102, 153, 255));
        btn45.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn45.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn45.setMaximumSize(new java.awt.Dimension(20, 20));
        btn45.setPreferredSize(new java.awt.Dimension(20, 20));
        btn45.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn45MouseClicked(evt);
            }
        });
        jPanel5.add(btn45);

        btn46.setBackground(new java.awt.Color(102, 153, 255));
        btn46.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn46.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn46.setMaximumSize(new java.awt.Dimension(20, 20));
        btn46.setPreferredSize(new java.awt.Dimension(20, 20));
        btn46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn46MouseClicked(evt);
            }
        });
        jPanel5.add(btn46);

        btn47.setBackground(new java.awt.Color(102, 153, 255));
        btn47.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn47.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn47.setMaximumSize(new java.awt.Dimension(20, 20));
        btn47.setPreferredSize(new java.awt.Dimension(20, 20));
        btn47.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn47MouseClicked(evt);
            }
        });
        jPanel5.add(btn47);

        btn48.setBackground(new java.awt.Color(102, 153, 255));
        btn48.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn48.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn48.setMaximumSize(new java.awt.Dimension(20, 20));
        btn48.setPreferredSize(new java.awt.Dimension(20, 20));
        btn48.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn48MouseClicked(evt);
            }
        });
        jPanel5.add(btn48);

        btn49.setBackground(new java.awt.Color(102, 153, 255));
        btn49.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn49.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn49.setMaximumSize(new java.awt.Dimension(20, 20));
        btn49.setPreferredSize(new java.awt.Dimension(20, 20));
        btn49.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn49MouseClicked(evt);
            }
        });
        jPanel5.add(btn49);

        btn50.setBackground(new java.awt.Color(102, 153, 255));
        btn50.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn50.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn50.setMaximumSize(new java.awt.Dimension(20, 20));
        btn50.setPreferredSize(new java.awt.Dimension(20, 20));
        btn50.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn50MouseClicked(evt);
            }
        });
        jPanel5.add(btn50);

        btn51.setBackground(new java.awt.Color(102, 153, 255));
        btn51.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn51.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn51.setMaximumSize(new java.awt.Dimension(20, 20));
        btn51.setPreferredSize(new java.awt.Dimension(20, 20));
        btn51.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn51MouseClicked(evt);
            }
        });
        jPanel5.add(btn51);

        btn52.setBackground(new java.awt.Color(102, 153, 255));
        btn52.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn52.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn52.setMaximumSize(new java.awt.Dimension(20, 20));
        btn52.setPreferredSize(new java.awt.Dimension(20, 20));
        btn52.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn52MouseClicked(evt);
            }
        });
        jPanel5.add(btn52);

        btn53.setBackground(new java.awt.Color(102, 153, 255));
        btn53.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn53.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn53.setMaximumSize(new java.awt.Dimension(20, 20));
        btn53.setPreferredSize(new java.awt.Dimension(20, 20));
        btn53.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn53MouseClicked(evt);
            }
        });
        jPanel5.add(btn53);

        btn54.setBackground(new java.awt.Color(102, 153, 255));
        btn54.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn54.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn54.setMaximumSize(new java.awt.Dimension(20, 20));
        btn54.setPreferredSize(new java.awt.Dimension(20, 20));
        btn54.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn54MouseClicked(evt);
            }
        });
        jPanel5.add(btn54);

        btn55.setBackground(new java.awt.Color(102, 153, 255));
        btn55.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn55.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn55.setMaximumSize(new java.awt.Dimension(20, 20));
        btn55.setPreferredSize(new java.awt.Dimension(20, 20));
        btn55.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn55MouseClicked(evt);
            }
        });
        jPanel5.add(btn55);

        btn56.setBackground(new java.awt.Color(102, 153, 255));
        btn56.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn56.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn56.setMaximumSize(new java.awt.Dimension(20, 20));
        btn56.setPreferredSize(new java.awt.Dimension(20, 20));
        btn56.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn56MouseClicked(evt);
            }
        });
        jPanel5.add(btn56);

        btn57.setBackground(new java.awt.Color(102, 153, 255));
        btn57.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn57.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn57.setMaximumSize(new java.awt.Dimension(20, 20));
        btn57.setPreferredSize(new java.awt.Dimension(20, 20));
        btn57.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn57MouseClicked(evt);
            }
        });
        jPanel5.add(btn57);

        btn58.setBackground(new java.awt.Color(102, 153, 255));
        btn58.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn58.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn58.setMaximumSize(new java.awt.Dimension(20, 20));
        btn58.setPreferredSize(new java.awt.Dimension(20, 20));
        btn58.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn58MouseClicked(evt);
            }
        });
        jPanel5.add(btn58);

        btn59.setBackground(new java.awt.Color(102, 153, 255));
        btn59.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn59.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn59.setMaximumSize(new java.awt.Dimension(20, 20));
        btn59.setPreferredSize(new java.awt.Dimension(20, 20));
        btn59.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn59MouseClicked(evt);
            }
        });
        jPanel5.add(btn59);

        btn60.setBackground(new java.awt.Color(102, 153, 255));
        btn60.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn60.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn60.setMaximumSize(new java.awt.Dimension(20, 20));
        btn60.setPreferredSize(new java.awt.Dimension(20, 20));
        btn60.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn60MouseClicked(evt);
            }
        });
        jPanel5.add(btn60);

        btn61.setBackground(new java.awt.Color(102, 153, 255));
        btn61.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn61.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn61.setMaximumSize(new java.awt.Dimension(20, 20));
        btn61.setPreferredSize(new java.awt.Dimension(20, 20));
        btn61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn61MouseClicked(evt);
            }
        });
        jPanel5.add(btn61);

        btn62.setBackground(new java.awt.Color(102, 153, 255));
        btn62.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn62.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn62.setMaximumSize(new java.awt.Dimension(20, 20));
        btn62.setPreferredSize(new java.awt.Dimension(20, 20));
        btn62.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn62MouseClicked(evt);
            }
        });
        jPanel5.add(btn62);

        btn63.setBackground(new java.awt.Color(102, 153, 255));
        btn63.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn63.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn63.setMaximumSize(new java.awt.Dimension(20, 20));
        btn63.setPreferredSize(new java.awt.Dimension(20, 20));
        btn63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn63MouseClicked(evt);
            }
        });
        jPanel5.add(btn63);

        btn64.setBackground(new java.awt.Color(102, 153, 255));
        btn64.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn64.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn64.setMaximumSize(new java.awt.Dimension(20, 20));
        btn64.setPreferredSize(new java.awt.Dimension(20, 20));
        btn64.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn64MouseClicked(evt);
            }
        });
        jPanel5.add(btn64);

        btn65.setBackground(new java.awt.Color(102, 153, 255));
        btn65.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn65.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn65.setMaximumSize(new java.awt.Dimension(20, 20));
        btn65.setPreferredSize(new java.awt.Dimension(20, 20));
        btn65.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn65MouseClicked(evt);
            }
        });
        jPanel5.add(btn65);

        btn66.setBackground(new java.awt.Color(102, 153, 255));
        btn66.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn66.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn66.setMaximumSize(new java.awt.Dimension(20, 20));
        btn66.setPreferredSize(new java.awt.Dimension(20, 20));
        btn66.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn66MouseClicked(evt);
            }
        });
        jPanel5.add(btn66);

        btn67.setBackground(new java.awt.Color(102, 153, 255));
        btn67.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn67.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn67.setMaximumSize(new java.awt.Dimension(20, 20));
        btn67.setPreferredSize(new java.awt.Dimension(20, 20));
        btn67.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn67MouseClicked(evt);
            }
        });
        jPanel5.add(btn67);

        btn68.setBackground(new java.awt.Color(102, 153, 255));
        btn68.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn68.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn68.setMaximumSize(new java.awt.Dimension(20, 20));
        btn68.setPreferredSize(new java.awt.Dimension(20, 20));
        btn68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn68MouseClicked(evt);
            }
        });
        jPanel5.add(btn68);

        btn69.setBackground(new java.awt.Color(102, 153, 255));
        btn69.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn69.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn69.setMaximumSize(new java.awt.Dimension(20, 20));
        btn69.setPreferredSize(new java.awt.Dimension(20, 20));
        btn69.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn69MouseClicked(evt);
            }
        });
        jPanel5.add(btn69);

        btn70.setBackground(new java.awt.Color(102, 153, 255));
        btn70.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn70.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn70.setMaximumSize(new java.awt.Dimension(20, 20));
        btn70.setPreferredSize(new java.awt.Dimension(20, 20));
        btn70.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn70MouseClicked(evt);
            }
        });
        jPanel5.add(btn70);

        btn71.setBackground(new java.awt.Color(102, 153, 255));
        btn71.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn71.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn71.setMaximumSize(new java.awt.Dimension(20, 20));
        btn71.setPreferredSize(new java.awt.Dimension(20, 20));
        btn71.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn71MouseClicked(evt);
            }
        });
        jPanel5.add(btn71);

        btn72.setBackground(new java.awt.Color(102, 153, 255));
        btn72.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn72.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn72.setMaximumSize(new java.awt.Dimension(20, 20));
        btn72.setPreferredSize(new java.awt.Dimension(20, 20));
        btn72.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn72MouseClicked(evt);
            }
        });
        jPanel5.add(btn72);

        btn73.setBackground(new java.awt.Color(102, 153, 255));
        btn73.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn73.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn73.setMaximumSize(new java.awt.Dimension(20, 20));
        btn73.setPreferredSize(new java.awt.Dimension(20, 20));
        btn73.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn73MouseClicked(evt);
            }
        });
        jPanel5.add(btn73);

        btn74.setBackground(new java.awt.Color(102, 153, 255));
        btn74.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn74.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn74.setMaximumSize(new java.awt.Dimension(20, 20));
        btn74.setPreferredSize(new java.awt.Dimension(20, 20));
        btn74.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn74MouseClicked(evt);
            }
        });
        jPanel5.add(btn74);

        btn75.setBackground(new java.awt.Color(102, 153, 255));
        btn75.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn75.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn75.setMaximumSize(new java.awt.Dimension(20, 20));
        btn75.setPreferredSize(new java.awt.Dimension(20, 20));
        btn75.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn75MouseClicked(evt);
            }
        });
        jPanel5.add(btn75);

        btn76.setBackground(new java.awt.Color(102, 153, 255));
        btn76.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn76.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn76.setMaximumSize(new java.awt.Dimension(20, 20));
        btn76.setPreferredSize(new java.awt.Dimension(20, 20));
        btn76.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn76MouseClicked(evt);
            }
        });
        jPanel5.add(btn76);

        btn77.setBackground(new java.awt.Color(102, 153, 255));
        btn77.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn77.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn77.setMaximumSize(new java.awt.Dimension(20, 20));
        btn77.setPreferredSize(new java.awt.Dimension(20, 20));
        btn77.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn77MouseClicked(evt);
            }
        });
        jPanel5.add(btn77);

        btn78.setBackground(new java.awt.Color(102, 153, 255));
        btn78.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn78.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn78.setMaximumSize(new java.awt.Dimension(20, 20));
        btn78.setPreferredSize(new java.awt.Dimension(20, 20));
        btn78.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn78MouseClicked(evt);
            }
        });
        jPanel5.add(btn78);

        btn79.setBackground(new java.awt.Color(102, 153, 255));
        btn79.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn79.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn79.setMaximumSize(new java.awt.Dimension(20, 20));
        btn79.setPreferredSize(new java.awt.Dimension(20, 20));
        btn79.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn79MouseClicked(evt);
            }
        });
        jPanel5.add(btn79);

        btn80.setBackground(new java.awt.Color(102, 153, 255));
        btn80.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn80.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn80.setMaximumSize(new java.awt.Dimension(20, 20));
        btn80.setPreferredSize(new java.awt.Dimension(20, 20));
        btn80.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn80MouseClicked(evt);
            }
        });
        jPanel5.add(btn80);

        btn81.setBackground(new java.awt.Color(102, 153, 255));
        btn81.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn81.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn81.setMaximumSize(new java.awt.Dimension(20, 20));
        btn81.setPreferredSize(new java.awt.Dimension(20, 20));
        btn81.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn81MouseClicked(evt);
            }
        });
        jPanel5.add(btn81);

        btn82.setBackground(new java.awt.Color(102, 153, 255));
        btn82.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn82.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn82.setMaximumSize(new java.awt.Dimension(20, 20));
        btn82.setPreferredSize(new java.awt.Dimension(20, 20));
        btn82.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn82MouseClicked(evt);
            }
        });
        jPanel5.add(btn82);

        btn83.setBackground(new java.awt.Color(102, 153, 255));
        btn83.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn83.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn83.setMaximumSize(new java.awt.Dimension(20, 20));
        btn83.setPreferredSize(new java.awt.Dimension(20, 20));
        btn83.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn83MouseClicked(evt);
            }
        });
        jPanel5.add(btn83);

        btn84.setBackground(new java.awt.Color(102, 153, 255));
        btn84.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn84.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn84.setMaximumSize(new java.awt.Dimension(20, 20));
        btn84.setPreferredSize(new java.awt.Dimension(20, 20));
        btn84.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn84MouseClicked(evt);
            }
        });
        jPanel5.add(btn84);

        btn85.setBackground(new java.awt.Color(102, 153, 255));
        btn85.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn85.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn85.setMaximumSize(new java.awt.Dimension(20, 20));
        btn85.setPreferredSize(new java.awt.Dimension(20, 20));
        btn85.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn85MouseClicked(evt);
            }
        });
        jPanel5.add(btn85);

        btn86.setBackground(new java.awt.Color(102, 153, 255));
        btn86.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn86.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn86.setMaximumSize(new java.awt.Dimension(20, 20));
        btn86.setPreferredSize(new java.awt.Dimension(20, 20));
        btn86.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn86MouseClicked(evt);
            }
        });
        jPanel5.add(btn86);

        btn87.setBackground(new java.awt.Color(102, 153, 255));
        btn87.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn87.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn87.setMaximumSize(new java.awt.Dimension(20, 20));
        btn87.setPreferredSize(new java.awt.Dimension(20, 20));
        btn87.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn87MouseClicked(evt);
            }
        });
        jPanel5.add(btn87);

        btn88.setBackground(new java.awt.Color(102, 153, 255));
        btn88.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn88.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn88.setMaximumSize(new java.awt.Dimension(20, 20));
        btn88.setPreferredSize(new java.awt.Dimension(20, 20));
        btn88.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn88MouseClicked(evt);
            }
        });
        jPanel5.add(btn88);

        btn89.setBackground(new java.awt.Color(102, 153, 255));
        btn89.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn89.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn89.setMaximumSize(new java.awt.Dimension(20, 20));
        btn89.setPreferredSize(new java.awt.Dimension(20, 20));
        btn89.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn89MouseClicked(evt);
            }
        });
        jPanel5.add(btn89);

        btn90.setBackground(new java.awt.Color(102, 153, 255));
        btn90.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn90.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn90.setMaximumSize(new java.awt.Dimension(20, 20));
        btn90.setPreferredSize(new java.awt.Dimension(20, 20));
        btn90.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn90MouseClicked(evt);
            }
        });
        jPanel5.add(btn90);

        btn91.setBackground(new java.awt.Color(102, 153, 255));
        btn91.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn91.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn91.setMaximumSize(new java.awt.Dimension(20, 20));
        btn91.setPreferredSize(new java.awt.Dimension(20, 20));
        btn91.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn91MouseClicked(evt);
            }
        });
        jPanel5.add(btn91);

        btn92.setBackground(new java.awt.Color(102, 153, 255));
        btn92.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn92.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn92.setMaximumSize(new java.awt.Dimension(20, 20));
        btn92.setPreferredSize(new java.awt.Dimension(20, 20));
        btn92.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn92MouseClicked(evt);
            }
        });
        jPanel5.add(btn92);

        btn93.setBackground(new java.awt.Color(102, 153, 255));
        btn93.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn93.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn93.setMaximumSize(new java.awt.Dimension(20, 20));
        btn93.setPreferredSize(new java.awt.Dimension(20, 20));
        btn93.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn93MouseClicked(evt);
            }
        });
        jPanel5.add(btn93);

        btn94.setBackground(new java.awt.Color(102, 153, 255));
        btn94.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn94.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn94.setMaximumSize(new java.awt.Dimension(20, 20));
        btn94.setPreferredSize(new java.awt.Dimension(20, 20));
        btn94.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn94MouseClicked(evt);
            }
        });
        jPanel5.add(btn94);

        btn95.setBackground(new java.awt.Color(102, 153, 255));
        btn95.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn95.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn95.setMaximumSize(new java.awt.Dimension(20, 20));
        btn95.setPreferredSize(new java.awt.Dimension(20, 20));
        btn95.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn95MouseClicked(evt);
            }
        });
        jPanel5.add(btn95);

        btn96.setBackground(new java.awt.Color(102, 153, 255));
        btn96.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn96.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn96.setMaximumSize(new java.awt.Dimension(20, 20));
        btn96.setPreferredSize(new java.awt.Dimension(20, 20));
        btn96.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn96MouseClicked(evt);
            }
        });
        jPanel5.add(btn96);

        btn97.setBackground(new java.awt.Color(102, 153, 255));
        btn97.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn97.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn97.setMaximumSize(new java.awt.Dimension(20, 20));
        btn97.setPreferredSize(new java.awt.Dimension(20, 20));
        btn97.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn97MouseClicked(evt);
            }
        });
        jPanel5.add(btn97);

        btn98.setBackground(new java.awt.Color(102, 153, 255));
        btn98.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn98.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn98.setMaximumSize(new java.awt.Dimension(20, 20));
        btn98.setPreferredSize(new java.awt.Dimension(20, 20));
        btn98.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn98MouseClicked(evt);
            }
        });
        jPanel5.add(btn98);

        btn99.setBackground(new java.awt.Color(102, 153, 255));
        btn99.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn99.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn99.setMaximumSize(new java.awt.Dimension(20, 20));
        btn99.setPreferredSize(new java.awt.Dimension(20, 20));
        btn99.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn99MouseClicked(evt);
            }
        });
        jPanel5.add(btn99);

        btn100.setBackground(new java.awt.Color(102, 153, 255));
        btn100.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn100.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn100.setMaximumSize(new java.awt.Dimension(20, 20));
        btn100.setPreferredSize(new java.awt.Dimension(20, 20));
        btn100.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn100MouseClicked(evt);
            }
        });
        jPanel5.add(btn100);

        btn101.setBackground(new java.awt.Color(102, 153, 255));
        btn101.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn101.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn101.setMaximumSize(new java.awt.Dimension(20, 20));
        btn101.setPreferredSize(new java.awt.Dimension(20, 20));
        btn101.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn101MouseClicked(evt);
            }
        });
        jPanel5.add(btn101);

        btn102.setBackground(new java.awt.Color(102, 153, 255));
        btn102.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn102.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn102.setMaximumSize(new java.awt.Dimension(20, 20));
        btn102.setPreferredSize(new java.awt.Dimension(20, 20));
        btn102.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn102MouseClicked(evt);
            }
        });
        jPanel5.add(btn102);

        btn103.setBackground(new java.awt.Color(102, 153, 255));
        btn103.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn103.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn103.setMaximumSize(new java.awt.Dimension(20, 20));
        btn103.setPreferredSize(new java.awt.Dimension(20, 20));
        btn103.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn103MouseClicked(evt);
            }
        });
        jPanel5.add(btn103);

        btn104.setBackground(new java.awt.Color(102, 153, 255));
        btn104.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn104.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn104.setMaximumSize(new java.awt.Dimension(20, 20));
        btn104.setPreferredSize(new java.awt.Dimension(20, 20));
        btn104.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn104MouseClicked(evt);
            }
        });
        jPanel5.add(btn104);

        btn105.setBackground(new java.awt.Color(102, 153, 255));
        btn105.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn105.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn105.setMaximumSize(new java.awt.Dimension(20, 20));
        btn105.setPreferredSize(new java.awt.Dimension(20, 20));
        btn105.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn105MouseClicked(evt);
            }
        });
        jPanel5.add(btn105);

        btn106.setBackground(new java.awt.Color(102, 153, 255));
        btn106.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn106.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn106.setMaximumSize(new java.awt.Dimension(20, 20));
        btn106.setPreferredSize(new java.awt.Dimension(20, 20));
        btn106.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn106MouseClicked(evt);
            }
        });
        jPanel5.add(btn106);

        btn107.setBackground(new java.awt.Color(102, 153, 255));
        btn107.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn107.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn107.setMaximumSize(new java.awt.Dimension(20, 20));
        btn107.setPreferredSize(new java.awt.Dimension(20, 20));
        btn107.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn107MouseClicked(evt);
            }
        });
        jPanel5.add(btn107);

        btn108.setBackground(new java.awt.Color(102, 153, 255));
        btn108.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn108.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn108.setMaximumSize(new java.awt.Dimension(20, 20));
        btn108.setPreferredSize(new java.awt.Dimension(20, 20));
        btn108.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn108MouseClicked(evt);
            }
        });
        jPanel5.add(btn108);

        btn109.setBackground(new java.awt.Color(102, 153, 255));
        btn109.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn109.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn109.setMaximumSize(new java.awt.Dimension(20, 20));
        btn109.setPreferredSize(new java.awt.Dimension(20, 20));
        btn109.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn109MouseClicked(evt);
            }
        });
        jPanel5.add(btn109);

        btn110.setBackground(new java.awt.Color(102, 153, 255));
        btn110.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn110.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn110.setMaximumSize(new java.awt.Dimension(20, 20));
        btn110.setPreferredSize(new java.awt.Dimension(20, 20));
        btn110.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn110MouseClicked(evt);
            }
        });
        jPanel5.add(btn110);

        btn111.setBackground(new java.awt.Color(102, 153, 255));
        btn111.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn111.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn111.setMaximumSize(new java.awt.Dimension(20, 20));
        btn111.setPreferredSize(new java.awt.Dimension(20, 20));
        btn111.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn111MouseClicked(evt);
            }
        });
        jPanel5.add(btn111);

        btn112.setBackground(new java.awt.Color(102, 153, 255));
        btn112.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn112.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn112.setMaximumSize(new java.awt.Dimension(20, 20));
        btn112.setPreferredSize(new java.awt.Dimension(20, 20));
        btn112.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn112MouseClicked(evt);
            }
        });
        jPanel5.add(btn112);

        btn113.setBackground(new java.awt.Color(102, 153, 255));
        btn113.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn113.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn113.setMaximumSize(new java.awt.Dimension(20, 20));
        btn113.setPreferredSize(new java.awt.Dimension(20, 20));
        btn113.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn113MouseClicked(evt);
            }
        });
        jPanel5.add(btn113);

        btn114.setBackground(new java.awt.Color(102, 153, 255));
        btn114.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn114.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn114.setMaximumSize(new java.awt.Dimension(20, 20));
        btn114.setPreferredSize(new java.awt.Dimension(20, 20));
        btn114.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn114MouseClicked(evt);
            }
        });
        jPanel5.add(btn114);

        btn115.setBackground(new java.awt.Color(102, 153, 255));
        btn115.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn115.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn115.setMaximumSize(new java.awt.Dimension(20, 20));
        btn115.setPreferredSize(new java.awt.Dimension(20, 20));
        btn115.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn115MouseClicked(evt);
            }
        });
        jPanel5.add(btn115);

        btn116.setBackground(new java.awt.Color(102, 153, 255));
        btn116.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn116.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn116.setMaximumSize(new java.awt.Dimension(20, 20));
        btn116.setPreferredSize(new java.awt.Dimension(20, 20));
        btn116.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn116MouseClicked(evt);
            }
        });
        jPanel5.add(btn116);

        btn117.setBackground(new java.awt.Color(102, 153, 255));
        btn117.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn117.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn117.setMaximumSize(new java.awt.Dimension(20, 20));
        btn117.setPreferredSize(new java.awt.Dimension(20, 20));
        btn117.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn117MouseClicked(evt);
            }
        });
        jPanel5.add(btn117);

        btn118.setBackground(new java.awt.Color(102, 153, 255));
        btn118.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn118.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn118.setMaximumSize(new java.awt.Dimension(20, 20));
        btn118.setPreferredSize(new java.awt.Dimension(20, 20));
        btn118.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn118MouseClicked(evt);
            }
        });
        jPanel5.add(btn118);

        btn119.setBackground(new java.awt.Color(102, 153, 255));
        btn119.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn119.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn119.setMaximumSize(new java.awt.Dimension(20, 20));
        btn119.setPreferredSize(new java.awt.Dimension(20, 20));
        btn119.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn119MouseClicked(evt);
            }
        });
        jPanel5.add(btn119);

        btn120.setBackground(new java.awt.Color(102, 153, 255));
        btn120.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn120.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn120.setMaximumSize(new java.awt.Dimension(20, 20));
        btn120.setPreferredSize(new java.awt.Dimension(20, 20));
        btn120.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn120MouseClicked(evt);
            }
        });
        jPanel5.add(btn120);

        btn121.setBackground(new java.awt.Color(102, 153, 255));
        btn121.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn121.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn121.setMaximumSize(new java.awt.Dimension(20, 20));
        btn121.setPreferredSize(new java.awt.Dimension(20, 20));
        btn121.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn121MouseClicked(evt);
            }
        });
        jPanel5.add(btn121);

        btn122.setBackground(new java.awt.Color(102, 153, 255));
        btn122.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn122.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn122.setMaximumSize(new java.awt.Dimension(20, 20));
        btn122.setPreferredSize(new java.awt.Dimension(20, 20));
        btn122.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn122MouseClicked(evt);
            }
        });
        jPanel5.add(btn122);

        btn123.setBackground(new java.awt.Color(102, 153, 255));
        btn123.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn123.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn123.setMaximumSize(new java.awt.Dimension(20, 20));
        btn123.setPreferredSize(new java.awt.Dimension(20, 20));
        btn123.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn123MouseClicked(evt);
            }
        });
        jPanel5.add(btn123);

        btn124.setBackground(new java.awt.Color(102, 153, 255));
        btn124.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn124.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn124.setMaximumSize(new java.awt.Dimension(20, 20));
        btn124.setPreferredSize(new java.awt.Dimension(20, 20));
        btn124.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn124MouseClicked(evt);
            }
        });
        jPanel5.add(btn124);

        btn125.setBackground(new java.awt.Color(102, 153, 255));
        btn125.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn125.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn125.setMaximumSize(new java.awt.Dimension(20, 20));
        btn125.setPreferredSize(new java.awt.Dimension(20, 20));
        btn125.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn125MouseClicked(evt);
            }
        });
        jPanel5.add(btn125);

        btn126.setBackground(new java.awt.Color(102, 153, 255));
        btn126.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn126.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn126.setMaximumSize(new java.awt.Dimension(20, 20));
        btn126.setPreferredSize(new java.awt.Dimension(20, 20));
        btn126.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn126MouseClicked(evt);
            }
        });
        jPanel5.add(btn126);

        btn127.setBackground(new java.awt.Color(102, 153, 255));
        btn127.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn127.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn127.setMaximumSize(new java.awt.Dimension(20, 20));
        btn127.setPreferredSize(new java.awt.Dimension(20, 20));
        btn127.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn127MouseClicked(evt);
            }
        });
        jPanel5.add(btn127);

        btn128.setBackground(new java.awt.Color(102, 153, 255));
        btn128.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn128.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn128.setMaximumSize(new java.awt.Dimension(20, 20));
        btn128.setPreferredSize(new java.awt.Dimension(20, 20));
        btn128.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn128MouseClicked(evt);
            }
        });
        jPanel5.add(btn128);

        btn129.setBackground(new java.awt.Color(102, 153, 255));
        btn129.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn129.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn129.setMaximumSize(new java.awt.Dimension(20, 20));
        btn129.setPreferredSize(new java.awt.Dimension(20, 20));
        btn129.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn129MouseClicked(evt);
            }
        });
        jPanel5.add(btn129);

        btn130.setBackground(new java.awt.Color(102, 153, 255));
        btn130.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn130.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn130.setMaximumSize(new java.awt.Dimension(20, 20));
        btn130.setPreferredSize(new java.awt.Dimension(20, 20));
        btn130.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn130MouseClicked(evt);
            }
        });
        jPanel5.add(btn130);

        btn131.setBackground(new java.awt.Color(102, 153, 255));
        btn131.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn131.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn131.setMaximumSize(new java.awt.Dimension(20, 20));
        btn131.setPreferredSize(new java.awt.Dimension(20, 20));
        btn131.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn131MouseClicked(evt);
            }
        });
        jPanel5.add(btn131);

        btn132.setBackground(new java.awt.Color(102, 153, 255));
        btn132.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn132.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn132.setMaximumSize(new java.awt.Dimension(20, 20));
        btn132.setPreferredSize(new java.awt.Dimension(20, 20));
        btn132.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn132MouseClicked(evt);
            }
        });
        jPanel5.add(btn132);

        btn133.setBackground(new java.awt.Color(102, 153, 255));
        btn133.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn133.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn133.setMaximumSize(new java.awt.Dimension(20, 20));
        btn133.setPreferredSize(new java.awt.Dimension(20, 20));
        btn133.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn133MouseClicked(evt);
            }
        });
        jPanel5.add(btn133);

        btn134.setBackground(new java.awt.Color(102, 153, 255));
        btn134.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn134.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn134.setMaximumSize(new java.awt.Dimension(20, 20));
        btn134.setPreferredSize(new java.awt.Dimension(20, 20));
        btn134.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn134MouseClicked(evt);
            }
        });
        jPanel5.add(btn134);

        btn135.setBackground(new java.awt.Color(102, 153, 255));
        btn135.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn135.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn135.setMaximumSize(new java.awt.Dimension(20, 20));
        btn135.setPreferredSize(new java.awt.Dimension(20, 20));
        btn135.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn135MouseClicked(evt);
            }
        });
        jPanel5.add(btn135);

        btn136.setBackground(new java.awt.Color(102, 153, 255));
        btn136.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn136.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn136.setMaximumSize(new java.awt.Dimension(20, 20));
        btn136.setPreferredSize(new java.awt.Dimension(20, 20));
        btn136.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn136MouseClicked(evt);
            }
        });
        jPanel5.add(btn136);

        btn137.setBackground(new java.awt.Color(102, 153, 255));
        btn137.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn137.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn137.setMaximumSize(new java.awt.Dimension(20, 20));
        btn137.setPreferredSize(new java.awt.Dimension(20, 20));
        btn137.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn137MouseClicked(evt);
            }
        });
        jPanel5.add(btn137);

        btn138.setBackground(new java.awt.Color(102, 153, 255));
        btn138.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn138.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn138.setMaximumSize(new java.awt.Dimension(20, 20));
        btn138.setPreferredSize(new java.awt.Dimension(20, 20));
        btn138.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn138MouseClicked(evt);
            }
        });
        jPanel5.add(btn138);

        btn139.setBackground(new java.awt.Color(102, 153, 255));
        btn139.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn139.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn139.setMaximumSize(new java.awt.Dimension(20, 20));
        btn139.setPreferredSize(new java.awt.Dimension(20, 20));
        btn139.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn139MouseClicked(evt);
            }
        });
        jPanel5.add(btn139);

        btn140.setBackground(new java.awt.Color(102, 153, 255));
        btn140.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn140.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn140.setMaximumSize(new java.awt.Dimension(20, 20));
        btn140.setPreferredSize(new java.awt.Dimension(20, 20));
        btn140.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn140MouseClicked(evt);
            }
        });
        jPanel5.add(btn140);

        btn141.setBackground(new java.awt.Color(102, 153, 255));
        btn141.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn141.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn141.setMaximumSize(new java.awt.Dimension(20, 20));
        btn141.setPreferredSize(new java.awt.Dimension(20, 20));
        btn141.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn141MouseClicked(evt);
            }
        });
        jPanel5.add(btn141);

        btn142.setBackground(new java.awt.Color(102, 153, 255));
        btn142.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn142.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn142.setMaximumSize(new java.awt.Dimension(20, 20));
        btn142.setPreferredSize(new java.awt.Dimension(20, 20));
        btn142.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn142MouseClicked(evt);
            }
        });
        jPanel5.add(btn142);

        btn143.setBackground(new java.awt.Color(102, 153, 255));
        btn143.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn143.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn143.setMaximumSize(new java.awt.Dimension(20, 20));
        btn143.setPreferredSize(new java.awt.Dimension(20, 20));
        btn143.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn143MouseClicked(evt);
            }
        });
        jPanel5.add(btn143);

        btn144.setBackground(new java.awt.Color(102, 153, 255));
        btn144.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn144.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn144.setMaximumSize(new java.awt.Dimension(20, 20));
        btn144.setPreferredSize(new java.awt.Dimension(20, 20));
        btn144.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn144MouseClicked(evt);
            }
        });
        jPanel5.add(btn144);

        btn145.setBackground(new java.awt.Color(102, 153, 255));
        btn145.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn145.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn145.setMaximumSize(new java.awt.Dimension(20, 20));
        btn145.setPreferredSize(new java.awt.Dimension(20, 20));
        btn145.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn145MouseClicked(evt);
            }
        });
        jPanel5.add(btn145);

        btn146.setBackground(new java.awt.Color(102, 153, 255));
        btn146.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn146.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn146.setMaximumSize(new java.awt.Dimension(20, 20));
        btn146.setPreferredSize(new java.awt.Dimension(20, 20));
        btn146.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn146MouseClicked(evt);
            }
        });
        jPanel5.add(btn146);

        btn147.setBackground(new java.awt.Color(102, 153, 255));
        btn147.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn147.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn147.setMaximumSize(new java.awt.Dimension(20, 20));
        btn147.setPreferredSize(new java.awt.Dimension(20, 20));
        btn147.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn147MouseClicked(evt);
            }
        });
        jPanel5.add(btn147);

        btn148.setBackground(new java.awt.Color(102, 153, 255));
        btn148.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn148.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn148.setMaximumSize(new java.awt.Dimension(20, 20));
        btn148.setPreferredSize(new java.awt.Dimension(20, 20));
        btn148.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn148MouseClicked(evt);
            }
        });
        jPanel5.add(btn148);

        btn149.setBackground(new java.awt.Color(102, 153, 255));
        btn149.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn149.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn149.setMaximumSize(new java.awt.Dimension(20, 20));
        btn149.setPreferredSize(new java.awt.Dimension(20, 20));
        btn149.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn149MouseClicked(evt);
            }
        });
        jPanel5.add(btn149);

        btn150.setBackground(new java.awt.Color(102, 153, 255));
        btn150.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn150.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn150.setMaximumSize(new java.awt.Dimension(20, 20));
        btn150.setPreferredSize(new java.awt.Dimension(20, 20));
        btn150.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn150MouseClicked(evt);
            }
        });
        jPanel5.add(btn150);

        btn151.setBackground(new java.awt.Color(102, 153, 255));
        btn151.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn151.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn151.setMaximumSize(new java.awt.Dimension(20, 20));
        btn151.setPreferredSize(new java.awt.Dimension(20, 20));
        btn151.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn151MouseClicked(evt);
            }
        });
        jPanel5.add(btn151);

        btn152.setBackground(new java.awt.Color(102, 153, 255));
        btn152.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn152.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn152.setMaximumSize(new java.awt.Dimension(20, 20));
        btn152.setPreferredSize(new java.awt.Dimension(20, 20));
        btn152.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn152MouseClicked(evt);
            }
        });
        jPanel5.add(btn152);

        btn153.setBackground(new java.awt.Color(102, 153, 255));
        btn153.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn153.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn153.setMaximumSize(new java.awt.Dimension(20, 20));
        btn153.setPreferredSize(new java.awt.Dimension(20, 20));
        btn153.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn153MouseClicked(evt);
            }
        });
        jPanel5.add(btn153);

        btn154.setBackground(new java.awt.Color(102, 153, 255));
        btn154.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn154.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn154.setMaximumSize(new java.awt.Dimension(20, 20));
        btn154.setPreferredSize(new java.awt.Dimension(20, 20));
        btn154.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn154MouseClicked(evt);
            }
        });
        jPanel5.add(btn154);

        btn155.setBackground(new java.awt.Color(102, 153, 255));
        btn155.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn155.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn155.setMaximumSize(new java.awt.Dimension(20, 20));
        btn155.setPreferredSize(new java.awt.Dimension(20, 20));
        btn155.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn155MouseClicked(evt);
            }
        });
        jPanel5.add(btn155);

        btn156.setBackground(new java.awt.Color(102, 153, 255));
        btn156.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn156.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn156.setMaximumSize(new java.awt.Dimension(20, 20));
        btn156.setPreferredSize(new java.awt.Dimension(20, 20));
        btn156.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn156MouseClicked(evt);
            }
        });
        jPanel5.add(btn156);

        btn157.setBackground(new java.awt.Color(102, 153, 255));
        btn157.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn157.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn157.setMaximumSize(new java.awt.Dimension(20, 20));
        btn157.setPreferredSize(new java.awt.Dimension(20, 20));
        btn157.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn157MouseClicked(evt);
            }
        });
        jPanel5.add(btn157);

        btn158.setBackground(new java.awt.Color(102, 153, 255));
        btn158.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn158.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn158.setMaximumSize(new java.awt.Dimension(20, 20));
        btn158.setPreferredSize(new java.awt.Dimension(20, 20));
        btn158.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn158MouseClicked(evt);
            }
        });
        jPanel5.add(btn158);

        btn159.setBackground(new java.awt.Color(102, 153, 255));
        btn159.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn159.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn159.setMaximumSize(new java.awt.Dimension(20, 20));
        btn159.setPreferredSize(new java.awt.Dimension(20, 20));
        btn159.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn159MouseClicked(evt);
            }
        });
        jPanel5.add(btn159);

        btn160.setBackground(new java.awt.Color(102, 153, 255));
        btn160.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn160.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn160.setMaximumSize(new java.awt.Dimension(20, 20));
        btn160.setPreferredSize(new java.awt.Dimension(20, 20));
        btn160.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn160MouseClicked(evt);
            }
        });
        jPanel5.add(btn160);

        btn161.setBackground(new java.awt.Color(102, 153, 255));
        btn161.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn161.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn161.setMaximumSize(new java.awt.Dimension(20, 20));
        btn161.setPreferredSize(new java.awt.Dimension(20, 20));
        btn161.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn161MouseClicked(evt);
            }
        });
        jPanel5.add(btn161);

        btn162.setBackground(new java.awt.Color(102, 153, 255));
        btn162.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn162.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn162.setMaximumSize(new java.awt.Dimension(20, 20));
        btn162.setPreferredSize(new java.awt.Dimension(20, 20));
        btn162.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn162MouseClicked(evt);
            }
        });
        jPanel5.add(btn162);

        btn163.setBackground(new java.awt.Color(102, 153, 255));
        btn163.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn163.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn163.setMaximumSize(new java.awt.Dimension(20, 20));
        btn163.setPreferredSize(new java.awt.Dimension(20, 20));
        btn163.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn163MouseClicked(evt);
            }
        });
        jPanel5.add(btn163);

        btn164.setBackground(new java.awt.Color(102, 153, 255));
        btn164.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn164.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn164.setMaximumSize(new java.awt.Dimension(20, 20));
        btn164.setPreferredSize(new java.awt.Dimension(20, 20));
        btn164.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn164MouseClicked(evt);
            }
        });
        jPanel5.add(btn164);

        btn165.setBackground(new java.awt.Color(102, 153, 255));
        btn165.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn165.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn165.setMaximumSize(new java.awt.Dimension(20, 20));
        btn165.setPreferredSize(new java.awt.Dimension(20, 20));
        btn165.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn165MouseClicked(evt);
            }
        });
        jPanel5.add(btn165);

        btn166.setBackground(new java.awt.Color(102, 153, 255));
        btn166.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn166.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn166.setMaximumSize(new java.awt.Dimension(20, 20));
        btn166.setPreferredSize(new java.awt.Dimension(20, 20));
        btn166.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn166MouseClicked(evt);
            }
        });
        jPanel5.add(btn166);

        btn167.setBackground(new java.awt.Color(102, 153, 255));
        btn167.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn167.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn167.setMaximumSize(new java.awt.Dimension(20, 20));
        btn167.setPreferredSize(new java.awt.Dimension(20, 20));
        btn167.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn167MouseClicked(evt);
            }
        });
        jPanel5.add(btn167);

        btn168.setBackground(new java.awt.Color(102, 153, 255));
        btn168.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn168.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn168.setMaximumSize(new java.awt.Dimension(20, 20));
        btn168.setPreferredSize(new java.awt.Dimension(20, 20));
        btn168.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn168MouseClicked(evt);
            }
        });
        jPanel5.add(btn168);

        btn169.setBackground(new java.awt.Color(102, 153, 255));
        btn169.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn169.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn169.setMaximumSize(new java.awt.Dimension(20, 20));
        btn169.setPreferredSize(new java.awt.Dimension(20, 20));
        btn169.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn169MouseClicked(evt);
            }
        });
        jPanel5.add(btn169);

        btn170.setBackground(new java.awt.Color(102, 153, 255));
        btn170.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn170.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn170.setMaximumSize(new java.awt.Dimension(20, 20));
        btn170.setPreferredSize(new java.awt.Dimension(20, 20));
        btn170.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn170MouseClicked(evt);
            }
        });
        jPanel5.add(btn170);

        btn171.setBackground(new java.awt.Color(102, 153, 255));
        btn171.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn171.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn171.setMaximumSize(new java.awt.Dimension(20, 20));
        btn171.setPreferredSize(new java.awt.Dimension(20, 20));
        btn171.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn171MouseClicked(evt);
            }
        });
        jPanel5.add(btn171);

        btn172.setBackground(new java.awt.Color(102, 153, 255));
        btn172.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn172.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn172.setMaximumSize(new java.awt.Dimension(20, 20));
        btn172.setPreferredSize(new java.awt.Dimension(20, 20));
        btn172.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn172MouseClicked(evt);
            }
        });
        jPanel5.add(btn172);

        btn173.setBackground(new java.awt.Color(102, 153, 255));
        btn173.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn173.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn173.setMaximumSize(new java.awt.Dimension(20, 20));
        btn173.setPreferredSize(new java.awt.Dimension(20, 20));
        btn173.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn173MouseClicked(evt);
            }
        });
        jPanel5.add(btn173);

        btn174.setBackground(new java.awt.Color(102, 153, 255));
        btn174.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn174.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn174.setMaximumSize(new java.awt.Dimension(20, 20));
        btn174.setPreferredSize(new java.awt.Dimension(20, 20));
        btn174.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn174MouseClicked(evt);
            }
        });
        jPanel5.add(btn174);

        btn175.setBackground(new java.awt.Color(102, 153, 255));
        btn175.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn175.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn175.setMaximumSize(new java.awt.Dimension(20, 20));
        btn175.setPreferredSize(new java.awt.Dimension(20, 20));
        btn175.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn175MouseClicked(evt);
            }
        });
        jPanel5.add(btn175);

        btn176.setBackground(new java.awt.Color(102, 153, 255));
        btn176.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn176.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn176.setMaximumSize(new java.awt.Dimension(20, 20));
        btn176.setPreferredSize(new java.awt.Dimension(20, 20));
        btn176.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn176MouseClicked(evt);
            }
        });
        jPanel5.add(btn176);

        btn177.setBackground(new java.awt.Color(102, 153, 255));
        btn177.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn177.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn177.setMaximumSize(new java.awt.Dimension(20, 20));
        btn177.setPreferredSize(new java.awt.Dimension(20, 20));
        btn177.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn177MouseClicked(evt);
            }
        });
        jPanel5.add(btn177);

        btn178.setBackground(new java.awt.Color(102, 153, 255));
        btn178.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn178.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn178.setMaximumSize(new java.awt.Dimension(20, 20));
        btn178.setPreferredSize(new java.awt.Dimension(20, 20));
        btn178.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn178MouseClicked(evt);
            }
        });
        jPanel5.add(btn178);

        btn179.setBackground(new java.awt.Color(102, 153, 255));
        btn179.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn179.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn179.setMaximumSize(new java.awt.Dimension(20, 20));
        btn179.setPreferredSize(new java.awt.Dimension(20, 20));
        btn179.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn179MouseClicked(evt);
            }
        });
        jPanel5.add(btn179);

        btn180.setBackground(new java.awt.Color(102, 153, 255));
        btn180.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn180.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn180.setMaximumSize(new java.awt.Dimension(20, 20));
        btn180.setPreferredSize(new java.awt.Dimension(20, 20));
        btn180.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn180MouseClicked(evt);
            }
        });
        jPanel5.add(btn180);

        btn181.setBackground(new java.awt.Color(102, 153, 255));
        btn181.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn181.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn181.setMaximumSize(new java.awt.Dimension(20, 20));
        btn181.setPreferredSize(new java.awt.Dimension(20, 20));
        btn181.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn181MouseClicked(evt);
            }
        });
        jPanel5.add(btn181);

        btn182.setBackground(new java.awt.Color(102, 153, 255));
        btn182.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn182.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn182.setMaximumSize(new java.awt.Dimension(20, 20));
        btn182.setPreferredSize(new java.awt.Dimension(20, 20));
        btn182.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn182MouseClicked(evt);
            }
        });
        jPanel5.add(btn182);

        btn183.setBackground(new java.awt.Color(102, 153, 255));
        btn183.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn183.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn183.setMaximumSize(new java.awt.Dimension(20, 20));
        btn183.setPreferredSize(new java.awt.Dimension(20, 20));
        btn183.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn183MouseClicked(evt);
            }
        });
        jPanel5.add(btn183);

        btn184.setBackground(new java.awt.Color(102, 153, 255));
        btn184.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn184.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn184.setMaximumSize(new java.awt.Dimension(20, 20));
        btn184.setPreferredSize(new java.awt.Dimension(20, 20));
        btn184.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn184MouseClicked(evt);
            }
        });
        jPanel5.add(btn184);

        btn185.setBackground(new java.awt.Color(102, 153, 255));
        btn185.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn185.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn185.setMaximumSize(new java.awt.Dimension(20, 20));
        btn185.setPreferredSize(new java.awt.Dimension(20, 20));
        btn185.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn185MouseClicked(evt);
            }
        });
        jPanel5.add(btn185);

        btn186.setBackground(new java.awt.Color(102, 153, 255));
        btn186.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn186.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn186.setMaximumSize(new java.awt.Dimension(20, 20));
        btn186.setPreferredSize(new java.awt.Dimension(20, 20));
        btn186.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn186MouseClicked(evt);
            }
        });
        jPanel5.add(btn186);

        btn187.setBackground(new java.awt.Color(102, 153, 255));
        btn187.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn187.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn187.setMaximumSize(new java.awt.Dimension(20, 20));
        btn187.setPreferredSize(new java.awt.Dimension(20, 20));
        btn187.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn187MouseClicked(evt);
            }
        });
        jPanel5.add(btn187);

        btn188.setBackground(new java.awt.Color(102, 153, 255));
        btn188.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn188.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn188.setMaximumSize(new java.awt.Dimension(20, 20));
        btn188.setPreferredSize(new java.awt.Dimension(20, 20));
        btn188.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn188MouseClicked(evt);
            }
        });
        jPanel5.add(btn188);

        btn189.setBackground(new java.awt.Color(102, 153, 255));
        btn189.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn189.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn189.setMaximumSize(new java.awt.Dimension(20, 20));
        btn189.setPreferredSize(new java.awt.Dimension(20, 20));
        btn189.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn189MouseClicked(evt);
            }
        });
        jPanel5.add(btn189);

        btn190.setBackground(new java.awt.Color(102, 153, 255));
        btn190.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn190.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn190.setMaximumSize(new java.awt.Dimension(20, 20));
        btn190.setPreferredSize(new java.awt.Dimension(20, 20));
        btn190.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn190MouseClicked(evt);
            }
        });
        jPanel5.add(btn190);

        btn191.setBackground(new java.awt.Color(102, 153, 255));
        btn191.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn191.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn191.setMaximumSize(new java.awt.Dimension(20, 20));
        btn191.setPreferredSize(new java.awt.Dimension(20, 20));
        btn191.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn191MouseClicked(evt);
            }
        });
        jPanel5.add(btn191);

        btn192.setBackground(new java.awt.Color(102, 153, 255));
        btn192.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn192.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn192.setMaximumSize(new java.awt.Dimension(20, 20));
        btn192.setPreferredSize(new java.awt.Dimension(20, 20));
        btn192.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn192MouseClicked(evt);
            }
        });
        jPanel5.add(btn192);

        btn193.setBackground(new java.awt.Color(102, 153, 255));
        btn193.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn193.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn193.setMaximumSize(new java.awt.Dimension(20, 20));
        btn193.setPreferredSize(new java.awt.Dimension(20, 20));
        btn193.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn193MouseClicked(evt);
            }
        });
        jPanel5.add(btn193);

        btn194.setBackground(new java.awt.Color(102, 153, 255));
        btn194.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn194.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn194.setMaximumSize(new java.awt.Dimension(20, 20));
        btn194.setPreferredSize(new java.awt.Dimension(20, 20));
        btn194.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn194MouseClicked(evt);
            }
        });
        jPanel5.add(btn194);

        btn195.setBackground(new java.awt.Color(102, 153, 255));
        btn195.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn195.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn195.setMaximumSize(new java.awt.Dimension(20, 20));
        btn195.setPreferredSize(new java.awt.Dimension(20, 20));
        btn195.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn195MouseClicked(evt);
            }
        });
        jPanel5.add(btn195);

        btn196.setBackground(new java.awt.Color(102, 153, 255));
        btn196.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn196.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn196.setMaximumSize(new java.awt.Dimension(20, 20));
        btn196.setPreferredSize(new java.awt.Dimension(20, 20));
        btn196.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn196MouseClicked(evt);
            }
        });
        jPanel5.add(btn196);

        btn197.setBackground(new java.awt.Color(102, 153, 255));
        btn197.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn197.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn197.setMaximumSize(new java.awt.Dimension(20, 20));
        btn197.setPreferredSize(new java.awt.Dimension(20, 20));
        btn197.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn197MouseClicked(evt);
            }
        });
        jPanel5.add(btn197);

        btn198.setBackground(new java.awt.Color(102, 153, 255));
        btn198.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn198.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn198.setMaximumSize(new java.awt.Dimension(20, 20));
        btn198.setPreferredSize(new java.awt.Dimension(20, 20));
        btn198.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn198MouseClicked(evt);
            }
        });
        jPanel5.add(btn198);

        btn199.setBackground(new java.awt.Color(102, 153, 255));
        btn199.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn199.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn199.setMaximumSize(new java.awt.Dimension(20, 20));
        btn199.setPreferredSize(new java.awt.Dimension(20, 20));
        btn199.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn199MouseClicked(evt);
            }
        });
        jPanel5.add(btn199);

        btn200.setBackground(new java.awt.Color(102, 153, 255));
        btn200.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn200.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn200.setMaximumSize(new java.awt.Dimension(20, 20));
        btn200.setPreferredSize(new java.awt.Dimension(20, 20));
        btn200.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn200MouseClicked(evt);
            }
        });
        jPanel5.add(btn200);

        btn201.setBackground(new java.awt.Color(102, 153, 255));
        btn201.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn201.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn201.setMaximumSize(new java.awt.Dimension(20, 20));
        btn201.setPreferredSize(new java.awt.Dimension(20, 20));
        btn201.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn201MouseClicked(evt);
            }
        });
        jPanel5.add(btn201);

        btn202.setBackground(new java.awt.Color(102, 153, 255));
        btn202.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn202.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn202.setMaximumSize(new java.awt.Dimension(20, 20));
        btn202.setPreferredSize(new java.awt.Dimension(20, 20));
        btn202.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn202MouseClicked(evt);
            }
        });
        jPanel5.add(btn202);

        btn203.setBackground(new java.awt.Color(102, 153, 255));
        btn203.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn203.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn203.setMaximumSize(new java.awt.Dimension(20, 20));
        btn203.setPreferredSize(new java.awt.Dimension(20, 20));
        btn203.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn203MouseClicked(evt);
            }
        });
        jPanel5.add(btn203);

        btn204.setBackground(new java.awt.Color(102, 153, 255));
        btn204.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn204.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn204.setMaximumSize(new java.awt.Dimension(20, 20));
        btn204.setPreferredSize(new java.awt.Dimension(20, 20));
        btn204.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn204MouseClicked(evt);
            }
        });
        jPanel5.add(btn204);

        btn205.setBackground(new java.awt.Color(102, 153, 255));
        btn205.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn205.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn205.setMaximumSize(new java.awt.Dimension(20, 20));
        btn205.setPreferredSize(new java.awt.Dimension(20, 20));
        btn205.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn205MouseClicked(evt);
            }
        });
        jPanel5.add(btn205);

        btn206.setBackground(new java.awt.Color(102, 153, 255));
        btn206.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn206.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn206.setMaximumSize(new java.awt.Dimension(20, 20));
        btn206.setPreferredSize(new java.awt.Dimension(20, 20));
        btn206.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn206MouseClicked(evt);
            }
        });
        jPanel5.add(btn206);

        btn207.setBackground(new java.awt.Color(102, 153, 255));
        btn207.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn207.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn207.setMaximumSize(new java.awt.Dimension(20, 20));
        btn207.setPreferredSize(new java.awt.Dimension(20, 20));
        btn207.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn207MouseClicked(evt);
            }
        });
        jPanel5.add(btn207);

        btn208.setBackground(new java.awt.Color(102, 153, 255));
        btn208.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn208.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn208.setMaximumSize(new java.awt.Dimension(20, 20));
        btn208.setPreferredSize(new java.awt.Dimension(20, 20));
        btn208.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn208MouseClicked(evt);
            }
        });
        jPanel5.add(btn208);

        btn209.setBackground(new java.awt.Color(102, 153, 255));
        btn209.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn209.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn209.setMaximumSize(new java.awt.Dimension(20, 20));
        btn209.setPreferredSize(new java.awt.Dimension(20, 20));
        btn209.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn209MouseClicked(evt);
            }
        });
        jPanel5.add(btn209);

        btn210.setBackground(new java.awt.Color(102, 153, 255));
        btn210.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn210.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn210.setMaximumSize(new java.awt.Dimension(20, 20));
        btn210.setPreferredSize(new java.awt.Dimension(20, 20));
        btn210.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn210MouseClicked(evt);
            }
        });
        jPanel5.add(btn210);

        btn211.setBackground(new java.awt.Color(102, 153, 255));
        btn211.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn211.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn211.setMaximumSize(new java.awt.Dimension(20, 20));
        btn211.setPreferredSize(new java.awt.Dimension(20, 20));
        btn211.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn211MouseClicked(evt);
            }
        });
        jPanel5.add(btn211);

        btn212.setBackground(new java.awt.Color(102, 153, 255));
        btn212.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn212.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn212.setMaximumSize(new java.awt.Dimension(20, 20));
        btn212.setPreferredSize(new java.awt.Dimension(20, 20));
        btn212.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn212MouseClicked(evt);
            }
        });
        jPanel5.add(btn212);

        btn213.setBackground(new java.awt.Color(102, 153, 255));
        btn213.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn213.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn213.setMaximumSize(new java.awt.Dimension(20, 20));
        btn213.setPreferredSize(new java.awt.Dimension(20, 20));
        btn213.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn213MouseClicked(evt);
            }
        });
        jPanel5.add(btn213);

        btn214.setBackground(new java.awt.Color(102, 153, 255));
        btn214.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn214.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn214.setMaximumSize(new java.awt.Dimension(20, 20));
        btn214.setPreferredSize(new java.awt.Dimension(20, 20));
        btn214.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn214MouseClicked(evt);
            }
        });
        jPanel5.add(btn214);

        btn215.setBackground(new java.awt.Color(102, 153, 255));
        btn215.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn215.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn215.setMaximumSize(new java.awt.Dimension(20, 20));
        btn215.setPreferredSize(new java.awt.Dimension(20, 20));
        btn215.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn215MouseClicked(evt);
            }
        });
        jPanel5.add(btn215);

        btn216.setBackground(new java.awt.Color(102, 153, 255));
        btn216.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn216.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn216.setMaximumSize(new java.awt.Dimension(20, 20));
        btn216.setPreferredSize(new java.awt.Dimension(20, 20));
        btn216.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn216MouseClicked(evt);
            }
        });
        jPanel5.add(btn216);

        btn217.setBackground(new java.awt.Color(102, 153, 255));
        btn217.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn217.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn217.setMaximumSize(new java.awt.Dimension(20, 20));
        btn217.setPreferredSize(new java.awt.Dimension(20, 20));
        btn217.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn217MouseClicked(evt);
            }
        });
        jPanel5.add(btn217);

        btn218.setBackground(new java.awt.Color(102, 153, 255));
        btn218.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn218.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn218.setMaximumSize(new java.awt.Dimension(20, 20));
        btn218.setPreferredSize(new java.awt.Dimension(20, 20));
        btn218.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn218MouseClicked(evt);
            }
        });
        jPanel5.add(btn218);

        btn219.setBackground(new java.awt.Color(102, 153, 255));
        btn219.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn219.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn219.setMaximumSize(new java.awt.Dimension(20, 20));
        btn219.setPreferredSize(new java.awt.Dimension(20, 20));
        btn219.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn219MouseClicked(evt);
            }
        });
        jPanel5.add(btn219);

        btn220.setBackground(new java.awt.Color(102, 153, 255));
        btn220.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn220.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn220.setMaximumSize(new java.awt.Dimension(20, 20));
        btn220.setPreferredSize(new java.awt.Dimension(20, 20));
        btn220.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn220MouseClicked(evt);
            }
        });
        jPanel5.add(btn220);

        btn221.setBackground(new java.awt.Color(102, 153, 255));
        btn221.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn221.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn221.setMaximumSize(new java.awt.Dimension(20, 20));
        btn221.setPreferredSize(new java.awt.Dimension(20, 20));
        btn221.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn221MouseClicked(evt);
            }
        });
        jPanel5.add(btn221);

        btn222.setBackground(new java.awt.Color(102, 153, 255));
        btn222.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn222.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn222.setMaximumSize(new java.awt.Dimension(20, 20));
        btn222.setPreferredSize(new java.awt.Dimension(20, 20));
        btn222.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn222MouseClicked(evt);
            }
        });
        jPanel5.add(btn222);

        btn223.setBackground(new java.awt.Color(102, 153, 255));
        btn223.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn223.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn223.setMaximumSize(new java.awt.Dimension(20, 20));
        btn223.setPreferredSize(new java.awt.Dimension(20, 20));
        btn223.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn223MouseClicked(evt);
            }
        });
        jPanel5.add(btn223);

        btn224.setBackground(new java.awt.Color(102, 153, 255));
        btn224.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn224.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn224.setMaximumSize(new java.awt.Dimension(20, 20));
        btn224.setPreferredSize(new java.awt.Dimension(20, 20));
        btn224.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn224MouseClicked(evt);
            }
        });
        jPanel5.add(btn224);

        btn225.setBackground(new java.awt.Color(102, 153, 255));
        btn225.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn225.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn225.setMaximumSize(new java.awt.Dimension(20, 20));
        btn225.setPreferredSize(new java.awt.Dimension(20, 20));
        btn225.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn225MouseClicked(evt);
            }
        });
        jPanel5.add(btn225);

        btn226.setBackground(new java.awt.Color(102, 153, 255));
        btn226.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn226.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn226.setMaximumSize(new java.awt.Dimension(20, 20));
        btn226.setPreferredSize(new java.awt.Dimension(20, 20));
        btn226.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn226MouseClicked(evt);
            }
        });
        jPanel5.add(btn226);

        btn227.setBackground(new java.awt.Color(102, 153, 255));
        btn227.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn227.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn227.setMaximumSize(new java.awt.Dimension(20, 20));
        btn227.setPreferredSize(new java.awt.Dimension(20, 20));
        btn227.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn227MouseClicked(evt);
            }
        });
        jPanel5.add(btn227);

        btn228.setBackground(new java.awt.Color(102, 153, 255));
        btn228.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn228.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn228.setMaximumSize(new java.awt.Dimension(20, 20));
        btn228.setPreferredSize(new java.awt.Dimension(20, 20));
        btn228.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn228MouseClicked(evt);
            }
        });
        jPanel5.add(btn228);

        btn229.setBackground(new java.awt.Color(102, 153, 255));
        btn229.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn229.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn229.setMaximumSize(new java.awt.Dimension(20, 20));
        btn229.setPreferredSize(new java.awt.Dimension(20, 20));
        btn229.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn229MouseClicked(evt);
            }
        });
        jPanel5.add(btn229);

        btn230.setBackground(new java.awt.Color(102, 153, 255));
        btn230.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn230.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn230.setMaximumSize(new java.awt.Dimension(20, 20));
        btn230.setPreferredSize(new java.awt.Dimension(20, 20));
        btn230.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn230MouseClicked(evt);
            }
        });
        jPanel5.add(btn230);

        btn231.setBackground(new java.awt.Color(102, 153, 255));
        btn231.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn231.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn231.setMaximumSize(new java.awt.Dimension(20, 20));
        btn231.setPreferredSize(new java.awt.Dimension(20, 20));
        btn231.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn231MouseClicked(evt);
            }
        });
        jPanel5.add(btn231);

        btn232.setBackground(new java.awt.Color(102, 153, 255));
        btn232.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn232.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn232.setMaximumSize(new java.awt.Dimension(20, 20));
        btn232.setPreferredSize(new java.awt.Dimension(20, 20));
        btn232.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn232MouseClicked(evt);
            }
        });
        jPanel5.add(btn232);

        btn233.setBackground(new java.awt.Color(102, 153, 255));
        btn233.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn233.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn233.setMaximumSize(new java.awt.Dimension(20, 20));
        btn233.setPreferredSize(new java.awt.Dimension(20, 20));
        btn233.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn233MouseClicked(evt);
            }
        });
        jPanel5.add(btn233);

        btn234.setBackground(new java.awt.Color(102, 153, 255));
        btn234.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn234.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn234.setMaximumSize(new java.awt.Dimension(20, 20));
        btn234.setPreferredSize(new java.awt.Dimension(20, 20));
        btn234.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn234MouseClicked(evt);
            }
        });
        jPanel5.add(btn234);

        btn235.setBackground(new java.awt.Color(102, 153, 255));
        btn235.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn235.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn235.setMaximumSize(new java.awt.Dimension(20, 20));
        btn235.setPreferredSize(new java.awt.Dimension(20, 20));
        btn235.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn235MouseClicked(evt);
            }
        });
        jPanel5.add(btn235);

        btn236.setBackground(new java.awt.Color(102, 153, 255));
        btn236.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn236.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn236.setMaximumSize(new java.awt.Dimension(20, 20));
        btn236.setPreferredSize(new java.awt.Dimension(20, 20));
        btn236.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn236MouseClicked(evt);
            }
        });
        jPanel5.add(btn236);

        btn237.setBackground(new java.awt.Color(102, 153, 255));
        btn237.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn237.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn237.setMaximumSize(new java.awt.Dimension(20, 20));
        btn237.setPreferredSize(new java.awt.Dimension(20, 20));
        btn237.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn237MouseClicked(evt);
            }
        });
        jPanel5.add(btn237);

        btn238.setBackground(new java.awt.Color(102, 153, 255));
        btn238.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn238.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn238.setMaximumSize(new java.awt.Dimension(20, 20));
        btn238.setPreferredSize(new java.awt.Dimension(20, 20));
        btn238.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn238MouseClicked(evt);
            }
        });
        jPanel5.add(btn238);

        btn239.setBackground(new java.awt.Color(102, 153, 255));
        btn239.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn239.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn239.setMaximumSize(new java.awt.Dimension(20, 20));
        btn239.setPreferredSize(new java.awt.Dimension(20, 20));
        btn239.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn239MouseClicked(evt);
            }
        });
        jPanel5.add(btn239);

        btn240.setBackground(new java.awt.Color(102, 153, 255));
        btn240.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn240.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn240.setMaximumSize(new java.awt.Dimension(20, 20));
        btn240.setPreferredSize(new java.awt.Dimension(20, 20));
        btn240.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn240MouseClicked(evt);
            }
        });
        jPanel5.add(btn240);

        btn241.setBackground(new java.awt.Color(102, 153, 255));
        btn241.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn241.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn241.setMaximumSize(new java.awt.Dimension(20, 20));
        btn241.setPreferredSize(new java.awt.Dimension(20, 20));
        btn241.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn241MouseClicked(evt);
            }
        });
        jPanel5.add(btn241);

        btn242.setBackground(new java.awt.Color(102, 153, 255));
        btn242.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn242.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn242.setMaximumSize(new java.awt.Dimension(20, 20));
        btn242.setPreferredSize(new java.awt.Dimension(20, 20));
        btn242.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn242MouseClicked(evt);
            }
        });
        jPanel5.add(btn242);

        btn243.setBackground(new java.awt.Color(102, 153, 255));
        btn243.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn243.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn243.setMaximumSize(new java.awt.Dimension(20, 20));
        btn243.setPreferredSize(new java.awt.Dimension(20, 20));
        btn243.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn243MouseClicked(evt);
            }
        });
        jPanel5.add(btn243);

        btn244.setBackground(new java.awt.Color(102, 153, 255));
        btn244.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn244.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn244.setMaximumSize(new java.awt.Dimension(20, 20));
        btn244.setPreferredSize(new java.awt.Dimension(20, 20));
        btn244.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn244MouseClicked(evt);
            }
        });
        jPanel5.add(btn244);

        btn245.setBackground(new java.awt.Color(102, 153, 255));
        btn245.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn245.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn245.setMaximumSize(new java.awt.Dimension(20, 20));
        btn245.setPreferredSize(new java.awt.Dimension(20, 20));
        btn245.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn245MouseClicked(evt);
            }
        });
        jPanel5.add(btn245);

        btn246.setBackground(new java.awt.Color(102, 153, 255));
        btn246.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn246.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn246.setMaximumSize(new java.awt.Dimension(20, 20));
        btn246.setPreferredSize(new java.awt.Dimension(20, 20));
        btn246.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn246MouseClicked(evt);
            }
        });
        jPanel5.add(btn246);

        btn247.setBackground(new java.awt.Color(102, 153, 255));
        btn247.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn247.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn247.setMaximumSize(new java.awt.Dimension(20, 20));
        btn247.setPreferredSize(new java.awt.Dimension(20, 20));
        btn247.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn247MouseClicked(evt);
            }
        });
        jPanel5.add(btn247);

        btn248.setBackground(new java.awt.Color(102, 153, 255));
        btn248.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn248.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn248.setMaximumSize(new java.awt.Dimension(20, 20));
        btn248.setPreferredSize(new java.awt.Dimension(20, 20));
        btn248.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn248MouseClicked(evt);
            }
        });
        jPanel5.add(btn248);

        btn249.setBackground(new java.awt.Color(102, 153, 255));
        btn249.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn249.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn249.setMaximumSize(new java.awt.Dimension(20, 20));
        btn249.setPreferredSize(new java.awt.Dimension(20, 20));
        btn249.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn249MouseClicked(evt);
            }
        });
        jPanel5.add(btn249);

        btn250.setBackground(new java.awt.Color(102, 153, 255));
        btn250.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn250.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn250.setMaximumSize(new java.awt.Dimension(20, 20));
        btn250.setPreferredSize(new java.awt.Dimension(20, 20));
        btn250.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn250MouseClicked(evt);
            }
        });
        jPanel5.add(btn250);

        btn251.setBackground(new java.awt.Color(102, 153, 255));
        btn251.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn251.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn251.setMaximumSize(new java.awt.Dimension(20, 20));
        btn251.setPreferredSize(new java.awt.Dimension(20, 20));
        btn251.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn251MouseClicked(evt);
            }
        });
        jPanel5.add(btn251);

        btn252.setBackground(new java.awt.Color(102, 153, 255));
        btn252.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn252.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn252.setMaximumSize(new java.awt.Dimension(20, 20));
        btn252.setPreferredSize(new java.awt.Dimension(20, 20));
        btn252.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn252MouseClicked(evt);
            }
        });
        jPanel5.add(btn252);

        btn253.setBackground(new java.awt.Color(102, 153, 255));
        btn253.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn253.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn253.setMaximumSize(new java.awt.Dimension(20, 20));
        btn253.setPreferredSize(new java.awt.Dimension(20, 20));
        btn253.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn253MouseClicked(evt);
            }
        });
        jPanel5.add(btn253);

        btn254.setBackground(new java.awt.Color(102, 153, 255));
        btn254.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn254.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn254.setMaximumSize(new java.awt.Dimension(20, 20));
        btn254.setPreferredSize(new java.awt.Dimension(20, 20));
        btn254.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn254MouseClicked(evt);
            }
        });
        jPanel5.add(btn254);

        btn255.setBackground(new java.awt.Color(102, 153, 255));
        btn255.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn255.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn255.setMaximumSize(new java.awt.Dimension(20, 20));
        btn255.setPreferredSize(new java.awt.Dimension(20, 20));
        btn255.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn255MouseClicked(evt);
            }
        });
        jPanel5.add(btn255);

        btn256.setBackground(new java.awt.Color(102, 153, 255));
        btn256.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn256.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn256.setMaximumSize(new java.awt.Dimension(20, 20));
        btn256.setPreferredSize(new java.awt.Dimension(20, 20));
        btn256.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn256MouseClicked(evt);
            }
        });
        jPanel5.add(btn256);

        btn257.setBackground(new java.awt.Color(102, 153, 255));
        btn257.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn257.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn257.setMaximumSize(new java.awt.Dimension(20, 20));
        btn257.setPreferredSize(new java.awt.Dimension(20, 20));
        btn257.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn257MouseClicked(evt);
            }
        });
        jPanel5.add(btn257);

        btn258.setBackground(new java.awt.Color(102, 153, 255));
        btn258.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn258.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn258.setMaximumSize(new java.awt.Dimension(20, 20));
        btn258.setPreferredSize(new java.awt.Dimension(20, 20));
        btn258.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn258MouseClicked(evt);
            }
        });
        jPanel5.add(btn258);

        btn259.setBackground(new java.awt.Color(102, 153, 255));
        btn259.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn259.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn259.setMaximumSize(new java.awt.Dimension(20, 20));
        btn259.setPreferredSize(new java.awt.Dimension(20, 20));
        btn259.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn259MouseClicked(evt);
            }
        });
        jPanel5.add(btn259);

        btn260.setBackground(new java.awt.Color(102, 153, 255));
        btn260.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn260.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn260.setMaximumSize(new java.awt.Dimension(20, 20));
        btn260.setPreferredSize(new java.awt.Dimension(20, 20));
        btn260.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn260MouseClicked(evt);
            }
        });
        jPanel5.add(btn260);

        btn261.setBackground(new java.awt.Color(102, 153, 255));
        btn261.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn261.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn261.setMaximumSize(new java.awt.Dimension(20, 20));
        btn261.setPreferredSize(new java.awt.Dimension(20, 20));
        btn261.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn261MouseClicked(evt);
            }
        });
        jPanel5.add(btn261);

        btn262.setBackground(new java.awt.Color(102, 153, 255));
        btn262.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn262.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn262.setMaximumSize(new java.awt.Dimension(20, 20));
        btn262.setPreferredSize(new java.awt.Dimension(20, 20));
        btn262.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn262MouseClicked(evt);
            }
        });
        jPanel5.add(btn262);

        btn263.setBackground(new java.awt.Color(102, 153, 255));
        btn263.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn263.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn263.setMaximumSize(new java.awt.Dimension(20, 20));
        btn263.setPreferredSize(new java.awt.Dimension(20, 20));
        btn263.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn263MouseClicked(evt);
            }
        });
        jPanel5.add(btn263);

        btn264.setBackground(new java.awt.Color(102, 153, 255));
        btn264.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn264.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn264.setMaximumSize(new java.awt.Dimension(20, 20));
        btn264.setPreferredSize(new java.awt.Dimension(20, 20));
        btn264.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn264MouseClicked(evt);
            }
        });
        jPanel5.add(btn264);

        btn265.setBackground(new java.awt.Color(102, 153, 255));
        btn265.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn265.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn265.setMaximumSize(new java.awt.Dimension(20, 20));
        btn265.setPreferredSize(new java.awt.Dimension(20, 20));
        btn265.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn265MouseClicked(evt);
            }
        });
        jPanel5.add(btn265);

        btn266.setBackground(new java.awt.Color(102, 153, 255));
        btn266.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn266.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn266.setMaximumSize(new java.awt.Dimension(20, 20));
        btn266.setPreferredSize(new java.awt.Dimension(20, 20));
        btn266.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn266MouseClicked(evt);
            }
        });
        jPanel5.add(btn266);

        btn267.setBackground(new java.awt.Color(102, 153, 255));
        btn267.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn267.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn267.setMaximumSize(new java.awt.Dimension(20, 20));
        btn267.setPreferredSize(new java.awt.Dimension(20, 20));
        btn267.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn267MouseClicked(evt);
            }
        });
        jPanel5.add(btn267);

        btn268.setBackground(new java.awt.Color(102, 153, 255));
        btn268.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn268.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn268.setMaximumSize(new java.awt.Dimension(20, 20));
        btn268.setPreferredSize(new java.awt.Dimension(20, 20));
        btn268.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn268MouseClicked(evt);
            }
        });
        jPanel5.add(btn268);

        btn269.setBackground(new java.awt.Color(102, 153, 255));
        btn269.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn269.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn269.setMaximumSize(new java.awt.Dimension(20, 20));
        btn269.setPreferredSize(new java.awt.Dimension(20, 20));
        btn269.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn269MouseClicked(evt);
            }
        });
        jPanel5.add(btn269);

        btn270.setBackground(new java.awt.Color(102, 153, 255));
        btn270.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn270.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn270.setMaximumSize(new java.awt.Dimension(20, 20));
        btn270.setPreferredSize(new java.awt.Dimension(20, 20));
        btn270.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn270MouseClicked(evt);
            }
        });
        jPanel5.add(btn270);

        btn271.setBackground(new java.awt.Color(102, 153, 255));
        btn271.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn271.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn271.setMaximumSize(new java.awt.Dimension(20, 20));
        btn271.setPreferredSize(new java.awt.Dimension(20, 20));
        btn271.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn271MouseClicked(evt);
            }
        });
        jPanel5.add(btn271);

        btn272.setBackground(new java.awt.Color(102, 153, 255));
        btn272.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn272.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn272.setMaximumSize(new java.awt.Dimension(20, 20));
        btn272.setPreferredSize(new java.awt.Dimension(20, 20));
        btn272.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn272MouseClicked(evt);
            }
        });
        jPanel5.add(btn272);

        btn273.setBackground(new java.awt.Color(102, 153, 255));
        btn273.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn273.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn273.setMaximumSize(new java.awt.Dimension(20, 20));
        btn273.setPreferredSize(new java.awt.Dimension(20, 20));
        btn273.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn273MouseClicked(evt);
            }
        });
        jPanel5.add(btn273);

        btn274.setBackground(new java.awt.Color(102, 153, 255));
        btn274.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn274.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn274.setMaximumSize(new java.awt.Dimension(20, 20));
        btn274.setPreferredSize(new java.awt.Dimension(20, 20));
        btn274.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn274MouseClicked(evt);
            }
        });
        jPanel5.add(btn274);

        btn275.setBackground(new java.awt.Color(102, 153, 255));
        btn275.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn275.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn275.setMaximumSize(new java.awt.Dimension(20, 20));
        btn275.setPreferredSize(new java.awt.Dimension(20, 20));
        btn275.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn275MouseClicked(evt);
            }
        });
        jPanel5.add(btn275);

        btn276.setBackground(new java.awt.Color(102, 153, 255));
        btn276.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn276.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn276.setMaximumSize(new java.awt.Dimension(20, 20));
        btn276.setPreferredSize(new java.awt.Dimension(20, 20));
        btn276.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn276MouseClicked(evt);
            }
        });
        jPanel5.add(btn276);

        btn277.setBackground(new java.awt.Color(102, 153, 255));
        btn277.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn277.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn277.setMaximumSize(new java.awt.Dimension(20, 20));
        btn277.setPreferredSize(new java.awt.Dimension(20, 20));
        btn277.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn277MouseClicked(evt);
            }
        });
        jPanel5.add(btn277);

        btn278.setBackground(new java.awt.Color(102, 153, 255));
        btn278.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn278.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn278.setMaximumSize(new java.awt.Dimension(20, 20));
        btn278.setPreferredSize(new java.awt.Dimension(20, 20));
        btn278.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn278MouseClicked(evt);
            }
        });
        jPanel5.add(btn278);

        btn279.setBackground(new java.awt.Color(102, 153, 255));
        btn279.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn279.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn279.setMaximumSize(new java.awt.Dimension(20, 20));
        btn279.setPreferredSize(new java.awt.Dimension(20, 20));
        btn279.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn279MouseClicked(evt);
            }
        });
        jPanel5.add(btn279);

        btn280.setBackground(new java.awt.Color(102, 153, 255));
        btn280.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn280.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn280.setMaximumSize(new java.awt.Dimension(20, 20));
        btn280.setPreferredSize(new java.awt.Dimension(20, 20));
        btn280.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn280MouseClicked(evt);
            }
        });
        jPanel5.add(btn280);

        btn281.setBackground(new java.awt.Color(102, 153, 255));
        btn281.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn281.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn281.setMaximumSize(new java.awt.Dimension(20, 20));
        btn281.setPreferredSize(new java.awt.Dimension(20, 20));
        btn281.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn281MouseClicked(evt);
            }
        });
        jPanel5.add(btn281);

        btn282.setBackground(new java.awt.Color(102, 153, 255));
        btn282.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn282.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn282.setMaximumSize(new java.awt.Dimension(20, 20));
        btn282.setPreferredSize(new java.awt.Dimension(20, 20));
        btn282.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn282MouseClicked(evt);
            }
        });
        jPanel5.add(btn282);

        btn283.setBackground(new java.awt.Color(102, 153, 255));
        btn283.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn283.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn283.setMaximumSize(new java.awt.Dimension(20, 20));
        btn283.setPreferredSize(new java.awt.Dimension(20, 20));
        btn283.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn283MouseClicked(evt);
            }
        });
        jPanel5.add(btn283);

        btn284.setBackground(new java.awt.Color(102, 153, 255));
        btn284.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn284.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn284.setMaximumSize(new java.awt.Dimension(20, 20));
        btn284.setPreferredSize(new java.awt.Dimension(20, 20));
        btn284.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn284MouseClicked(evt);
            }
        });
        jPanel5.add(btn284);

        btn285.setBackground(new java.awt.Color(102, 153, 255));
        btn285.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn285.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn285.setMaximumSize(new java.awt.Dimension(20, 20));
        btn285.setPreferredSize(new java.awt.Dimension(20, 20));
        btn285.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn285MouseClicked(evt);
            }
        });
        jPanel5.add(btn285);

        btn286.setBackground(new java.awt.Color(102, 153, 255));
        btn286.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn286.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn286.setMaximumSize(new java.awt.Dimension(20, 20));
        btn286.setPreferredSize(new java.awt.Dimension(20, 20));
        btn286.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn286MouseClicked(evt);
            }
        });
        jPanel5.add(btn286);

        btn287.setBackground(new java.awt.Color(102, 153, 255));
        btn287.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn287.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn287.setMaximumSize(new java.awt.Dimension(20, 20));
        btn287.setPreferredSize(new java.awt.Dimension(20, 20));
        btn287.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn287MouseClicked(evt);
            }
        });
        jPanel5.add(btn287);

        btn288.setBackground(new java.awt.Color(102, 153, 255));
        btn288.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn288.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn288.setMaximumSize(new java.awt.Dimension(20, 20));
        btn288.setPreferredSize(new java.awt.Dimension(20, 20));
        btn288.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn288MouseClicked(evt);
            }
        });
        jPanel5.add(btn288);

        btn289.setBackground(new java.awt.Color(102, 153, 255));
        btn289.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn289.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn289.setMaximumSize(new java.awt.Dimension(20, 20));
        btn289.setPreferredSize(new java.awt.Dimension(20, 20));
        btn289.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn289MouseClicked(evt);
            }
        });
        jPanel5.add(btn289);

        btn290.setBackground(new java.awt.Color(102, 153, 255));
        btn290.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn290.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn290.setMaximumSize(new java.awt.Dimension(20, 20));
        btn290.setPreferredSize(new java.awt.Dimension(20, 20));
        btn290.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn290MouseClicked(evt);
            }
        });
        jPanel5.add(btn290);

        btn291.setBackground(new java.awt.Color(102, 153, 255));
        btn291.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn291.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn291.setMaximumSize(new java.awt.Dimension(20, 20));
        btn291.setPreferredSize(new java.awt.Dimension(20, 20));
        btn291.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn291MouseClicked(evt);
            }
        });
        jPanel5.add(btn291);

        btn292.setBackground(new java.awt.Color(102, 153, 255));
        btn292.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn292.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn292.setMaximumSize(new java.awt.Dimension(20, 20));
        btn292.setPreferredSize(new java.awt.Dimension(20, 20));
        btn292.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn292MouseClicked(evt);
            }
        });
        jPanel5.add(btn292);

        btn293.setBackground(new java.awt.Color(102, 153, 255));
        btn293.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn293.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn293.setMaximumSize(new java.awt.Dimension(20, 20));
        btn293.setPreferredSize(new java.awt.Dimension(20, 20));
        btn293.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn293MouseClicked(evt);
            }
        });
        jPanel5.add(btn293);

        btn294.setBackground(new java.awt.Color(102, 153, 255));
        btn294.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn294.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn294.setMaximumSize(new java.awt.Dimension(20, 20));
        btn294.setPreferredSize(new java.awt.Dimension(20, 20));
        btn294.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn294MouseClicked(evt);
            }
        });
        jPanel5.add(btn294);

        btn295.setBackground(new java.awt.Color(102, 153, 255));
        btn295.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn295.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn295.setMaximumSize(new java.awt.Dimension(20, 20));
        btn295.setPreferredSize(new java.awt.Dimension(20, 20));
        btn295.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn295MouseClicked(evt);
            }
        });
        jPanel5.add(btn295);

        btn296.setBackground(new java.awt.Color(102, 153, 255));
        btn296.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn296.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn296.setMaximumSize(new java.awt.Dimension(20, 20));
        btn296.setPreferredSize(new java.awt.Dimension(20, 20));
        btn296.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn296MouseClicked(evt);
            }
        });
        jPanel5.add(btn296);

        btn297.setBackground(new java.awt.Color(102, 153, 255));
        btn297.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn297.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn297.setMaximumSize(new java.awt.Dimension(20, 20));
        btn297.setPreferredSize(new java.awt.Dimension(20, 20));
        btn297.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn297MouseClicked(evt);
            }
        });
        jPanel5.add(btn297);

        btn298.setBackground(new java.awt.Color(102, 153, 255));
        btn298.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn298.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn298.setMaximumSize(new java.awt.Dimension(20, 20));
        btn298.setPreferredSize(new java.awt.Dimension(20, 20));
        btn298.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn298MouseClicked(evt);
            }
        });
        jPanel5.add(btn298);

        btn299.setBackground(new java.awt.Color(102, 153, 255));
        btn299.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn299.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn299.setMaximumSize(new java.awt.Dimension(20, 20));
        btn299.setPreferredSize(new java.awt.Dimension(20, 20));
        btn299.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn299MouseClicked(evt);
            }
        });
        jPanel5.add(btn299);

        btn300.setBackground(new java.awt.Color(102, 153, 255));
        btn300.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn300.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn300.setMaximumSize(new java.awt.Dimension(20, 20));
        btn300.setPreferredSize(new java.awt.Dimension(20, 20));
        btn300.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn300MouseClicked(evt);
            }
        });
        jPanel5.add(btn300);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn1MouseClicked
        value = 1;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }

    }//GEN-LAST:event_btn1MouseClicked

    private void btn2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn2MouseClicked
        value = 2;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn2MouseClicked

    private void btn3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn3MouseClicked
        value = 3;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn3MouseClicked

    private void btn4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn4MouseClicked
        value = 4;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn4MouseClicked

    private void btn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn5MouseClicked
        value = 5;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn5MouseClicked

    private void btn6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn6MouseClicked
        value = 6;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn6MouseClicked

    private void btn7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn7MouseClicked
        value = 7;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn7MouseClicked

    private void btn8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn8MouseClicked
        value = 8;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn8MouseClicked

    private void btn9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn9MouseClicked
        value = 9;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn9MouseClicked

    private void btn10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn10MouseClicked
        value = 10;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn10MouseClicked

    private void btn11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn11MouseClicked
        value = 11;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn11MouseClicked

    private void btn12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn12MouseClicked
        value = 12;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn12MouseClicked

    private void btn13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn13MouseClicked
        value = 13;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn13MouseClicked

    private void btn14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn14MouseClicked
        value = 14;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn14MouseClicked

    private void btn15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn15MouseClicked
        value = 15;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn15MouseClicked

    private void btn16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn16MouseClicked
        value = 16;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn16MouseClicked

    private void btn17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn17MouseClicked
        value = 17;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn17MouseClicked

    private void btn18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn18MouseClicked
        value = 18;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn18MouseClicked

    private void btn19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn19MouseClicked
        value = 19;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn19MouseClicked

    private void btn20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn20MouseClicked
        value = 20;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn20MouseClicked

    private void btn21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn21MouseClicked
        value = 21;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn21MouseClicked

    private void btn22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn22MouseClicked
        value = 22;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn22MouseClicked

    private void btn23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn23MouseClicked
        value = 23;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn23MouseClicked

    private void btn24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn24MouseClicked
        value = 24;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn24MouseClicked

    private void btn25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn25MouseClicked
        value = 25;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn25MouseClicked

    private void btn26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn26MouseClicked
        value = 26;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn26MouseClicked

    private void btn27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn27MouseClicked
        value = 27;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn27MouseClicked

    private void btn28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn28MouseClicked
        value = 28;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn28MouseClicked

    private void btn29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn29MouseClicked
        value = 29;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn29MouseClicked

    private void btn30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn30MouseClicked
        value = 30;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn30MouseClicked

    private void btn31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn31MouseClicked
        value = 31;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn31MouseClicked

    private void btn32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn32MouseClicked
        value = 32;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn32MouseClicked

    private void btn33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn33MouseClicked
        value = 33;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn33MouseClicked

    private void btn34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn34MouseClicked
        value = 34;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn34MouseClicked

    private void btn35MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn35MouseClicked
        value = 35;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn35MouseClicked

    private void btn36MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn36MouseClicked
        value = 36;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn36MouseClicked

    private void btn37MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn37MouseClicked
        value = 37;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn37MouseClicked

    private void btn38MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn38MouseClicked
        value = 38;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn38MouseClicked

    private void btn39MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn39MouseClicked
        value = 39;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn39MouseClicked

    private void btn40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn40MouseClicked
        value = 40;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn40MouseClicked

    private void btn41MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn41MouseClicked
        value = 41;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn41MouseClicked

    private void btn42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn42MouseClicked
        value = 42;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn42MouseClicked

    private void btn43MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn43MouseClicked
        value = 43;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn43MouseClicked

    private void btn44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn44MouseClicked
        value = 44;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn44MouseClicked

    private void btn45MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn45MouseClicked
        value = 45;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn45MouseClicked

    private void btn46MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn46MouseClicked
        value = 46;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn46MouseClicked

    private void btn47MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn47MouseClicked
        value = 47;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn47MouseClicked

    private void btn48MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn48MouseClicked
        value = 48;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn48MouseClicked

    private void btn49MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn49MouseClicked
        value = 49;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn49MouseClicked

    private void btn50MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn50MouseClicked
        value = 50;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn50MouseClicked

    private void btn51MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn51MouseClicked
        value = 51;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn51MouseClicked

    private void btn52MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn52MouseClicked
        value = 52;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn52MouseClicked

    private void btn53MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn53MouseClicked
        value = 53;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn53MouseClicked

    private void btn54MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn54MouseClicked
        value = 54;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn54MouseClicked

    private void btn55MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn55MouseClicked
        value = 55;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn55MouseClicked

    private void btn56MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn56MouseClicked
        value = 56;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn56MouseClicked

    private void btn57MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn57MouseClicked
        value = 57;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn57MouseClicked

    private void btn58MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn58MouseClicked
        value = 58;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn58MouseClicked

    private void btn59MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn59MouseClicked
        value = 59;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn59MouseClicked

    private void btn60MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn60MouseClicked
        value = 60;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn60MouseClicked

    private void btn61MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn61MouseClicked
        value = 61;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn61MouseClicked

    private void btn62MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn62MouseClicked
        value = 62;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn62MouseClicked

    private void btn63MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn63MouseClicked
        value = 63;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn63MouseClicked

    private void btn64MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn64MouseClicked
        value = 64;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn64MouseClicked

    private void btn65MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn65MouseClicked
        value = 65;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn65MouseClicked

    private void btn66MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn66MouseClicked
        value = 66;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn66MouseClicked

    private void btn67MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn67MouseClicked
        value = 67;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn67MouseClicked

    private void btn68MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn68MouseClicked
        value = 68;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn68MouseClicked

    private void btn69MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn69MouseClicked
        value = 69;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn69MouseClicked

    private void btn70MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn70MouseClicked
        value = 70;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn70MouseClicked

    private void btn71MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn71MouseClicked
        value = 71;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn71MouseClicked

    private void btn72MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn72MouseClicked
        value = 72;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn72MouseClicked

    private void btn73MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn73MouseClicked
        value = 73;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn73MouseClicked

    private void btn74MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn74MouseClicked
        value = 74;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn74MouseClicked

    private void btn75MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn75MouseClicked
        value = 75;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn75MouseClicked

    private void btn76MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn76MouseClicked
        value = 76;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn76MouseClicked

    private void btn77MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn77MouseClicked
        value = 77;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn77MouseClicked

    private void btn78MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn78MouseClicked
        value = 78;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn78MouseClicked

    private void btn79MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn79MouseClicked
        value = 79;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn79MouseClicked

    private void btn80MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn80MouseClicked
        value = 80;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn80MouseClicked

    private void btn81MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn81MouseClicked
        value = 81;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn81MouseClicked

    private void btn82MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn82MouseClicked
        value = 82;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn82MouseClicked

    private void btn83MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn83MouseClicked
        value = 83;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn83MouseClicked

    private void btn84MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn84MouseClicked
        value = 84;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn84MouseClicked

    private void btn85MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn85MouseClicked
        value = 85;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn85MouseClicked

    private void btn86MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn86MouseClicked
        value = 86;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn86MouseClicked

    private void btn87MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn87MouseClicked
        value = 87;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn87MouseClicked

    private void btn88MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn88MouseClicked
        value = 88;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn88MouseClicked

    private void btn89MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn89MouseClicked
        value = 89;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn89MouseClicked

    private void btn90MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn90MouseClicked
        value = 90;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn90MouseClicked

    private void btn91MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn91MouseClicked
        value = 91;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn91MouseClicked

    private void btn92MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn92MouseClicked
        value = 92;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn92MouseClicked

    private void btn93MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn93MouseClicked
        value = 93;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn93MouseClicked

    private void btn94MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn94MouseClicked
        value = 94;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn94MouseClicked

    private void btn95MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn95MouseClicked
        value = 95;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn95MouseClicked

    private void btn96MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn96MouseClicked
        value = 96;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn96MouseClicked

    private void btn97MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn97MouseClicked
        value = 97;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn97MouseClicked

    private void btn98MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn98MouseClicked
        value = 98;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn98MouseClicked

    private void btn99MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn99MouseClicked
        value = 99;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn99MouseClicked

    private void btn100MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn100MouseClicked
        value = 100;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn100MouseClicked

    private void btn101MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn101MouseClicked
        value = 101;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn101MouseClicked

    private void btn102MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn102MouseClicked
        value = 102;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn102MouseClicked

    private void btn103MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn103MouseClicked
        value = 103;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn103MouseClicked

    private void btn104MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn104MouseClicked
        value = 104;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn104MouseClicked

    private void btn105MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn105MouseClicked
        value = 105;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn105MouseClicked

    private void btn106MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn106MouseClicked
        value = 106;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn106MouseClicked

    private void btn107MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn107MouseClicked
        value = 107;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn107MouseClicked

    private void btn108MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn108MouseClicked
        value = 108;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn108MouseClicked

    private void btn109MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn109MouseClicked
        value = 109;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn109MouseClicked

    private void btn110MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn110MouseClicked
        value = 110;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn110MouseClicked

    private void btn111MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn111MouseClicked
        value = 111;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn111MouseClicked

    private void btn112MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn112MouseClicked
        value = 112;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn112MouseClicked

    private void btn113MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn113MouseClicked
        value = 113;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn113MouseClicked

    private void btn114MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn114MouseClicked
        value = 114;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn114MouseClicked

    private void btn115MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn115MouseClicked
        value = 115;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn115MouseClicked

    private void btn116MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn116MouseClicked
        value = 116;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn116MouseClicked

    private void btn117MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn117MouseClicked
        value = 117;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn117MouseClicked

    private void btn118MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn118MouseClicked
        value = 118;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn118MouseClicked

    private void btn119MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn119MouseClicked
        value = 119;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn119MouseClicked

    private void btn120MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn120MouseClicked
        value = 120;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn120MouseClicked

    private void btn121MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn121MouseClicked
        value = 121;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn121MouseClicked

    private void btn122MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn122MouseClicked
        value = 122;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn122MouseClicked

    private void btn123MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn123MouseClicked
        value = 123;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn123MouseClicked

    private void btn124MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn124MouseClicked
        value = 124;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn124MouseClicked

    private void btn125MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn125MouseClicked
        value = 125;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn125MouseClicked

    private void btn126MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn126MouseClicked
        value = 126;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn126MouseClicked

    private void btn127MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn127MouseClicked
        value = 127;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn127MouseClicked

    private void btn128MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn128MouseClicked
        value = 128;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn128MouseClicked

    private void btn129MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn129MouseClicked
        value = 129;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn129MouseClicked

    private void btn130MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn130MouseClicked
        value = 130;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn130MouseClicked

    private void btn131MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn131MouseClicked
        value = 131;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn131MouseClicked

    private void btn132MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn132MouseClicked
        value = 132;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn132MouseClicked

    private void btn133MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn133MouseClicked
        value = 133;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn133MouseClicked

    private void btn134MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn134MouseClicked
        value = 134;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn134MouseClicked

    private void btn135MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn135MouseClicked
        value = 135;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn135MouseClicked

    private void btn136MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn136MouseClicked
        value = 136;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn136MouseClicked

    private void btn137MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn137MouseClicked
        value = 137;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn137MouseClicked

    private void btn138MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn138MouseClicked
        value = 138;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn138MouseClicked

    private void btn139MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn139MouseClicked
        value = 139;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn139MouseClicked

    private void btn140MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn140MouseClicked
        value = 140;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn140MouseClicked

    private void btn141MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn141MouseClicked
        value = 141;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn141MouseClicked

    private void btn142MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn142MouseClicked
        value = 142;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn142MouseClicked

    private void btn144MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn144MouseClicked
        value = 144;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn144MouseClicked

    private void btn143MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn143MouseClicked
        value = 143;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn143MouseClicked

    private void btn145MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn145MouseClicked
        value = 145;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn145MouseClicked

    private void btn146MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn146MouseClicked
        value = 146;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn146MouseClicked

    private void btn147MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn147MouseClicked
        value = 147;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn147MouseClicked

    private void btn148MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn148MouseClicked
        value = 148;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn148MouseClicked

    private void btn149MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn149MouseClicked
        value = 149;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn149MouseClicked

    private void btn150MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn150MouseClicked
        value = 150;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn150MouseClicked

    private void btn151MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn151MouseClicked
        value = 151;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn151MouseClicked

    private void btn152MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn152MouseClicked
        value = 152;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn152MouseClicked

    private void btn153MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn153MouseClicked
        value = 153;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn153MouseClicked

    private void btn154MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn154MouseClicked
        value = 154;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn154MouseClicked

    private void btn155MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn155MouseClicked
        value = 155;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn155MouseClicked

    private void btn156MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn156MouseClicked
        value = 156;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn156MouseClicked

    private void btn157MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn157MouseClicked
        value = 157;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn157MouseClicked

    private void btn158MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn158MouseClicked
        value = 158;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn158MouseClicked

    private void btn159MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn159MouseClicked
        value = 159;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn159MouseClicked

    private void btn160MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn160MouseClicked
        value = 160;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn160MouseClicked

    private void btn161MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn161MouseClicked
        value = 161;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn161MouseClicked

    private void btn162MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn162MouseClicked
        value = 162;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn162MouseClicked

    private void btn163MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn163MouseClicked
        value = 163;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn163MouseClicked

    private void btn164MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn164MouseClicked
        value = 164;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn164MouseClicked

    private void btn165MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn165MouseClicked
        value = 165;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn165MouseClicked

    private void btn166MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn166MouseClicked
        value = 166;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn166MouseClicked

    private void btn167MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn167MouseClicked
        value = 167;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn167MouseClicked

    private void btn168MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn168MouseClicked
        value = 168;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn168MouseClicked

    private void btn169MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn169MouseClicked
        value = 169;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn169MouseClicked

    private void btn170MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn170MouseClicked
        value = 170;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn170MouseClicked

    private void btn171MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn171MouseClicked
        value = 171;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn171MouseClicked

    private void btn172MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn172MouseClicked
        value = 172;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn172MouseClicked

    private void btn173MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn173MouseClicked
        value = 173;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn173MouseClicked

    private void btn174MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn174MouseClicked
        value = 174;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn174MouseClicked

    private void btn175MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn175MouseClicked
        value = 175;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn175MouseClicked

    private void btn176MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn176MouseClicked
        value = 176;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn176MouseClicked

    private void btn177MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn177MouseClicked
        value = 177;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn177MouseClicked

    private void btn178MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn178MouseClicked
        value = 178;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn178MouseClicked

    private void btn179MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn179MouseClicked
        value = 179;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn179MouseClicked

    private void btn180MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn180MouseClicked
        value = 180;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn180MouseClicked

    private void btn181MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn181MouseClicked
        value = 181;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn181MouseClicked

    private void btn182MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn182MouseClicked
        value = 182;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn182MouseClicked

    private void btn183MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn183MouseClicked
        value = 183;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn183MouseClicked

    private void btn184MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn184MouseClicked
        value = 184;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn184MouseClicked

    private void btn185MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn185MouseClicked
        value = 185;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn185MouseClicked

    private void btn186MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn186MouseClicked
        value = 186;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn186MouseClicked

    private void btn187MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn187MouseClicked
        value = 187;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn187MouseClicked

    private void btn188MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn188MouseClicked
        value = 188;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn188MouseClicked

    private void btn189MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn189MouseClicked
        value = 189;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn189MouseClicked

    private void btn190MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn190MouseClicked
        value = 190;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn190MouseClicked

    private void btn191MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn191MouseClicked
        value = 191;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn191MouseClicked

    private void btn192MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn192MouseClicked
        value = 192;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn192MouseClicked

    private void btn193MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn193MouseClicked
        value = 193;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn193MouseClicked

    private void btn194MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn194MouseClicked
        value = 194;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn194MouseClicked

    private void btn195MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn195MouseClicked
        value = 195;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn195MouseClicked

    private void btn196MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn196MouseClicked
        value = 196;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn196MouseClicked

    private void btn197MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn197MouseClicked
        value = 197;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn197MouseClicked

    private void btn198MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn198MouseClicked
        value = 198;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn198MouseClicked

    private void btn199MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn199MouseClicked
        value = 199;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn199MouseClicked

    private void btn200MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn200MouseClicked
        value = 200;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn200MouseClicked

    private void btn201MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn201MouseClicked
        value = 201;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn201MouseClicked

    private void btn202MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn202MouseClicked
        value = 202;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn202MouseClicked

    private void btn203MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn203MouseClicked
        value = 203;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn203MouseClicked

    private void btn204MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn204MouseClicked
        value = 204;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn204MouseClicked

    private void btn205MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn205MouseClicked
        value = 205;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn205MouseClicked

    private void btn206MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn206MouseClicked
        value = 206;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn206MouseClicked

    private void btn207MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn207MouseClicked
        value = 207;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn207MouseClicked

    private void btn208MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn208MouseClicked
        value = 208;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn208MouseClicked

    private void btn209MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn209MouseClicked
        value = 209;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn209MouseClicked

    private void btn210MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn210MouseClicked
        value = 210;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn210MouseClicked

    private void btn211MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn211MouseClicked
        value = 211;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn211MouseClicked

    private void btn212MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn212MouseClicked
        value = 212;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn212MouseClicked

    private void btn213MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn213MouseClicked
        value = 213;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn213MouseClicked

    private void btn214MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn214MouseClicked
        value = 214;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn214MouseClicked

    private void btn215MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn215MouseClicked
        value = 215;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn215MouseClicked

    private void btn216MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn216MouseClicked
        value = 216;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn216MouseClicked

    private void btn217MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn217MouseClicked
        value = 217;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn217MouseClicked

    private void btn218MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn218MouseClicked
        value = 218;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn218MouseClicked

    private void btn219MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn219MouseClicked
        value = 21;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn219MouseClicked

    private void btn220MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn220MouseClicked
        value = 220;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn220MouseClicked

    private void btn221MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn221MouseClicked
        value = 221;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn221MouseClicked

    private void btn222MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn222MouseClicked
        value = 222;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn222MouseClicked

    private void btn223MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn223MouseClicked
        value = 223;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn223MouseClicked

    private void btn224MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn224MouseClicked
        value = 224;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn224MouseClicked

    private void btn225MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn225MouseClicked
        value = 225;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn225MouseClicked

    private void btn226MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn226MouseClicked
        value = 226;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn226MouseClicked

    private void btn227MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn227MouseClicked
        value = 227;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn227MouseClicked

    private void btn228MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn228MouseClicked
        value = 228;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn228MouseClicked

    private void btn229MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn229MouseClicked
        value = 229;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn229MouseClicked

    private void btn230MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn230MouseClicked
        value = 230;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn230MouseClicked

    private void btn231MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn231MouseClicked
        value = 231;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn231MouseClicked

    private void btn232MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn232MouseClicked
        value = 232;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn232MouseClicked

    private void btn233MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn233MouseClicked
        value = 233;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn233MouseClicked

    private void btn234MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn234MouseClicked
        value = 234;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn234MouseClicked

    private void btn235MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn235MouseClicked
        value = 235;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn235MouseClicked

    private void btn236MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn236MouseClicked
        value = 236;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn236MouseClicked

    private void btn237MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn237MouseClicked
        value = 237;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn237MouseClicked

    private void btn238MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn238MouseClicked
        value = 238;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn238MouseClicked

    private void btn239MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn239MouseClicked
        value = 239;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn239MouseClicked

    private void btn240MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn240MouseClicked
        value = 240;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn240MouseClicked

    private void btn241MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn241MouseClicked
        value = 241;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn241MouseClicked

    private void btn242MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn242MouseClicked
        value = 242;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn242MouseClicked

    private void btn243MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn243MouseClicked
        value = 243;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn243MouseClicked

    private void btn244MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn244MouseClicked
        value = 244;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn244MouseClicked

    private void btn245MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn245MouseClicked
        value = 245;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn245MouseClicked

    private void btn246MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn246MouseClicked
        value = 246;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn246MouseClicked

    private void btn247MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn247MouseClicked
        value = 247;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn247MouseClicked

    private void btn248MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn248MouseClicked
        value = 248;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn248MouseClicked

    private void btn249MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn249MouseClicked
        value = 249;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn249MouseClicked

    private void btn250MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn250MouseClicked
        value = 250;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn250MouseClicked

    private void btn251MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn251MouseClicked
        value = 251;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn251MouseClicked

    private void btn252MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn252MouseClicked
        value = 252;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn252MouseClicked

    private void btn253MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn253MouseClicked
        value = 253;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn253MouseClicked

    private void btn254MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn254MouseClicked
        value = 254;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn254MouseClicked

    private void btn255MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn255MouseClicked
        value = 255;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn255MouseClicked

    private void btn256MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn256MouseClicked
        value = 256;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn256MouseClicked

    private void btn257MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn257MouseClicked
        value = 257;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn257MouseClicked

    private void btn258MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn258MouseClicked
        value = 258;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn258MouseClicked

    private void btn259MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn259MouseClicked
        value = 259;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn259MouseClicked

    private void btn260MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn260MouseClicked
        value = 260;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn260MouseClicked

    private void btn261MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn261MouseClicked
        value = 261;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn261MouseClicked

    private void btn262MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn262MouseClicked
        value = 262;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn262MouseClicked

    private void btn263MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn263MouseClicked
        value = 263;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn263MouseClicked

    private void btn264MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn264MouseClicked
        value = 264;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn264MouseClicked

    private void btn265MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn265MouseClicked
        value = 265;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn265MouseClicked

    private void btn266MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn266MouseClicked
        value = 266;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn266MouseClicked

    private void btn267MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn267MouseClicked
        value = 267;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn267MouseClicked

    private void btn268MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn268MouseClicked
        value = 268;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn268MouseClicked

    private void btn269MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn269MouseClicked
        value = 269;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn269MouseClicked

    private void btn270MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn270MouseClicked
        value = 270;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn270MouseClicked

    private void btn271MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn271MouseClicked
        value = 271;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn271MouseClicked

    private void btn272MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn272MouseClicked
        value = 272;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn272MouseClicked

    private void btn273MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn273MouseClicked
        value = 273;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn273MouseClicked

    private void btn274MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn274MouseClicked
        value = 274;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn274MouseClicked

    private void btn275MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn275MouseClicked
        value = 275;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn275MouseClicked

    private void btn276MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn276MouseClicked
        value = 276;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn276MouseClicked

    private void btn277MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn277MouseClicked
        value = 277;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn277MouseClicked

    private void btn278MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn278MouseClicked
        value = 278;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn278MouseClicked

    private void btn279MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn279MouseClicked
        value = 279;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn279MouseClicked

    private void btn280MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn280MouseClicked
        value = 280;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn280MouseClicked

    private void btn281MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn281MouseClicked
        value = 281;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn281MouseClicked

    private void btn282MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn282MouseClicked
        value = 282;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn282MouseClicked

    private void btn283MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn283MouseClicked
        value = 283;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn283MouseClicked

    private void btn284MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn284MouseClicked
        value = 284;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn284MouseClicked

    private void btn285MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn285MouseClicked
        value = 285;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn285MouseClicked

    private void btn286MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn286MouseClicked
        value = 286;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn286MouseClicked

    private void btn287MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn287MouseClicked
        value = 287;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn287MouseClicked

    private void btn288MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn288MouseClicked
        value = 288;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn288MouseClicked

    private void btn289MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn289MouseClicked
        value = 289;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn289MouseClicked

    private void btn290MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn290MouseClicked
        value = 290;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn290MouseClicked

    private void btn291MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn291MouseClicked
        value = 291;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn291MouseClicked

    private void btn292MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn292MouseClicked
        value = 292;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn292MouseClicked

    private void btn293MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn293MouseClicked
        value = 293;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn293MouseClicked

    private void btn294MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn294MouseClicked
        value = 294;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn294MouseClicked

    private void btn295MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn295MouseClicked
        value = 295;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn295MouseClicked

    private void btn296MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn296MouseClicked
        value = 296;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn296MouseClicked

    private void btn297MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn297MouseClicked
        value = 297;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn297MouseClicked

    private void btn298MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn298MouseClicked
        value = 298;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn298MouseClicked

    private void btn299MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn299MouseClicked
        value = 299;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn299MouseClicked

    private void btn300MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn300MouseClicked
        value = 300;
        if (evt.getButton() == 1) {
            if (!buttonStatusList.get(value)) {
                if (!mineGenerated) {
                    generateMineSlots(value);
                    manageSlot(value);
                } else if (checkDuplicateSlot(value, mineList)) {
                    mineHit();
                } else {
                    manageSlot(value);
                }
            }
        } else if (evt.getButton() == 3) {
            manageFlag(value);
        }
    }//GEN-LAST:event_btn300MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MineApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MineApp app = new MineApp();
                app.setVisible(true);
                app.setSize(650, 550);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn10;
    private javax.swing.JButton btn100;
    private javax.swing.JButton btn101;
    private javax.swing.JButton btn102;
    private javax.swing.JButton btn103;
    private javax.swing.JButton btn104;
    private javax.swing.JButton btn105;
    private javax.swing.JButton btn106;
    private javax.swing.JButton btn107;
    private javax.swing.JButton btn108;
    private javax.swing.JButton btn109;
    private javax.swing.JButton btn11;
    private javax.swing.JButton btn110;
    private javax.swing.JButton btn111;
    private javax.swing.JButton btn112;
    private javax.swing.JButton btn113;
    private javax.swing.JButton btn114;
    private javax.swing.JButton btn115;
    private javax.swing.JButton btn116;
    private javax.swing.JButton btn117;
    private javax.swing.JButton btn118;
    private javax.swing.JButton btn119;
    private javax.swing.JButton btn12;
    private javax.swing.JButton btn120;
    private javax.swing.JButton btn121;
    private javax.swing.JButton btn122;
    private javax.swing.JButton btn123;
    private javax.swing.JButton btn124;
    private javax.swing.JButton btn125;
    private javax.swing.JButton btn126;
    private javax.swing.JButton btn127;
    private javax.swing.JButton btn128;
    private javax.swing.JButton btn129;
    private javax.swing.JButton btn13;
    private javax.swing.JButton btn130;
    private javax.swing.JButton btn131;
    private javax.swing.JButton btn132;
    private javax.swing.JButton btn133;
    private javax.swing.JButton btn134;
    private javax.swing.JButton btn135;
    private javax.swing.JButton btn136;
    private javax.swing.JButton btn137;
    private javax.swing.JButton btn138;
    private javax.swing.JButton btn139;
    private javax.swing.JButton btn14;
    private javax.swing.JButton btn140;
    private javax.swing.JButton btn141;
    private javax.swing.JButton btn142;
    private javax.swing.JButton btn143;
    private javax.swing.JButton btn144;
    private javax.swing.JButton btn145;
    private javax.swing.JButton btn146;
    private javax.swing.JButton btn147;
    private javax.swing.JButton btn148;
    private javax.swing.JButton btn149;
    private javax.swing.JButton btn15;
    private javax.swing.JButton btn150;
    private javax.swing.JButton btn151;
    private javax.swing.JButton btn152;
    private javax.swing.JButton btn153;
    private javax.swing.JButton btn154;
    private javax.swing.JButton btn155;
    private javax.swing.JButton btn156;
    private javax.swing.JButton btn157;
    private javax.swing.JButton btn158;
    private javax.swing.JButton btn159;
    private javax.swing.JButton btn16;
    private javax.swing.JButton btn160;
    private javax.swing.JButton btn161;
    private javax.swing.JButton btn162;
    private javax.swing.JButton btn163;
    private javax.swing.JButton btn164;
    private javax.swing.JButton btn165;
    private javax.swing.JButton btn166;
    private javax.swing.JButton btn167;
    private javax.swing.JButton btn168;
    private javax.swing.JButton btn169;
    private javax.swing.JButton btn17;
    private javax.swing.JButton btn170;
    private javax.swing.JButton btn171;
    private javax.swing.JButton btn172;
    private javax.swing.JButton btn173;
    private javax.swing.JButton btn174;
    private javax.swing.JButton btn175;
    private javax.swing.JButton btn176;
    private javax.swing.JButton btn177;
    private javax.swing.JButton btn178;
    private javax.swing.JButton btn179;
    private javax.swing.JButton btn18;
    private javax.swing.JButton btn180;
    private javax.swing.JButton btn181;
    private javax.swing.JButton btn182;
    private javax.swing.JButton btn183;
    private javax.swing.JButton btn184;
    private javax.swing.JButton btn185;
    private javax.swing.JButton btn186;
    private javax.swing.JButton btn187;
    private javax.swing.JButton btn188;
    private javax.swing.JButton btn189;
    private javax.swing.JButton btn19;
    private javax.swing.JButton btn190;
    private javax.swing.JButton btn191;
    private javax.swing.JButton btn192;
    private javax.swing.JButton btn193;
    private javax.swing.JButton btn194;
    private javax.swing.JButton btn195;
    private javax.swing.JButton btn196;
    private javax.swing.JButton btn197;
    private javax.swing.JButton btn198;
    private javax.swing.JButton btn199;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn20;
    private javax.swing.JButton btn200;
    private javax.swing.JButton btn201;
    private javax.swing.JButton btn202;
    private javax.swing.JButton btn203;
    private javax.swing.JButton btn204;
    private javax.swing.JButton btn205;
    private javax.swing.JButton btn206;
    private javax.swing.JButton btn207;
    private javax.swing.JButton btn208;
    private javax.swing.JButton btn209;
    private javax.swing.JButton btn21;
    private javax.swing.JButton btn210;
    private javax.swing.JButton btn211;
    private javax.swing.JButton btn212;
    private javax.swing.JButton btn213;
    private javax.swing.JButton btn214;
    private javax.swing.JButton btn215;
    private javax.swing.JButton btn216;
    private javax.swing.JButton btn217;
    private javax.swing.JButton btn218;
    private javax.swing.JButton btn219;
    private javax.swing.JButton btn22;
    private javax.swing.JButton btn220;
    private javax.swing.JButton btn221;
    private javax.swing.JButton btn222;
    private javax.swing.JButton btn223;
    private javax.swing.JButton btn224;
    private javax.swing.JButton btn225;
    private javax.swing.JButton btn226;
    private javax.swing.JButton btn227;
    private javax.swing.JButton btn228;
    private javax.swing.JButton btn229;
    private javax.swing.JButton btn23;
    private javax.swing.JButton btn230;
    private javax.swing.JButton btn231;
    private javax.swing.JButton btn232;
    private javax.swing.JButton btn233;
    private javax.swing.JButton btn234;
    private javax.swing.JButton btn235;
    private javax.swing.JButton btn236;
    private javax.swing.JButton btn237;
    private javax.swing.JButton btn238;
    private javax.swing.JButton btn239;
    private javax.swing.JButton btn24;
    private javax.swing.JButton btn240;
    private javax.swing.JButton btn241;
    private javax.swing.JButton btn242;
    private javax.swing.JButton btn243;
    private javax.swing.JButton btn244;
    private javax.swing.JButton btn245;
    private javax.swing.JButton btn246;
    private javax.swing.JButton btn247;
    private javax.swing.JButton btn248;
    private javax.swing.JButton btn249;
    private javax.swing.JButton btn25;
    private javax.swing.JButton btn250;
    private javax.swing.JButton btn251;
    private javax.swing.JButton btn252;
    private javax.swing.JButton btn253;
    private javax.swing.JButton btn254;
    private javax.swing.JButton btn255;
    private javax.swing.JButton btn256;
    private javax.swing.JButton btn257;
    private javax.swing.JButton btn258;
    private javax.swing.JButton btn259;
    private javax.swing.JButton btn26;
    private javax.swing.JButton btn260;
    private javax.swing.JButton btn261;
    private javax.swing.JButton btn262;
    private javax.swing.JButton btn263;
    private javax.swing.JButton btn264;
    private javax.swing.JButton btn265;
    private javax.swing.JButton btn266;
    private javax.swing.JButton btn267;
    private javax.swing.JButton btn268;
    private javax.swing.JButton btn269;
    private javax.swing.JButton btn27;
    private javax.swing.JButton btn270;
    private javax.swing.JButton btn271;
    private javax.swing.JButton btn272;
    private javax.swing.JButton btn273;
    private javax.swing.JButton btn274;
    private javax.swing.JButton btn275;
    private javax.swing.JButton btn276;
    private javax.swing.JButton btn277;
    private javax.swing.JButton btn278;
    private javax.swing.JButton btn279;
    private javax.swing.JButton btn28;
    private javax.swing.JButton btn280;
    private javax.swing.JButton btn281;
    private javax.swing.JButton btn282;
    private javax.swing.JButton btn283;
    private javax.swing.JButton btn284;
    private javax.swing.JButton btn285;
    private javax.swing.JButton btn286;
    private javax.swing.JButton btn287;
    private javax.swing.JButton btn288;
    private javax.swing.JButton btn289;
    private javax.swing.JButton btn29;
    private javax.swing.JButton btn290;
    private javax.swing.JButton btn291;
    private javax.swing.JButton btn292;
    private javax.swing.JButton btn293;
    private javax.swing.JButton btn294;
    private javax.swing.JButton btn295;
    private javax.swing.JButton btn296;
    private javax.swing.JButton btn297;
    private javax.swing.JButton btn298;
    private javax.swing.JButton btn299;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn30;
    private javax.swing.JButton btn300;
    private javax.swing.JButton btn31;
    private javax.swing.JButton btn32;
    private javax.swing.JButton btn33;
    private javax.swing.JButton btn34;
    private javax.swing.JButton btn35;
    private javax.swing.JButton btn36;
    private javax.swing.JButton btn37;
    private javax.swing.JButton btn38;
    private javax.swing.JButton btn39;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn40;
    private javax.swing.JButton btn41;
    private javax.swing.JButton btn42;
    private javax.swing.JButton btn43;
    private javax.swing.JButton btn44;
    private javax.swing.JButton btn45;
    private javax.swing.JButton btn46;
    private javax.swing.JButton btn47;
    private javax.swing.JButton btn48;
    private javax.swing.JButton btn49;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn50;
    private javax.swing.JButton btn51;
    private javax.swing.JButton btn52;
    private javax.swing.JButton btn53;
    private javax.swing.JButton btn54;
    private javax.swing.JButton btn55;
    private javax.swing.JButton btn56;
    private javax.swing.JButton btn57;
    private javax.swing.JButton btn58;
    private javax.swing.JButton btn59;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn60;
    private javax.swing.JButton btn61;
    private javax.swing.JButton btn62;
    private javax.swing.JButton btn63;
    private javax.swing.JButton btn64;
    private javax.swing.JButton btn65;
    private javax.swing.JButton btn66;
    private javax.swing.JButton btn67;
    private javax.swing.JButton btn68;
    private javax.swing.JButton btn69;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn70;
    private javax.swing.JButton btn71;
    private javax.swing.JButton btn72;
    private javax.swing.JButton btn73;
    private javax.swing.JButton btn74;
    private javax.swing.JButton btn75;
    private javax.swing.JButton btn76;
    private javax.swing.JButton btn77;
    private javax.swing.JButton btn78;
    private javax.swing.JButton btn79;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn80;
    private javax.swing.JButton btn81;
    private javax.swing.JButton btn82;
    private javax.swing.JButton btn83;
    private javax.swing.JButton btn84;
    private javax.swing.JButton btn85;
    private javax.swing.JButton btn86;
    private javax.swing.JButton btn87;
    private javax.swing.JButton btn88;
    private javax.swing.JButton btn89;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btn90;
    private javax.swing.JButton btn91;
    private javax.swing.JButton btn92;
    private javax.swing.JButton btn93;
    private javax.swing.JButton btn94;
    private javax.swing.JButton btn95;
    private javax.swing.JButton btn96;
    private javax.swing.JButton btn97;
    private javax.swing.JButton btn98;
    private javax.swing.JButton btn99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField txtTimer;
    // End of variables declaration//GEN-END:variables
}
