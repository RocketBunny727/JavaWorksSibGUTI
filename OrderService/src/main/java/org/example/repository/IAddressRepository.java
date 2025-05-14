package org.example.repository;

import org.example.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByCityAndStreetAndNumberAndZipcode(String city, String street, String number, String zipcode);
}
