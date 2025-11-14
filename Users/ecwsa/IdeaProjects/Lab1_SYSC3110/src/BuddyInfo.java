import java.io.Serializable;
import java.util.Scanner;

public class BuddyInfo implements Serializable {
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
        return  name + "~" + address + "~" + phoneNumber;
    }
    public String fullBuddyInfo(){
        return "Name: " + name + " Address: " + address + " PhoneNumber: " + phoneNumber;
    }

    public static BuddyInfo importBuddyInfo(String buddyString){
        try(Scanner sc = new Scanner(buddyString)) {
            sc.useDelimiter("~");
            String name = sc.hasNext() ? sc.next().trim() : "";
            String address = sc.hasNext() ? sc.next().trim() : "";
            int phoneNumber = sc.hasNext() ? Integer.parseInt(sc.next().trim()) : 0;
            if (name.isEmpty()){
                throw new IllegalArgumentException("Name cannot be empty");
            }
            return new BuddyInfo(name, address, phoneNumber);
        }
    }

    public static void main(String[] args) {
        BuddyInfo Elmo = new BuddyInfo("Elmo", "Sesame Street", 123456789);
        System.out.println("Hello " + Elmo.getName());
        System.out.println(Elmo.toString());
    }
}
