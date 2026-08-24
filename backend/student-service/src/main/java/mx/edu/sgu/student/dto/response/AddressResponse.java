package mx.edu.sgu.student.dto.response;

import mx.edu.sgu.student.domain.Address;

import java.util.UUID;

public record AddressResponse(
        UUID id,
        Address.AddressType addressType,
        String street,
        String extNumber,
        String intNumber,
        String neighborhood,
        String city,
        String state,
        String postalCode,
        String country
) {
    public static AddressResponse from(Address a) {
        return new AddressResponse(
                a.getId(), a.getAddressType(), a.getStreet(), a.getExtNumber(), a.getIntNumber(),
                a.getNeighborhood(), a.getCity(), a.getState(), a.getPostalCode(), a.getCountry()
        );
    }
}
