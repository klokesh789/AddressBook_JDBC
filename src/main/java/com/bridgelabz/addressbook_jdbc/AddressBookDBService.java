package com.bridgelabz.addressbook_jdbc;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	private static AddressBookDBService addressBookDBService;

	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}

	public List<AddressBookData> readData() throws AddressBookException {
		String sql = "SELECT * FROM addressBook; ";
		return this.getAddressBookDataUsingDB(sql);
	}

	List<AddressBookData> getAddressBookDataUsingDB(String sql) throws AddressBookException {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection connection = AddressBookConnection.getConnection();) {
			Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = ((java.sql.Statement) statement).executeQuery(sql);
			addressBookList = this.getAddressBookData(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookList;
	}

	private List<AddressBookData> getAddressBookData(ResultSet resultSet) throws AddressBookException {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString("FirstName");
				String lastName = resultSet.getString("LastName");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("City");
				String state = resultSet.getString("State");
				java.math.BigDecimal zip = resultSet.getBigDecimal("Zip");
				java.math.BigDecimal phoneNo = resultSet.getBigDecimal("PhoneNumber");
				String email = resultSet.getString("Email");
				addressBookList
						.add(new AddressBookData(firstName, lastName, address, city, state, zip, phoneNo, email));
			}
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookList;
	}

	public int updateAddressBookData(String firstname, String address) {
		// TODO Auto-generated method stub
		return 0;
	}
}
