/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mine;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Nat
 */
public class MineApp extends javax.swing.JFrame {

    int value = 0;
    boolean mineGenerated = false;
    HashMap<Integer, ArrayList<Integer>> adjacentMap;
    HashMap<Integer, JButton> buttonMap;
    ArrayList<Integer> adjacentList;
    ArrayList<Integer> mineList;
    ArrayList<Integer> ChangeList;
    HashMap<Integer, Boolean> buttonStatusList;
    JButton defaultButton;
    MouseListener listener = null;

    public MineApp() {
        initComponents();
        getAdjacentBtn();
        mapButton();
        defaultButton = new JButton();
    }

    private void mineHit() {
        for (int i = 0; i < mineList.size(); i++) {
            defaultButton = buttonMap.get(mineList.get(i));
            defaultButton.setBackground(Color.DARK_GRAY);
        }
        if (JOptionPane.showConfirmDialog(this, "Game Over!\n\nContinue?",
                "Game Over!", JOptionPane.OK_OPTION) == 0) {
            MineApp newApp = new MineApp();
            newApp.setVisible(true);
            newApp.setLocation(this.getLocation());
            newApp.setSize(650, 550);
        }
        this.dispose();
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
            if (JOptionPane.showConfirmDialog(this, "Victory!\n\nContinue?",
                    "Victory!", JOptionPane.OK_OPTION) == 0) {
                MineApp newApp = new MineApp();
                newApp.setVisible(true);
                newApp.setSize(650, 550);
            }
            this.dispose();
        }
    }

    private void manageAdjacentButton(int slot) {
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

    private void manageNonAdjacentButton(int slot) {
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
//        for (int i = 0; i < mineList.size(); i++) {
//            buttonStatusList.replace(mineList.get(i), true);
//        }
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
        buttonStatusList = new HashMap<>();
        for (int i = 1; i <= 300; i++) {
            buttonStatusList.put(i, false);
        }
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 876, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

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
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
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
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
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
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
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
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
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
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
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
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn6ActionPerformed(evt);
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
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn7ActionPerformed(evt);
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
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn8ActionPerformed(evt);
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
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn9ActionPerformed(evt);
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
        btn10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn10ActionPerformed(evt);
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
        btn11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn11ActionPerformed(evt);
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
        btn12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn12ActionPerformed(evt);
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
        btn13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn13ActionPerformed(evt);
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
        btn14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn14ActionPerformed(evt);
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
        btn15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn15ActionPerformed(evt);
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
        btn16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn16ActionPerformed(evt);
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
        btn17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn17ActionPerformed(evt);
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
        btn18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn18ActionPerformed(evt);
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
        btn19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn19ActionPerformed(evt);
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
        btn20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn20ActionPerformed(evt);
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
        btn21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn21ActionPerformed(evt);
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
        btn22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn22ActionPerformed(evt);
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
        btn23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn23ActionPerformed(evt);
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
        btn24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn24ActionPerformed(evt);
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
        btn25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn25ActionPerformed(evt);
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
        btn26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn26ActionPerformed(evt);
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
        btn27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn27ActionPerformed(evt);
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
        btn28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn28ActionPerformed(evt);
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
        btn29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn29ActionPerformed(evt);
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
        btn30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn30ActionPerformed(evt);
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
        btn31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn31ActionPerformed(evt);
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
        btn32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn32ActionPerformed(evt);
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
        btn33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn33ActionPerformed(evt);
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
        btn34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn34ActionPerformed(evt);
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
        btn35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn35ActionPerformed(evt);
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
        btn36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn36ActionPerformed(evt);
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
        btn37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn37ActionPerformed(evt);
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
        btn38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn38ActionPerformed(evt);
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
        btn39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn39ActionPerformed(evt);
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
        btn40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn40ActionPerformed(evt);
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
        btn41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn41ActionPerformed(evt);
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
        btn42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn42ActionPerformed(evt);
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
        btn43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn43ActionPerformed(evt);
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
        btn44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn44ActionPerformed(evt);
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
        btn45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn45ActionPerformed(evt);
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
        btn46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn46ActionPerformed(evt);
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
        btn47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn47ActionPerformed(evt);
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
        btn48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn48ActionPerformed(evt);
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
        btn49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn49ActionPerformed(evt);
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
        btn50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn50ActionPerformed(evt);
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
        btn51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn51ActionPerformed(evt);
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
        btn52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn52ActionPerformed(evt);
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
        btn53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn53ActionPerformed(evt);
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
        btn54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn54ActionPerformed(evt);
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
        btn55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn55ActionPerformed(evt);
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
        btn56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn56ActionPerformed(evt);
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
        btn57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn57ActionPerformed(evt);
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
        btn58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn58ActionPerformed(evt);
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
        btn59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn59ActionPerformed(evt);
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
        btn60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn60ActionPerformed(evt);
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
        btn61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn61ActionPerformed(evt);
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
        btn62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn62ActionPerformed(evt);
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
        btn63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn63ActionPerformed(evt);
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
        btn64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn64ActionPerformed(evt);
            }
        });
        jPanel5.add(btn64);

        btn65.setBackground(new java.awt.Color(102, 153, 255));
        btn65.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn65.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn65.setMaximumSize(new java.awt.Dimension(20, 20));
        btn65.setPreferredSize(new java.awt.Dimension(20, 20));
        btn65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn65ActionPerformed(evt);
            }
        });
        jPanel5.add(btn65);

        btn66.setBackground(new java.awt.Color(102, 153, 255));
        btn66.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn66.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn66.setMaximumSize(new java.awt.Dimension(20, 20));
        btn66.setPreferredSize(new java.awt.Dimension(20, 20));
        btn66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn66ActionPerformed(evt);
            }
        });
        jPanel5.add(btn66);

        btn67.setBackground(new java.awt.Color(102, 153, 255));
        btn67.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn67.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn67.setMaximumSize(new java.awt.Dimension(20, 20));
        btn67.setPreferredSize(new java.awt.Dimension(20, 20));
        btn67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn67ActionPerformed(evt);
            }
        });
        jPanel5.add(btn67);

        btn68.setBackground(new java.awt.Color(102, 153, 255));
        btn68.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn68.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn68.setMaximumSize(new java.awt.Dimension(20, 20));
        btn68.setPreferredSize(new java.awt.Dimension(20, 20));
        btn68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn68ActionPerformed(evt);
            }
        });
        jPanel5.add(btn68);

        btn69.setBackground(new java.awt.Color(102, 153, 255));
        btn69.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn69.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn69.setMaximumSize(new java.awt.Dimension(20, 20));
        btn69.setPreferredSize(new java.awt.Dimension(20, 20));
        btn69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn69ActionPerformed(evt);
            }
        });
        jPanel5.add(btn69);

        btn70.setBackground(new java.awt.Color(102, 153, 255));
        btn70.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn70.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn70.setMaximumSize(new java.awt.Dimension(20, 20));
        btn70.setPreferredSize(new java.awt.Dimension(20, 20));
        btn70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn70ActionPerformed(evt);
            }
        });
        jPanel5.add(btn70);

        btn71.setBackground(new java.awt.Color(102, 153, 255));
        btn71.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn71.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn71.setMaximumSize(new java.awt.Dimension(20, 20));
        btn71.setPreferredSize(new java.awt.Dimension(20, 20));
        btn71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn71ActionPerformed(evt);
            }
        });
        jPanel5.add(btn71);

        btn72.setBackground(new java.awt.Color(102, 153, 255));
        btn72.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn72.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn72.setMaximumSize(new java.awt.Dimension(20, 20));
        btn72.setPreferredSize(new java.awt.Dimension(20, 20));
        btn72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn72ActionPerformed(evt);
            }
        });
        jPanel5.add(btn72);

        btn73.setBackground(new java.awt.Color(102, 153, 255));
        btn73.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn73.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn73.setMaximumSize(new java.awt.Dimension(20, 20));
        btn73.setPreferredSize(new java.awt.Dimension(20, 20));
        btn73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn73ActionPerformed(evt);
            }
        });
        jPanel5.add(btn73);

        btn74.setBackground(new java.awt.Color(102, 153, 255));
        btn74.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn74.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn74.setMaximumSize(new java.awt.Dimension(20, 20));
        btn74.setPreferredSize(new java.awt.Dimension(20, 20));
        btn74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn74ActionPerformed(evt);
            }
        });
        jPanel5.add(btn74);

        btn75.setBackground(new java.awt.Color(102, 153, 255));
        btn75.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn75.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn75.setMaximumSize(new java.awt.Dimension(20, 20));
        btn75.setPreferredSize(new java.awt.Dimension(20, 20));
        btn75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn75ActionPerformed(evt);
            }
        });
        jPanel5.add(btn75);

        btn76.setBackground(new java.awt.Color(102, 153, 255));
        btn76.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn76.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn76.setMaximumSize(new java.awt.Dimension(20, 20));
        btn76.setPreferredSize(new java.awt.Dimension(20, 20));
        btn76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn76ActionPerformed(evt);
            }
        });
        jPanel5.add(btn76);

        btn77.setBackground(new java.awt.Color(102, 153, 255));
        btn77.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn77.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn77.setMaximumSize(new java.awt.Dimension(20, 20));
        btn77.setPreferredSize(new java.awt.Dimension(20, 20));
        btn77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn77ActionPerformed(evt);
            }
        });
        jPanel5.add(btn77);

        btn78.setBackground(new java.awt.Color(102, 153, 255));
        btn78.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn78.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn78.setMaximumSize(new java.awt.Dimension(20, 20));
        btn78.setPreferredSize(new java.awt.Dimension(20, 20));
        btn78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn78ActionPerformed(evt);
            }
        });
        jPanel5.add(btn78);

        btn79.setBackground(new java.awt.Color(102, 153, 255));
        btn79.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn79.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn79.setMaximumSize(new java.awt.Dimension(20, 20));
        btn79.setPreferredSize(new java.awt.Dimension(20, 20));
        btn79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn79ActionPerformed(evt);
            }
        });
        jPanel5.add(btn79);

        btn80.setBackground(new java.awt.Color(102, 153, 255));
        btn80.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn80.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn80.setMaximumSize(new java.awt.Dimension(20, 20));
        btn80.setPreferredSize(new java.awt.Dimension(20, 20));
        btn80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn80ActionPerformed(evt);
            }
        });
        jPanel5.add(btn80);

        btn81.setBackground(new java.awt.Color(102, 153, 255));
        btn81.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn81.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn81.setMaximumSize(new java.awt.Dimension(20, 20));
        btn81.setPreferredSize(new java.awt.Dimension(20, 20));
        btn81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn81ActionPerformed(evt);
            }
        });
        jPanel5.add(btn81);

        btn82.setBackground(new java.awt.Color(102, 153, 255));
        btn82.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn82.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn82.setMaximumSize(new java.awt.Dimension(20, 20));
        btn82.setPreferredSize(new java.awt.Dimension(20, 20));
        btn82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn82ActionPerformed(evt);
            }
        });
        jPanel5.add(btn82);

        btn83.setBackground(new java.awt.Color(102, 153, 255));
        btn83.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn83.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn83.setMaximumSize(new java.awt.Dimension(20, 20));
        btn83.setPreferredSize(new java.awt.Dimension(20, 20));
        btn83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn83ActionPerformed(evt);
            }
        });
        jPanel5.add(btn83);

        btn84.setBackground(new java.awt.Color(102, 153, 255));
        btn84.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn84.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn84.setMaximumSize(new java.awt.Dimension(20, 20));
        btn84.setPreferredSize(new java.awt.Dimension(20, 20));
        btn84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn84ActionPerformed(evt);
            }
        });
        jPanel5.add(btn84);

        btn85.setBackground(new java.awt.Color(102, 153, 255));
        btn85.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn85.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn85.setMaximumSize(new java.awt.Dimension(20, 20));
        btn85.setPreferredSize(new java.awt.Dimension(20, 20));
        btn85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn85ActionPerformed(evt);
            }
        });
        jPanel5.add(btn85);

        btn86.setBackground(new java.awt.Color(102, 153, 255));
        btn86.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn86.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn86.setMaximumSize(new java.awt.Dimension(20, 20));
        btn86.setPreferredSize(new java.awt.Dimension(20, 20));
        btn86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn86ActionPerformed(evt);
            }
        });
        jPanel5.add(btn86);

        btn87.setBackground(new java.awt.Color(102, 153, 255));
        btn87.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn87.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn87.setMaximumSize(new java.awt.Dimension(20, 20));
        btn87.setPreferredSize(new java.awt.Dimension(20, 20));
        btn87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn87ActionPerformed(evt);
            }
        });
        jPanel5.add(btn87);

        btn88.setBackground(new java.awt.Color(102, 153, 255));
        btn88.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn88.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn88.setMaximumSize(new java.awt.Dimension(20, 20));
        btn88.setPreferredSize(new java.awt.Dimension(20, 20));
        btn88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn88ActionPerformed(evt);
            }
        });
        jPanel5.add(btn88);

        btn89.setBackground(new java.awt.Color(102, 153, 255));
        btn89.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn89.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn89.setMaximumSize(new java.awt.Dimension(20, 20));
        btn89.setPreferredSize(new java.awt.Dimension(20, 20));
        btn89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn89ActionPerformed(evt);
            }
        });
        jPanel5.add(btn89);

        btn90.setBackground(new java.awt.Color(102, 153, 255));
        btn90.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn90.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn90.setMaximumSize(new java.awt.Dimension(20, 20));
        btn90.setPreferredSize(new java.awt.Dimension(20, 20));
        btn90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn90ActionPerformed(evt);
            }
        });
        jPanel5.add(btn90);

        btn91.setBackground(new java.awt.Color(102, 153, 255));
        btn91.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn91.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn91.setMaximumSize(new java.awt.Dimension(20, 20));
        btn91.setPreferredSize(new java.awt.Dimension(20, 20));
        btn91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn91ActionPerformed(evt);
            }
        });
        jPanel5.add(btn91);

        btn92.setBackground(new java.awt.Color(102, 153, 255));
        btn92.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn92.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn92.setMaximumSize(new java.awt.Dimension(20, 20));
        btn92.setPreferredSize(new java.awt.Dimension(20, 20));
        btn92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn92ActionPerformed(evt);
            }
        });
        jPanel5.add(btn92);

        btn93.setBackground(new java.awt.Color(102, 153, 255));
        btn93.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn93.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn93.setMaximumSize(new java.awt.Dimension(20, 20));
        btn93.setPreferredSize(new java.awt.Dimension(20, 20));
        btn93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn93ActionPerformed(evt);
            }
        });
        jPanel5.add(btn93);

        btn94.setBackground(new java.awt.Color(102, 153, 255));
        btn94.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn94.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn94.setMaximumSize(new java.awt.Dimension(20, 20));
        btn94.setPreferredSize(new java.awt.Dimension(20, 20));
        btn94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn94ActionPerformed(evt);
            }
        });
        jPanel5.add(btn94);

        btn95.setBackground(new java.awt.Color(102, 153, 255));
        btn95.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn95.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn95.setMaximumSize(new java.awt.Dimension(20, 20));
        btn95.setPreferredSize(new java.awt.Dimension(20, 20));
        btn95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn95ActionPerformed(evt);
            }
        });
        jPanel5.add(btn95);

        btn96.setBackground(new java.awt.Color(102, 153, 255));
        btn96.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn96.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn96.setMaximumSize(new java.awt.Dimension(20, 20));
        btn96.setPreferredSize(new java.awt.Dimension(20, 20));
        btn96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn96ActionPerformed(evt);
            }
        });
        jPanel5.add(btn96);

        btn97.setBackground(new java.awt.Color(102, 153, 255));
        btn97.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn97.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn97.setMaximumSize(new java.awt.Dimension(20, 20));
        btn97.setPreferredSize(new java.awt.Dimension(20, 20));
        btn97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn97ActionPerformed(evt);
            }
        });
        jPanel5.add(btn97);

        btn98.setBackground(new java.awt.Color(102, 153, 255));
        btn98.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn98.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn98.setMaximumSize(new java.awt.Dimension(20, 20));
        btn98.setPreferredSize(new java.awt.Dimension(20, 20));
        btn98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn98ActionPerformed(evt);
            }
        });
        jPanel5.add(btn98);

        btn99.setBackground(new java.awt.Color(102, 153, 255));
        btn99.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn99.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn99.setMaximumSize(new java.awt.Dimension(20, 20));
        btn99.setPreferredSize(new java.awt.Dimension(20, 20));
        btn99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn99ActionPerformed(evt);
            }
        });
        jPanel5.add(btn99);

        btn100.setBackground(new java.awt.Color(102, 153, 255));
        btn100.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn100.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn100.setMaximumSize(new java.awt.Dimension(20, 20));
        btn100.setPreferredSize(new java.awt.Dimension(20, 20));
        btn100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn100ActionPerformed(evt);
            }
        });
        jPanel5.add(btn100);

        btn101.setBackground(new java.awt.Color(102, 153, 255));
        btn101.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn101.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn101.setMaximumSize(new java.awt.Dimension(20, 20));
        btn101.setPreferredSize(new java.awt.Dimension(20, 20));
        btn101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn101ActionPerformed(evt);
            }
        });
        jPanel5.add(btn101);

        btn102.setBackground(new java.awt.Color(102, 153, 255));
        btn102.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn102.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn102.setMaximumSize(new java.awt.Dimension(20, 20));
        btn102.setPreferredSize(new java.awt.Dimension(20, 20));
        btn102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn102ActionPerformed(evt);
            }
        });
        jPanel5.add(btn102);

        btn103.setBackground(new java.awt.Color(102, 153, 255));
        btn103.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn103.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn103.setMaximumSize(new java.awt.Dimension(20, 20));
        btn103.setPreferredSize(new java.awt.Dimension(20, 20));
        btn103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn103ActionPerformed(evt);
            }
        });
        jPanel5.add(btn103);

        btn104.setBackground(new java.awt.Color(102, 153, 255));
        btn104.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn104.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn104.setMaximumSize(new java.awt.Dimension(20, 20));
        btn104.setPreferredSize(new java.awt.Dimension(20, 20));
        btn104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn104ActionPerformed(evt);
            }
        });
        jPanel5.add(btn104);

        btn105.setBackground(new java.awt.Color(102, 153, 255));
        btn105.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn105.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn105.setMaximumSize(new java.awt.Dimension(20, 20));
        btn105.setPreferredSize(new java.awt.Dimension(20, 20));
        btn105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn105ActionPerformed(evt);
            }
        });
        jPanel5.add(btn105);

        btn106.setBackground(new java.awt.Color(102, 153, 255));
        btn106.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn106.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn106.setMaximumSize(new java.awt.Dimension(20, 20));
        btn106.setPreferredSize(new java.awt.Dimension(20, 20));
        btn106.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn106ActionPerformed(evt);
            }
        });
        jPanel5.add(btn106);

        btn107.setBackground(new java.awt.Color(102, 153, 255));
        btn107.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn107.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn107.setMaximumSize(new java.awt.Dimension(20, 20));
        btn107.setPreferredSize(new java.awt.Dimension(20, 20));
        btn107.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn107ActionPerformed(evt);
            }
        });
        jPanel5.add(btn107);

        btn108.setBackground(new java.awt.Color(102, 153, 255));
        btn108.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn108.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn108.setMaximumSize(new java.awt.Dimension(20, 20));
        btn108.setPreferredSize(new java.awt.Dimension(20, 20));
        btn108.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn108ActionPerformed(evt);
            }
        });
        jPanel5.add(btn108);

        btn109.setBackground(new java.awt.Color(102, 153, 255));
        btn109.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn109.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn109.setMaximumSize(new java.awt.Dimension(20, 20));
        btn109.setPreferredSize(new java.awt.Dimension(20, 20));
        btn109.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn109ActionPerformed(evt);
            }
        });
        jPanel5.add(btn109);

        btn110.setBackground(new java.awt.Color(102, 153, 255));
        btn110.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn110.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn110.setMaximumSize(new java.awt.Dimension(20, 20));
        btn110.setPreferredSize(new java.awt.Dimension(20, 20));
        btn110.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn110ActionPerformed(evt);
            }
        });
        jPanel5.add(btn110);

        btn111.setBackground(new java.awt.Color(102, 153, 255));
        btn111.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn111.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn111.setMaximumSize(new java.awt.Dimension(20, 20));
        btn111.setPreferredSize(new java.awt.Dimension(20, 20));
        btn111.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn111ActionPerformed(evt);
            }
        });
        jPanel5.add(btn111);

        btn112.setBackground(new java.awt.Color(102, 153, 255));
        btn112.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn112.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn112.setMaximumSize(new java.awt.Dimension(20, 20));
        btn112.setPreferredSize(new java.awt.Dimension(20, 20));
        btn112.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn112ActionPerformed(evt);
            }
        });
        jPanel5.add(btn112);

        btn113.setBackground(new java.awt.Color(102, 153, 255));
        btn113.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn113.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn113.setMaximumSize(new java.awt.Dimension(20, 20));
        btn113.setPreferredSize(new java.awt.Dimension(20, 20));
        btn113.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn113ActionPerformed(evt);
            }
        });
        jPanel5.add(btn113);

        btn114.setBackground(new java.awt.Color(102, 153, 255));
        btn114.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn114.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn114.setMaximumSize(new java.awt.Dimension(20, 20));
        btn114.setPreferredSize(new java.awt.Dimension(20, 20));
        btn114.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn114ActionPerformed(evt);
            }
        });
        jPanel5.add(btn114);

        btn115.setBackground(new java.awt.Color(102, 153, 255));
        btn115.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn115.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn115.setMaximumSize(new java.awt.Dimension(20, 20));
        btn115.setPreferredSize(new java.awt.Dimension(20, 20));
        btn115.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn115ActionPerformed(evt);
            }
        });
        jPanel5.add(btn115);

        btn116.setBackground(new java.awt.Color(102, 153, 255));
        btn116.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn116.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn116.setMaximumSize(new java.awt.Dimension(20, 20));
        btn116.setPreferredSize(new java.awt.Dimension(20, 20));
        btn116.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn116ActionPerformed(evt);
            }
        });
        jPanel5.add(btn116);

        btn117.setBackground(new java.awt.Color(102, 153, 255));
        btn117.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn117.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn117.setMaximumSize(new java.awt.Dimension(20, 20));
        btn117.setPreferredSize(new java.awt.Dimension(20, 20));
        btn117.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn117ActionPerformed(evt);
            }
        });
        jPanel5.add(btn117);

        btn118.setBackground(new java.awt.Color(102, 153, 255));
        btn118.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn118.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn118.setMaximumSize(new java.awt.Dimension(20, 20));
        btn118.setPreferredSize(new java.awt.Dimension(20, 20));
        btn118.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn118ActionPerformed(evt);
            }
        });
        jPanel5.add(btn118);

        btn119.setBackground(new java.awt.Color(102, 153, 255));
        btn119.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn119.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn119.setMaximumSize(new java.awt.Dimension(20, 20));
        btn119.setPreferredSize(new java.awt.Dimension(20, 20));
        btn119.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn119ActionPerformed(evt);
            }
        });
        jPanel5.add(btn119);

        btn120.setBackground(new java.awt.Color(102, 153, 255));
        btn120.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn120.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn120.setMaximumSize(new java.awt.Dimension(20, 20));
        btn120.setPreferredSize(new java.awt.Dimension(20, 20));
        btn120.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn120ActionPerformed(evt);
            }
        });
        jPanel5.add(btn120);

        btn121.setBackground(new java.awt.Color(102, 153, 255));
        btn121.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn121.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn121.setMaximumSize(new java.awt.Dimension(20, 20));
        btn121.setPreferredSize(new java.awt.Dimension(20, 20));
        btn121.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn121ActionPerformed(evt);
            }
        });
        jPanel5.add(btn121);

        btn122.setBackground(new java.awt.Color(102, 153, 255));
        btn122.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn122.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn122.setMaximumSize(new java.awt.Dimension(20, 20));
        btn122.setPreferredSize(new java.awt.Dimension(20, 20));
        btn122.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn122ActionPerformed(evt);
            }
        });
        jPanel5.add(btn122);

        btn123.setBackground(new java.awt.Color(102, 153, 255));
        btn123.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn123.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn123.setMaximumSize(new java.awt.Dimension(20, 20));
        btn123.setPreferredSize(new java.awt.Dimension(20, 20));
        btn123.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn123ActionPerformed(evt);
            }
        });
        jPanel5.add(btn123);

        btn124.setBackground(new java.awt.Color(102, 153, 255));
        btn124.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn124.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn124.setMaximumSize(new java.awt.Dimension(20, 20));
        btn124.setPreferredSize(new java.awt.Dimension(20, 20));
        btn124.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn124ActionPerformed(evt);
            }
        });
        jPanel5.add(btn124);

        btn125.setBackground(new java.awt.Color(102, 153, 255));
        btn125.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn125.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn125.setMaximumSize(new java.awt.Dimension(20, 20));
        btn125.setPreferredSize(new java.awt.Dimension(20, 20));
        btn125.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn125ActionPerformed(evt);
            }
        });
        jPanel5.add(btn125);

        btn126.setBackground(new java.awt.Color(102, 153, 255));
        btn126.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn126.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn126.setMaximumSize(new java.awt.Dimension(20, 20));
        btn126.setPreferredSize(new java.awt.Dimension(20, 20));
        btn126.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn126ActionPerformed(evt);
            }
        });
        jPanel5.add(btn126);

        btn127.setBackground(new java.awt.Color(102, 153, 255));
        btn127.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn127.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn127.setMaximumSize(new java.awt.Dimension(20, 20));
        btn127.setPreferredSize(new java.awt.Dimension(20, 20));
        btn127.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn127ActionPerformed(evt);
            }
        });
        jPanel5.add(btn127);

        btn128.setBackground(new java.awt.Color(102, 153, 255));
        btn128.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn128.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn128.setMaximumSize(new java.awt.Dimension(20, 20));
        btn128.setPreferredSize(new java.awt.Dimension(20, 20));
        btn128.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn128ActionPerformed(evt);
            }
        });
        jPanel5.add(btn128);

        btn129.setBackground(new java.awt.Color(102, 153, 255));
        btn129.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn129.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn129.setMaximumSize(new java.awt.Dimension(20, 20));
        btn129.setPreferredSize(new java.awt.Dimension(20, 20));
        btn129.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn129ActionPerformed(evt);
            }
        });
        jPanel5.add(btn129);

        btn130.setBackground(new java.awt.Color(102, 153, 255));
        btn130.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn130.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn130.setMaximumSize(new java.awt.Dimension(20, 20));
        btn130.setPreferredSize(new java.awt.Dimension(20, 20));
        btn130.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn130ActionPerformed(evt);
            }
        });
        jPanel5.add(btn130);

        btn131.setBackground(new java.awt.Color(102, 153, 255));
        btn131.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn131.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn131.setMaximumSize(new java.awt.Dimension(20, 20));
        btn131.setPreferredSize(new java.awt.Dimension(20, 20));
        btn131.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn131ActionPerformed(evt);
            }
        });
        jPanel5.add(btn131);

        btn132.setBackground(new java.awt.Color(102, 153, 255));
        btn132.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn132.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn132.setMaximumSize(new java.awt.Dimension(20, 20));
        btn132.setPreferredSize(new java.awt.Dimension(20, 20));
        btn132.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn132ActionPerformed(evt);
            }
        });
        jPanel5.add(btn132);

        btn133.setBackground(new java.awt.Color(102, 153, 255));
        btn133.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn133.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn133.setMaximumSize(new java.awt.Dimension(20, 20));
        btn133.setPreferredSize(new java.awt.Dimension(20, 20));
        btn133.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn133ActionPerformed(evt);
            }
        });
        jPanel5.add(btn133);

        btn134.setBackground(new java.awt.Color(102, 153, 255));
        btn134.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn134.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn134.setMaximumSize(new java.awt.Dimension(20, 20));
        btn134.setPreferredSize(new java.awt.Dimension(20, 20));
        btn134.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn134ActionPerformed(evt);
            }
        });
        jPanel5.add(btn134);

        btn135.setBackground(new java.awt.Color(102, 153, 255));
        btn135.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn135.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn135.setMaximumSize(new java.awt.Dimension(20, 20));
        btn135.setPreferredSize(new java.awt.Dimension(20, 20));
        btn135.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn135ActionPerformed(evt);
            }
        });
        jPanel5.add(btn135);

        btn136.setBackground(new java.awt.Color(102, 153, 255));
        btn136.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn136.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn136.setMaximumSize(new java.awt.Dimension(20, 20));
        btn136.setPreferredSize(new java.awt.Dimension(20, 20));
        btn136.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn136ActionPerformed(evt);
            }
        });
        jPanel5.add(btn136);

        btn137.setBackground(new java.awt.Color(102, 153, 255));
        btn137.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn137.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn137.setMaximumSize(new java.awt.Dimension(20, 20));
        btn137.setPreferredSize(new java.awt.Dimension(20, 20));
        btn137.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn137ActionPerformed(evt);
            }
        });
        jPanel5.add(btn137);

        btn138.setBackground(new java.awt.Color(102, 153, 255));
        btn138.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn138.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn138.setMaximumSize(new java.awt.Dimension(20, 20));
        btn138.setPreferredSize(new java.awt.Dimension(20, 20));
        btn138.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn138ActionPerformed(evt);
            }
        });
        jPanel5.add(btn138);

        btn139.setBackground(new java.awt.Color(102, 153, 255));
        btn139.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn139.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn139.setMaximumSize(new java.awt.Dimension(20, 20));
        btn139.setPreferredSize(new java.awt.Dimension(20, 20));
        btn139.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn139ActionPerformed(evt);
            }
        });
        jPanel5.add(btn139);

        btn140.setBackground(new java.awt.Color(102, 153, 255));
        btn140.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn140.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn140.setMaximumSize(new java.awt.Dimension(20, 20));
        btn140.setPreferredSize(new java.awt.Dimension(20, 20));
        btn140.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn140ActionPerformed(evt);
            }
        });
        jPanel5.add(btn140);

        btn141.setBackground(new java.awt.Color(102, 153, 255));
        btn141.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn141.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn141.setMaximumSize(new java.awt.Dimension(20, 20));
        btn141.setPreferredSize(new java.awt.Dimension(20, 20));
        btn141.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn141ActionPerformed(evt);
            }
        });
        jPanel5.add(btn141);

        btn142.setBackground(new java.awt.Color(102, 153, 255));
        btn142.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn142.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn142.setMaximumSize(new java.awt.Dimension(20, 20));
        btn142.setPreferredSize(new java.awt.Dimension(20, 20));
        btn142.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn142ActionPerformed(evt);
            }
        });
        jPanel5.add(btn142);

        btn143.setBackground(new java.awt.Color(102, 153, 255));
        btn143.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn143.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn143.setMaximumSize(new java.awt.Dimension(20, 20));
        btn143.setPreferredSize(new java.awt.Dimension(20, 20));
        btn143.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn143ActionPerformed(evt);
            }
        });
        jPanel5.add(btn143);

        btn144.setBackground(new java.awt.Color(102, 153, 255));
        btn144.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn144.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn144.setMaximumSize(new java.awt.Dimension(20, 20));
        btn144.setPreferredSize(new java.awt.Dimension(20, 20));
        btn144.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn144ActionPerformed(evt);
            }
        });
        jPanel5.add(btn144);

        btn145.setBackground(new java.awt.Color(102, 153, 255));
        btn145.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn145.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn145.setMaximumSize(new java.awt.Dimension(20, 20));
        btn145.setPreferredSize(new java.awt.Dimension(20, 20));
        btn145.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn145ActionPerformed(evt);
            }
        });
        jPanel5.add(btn145);

        btn146.setBackground(new java.awt.Color(102, 153, 255));
        btn146.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn146.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn146.setMaximumSize(new java.awt.Dimension(20, 20));
        btn146.setPreferredSize(new java.awt.Dimension(20, 20));
        btn146.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn146ActionPerformed(evt);
            }
        });
        jPanel5.add(btn146);

        btn147.setBackground(new java.awt.Color(102, 153, 255));
        btn147.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn147.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn147.setMaximumSize(new java.awt.Dimension(20, 20));
        btn147.setPreferredSize(new java.awt.Dimension(20, 20));
        btn147.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn147ActionPerformed(evt);
            }
        });
        jPanel5.add(btn147);

        btn148.setBackground(new java.awt.Color(102, 153, 255));
        btn148.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn148.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn148.setMaximumSize(new java.awt.Dimension(20, 20));
        btn148.setPreferredSize(new java.awt.Dimension(20, 20));
        btn148.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn148ActionPerformed(evt);
            }
        });
        jPanel5.add(btn148);

        btn149.setBackground(new java.awt.Color(102, 153, 255));
        btn149.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn149.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn149.setMaximumSize(new java.awt.Dimension(20, 20));
        btn149.setPreferredSize(new java.awt.Dimension(20, 20));
        btn149.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn149ActionPerformed(evt);
            }
        });
        jPanel5.add(btn149);

        btn150.setBackground(new java.awt.Color(102, 153, 255));
        btn150.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn150.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn150.setMaximumSize(new java.awt.Dimension(20, 20));
        btn150.setPreferredSize(new java.awt.Dimension(20, 20));
        btn150.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn150ActionPerformed(evt);
            }
        });
        jPanel5.add(btn150);

        btn151.setBackground(new java.awt.Color(102, 153, 255));
        btn151.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn151.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn151.setMaximumSize(new java.awt.Dimension(20, 20));
        btn151.setPreferredSize(new java.awt.Dimension(20, 20));
        btn151.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn151ActionPerformed(evt);
            }
        });
        jPanel5.add(btn151);

        btn152.setBackground(new java.awt.Color(102, 153, 255));
        btn152.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn152.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn152.setMaximumSize(new java.awt.Dimension(20, 20));
        btn152.setPreferredSize(new java.awt.Dimension(20, 20));
        btn152.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn152ActionPerformed(evt);
            }
        });
        jPanel5.add(btn152);

        btn153.setBackground(new java.awt.Color(102, 153, 255));
        btn153.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn153.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn153.setMaximumSize(new java.awt.Dimension(20, 20));
        btn153.setPreferredSize(new java.awt.Dimension(20, 20));
        btn153.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn153ActionPerformed(evt);
            }
        });
        jPanel5.add(btn153);

        btn154.setBackground(new java.awt.Color(102, 153, 255));
        btn154.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn154.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn154.setMaximumSize(new java.awt.Dimension(20, 20));
        btn154.setPreferredSize(new java.awt.Dimension(20, 20));
        btn154.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn154ActionPerformed(evt);
            }
        });
        jPanel5.add(btn154);

        btn155.setBackground(new java.awt.Color(102, 153, 255));
        btn155.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn155.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn155.setMaximumSize(new java.awt.Dimension(20, 20));
        btn155.setPreferredSize(new java.awt.Dimension(20, 20));
        btn155.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn155ActionPerformed(evt);
            }
        });
        jPanel5.add(btn155);

        btn156.setBackground(new java.awt.Color(102, 153, 255));
        btn156.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn156.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn156.setMaximumSize(new java.awt.Dimension(20, 20));
        btn156.setPreferredSize(new java.awt.Dimension(20, 20));
        btn156.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn156ActionPerformed(evt);
            }
        });
        jPanel5.add(btn156);

        btn157.setBackground(new java.awt.Color(102, 153, 255));
        btn157.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn157.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn157.setMaximumSize(new java.awt.Dimension(20, 20));
        btn157.setPreferredSize(new java.awt.Dimension(20, 20));
        btn157.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn157ActionPerformed(evt);
            }
        });
        jPanel5.add(btn157);

        btn158.setBackground(new java.awt.Color(102, 153, 255));
        btn158.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn158.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn158.setMaximumSize(new java.awt.Dimension(20, 20));
        btn158.setPreferredSize(new java.awt.Dimension(20, 20));
        btn158.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn158ActionPerformed(evt);
            }
        });
        jPanel5.add(btn158);

        btn159.setBackground(new java.awt.Color(102, 153, 255));
        btn159.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn159.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn159.setMaximumSize(new java.awt.Dimension(20, 20));
        btn159.setPreferredSize(new java.awt.Dimension(20, 20));
        btn159.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn159ActionPerformed(evt);
            }
        });
        jPanel5.add(btn159);

        btn160.setBackground(new java.awt.Color(102, 153, 255));
        btn160.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn160.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn160.setMaximumSize(new java.awt.Dimension(20, 20));
        btn160.setPreferredSize(new java.awt.Dimension(20, 20));
        btn160.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn160ActionPerformed(evt);
            }
        });
        jPanel5.add(btn160);

        btn161.setBackground(new java.awt.Color(102, 153, 255));
        btn161.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn161.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn161.setMaximumSize(new java.awt.Dimension(20, 20));
        btn161.setPreferredSize(new java.awt.Dimension(20, 20));
        btn161.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn161ActionPerformed(evt);
            }
        });
        jPanel5.add(btn161);

        btn162.setBackground(new java.awt.Color(102, 153, 255));
        btn162.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn162.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn162.setMaximumSize(new java.awt.Dimension(20, 20));
        btn162.setPreferredSize(new java.awt.Dimension(20, 20));
        btn162.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn162ActionPerformed(evt);
            }
        });
        jPanel5.add(btn162);

        btn163.setBackground(new java.awt.Color(102, 153, 255));
        btn163.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn163.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn163.setMaximumSize(new java.awt.Dimension(20, 20));
        btn163.setPreferredSize(new java.awt.Dimension(20, 20));
        btn163.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn163ActionPerformed(evt);
            }
        });
        jPanel5.add(btn163);

        btn164.setBackground(new java.awt.Color(102, 153, 255));
        btn164.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn164.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn164.setMaximumSize(new java.awt.Dimension(20, 20));
        btn164.setPreferredSize(new java.awt.Dimension(20, 20));
        btn164.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn164ActionPerformed(evt);
            }
        });
        jPanel5.add(btn164);

        btn165.setBackground(new java.awt.Color(102, 153, 255));
        btn165.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn165.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn165.setMaximumSize(new java.awt.Dimension(20, 20));
        btn165.setPreferredSize(new java.awt.Dimension(20, 20));
        btn165.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn165ActionPerformed(evt);
            }
        });
        jPanel5.add(btn165);

        btn166.setBackground(new java.awt.Color(102, 153, 255));
        btn166.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn166.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn166.setMaximumSize(new java.awt.Dimension(20, 20));
        btn166.setPreferredSize(new java.awt.Dimension(20, 20));
        btn166.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn166ActionPerformed(evt);
            }
        });
        jPanel5.add(btn166);

        btn167.setBackground(new java.awt.Color(102, 153, 255));
        btn167.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn167.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn167.setMaximumSize(new java.awt.Dimension(20, 20));
        btn167.setPreferredSize(new java.awt.Dimension(20, 20));
        btn167.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn167ActionPerformed(evt);
            }
        });
        jPanel5.add(btn167);

        btn168.setBackground(new java.awt.Color(102, 153, 255));
        btn168.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn168.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn168.setMaximumSize(new java.awt.Dimension(20, 20));
        btn168.setPreferredSize(new java.awt.Dimension(20, 20));
        btn168.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn168ActionPerformed(evt);
            }
        });
        jPanel5.add(btn168);

        btn169.setBackground(new java.awt.Color(102, 153, 255));
        btn169.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn169.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn169.setMaximumSize(new java.awt.Dimension(20, 20));
        btn169.setPreferredSize(new java.awt.Dimension(20, 20));
        btn169.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn169ActionPerformed(evt);
            }
        });
        jPanel5.add(btn169);

        btn170.setBackground(new java.awt.Color(102, 153, 255));
        btn170.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn170.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn170.setMaximumSize(new java.awt.Dimension(20, 20));
        btn170.setPreferredSize(new java.awt.Dimension(20, 20));
        btn170.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn170ActionPerformed(evt);
            }
        });
        jPanel5.add(btn170);

        btn171.setBackground(new java.awt.Color(102, 153, 255));
        btn171.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn171.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn171.setMaximumSize(new java.awt.Dimension(20, 20));
        btn171.setPreferredSize(new java.awt.Dimension(20, 20));
        btn171.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn171ActionPerformed(evt);
            }
        });
        jPanel5.add(btn171);

        btn172.setBackground(new java.awt.Color(102, 153, 255));
        btn172.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn172.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn172.setMaximumSize(new java.awt.Dimension(20, 20));
        btn172.setPreferredSize(new java.awt.Dimension(20, 20));
        btn172.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn172ActionPerformed(evt);
            }
        });
        jPanel5.add(btn172);

        btn173.setBackground(new java.awt.Color(102, 153, 255));
        btn173.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn173.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn173.setMaximumSize(new java.awt.Dimension(20, 20));
        btn173.setPreferredSize(new java.awt.Dimension(20, 20));
        btn173.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn173ActionPerformed(evt);
            }
        });
        jPanel5.add(btn173);

        btn174.setBackground(new java.awt.Color(102, 153, 255));
        btn174.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn174.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn174.setMaximumSize(new java.awt.Dimension(20, 20));
        btn174.setPreferredSize(new java.awt.Dimension(20, 20));
        btn174.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn174ActionPerformed(evt);
            }
        });
        jPanel5.add(btn174);

        btn175.setBackground(new java.awt.Color(102, 153, 255));
        btn175.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn175.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn175.setMaximumSize(new java.awt.Dimension(20, 20));
        btn175.setPreferredSize(new java.awt.Dimension(20, 20));
        btn175.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn175ActionPerformed(evt);
            }
        });
        jPanel5.add(btn175);

        btn176.setBackground(new java.awt.Color(102, 153, 255));
        btn176.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn176.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn176.setMaximumSize(new java.awt.Dimension(20, 20));
        btn176.setPreferredSize(new java.awt.Dimension(20, 20));
        btn176.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn176ActionPerformed(evt);
            }
        });
        jPanel5.add(btn176);

        btn177.setBackground(new java.awt.Color(102, 153, 255));
        btn177.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn177.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn177.setMaximumSize(new java.awt.Dimension(20, 20));
        btn177.setPreferredSize(new java.awt.Dimension(20, 20));
        btn177.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn177ActionPerformed(evt);
            }
        });
        jPanel5.add(btn177);

        btn178.setBackground(new java.awt.Color(102, 153, 255));
        btn178.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn178.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn178.setMaximumSize(new java.awt.Dimension(20, 20));
        btn178.setPreferredSize(new java.awt.Dimension(20, 20));
        btn178.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn178ActionPerformed(evt);
            }
        });
        jPanel5.add(btn178);

        btn179.setBackground(new java.awt.Color(102, 153, 255));
        btn179.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn179.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn179.setMaximumSize(new java.awt.Dimension(20, 20));
        btn179.setPreferredSize(new java.awt.Dimension(20, 20));
        btn179.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn179ActionPerformed(evt);
            }
        });
        jPanel5.add(btn179);

        btn180.setBackground(new java.awt.Color(102, 153, 255));
        btn180.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn180.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn180.setMaximumSize(new java.awt.Dimension(20, 20));
        btn180.setPreferredSize(new java.awt.Dimension(20, 20));
        btn180.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn180ActionPerformed(evt);
            }
        });
        jPanel5.add(btn180);

        btn181.setBackground(new java.awt.Color(102, 153, 255));
        btn181.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn181.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn181.setMaximumSize(new java.awt.Dimension(20, 20));
        btn181.setPreferredSize(new java.awt.Dimension(20, 20));
        btn181.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn181ActionPerformed(evt);
            }
        });
        jPanel5.add(btn181);

        btn182.setBackground(new java.awt.Color(102, 153, 255));
        btn182.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn182.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn182.setMaximumSize(new java.awt.Dimension(20, 20));
        btn182.setPreferredSize(new java.awt.Dimension(20, 20));
        btn182.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn182ActionPerformed(evt);
            }
        });
        jPanel5.add(btn182);

        btn183.setBackground(new java.awt.Color(102, 153, 255));
        btn183.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn183.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn183.setMaximumSize(new java.awt.Dimension(20, 20));
        btn183.setPreferredSize(new java.awt.Dimension(20, 20));
        btn183.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn183ActionPerformed(evt);
            }
        });
        jPanel5.add(btn183);

        btn184.setBackground(new java.awt.Color(102, 153, 255));
        btn184.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn184.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn184.setMaximumSize(new java.awt.Dimension(20, 20));
        btn184.setPreferredSize(new java.awt.Dimension(20, 20));
        btn184.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn184ActionPerformed(evt);
            }
        });
        jPanel5.add(btn184);

        btn185.setBackground(new java.awt.Color(102, 153, 255));
        btn185.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn185.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn185.setMaximumSize(new java.awt.Dimension(20, 20));
        btn185.setPreferredSize(new java.awt.Dimension(20, 20));
        btn185.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn185ActionPerformed(evt);
            }
        });
        jPanel5.add(btn185);

        btn186.setBackground(new java.awt.Color(102, 153, 255));
        btn186.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn186.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn186.setMaximumSize(new java.awt.Dimension(20, 20));
        btn186.setPreferredSize(new java.awt.Dimension(20, 20));
        btn186.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn186ActionPerformed(evt);
            }
        });
        jPanel5.add(btn186);

        btn187.setBackground(new java.awt.Color(102, 153, 255));
        btn187.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn187.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn187.setMaximumSize(new java.awt.Dimension(20, 20));
        btn187.setPreferredSize(new java.awt.Dimension(20, 20));
        btn187.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn187ActionPerformed(evt);
            }
        });
        jPanel5.add(btn187);

        btn188.setBackground(new java.awt.Color(102, 153, 255));
        btn188.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn188.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn188.setMaximumSize(new java.awt.Dimension(20, 20));
        btn188.setPreferredSize(new java.awt.Dimension(20, 20));
        btn188.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn188ActionPerformed(evt);
            }
        });
        jPanel5.add(btn188);

        btn189.setBackground(new java.awt.Color(102, 153, 255));
        btn189.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn189.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn189.setMaximumSize(new java.awt.Dimension(20, 20));
        btn189.setPreferredSize(new java.awt.Dimension(20, 20));
        btn189.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn189ActionPerformed(evt);
            }
        });
        jPanel5.add(btn189);

        btn190.setBackground(new java.awt.Color(102, 153, 255));
        btn190.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn190.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn190.setMaximumSize(new java.awt.Dimension(20, 20));
        btn190.setPreferredSize(new java.awt.Dimension(20, 20));
        btn190.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn190ActionPerformed(evt);
            }
        });
        jPanel5.add(btn190);

        btn191.setBackground(new java.awt.Color(102, 153, 255));
        btn191.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn191.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn191.setMaximumSize(new java.awt.Dimension(20, 20));
        btn191.setPreferredSize(new java.awt.Dimension(20, 20));
        btn191.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn191ActionPerformed(evt);
            }
        });
        jPanel5.add(btn191);

        btn192.setBackground(new java.awt.Color(102, 153, 255));
        btn192.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn192.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn192.setMaximumSize(new java.awt.Dimension(20, 20));
        btn192.setPreferredSize(new java.awt.Dimension(20, 20));
        btn192.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn192ActionPerformed(evt);
            }
        });
        jPanel5.add(btn192);

        btn193.setBackground(new java.awt.Color(102, 153, 255));
        btn193.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn193.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn193.setMaximumSize(new java.awt.Dimension(20, 20));
        btn193.setPreferredSize(new java.awt.Dimension(20, 20));
        btn193.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn193ActionPerformed(evt);
            }
        });
        jPanel5.add(btn193);

        btn194.setBackground(new java.awt.Color(102, 153, 255));
        btn194.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn194.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn194.setMaximumSize(new java.awt.Dimension(20, 20));
        btn194.setPreferredSize(new java.awt.Dimension(20, 20));
        btn194.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn194ActionPerformed(evt);
            }
        });
        jPanel5.add(btn194);

        btn195.setBackground(new java.awt.Color(102, 153, 255));
        btn195.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn195.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn195.setMaximumSize(new java.awt.Dimension(20, 20));
        btn195.setPreferredSize(new java.awt.Dimension(20, 20));
        btn195.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn195ActionPerformed(evt);
            }
        });
        jPanel5.add(btn195);

        btn196.setBackground(new java.awt.Color(102, 153, 255));
        btn196.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn196.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn196.setMaximumSize(new java.awt.Dimension(20, 20));
        btn196.setPreferredSize(new java.awt.Dimension(20, 20));
        btn196.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn196ActionPerformed(evt);
            }
        });
        jPanel5.add(btn196);

        btn197.setBackground(new java.awt.Color(102, 153, 255));
        btn197.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn197.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn197.setMaximumSize(new java.awt.Dimension(20, 20));
        btn197.setPreferredSize(new java.awt.Dimension(20, 20));
        btn197.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn197ActionPerformed(evt);
            }
        });
        jPanel5.add(btn197);

        btn198.setBackground(new java.awt.Color(102, 153, 255));
        btn198.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn198.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn198.setMaximumSize(new java.awt.Dimension(20, 20));
        btn198.setPreferredSize(new java.awt.Dimension(20, 20));
        btn198.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn198ActionPerformed(evt);
            }
        });
        jPanel5.add(btn198);

        btn199.setBackground(new java.awt.Color(102, 153, 255));
        btn199.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn199.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn199.setMaximumSize(new java.awt.Dimension(20, 20));
        btn199.setPreferredSize(new java.awt.Dimension(20, 20));
        btn199.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn199ActionPerformed(evt);
            }
        });
        jPanel5.add(btn199);

        btn200.setBackground(new java.awt.Color(102, 153, 255));
        btn200.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn200.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn200.setMaximumSize(new java.awt.Dimension(20, 20));
        btn200.setPreferredSize(new java.awt.Dimension(20, 20));
        btn200.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn200ActionPerformed(evt);
            }
        });
        jPanel5.add(btn200);

        btn201.setBackground(new java.awt.Color(102, 153, 255));
        btn201.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn201.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn201.setMaximumSize(new java.awt.Dimension(20, 20));
        btn201.setPreferredSize(new java.awt.Dimension(20, 20));
        btn201.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn201ActionPerformed(evt);
            }
        });
        jPanel5.add(btn201);

        btn202.setBackground(new java.awt.Color(102, 153, 255));
        btn202.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn202.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn202.setMaximumSize(new java.awt.Dimension(20, 20));
        btn202.setPreferredSize(new java.awt.Dimension(20, 20));
        btn202.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn202ActionPerformed(evt);
            }
        });
        jPanel5.add(btn202);

        btn203.setBackground(new java.awt.Color(102, 153, 255));
        btn203.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn203.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn203.setMaximumSize(new java.awt.Dimension(20, 20));
        btn203.setPreferredSize(new java.awt.Dimension(20, 20));
        btn203.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn203ActionPerformed(evt);
            }
        });
        jPanel5.add(btn203);

        btn204.setBackground(new java.awt.Color(102, 153, 255));
        btn204.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn204.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn204.setMaximumSize(new java.awt.Dimension(20, 20));
        btn204.setPreferredSize(new java.awt.Dimension(20, 20));
        btn204.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn204ActionPerformed(evt);
            }
        });
        jPanel5.add(btn204);

        btn205.setBackground(new java.awt.Color(102, 153, 255));
        btn205.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn205.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn205.setMaximumSize(new java.awt.Dimension(20, 20));
        btn205.setPreferredSize(new java.awt.Dimension(20, 20));
        btn205.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn205ActionPerformed(evt);
            }
        });
        jPanel5.add(btn205);

        btn206.setBackground(new java.awt.Color(102, 153, 255));
        btn206.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn206.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn206.setMaximumSize(new java.awt.Dimension(20, 20));
        btn206.setPreferredSize(new java.awt.Dimension(20, 20));
        btn206.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn206ActionPerformed(evt);
            }
        });
        jPanel5.add(btn206);

        btn207.setBackground(new java.awt.Color(102, 153, 255));
        btn207.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn207.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn207.setMaximumSize(new java.awt.Dimension(20, 20));
        btn207.setPreferredSize(new java.awt.Dimension(20, 20));
        btn207.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn207ActionPerformed(evt);
            }
        });
        jPanel5.add(btn207);

        btn208.setBackground(new java.awt.Color(102, 153, 255));
        btn208.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn208.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn208.setMaximumSize(new java.awt.Dimension(20, 20));
        btn208.setPreferredSize(new java.awt.Dimension(20, 20));
        btn208.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn208ActionPerformed(evt);
            }
        });
        jPanel5.add(btn208);

        btn209.setBackground(new java.awt.Color(102, 153, 255));
        btn209.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn209.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn209.setMaximumSize(new java.awt.Dimension(20, 20));
        btn209.setPreferredSize(new java.awt.Dimension(20, 20));
        btn209.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn209ActionPerformed(evt);
            }
        });
        jPanel5.add(btn209);

        btn210.setBackground(new java.awt.Color(102, 153, 255));
        btn210.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn210.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn210.setMaximumSize(new java.awt.Dimension(20, 20));
        btn210.setPreferredSize(new java.awt.Dimension(20, 20));
        btn210.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn210ActionPerformed(evt);
            }
        });
        jPanel5.add(btn210);

        btn211.setBackground(new java.awt.Color(102, 153, 255));
        btn211.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn211.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn211.setMaximumSize(new java.awt.Dimension(20, 20));
        btn211.setPreferredSize(new java.awt.Dimension(20, 20));
        btn211.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn211ActionPerformed(evt);
            }
        });
        jPanel5.add(btn211);

        btn212.setBackground(new java.awt.Color(102, 153, 255));
        btn212.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn212.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn212.setMaximumSize(new java.awt.Dimension(20, 20));
        btn212.setPreferredSize(new java.awt.Dimension(20, 20));
        btn212.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn212ActionPerformed(evt);
            }
        });
        jPanel5.add(btn212);

        btn213.setBackground(new java.awt.Color(102, 153, 255));
        btn213.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn213.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn213.setMaximumSize(new java.awt.Dimension(20, 20));
        btn213.setPreferredSize(new java.awt.Dimension(20, 20));
        btn213.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn213ActionPerformed(evt);
            }
        });
        jPanel5.add(btn213);

        btn214.setBackground(new java.awt.Color(102, 153, 255));
        btn214.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn214.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn214.setMaximumSize(new java.awt.Dimension(20, 20));
        btn214.setPreferredSize(new java.awt.Dimension(20, 20));
        btn214.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn214ActionPerformed(evt);
            }
        });
        jPanel5.add(btn214);

        btn215.setBackground(new java.awt.Color(102, 153, 255));
        btn215.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn215.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn215.setMaximumSize(new java.awt.Dimension(20, 20));
        btn215.setPreferredSize(new java.awt.Dimension(20, 20));
        btn215.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn215ActionPerformed(evt);
            }
        });
        jPanel5.add(btn215);

        btn216.setBackground(new java.awt.Color(102, 153, 255));
        btn216.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn216.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn216.setMaximumSize(new java.awt.Dimension(20, 20));
        btn216.setPreferredSize(new java.awt.Dimension(20, 20));
        btn216.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn216ActionPerformed(evt);
            }
        });
        jPanel5.add(btn216);

        btn217.setBackground(new java.awt.Color(102, 153, 255));
        btn217.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn217.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn217.setMaximumSize(new java.awt.Dimension(20, 20));
        btn217.setPreferredSize(new java.awt.Dimension(20, 20));
        btn217.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn217ActionPerformed(evt);
            }
        });
        jPanel5.add(btn217);

        btn218.setBackground(new java.awt.Color(102, 153, 255));
        btn218.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn218.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn218.setMaximumSize(new java.awt.Dimension(20, 20));
        btn218.setPreferredSize(new java.awt.Dimension(20, 20));
        btn218.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn218ActionPerformed(evt);
            }
        });
        jPanel5.add(btn218);

        btn219.setBackground(new java.awt.Color(102, 153, 255));
        btn219.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn219.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn219.setMaximumSize(new java.awt.Dimension(20, 20));
        btn219.setPreferredSize(new java.awt.Dimension(20, 20));
        btn219.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn219ActionPerformed(evt);
            }
        });
        jPanel5.add(btn219);

        btn220.setBackground(new java.awt.Color(102, 153, 255));
        btn220.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn220.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn220.setMaximumSize(new java.awt.Dimension(20, 20));
        btn220.setPreferredSize(new java.awt.Dimension(20, 20));
        btn220.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn220ActionPerformed(evt);
            }
        });
        jPanel5.add(btn220);

        btn221.setBackground(new java.awt.Color(102, 153, 255));
        btn221.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn221.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn221.setMaximumSize(new java.awt.Dimension(20, 20));
        btn221.setPreferredSize(new java.awt.Dimension(20, 20));
        btn221.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn221ActionPerformed(evt);
            }
        });
        jPanel5.add(btn221);

        btn222.setBackground(new java.awt.Color(102, 153, 255));
        btn222.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn222.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn222.setMaximumSize(new java.awt.Dimension(20, 20));
        btn222.setPreferredSize(new java.awt.Dimension(20, 20));
        btn222.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn222ActionPerformed(evt);
            }
        });
        jPanel5.add(btn222);

        btn223.setBackground(new java.awt.Color(102, 153, 255));
        btn223.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn223.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn223.setMaximumSize(new java.awt.Dimension(20, 20));
        btn223.setPreferredSize(new java.awt.Dimension(20, 20));
        btn223.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn223ActionPerformed(evt);
            }
        });
        jPanel5.add(btn223);

        btn224.setBackground(new java.awt.Color(102, 153, 255));
        btn224.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn224.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn224.setMaximumSize(new java.awt.Dimension(20, 20));
        btn224.setPreferredSize(new java.awt.Dimension(20, 20));
        btn224.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn224ActionPerformed(evt);
            }
        });
        jPanel5.add(btn224);

        btn225.setBackground(new java.awt.Color(102, 153, 255));
        btn225.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn225.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn225.setMaximumSize(new java.awt.Dimension(20, 20));
        btn225.setPreferredSize(new java.awt.Dimension(20, 20));
        btn225.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn225ActionPerformed(evt);
            }
        });
        jPanel5.add(btn225);

        btn226.setBackground(new java.awt.Color(102, 153, 255));
        btn226.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn226.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn226.setMaximumSize(new java.awt.Dimension(20, 20));
        btn226.setPreferredSize(new java.awt.Dimension(20, 20));
        btn226.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn226ActionPerformed(evt);
            }
        });
        jPanel5.add(btn226);

        btn227.setBackground(new java.awt.Color(102, 153, 255));
        btn227.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn227.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn227.setMaximumSize(new java.awt.Dimension(20, 20));
        btn227.setPreferredSize(new java.awt.Dimension(20, 20));
        btn227.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn227ActionPerformed(evt);
            }
        });
        jPanel5.add(btn227);

        btn228.setBackground(new java.awt.Color(102, 153, 255));
        btn228.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn228.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn228.setMaximumSize(new java.awt.Dimension(20, 20));
        btn228.setPreferredSize(new java.awt.Dimension(20, 20));
        btn228.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn228ActionPerformed(evt);
            }
        });
        jPanel5.add(btn228);

        btn229.setBackground(new java.awt.Color(102, 153, 255));
        btn229.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn229.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn229.setMaximumSize(new java.awt.Dimension(20, 20));
        btn229.setPreferredSize(new java.awt.Dimension(20, 20));
        btn229.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn229ActionPerformed(evt);
            }
        });
        jPanel5.add(btn229);

        btn230.setBackground(new java.awt.Color(102, 153, 255));
        btn230.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn230.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn230.setMaximumSize(new java.awt.Dimension(20, 20));
        btn230.setPreferredSize(new java.awt.Dimension(20, 20));
        btn230.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn230ActionPerformed(evt);
            }
        });
        jPanel5.add(btn230);

        btn231.setBackground(new java.awt.Color(102, 153, 255));
        btn231.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn231.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn231.setMaximumSize(new java.awt.Dimension(20, 20));
        btn231.setPreferredSize(new java.awt.Dimension(20, 20));
        btn231.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn231ActionPerformed(evt);
            }
        });
        jPanel5.add(btn231);

        btn232.setBackground(new java.awt.Color(102, 153, 255));
        btn232.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn232.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn232.setMaximumSize(new java.awt.Dimension(20, 20));
        btn232.setPreferredSize(new java.awt.Dimension(20, 20));
        btn232.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn232ActionPerformed(evt);
            }
        });
        jPanel5.add(btn232);

        btn233.setBackground(new java.awt.Color(102, 153, 255));
        btn233.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn233.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn233.setMaximumSize(new java.awt.Dimension(20, 20));
        btn233.setPreferredSize(new java.awt.Dimension(20, 20));
        btn233.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn233ActionPerformed(evt);
            }
        });
        jPanel5.add(btn233);

        btn234.setBackground(new java.awt.Color(102, 153, 255));
        btn234.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn234.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn234.setMaximumSize(new java.awt.Dimension(20, 20));
        btn234.setPreferredSize(new java.awt.Dimension(20, 20));
        btn234.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn234ActionPerformed(evt);
            }
        });
        jPanel5.add(btn234);

        btn235.setBackground(new java.awt.Color(102, 153, 255));
        btn235.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn235.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn235.setMaximumSize(new java.awt.Dimension(20, 20));
        btn235.setPreferredSize(new java.awt.Dimension(20, 20));
        btn235.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn235ActionPerformed(evt);
            }
        });
        jPanel5.add(btn235);

        btn236.setBackground(new java.awt.Color(102, 153, 255));
        btn236.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn236.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn236.setMaximumSize(new java.awt.Dimension(20, 20));
        btn236.setPreferredSize(new java.awt.Dimension(20, 20));
        btn236.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn236ActionPerformed(evt);
            }
        });
        jPanel5.add(btn236);

        btn237.setBackground(new java.awt.Color(102, 153, 255));
        btn237.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn237.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn237.setMaximumSize(new java.awt.Dimension(20, 20));
        btn237.setPreferredSize(new java.awt.Dimension(20, 20));
        btn237.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn237ActionPerformed(evt);
            }
        });
        jPanel5.add(btn237);

        btn238.setBackground(new java.awt.Color(102, 153, 255));
        btn238.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn238.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn238.setMaximumSize(new java.awt.Dimension(20, 20));
        btn238.setPreferredSize(new java.awt.Dimension(20, 20));
        btn238.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn238ActionPerformed(evt);
            }
        });
        jPanel5.add(btn238);

        btn239.setBackground(new java.awt.Color(102, 153, 255));
        btn239.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn239.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn239.setMaximumSize(new java.awt.Dimension(20, 20));
        btn239.setPreferredSize(new java.awt.Dimension(20, 20));
        btn239.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn239ActionPerformed(evt);
            }
        });
        jPanel5.add(btn239);

        btn240.setBackground(new java.awt.Color(102, 153, 255));
        btn240.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn240.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn240.setMaximumSize(new java.awt.Dimension(20, 20));
        btn240.setPreferredSize(new java.awt.Dimension(20, 20));
        btn240.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn240ActionPerformed(evt);
            }
        });
        jPanel5.add(btn240);

        btn241.setBackground(new java.awt.Color(102, 153, 255));
        btn241.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn241.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn241.setMaximumSize(new java.awt.Dimension(20, 20));
        btn241.setPreferredSize(new java.awt.Dimension(20, 20));
        btn241.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn241ActionPerformed(evt);
            }
        });
        jPanel5.add(btn241);

        btn242.setBackground(new java.awt.Color(102, 153, 255));
        btn242.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn242.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn242.setMaximumSize(new java.awt.Dimension(20, 20));
        btn242.setPreferredSize(new java.awt.Dimension(20, 20));
        btn242.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn242ActionPerformed(evt);
            }
        });
        jPanel5.add(btn242);

        btn243.setBackground(new java.awt.Color(102, 153, 255));
        btn243.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn243.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn243.setMaximumSize(new java.awt.Dimension(20, 20));
        btn243.setPreferredSize(new java.awt.Dimension(20, 20));
        btn243.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn243ActionPerformed(evt);
            }
        });
        jPanel5.add(btn243);

        btn244.setBackground(new java.awt.Color(102, 153, 255));
        btn244.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn244.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn244.setMaximumSize(new java.awt.Dimension(20, 20));
        btn244.setPreferredSize(new java.awt.Dimension(20, 20));
        btn244.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn244ActionPerformed(evt);
            }
        });
        jPanel5.add(btn244);

        btn245.setBackground(new java.awt.Color(102, 153, 255));
        btn245.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn245.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn245.setMaximumSize(new java.awt.Dimension(20, 20));
        btn245.setPreferredSize(new java.awt.Dimension(20, 20));
        btn245.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn245ActionPerformed(evt);
            }
        });
        jPanel5.add(btn245);

        btn246.setBackground(new java.awt.Color(102, 153, 255));
        btn246.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn246.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn246.setMaximumSize(new java.awt.Dimension(20, 20));
        btn246.setPreferredSize(new java.awt.Dimension(20, 20));
        btn246.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn246ActionPerformed(evt);
            }
        });
        jPanel5.add(btn246);

        btn247.setBackground(new java.awt.Color(102, 153, 255));
        btn247.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn247.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn247.setMaximumSize(new java.awt.Dimension(20, 20));
        btn247.setPreferredSize(new java.awt.Dimension(20, 20));
        btn247.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn247ActionPerformed(evt);
            }
        });
        jPanel5.add(btn247);

        btn248.setBackground(new java.awt.Color(102, 153, 255));
        btn248.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn248.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn248.setMaximumSize(new java.awt.Dimension(20, 20));
        btn248.setPreferredSize(new java.awt.Dimension(20, 20));
        btn248.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn248ActionPerformed(evt);
            }
        });
        jPanel5.add(btn248);

        btn249.setBackground(new java.awt.Color(102, 153, 255));
        btn249.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn249.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn249.setMaximumSize(new java.awt.Dimension(20, 20));
        btn249.setPreferredSize(new java.awt.Dimension(20, 20));
        btn249.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn249ActionPerformed(evt);
            }
        });
        jPanel5.add(btn249);

        btn250.setBackground(new java.awt.Color(102, 153, 255));
        btn250.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn250.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn250.setMaximumSize(new java.awt.Dimension(20, 20));
        btn250.setPreferredSize(new java.awt.Dimension(20, 20));
        btn250.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn250ActionPerformed(evt);
            }
        });
        jPanel5.add(btn250);

        btn251.setBackground(new java.awt.Color(102, 153, 255));
        btn251.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn251.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn251.setMaximumSize(new java.awt.Dimension(20, 20));
        btn251.setPreferredSize(new java.awt.Dimension(20, 20));
        btn251.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn251ActionPerformed(evt);
            }
        });
        jPanel5.add(btn251);

        btn252.setBackground(new java.awt.Color(102, 153, 255));
        btn252.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn252.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn252.setMaximumSize(new java.awt.Dimension(20, 20));
        btn252.setPreferredSize(new java.awt.Dimension(20, 20));
        btn252.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn252ActionPerformed(evt);
            }
        });
        jPanel5.add(btn252);

        btn253.setBackground(new java.awt.Color(102, 153, 255));
        btn253.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn253.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn253.setMaximumSize(new java.awt.Dimension(20, 20));
        btn253.setPreferredSize(new java.awt.Dimension(20, 20));
        btn253.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn253ActionPerformed(evt);
            }
        });
        jPanel5.add(btn253);

        btn254.setBackground(new java.awt.Color(102, 153, 255));
        btn254.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn254.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn254.setMaximumSize(new java.awt.Dimension(20, 20));
        btn254.setPreferredSize(new java.awt.Dimension(20, 20));
        btn254.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn254ActionPerformed(evt);
            }
        });
        jPanel5.add(btn254);

        btn255.setBackground(new java.awt.Color(102, 153, 255));
        btn255.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn255.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn255.setMaximumSize(new java.awt.Dimension(20, 20));
        btn255.setPreferredSize(new java.awt.Dimension(20, 20));
        btn255.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn255ActionPerformed(evt);
            }
        });
        jPanel5.add(btn255);

        btn256.setBackground(new java.awt.Color(102, 153, 255));
        btn256.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn256.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn256.setMaximumSize(new java.awt.Dimension(20, 20));
        btn256.setPreferredSize(new java.awt.Dimension(20, 20));
        btn256.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn256ActionPerformed(evt);
            }
        });
        jPanel5.add(btn256);

        btn257.setBackground(new java.awt.Color(102, 153, 255));
        btn257.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn257.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn257.setMaximumSize(new java.awt.Dimension(20, 20));
        btn257.setPreferredSize(new java.awt.Dimension(20, 20));
        btn257.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn257ActionPerformed(evt);
            }
        });
        jPanel5.add(btn257);

        btn258.setBackground(new java.awt.Color(102, 153, 255));
        btn258.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn258.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn258.setMaximumSize(new java.awt.Dimension(20, 20));
        btn258.setPreferredSize(new java.awt.Dimension(20, 20));
        btn258.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn258ActionPerformed(evt);
            }
        });
        jPanel5.add(btn258);

        btn259.setBackground(new java.awt.Color(102, 153, 255));
        btn259.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn259.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn259.setMaximumSize(new java.awt.Dimension(20, 20));
        btn259.setPreferredSize(new java.awt.Dimension(20, 20));
        btn259.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn259ActionPerformed(evt);
            }
        });
        jPanel5.add(btn259);

        btn260.setBackground(new java.awt.Color(102, 153, 255));
        btn260.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn260.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn260.setMaximumSize(new java.awt.Dimension(20, 20));
        btn260.setPreferredSize(new java.awt.Dimension(20, 20));
        btn260.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn260ActionPerformed(evt);
            }
        });
        jPanel5.add(btn260);

        btn261.setBackground(new java.awt.Color(102, 153, 255));
        btn261.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn261.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn261.setMaximumSize(new java.awt.Dimension(20, 20));
        btn261.setPreferredSize(new java.awt.Dimension(20, 20));
        btn261.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn261ActionPerformed(evt);
            }
        });
        jPanel5.add(btn261);

        btn262.setBackground(new java.awt.Color(102, 153, 255));
        btn262.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn262.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn262.setMaximumSize(new java.awt.Dimension(20, 20));
        btn262.setPreferredSize(new java.awt.Dimension(20, 20));
        btn262.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn262ActionPerformed(evt);
            }
        });
        jPanel5.add(btn262);

        btn263.setBackground(new java.awt.Color(102, 153, 255));
        btn263.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn263.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn263.setMaximumSize(new java.awt.Dimension(20, 20));
        btn263.setPreferredSize(new java.awt.Dimension(20, 20));
        btn263.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn263ActionPerformed(evt);
            }
        });
        jPanel5.add(btn263);

        btn264.setBackground(new java.awt.Color(102, 153, 255));
        btn264.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn264.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn264.setMaximumSize(new java.awt.Dimension(20, 20));
        btn264.setPreferredSize(new java.awt.Dimension(20, 20));
        btn264.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn264ActionPerformed(evt);
            }
        });
        jPanel5.add(btn264);

        btn265.setBackground(new java.awt.Color(102, 153, 255));
        btn265.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn265.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn265.setMaximumSize(new java.awt.Dimension(20, 20));
        btn265.setPreferredSize(new java.awt.Dimension(20, 20));
        btn265.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn265ActionPerformed(evt);
            }
        });
        jPanel5.add(btn265);

        btn266.setBackground(new java.awt.Color(102, 153, 255));
        btn266.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn266.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn266.setMaximumSize(new java.awt.Dimension(20, 20));
        btn266.setPreferredSize(new java.awt.Dimension(20, 20));
        btn266.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn266ActionPerformed(evt);
            }
        });
        jPanel5.add(btn266);

        btn267.setBackground(new java.awt.Color(102, 153, 255));
        btn267.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn267.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn267.setMaximumSize(new java.awt.Dimension(20, 20));
        btn267.setPreferredSize(new java.awt.Dimension(20, 20));
        btn267.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn267ActionPerformed(evt);
            }
        });
        jPanel5.add(btn267);

        btn268.setBackground(new java.awt.Color(102, 153, 255));
        btn268.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn268.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn268.setMaximumSize(new java.awt.Dimension(20, 20));
        btn268.setPreferredSize(new java.awt.Dimension(20, 20));
        btn268.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn268ActionPerformed(evt);
            }
        });
        jPanel5.add(btn268);

        btn269.setBackground(new java.awt.Color(102, 153, 255));
        btn269.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn269.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn269.setMaximumSize(new java.awt.Dimension(20, 20));
        btn269.setPreferredSize(new java.awt.Dimension(20, 20));
        btn269.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn269ActionPerformed(evt);
            }
        });
        jPanel5.add(btn269);

        btn270.setBackground(new java.awt.Color(102, 153, 255));
        btn270.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn270.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn270.setMaximumSize(new java.awt.Dimension(20, 20));
        btn270.setPreferredSize(new java.awt.Dimension(20, 20));
        btn270.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn270ActionPerformed(evt);
            }
        });
        jPanel5.add(btn270);

        btn271.setBackground(new java.awt.Color(102, 153, 255));
        btn271.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn271.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn271.setMaximumSize(new java.awt.Dimension(20, 20));
        btn271.setPreferredSize(new java.awt.Dimension(20, 20));
        btn271.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn271ActionPerformed(evt);
            }
        });
        jPanel5.add(btn271);

        btn272.setBackground(new java.awt.Color(102, 153, 255));
        btn272.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn272.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn272.setMaximumSize(new java.awt.Dimension(20, 20));
        btn272.setPreferredSize(new java.awt.Dimension(20, 20));
        btn272.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn272ActionPerformed(evt);
            }
        });
        jPanel5.add(btn272);

        btn273.setBackground(new java.awt.Color(102, 153, 255));
        btn273.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn273.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn273.setMaximumSize(new java.awt.Dimension(20, 20));
        btn273.setPreferredSize(new java.awt.Dimension(20, 20));
        btn273.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn273ActionPerformed(evt);
            }
        });
        jPanel5.add(btn273);

        btn274.setBackground(new java.awt.Color(102, 153, 255));
        btn274.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn274.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn274.setMaximumSize(new java.awt.Dimension(20, 20));
        btn274.setPreferredSize(new java.awt.Dimension(20, 20));
        btn274.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn274ActionPerformed(evt);
            }
        });
        jPanel5.add(btn274);

        btn275.setBackground(new java.awt.Color(102, 153, 255));
        btn275.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn275.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn275.setMaximumSize(new java.awt.Dimension(20, 20));
        btn275.setPreferredSize(new java.awt.Dimension(20, 20));
        btn275.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn275ActionPerformed(evt);
            }
        });
        jPanel5.add(btn275);

        btn276.setBackground(new java.awt.Color(102, 153, 255));
        btn276.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn276.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn276.setMaximumSize(new java.awt.Dimension(20, 20));
        btn276.setPreferredSize(new java.awt.Dimension(20, 20));
        btn276.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn276ActionPerformed(evt);
            }
        });
        jPanel5.add(btn276);

        btn277.setBackground(new java.awt.Color(102, 153, 255));
        btn277.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn277.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn277.setMaximumSize(new java.awt.Dimension(20, 20));
        btn277.setPreferredSize(new java.awt.Dimension(20, 20));
        btn277.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn277ActionPerformed(evt);
            }
        });
        jPanel5.add(btn277);

        btn278.setBackground(new java.awt.Color(102, 153, 255));
        btn278.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn278.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn278.setMaximumSize(new java.awt.Dimension(20, 20));
        btn278.setPreferredSize(new java.awt.Dimension(20, 20));
        btn278.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn278ActionPerformed(evt);
            }
        });
        jPanel5.add(btn278);

        btn279.setBackground(new java.awt.Color(102, 153, 255));
        btn279.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn279.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn279.setMaximumSize(new java.awt.Dimension(20, 20));
        btn279.setPreferredSize(new java.awt.Dimension(20, 20));
        btn279.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn279ActionPerformed(evt);
            }
        });
        jPanel5.add(btn279);

        btn280.setBackground(new java.awt.Color(102, 153, 255));
        btn280.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn280.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn280.setMaximumSize(new java.awt.Dimension(20, 20));
        btn280.setPreferredSize(new java.awt.Dimension(20, 20));
        btn280.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn280ActionPerformed(evt);
            }
        });
        jPanel5.add(btn280);

        btn281.setBackground(new java.awt.Color(102, 153, 255));
        btn281.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn281.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn281.setMaximumSize(new java.awt.Dimension(20, 20));
        btn281.setPreferredSize(new java.awt.Dimension(20, 20));
        btn281.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn281ActionPerformed(evt);
            }
        });
        jPanel5.add(btn281);

        btn282.setBackground(new java.awt.Color(102, 153, 255));
        btn282.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn282.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn282.setMaximumSize(new java.awt.Dimension(20, 20));
        btn282.setPreferredSize(new java.awt.Dimension(20, 20));
        btn282.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn282ActionPerformed(evt);
            }
        });
        jPanel5.add(btn282);

        btn283.setBackground(new java.awt.Color(102, 153, 255));
        btn283.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn283.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn283.setMaximumSize(new java.awt.Dimension(20, 20));
        btn283.setPreferredSize(new java.awt.Dimension(20, 20));
        btn283.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn283ActionPerformed(evt);
            }
        });
        jPanel5.add(btn283);

        btn284.setBackground(new java.awt.Color(102, 153, 255));
        btn284.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn284.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn284.setMaximumSize(new java.awt.Dimension(20, 20));
        btn284.setPreferredSize(new java.awt.Dimension(20, 20));
        btn284.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn284ActionPerformed(evt);
            }
        });
        jPanel5.add(btn284);

        btn285.setBackground(new java.awt.Color(102, 153, 255));
        btn285.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn285.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn285.setMaximumSize(new java.awt.Dimension(20, 20));
        btn285.setPreferredSize(new java.awt.Dimension(20, 20));
        btn285.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn285ActionPerformed(evt);
            }
        });
        jPanel5.add(btn285);

        btn286.setBackground(new java.awt.Color(102, 153, 255));
        btn286.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn286.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn286.setMaximumSize(new java.awt.Dimension(20, 20));
        btn286.setPreferredSize(new java.awt.Dimension(20, 20));
        btn286.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn286ActionPerformed(evt);
            }
        });
        jPanel5.add(btn286);

        btn287.setBackground(new java.awt.Color(102, 153, 255));
        btn287.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn287.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn287.setMaximumSize(new java.awt.Dimension(20, 20));
        btn287.setPreferredSize(new java.awt.Dimension(20, 20));
        btn287.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn287ActionPerformed(evt);
            }
        });
        jPanel5.add(btn287);

        btn288.setBackground(new java.awt.Color(102, 153, 255));
        btn288.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn288.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn288.setMaximumSize(new java.awt.Dimension(20, 20));
        btn288.setPreferredSize(new java.awt.Dimension(20, 20));
        btn288.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn288ActionPerformed(evt);
            }
        });
        jPanel5.add(btn288);

        btn289.setBackground(new java.awt.Color(102, 153, 255));
        btn289.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn289.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn289.setMaximumSize(new java.awt.Dimension(20, 20));
        btn289.setPreferredSize(new java.awt.Dimension(20, 20));
        btn289.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn289ActionPerformed(evt);
            }
        });
        jPanel5.add(btn289);

        btn290.setBackground(new java.awt.Color(102, 153, 255));
        btn290.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn290.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn290.setMaximumSize(new java.awt.Dimension(20, 20));
        btn290.setPreferredSize(new java.awt.Dimension(20, 20));
        btn290.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn290ActionPerformed(evt);
            }
        });
        jPanel5.add(btn290);

        btn291.setBackground(new java.awt.Color(102, 153, 255));
        btn291.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn291.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn291.setMaximumSize(new java.awt.Dimension(20, 20));
        btn291.setPreferredSize(new java.awt.Dimension(20, 20));
        btn291.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn291ActionPerformed(evt);
            }
        });
        jPanel5.add(btn291);

        btn292.setBackground(new java.awt.Color(102, 153, 255));
        btn292.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn292.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn292.setMaximumSize(new java.awt.Dimension(20, 20));
        btn292.setPreferredSize(new java.awt.Dimension(20, 20));
        btn292.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn292ActionPerformed(evt);
            }
        });
        jPanel5.add(btn292);

        btn293.setBackground(new java.awt.Color(102, 153, 255));
        btn293.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn293.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn293.setMaximumSize(new java.awt.Dimension(20, 20));
        btn293.setPreferredSize(new java.awt.Dimension(20, 20));
        btn293.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn293ActionPerformed(evt);
            }
        });
        jPanel5.add(btn293);

        btn294.setBackground(new java.awt.Color(102, 153, 255));
        btn294.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn294.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn294.setMaximumSize(new java.awt.Dimension(20, 20));
        btn294.setPreferredSize(new java.awt.Dimension(20, 20));
        btn294.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn294ActionPerformed(evt);
            }
        });
        jPanel5.add(btn294);

        btn295.setBackground(new java.awt.Color(102, 153, 255));
        btn295.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn295.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn295.setMaximumSize(new java.awt.Dimension(20, 20));
        btn295.setPreferredSize(new java.awt.Dimension(20, 20));
        btn295.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn295ActionPerformed(evt);
            }
        });
        jPanel5.add(btn295);

        btn296.setBackground(new java.awt.Color(102, 153, 255));
        btn296.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn296.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn296.setMaximumSize(new java.awt.Dimension(20, 20));
        btn296.setPreferredSize(new java.awt.Dimension(20, 20));
        btn296.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn296ActionPerformed(evt);
            }
        });
        jPanel5.add(btn296);

        btn297.setBackground(new java.awt.Color(102, 153, 255));
        btn297.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn297.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn297.setMaximumSize(new java.awt.Dimension(20, 20));
        btn297.setPreferredSize(new java.awt.Dimension(20, 20));
        btn297.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn297ActionPerformed(evt);
            }
        });
        jPanel5.add(btn297);

        btn298.setBackground(new java.awt.Color(102, 153, 255));
        btn298.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn298.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn298.setMaximumSize(new java.awt.Dimension(20, 20));
        btn298.setPreferredSize(new java.awt.Dimension(20, 20));
        btn298.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn298ActionPerformed(evt);
            }
        });
        jPanel5.add(btn298);

        btn299.setBackground(new java.awt.Color(102, 153, 255));
        btn299.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn299.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn299.setMaximumSize(new java.awt.Dimension(20, 20));
        btn299.setPreferredSize(new java.awt.Dimension(20, 20));
        btn299.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn299ActionPerformed(evt);
            }
        });
        jPanel5.add(btn299);

        btn300.setBackground(new java.awt.Color(102, 153, 255));
        btn300.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        btn300.setMargin(new java.awt.Insets(-10, -10, -10, -10));
        btn300.setMaximumSize(new java.awt.Dimension(20, 20));
        btn300.setPreferredSize(new java.awt.Dimension(20, 20));
        btn300.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn300ActionPerformed(evt);
            }
        });
        jPanel5.add(btn300);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn105ActionPerformed
        value = 105;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn105ActionPerformed

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        value = 1;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn1ActionPerformed

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        value = 2;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn2ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        value = 3;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn3ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        value = 4;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn4ActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        value = 5;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn5ActionPerformed

    private void btn6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn6ActionPerformed
        value = 6;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn6ActionPerformed

    private void btn7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn7ActionPerformed
        value = 7;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn7ActionPerformed

    private void btn8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn8ActionPerformed
        value = 8;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn8ActionPerformed

    private void btn9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn9ActionPerformed
        value = 9;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn9ActionPerformed

    private void btn10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn10ActionPerformed
        value = 10;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn10ActionPerformed

    private void btn11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn11ActionPerformed
        value = 11;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn11ActionPerformed

    private void btn12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn12ActionPerformed
        value = 12;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn12ActionPerformed

    private void btn13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn13ActionPerformed
        value = 13;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn13ActionPerformed

    private void btn14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn14ActionPerformed
        value = 14;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn14ActionPerformed

    private void btn15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn15ActionPerformed
        value = 15;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn15ActionPerformed

    private void btn16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn16ActionPerformed
        value = 16;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn16ActionPerformed

    private void btn17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn17ActionPerformed
        value = 17;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn17ActionPerformed

    private void btn18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn18ActionPerformed
        value = 18;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn18ActionPerformed

    private void btn19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn19ActionPerformed
        value = 19;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn19ActionPerformed

    private void btn20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn20ActionPerformed
        value = 20;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn20ActionPerformed

    private void btn21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn21ActionPerformed
        value = 21;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn21ActionPerformed

    private void btn22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn22ActionPerformed
        value = 22;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn22ActionPerformed

    private void btn23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn23ActionPerformed
        value = 23;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn23ActionPerformed

    private void btn24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn24ActionPerformed
        value = 24;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn24ActionPerformed

    private void btn25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn25ActionPerformed
        value = 25;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn25ActionPerformed

    private void btn26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn26ActionPerformed
        value = 26;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn26ActionPerformed

    private void btn27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn27ActionPerformed
        value = 27;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn27ActionPerformed

    private void btn28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn28ActionPerformed
        value = 28;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn28ActionPerformed

    private void btn29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn29ActionPerformed
        value = 29;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn29ActionPerformed

    private void btn30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn30ActionPerformed
        value = 30;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn30ActionPerformed

    private void btn31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn31ActionPerformed
        value = 31;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn31ActionPerformed

    private void btn32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn32ActionPerformed
        value = 32;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn32ActionPerformed

    private void btn33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn33ActionPerformed
        value = 33;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn33ActionPerformed

    private void btn34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn34ActionPerformed
        value = 34;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn34ActionPerformed

    private void btn35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn35ActionPerformed
        value = 35;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn35ActionPerformed

    private void btn36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn36ActionPerformed
        value = 36;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn36ActionPerformed

    private void btn37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn37ActionPerformed
        value = 37;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn37ActionPerformed

    private void btn38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn38ActionPerformed
        value = 38;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn38ActionPerformed

    private void btn39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn39ActionPerformed
        value = 39;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn39ActionPerformed

    private void btn40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn40ActionPerformed
        value = 40;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn40ActionPerformed

    private void btn41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn41ActionPerformed
        value = 41;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn41ActionPerformed

    private void btn42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn42ActionPerformed
        value = 42;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn42ActionPerformed

    private void btn43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn43ActionPerformed
        value = 43;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn43ActionPerformed

    private void btn44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn44ActionPerformed
        value = 44;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn44ActionPerformed

    private void btn45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn45ActionPerformed
        value = 45;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn45ActionPerformed

    private void btn46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn46ActionPerformed
        value = 46;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn46ActionPerformed

    private void btn47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn47ActionPerformed
        value = 47;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn47ActionPerformed

    private void btn48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn48ActionPerformed
        value = 48;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn48ActionPerformed

    private void btn49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn49ActionPerformed
        value = 49;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn49ActionPerformed

    private void btn50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn50ActionPerformed
        value = 50;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn50ActionPerformed

    private void btn71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn71ActionPerformed
        value = 71;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn71ActionPerformed

    private void btn51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn51ActionPerformed
        value = 51;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn51ActionPerformed

    private void btn52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn52ActionPerformed
        value = 52;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn52ActionPerformed

    private void btn53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn53ActionPerformed
        value = 53;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn53ActionPerformed

    private void btn54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn54ActionPerformed
        value = 54;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn54ActionPerformed

    private void btn55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn55ActionPerformed
        value = 55;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn55ActionPerformed

    private void btn56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn56ActionPerformed
        value = 56;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn56ActionPerformed

    private void btn57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn57ActionPerformed
        value = 57;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn57ActionPerformed

    private void btn58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn58ActionPerformed
        value = 58;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn58ActionPerformed

    private void btn59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn59ActionPerformed
        value = 59;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn59ActionPerformed

    private void btn60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn60ActionPerformed
        value = 60;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn60ActionPerformed

    private void btn61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn61ActionPerformed
        value = 61;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn61ActionPerformed

    private void btn62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn62ActionPerformed
        value = 62;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn62ActionPerformed

    private void btn63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn63ActionPerformed
        value = 63;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn63ActionPerformed

    private void btn64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn64ActionPerformed
        value = 64;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn64ActionPerformed

    private void btn65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn65ActionPerformed
        value = 65;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn65ActionPerformed

    private void btn66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn66ActionPerformed
        value = 66;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn66ActionPerformed

    private void btn67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn67ActionPerformed
        value = 67;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn67ActionPerformed

    private void btn68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn68ActionPerformed
        value = 68;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn68ActionPerformed

    private void btn69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn69ActionPerformed
        value = 69;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn69ActionPerformed

    private void btn70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn70ActionPerformed
        value = 70;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn70ActionPerformed

    private void btn72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn72ActionPerformed
        value = 72;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn72ActionPerformed

    private void btn73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn73ActionPerformed
        value = 73;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn73ActionPerformed

    private void btn74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn74ActionPerformed
        value = 74;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn74ActionPerformed

    private void btn75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn75ActionPerformed
        value = 75;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn75ActionPerformed

    private void btn76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn76ActionPerformed
        value = 76;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn76ActionPerformed

    private void btn77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn77ActionPerformed
        value = 77;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn77ActionPerformed

    private void btn78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn78ActionPerformed
        value = 78;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn78ActionPerformed

    private void btn79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn79ActionPerformed
        value = 79;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn79ActionPerformed

    private void btn80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn80ActionPerformed
        value = 80;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn80ActionPerformed

    private void btn81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn81ActionPerformed
        value = 81;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn81ActionPerformed

    private void btn82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn82ActionPerformed
        value = 82;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn82ActionPerformed

    private void btn83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn83ActionPerformed
        value = 83;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn83ActionPerformed

    private void btn84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn84ActionPerformed
        value = 84;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn84ActionPerformed

    private void btn85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn85ActionPerformed
        value = 85;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn85ActionPerformed

    private void btn86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn86ActionPerformed
        value = 86;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn86ActionPerformed

    private void btn87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn87ActionPerformed
        value = 87;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn87ActionPerformed

    private void btn88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn88ActionPerformed
        value = 88;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn88ActionPerformed

    private void btn89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn89ActionPerformed
        value = 89;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn89ActionPerformed

    private void btn90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn90ActionPerformed
        value = 90;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn90ActionPerformed

    private void btn91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn91ActionPerformed
        value = 91;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn91ActionPerformed

    private void btn92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn92ActionPerformed
        value = 92;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn92ActionPerformed

    private void btn93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn93ActionPerformed
        value = 93;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn93ActionPerformed

    private void btn94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn94ActionPerformed
        value = 94;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn94ActionPerformed

    private void btn95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn95ActionPerformed
        value = 95;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn95ActionPerformed

    private void btn96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn96ActionPerformed
        value = 96;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn96ActionPerformed

    private void btn97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn97ActionPerformed
        value = 97;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn97ActionPerformed

    private void btn98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn98ActionPerformed
        value = 98;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn98ActionPerformed

    private void btn99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn99ActionPerformed
        value = 99;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn99ActionPerformed

    private void btn100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn100ActionPerformed
        value = 100;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn100ActionPerformed

    private void btn120ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn120ActionPerformed
        value = 120;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn120ActionPerformed

    private void btn119ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn119ActionPerformed
        value = 119;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn119ActionPerformed

    private void btn118ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn118ActionPerformed
        value = 118;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn118ActionPerformed

    private void btn117ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn117ActionPerformed
        value = 117;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn117ActionPerformed

    private void btn116ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn116ActionPerformed
        value = 116;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn116ActionPerformed

    private void btn115ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn115ActionPerformed
        value = 115;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn115ActionPerformed

    private void btn114ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn114ActionPerformed
        value = 114;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn114ActionPerformed

    private void btn113ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn113ActionPerformed
        value = 113;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn113ActionPerformed

    private void btn112ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn112ActionPerformed
        value = 112;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn112ActionPerformed

    private void btn111ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn111ActionPerformed
        value = 111;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn111ActionPerformed

    private void btn110ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn110ActionPerformed
        value = 110;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn110ActionPerformed

    private void btn109ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn109ActionPerformed
        value = 109;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn109ActionPerformed

    private void btn108ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn108ActionPerformed
        value = 108;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn108ActionPerformed

    private void btn107ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn107ActionPerformed
        value = 107;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn107ActionPerformed

    private void btn106ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn106ActionPerformed
        value = 106;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn106ActionPerformed

    private void btn104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn104ActionPerformed
        value = 104;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn104ActionPerformed

    private void btn103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn103ActionPerformed
        value = 103;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn103ActionPerformed

    private void btn102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn102ActionPerformed
        value = 102;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn102ActionPerformed

    private void btn101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn101ActionPerformed
        value = 101;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn101ActionPerformed

    private void btn121ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn121ActionPerformed
        value = 121;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn121ActionPerformed

    private void btn122ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn122ActionPerformed
        value = 122;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn122ActionPerformed

    private void btn123ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn123ActionPerformed
        value = 123;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn123ActionPerformed

    private void btn124ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn124ActionPerformed
        value = 124;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn124ActionPerformed

    private void btn125ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn125ActionPerformed
        value = 125;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn125ActionPerformed

    private void btn126ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn126ActionPerformed
        value = 126;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn126ActionPerformed

    private void btn127ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn127ActionPerformed
        value = 127;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn127ActionPerformed

    private void btn128ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn128ActionPerformed
        value = 128;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn128ActionPerformed

    private void btn129ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn129ActionPerformed
        value = 129;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn129ActionPerformed

    private void btn130ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn130ActionPerformed
        value = 130;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn130ActionPerformed

    private void btn131ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn131ActionPerformed
        value = 131;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn131ActionPerformed

    private void btn132ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn132ActionPerformed
        value = 132;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn132ActionPerformed

    private void btn133ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn133ActionPerformed
        value = 133;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn133ActionPerformed

    private void btn134ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn134ActionPerformed
        value = 134;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn134ActionPerformed

    private void btn135ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn135ActionPerformed
        value = 135;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn135ActionPerformed

    private void btn136ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn136ActionPerformed
        value = 136;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn136ActionPerformed

    private void btn137ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn137ActionPerformed
        value = 137;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn137ActionPerformed

    private void btn138ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn138ActionPerformed
        value = 138;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn138ActionPerformed

    private void btn139ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn139ActionPerformed
        value = 139;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn139ActionPerformed

    private void btn140ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn140ActionPerformed
        value = 140;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn140ActionPerformed

    private void btn180ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn180ActionPerformed
        value = 180;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn180ActionPerformed

    private void btn160ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn160ActionPerformed
        value = 160;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn160ActionPerformed

    private void btn159ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn159ActionPerformed
        value = 159;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn159ActionPerformed

    private void btn158ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn158ActionPerformed
        value = 158;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn158ActionPerformed

    private void btn157ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn157ActionPerformed
        value = 157;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn157ActionPerformed

    private void btn156ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn156ActionPerformed
        value = 156;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn156ActionPerformed

    private void btn155ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn155ActionPerformed
        value = 155;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn155ActionPerformed

    private void btn154ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn154ActionPerformed
        value = 154;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn154ActionPerformed

    private void btn153ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn153ActionPerformed
        value = 153;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn153ActionPerformed

    private void btn152ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn152ActionPerformed
        value = 152;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn152ActionPerformed

    private void btn151ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn151ActionPerformed
        value = 151;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn151ActionPerformed

    private void btn150ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn150ActionPerformed
        value = 150;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn150ActionPerformed

    private void btn149ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn149ActionPerformed
        value = 149;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn149ActionPerformed

    private void btn148ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn148ActionPerformed
        value = 148;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn148ActionPerformed

    private void btn147ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn147ActionPerformed
        value = 147;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn147ActionPerformed

    private void btn146ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn146ActionPerformed
        value = 146;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn146ActionPerformed

    private void btn145ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn145ActionPerformed
        value = 145;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn145ActionPerformed

    private void btn144ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn144ActionPerformed
        value = 144;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn144ActionPerformed

    private void btn143ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn143ActionPerformed
        value = 143;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn143ActionPerformed

    private void btn142ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn142ActionPerformed
        value = 142;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn142ActionPerformed

    private void btn141ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn141ActionPerformed
        value = 141;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn141ActionPerformed

    private void btn161ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn161ActionPerformed
        value = 161;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn161ActionPerformed

    private void btn162ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn162ActionPerformed
        value = 162;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn162ActionPerformed

    private void btn163ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn163ActionPerformed
        value = 163;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn163ActionPerformed

    private void btn164ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn164ActionPerformed
        value = 164;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn164ActionPerformed

    private void btn165ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn165ActionPerformed
        value = 165;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn165ActionPerformed

    private void btn166ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn166ActionPerformed
        value = 166;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn166ActionPerformed

    private void btn167ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn167ActionPerformed
        value = 167;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn167ActionPerformed

    private void btn168ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn168ActionPerformed
        value = 168;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn168ActionPerformed

    private void btn169ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn169ActionPerformed
        value = 169;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn169ActionPerformed

    private void btn170ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn170ActionPerformed
        value = 170;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn170ActionPerformed

    private void btn171ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn171ActionPerformed
        value = 171;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn171ActionPerformed

    private void btn172ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn172ActionPerformed
        value = 172;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn172ActionPerformed

    private void btn173ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn173ActionPerformed
        value = 173;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn173ActionPerformed

    private void btn174ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn174ActionPerformed
        value = 174;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn174ActionPerformed

    private void btn175ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn175ActionPerformed
        value = 175;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn175ActionPerformed

    private void btn176ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn176ActionPerformed
        value = 176;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn176ActionPerformed

    private void btn177ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn177ActionPerformed
        value = 177;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn177ActionPerformed

    private void btn178ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn178ActionPerformed
        value = 178;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn178ActionPerformed

    private void btn179ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn179ActionPerformed
        value = 179;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn179ActionPerformed

    private void btn200ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn200ActionPerformed
        value = 200;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn200ActionPerformed

    private void btn199ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn199ActionPerformed
        value = 199;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn199ActionPerformed

    private void btn198ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn198ActionPerformed
        value = 198;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn198ActionPerformed

    private void btn197ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn197ActionPerformed
        value = 197;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn197ActionPerformed

    private void btn196ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn196ActionPerformed
        value = 196;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn196ActionPerformed

    private void btn195ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn195ActionPerformed
        value = 195;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn195ActionPerformed

    private void btn194ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn194ActionPerformed
        value = 194;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn194ActionPerformed

    private void btn193ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn193ActionPerformed
        value = 193;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn193ActionPerformed

    private void btn192ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn192ActionPerformed
        value = 192;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn192ActionPerformed

    private void btn191ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn191ActionPerformed
        value = 191;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn191ActionPerformed

    private void btn190ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn190ActionPerformed
        value = 190;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn190ActionPerformed

    private void btn189ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn189ActionPerformed
        value = 189;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn189ActionPerformed

    private void btn188ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn188ActionPerformed
        value = 188;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn188ActionPerformed

    private void btn187ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn187ActionPerformed
        value = 187;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn187ActionPerformed

    private void btn186ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn186ActionPerformed
        value = 186;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn186ActionPerformed

    private void btn185ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn185ActionPerformed
        value = 185;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn185ActionPerformed

    private void btn184ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn184ActionPerformed
        value = 184;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn184ActionPerformed

    private void btn183ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn183ActionPerformed
        value = 183;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn183ActionPerformed

    private void btn182ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn182ActionPerformed
        value = 182;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn182ActionPerformed

    private void btn181ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn181ActionPerformed
        value = 181;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn181ActionPerformed

    private void btn201ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn201ActionPerformed
        value = 201;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn201ActionPerformed

    private void btn202ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn202ActionPerformed
        value = 202;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn202ActionPerformed

    private void btn203ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn203ActionPerformed
        value = 203;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn203ActionPerformed

    private void btn204ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn204ActionPerformed
        value = 204;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn204ActionPerformed

    private void btn205ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn205ActionPerformed
        value = 205;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn205ActionPerformed

    private void btn206ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn206ActionPerformed
        value = 206;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn206ActionPerformed

    private void btn207ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn207ActionPerformed
        value = 207;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn207ActionPerformed

    private void btn208ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn208ActionPerformed
        value = 208;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn208ActionPerformed

    private void btn209ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn209ActionPerformed
        value = 209;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn209ActionPerformed

    private void btn210ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn210ActionPerformed
        value = 210;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn210ActionPerformed

    private void btn211ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn211ActionPerformed
        value = 211;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn211ActionPerformed

    private void btn212ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn212ActionPerformed
        value = 212;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn212ActionPerformed

    private void btn213ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn213ActionPerformed
        value = 213;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn213ActionPerformed

    private void btn214ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn214ActionPerformed
        value = 214;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn214ActionPerformed

    private void btn215ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn215ActionPerformed
        value = 215;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn215ActionPerformed

    private void btn216ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn216ActionPerformed
        value = 216;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn216ActionPerformed

    private void btn217ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn217ActionPerformed
        value = 217;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn217ActionPerformed

    private void btn218ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn218ActionPerformed
        value = 218;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn218ActionPerformed

    private void btn219ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn219ActionPerformed
        value = 219;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn219ActionPerformed

    private void btn220ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn220ActionPerformed
        value = 220;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn220ActionPerformed

    private void btn221ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn221ActionPerformed
        value = 221;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn221ActionPerformed

    private void btn222ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn222ActionPerformed
        value = 222;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn222ActionPerformed

    private void btn223ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn223ActionPerformed
        value = 223;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn223ActionPerformed

    private void btn224ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn224ActionPerformed
        value = 224;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn224ActionPerformed

    private void btn225ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn225ActionPerformed
        value = 225;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn225ActionPerformed

    private void btn226ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn226ActionPerformed
        value = 226;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn226ActionPerformed

    private void btn227ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn227ActionPerformed
        value = 227;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn227ActionPerformed

    private void btn228ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn228ActionPerformed
        value = 228;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn228ActionPerformed

    private void btn229ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn229ActionPerformed
        value = 229;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn229ActionPerformed

    private void btn230ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn230ActionPerformed
        value = 230;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn230ActionPerformed

    private void btn231ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn231ActionPerformed
        value = 231;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn231ActionPerformed

    private void btn232ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn232ActionPerformed
        value = 232;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn232ActionPerformed

    private void btn233ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn233ActionPerformed
        value = 233;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn233ActionPerformed

    private void btn234ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn234ActionPerformed
        value = 234;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn234ActionPerformed

    private void btn235ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn235ActionPerformed
        value = 235;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn235ActionPerformed

    private void btn236ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn236ActionPerformed
        value = 236;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn236ActionPerformed

    private void btn237ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn237ActionPerformed
        value = 237;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn237ActionPerformed

    private void btn238ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn238ActionPerformed
        value = 238;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn238ActionPerformed

    private void btn239ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn239ActionPerformed
        value = 239;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn239ActionPerformed

    private void btn240ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn240ActionPerformed
        value = 240;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn240ActionPerformed

    private void btn241ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn241ActionPerformed
        value = 241;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn241ActionPerformed

    private void btn242ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn242ActionPerformed
        value = 242;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn242ActionPerformed

    private void btn243ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn243ActionPerformed
        value = 243;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn243ActionPerformed

    private void btn244ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn244ActionPerformed
        value = 244;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn244ActionPerformed

    private void btn245ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn245ActionPerformed
        value = 245;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn245ActionPerformed

    private void btn246ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn246ActionPerformed
        value = 246;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn246ActionPerformed

    private void btn247ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn247ActionPerformed
        value = 247;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn247ActionPerformed

    private void btn248ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn248ActionPerformed
        value = 248;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn248ActionPerformed

    private void btn249ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn249ActionPerformed
        value = 249;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn249ActionPerformed

    private void btn250ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn250ActionPerformed
        value = 250;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn250ActionPerformed

    private void btn251ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn251ActionPerformed
        value = 251;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn251ActionPerformed

    private void btn252ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn252ActionPerformed
        value = 252;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn252ActionPerformed

    private void btn253ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn253ActionPerformed
        value = 253;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn253ActionPerformed

    private void btn254ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn254ActionPerformed
        value = 254;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn254ActionPerformed

    private void btn255ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn255ActionPerformed
        value = 255;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn255ActionPerformed

    private void btn256ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn256ActionPerformed
        value = 256;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn256ActionPerformed

    private void btn257ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn257ActionPerformed
        value = 257;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn257ActionPerformed

    private void btn258ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn258ActionPerformed
        value = 258;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn258ActionPerformed

    private void btn259ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn259ActionPerformed
        value = 259;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn259ActionPerformed

    private void btn260ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn260ActionPerformed
        value = 260;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn260ActionPerformed

    private void btn261ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn261ActionPerformed
        value = 261;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn261ActionPerformed

    private void btn262ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn262ActionPerformed
        value = 262;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn262ActionPerformed

    private void btn263ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn263ActionPerformed
        value = 263;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn263ActionPerformed

    private void btn264ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn264ActionPerformed
        value = 264;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn264ActionPerformed

    private void btn265ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn265ActionPerformed
        value = 265;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn265ActionPerformed

    private void btn266ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn266ActionPerformed
        value = 266;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn266ActionPerformed

    private void btn267ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn267ActionPerformed
        value = 267;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn267ActionPerformed

    private void btn268ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn268ActionPerformed
        value = 268;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn268ActionPerformed

    private void btn269ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn269ActionPerformed
        value = 269;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn269ActionPerformed

    private void btn270ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn270ActionPerformed
        value = 270;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn270ActionPerformed

    private void btn271ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn271ActionPerformed
        value = 271;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn271ActionPerformed

    private void btn272ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn272ActionPerformed
        value = 272;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn272ActionPerformed

    private void btn273ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn273ActionPerformed
        value = 273;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn273ActionPerformed

    private void btn274ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn274ActionPerformed
        value = 274;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn274ActionPerformed

    private void btn275ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn275ActionPerformed
        value = 275;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn275ActionPerformed

    private void btn276ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn276ActionPerformed
        value = 276;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn276ActionPerformed

    private void btn277ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn277ActionPerformed
        value = 277;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn277ActionPerformed

    private void btn278ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn278ActionPerformed
        value = 278;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn278ActionPerformed

    private void btn279ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn279ActionPerformed
        value = 279;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn279ActionPerformed

    private void btn280ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn280ActionPerformed
        value = 280;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn280ActionPerformed

    private void btn281ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn281ActionPerformed
        value = 281;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn281ActionPerformed

    private void btn282ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn282ActionPerformed
        value = 282;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn282ActionPerformed

    private void btn283ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn283ActionPerformed
        value = 283;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn283ActionPerformed

    private void btn284ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn284ActionPerformed
        value = 284;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn284ActionPerformed

    private void btn285ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn285ActionPerformed
        value = 285;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn285ActionPerformed

    private void btn286ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn286ActionPerformed
        value = 286;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn286ActionPerformed

    private void btn287ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn287ActionPerformed
        value = 287;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn287ActionPerformed

    private void btn288ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn288ActionPerformed
        value = 288;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn288ActionPerformed

    private void btn289ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn289ActionPerformed
        value = 289;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn289ActionPerformed

    private void btn290ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn290ActionPerformed
        value = 290;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn290ActionPerformed

    private void btn291ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn291ActionPerformed
        value = 291;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn291ActionPerformed

    private void btn292ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn292ActionPerformed
        value = 292;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn292ActionPerformed

    private void btn293ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn293ActionPerformed
        value = 293;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn293ActionPerformed

    private void btn294ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn294ActionPerformed
        value = 294;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn294ActionPerformed

    private void btn295ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn295ActionPerformed
        value = 295;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn295ActionPerformed

    private void btn296ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn296ActionPerformed
        value = 296;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn296ActionPerformed

    private void btn297ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn297ActionPerformed
        value = 297;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn297ActionPerformed

    private void btn298ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn298ActionPerformed
        value = 298;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn298ActionPerformed

    private void btn299ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn299ActionPerformed
        value = 299;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn299ActionPerformed

    private void btn300ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn300ActionPerformed
        value = 300;
        if (!buttonStatusList.get(value)) {
            if (!mineGenerated) {
                generateMineSlots(value);
                manageSlot(value);

            } else if (checkDuplicateSlot(value, mineList)) {
                mineHit();
            } else {
                manageSlot(value);
            }

        }
    }//GEN-LAST:event_btn300ActionPerformed

    private void btn1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn1MouseClicked
        // TODO add your handling code here:
        if (evt.getButton() == 3) {
            String text = btn1.getText(); 
            if (text.equals("")) {
                buttonStatusList.replace(1, true);
                btn1.setForeground(Color.white);
                btn1.setText("?");
            } else if (text.equals("?")) {
                buttonStatusList.replace(1, true);
                btn1.setForeground(Color.red);
                btn1.setText("B");
            } else {
                buttonStatusList.replace(1, false);
                btn1.setText("");
            }
        }
    }//GEN-LAST:event_btn1MouseClicked

    private void btn2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn2MouseClicked

    private void btn3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn3MouseClicked

    private void btn4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn4MouseClicked

    private void btn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn5MouseClicked

    private void btn6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn6MouseClicked

    private void btn7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn7MouseClicked

    private void btn8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn8MouseClicked

    private void btn9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn9MouseClicked

    private void btn10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn10MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn10MouseClicked

    private void btn11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn11MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn11MouseClicked

    private void btn12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn12MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn12MouseClicked

    private void btn13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn13MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn13MouseClicked

    private void btn14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn14MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn14MouseClicked

    private void btn15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn15MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn15MouseClicked

    private void btn16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn16MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn16MouseClicked

    private void btn17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn17MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn17MouseClicked

    private void btn18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn18MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn18MouseClicked

    private void btn19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn19MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn19MouseClicked

    private void btn20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn20MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn20MouseClicked

    private void btn21MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn21MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn21MouseClicked

    private void btn22MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn22MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn22MouseClicked

    private void btn23MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn23MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn23MouseClicked

    private void btn24MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn24MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn24MouseClicked

    private void btn25MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn25MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn25MouseClicked

    private void btn26MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn26MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn26MouseClicked

    private void btn27MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn27MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn27MouseClicked

    private void btn28MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn28MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn28MouseClicked

    private void btn29MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn29MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn29MouseClicked

    private void btn30MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn30MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn30MouseClicked

    private void btn31MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn31MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn31MouseClicked

    private void btn32MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn32MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn32MouseClicked

    private void btn33MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn33MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn33MouseClicked

    private void btn34MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn34MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn34MouseClicked

    private void btn35MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn35MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn35MouseClicked

    private void btn36MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn36MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn36MouseClicked

    private void btn37MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn37MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn37MouseClicked

    private void btn38MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn38MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn38MouseClicked

    private void btn39MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn39MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn39MouseClicked

    private void btn40MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn40MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn40MouseClicked

    private void btn41MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn41MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn41MouseClicked

    private void btn42MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn42MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn42MouseClicked

    private void btn43MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn43MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn43MouseClicked

    private void btn44MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn44MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn44MouseClicked

    private void btn45MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn45MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn45MouseClicked

    private void btn46MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn46MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn46MouseClicked

    private void btn47MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn47MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn47MouseClicked

    private void btn48MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn48MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn48MouseClicked

    private void btn49MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn49MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn49MouseClicked

    private void btn50MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn50MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn50MouseClicked

    private void btn51MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn51MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn51MouseClicked

    private void btn52MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn52MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn52MouseClicked

    private void btn53MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn53MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn53MouseClicked

    private void btn54MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn54MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn54MouseClicked

    private void btn55MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn55MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn55MouseClicked

    private void btn56MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn56MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn56MouseClicked

    private void btn57MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn57MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn57MouseClicked

    private void btn58MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn58MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn58MouseClicked

    private void btn59MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn59MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn59MouseClicked

    private void btn60MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn60MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn60MouseClicked

    private void btn61MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn61MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn61MouseClicked

    private void btn62MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn62MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn62MouseClicked

    private void btn63MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn63MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn63MouseClicked

    private void btn64MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn64MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btn64MouseClicked

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
    // End of variables declaration//GEN-END:variables
}
