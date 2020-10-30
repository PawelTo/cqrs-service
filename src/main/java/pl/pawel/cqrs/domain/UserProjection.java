package pl.pawel.cqrs.domain;

import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;
import pl.pawel.cqrs.persistence.entity.UserEntity;
import pl.pawel.cqrs.persistence.repository.UserEntityRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Long.valueOf;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Service
public class UserProjection {

    private final UserEntityRepository userEntityRepository;

    @EventHandler
    public void on(UserCreatedEvent userCreatedEvent) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(valueOf(userCreatedEvent.getUserId()));
        userEntity.setName(userCreatedEvent.getName());
        userEntityRepository.save(userEntity);
    }

    @EventHandler
    public void on(UserChangedNameEvent userChangedNameEvent) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(valueOf(userChangedNameEvent.getUserId()));
        userEntity.setName(userChangedNameEvent.getName());
        userEntity.setSurname(userChangedNameEvent.getSurname());
        userEntityRepository.save(userEntity);
    }

    @EventHandler
    public void on(UserChangedOrganizationEvent userChangedOrganizationEvent) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(valueOf(userChangedOrganizationEvent.getUserId()));
        userEntity.setOrganization(userChangedOrganizationEvent.getOrganization().toString());
        userEntityRepository.save(userEntity);
    }

    @QueryHandler
    public List<User> handle(FindAllUsersQuery findAllUsersQuery) {
        return userEntityRepository.findAll()
                .stream()
                .map(userEntity ->
                        User.builder()
                                .id(userEntity.getId())
                                .name(userEntity.getName())
                                .organization(User.Organization.valueOf(userEntity.getOrganization()))
                                .surname(userEntity.getSurname())
                                .build())
                .collect(toList());
    }
}