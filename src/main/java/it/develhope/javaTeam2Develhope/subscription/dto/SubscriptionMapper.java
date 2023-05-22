package it.develhope.javaTeam2Develhope.subscription.dto;

import it.develhope.javaTeam2Develhope.subscription.Subscription;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {
    private final ModelMapper modelMapper;

    public SubscriptionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SubscriptionDTO toDto(Subscription subscription) {
        return modelMapper.map(subscription, SubscriptionDTO.class);
    }
}
