package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.entity.RentalStatus;

import java.util.UUID;

@Data
@Builder
public class RentalView {
    private final UUID rentalId;
    private final UUID bookId;
    private final UUID bookCopyId;
    private final RentalStatus status;
}
