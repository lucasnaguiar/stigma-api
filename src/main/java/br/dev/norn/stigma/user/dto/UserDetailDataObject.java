package br.dev.norn.stigma.user.dto;

import br.dev.norn.stigma.user.model.User;

public record UserDetailDataObject (
        Long id,
        Long studioId,
        String name,
        String email,
        String phoneNumber,
        Boolean isActive
) {
    public UserDetailDataObject (User user) {
        this(
                user.getId(),
                user.getStudioId(),
                user.getName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getIsActive()
        );
    }
}