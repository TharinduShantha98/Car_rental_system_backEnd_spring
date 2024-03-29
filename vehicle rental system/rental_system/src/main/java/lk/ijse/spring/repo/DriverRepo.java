package lk.ijse.spring.repo;

import lk.ijse.spring.entity.Driver;
import lk.ijse.spring.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DriverRepo extends JpaRepository<Driver, String> {
    List<Driver>  findDriverByStatus(String status);
    Driver findTopByOrderByDriverIdDesc();


}
