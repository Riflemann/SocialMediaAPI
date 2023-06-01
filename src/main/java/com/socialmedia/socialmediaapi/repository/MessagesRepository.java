package com.socialmedia.socialmediaapi.repository;

import com.socialmedia.socialmediaapi.models.Messages;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MessagesRepository extends JpaRepository<Messages, Integer> {

    @Transactional
    List<Messages> getAllByUserToIsLike(int userTo);

    @Transactional
    List<Messages> getAllByUserFrom(int userTo);

    default void updateMessagesIsRead(List<Messages> messagesList, EntityManagerFactory entityManagerFactory) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            for (Messages message : messagesList) {
                Messages existingMessage = entityManager.find(Messages.class, message.getId());
                if (existingMessage != null) {
                    existingMessage.setRead(true);
                    entityManager.merge(existingMessage);
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

}
