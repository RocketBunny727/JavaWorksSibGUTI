package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.dto.CustomerRequest;
import org.example.model.Address;
import org.example.model.AuthUser;
import org.example.model.Customer;
import org.example.repository.IAddressRepository;
import org.example.repository.IAuthUserRepository;
import org.example.repository.ICustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final ICustomerRepository customerRepository;
    private final IAuthUserRepository authUserRepository;
    private final IAddressRepository addressRepository;

    public Optional<Customer> findCustomerByLogin(String login) {
        return authUserRepository.findByLogin(login)
                .map(AuthUser::getCustomer);
    }

    public Optional<Customer> findCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer addNewCustomerIfNotExists(Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findByName(customer.getName());

        if (existingCustomer.isPresent()) {
            return existingCustomer.get();
        }

        Address address = customer.getAddress();
        Optional<Address> existingAddress = addressRepository.findByCityAndStreetAndNumberAndZipcode(
                address.getCity(),
                address.getStreet(),
                address.getNumber(),
                address.getZipcode()
        );

        if (existingAddress.isPresent()) {
            customer.setAddress(existingAddress.get());
        } else {
            Address savedAddress = addressRepository.saveAndFlush(address);
            customer.setAddress(savedAddress);
        }

        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, CustomerRequest body) {
        return customerRepository.findById(id).map( customer -> {
            customer.setName(body.getName());

            Address address = addressRepository.findByCityAndStreetAndNumberAndZipcode(
                    body.getAddress().getCity(),
                    body.getAddress().getStreet(),
                    body.getAddress().getNumber(),
                    body.getAddress().getZipcode()
            ).orElseGet(() -> addressRepository.save(body.getAddress()));

            customer.setAddress(address);

            return customerRepository.save(customer);
        }).orElseThrow(() -> new EntityNotFoundException("Customer with id " + id + " not found"));
    }

    public void deleteCustomer(Long id) { customerRepository.deleteById(id); }
}
