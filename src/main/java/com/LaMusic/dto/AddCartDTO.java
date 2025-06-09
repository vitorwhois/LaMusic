package com.LaMusic.dto;

import java.util.UUID;

public record AddCartDTO(UUID productId, Integer quantity) {}
