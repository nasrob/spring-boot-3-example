package com.nasdev.springboot3example.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerRowMapperTest {

    @Test
    void testMapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Maria");
        when(resultSet.getString("email")).thenReturn("maria@test.com");

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(1, "Maria", "maria@test.com", 19);
        assertThat(actual).isEqualTo(expected);
    }

}