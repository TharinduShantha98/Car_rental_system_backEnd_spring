package lk.ijse.spring.service.impl;

import lk.ijse.spring.dto.DriverDto;
import lk.ijse.spring.entity.Driver;
import lk.ijse.spring.repo.DriverRepo;
import lk.ijse.spring.service.DriverService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class DriverServiceImpl implements DriverService {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    ModelMapper modelMapper;



    @Override
    public void saveDriver(DriverDto driverDto) {
        if(!driverRepo.existsById(driverDto.getDriverId())){
            driverRepo.save(modelMapper.map(driverDto, Driver.class));

        }else{
            throw new RuntimeException("This driver"+ driverDto.getDriverId()+ "already exist");
        }
    }

    @Override
    public DriverDto searchDriver(String id) {

        if(driverRepo.existsById(id)){
            Optional<Driver> byId = driverRepo.findById(id);
            Driver driver = byId.get();
            return  modelMapper.map(driver, DriverDto.class);

        }else{
            throw new RuntimeException("this Driver not found in database");
        }


    }

    @Override
    public void deleteDriver(String id) {
        if(driverRepo.existsById(id)){
            driverRepo.deleteById(id);

        }else{
            throw new RuntimeException("this Driver not found in  database");
        }

    }

    @Override
    public void updateDriver(DriverDto driverDto) {
        if(driverRepo.existsById(driverDto.getDriverId())){
            driverRepo.save(modelMapper.map(driverDto,Driver.class));
        }else{
            throw  new RuntimeException("this Driver not found in database");
        }



    }

    @Override
    public List<DriverDto> getAllDriver() {

        List<Driver> all = driverRepo.findAll();
        List<DriverDto> allDrivers = new ArrayList<>();
        if(!all.isEmpty()){
            for (Driver d:all
                 ) {
                allDrivers.add(modelMapper.map(d,DriverDto.class));
            }

            return allDrivers;

        }else{
            return null;
            //throw new RuntimeException("please input first Drivers for database");
        }

    }

    @Override
    public List<String> availableDrivers() {
        List<String>  availableDrivers =  new ArrayList<>();
        List<Driver> available = driverRepo.findDriverByStatus("available");

        for(int i =0; i < available.size(); i++){


            availableDrivers.add(available.get(i).getDriverId());
        }

        return availableDrivers;


    }

    @Override
    public String nextDriverId() {
        Driver topByOrderByDriverIdDesc = driverRepo.findTopByOrderByDriverIdDesc();
        String driverId = topByOrderByDriverIdDesc.getDriverId();

        if (topByOrderByDriverIdDesc != null) {

            int tempId = Integer.parseInt(driverId.split("-")[1]);
            tempId = tempId +1;
            return  "D-" + tempId;

        }else{
            return "D-100";
        }
    }


}
