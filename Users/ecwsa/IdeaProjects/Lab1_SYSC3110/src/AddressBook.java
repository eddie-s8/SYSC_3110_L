import java.util.*;
import javax.swing.*;



public class AddressBook extends DefaultListModel<BuddyInfo>
{
    private List<BuddyInfo> buddies;
    public  AddressBook(){
        super();
    }

    public void addBuddy(BuddyInfo buddy){
        if (this.contains(buddy)){
            System.out.println("Buddy " + buddy.getName() + " already exists");
        }
        this.addElement(buddy);
    }

    public void removeBuddy(BuddyInfo buddy){
        this.removeElement(buddy);
    }

    public void removeBuddy(int index){
        this.removeElementAt(index);
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
        if (this.contains(buddy)){
            System.out.println("Buddy " + buddy.getName() + buddy.getAddress() + buddy.getPhoneNumber());
            return true;
        }
        System.out.println("Buddy not found");
        return false;
    }

    public BuddyInfo getBuddy(int index){
        if (this.contains(index)){
            return this.getElementAt(index);
        }
        return null;

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

