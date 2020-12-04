/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mine;

import java.util.ArrayList;

/**
 *
 * @author Nat
 */
public class Mine {

    
    static ArrayList<Integer> adjacentList;
    static ArrayList<Integer> MineList;
    private static void generateMineSlots(int slot) {
        adjacentList = new ArrayList<>();
        adjacentList.add(84);
        adjacentList.add(85);
        adjacentList.add(86);
        adjacentList.add(104);
        adjacentList.add(106);
        adjacentList.add(124);
        adjacentList.add(125);
        adjacentList.add(126);
        MineList = new ArrayList<>();
        for (int i = 0; i < 70; i++) {
            int mineSlot = (int)(Math.random()*300+1);
            if(!checkDuplicateSlot(mineSlot, MineList) && 
               !checkDuplicateSlot(mineSlot, adjacentList) && mineSlot != slot)
                MineList.add(mineSlot);
        }
    }
    private static boolean checkDuplicateSlot(int slot, ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if(slot == list.get(i))
                return true;
        }
        return false;
    }
//    public static void main(String[] args) {
//        int[] a = {1,2,4,5,7,8,9,10};
//        int[] b = {3,6,8,9,11,10};
//        for (int j = 0; j <b.length; j++) {
//            for (int i = 0; i < a.length; i++) {
//                System.out.println(j+" - "+i);
//                if(b[j]==a[i])
//                    System.out.println(b[j]);
//            }
//            
//        }
//        }
    
}
