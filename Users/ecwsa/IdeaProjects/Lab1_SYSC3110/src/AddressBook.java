import java.io.*;
import java.util.*;
import javax.swing.*;



public class AddressBook implements Serializable
{
    private List<BuddyInfo> buddies = new ArrayList<>();
    public  AddressBook(){
        super();
    }

    public void addBuddy(BuddyInfo buddy){
        if (this.buddies.contains(buddy)){
            System.out.println("Buddy " + buddy.getName() + " already exists");
        }
        this.buddies.add(buddy);
    }

    public void removeBuddy(BuddyInfo buddy){
        this.buddies.remove(buddy);
    }

    public void removeBuddy(int index){
        this.buddies.remove(index);
    }
    public BuddyInfo getBuddy(String buddyName){
        for (BuddyInfo buddy : this.buddies){
            if (buddy.getName().equals(buddyName)){
                return buddy;
            }
        }
        return null;
    }


    public boolean findBuddy(BuddyInfo buddy){
        if (this.buddies.contains(buddy)){
            System.out.println("Buddy " + buddy.getName() + buddy.getAddress() + buddy.getPhoneNumber());
            return true;
        }
        System.out.println("Buddy not found");
        return false;
    }

    public BuddyInfo getBuddy(int index){
        if (this.buddies.contains(index)){
            return this.buddies.get(index);
        }
        return null;

    }

    public List<BuddyInfo> getBuddies() {
        return new ArrayList<>(this.buddies);
    }

    public void save(String file) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for (BuddyInfo buddy : this.buddies){
                bw.write(buddy.toString());
                bw.newLine();
            }
            bw.flush();
            System.out.println("Saved AddressBook to: " + file);
        } catch(IOException e){
            System.out.println("ERROR SAVING: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static AddressBook importAddressBook(String file) throws IOException{
        AddressBook addressBook = new AddressBook();

        try (BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            int i = 0;
            while ((line = br.readLine()) != null){
                i++;
                line=line.trim();
                if (line.isEmpty()){
                    continue;
                }
                try {
                    BuddyInfo buddy = BuddyInfo.importBuddyInfo(line);
                    addressBook.addBuddy(buddy);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error on line " + i + ": " + e.getMessage());
                    System.err.println("problem line: " + line);
                }
            }
        }
        return addressBook;
    }

    public void serializeAddressBook(String file) throws IOException{
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(this);
            System.out.println("Serialized AddressBook to: " + file);
        }
    }
    public static AddressBook deserializeAddressBook(String file) throws IOException{
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            AddressBook addressBook = (AddressBook) ois.readObject();
            System.out.println("Deserialized AddressBook from: " + file);
            return addressBook;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args)
    {
        System.out.println("Address Book");
        BuddyInfo buddy1 = new BuddyInfo("Grinch", "Who-Ville", 999);
        AddressBook addressBook = new AddressBook();
        addressBook.addBuddy(buddy1);
        addressBook.removeBuddy(buddy1);
        BuddyInfo buddy2 = new BuddyInfo("Santa", "North-Pole", 888);
        addressBook.addBuddy(buddy2);
        addressBook.removeBuddy(buddy2);
    }
}

