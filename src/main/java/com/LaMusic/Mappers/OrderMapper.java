package com.LaMusic.Mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.LaMusic.dto.OrderAddressDto;
import com.LaMusic.dto.OrderItemResponseDto;
import com.LaMusic.dto.OrderResponseDto;
import com.LaMusic.entity.Order;

@Component
public class OrderMapper {

	public static OrderResponseDto mapToDto(Order order) {
	    List<OrderItemResponseDto> items = order.getItems().stream()
	        .map(item -> new OrderItemResponseDto(
	            item.getProduct().getId(),
	            item.getProduct().getName(),
	            item.getQuantity(),
	            item.getUnitPrice()
	        )).toList();

	    OrderAddressDto shipping = new OrderAddressDto(
	        order.getShippingAddress().getRecipientName(),
	        order.getShippingAddress().getStreet(),
	        order.getShippingAddress().getNumber(),
	        order.getShippingAddress().getComplement(),
	        order.getShippingAddress().getNeighborhood(),
	        order.getShippingAddress().getCity(),
	        order.getShippingAddress().getState(),
	        order.getShippingAddress().getZipCode(),
	        order.getShippingAddress().getCountry()
	    );

	    OrderAddressDto billing = new OrderAddressDto(
	        order.getBillingAddress().getRecipientName(),
	        order.getBillingAddress().getStreet(),
	        order.getBillingAddress().getNumber(),
	        order.getBillingAddress().getComplement(),
	        order.getBillingAddress().getNeighborhood(),
	        order.getBillingAddress().getCity(),
	        order.getBillingAddress().getState(),
	        order.getBillingAddress().getZipCode(),
	        order.getBillingAddress().getCountry()
	    );

	    return new OrderResponseDto(
	        order.getId(),
	        order.getOrderDate(),
	        order.getTotalAmount(),
	        shipping,
	        billing,
	        items
	    );
	}

	
}
