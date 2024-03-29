package lk.ijse.spring.service.impl;


import lk.ijse.spring.dto.OrdersDto;
import lk.ijse.spring.entity.OrderDetail;
import lk.ijse.spring.entity.Orders;
import lk.ijse.spring.repo.OrderDetailRepo;
import lk.ijse.spring.repo.OrderRepo;
import lk.ijse.spring.service.OrderService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @Autowired
    private ModelMapper modelMapper;




    @Override
    public boolean addOrder(OrdersDto ordersDto) {

        if(!orderRepo.existsById(ordersDto.getOrderId())){
            orderRepo.save(modelMapper.map(ordersDto, Orders.class));

            if (ordersDto.getOrderDetails().size()<1) {
                throw  new RuntimeException("no cars added for the order");
            }

            for (OrderDetail orderDetail: ordersDto.getOrderDetails()
                 ) {
                orderDetailRepo.save(orderDetail);
                return true;

            }
        }




        return false;
    }

    @Override
    public OrdersDto searchOrder(String id) {
        if (orderRepo.existsById(id)) {
            return modelMapper.map(orderRepo.findById(id),OrdersDto.class);
        }else {
            throw new RuntimeException("this order id not found");
        }

    }

    @Override
    public boolean updateOrder(OrdersDto ordersDto) {
        if (orderRepo.existsById(ordersDto.getOrderId())) {
            orderRepo.save(modelMapper.map(ordersDto,Orders.class));

            if(ordersDto.getOrderDetails().size()>1){
                for (OrderDetail o1:ordersDto.getOrderDetails()
                     ) {
                    orderDetailRepo.save(o1);
                }
                return true;
            }


        }


        return false;
    }

    @Override
    public boolean deleteOrder(String id) {


        if(orderRepo.existsById(id)){
            orderRepo.deleteById(id);

            if(orderDetailRepo.existsById(id)){
                orderDetailRepo.deleteById(id);
                return true;
            }
        }

        return false;
    }

    @Override
    public List<OrdersDto> getAllOrder() {

       /* List<Orders> all = orderRepo.findAll();
        List<OrdersDto> allOrders = new ArrayList<>();

        for (Orders o1: all
             ) {
            allOrders.add(modelMapper.map(o1,OrdersDto.class));
        }

        return allOrders;*/


        return modelMapper.map(orderRepo.findAll(), new TypeToken<List<OrdersDto>>() {
        }.getType());


    }

    @Override
    public String lastOrderId() {
        Orders lastOrder = orderRepo.findTopByOrderByOrderIdDesc();
        String lastOrderId =  lastOrder.getOrderId();

        if (lastOrder != null) {

            int tempId = Integer.parseInt(lastOrderId.split("-")[1]);
            tempId = tempId +1;
            return  "O-" + tempId;

        }else{
            return "O-100";
        }

    }
}
