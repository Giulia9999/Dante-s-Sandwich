package it.develhope;

import it.develhope.orders.Orders;
import it.develhope.orders.OrdersRepo;

import it.develhope.users.Users;
import it.develhope.users.UsersRepo;

public class AccessDB {
    public static void main(String[] args) {
        System.out.println(new OrdersRepo().getOrderById(new Orders()));
        new UsersRepo().insertUser(new Users());
    }
}
