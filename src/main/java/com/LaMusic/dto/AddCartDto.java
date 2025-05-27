package com.LaMusic.dto;

import java.util.UUID;

public record AddCartDto (UUID userId, Long productId, Integer quantity){

}
