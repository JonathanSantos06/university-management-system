package mx.edu.sgu.student.repository;

import mx.edu.sgu.student.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByStudentId(UUID studentId);
}
