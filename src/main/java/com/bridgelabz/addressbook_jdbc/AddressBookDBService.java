package com.bridgelabz.addressbook_jdbc;

import java.beans.Statement;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {
	private PreparedStatement addressBookPreparedStatement;
	private static AddressBookDBService addressBookDBService;
	private List<AddressBookData> addressBookData;

	private AddressBookDBService() {
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/address_book_system?useSSL=false";
		String username = "root";
		String password = "******";
		Connection con;
		System.out.println("Connecting to database ");
		con = DriverManager.getConnection(jdbcURL, username, password);
		System.out.println("Connection is successful ");
		return con;

	}

	public static AddressBookDBService getInstance() {
		if (addressBookDBService == null)
			addressBookDBService = new AddressBookDBService();
		return addressBookDBService;
	}

	public List<AddressBookData> readData() throws AddressBookException {
		String query;
		query = "select * from addressBook";
		return getAddressBookDataUsingDB(query);
	}


	private List<AddressBookData> getAddressBookDataUsingDB(String sql) throws AddressBookException {
		List<AddressBookData> addressBookData = new ArrayList<>();
		try (Connection connection = this.getConnection()) {

	List<AddressBookData> getAddressBookDataUsingDB(String sql) throws AddressBookException {
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection connection = AddressBookConnection.getConnection();) {
  	Statement statement = (Statement) connection.createStatement();
			ResultSet resultSet = ((java.sql.Statement) statement).executeQuery(sql);
			addressBookData = this.getAddressBookDetails(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}

	private void prepareAddressBookStatement() throws AddressBookException {
		try {
			Connection connection = this.getConnection();
			String query = "select * from addressBook where FirstName = ?";
			addressBookPreparedStatement = connection.prepareStatement(query);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
	}

	private List<AddressBookData> getAddressBookDetails(ResultSet resultSet) throws AddressBookException {
		List<AddressBookData> addressBookData = new ArrayList<>();
		try {
			while (resultSet.next()) {
				String firstName = resultSet.getString("FirstName");
				String lastName = resultSet.getString("LastName");
				String address = resultSet.getString("Address");
				String city = resultSet.getString("City");
				String state = resultSet.getString("State");
				BigDecimal zip = resultSet.getBigDecimal("Zip");
				BigDecimal phoneNo = resultSet.getBigDecimal("PhoneNumber");
				String email = resultSet.getString("Email");
				addressBookData
						.add(new AddressBookData(firstName, lastName, address, city, state, zip, phoneNo, email));
			}
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		return addressBookData;
	}

	public int updateAddressBookData(String firstname, String address) throws AddressBookException {
		String query = String.format("update addressBook set Address = '%s' where FirstName = '%s';", address,
				firstname);
		try (Connection connection = this.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			return preparedStatement.executeUpdate(query);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
	}

	public List<AddressBookData> getAddressBookData(String firstname) throws AddressBookException {
		if (this.addressBookPreparedStatement == null)
			this.prepareAddressBookStatement();
		try {
			addressBookPreparedStatement.setString(1, firstname);
			ResultSet resultSet = addressBookPreparedStatement.executeQuery();
			addressBookData = this.getAddressBookDetails(resultSet);
		} catch (SQLException e) {
			throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
		}
		System.out.println(addressBookData);
		return addressBookData;
	}

	public List<AddressBookData> readData(LocalDate start, LocalDate end) throws AddressBookException {
		String query = null;
		if (start != null)
			query = String.format("select * from addressBook where Date between '%s' and '%s';", start, end);
		if (start == null)
			query = "select * from addressBook";
		List<AddressBookData> addressBookList = new ArrayList<>();
		try (Connection con = this.getConnection();) {
			Statement statement = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) statement).executeQuery(query);
			addressBookList = this.getAddressBookDetails(rs);
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
