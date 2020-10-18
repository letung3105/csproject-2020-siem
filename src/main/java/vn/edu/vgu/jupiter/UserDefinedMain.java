package vn.edu.vgu.jupiter;

import vn.edu.vgu.jupiter.scan_alerts.UserDefinedPortScanMain;

import java.util.Scanner;

/**
 * Class for modifying the variables by user inputs and run the inner main programs
 */
public class UserDefinedMain {
    public static UserDefinedPortScanMain portScanMain;

    public static void main(String[] args) {
        String netDevName = args.length > 0 ? args[0] : "";

        portScanMain = new UserDefinedPortScanMain();

        //Prompt user for inputs
        // TODO: This can be changed by prompting in the GUI, then rerun the portScanMain when a change is applied
        changeVerticalScanAlertParam();
        changeHorizontalScanAlertParam();
        changeBlockScanAlertParam();

        //Run
        portScanMain.run(netDevName);
    }

    public static void changeVerticalScanAlertParam(){
        System.out.println("Vertical port scan alert: " + portScanMain.getMinConnectionCountVertical()
                                + "/" + portScanMain.getTimeWindowVertical() + " (connections/seconds)");

        System.out.println("Input new minimum connections: ");
        portScanMain.setMinConnectionCountVertical(getInput());
        System.out.println("Input new time threshold: ");
        portScanMain.setTimeWindowVertical(getInput());

        System.out.println("New Vertical port scan alert: " + portScanMain.getMinConnectionCountVertical()
                + "/" + portScanMain.getTimeWindowVertical() + " (connections/seconds)");
    }

    public static void changeHorizontalScanAlertParam(){
        System.out.println("Horizontal port scan alert: " + portScanMain.getMinConnectionCountHorizontal()
                + "/" + portScanMain.getTimeWindowHorizontal() + " (connections/seconds)");

        System.out.println("Input new minimum connections: ");
        portScanMain.setMinConnectionCountHorizontal(getInput());
        System.out.println("Input new time threshold: ");
        portScanMain.setTimeWindowHorizontal(getInput());

        System.out.println("New Horizontal port scan alert: " + portScanMain.getMinConnectionCountHorizontal()
                + "/" + portScanMain.getTimeWindowHorizontal() + " (connections/seconds)");
    }

    public static void changeBlockScanAlertParam(){
        System.out.println("Block port scan alert: " + portScanMain.getMinAddressCount() + " addresses on " +
                portScanMain.getMinPortsCount() + " ports/ " + portScanMain.getTimeWindowBlock() + " seconds");

        System.out.println("Input new minimum addresses: ");
        portScanMain.setMinAddressCount(getInput());
        System.out.println("Input new minimum ports: ");
        portScanMain.setMinPortsCount(getInput());
        System.out.println("Input new time threshold: ");
        portScanMain.setTimeWindowBlock(getInput());

        System.out.println("New Block port scan alert: " + portScanMain.getMinAddressCount() + "addresses on " +
                portScanMain.getMinPortsCount() + "ports/" + portScanMain.getTimeWindowBlock() + " seconds");
    }

    private static int getInput(){
        //Scanner is used here for simplicity, when the GUI is applied, this is not necessary
        Scanner scanner = new Scanner(System.in);
        boolean isCorrectInput = false;
        String input = null;
        while(!isCorrectInput){
            input = scanner.nextLine();
            isCorrectInput = isInteger(input);
        }
        int result = Integer.parseInt(input);
        return result;
    }

    private static boolean isInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}
