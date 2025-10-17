public class BuddyInfo {
    private String name;
    private String address;
    private int phoneNumber;

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public BuddyInfo() {
        this("Bob", "Carleton University", 888634);
    }

    public BuddyInfo(String name, String address, int phoneNumber) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    @Override
    public String toString() {
        return "Name: " + name;
    }
    public String fullBuddyInfo(){
        return "Name: " + name + " Address: " + address + " PhoneNumber: " + phoneNumber;
    }

    public static void main(String[] args) {
        BuddyInfo Elmo = new BuddyInfo("Elmo", "Sesame Street", 123456789);
        System.out.println("Hello " + Elmo.getName());
    }
}
