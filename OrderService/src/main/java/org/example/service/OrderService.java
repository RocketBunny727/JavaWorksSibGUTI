package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.example.dto.OrderRequest;
import org.example.model.*;
import org.example.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final IOrderRepository orderRepository;
    private final ICustomerRepository customerRepository;
    private final IAddressRepository addressRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IItemRepository itemRepository;
    private final IPaymentRepository paymentRepository;

    public OrderService(IOrderRepository orderRepository,
                        ICustomerRepository customerRepository,
                        IAddressRepository addressRepository,
                        IOrderDetailRepository orderDetailRepository,
                        IItemRepository itemRepository,
                        IPaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.itemRepository = itemRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findOrdersByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    public List<Order> findOrders(String city, String street, String number, String zipcode,
                                  LocalDateTime from, LocalDateTime to,
                                  String paymentType, String customerName, String orderStatus) {
        return orderRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Order, Customer> customerJoin = root.join("customer");
            Join<Customer, Address> addressJoin = customerJoin.join("address");
            Join<Order, Payment> paymentJoin = root.join("payments", JoinType.LEFT);

            if (city != null) predicates.add(cb.equal(addressJoin.get("city"), city));
            if (street != null) predicates.add(cb.equal(addressJoin.get("street"), street));
            if (number != null) predicates.add(cb.equal(addressJoin.get("number"), number));
            if (zipcode != null) predicates.add(cb.equal(addressJoin.get("zipcode"), zipcode));
            if (from != null) predicates.add(cb.greaterThanOrEqualTo(root.get("date"), from));
            if (to != null) predicates.add(cb.lessThanOrEqualTo(root.get("date"), to));
            if (paymentType != null) predicates.add(cb.equal(paymentJoin.get("paymentType"), paymentType));
            if (customerName != null) predicates.add(cb.equal(customerJoin.get("name"), customerName));
            if (orderStatus != null) predicates.add(cb.equal(root.get("status"), orderStatus));

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }

    public Order createOrder(OrderRequest body) {
//        Customer customer = customerRepository.findByName(body.getCustomer().getName())
//                .orElseGet(() -> customerRepository.save(body.getCustomer()));
//
//        Address address = addressRepository.findByCityAndStreetAndNumberAndZipcode(
//                        body.getCustomer().getAddress().getCity(),
//                        body.getCustomer().getAddress().getStreet(),
//                        body.getCustomer().getAddress().getNumber(),
//                        body.getCustomer().getAddress().getZipcode())
//                .orElseGet(() -> addressRepository.save(body.getCustomer().getAddress()));
//
//        customer.setAddress(address);

        Order order = new Order();
        order.setDate(body.getDate());
        order.setStatus(body.getStatus());
        order.setCustomer(body.getCustomer());

        body.getOrderDetails().forEach(od -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(od.getQuantity());
            orderDetail.setTaxStatus(od.getTaxStatus());

            Item item = od.getItem();
            if (item.getId() == null) {
                item.setShippingWeight(od.getItem().getShippingWeight());
                item = itemRepository.save(item);
            }
            orderDetail.setItem(item);
            orderDetail.setOrder(order);

            orderDetailRepository.save(orderDetail);
        });

        order.setOrderDetails(body.getOrderDetails());

        body.getPayments().forEach(payment -> {
            if (payment.getId() == null) {
                if (payment instanceof Cash) {
                    payment.setType("CASH");
                } else if (payment instanceof Credit) {
                    payment.setType("CREDIT");
                } else if (payment instanceof Check) {
                    payment.setType("CHECK");
                }
                payment.setOrder(order);
                payment = paymentRepository.save(payment);
            }
            paymentRepository.save(payment);
        });

        order.setPayments(body.getPayments());

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderRequest body) {
        return orderRepository.findById(id).map(order -> {
            order.setDate(body.getDate());
            order.setStatus(body.getStatus());

            Customer customer = customerRepository.findByName(body.getCustomer().getName())
                    .orElseGet(() -> customerRepository.save(body.getCustomer()));

            Address address = addressRepository.findByCityAndStreetAndNumberAndZipcode(
                            body.getCustomer().getAddress().getCity(),
                            body.getCustomer().getAddress().getStreet(),
                            body.getCustomer().getAddress().getNumber(),
                            body.getCustomer().getAddress().getZipcode())
                    .orElseGet(() -> addressRepository.save(body.getCustomer().getAddress()));

            customer.setAddress(address);
            order.setCustomer(customer);

            body.getOrderDetails().forEach(od -> {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(od.getQuantity());
                orderDetail.setTaxStatus(od.getTaxStatus());

                Item item = od.getItem();
                if (item.getId() == null) {
                    item.setShippingWeight(od.getItem().getShippingWeight());
                    item = itemRepository.save(item);
                }
                orderDetail.setItem(item);
                orderDetail.setOrder(order);

                orderDetailRepository.save(orderDetail);
            });

            order.setOrderDetails(body.getOrderDetails());

            body.getPayments().forEach(payment -> {
                if (payment.getId() == null) {
                    if (payment instanceof Cash) {
                        payment.setType("CASH");
                    } else if (payment instanceof Credit) {
                        payment.setType("CREDIT");
                    } else if (payment instanceof Check) {
                        payment.setType("CHECK");
                    }
                    payment.setOrder(order);
                    paymentRepository.save(payment);
                }
                payment.setOrder(order);
                paymentRepository.save(payment);
            });


            order.setPayments(body.getPayments());

            return orderRepository.save(order);
        }).orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
