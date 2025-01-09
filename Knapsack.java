import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Knapsack {
    private int numObjects;
    private float maxWeight;
    private float totProf;
    private int[] profits;
    private int[] weights;
    private float[] unit;
    private float[] unsort;

    /**
     * Constructor for a knapsack
     * read in a file, store the first line as a sting
     * store that line as the number of objects
     * next line
     * store as max weight
     * next line, instantiate the arrays, start for loop:
     * use the first number of the line in the profit array
     * use the second number in the weight array
     * store the result of dividing profit by weight for each item in unit array
     * copy unsorted array
     * sort unit
     * @param fileName the file to be read in to set up the knapsack
     */
    public Knapsack(String fileName) {
        try {
            String curLine = "";
            File file = new File(fileName);
            Scanner scan = new Scanner(file);
            curLine = scan.nextLine();
            numObjects = Integer.parseInt(curLine);
            curLine = scan.nextLine();
            maxWeight = Integer.parseInt(curLine);
            profits = new int[numObjects];
            weights = new int[numObjects];
            for(int i = 0; i < numObjects; i++) {
                curLine = scan.nextLine();
                if (!curLine.equals("")) {
                    profits[i] = Integer.parseInt(curLine.substring(0, curLine.indexOf(",")));
                    curLine = curLine.substring(curLine.indexOf(" ") + 1);
                    weights[i] = Integer.parseInt(curLine);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("There was an error.");
            e.printStackTrace();
        }
        unit = new float[numObjects];
        for(int i = 0; i < numObjects; i++) {
            unit[i] = (float) profits[i] / weights[i];
        }
        unsort = unit;
        unit = mergeSort(unit);
    }

    /**
     * this method calls split
     * @param arr the unsorted array
     * @return the sorted array
     */
    public static float[] mergeSort(float[] arr){
        arr = split(arr, 0, arr.length - 1);
        return arr;
    }

    /**
     * base case if the bounds are equal
     * create a new array only containing that number and return it
     * recursive case otherwise
     * calculate the middle of the given split bounds
     * create new array for the left and right sides of the middle
     * call split again for each side to instantiate the arrays
     * this method calls merge
     * @param arr the array to split
     * @param left the left bound of the split
     * @param right the right bound of the split
     * @return an array after being merged back
     */
    public static float[] split(float[] arr, int left, int right) {
        if (left == right) {
            float[] retArr = new float[1];
            retArr[0] = arr[left];
            return retArr;
        } else {
            int mid = ((right - left) / 2) + left;
            float[] leftarr = split (arr, left, mid);
            float[] rightarr = split (arr, mid+1, right);
            return merge(leftarr, rightarr);
        }
    }

    /**
     * create a new array to hold both given
     * create and set equal to 0 a count one, two and main variable
     * iterate through both arrays
     * count one keeps track of the location in the first array
     * count two keeps track of the location in the second array
     * maincount keeps track of the location in the merged array
     * if the end of either array is reached, fill the other in completely
     * if not, put the smallest one in the main array next and increment the location in that array
     * @param arrOne one of the arrays to combine
     * @param arrTwo the other array to combine
     * @return the full array
     */
    public static float[] merge(float[] arrOne, float[] arrTwo) {
        float[] array = new float[arrOne.length + arrTwo.length];
        int countOne = 0;
        int countTwo = 0;
        int mainCount = 0;
        while (countOne < arrOne.length || countTwo < arrTwo.length) {
            if (countOne == arrOne.length) {
                array[mainCount] = arrTwo[countTwo];
                mainCount++;
                countTwo++;
            } else if (countTwo == arrTwo.length) {
                array[mainCount] = arrOne[countOne];
                mainCount++;
                countOne++;
            } else {
                if (arrOne[countOne] > arrTwo[countTwo]) {
                    array[mainCount] = arrTwo[countTwo];
                    mainCount++;
                    countTwo++;
                } else {
                    array[mainCount] = arrOne[countOne];
                    mainCount++;
                    countOne++;
                }
            }
        }
        return array;
    }

    /**
     * create variables to store the location of the largest unit value and the location of that in the original list
     * iterate until there is no space in the knapsack
     * find the location of the object with the next biggest unit value and store in orgLoc
     * if the sack can fit the whole object, add the profit to the profit total
     * subtract the weight from the weight total
     * decrement curOb, set the used value to -1
     * if the whole object cannot fit, multiply the profit of the object by:
     * the remaining weight divided by the weight of the object
     * @return the final value of total profit
     */
    public float findTotProf() {
        int curOb = unit.length - 1;
        int orgLoc = 0;
        while (maxWeight > 0) {
            for(int i = 0; i < unsort.length; i++) {
                if (unit[curOb] == unsort[i]) {
                    orgLoc = i;
                }
            }
            if(weights[orgLoc] <= maxWeight) {
                totProf += profits[orgLoc];
                maxWeight = maxWeight - weights[orgLoc];
                curOb--;
                unsort[orgLoc] = -1;
            } else {
                totProf += profits[orgLoc] * (maxWeight / weights[orgLoc]);
                maxWeight = 0;
            }
        }
        return totProf;
    }
}