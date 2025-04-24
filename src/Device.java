import java.util.*;

abstract class Device {
    protected String name;
    protected String protocol;
    protected String status;

    public Device(String name, String protocol) {
        this.name = name;
        this.protocol = protocol;
        this.status = "off";
    }

    public String getName() { return name; }
    public String getProtocol() { return protocol; }
    public String getStatus() { return status; }

    public abstract boolean setProperty(String property, String value);
    public abstract String getInfo();
}

class Light extends Device {
    private int brightness;

    public Light(String name, String protocol) {
        super(name, protocol);
        this.brightness = 50;
    }

    @Override
    public boolean setProperty(String property, String value) {
        switch (property) {
            case "status":
                if (value.equals("on") || value.equals("off")) {
                    status = value;
                    return true;
                }
                return false;
            case "brightness":
                try {
                    int brightnessValue = Integer.parseInt(value);
                    if (brightnessValue >= 0 && brightnessValue <= 100) {
                        brightness = brightnessValue;
                        return true;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getInfo() {
        return String.format("light: %s %s %d%% %s", name, status, brightness, protocol);
    }
}

class Thermostat extends Device {
    private int temperature;

    public Thermostat(String name, String protocol) {
        super(name, protocol);
        this.temperature = 20;
    }

    @Override
    public boolean setProperty(String property, String value) {
        switch (property) {
            case "status":
                if (value.equals("on") || value.equals("off")) {
                    status = value;
                    return true;
                }
                return false;
            case "temperature":
                try {
                    int tempValue = Integer.parseInt(value);
                    if (tempValue >= 10 && tempValue <= 30) {
                        temperature = tempValue;
                        return true;
                    }
                } catch (NumberFormatException e) {
                    return false;
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    public String getInfo() {
        return String.format("thermostat: %s %s %dC %s", name, status, temperature, protocol);
    }
}

class Rule {
    private String deviceName;
    private String time;
    private String action;

    public Rule(String deviceName, String time, String action) {
        this.deviceName = deviceName;
        this.time = time;
        this.action = action;
    }

    public String getDeviceName() { return deviceName; }
    public String getTime() { return time; }
    public String getAction() { return action; }

    public String getInfo() {
        return String.format("%s %s %s", deviceName, time, action);
    }
}

class SmartHomeSystem {
    private List<Device> devices;
    private Map<String, Device> deviceMap;
    private List<Rule> rules;

    public SmartHomeSystem() {
        devices = new ArrayList<>();
        deviceMap = new HashMap<>();
        rules = new ArrayList<>();
    }

    public String addDevice(String type, String name, String protocol) {
        if (deviceMap.containsKey(name)) {
            return "duplicate device name";
        }

        if (!protocol.equals("WiFi") && !protocol.equals("Bluetooth")) {
            return "invalid input";
        }

        Device device;
        if (type.equals("light")) {
            device = new Light(name, protocol);
        } else if (type.equals("thermostat")) {
            device = new Thermostat(name, protocol);
        } else {
            return "invalid input";
        }

        devices.add(device);
        deviceMap.put(name, device);
        return "device added successfully";
    }

    public String setDevice(String name, String property, String value) {
        if (!deviceMap.containsKey(name)) {
            return "device not found";
        }

        Device device = deviceMap.get(name);
        if (!device.setProperty(property, value)) {
            if (property.equals("status") || property.equals("brightness") || property.equals("temperature")) {
                return "invalid value";
            }
            return "invalid property";
        }

        return "device updated successfully";
    }

    public String removeDevice(String name) {
        if (!deviceMap.containsKey(name)) {
            return "device not found";
        }

        Device device = deviceMap.get(name);
        devices.remove(device);
        deviceMap.remove(name);

        // حذف قوانین مرتبط با دستگاه
        rules.removeIf(rule -> rule.getDeviceName().equals(name));
        return "device removed successfully";
    }

    public List<String> listDevices() {
        List<String> result = new ArrayList<>();
        if (devices.isEmpty()) {
            result.add("");
            return result;
        }

        for (Device device : devices) {
            result.add(device.getInfo());
        }
        return result;
    }

    public String addRule(String name, String time, String action) {
        if (!deviceMap.containsKey(name)) {
            return "device not found";
        }

        if (!isValidTime(time)) {
            return "invalid time";
        }

        if (!action.equals("on") && !action.equals("off")) {
            return "invalid action";
        }

        for (Rule rule : rules) {
            if (rule.getDeviceName().equals(name) && rule.getTime().equals(time)) {
                return "duplicate rule";
            }
        }

        rules.add(new Rule(name, time, action));
        return "rule added successfully";
    }

    public String checkRules(String time) {
        if (!isValidTime(time)) {
            return "invalid time";
        }

        for (Rule rule : rules) {
            if (rule.getTime().equals(time) && deviceMap.containsKey(rule.getDeviceName())) {
                Device device = deviceMap.get(rule.getDeviceName());
                device.setProperty("status", rule.getAction());
            }
        }

        return "rules checked";
    }

    public List<String> listRules() {
        List<String> result = new ArrayList<>();
        if (rules.isEmpty()) {
            result.add("");
            return result;
        }

        for (Rule rule : rules) {
            result.add(rule.getInfo());
        }
        return result;
    }

    private boolean isValidTime(String time) {
        if (time.length() != 5 || time.charAt(2) != ':') {
            return false;
        }

        try {
            int hours = Integer.parseInt(time.substring(0, 2));
            int minutes = Integer.parseInt(time.substring(3));

            return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}