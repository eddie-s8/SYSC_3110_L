import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;



public class TestLab8 {

    private AddressBook addressBook1;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setup() {
        addressBook1 = new AddressBook();

        addressBook1.addBuddy(new BuddyInfo("Chopper", "Drum Island", 123));
        addressBook1.addBuddy(new BuddyInfo("Eneru", "Sky Island", 999));
        addressBook1.addBuddy(new BuddyInfo("Bonclay", "Impel Down", 456));
        addressBook1.addBuddy(new BuddyInfo("Kinemon", "Wano", 333));
    }

    @Test
    void testExport() throws IOException {
        File exportFile = tempDir.resolve("test_export.text").toFile();

        addressBook1.save(exportFile.getAbsolutePath());

        assertTrue(exportFile.exists(), "Export file Should exist");
        assertTrue(exportFile.length() > 0, "Export file length should be greater than 0");
    }

    @Test
    void testImport() throws IOException {
        File exportFile = tempDir.resolve("test_export.text").toFile();

        addressBook1.save(exportFile.getAbsolutePath());

        AddressBook addressBook2 = AddressBook.importAddressBook(exportFile.getAbsolutePath());
        assertEquals(addressBook1.getBuddies().size(), addressBook2.getBuddies().size());

        assertEquals(addressBook1.getBuddy(0), addressBook2.getBuddy(0));
        assertEquals(addressBook1.getBuddy(1), addressBook2.getBuddy(1));
        assertEquals(addressBook1.getBuddy(2), addressBook2.getBuddy(2));
        assertEquals(addressBook1.getBuddy(3), addressBook2.getBuddy(3));


    }

    @Test
    void testSerialize() throws IOException {
        File serializeFile = tempDir.resolve("test_serialize_file.ser").toFile();
        addressBook1.serializeAddressBook(serializeFile.getAbsolutePath());

        assertTrue(serializeFile.exists(), "Serialize file Should exist");
        assertTrue(serializeFile.length() > 0, "Serialize file length should be greater than 0");

    }

    @Test
    void testDeserialize() throws IOException {
        File serializeFile = tempDir.resolve("test_serialize_file.ser").toFile();

        addressBook1.serializeAddressBook(serializeFile.getAbsolutePath());

        AddressBook addressBook2 = AddressBook.deserializeAddressBook(serializeFile.getAbsolutePath());
        assertEquals(addressBook1.getBuddies().size(), addressBook2.getBuddies().size());

        assertEquals(addressBook1.getBuddy(0), addressBook2.getBuddy(0));
        assertEquals(addressBook1.getBuddy(1), addressBook2.getBuddy(1));
        assertEquals(addressBook1.getBuddy(2), addressBook2.getBuddy(2));
        assertEquals(addressBook1.getBuddy(3), addressBook2.getBuddy(3));

    }
}
