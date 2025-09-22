package com.example.springboot.repositories.specs;

import com.example.springboot.models.entities.Task;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TaskSpecs {

    public static Specification<Task> doneLast7Days() {
        return ((root, query, cb) ->
                cb.and(
                        cb.equal(root.get("status"), "DONE"),
                        cb.greaterThanOrEqualTo(root.get("updatedAt"), LocalDateTime.now().minusDays(7))
                ));
    }

    public static Specification<Task> createdLast7Days() {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("createdAt"), LocalDateTime.now().minusDays(7));
    }

    public static Specification<Task> updatedLast7Days() {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("updatedAt"), LocalDateTime.now().minusDays(7));
    }

    public static Specification<Task> toDoLast7Days() {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("dueDate"), LocalDateTime.now().minusDays(7));
    }

    public static Specification<Task> hasAssignee(Long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("assignee").get("id"), userId);
    }

    public static Specification<Task> hasNoAssignee() {
        return (root, query, cb) ->
                cb.isNull(root.get("assignee"));
    }

    public static Specification<Task> hasProject(String projectKey) {
        return (root, query, cb) ->
                cb.equal(root.get("project").get("key"), projectKey);
    }
}
