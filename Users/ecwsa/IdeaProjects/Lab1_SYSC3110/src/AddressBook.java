import java.util.*;

public class AddressBook
{
    private List<BuddyInfo> buddies;
    public  AddressBook(){
        this.buddies = new ArrayList<>();
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

    public static void main(String[] args)
    {
        System.out.println("Address Book");
        BuddyInfo buddy1 = new BuddyInfo("Grinch", "Who-Ville", 999);
        AddressBook addressBook = new AddressBook();
        addressBook.addBuddy(buddy1);
        addressBook.removeBuddy(buddy1);
    }
}
