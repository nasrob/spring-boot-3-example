package com.nasdev.springboot3example.customer;

import com.nasdev.springboot3example.exception.DuplicateResourceException;
import com.nasdev.springboot3example.exception.RequestValidateException;
import com.nasdev.springboot3example.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void testGetAllCustomers() {
        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void testGetCustomer() {
        // Given
        int id = 11;
        Customer customer = new Customer(id, "Alfred", "alfred@gmail.com", 27);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(11);

        // Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        int id = 11;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void testAddCustomer() {
        // Given
        String email = "alfred@gmail.com";
        CustomerRequest customerRequest = new CustomerRequest("Alfred", email, 27);
        when(customerDao.isExistsWithEmail(email)).thenReturn(false);

        // When
        underTest.addCustomer(customerRequest);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(customerRequest.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customerRequest.email());
        assertThat(capturedCustomer.getAge()).isEqualTo(customerRequest.age());
    }

    @Test
    void testAddCustomerWillThrowWenEmailExists() {
        // Given
        String email = "alfred@gmail.com";
        CustomerRequest customerRequest = new CustomerRequest("Alfred", email, 27);
        when(customerDao.isExistsWithEmail(email)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.addCustomer(customerRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDao, never()).insertCustomer(any());
    }

    @Test
    void testDeleteCustomer() {
        // Given
        int id = 11;

        when(customerDao.isExistsWithId(id)).thenReturn(true);

        // When
         underTest.deleteCustomer(11);

        // Then
        verify(customerDao).deleteCustomerById(id);
        // assertThat(actual).isEqualTo(customer);
    }

    @Test
    void deleteCustomerWillThrowWhenNotExists() {
        // Given
        int id = 11;

        when(customerDao.isExistsWithId(id)).thenReturn(false);

        // When
        assertThatThrownBy(() -> underTest.deleteCustomer(11))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        // Then
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void testUpdateAllCustomerProps() {
        // Given
        int id = 11;
        String email = "alfred@gmail.com";
        Customer customer = new Customer(id, "Alfred", email, 27);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Georges@gmail.com";

        CustomerRequest request = new CustomerRequest("Georges", newEmail, 33);

        when(customerDao.isExistsWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(request.name());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(cupturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void testUpdateCustomerName() {
        // Given
        int id = 11;
        String email = "alfred@gmail.com";
        Customer customer = new Customer(id, "Alfred", "alfred@gmail.com", 27);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRequest request = new CustomerRequest("Georges", null, null);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(request.name());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(cupturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void testUpdateCustomerEmail() {
        // Given
        int id = 11;
        String email = "alfred@gmail.com";
        Customer customer = new Customer(id, "Alfred", "alfred@gmail.com", 27);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Georges@gmail.com";
        CustomerRequest request = new CustomerRequest(null, newEmail, null);
        when(customerDao.isExistsWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(cupturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void testUpdateCustomerAge() {
        // Given
        int id = 11;
        String email = "alfred@gmail.com";
        Customer customer = new Customer(id, "Alfred", "alfred@gmail.com", 27);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRequest request = new CustomerRequest(null, null, 77);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(cupturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void testUpdateCustomerEmailThrowsException() {
        // Given
        int id = 11;
        String email = "alfred@gmail.com";
        Customer customer = new Customer(id, "Alfred", "alfred@gmail.com", 27);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Georges@gmail.com";
        CustomerRequest request = new CustomerRequest(null, newEmail, null);
        when(customerDao.isExistsWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void testUpdateCustomerThrowsExceptionWhenHasNoChanges() {
        // Given
        int id = 11;
        String email = "alfred@gmail.com";
        Customer customer = new Customer(id, "Alfred", "alfred@gmail.com", 27);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRequest request = new CustomerRequest(customer.getName(), customer.getEmail(), customer.getAge());

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidateException.class)
                .hasMessage("no data changes found");

        // Then
        verify(customerDao, never()).updateCustomer(any());
    }
}