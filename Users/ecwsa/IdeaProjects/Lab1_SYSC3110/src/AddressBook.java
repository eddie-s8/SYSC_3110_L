import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;


public class AddressBook extends DefaultHandler implements Serializable
{
    private List<BuddyInfo> buddies = new ArrayList<>();
    private StringBuilder current;
    private BuddyInfo currentBuddy;
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

    public String toXML() {
        try{
            StringWriter sw = new StringWriter();
            XMLStreamWriter xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(sw);

            xmlWriter.writeStartElement("AddressBook");
            xmlWriter.writeCharacters("\n");

            for (BuddyInfo buddy : buddies){
                xmlWriter.writeStartElement("BuddyInfo");
                xmlWriter.writeStartElement("name");
                xmlWriter.writeCharacters(buddy.getName() != null ? buddy.getName() : "");
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("address");
                xmlWriter.writeCharacters(buddy.getAddress() != null ? buddy.getAddress() : "");
                xmlWriter.writeEndElement();

                xmlWriter.writeStartElement("phoneNumber");
                xmlWriter.writeCharacters(String.valueOf(buddy.getPhoneNumber()));
                xmlWriter.writeEndElement();

                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();
            xmlWriter.close();
            return sw.toString();
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return "error";
        }
    }

    public void exportToXML(String file){
        try (FileWriter writer = new FileWriter(file)){
            writer.write(toXML());
            System.out.println("Exported AddressBook to: " + file);
        } catch (Exception e){
            System.out.println("ERROR EXPORTING to XML: " + e.getMessage());
        }
    }

    public void importFromXML(String file){

        try{
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser s = spf.newSAXParser();

            File fileXML = new File(file);

            if (!fileXML.exists()){
                System.err.println("File " + fileXML.getAbsolutePath() + " does not exist");
                return;
            }
            s.parse(fileXML, this);
            System.out.println("Imported AddressBook from: " + file);

        } catch (Exception e){
            System.out.println("ERROR IMPORTING from XML: " + e.getMessage());
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
        current = new StringBuilder();
        if ("BuddyInfo".equals(qName)){
            currentBuddy = new BuddyInfo();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
        if (currentBuddy != null){
            switch (qName){
                case "name":
                    currentBuddy.setName(current.toString().trim());
                    break;
                case "address":
                    currentBuddy.setAddress(current.toString().trim());
                    break;
                case "phoneNumber":
                    String phone = current.toString().trim();
                    String cleanPhone = phone.replaceAll("[^0-9]", "");
                    currentBuddy.setPhoneNumber(Integer.parseInt(cleanPhone));
                    break;
                case "BuddyInfo":
                    buddies.add(currentBuddy);
                    currentBuddy = null;
                    break;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException{
        if (current != null){
            current.append(new String(ch, start, length));
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

