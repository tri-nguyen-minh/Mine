/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author TMN
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
            int mineSlot = (int) (Math.random() * 300 + 1);
            if (!checkDuplicateSlot(mineSlot, MineList)
                    && !checkDuplicateSlot(mineSlot, adjacentList) && mineSlot != slot) {
                MineList.add(mineSlot);
            }
        }
    }

    private static boolean checkDuplicateSlot(int slot, ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            if (slot == list.get(i)) {
                return true;
            }
        }
        return false;
    }

    static void test(float n) {
        int a = 0, sum = 0, count = 0;
        if (n < 0) {
            n = 0 - n;
        }
        for (int i = 0; n > 0; i++) {
            a = (int) n % 10;
            System.out.println(a);
            sum += a;
            n = (n - a) / 10;
//            System.out.println("n = " + n);
            count = i;
        }
        if ((sum - count) % 9 > 0) {
            System.out.println("false");
        } else {
            System.out.println("true");
        }
    }

    static int count(int[] arr, long l, long r) {
        int count = 0;
        for (long i = l; i <= r; i++) {
            int c = 0;
            System.out.println(i);
            for (int j = 0; j < arr.length; j++) {
                if (i % arr[j] == 0) {
                    c++;
                }
            }
            if (c == arr.length) {
                count++;
            }
        }
        return count;
    }

    static int deposit(int t, int s, int p) {
        double sum = (double) s;
        int year = 0;
        if (t <= s) {
            return 0;
        } else {
            while (sum < t) {
                double interest = ((double) p / 100) * sum;
                sum += interest;
                year++;
            }
        }
        return year;
    }

    static String out(String name) {
        String word, output = "";
        String[] list = name.trim().split(" ");
        for (int i = 0; i < list.length; i++) {
            word = "";
            list[i] = list[i].toLowerCase();
            char[] c = list[i].toCharArray();

            int t = (int) c[0];
            c[0] = (char) (t - 32);
            for (char d : c) {
                word += d;
            }
            list[i] = word;
        }
        for (String s : list) {
            output += " " + s;
        }
        return output.substring(1);
    }

    static int card(int n) {
        if (n == 1) {
            return 2;
        } else {
            return (n * 2) + (n - 1) + card(n - 1);
        }
    }

    static long maxReward(int n, long[] k) {
        int lowest = getLowest(k);
        long output = k[lowest];
        System.out.println("first output: " + output);
        long firstGCD;
        if (lowest == 0) {
            firstGCD = getGCD(output, k[1]);
        } else {
            firstGCD = getGCD(output, k[0]);
        }
        System.out.println("first gcd: " + firstGCD);
        for (int i = 0; i < n; i++) {
            System.out.println("i: " + i);
            if (i != lowest) {
                long test = getGCD(output, k[i]);
                System.out.println("test: " + test);
                if (test % firstGCD != 0) {
                    return output;
                } else if (test == 1) {
                    return output;
                }
            }
        }
        return firstGCD;
    }

    static long getGCD(long a, long b) {
        if (b == 0) {
            return a;
        }
        return getGCD(b, a % b);
    }

    static int getLowest(long[] k) {
        int lowest = 0;
        for (int i = 1; i < k.length; i++) {
            if (k[lowest] > k[i]) {
                lowest = i;
            }
        }
        return lowest;
    }

    static int minimumSquares1(int n, int m) {
        if (n == 0 || m == 0) {
            return 0;
        }
        if (n > m) {
            int temp = n;
            n = m;
            m = temp;
        }
        int count = 0, i, square;
        while (n > 0) {
            i = 1;
            square = 0;
            while ((n * i) <= m) {
                count++;
                square++;
                i++;
            }
            int temp = n;
            n = m - (n * square);
            m = temp;
        }
        return count;
    }

    static int migratoryBirds(int[] list_of_birds) {
        if (list_of_birds.length == 1) {
            return list_of_birds[0];
        }
        Set<Integer> set = new TreeSet<>();
        for (int i : list_of_birds) {
            set.add(i);
        }
        ArrayList<Integer> list = new ArrayList<>();
        for (Integer i : set) {
            list.add(i);
        }
        System.out.println(list);
        int count = list.get(0);
        int number = countBird(list_of_birds, count);
        System.out.println(count + " - " + number);
        for (int i = 1; i < list.size(); i++) {
            int test = countBird(list_of_birds, list.get(i));
            if (test > number) {
                number = test;
                count = list.get(i);
            }
        }
        return count;
    }

    static int countBird(int[] list, int bird) {
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            if (list[i] == bird) {
                count++;
            }
        }
        return count;
    }

    static int lego(int a, int b, int n) {
        int count = 0;
        if (a < b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        while (n >= a) {
            n -= a;
            count++;
        }
        System.out.println("N = " + n);
        while ((n % b) != 0 && count >= 0) {
            n += a;
            count--;
            System.out.println("N: " + n);
            System.out.println("C: " + count);
        }
        if (count <= 0) {
            return -1;
        } else {
            count += n / b;
        }
        return count;
    }

    public static void main(String[] args) {
//        int[] a = {1, 4, 5, 4, 4, 3};
//        System.out.println(migratoryBirds(a));
//        String z = "aaba";
//        char[] li = z.toCharArray();
//        System.out.println(li[0] == li[2]);

        int k = 4;
        int[] a = {81,12,42,6,7};
        Set<Integer> set = new TreeSet<>();
        for (int i : a) {
            set.add(i);
        }
        ArrayList<Integer> arr = new ArrayList<>();
        for (Integer i : set) {
            arr.add(i);
        }
        System.out.println(set);
        if (k > arr.get(0)) {
        int s = 0, count = 0;
        while (k < arr.get(arr.size()-1)) {
            System.out.println("K: " + k);
        while (k >= arr.get(s)) {
            s++;
        }
        s--;
        System.out.println(s + " - " + arr.get(s));
        k += arr.get(s);
        count++;
        }
        System.out.println("Result: " + count);
        } else {
            System.out.println("-1");
        }
        

//        int count = 64;
//        String a = "I dont now ";
//        char[] c = a.toCharArray();
//        for (char d : c) {
//            System.out.println("-"+d+"-");
//        }
//        String out = "";
//        while (count >0) {
//            out = (count%2) + out;
//            count /= 2;
//        }
//        System.out.println(out);
//        System.out.println(Math.sqrt(4.5) == (int) Math.sqrt(4.5));
//        long[] k = {42,42,42,42,42};
//        System.out.println("lowest: "+getLowest(k));
//        System.out.println("Result: "+maxReward(5, k));
//        test(0);
//        int[] arr = {3,5,3};
//        System.out.println(count(arr, 10, 60));
//        int n = 3;
//        System.out.println(card(n));
//        for (String arg : args) {
//            
//        }
//        
//        String s = "0b9123dsad384";
//        String[] st = s.split("[a-zA-Z]");
//        for (String string : st) {
//            System.out.println(string);
//        }
//        System.out.println(s.length());
//        char[] c = s.toCharArray();
//        for (char d : c) {
//            System.out.println((int) d);
//        }
//        String f = s.toLowerCase();
//        System.out.println(s.equals(f));
//        System.out.println(deposit(1000000, 50000, 20));
//        String t = "abd";
//        char[] s = t.toCharArray();
//        for (char d : s) {
//                t += d;
//            }
//        System.out.println(t);
//        System.out.println(out("09123a"));
//        int[] arr = {1,5,7,2,8};
//        int x = 7, y = -15;
//        while (y != 0) {
//            int t = x & y;
//            System.out.println("1st ");
//            System.out.println("t: " + t + ", x: " + x + ", y: " + y);
//            x = x ^ y;
//            y = t << 1;
//            System.out.println("2nd ");
//            System.out.println("t: " + t + ", x: " + x + ", y: " + y);
//        }
//        System.out.println(x);
//        System.out.print("a".equals("a") + ": ");
//        ArrayList<Integer> numbers = new ArrayList<>();
//    numbers.add(5);
//    numbers.add(9);
//    numbers.add(8);
//    numbers.add(1);
//    numbers.forEach( (n) -> { System.out.println(n); } );
//        String s = new Scanner(System.in).nextLine();
//        System.out.println(s);
/*
x 00111
y 01111
t 00111
x 01000
y 01110
t 01000
x 00110
y 10000
t 00000
x 10110

         */
    }
}
