import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class XMLTesting {
    private AddressBook addressBook;
    private BuddyInfo bud1;
    private BuddyInfo bud2;

    private final String file = "test.xml";

    @BeforeEach
    public void setUp(){
        addressBook = new AddressBook();
        bud1 = new BuddyInfo("Mike", "123 street", 123);
        bud2 = new BuddyInfo("John", "987 street", 987);

        addressBook.addBuddy(bud1);
        addressBook.addBuddy(bud2);
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.deleteIfExists(Path.of(file));
    }

    @Test
    public void testBuddytoXML(){
        String xml = bud1.toXML();

        assertNotNull(xml, "should not be null");
        assertTrue(xml.contains("<BuddyInfo>"), "should contain BuddyInfo tag");
        assertTrue(xml.contains("<name>Mike</name>"), "Should contain name");
        assertTrue(xml.contains("<address>123 street</address>"), "Should contain address");
        assertTrue(xml.contains("<phoneNumber>123</phoneNumber>"), "Should contain phone number");

        //null test
        BuddyInfo buddyInfo = new BuddyInfo();
        String nullXML = buddyInfo.toXML();
        assertNotNull(nullXML, "should handle null values");
    }

    @Test
    public void testAddressToXML(){
        String xml = addressBook.toXML();
        assertNotNull(xml, "should not be null");
        assertTrue(xml.contains("<AddressBook>"), "Should contain AddressBook tag");
        assertTrue(xml.contains("</AddressBook>"), "Should close AddressBook tag");
        assertTrue(xml.contains("Mike"), "Should contain buddy names");
        assertTrue(xml.contains("John"), "Should contain all buddies");

        //null test
        AddressBook addressBook = new AddressBook();
        String nullXML = addressBook.toXML();
        assertNotNull(nullXML, "should handle null values");
        assertTrue(nullXML.contains("<AddressBook>"), "Should contain AddressBook tag root element");
    }

    @Test
    public void testExportXML(){
        addressBook.exportToXML(file);

        File x = new File(file);

        assertTrue(x.exists(), "xml file created");
        assertTrue(x.length() > 0, "xml file length 0");

        try {
            String content = Files.readString(Path.of(file));
            assertTrue(content.contains("<AddressBook>"), "File should contain XML structure");
            assertTrue(content.contains("John"), "File should contain buddy data");
        } catch (IOException e) {
            fail("Should be able to read exported file");
        }
    }

    @Test
    public void testImportXML(){
        addressBook.exportToXML(file);

        AddressBook imported = new AddressBook();
        imported.importFromXML(file);

        assertEquals(2, imported.getBuddies().size());
        BuddyInfo importedBud1 = imported.getBuddies().get(0);
        assertEquals(bud1.getName(), importedBud1.getName());
        assertEquals(bud1.getPhoneNumber(), importedBud1.getPhoneNumber());
        assertEquals(bud1.getAddress(), importedBud1.getAddress());

        BuddyInfo importedBud2 = imported.getBuddies().get(1);
        assertEquals(bud2.getName(), importedBud2.getName());
        assertEquals(bud2.getPhoneNumber(), importedBud2.getPhoneNumber());
        assertEquals(bud2.getAddress(), importedBud2.getAddress());
    }
}
