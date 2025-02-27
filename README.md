1. Получить список проектов с задачами и комментариями, в которых задействованы пользователи с определенными ролями 
(например, только "Администратор" или "Менеджер")

Предположим, у вас есть поле role в сущности User, которое определяет роль пользователя (например, "Администратор", 
"Менеджер", "Разработчик"). Вам нужно найти все проекты, которые связаны с пользователями, имеющими роль "Менеджер" 
или "Администратор", и вывести задачи и комментарии для этих проектов.

    @Query("SELECT p FROM Project p " +
    "JOIN ProjectUser pu ON p.id = pu.project.id " +
    "JOIN User u ON pu.user.id = u.id " +
    "LEFT JOIN Task t ON p.id = t.project.id " +
    "LEFT JOIN Comment c ON t.id = c.task.id " +
    "WHERE u.role IN ('ADMIN', 'MANAGER')")
    List<Project> findProjectsForUsersWithRoles();

Здесь:

Мы используем фильтрацию по ролям пользователей с помощью IN ('ADMIN', 'MANAGER').
Присоединяем задачи и комментарии для этих проектов.
Пояснение:

Этот запрос возвращает проекты, связанные с пользователями с определенными ролями.
Вы можете модифицировать запрос, чтобы получить только те задачи и комментарии, которые связаны с пользователями, имеющими такие роли.
---------------------------------------------------------------------------------------------------------------------------------------------
2. Найти проекты, в которых задачи, имеющие статус "В процессе", связаны с пользователями, не имеющими активных задач более 7 дней.
   В этом запросе мы ищем проекты, где задачи в статусе "В процессе", но при этом пользователь не должен иметь активных задач, 
которые находятся в этом статусе более 7 дней.

Реализация:

    @Query("SELECT p FROM Project p " +
    "JOIN Task t ON p.id = t.project.id " +
    "JOIN User u ON t.user.id = u.id " +
    "WHERE t.status = 'IN_PROGRESS' " +
    "AND u.id NOT IN (" +
    "    SELECT t2.user.id FROM Task t2 WHERE t2.status = 'IN_PROGRESS' " +
    "    AND DATEDIFF(CURRENT_DATE, t2.dueDate) > 7 " +
    ")")
    List<Project> findProjectsWithUsersHavingInactiveTasks();

Здесь:

Основной запрос находит проекты с задачами в статусе "IN_PROGRESS".
Во вложенном запросе фильтруются пользователи, у которых есть задачи в этом статусе, которые не обновляются больше 7 дней.
Пояснение:

Этот запрос находит проекты с задачами, которые в процессе выполнения, но при этом исключает тех пользователей, у которых задачи застарели на более чем 7 дней.

---------------------------------------------------------------------------------------------------------------------------------------------
3. Составить отчет по задачам с количеством комментариев, статусом и временем создания задачи для каждого проекта и каждого пользователя
   Допустим, вы хотите составить отчет, который покажет, сколько комментариев в каждой задаче, статус задачи и время создания задачи для каждого 
пользователя в каждом проекте.

Реализация:

    @Query("SELECT p.id AS projectId, u.id AS userId, t.id AS taskId, COUNT(c.id) AS commentCount, t.status AS taskStatus, t.createdDate AS taskCreatedDate " +
    "FROM Project p " +
    "JOIN Task t ON p.id = t.project.id " +
    "JOIN User u ON t.user.id = u.id " +
    "LEFT JOIN Comment c ON t.id = c.task.id " +
    "GROUP BY p.id, u.id, t.id")
    List<Object[]> generateTaskReport();

Здесь:

Мы агрегируем данные по проектам, пользователям и задачам, подсчитываем количество комментариев для каждой задачи и показываем статус и дату создания задачи.
Пояснение:

Этот запрос генерирует отчет с деталями по каждому пользователю и задаче в проекте, что полезно для анализа активности и статуса задач.

---------------------------------------------------------------------------------------------------------------------------------------------
4. Использование Feign клиента для получения информации о пользователях, их задачах и комментариях из внешнего сервиса

Предположим, что у вас есть внешний сервис, который хранит информацию о задачах и комментариях пользователей (например, микросервис для комментариев). 
Вам нужно интегрировать этот сервис в ваше приложение, чтобы получить список комментариев для задач, в которых участвуют пользователи с определенной ролью.

Для этого вы можете использовать Feign клиент для взаимодействия с внешним сервисом.

Реализация Feign клиента:

    1. Создание Feign клиента для взаимодействия с внешним сервисом:

    @FeignClient(name = "comment-service", url = "http://comment-service/api")
    public interface CommentServiceClient {

    @GetMapping("/tasks/{taskId}/comments")
    List<CommentDto> getCommentsByTaskId(@PathVariable("taskId") Long taskId);
}

    2. Использование Feign клиента в сервисе для получения комментариев:

@Service
public class ProjectService {

    @Autowired
    private CommentServiceClient commentServiceClient;

    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectDto> getProjectsWithCommentsFromExternalService(Long userId) {
        List<Project> projects = projectRepository.findProjectsByUserId(userId);
        
        return projects.stream()
            .map(project -> {
                List<TaskDto> taskDtos = project.getTasks().stream()
                        .map(task -> {
                            // Используем Feign клиент для получения комментариев для каждой задачи
                            List<CommentDto> comments = commentServiceClient.getCommentsByTaskId(task.getId());
                            return new TaskDto(task.getId(), task.getTitle(), task.getDescription(), comments);
                        })
                        .collect(Collectors.toList());
                
                return new ProjectDto(project.getId(), project.getTitle(), project.getDescription(), taskDtos);
            })
            .collect(Collectors.toList());
        }
    }

Пояснение:

Feign клиент используется для получения комментариев для каждой задачи из внешнего сервиса (comment-service).
Мы используем @FeignClient для того, чтобы вызывать REST API внешнего сервиса, который возвращает список комментариев для задачи.
В методе getProjectsWithCommentsFromExternalService мы получаем проекты пользователя, а затем для каждой задачи используем Feign 
клиент для получения комментариев.

---------------------------------------------------------------------------------------------------------------------------------------------
5. Сложный запрос с фильтрацией по датам и состояниям задачи для активных пользователей

Предположим, вам нужно найти все проекты, в которых задачи с определенным статусом были завершены после заданной даты, но только для пользователей,
которые активно работали в этих проектах (например, пользователи, у которых есть задачи, завершенные в последние 30 дней).

    @Query("SELECT p FROM Project p " +
       "JOIN Task t ON p.id = t.project.id " +
       "JOIN User u ON t.user.id = u.id " +
       "WHERE t.status = 'COMPLETED' " +
       "AND t.dueDate > :startDate " +
       "AND u.id IN (" +
       "    SELECT DISTINCT t2.user.id " +
       "    FROM Task t2 " +
       "    WHERE t2.dueDate > :activePeriodStartDate" +
       ")")
    List<Project> findProjectsForActiveUsers(@Param("startDate") LocalDate startDate, @Param("activePeriodStartDate") LocalDate activePeriodStartDate);

Здесь:

Мы ищем проекты, в которых есть задачи со статусом "COMPLETED", которые были завершены после определенной даты.
Используем подзапрос для нахождения пользователей, у которых были завершенные задачи за последний период времени.






