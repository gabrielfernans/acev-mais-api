package com.acev.api.services;

import com.acev.api.dtos.address.AddressRequestDTO;
import com.acev.api.dtos.address.AddressResponseDTO;
import com.acev.api.models.AddressModel;
import com.acev.api.repositories.AddressRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressService {
  @Autowired
  private AddressRepository addressRepository;

  public Optional<AddressModel> getAddressById(UUID id) {
    return addressRepository.findById(id);
  }

  public List<AddressResponseDTO> getAllAddresses() {
    List<AddressModel> addresses = addressRepository.findAll();

    return addresses.stream().map(AddressResponseDTO::convertAddressDTO)
          .collect(Collectors.toList());
  }

  public AddressModel createAddress(@Valid AddressRequestDTO addressDTO) {
    var addressModel = new AddressModel();
    BeanUtils.copyProperties(addressDTO, addressModel);
    return addressRepository.save(addressModel);
  }

  public AddressModel updateAddress(@Valid AddressRequestDTO addressDTO, AddressModel address) {
    BeanUtils.copyProperties(addressDTO, address);
    address.setUpdatedAt(LocalDateTime.now());
    return addressRepository.save(address);
  }

  public void deleteAddress(AddressModel address) {
    addressRepository.delete(address);
  }


}
