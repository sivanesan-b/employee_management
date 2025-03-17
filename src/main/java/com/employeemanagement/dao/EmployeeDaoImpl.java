package com.employeemanagement.dao;

import com.employeemanagement.config.DbConfiguration;
import com.employeemanagement.config.LoggerConfig;
import com.employeemanagement.model.Address;
import com.employeemanagement.model.AddressTypes;
import com.employeemanagement.model.Employee;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDaoImpl implements EmployeeDao{
    private static final Logger LOGGER = LoggerConfig.getLogger();
    @Override
    public boolean isAdmin(String username, String password) {
        String adminVerifyQuery = "SELECT admin_id FROM admin WHERE admin_username = ? AND admin_password = ?;";
        try (Connection connection = DbConfiguration.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(adminVerifyQuery);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,"Unable to connect to DB", e);
        }
        return false;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        Connection connection = null;
        int employeeQueryResult;
        boolean isSameAddress;
        int addressQueryResult1, addressQueryResult2 = 0;

        try {
            connection = DbConfiguration.getConnection();
            //verifying whether the phone already exists in database or not
            connection.setAutoCommit(false);
            String verificationQuery = "SELECT employee_id FROM employee WHERE employee_phone_no = ?";
            PreparedStatement verificationPreparedStatement = connection.prepareStatement(verificationQuery);
            verificationPreparedStatement.setString(1,employee.getPhone());
            ResultSet verificationQueryResult = verificationPreparedStatement.executeQuery();
            if(verificationQueryResult.next()){
                LOGGER.log(Level.WARNING, "Phone Number already exists");
                return null;
            }
            String employeeQuery = "INSERT INTO employee(employee_name, employee_phone_no) VALUES(?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(employeeQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getPhone());
            employeeQueryResult = preparedStatement.executeUpdate();

            //retrieving auto incremented primary key from insert query
            if(employeeQueryResult == 1){
                try(ResultSet resultSets = preparedStatement.getGeneratedKeys()){
                    if(resultSets.next()){
                        employee.setId(resultSets.getInt(1));
                    }
                }
            }

            // checking two addresses are equal
            isSameAddress = employee.getPermanentAddress().equals(employee.getCurrentAddress());

            //inserting address into db
            String addressInsertQuery = "INSERT INTO employee_address(employee_id, address_type, street_name, state) VALUES(?, ?, ?, ?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(addressInsertQuery);
            preparedStatement1.setLong(1, employee.getId());
            preparedStatement1.setString(3, employee.getPermanentAddress().getStreetName());
            preparedStatement1.setString(4, employee.getPermanentAddress().getState());

            // checking if the current address and permanent address are same or not
            if(isSameAddress){
                preparedStatement1.setString(2, AddressTypes.BOTH_PERMANENT_AND_CURRENT.toString());
                addressQueryResult1 = preparedStatement1.executeUpdate();
            } else {
                preparedStatement1.setString(2, AddressTypes.PERMANENT.toString());
                addressQueryResult1 = preparedStatement1.executeUpdate();

                // inserting current address into db
                PreparedStatement preparedStatement2 = connection.prepareStatement(addressInsertQuery);
                preparedStatement2.setLong(1, employee.getId());
                preparedStatement2.setString(2, AddressTypes.CURRENT.toString());
                preparedStatement2.setString(3, employee.getCurrentAddress().getStreetName());
                preparedStatement2.setString(4, employee.getCurrentAddress().getState());
                addressQueryResult2 = preparedStatement2.executeUpdate();
            }
            connection.commit();
            if(employeeQueryResult == 1 &&
                    (addressQueryResult1 == 1 || addressQueryResult2 == 1)){
                LOGGER.log(Level.INFO,"Employee Added successfully");
            }else {
                LOGGER.log(Level.SEVERE,"Something went wrong, Unable to add Employee");
            }
            return employee;
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE,"Unable to rollback", ex);
                }
            }
            LOGGER.log(Level.SEVERE,"Unable to add Employee", e);
        } finally {
            try {
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Unable to close the Connection");
            }
        }
        return new Employee();
    }

    @Override
    public Employee getEmployee(long employeeId) {
        Employee employee = new Employee();

        try(Connection connection = DbConfiguration.getConnection()){
            // e->employee   ea->employee_address
            String selectQuery = "SELECT e.employee_name, e.employee_id, e.employee_phone_no,ea.address_type, ea.street_name, ea.state " +
                    "FROM employee AS e " +
                    "INNER JOIN employee_address " +
                    "AS ea ON e.employee_id = ea.employee_id " +
                    "WHERE e.employee_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setLong(1, employeeId);
            ResultSet resultSets = preparedStatement.executeQuery();
            if (resultSets.next()) {
                employee.setId(employeeId);
                employee.setName(resultSets.getString("employee_name"));
                employee.setPhone(resultSets.getString("employee_phone_no"));
                if(resultSets.getString("address_type")
                        .equals(AddressTypes.BOTH_PERMANENT_AND_CURRENT.toString())){
                    employee.getPermanentAddress().setAddressType(AddressTypes.BOTH_PERMANENT_AND_CURRENT);
                    employee.getPermanentAddress().setStreetName(resultSets.getString("street_name"));
                    employee.getPermanentAddress().setState(resultSets.getString("state"));
                }else{
                    do{
                        if(resultSets.getString("address_type")
                                .equals(AddressTypes.PERMANENT.toString())){
                            employee.getPermanentAddress().setAddressType(AddressTypes.PERMANENT);
                            employee.getPermanentAddress().setStreetName(resultSets.getString("street_name"));
                            employee.getPermanentAddress().setState(resultSets.getString("state"));
                        } else {
                            employee.getCurrentAddress().setAddressType(AddressTypes.CURRENT);
                            employee.getCurrentAddress().setStreetName(resultSets.getString("street_name"));
                            employee.getCurrentAddress().setState(resultSets.getString("street_name"));
                        }
                    }while (resultSets.next());
                }
                return employee;
            } else {
                LOGGER.log(Level.WARNING, "No Employee found for the id :" + employeeId);
                return null;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to get Employee Details.", ex);
        }
        return new Employee();
    }

    @Override
    public int removeEmployee(long employeeId) {
        try (Connection connection = DbConfiguration.getConnection()){
            String query = "DELETE FROM employee WHERE employee_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, employeeId);
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Unable to delete Employee");
        }
        return -1;
    }

    @Override
    public int updateEmployee(Employee oldEmployee, Employee newEmployee) {
        String employeeUpdateQuery = "UPDATE employee SET employee_name = ?, phone = ? WHERE employee_id = ?";
        int rows = 0;
        Connection connection = null;
        PreparedStatement addressPreparedStmt;
        try {
            connection = DbConfiguration.getConnection();
            connection.setAutoCommit(false);
            PreparedStatement employeePreparedStmt = connection.prepareStatement(employeeUpdateQuery);
            employeePreparedStmt.setString(1, newEmployee.getName());
            employeePreparedStmt.setString(2, newEmployee.getPhone());
            employeePreparedStmt.setLong(3, newEmployee.getId());
            rows += employeePreparedStmt.executeUpdate();

            // extracting addresses
            long employeeId = newEmployee.getId();
            Address oldCurrent = oldEmployee.getCurrentAddress();
            Address oldPermanent = oldEmployee.getPermanentAddress();
            Address newCurrent = newEmployee.getCurrentAddress();
            Address newPermanent = newEmployee.getPermanentAddress();

            // one address in db and employee
            if (oldPermanent.getAddressType().equals(AddressTypes.BOTH_PERMANENT_AND_CURRENT) && oldCurrent.getAddressType() == null) {
                if (newPermanent.equals(newCurrent)) {
                    // update one address as "both_permanent_and_current"
                    String updateQuery = "UPDATE employee_address SET street_name = ?, state = ?, address_type = 'BOTH_PERMANENT_AND_CURRENT' " +
                            "WHERE employee_id = ?";
                    addressPreparedStmt = connection.prepareStatement(updateQuery);
                    addressPreparedStmt.setString(1, newCurrent.getStreetName());
                    addressPreparedStmt.setString(2, newCurrent.getState());
                    addressPreparedStmt.setLong(3, employeeId);
                    rows += addressPreparedStmt.executeUpdate();
                    LOGGER.log(Level.INFO, "Updated single address as both permanent and current.");
                } else {
                    // convert single address to two separate addresses
                    String updateExisting = "UPDATE employee_address SET street_name = ?, state = ?, address_type = 'PERMANENT' " +
                            "WHERE employee_id = ?";
                    addressPreparedStmt = connection.prepareStatement(updateExisting);
                    addressPreparedStmt.setString(1, newPermanent.getStreetName());
                    addressPreparedStmt.setString(2, newPermanent.getState());
                    addressPreparedStmt.setLong(3, employeeId);
                    rows += addressPreparedStmt.executeUpdate();
                    addressPreparedStmt.close();

                    String insertNew = "INSERT INTO employee_address (employee_id, address_type, street_name, state) " +
                            "VALUES (?, 'CURRENT', ?, ?)";
                    addressPreparedStmt = connection.prepareStatement(insertNew);
                    addressPreparedStmt.setLong(1, employeeId);
                    addressPreparedStmt.setString(2, newCurrent.getStreetName());
                    addressPreparedStmt.setString(3, newCurrent.getState());
                    rows += addressPreparedStmt.executeUpdate();
                    LOGGER.log(Level.INFO, "Updated one address and added a new address.");
                }
            } // Two addresses exist in DB
            else if (oldPermanent.getAddressType() != null && oldCurrent.getAddressType() != null) {
                if (newPermanent.equals(newCurrent)) {
                    // update into one address as "both_permanent_and_current"
                    String updateQuery = "UPDATE employee_address SET street_name = ?, state = ?, address_type = 'BOTH_PERMANENT_AND_CURRENT' " +
                            "WHERE employee_id = ? AND address_type = 'PERMANENT'";
                    addressPreparedStmt = connection.prepareStatement(updateQuery);
                    addressPreparedStmt.setString(1, newPermanent.getStreetName());
                    addressPreparedStmt.setString(2, newPermanent.getState());
                    addressPreparedStmt.setLong(3, employeeId);
                    rows += addressPreparedStmt.executeUpdate();
                    addressPreparedStmt.close();

                    String deleteQuery = "DELETE FROM employee_address WHERE employee_id = ? AND address_type = 'CURRENT'";
                    addressPreparedStmt = connection.prepareStatement(deleteQuery);
                    addressPreparedStmt.setLong(1, employeeId);
                    rows += addressPreparedStmt.executeUpdate();
                    LOGGER.log(Level.INFO, "Merged two addresses into one.");
                } else {
                    // update both addresses
                    if (!oldPermanent.equals(newPermanent)) {
                        String updatePermanent = "UPDATE employee_address SET street_name = ?, state = ? " +
                                "WHERE employee_id = ? AND address_type = 'PERMANENT'";
                        addressPreparedStmt = connection.prepareStatement(updatePermanent);
                        addressPreparedStmt.setString(1, newPermanent.getStreetName());
                        addressPreparedStmt.setString(2, newPermanent.getState());
                        addressPreparedStmt.setLong(3, employeeId);
                        rows += addressPreparedStmt.executeUpdate();
                        addressPreparedStmt.close();
                    }

                    if (!oldCurrent.equals(newCurrent)) {
                        String updateCurrent = "UPDATE employee_address SET street_name = ?, state = ? " +
                                "WHERE employee_id = ? AND address_type = 'CURRENT'";
                        addressPreparedStmt = connection.prepareStatement(updateCurrent);
                        addressPreparedStmt.setString(1, newCurrent.getStreetName());
                        addressPreparedStmt.setString(2, newCurrent.getState());
                        addressPreparedStmt.setLong(3, employeeId);
                        rows += addressPreparedStmt.executeUpdate();
                        addressPreparedStmt.close();
                    }
                    LOGGER.log(Level.INFO, "Updated both addresses separately.");
                }
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Unable to rollback ", ex);
            }
            LOGGER.log(Level.WARNING, "Unable to update employee details", e);
        } finally {
            try {
                if (connection != null){
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Unable to close connection");
            }
        }
        return rows;
    }
}
