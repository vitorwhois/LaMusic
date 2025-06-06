package com.LaMusic.dto;

import java.util.UUID;

public record AddCartDTO (UUID userId, UUID productId, Integer quantity){

}
