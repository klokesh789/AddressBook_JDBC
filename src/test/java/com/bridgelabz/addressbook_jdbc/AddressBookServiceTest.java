package com.bridgelabz.addressbook_jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class AddressBookServiceTest {
	AddressBookDBService addressBook = new AddressBookDBService() ;
    
    // UC16 - Ability to retrieve data from database
         @Test
        public void  givenAddressBookData_WhenReturned_ShouldMatchEntryCount() throws AddressBookException
        {
        	List<AddressBookData> addressBookList = addressBook.readData();
        	Assert.assertEquals(3,addressBookList.size());
        }
}
