import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SmartHomeSystem smartHome = new SmartHomeSystem();

        int q = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < q; i++) {
            String input = scanner.nextLine().trim();
            String[] tokens = input.split(" ");
            String command = tokens[0];

            try {
                switch (command) {
                    case "add_device":
                        if (tokens.length != 4) {
                            System.out.println("invalid input");
                            break;
                        }
                        System.out.println(smartHome.addDevice(tokens[1], tokens[2], tokens[3]));
                        break;

                    case "set_device":
                        if (tokens.length != 4) {
                            System.out.println("invalid input");
                            break;
                        }
                        System.out.println(smartHome.setDevice(tokens[1], tokens[2], tokens[3]));
                        break;

                    case "remove_device":
                        if (tokens.length != 2) {
                            System.out.println("invalid input");
                            break;
                        }
                        System.out.println(smartHome.removeDevice(tokens[1]));
                        break;

                    case "list_devices":
                        if (tokens.length != 1) {
                            System.out.println("invalid input");
                            break;
                        }
                        List<String> devices = smartHome.listDevices();
                        for (String deviceInfo : devices) {
                            System.out.println(deviceInfo);
                        }
                        break;

                    case "add_rule":
                        if (tokens.length != 4) {
                            System.out.println("invalid input");
                            break;
                        }
                        System.out.println(smartHome.addRule(tokens[1], tokens[2], tokens[3]));
                        break;

                    case "check_rules":
                        if (tokens.length != 2) {
                            System.out.println("invalid input");
                            break;
                        }
                        System.out.println(smartHome.checkRules(tokens[1]));
                        break;

                    case "list_rules":
                        if (tokens.length != 1) {
                            System.out.println("invalid input");
                            break;
                        }
                        List<String> rules = smartHome.listRules();
                        for (String ruleInfo : rules) {
                            System.out.println(ruleInfo);
                        }
                        break;

                    default:
                        System.out.println("invalid command");
                }
            } catch (Exception e) {
                System.out.println("invalid input");
            }
        }

        scanner.close();
    }
}